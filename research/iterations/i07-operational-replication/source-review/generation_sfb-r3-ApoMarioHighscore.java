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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import apoMario.ApoMarioConstants;
import apoMario.entity.ApoMarioPlayer;
import apoMario.level.ApoMarioLevel;

/** Persistent, score-sorted highscore table. Durations are milliseconds. */
public class ApoMarioHighscore {
    private static final int MAX_ENTRIES = 100;
    private static final int MAX_NAME = 40;
    private final Path store;
    private final List<Entry> entries = new ArrayList<Entry>();
    private final Map<ApoMarioLevel, String> recordedLevels = new IdentityHashMap<ApoMarioLevel, String>();

    private static final class Entry {
        String name; int score; int time;
        Entry(String name, int score, int time) { this.name = name; this.score = score; this.time = time; }
    }

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (survivalTime < 0 || playerName == null) return false;
        String name = normalize(playerName);
        if (name.length() == 0) name = "Player";
        entries.add(new Entry(name, score, survivalTime));
        sort();
        while (entries.size() > MAX_ENTRIES) entries.remove(entries.size() - 1);
        persistAcrossRuns();
        return true;
    }

    public synchronized void recordRunEnd(ApoMarioLevel level) {
        if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) return;
        ApoMarioPlayer player = level.getPlayers().get(0);
        String name = player.getTeamName();
        if (name == null) name = "";
        String fingerprint = player.getPoints() + "|" + level.getPassedTime() + "|" + name;
        if (fingerprint.equals(recordedLevels.get(level))) return;
        if (storeRun(player.getPoints(), level.getPassedTime(), name)) {
            recordedLevels.put(level, fingerprint);
        }
    }

    public synchronized List<String> getPlayersNames() {
        List<String> result = new ArrayList<String>();
        for (Entry e : entries) result.add(e.name);
        return Collections.unmodifiableList(result);
    }
    public synchronized List<Integer> getPlayersScores() {
        List<Integer> result = new ArrayList<Integer>();
        for (Entry e : entries) result.add(e.score);
        return Collections.unmodifiableList(result);
    }
    public synchronized List<Integer> getSurvivalTimes() {
        List<Integer> result = new ArrayList<Integer>();
        for (Entry e : entries) result.add(e.time);
        return Collections.unmodifiableList(result);
    }

    public synchronized void persistAcrossRuns() {
        if (store == null) return;
        try {
            Path parent = store.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Path temp = store.resolveSibling(store.getFileName().toString() + ".tmp");
            BufferedWriter out = Files.newBufferedWriter(temp, StandardCharsets.UTF_8);
            try {
                out.write("APO_MARIO_HIGHSCORE_1"); out.newLine();
                for (Entry e : entries) {
                    out.write(Base64.getEncoder().encodeToString(e.name.getBytes(StandardCharsets.UTF_8)));
                    out.write('\t'); out.write(Integer.toString(e.score)); out.write('\t'); out.write(Integer.toString(e.time)); out.newLine();
                }
            } finally { out.close(); }
            try { Files.move(temp, store, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(temp, store, StandardCopyOption.REPLACE_EXISTING); }
        } catch (IOException ex) { /* A failed save must not stop the game. */ }
    }

    public synchronized void render(Graphics2D g) {
        int w = ApoMarioConstants.GAME_WIDTH, h = ApoMarioConstants.GAME_HEIGHT;
        g.setColor(new Color(255, 255, 255, 235)); g.fillRoundRect(18, 12, w - 36, h - 24, 16, 16);
        g.setColor(Color.BLACK); g.drawRoundRect(18, 12, w - 36, h - 24, 16, 16);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        g.drawString("HIGHSCORES", 28, 38);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        g.drawString("Name", 30, 60); g.drawString("Points", w - 125, 60); g.drawString("Time", w - 65, 60);
        for (int i = 0; i < entries.size() && i < 10; i++) {
            Entry e = entries.get(i); int y = 82 + i * 16;
            g.drawString((i + 1) + ". " + e.name, 30, y);
            g.drawString(Integer.toString(e.score), w - 125, y);
            g.drawString(formatTime(e.time), w - 65, y);
        }
        g.drawString("H / ESC", 30, h - 24);
    }

    private void load() {
        if (store == null || !Files.isRegularFile(store)) return;
        try {
            BufferedReader in = Files.newBufferedReader(store, StandardCharsets.UTF_8);
            try {
                if (!"APO_MARIO_HIGHSCORE_1".equals(in.readLine())) return;
                String line; int count = 0;
                while (count++ < MAX_ENTRIES && (line = in.readLine()) != null) {
                    String[] p = line.split("\\t", -1); if (p.length != 3) continue;
                    try {
                        String name = normalize(new String(Base64.getDecoder().decode(p[0]), StandardCharsets.UTF_8));
                        int score = Integer.parseInt(p[1]), time = Integer.parseInt(p[2]);
                        if (name.length() > 0 && time >= 0) entries.add(new Entry(name, score, time));
                    } catch (IllegalArgumentException ex) { /* skip malformed records */ }
                }
            } finally { in.close(); }
            sort();
        } catch (IOException ex) { /* start with an empty table */ }
    }
    private void sort() { Collections.sort(entries, new Comparator<Entry>() { public int compare(Entry a, Entry b) { return b.score < a.score ? -1 : (b.score == a.score ? 0 : 1); } }); }
    private static String normalize(String value) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < value.length() && b.length() < MAX_NAME; i++) if (!Character.isISOControl(value.charAt(i))) b.append(value.charAt(i));
        return b.toString().trim();
    }
    private static String formatTime(int millis) {
        long seconds = millis / 1000L; return (seconds / 60L) + ":" + String.format("%02d", Long.valueOf(seconds % 60L));
    }
}