package research.context;

import com.sun.source.tree.*;
import com.sun.source.util.*;
import javax.tools.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/** Syntax-backed observations; does not claim resolved types or proven taint. */
public final class AstFacts {
    static String encode(Object value) {return Base64.getEncoder().encodeToString(String.valueOf(value).getBytes(StandardCharsets.UTF_8));}
    static void emit(String kind, String file, String method, long line, String text, String origins) {
        System.out.println(Stream.of(kind,file,method,String.valueOf(line),text,origins).map(AstFacts::encode).collect(Collectors.joining("\t")));
    }
    public static void main(String[] args) throws Exception {
        Path root=Paths.get(args[0]);
        JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics=new DiagnosticCollector<>();
        try(StandardJavaFileManager manager=compiler.getStandardFileManager(diagnostics,null,StandardCharsets.UTF_8)) {
            List<Path> paths;
            try(Stream<Path> stream=Files.walk(root)) {paths=stream.filter(p->p.toString().endsWith(".java")).sorted().collect(Collectors.toList());}
            JavacTask task=(JavacTask)compiler.getTask(null,manager,diagnostics,Arrays.asList("-proc:none"),null,manager.getJavaFileObjectsFromPaths(paths));
            List<CompilationUnitTree> units=new ArrayList<>(); task.parse().forEach(units::add);
            Set<String> failed=new HashSet<>();
            for(Diagnostic<? extends JavaFileObject> d:diagnostics.getDiagnostics()) if(d.getKind()==Diagnostic.Kind.ERROR && d.getSource()!=null) {
                String file=root.relativize(Paths.get(d.getSource().toUri())).toString(); failed.add(file);
                emit("parse_error",file,"",d.getLineNumber(),d.getMessage(Locale.ROOT),"");
            }
            SourcePositions positions=Trees.instance(task).getSourcePositions();
            for(CompilationUnitTree unit:units) {
                String file=root.relativize(Paths.get(unit.getSourceFile().toUri())).toString();
                if(failed.contains(file)) continue;
                emit("parsed",file,"",1,"","");
                new TreePathScanner<Void,Void>() {
                    String method="<class>";
                    Map<String,Set<String>> flows=new HashMap<>();
                    long line(Tree tree) {return unit.getLineMap().getLineNumber(positions.getStartPosition(unit,tree));}
                    boolean input(MethodInvocationTree call) {return call.getMethodSelect().toString().matches(".*\\.(readLine|readInt|readLong|readShort|readUTF|nextLine|readObject|readAllLines|getParameter)\\b");}
                    Set<String> origins(Tree tree) {
                        Set<String> found=new TreeSet<>();
                        if(tree!=null) new TreeScanner<Void,Void>() {
                            public Void visitIdentifier(IdentifierTree t,Void v) {found.addAll(flows.getOrDefault(t.getName().toString(),Collections.emptySet()));return super.visitIdentifier(t,v);}
                            public Void visitMethodInvocation(MethodInvocationTree t,Void v) {if(input(t)) found.add(file+":"+line(t)+" "+t.getMethodSelect());return super.visitMethodInvocation(t,v);}
                        }.scan(tree,null);
                        return found;
                    }
                    void record(String kind,Tree node,String text,Set<String> sources) {emit(kind,file,method,line(node),text,String.join(" | ",sources));}
                    public Void visitMethod(MethodTree t,Void v) {
                        String old=method; Map<String,Set<String>> prior=flows; method=t.getName().toString(); flows=new HashMap<>();
                        Void result=super.visitMethod(t,v);method=old;flows=prior;return result;
                    }
                    public Void visitVariable(VariableTree t,Void v) {flows.put(t.getName().toString(),origins(t.getInitializer()));return super.visitVariable(t,v);}
                    public Void visitAssignment(AssignmentTree t,Void v) {flows.put(t.getVariable().toString(),origins(t.getExpression()));return super.visitAssignment(t,v);}
                    public Void visitImport(ImportTree t,Void v) {record("import",t,t.getQualifiedIdentifier().toString(),Collections.emptySet());return super.visitImport(t,v);}
                    public Void visitLiteral(LiteralTree t,Void v) {if(t.getValue() instanceof String && t.getValue().toString().matches("(?s)https?://.*")) record("endpoint",t,t.getValue().toString(),Collections.emptySet());return super.visitLiteral(t,v);}
                    public Void visitNewClass(NewClassTree t,Void v) {record("construct",t,t.getIdentifier()+"("+t.getArguments().toString()+")",Collections.emptySet());return super.visitNewClass(t,v);}
                    public Void visitMethodInvocation(MethodInvocationTree t,Void v) {
                        record("call",t,t.toString(),Collections.emptySet());
                        if(t.getMethodSelect().toString().matches(".*\\.(add|addAll|put|write|writeBytes|loadClass|defineClass)")) {
                            Set<String> source=new TreeSet<>();for(ExpressionTree arg:t.getArguments()) source.addAll(origins(arg));
                            if(!source.isEmpty()) record("flow_candidate",t,t.toString(),source);
                        }
                        return super.visitMethodInvocation(t,v);
                    }
                    public Void visitNewArray(NewArrayTree t,Void v) {
                        Set<String> source=new TreeSet<>(); for(ExpressionTree dimension:t.getDimensions())source.addAll(origins(dimension));
                        if(!source.isEmpty())record("flow_candidate",t,t.toString(),source);
                        return super.visitNewArray(t,v);
                    }
                }.scan(unit,null);
            }
        }
    }
}
