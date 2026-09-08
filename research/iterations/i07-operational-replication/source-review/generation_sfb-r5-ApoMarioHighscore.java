package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import apoMario.entity.ApoMarioPlayer;
import apoMario.level.ApoMarioLevel;

public class ApoMarioHighscore {
    private static final int MAX_ENTRIES = 100;
    private static final int MAX_NAME_LENGTH = 32;
    private final Path store;
    private final List<Run> runs = new ArrayList<Run>();

    private static final class Run {
        private final String name;
        private final int score;
        private final int time;
        private Run(String name, int score, int time) {
            this.name = name;
            this.score = score;
            this.time = time;
        }
    }

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (survivalTime < 0 || playerName == null) {
            return false;
        }
        String name = normalize(playerName);
        if (name.length() == 0) {
            name = "Player";
        }
        runs.add(new Run(name, score, survivalTime));
        sortAndTrim();
        persistAcrossRuns();
        return true;
    }

    public synchronized void recordRunEnd(ApoMarioLevel level) {
        if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) {
            return;
        }
        ApoMarioPlayer player = level.getPlayers().get(0);
        String name = player.getTeamName();
        if (name == null || name.trim().length() == 0) {
            name = "Player";
        }
        storeRun(player.getPoints(), level.getPassedTime(), name);
    }

    public synchronized List<String> getPlayersNames() {
        List<String> result = new ArrayList<String>();
        for (Run run : runs) result.add(run.name);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getPlayersScores() {
        List<Integer> result = new ArrayList<Integer>();
        for (Run run : runs) result.add(run.score);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getSurvivalTimes() {
        List<Integer> result = new ArrayList<Integer>();
        for (Run run : runs) result.add(run.time);
        return Collections.unmodifiableList(result);
    }

    public synchronized void persistAcrossRuns() {
        if (store == null) return;
        Path temporary = store.resolveSibling(store.getFileName().toString() + ".tmp");
        try {
            Path parent = store.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            BufferedWriter writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8);
            try {
                for (Run run : runs) {
                    writer.write(Integer.toString(run.score));
                    writer.write('\t');
                    writer.write(Integer.toString(run.time));
                    writer.write('\t');
                    writer.write(run.name.replace('\t', ' '));
                    writer.newLine();
                }
            } finally {
                writer.close();
            }
            Files.move(temporary, store, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            try { Files.deleteIfExists(temporary); } catch (IOException ignored) { }
        }
    }

    private void load() {
        if (store == null || !Files.isRegularFile(store)) return;
        try {
            BufferedReader reader = Files.newBufferedReader(store, StandardCharsets.UTF_8);
            try {
                String line;
                int count = 0;
                while (count++ < MAX_ENTRIES && (line = reader.readLine()) != null) {
                    String[] fields = line.split("\\t", 3);
                    if (fields.length != 3) continue;
                    try {
                        int score = Integer.parseInt(fields[0]);
                        int time = Integer.parseInt(fields[1]);
                        String name = normalize(fields[2]);
                        if (time >= 0 && name.length() > 0) runs.add(new Run(name, score, time));
                    } catch (NumberFormatException ignored) { }
                }
            } finally {
                reader.close();
            }
            sortAndTrim();
        } catch (IOException ignored) { }
    }

    private String normalize(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length() && result.length() < MAX_NAME_LENGTH; i++) {
            char c = value.charAt(i);
            if (c >= 32 && c != 127) result.append(c);
        }
        return result.toString().trim();
    }

    private void sortAndTrim() {
        Collections.sort(runs, new Comparator<Run>() {
            public int compare(Run first, Run second) {
                return second.score < first.score ? -1 : (second.score == first.score ? 0 : 1);
            }
        });
        while (runs.size() > MAX_ENTRIES) runs.remove(runs.size() - 1);
    }

    public synchronized void render(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setColor(new Color(255, 255, 255, 235));
        graphics.fillRoundRect(x, y, width, height, 18, 18);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(x, y, width, height, 18, 18);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        graphics.drawString("HIGHSCORES", x + 20, y + 28);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        int row = y + 52;
        for (int i = 0; i < runs.size() && i < 10; i++) {
            Run run = runs.get(i);
            int seconds = run.time / 1000;
            String formatted = (seconds / 60) + ":" + (seconds % 60 < 10 ? "0" : "") + (seconds % 60);
            graphics.drawString((i + 1) + ". " + run.name, x + 20, row);
            graphics.drawString(Integer.toString(run.score), x + width - 130, row);
            graphics.drawString(formatted, x + width - 65, row);
            row += 21;
        }
    }
}