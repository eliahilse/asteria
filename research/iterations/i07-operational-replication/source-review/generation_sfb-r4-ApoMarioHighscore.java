package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

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

import apoMario.level.ApoMarioLevel;
import apoMario.entity.ApoMarioPlayer;

/** Persistent, tuple-based highscore table. Durations are milliseconds. */
public class ApoMarioHighscore {
    private static final int MAX_ENTRIES = 100;
    private static final int MAX_NAME = 40;
    private final Path store;
    private final List<Entry> entries = new ArrayList<Entry>();

    private static final class Entry {
        final String name;
        final int score;
        final int time;
        Entry(String name, int score, int time) { this.name = name; this.score = score; this.time = time; }
    }

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (survivalTime < 0 || playerName == null) return false;
        String name = normalize(playerName);
        if (name.length() == 0) return false;
        entries.add(new Entry(name, score, survivalTime));
        sortAndLimit();
        return persistInternal();
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

    public synchronized void persistAcrossRuns() { persistInternal(); }

    public synchronized void recordRunEnd(ApoMarioLevel level) {
        if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) return;
        ApoMarioPlayer selected = null;
        for (ApoMarioPlayer p : level.getPlayers()) {
            if (p != null && p.isBVisible() && p.getAi() == null) { selected = p; break; }
        }
        if (selected == null) selected = level.getPlayers().get(0);
        if (selected == null) return;
        String name = selected.getTeamName();
        if (name == null || name.trim().length() == 0) name = "Player";
        storeRun(selected.getPoints(), Math.max(0, level.getPassedTime()), name);
    }

    private String normalize(String value) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < value.length() && b.length() < MAX_NAME; i++) {
            char c = value.charAt(i);
            if (c >= 32 && c != '\t' && c != ':' && c != '\n' && c != '\r') b.append(c);
        }
        return b.toString().trim();
    }

    private void sortAndLimit() {
        Collections.sort(entries, new Comparator<Entry>() {
            public int compare(Entry a, Entry b) { return b.score == a.score ? 0 : (b.score < a.score ? -1 : 1); }
        });
        while (entries.size() > MAX_ENTRIES) entries.remove(entries.size() - 1);
    }

    private void load() {
        if (store == null || !Files.isRegularFile(store)) return;
        try (BufferedReader r = Files.newBufferedReader(store, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null && entries.size() < MAX_ENTRIES) {
                String[] p = line.split(":", -1);
                if (p.length != 3 || !"HS1".equals(p[0])) continue;
                try {
                    int score = Integer.parseInt(p[1]);
                    int time = Integer.parseInt(p[2].substring(0, p[2].indexOf('|')));
                    String name = normalize(p[2].substring(p[2].indexOf('|') + 1));
                    if (name.length() > 0 && time >= 0) entries.add(new Entry(name, score, time));
                } catch (RuntimeException ignored) { }
            }
            sortAndLimit();
        } catch (IOException ignored) { }
    }

    private boolean persistInternal() {
        if (store == null) return false;
        try {
            Path parent = store.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Path temp = store.resolveSibling(store.getFileName().toString() + ".tmp");
            try (BufferedWriter w = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                for (Entry e : entries) w.write("HS1:" + e.score + ":" + e.time + "|" + e.name + "\n");
            }
            try {
                Files.move(temp, store, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temp, store, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException ex) { return false; }
    }
}