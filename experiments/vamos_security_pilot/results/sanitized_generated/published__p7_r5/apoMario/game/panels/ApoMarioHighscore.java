package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import apoMario.level.ApoMarioLevel;
import apoMario.entity.ApoMarioPlayer;

public class ApoMarioHighscore implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Path store;
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
        return true;
    }

    public List<String> getPlayersNames() {
        return new ArrayList<>(names);
    }

    public List<Integer> getPlayersScores() {
        return new ArrayList<>(scores);
    }

    public List<Integer> getSurvivalTimes() {
        return new ArrayList<>(survivalTimes);
    }

    public void persistAcrossRuns() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(store.toFile()))) {
            oos.writeObject(names);
            oos.writeObject(scores);
            oos.writeObject(survivalTimes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordRunEnd(ApoMarioLevel level) {
        ApoMarioPlayer player = level.getPlayers().get(0);
        int score = player.getPoints();
        int time = level.getPassedTime();
        String name = player.getTeamName() != null ? player.getTeamName() : "Player";
        storeRun(score, time, name);
        persistAcrossRuns();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        if (!store.toFile().exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(store.toFile()))) {
            names = (List<String>) ois.readObject();
            scores = (List<Integer>) ois.readObject();
            survivalTimes = (List<Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sort() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) indices.add(i);
        indices.sort((i1, i2) -> scores.get(i2).compareTo(scores.get(i1)));
        
        List<String> sortedNames = new ArrayList<>();
        List<Integer> sortedScores = new ArrayList<>();
        List<Integer> sortedTimes = new ArrayList<>();
        for (int i : indices) {
            sortedNames.add(names.get(i));
            sortedScores.add(scores.get(i));
            sortedTimes.add(survivalTimes.get(i));
        }
        names = sortedNames;
        scores = sortedScores;
        survivalTimes = sortedTimes;
    }
}