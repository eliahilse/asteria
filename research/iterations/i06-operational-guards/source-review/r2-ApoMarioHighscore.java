package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    private static final int MAGIC = 0x41504D48;
    private static final int VERSION = 1;
    private static final int MAX_ENTRIES = 1000;
    private static final int MAX_NAME_BYTES = 128;
    private static final long MAX_FILE_BYTES = 1024L * 1024L;

    private static final class Run {
        final int score;
        final int time;
        final String name;
        Run(int score, int time, String name) { this.score = score; this.time = time; this.name = name; }
    }

    private final Path store;
    private final List<Run> runs = new ArrayList<Run>();

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (!valid(score, survivalTime, playerName)) return false;
        runs.add(new Run(score, survivalTime, normalize(playerName)));
        sort();
        if (runs.size() > MAX_ENTRIES) runs.remove(runs.size() - 1);
        persistAcrossRuns();
        return true;
    }

    public synchronized List<String> getPlayersNames() {
        List<String> result = new ArrayList<String>();
        for (Run r : runs) result.add(r.name);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getPlayersScores() {
        List<Integer> result = new ArrayList<Integer>();
        for (Run r : runs) result.add(r.score);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getSurvivalTimes() {
        List<Integer> result = new ArrayList<Integer>();
        for (Run r : runs) result.add(r.time);
        return Collections.unmodifiableList(result);
    }

    public synchronized void recordRunEnd(ApoMarioLevel level) {
        if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) return;
        ApoMarioPlayer chosen = level.getPlayers().get(0);
        for (ApoMarioPlayer player : level.getPlayers()) {
            if (player != null && player.getAi() == null && player.isBVisible()) { chosen = player; break; }
        }
        if (chosen == null) return;
        String name = chosen.getTeamName();
        if (name == null || name.trim().length() == 0) name = "Player";
        storeRun(chosen.getPoints(), Math.max(0, level.getPassedTime()), name);
    }

    public synchronized void persistAcrossRuns() {
        if (store == null) return;
        Path tmp = null;
        try {
            Path absolute = store.toAbsolutePath();
            Path parent = absolute.getParent();
            if (parent != null) Files.createDirectories(parent);
            tmp = Files.createTempFile(parent, absolute.getFileName().toString(), ".tmp");
            DataOutputStream out = new DataOutputStream(Files.newOutputStream(tmp));
            out.writeInt(MAGIC);
            out.writeInt(VERSION);
            out.writeInt(runs.size());
            for (Run r : runs) {
                byte[] bytes = r.name.getBytes(StandardCharsets.UTF_8);
                out.writeInt(r.score);
                out.writeInt(r.time);
                out.writeInt(bytes.length);
                out.write(bytes);
            }
            out.flush();
            out.close();
            try { Files.move(tmp, absolute, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (IOException ex) { Files.move(tmp, absolute, StandardCopyOption.REPLACE_EXISTING); }
            tmp = null;
        } catch (IOException ex) {
            // Storage failure must not interrupt the game.
        } finally {
            if (tmp != null) try { Files.deleteIfExists(tmp); } catch (IOException ignored) { }
        }
    }

    private void load() {
        if (store == null) return;
        try {
            Path absolute = store.toAbsolutePath();
            if (!Files.exists(absolute) || Files.size(absolute) > MAX_FILE_BYTES) return;
            DataInputStream in = new DataInputStream(Files.newInputStream(absolute));
            if (in.readInt() != MAGIC || in.readInt() != VERSION) { in.close(); return; }
            int count = in.readInt();
            if (count < 0 || count > MAX_ENTRIES) { in.close(); return; }
            List<Run> loaded = new ArrayList<Run>();
            for (int i = 0; i < count; i++) {
                int score = in.readInt();
                int time = in.readInt();
                int length = in.readInt();
                if (length < 1 || length > MAX_NAME_BYTES) { in.close(); return; }
                byte[] bytes = new byte[length];
                in.readFully(bytes);
                String name = new String(bytes, StandardCharsets.UTF_8);
                if (!valid(score, time, name)) { in.close(); return; }
                loaded.add(new Run(score, time, normalize(name)));
            }
            in.close();
            runs.clear();
            runs.addAll(loaded);
            sort();
        } catch (IOException | RuntimeException ignored) {
            // Malformed stores are ignored without exposing partial records.
        }
    }

    private void sort() {
        Collections.sort(runs, new Comparator<Run>() {
            public int compare(Run a, Run b) {
                if (a.score != b.score) return a.score < b.score ? 1 : -1;
                return a.name.compareTo(b.name);
            }
        });
    }

    private static String normalize(String name) { return name.trim(); }

    private static boolean valid(int score, int time, String name) {
        if (time < 0 || name == null) return false;
        String normalized = normalize(name);
        if (normalized.length() == 0 || normalized.length() > 64) return false;
        if (normalized.getBytes(StandardCharsets.UTF_8).length > MAX_NAME_BYTES) return false;
        for (int i = 0; i < normalized.length(); i++) {
            if (Character.isISOControl(normalized.charAt(i))) return false;
        }
        return true;
    }
}