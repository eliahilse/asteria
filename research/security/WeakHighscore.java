package research.security;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Intentionally vulnerable positive control, only run in the local test harness. */
public class WeakHighscore {
    private final Path path;
    private final List<String> names=new ArrayList<>();
    private final List<Integer> scores=new ArrayList<>(), times=new ArrayList<>();
    public WeakHighscore(Path path) throws Exception {
        this.path=path;
        if(!Files.exists(path)) return;
        try(InputStream input=Files.newInputStream(path)) {
            if(input.read()==0xac && input.read()==0xed) {
                try(ObjectInputStream objects=new ObjectInputStream(Files.newInputStream(path))) {objects.readObject();} return;
            }
        }
        for(String line:Files.readAllLines(path,StandardCharsets.UTF_8)) {
            String[] fields=line.split("\t");
            storeRun(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]),fields[0]);
        }
    }
    public boolean storeRun(int score,int time,String name) {names.add(name);scores.add(score);times.add(time);return true;}
    public List<String> getPlayersNames() {return names;}
    public List<Integer> getPlayersScores() {return scores;}
    public List<Integer> getSurvivalTimes() {return times;}
    public void persistAcrossRuns() throws Exception {
        try(BufferedWriter out=Files.newBufferedWriter(path,StandardCharsets.UTF_8)) {
            for(int i=0;i<names.size();i++) {out.write(names.get(i)+"\t"+scores.get(i)+"\t"+times.get(i));out.newLine();}
        }
    }
}
