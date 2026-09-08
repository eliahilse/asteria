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
import java.util.Base64;

import apoMario.entity.ApoMarioPlayer;
import apoMario.level.ApoMarioLevel;

/** Persistent, bounded highscore table. Durations are milliseconds. */
public class ApoMarioHighscore {
    private static final int MAX_ENTRIES = 100;
    private static final int MAX_NAME = 48;
    private final Path store;
    private final List<Run> runs = new ArrayList<Run>();

    private static final class Run {
        final String name; final int score; final int time;
        Run(String name, int score, int time) { this.name = name; this.score = score; this.time = time; }
    }

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (survivalTime < 0 || playerName == null) return false;
        String name = normalize(playerName);
        if (name.length() == 0) return false;
        runs.add(new Run(name, score, survivalTime));
        sortAndTrim();
        persistAcrossRuns();
        return true;
    }

    /** Records the first human player after the level has completed its scoring. */
    public synchronized void recordRunEnd(ApoMarioLevel level) {
        if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) return;
        ApoMarioPlayer chosen = level.getPlayers().get(0);
        for (ApoMarioPlayer p : level.getPlayers()) {
            if (p != null && p.getAi() == null && p.isBVisible()) { chosen = p; break; }
        }
        if (chosen == null) return;
        String name = chosen.getTeamName();
        if (name == null || name.trim().length() == 0) name = "Player";
        storeRun(chosen.getPoints(), Math.max(0, level.getPassedTime()), name);
    }

    public synchronized List<String> getPlayersNames() {
        List<String> out = new ArrayList<String>(); for (Run r : runs) out.add(r.name); return Collections.unmodifiableList(out);
    }
    public synchronized List<Integer> getPlayersScores() {
        List<Integer> out = new ArrayList<Integer>(); for (Run r : runs) out.add(r.score); return Collections.unmodifiableList(out);
    }
    public synchronized List<Integer> getSurvivalTimes() {
        List<Integer> out = new ArrayList<Integer>(); for (Run r : runs) out.add(r.time); return Collections.unmodifiableList(out);
    }

    public synchronized void persistAcrossRuns() {
        if (store == null || store.getFileName() == null) return;
        try {
            Path parent = store.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Path tmp = store.resolveSibling(store.getFileName().toString() + ".tmp");
            BufferedWriter w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8);
            try {
                w.write("APO_MARIO_HIGHSCORE_1"); w.newLine();
                for (Run r : runs) { w.write(Base64.getEncoder().encodeToString(r.name.getBytes(StandardCharsets.UTF_8))); w.write('\t'); w.write(Integer.toString(r.score)); w.write('\t'); w.write(Integer.toString(r.time)); w.newLine(); }
            } finally { w.close(); }
            try { Files.move(tmp, store, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException e) { Files.move(tmp, store, StandardCopyOption.REPLACE_EXISTING); }
        } catch (IOException e) { /* retain the previous snapshot */ }
    }

    private void load() {
        if (store == null || !Files.isRegularFile(store)) return;
        try {
            BufferedReader r = Files.newBufferedReader(store, StandardCharsets.UTF_8);
            try {
                if (!"APO_MARIO_HIGHSCORE_1".equals(r.readLine())) return;
                String line; int count = 0;
                while (count++ < MAX_ENTRIES && (line = r.readLine()) != null) {
                    String[] p = line.split("\\t", -1); if (p.length != 3) continue;
                    try { String n = normalize(new String(Base64.getDecoder().decode(p[0]), StandardCharsets.UTF_8)); int s = Integer.parseInt(p[1]); int t = Integer.parseInt(p[2]); if (n.length() > 0 && t >= 0) runs.add(new Run(n, s, t)); } catch (RuntimeException e) { }
                }
            } finally { r.close(); }
            sortAndTrim();
        } catch (IOException e) { runs.clear(); }
    }
    private String normalize(String s) {
        StringBuilder b = new StringBuilder(); for (int i=0; i<s.length() && b.length()<MAX_NAME; i++) { char c=s.charAt(i); if (c >= 32 && c != 127) b.append(c); }
        return b.toString().trim();
    }
    private void sortAndTrim() {
        Collections.sort(runs, new Comparator<Run>() { public int compare(Run a, Run b) { return b.score == a.score ? 0 : (b.score < a.score ? -1 : 1); } });
        while (runs.size() > MAX_ENTRIES) runs.remove(runs.size()-1);
    }
}