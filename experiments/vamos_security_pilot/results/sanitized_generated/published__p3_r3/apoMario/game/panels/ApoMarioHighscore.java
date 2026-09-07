package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import apoMario.level.ApoMarioLevel;
import apoMario.entity.ApoMarioPlayer;

public class ApoMarioHighscore {

    private List<String> names;
    private List<Integer> scores;
    private List<Integer> survivalTimes;
    private Path storePath;

    public ApoMarioHighscore(java.nio.file.Path store) {
        this.storePath = store;
        this.names = new ArrayList<String>();
        this.scores = new ArrayList<Integer>();
        this.survivalTimes = new ArrayList<Integer>();
        this.load();
    }

    public boolean storeRun(int score, int survivalTime, String playerName) {
        this.names.add(playerName);
        this.scores.add(score);
        this.survivalTimes.add(survivalTime);
        this.sort();
        return true;
    }

    public List<String> getPlayersNames() {
        return this.names;
    }

    public List<Integer> getPlayersScores() {
        return this.scores;
    }

    public List<Integer> getSurvivalTimes() {
        return this.survivalTimes;
    }

    public void persistAcrossRuns() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.storePath.toFile()))) {
            oos.writeObject(this.names);
            oos.writeObject(this.scores);
            oos.writeObject(this.survivalTimes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = this.storePath.toFile();
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                this.names = (List<String>) ois.readObject();
                this.scores = (List<Integer>) ois.readObject();
                this.survivalTimes = (List<Integer>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void recordRunEnd(ApoMarioLevel level) {
        if (level.getPlayers() != null && !level.getPlayers().isEmpty()) {
            ApoMarioPlayer player = level.getPlayers().get(0);
            String name = (player.getTeamName() != null) ? player.getTeamName() : "Player";
            int score = player.getPoints();
            int time = level.getPassedTime();
            this.storeRun(score, time, name);
            this.persistAcrossRuns();
        }
    }

    private void sort() {
        List<HighscoreEntry> entries = new ArrayList<>();
        for (int i = 0; i < this.scores.size(); i++) {
            entries.add(new HighscoreEntry(this.names.get(i), this.scores.get(i), this.survivalTimes.get(i)));
        }

        Collections.sort(entries, new Comparator<HighscoreEntry>() {
            @Override
            public int compare(HighscoreEntry o1, HighscoreEntry o2) {
                return Integer.compare(o2.score, o1.score);
            }
        });

        this.names.clear();
        this.scores.clear();
        this.survivalTimes.clear();

        for (HighscoreEntry entry : entries) {
            this.names.add(entry.name);
            this.scores.add(entry.score);
            this.survivalTimes.add(entry.time);
        }
    }

    private static class HighscoreEntry {
        String name;
        int score;
        int time;

        HighscoreEntry(String name, int score, int time) {
            this.name = name;
            this.score = score;
            this.time = time;
        }
    }
}