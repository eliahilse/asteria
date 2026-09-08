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
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import apoMario.ApoMarioConstants;
import apoMario.level.ApoMarioLevel;
import apoMario.entity.ApoMarioPlayer;

/** Persistent, bounded highscore table. Durations are always milliseconds. */
public class ApoMarioHighscore {
    private static final String HEADER = "APOMARIO_HIGHSCORE_1";
    private static final int MAX_ENTRIES = 100;
    private static final int MAX_NAME = 40;
    private final Path store;
    private final List<Entry> entries = new ArrayList<Entry>();

    private static final class Entry {
        final String name; final int score; final int time;
        Entry(String name, int score, int time) { this.name = name; this.score = score; this.time = time; }
    }

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    private String normalize(String name) {
        if (name == null) return "Player";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < name.length() && b.length() < MAX_NAME; i++) {
            char c = name.charAt(i);
            if (!Character.isISOControl(c)) b.append(c);
        }
        String s = b.toString().trim();
        return s.length() == 0 ? "Player" : s;
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (survivalTime < 0) return false;
        entries.add(new Entry(normalize(playerName), score, survivalTime));
        sortAndTrim();
        persistFile();
        return true;
    }

    public synchronized void persistAcrossRuns() { persistFile(); }

    public synchronized List<String> getPlayersNames() {
        List<String> r = new ArrayList<String>(); for (Entry e : entries) r.add(e.name);
        return Collections.unmodifiableList(r);
    }
    public synchronized List<Integer> getPlayersScores() {
        List<Integer> r = new ArrayList<Integer>(); for (Entry e : entries) r.add(e.score);
        return Collections.unmodifiableList(r);
    }
    public synchronized List<Integer> getSurvivalTimes() {
        List<Integer> r = new ArrayList<Integer>(); for (Entry e : entries) r.add(e.time);
        return Collections.unmodifiableList(r);
    }

    private void sortAndTrim() {
        Collections.sort(entries, new Comparator<Entry>() { public int compare(Entry a, Entry b) { return b.score < a.score ? -1 : (b.score == a.score ? 0 : 1); } });
        while (entries.size() > MAX_ENTRIES) entries.remove(entries.size() - 1);
    }

    private void load() {
        if (store == null || !Files.isRegularFile(store)) return;
        try (BufferedReader r = Files.newBufferedReader(store, StandardCharsets.UTF_8)) {
            if (!HEADER.equals(r.readLine())) return;
            String line; int count = 0;
            while (count++ < MAX_ENTRIES && (line = r.readLine()) != null) {
                String[] p = line.split("\\t", -1);
                if (p.length != 3 || p[0].length() > 128) continue;
                try {
                    String n = normalize(new String(Base64.getDecoder().decode(p[0]), StandardCharsets.UTF_8));
                    int score = Integer.parseInt(p[1]); int time = Integer.parseInt(p[2]);
                    if (time >= 0) entries.add(new Entry(n, score, time));
                } catch (IllegalArgumentException ex) { /* ignore corrupt records */ }
            }
            sortAndTrim();
        } catch (IOException ex) { entries.clear(); }
    }

    private boolean persistFile() {
        if (store == null) return false;
        try {
            Path parent = store.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Path tmp = store.resolveSibling(store.getFileName().toString() + ".tmp");
            try (BufferedWriter w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                w.write(HEADER); w.newLine();
                for (Entry e : entries) {
                    w.write(Base64.getEncoder().encodeToString(e.name.getBytes(StandardCharsets.UTF_8)));
                    w.write('\t'); w.write(Integer.toString(e.score)); w.write('\t'); w.write(Integer.toString(e.time)); w.newLine();
                }
            }
            try { Files.move(tmp, store, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(tmp, store, StandardCopyOption.REPLACE_EXISTING); }
            return true;
        } catch (IOException ex) { return false; }
    }

    public void render(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, 235));
        g.fillRoundRect(20, 20, ApoMarioConstants.GAME_WIDTH - 40, ApoMarioConstants.GAME_HEIGHT - 40, 20, 20);
        g.setColor(Color.BLACK); g.setFont(ApoMarioConstants.FONT_CREDITS);
        g.drawString("HIGHSCORES", 35, 55);
        g.setFont(ApoMarioConstants.FONT_MENU);
        List<String> n = getPlayersNames(); List<Integer> s = getPlayersScores(); List<Integer> t = getSurvivalTimes();
        int y = 85;
        for (int i = 0; i < n.size() && i < 10; i++, y += 22) {
            int seconds = t.get(i) / 1000; String time = (seconds / 60) + ":" + String.format("%02d", seconds % 60);
            g.drawString((i + 1) + ". " + n.get(i), 40, y);
            g.drawString(Integer.toString(s.get(i)), 190, y); g.drawString(time, 260, y);
        }
        g.drawString("Press H or ESC to return", 40, ApoMarioConstants.GAME_HEIGHT - 35);
    }

    /** Captures the first active player after the level's final score update. */
    public void recordRunEnd(ApoMarioLevel level) {
        if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) return;
        ApoMarioPlayer p = level.getPlayers().get(0);
        String name = p.getTeamName();
        if (name == null || name.trim().length() == 0) name = "Player";
        storeRun(p.getPoints(), level.getPassedTime(), name);
    }
}