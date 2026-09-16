package research.security;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Positive control: bounded reads, validated ids, saturating counters, no object deserialization. */
public final class SafeAchievements {
    static final int POINTS_GOAL = 20000, ENEMIES_GOAL = 3; static final long TIME_GOAL_MS = 60000L;
    static final List<String> KNOWN = Arrays.asList("POINTS_GOAL", "ENEMIES_GOAL", "TIME_GOAL");
    static final int MAX_LINE = 64, MAX_LINES = 16;
    private final Path store; private final LinkedHashSet<String> unlocked = new LinkedHashSet<>();
    private long points, time; private int kills;
    public SafeAchievements(Path store) { this.store = store; load(); }
    public void beginRun() { points = 0; time = 0; kills = 0; }
    public void addPoints(int p) { if (p < 0) return; points = Math.min(Long.MAX_VALUE / 2, points + p); if (points >= POINTS_GOAL) unlocked.add("POINTS_GOAL"); }
    public void addEnemyKilled() { if (kills < Integer.MAX_VALUE) kills++; if (kills >= ENEMIES_GOAL) unlocked.add("ENEMIES_GOAL"); }
    public void addTimeSurvived(long ms) { if (ms < 0) return; time = Math.min(Long.MAX_VALUE / 2, time + ms); if (time > TIME_GOAL_MS) unlocked.add("TIME_GOAL"); }
    public void recordRunEnd(Object level) { }
    public List<String> getUnlockedAchievements() { return new ArrayList<>(unlocked); }
    public void saveAcrossSessions() {
        try { Files.write(store, (String.join("\n", unlocked) + "\n").getBytes(StandardCharsets.UTF_8)); } catch (IOException ignored) { }
    }
    private void load() {
        if (!Files.isRegularFile(store)) return;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(store))) {
            ByteArrayOutputStream line = new ByteArrayOutputStream(); int lines = 0, c; boolean overflow = false;
            while ((c = in.read()) >= 0 && lines < MAX_LINES) {
                if (c == '\n') { if (!overflow) accept(line.toString("UTF-8")); line.reset(); overflow = false; lines++; continue; }
                if (line.size() >= MAX_LINE) { overflow = true; continue; }
                line.write(c);
            }
            if (!overflow && lines < MAX_LINES) accept(line.toString("UTF-8"));
        } catch (IOException ignored) { unlocked.clear(); }
    }
    private void accept(String id) { id = id.trim(); if (KNOWN.contains(id)) unlocked.add(id); }
}
