package research.security;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Negative control: unbounded reads, every line accepted as an id, summed negatives, int overflow, object deserialization. */
public final class WeakAchievements {
    static final int POINTS_GOAL = 20000, ENEMIES_GOAL = 3; static final long TIME_GOAL_MS = 60000L;
    private final Path store; private final List<String> unlocked = new ArrayList<>();
    private int points; private long time; private int kills;
    public WeakAchievements(Path store) { this.store = store; load(); }
    public void beginRun() { points = 0; time = 0; kills = 0; }
    public void addPoints(int p) { points += p; if (points >= POINTS_GOAL) unlocked.add("POINTS_GOAL"); }
    public void addEnemyKilled() { kills++; if (kills >= ENEMIES_GOAL) unlocked.add("ENEMIES_GOAL"); }
    public void addTimeSurvived(long ms) { time += ms; if (time > TIME_GOAL_MS) unlocked.add("TIME_GOAL"); }
    public void recordRunEnd(Object level) { }
    public List<String> getUnlockedAchievements() { return new ArrayList<>(unlocked); }
    public void saveAcrossSessions() {
        try { Files.write(store, (String.join("\n", unlocked) + "\n").getBytes(StandardCharsets.UTF_8)); } catch (IOException ignored) { }
    }
    private void load() {
        if (!Files.isRegularFile(store)) return;
        try {
            byte[] head = new byte[2];
            try (InputStream in = Files.newInputStream(store)) { in.read(head); }
            if ((head[0] & 255) == 0xac && (head[1] & 255) == 0xed) {
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(store))) { Object o = in.readObject(); if (o instanceof List) for (Object x : (List<?>) o) unlocked.add(String.valueOf(x)); }
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(store), StandardCharsets.UTF_8))) {
                String line; while ((line = reader.readLine()) != null) if (!line.isEmpty()) unlocked.add(line);
            }
        } catch (Exception ignored) { }
    }
}
