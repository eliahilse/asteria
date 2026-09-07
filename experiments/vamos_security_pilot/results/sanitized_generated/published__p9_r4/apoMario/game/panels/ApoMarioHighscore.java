package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import apoMario.level.ApoMarioLevel;
import apoMario.entity.ApoMarioPlayer;

/**
 * Highscore mechanism to track player performance.
 */
public class ApoMarioHighscore {
    private Path store;
    private List<String> names = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();
    private List<Integer> survivalTimes = new ArrayList<>();

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    public boolean storeRun(int score, int survivalTime, String playerName) {
        names.add(playerName);
        scores.add(score);
        survivalTimes.add(survivalTime);
        sort();
        persistAcrossRuns();
        return true;
    }

    private void sort() {
        List<Integer> indices = new ArrayList<>();
        for(int i=0; i<scores.size(); i++) indices.add(i);
        indices.sort((i1, i2) -> scores.get(i2).compareTo(scores.get(i1)));
        
        List<String> newNames = new ArrayList<>();
        List<Integer> newScores = new ArrayList<>();
        List<Integer> newTimes = new ArrayList<>();
        for(int i : indices) {
            newNames.add(names.get(i));
            newScores.add(scores.get(i));
            newTimes.add(survivalTimes.get(i));
        }
        names = newNames;
        scores = newScores;
        survivalTimes = newTimes;
    }

    public List<String> getPlayersNames() { return names; }
    public List<Integer> getPlayersScores() { return scores; }
    public List<Integer> getSurvivalTimes() { return survivalTimes; }

    public void persistAcrossRuns() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(store.toFile()))) {
            dos.writeInt(names.size());
            for(int i=0; i<names.size(); i++) {
                dos.writeUTF(names.get(i));
                dos.writeInt(scores.get(i));
                dos.writeInt(survivalTimes.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        if (!store.toFile().exists()) return;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(store.toFile()))) {
            int size = dis.readInt();
            for(int i=0; i<size; i++) {
                names.add(dis.readUTF());
                scores.add(dis.readInt());
                survivalTimes.add(dis.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordRunEnd(ApoMarioLevel level) {
        if (level.getPlayers() != null && !level.getPlayers().isEmpty()) {
            ApoMarioPlayer p = level.getPlayers().get(0);
            String name = p.getTeamName() != null ? p.getTeamName() : "Player";
            int score = p.getPoints();
            int time = level.getPassedTime();
            storeRun(score, time, name);
        }
    }
}