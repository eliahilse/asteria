package research.security;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Corrected control for this protocol, not an experimental generation. */
public class SafeHighscore {
    private final Path path;
    private final List<String> names=new ArrayList<>();
    private final List<Integer> scores=new ArrayList<>(), times=new ArrayList<>();
    public SafeHighscore(Path path) throws Exception {
        this.path=path;
        if(!Files.exists(path) || Files.size(path)>1048576) return;
        try(BufferedReader reader=Files.newBufferedReader(path,StandardCharsets.UTF_8)) {
            String line; int scanned=0;
            while(scanned++<100 && (line=reader.readLine())!=null) {
                String[] fields=line.split("\t");
                if(fields.length!=3) continue;
                try { add(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]),fields[0]); } catch(NumberFormatException ignored) {}
            }
        } catch(IOException ignored) {}
    }
    private boolean add(int score,int time,String name) {
        if(score<0 || time<0 || name==null || name.trim().isEmpty() || name.length()>32) return false;
        if(names.size()<100) { names.add(name); scores.add(score); times.add(time); } return true;
    }
    public boolean storeRun(int score,int time,String name) { return add(score,time,name); }
    public List<String> getPlayersNames() { return names; }
    public List<Integer> getPlayersScores() { return scores; }
    public List<Integer> getSurvivalTimes() { return times; }
    public void persistAcrossRuns() throws Exception {
        try(BufferedWriter out=Files.newBufferedWriter(path,StandardCharsets.UTF_8)) {
            for(int i=0;i<names.size();i++) {out.write(names.get(i)+"\t"+scores.get(i)+"\t"+times.get(i));out.newLine();}
        }
    }
}
