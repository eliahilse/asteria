package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
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
    private static final int MAX_NAME = 40;
    private final Path store;
    private final List<Entry> entries = new ArrayList<Entry>();

    private static final class Entry {
        final String name;
        final int score;
        final int survivalTime;
        Entry(String name, int score, int survivalTime) {
            this.name = name;
            this.score = score;
            this.survivalTime = survivalTime;
        }
    }

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    private String normalizeName(String value) {
        if (value == null) return "Player";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length() && result.length() < MAX_NAME; i++) {
            char c = value.charAt(i);
            if (c >= 32 && c != '|' && c != '\\') result.append(c);
        }
        String name = result.toString().trim();
        return name.length() == 0 ? "Player" : name;
    }

    private void sortEntries() {
        Collections.sort(entries, new Comparator<Entry>() {
            public int compare(Entry first, Entry second) {
                return first.score == second.score ? 0 : (first.score > second.score ? -1 : 1);
            }
        });
        while (entries.size() > MAX_ENTRIES) entries.remove(entries.size() - 1);
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (survivalTime < 0) return false;
        entries.add(new Entry(normalizeName(playerName), score, survivalTime));
        sortEntries();
        persistAcrossRuns();
        return true;
    }

    public synchronized List<String> getPlayersNames() {
        List<String> result = new ArrayList<String>();
        for (Entry entry : entries) result.add(entry.name);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getPlayersScores() {
        List<Integer> result = new ArrayList<Integer>();
        for (Entry entry : entries) result.add(entry.score);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getSurvivalTimes() {
        List<Integer> result = new ArrayList<Integer>();
        for (Entry entry : entries) result.add(entry.survivalTime);
        return Collections.unmodifiableList(result);
    }

    public void recordRunEnd(ApoMarioLevel level) {
        if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) return;
        ApoMarioPlayer player = level.getPlayers().get(0);
        storeRun(player.getPoints(), Math.max(0, level.getPassedTime()), player.getTeamName());
    }

    public synchronized void persistAcrossRuns() {
        if (store == null) return;
        Path temporary = null;
        try {
            Path absolute = store.toAbsolutePath();
            if (absolute.getParent() != null) Files.createDirectories(absolute.getParent());
            temporary = absolute.resolveSibling(absolute.getFileName().toString() + ".tmp");
            BufferedWriter writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8);
            try {
                writer.write("APO_MARIO_HIGHSCORE_1");
                writer.newLine();
                for (Entry entry : entries) {
                    writer.write(entry.name + "|" + entry.score + "|" + entry.survivalTime);
                    writer.newLine();
                }
            } finally {
                writer.close();
            }
            try {
                Files.move(temporary, absolute, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, absolute, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ignored) {
            if (temporary != null) {
                try { Files.deleteIfExists(temporary); } catch (IOException ignoredAgain) { }
            }
        }
    }

    private synchronized void load() {
        if (store == null || !Files.isRegularFile(store)) return;
        try {
            BufferedReader reader = Files.newBufferedReader(store, StandardCharsets.UTF_8);
            try {
                if (!"APO_MARIO_HIGHSCORE_1".equals(reader.readLine())) return;
                String line;
                while (entries.size() < MAX_ENTRIES && (line = reader.readLine()) != null) {
                    String[] fields = line.split("\\|", -1);
                    if (fields.length != 3 || fields[0].length() == 0 || fields[0].length() > MAX_NAME) continue;
                    try {
                        int score = Integer.parseInt(fields[1]);
                        int time = Integer.parseInt(fields[2]);
                        if (time >= 0) entries.add(new Entry(normalizeName(fields[0]), score, time));
                    } catch (NumberFormatException ignored) { }
                }
            } finally {
                reader.close();
            }
            sortEntries();
        } catch (IOException ignored) { }
    }

    public synchronized void render(Graphics2D graphics, int x, int y, int width, int height) {
        Color oldColor = graphics.getColor();
        Font oldFont = graphics.getFont();
        graphics.setColor(new Color(255, 255, 255, 230));
        graphics.fillRoundRect(x, y, width, height, 18, 18);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(x, y, width, height, 18, 18);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        graphics.drawString("HIGHSCORES", x + 15, y + 25);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        for (int i = 0; i < entries.size() && i < 10; i++) {
            Entry entry = entries.get(i);
            int seconds = entry.survivalTime / 1000;
            String time = (seconds / 60) + ":" + String.format("%02d", seconds % 60);
            int row = y + 48 + i * 18;
            graphics.drawString((i + 1) + ". " + entry.name, x + 15, row);
            graphics.drawString(String.valueOf(entry.score), x + width - 105, row);
            graphics.drawString(time, x + width - 55, row);
        }
        graphics.setFont(oldFont);
        graphics.setColor(oldColor);
    }
}