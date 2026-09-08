package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import apoMario.entity.ApoMarioPlayer;
import apoMario.level.ApoMarioLevel;

/** Persistent, local highscore board for completed Mario runs. */
public class ApoMarioHighscore {
    private static final int MAGIC = 0x41504D48;
    private static final int VERSION = 1;
    private static final int MAX_ENTRIES = 1000;
    private static final int MAX_NAME = 64;

    private static final class Run {
        final int score;
        final int time;
        final String name;
        Run(int score, int time, String name) {
            this.score = score;
            this.time = time;
            this.name = name;
        }
    }

    private final Path store;
    private final ArrayList<Run> runs = new ArrayList<Run>();
    private final Map<ApoMarioLevel, Boolean> recordedLevels = new IdentityHashMap<ApoMarioLevel, Boolean>();

    public ApoMarioHighscore(Path store) {
        this.store = store;
        load();
    }

    public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
        if (!validName(playerName) || survivalTime < 0 || runs.size() >= MAX_ENTRIES) {
            return false;
        }
        runs.add(new Run(score, survivalTime, playerName.trim()));
        sort();
        persistFile();
        return true;
    }

    /** Records the best real player in the terminal level state, only once per level run. */
    public synchronized void recordRunEnd(ApoMarioLevel level) {
        if (level == null || recordedLevels.containsKey(level)) {
            return;
        }
        List<ApoMarioPlayer> players = level.getPlayers();
        if (players == null || players.isEmpty()) {
            return;
        }
        // The first slot is the live human/player-one run.  Do not choose the
        // numerically largest slot: player two may be an AI or a stale hidden
        // participant and must not replace the run being recorded.
        ApoMarioPlayer best = players.get(0);
        if (best == null) {
            return;
        }
        String name = best.getTeamName();
        if (name == null || name.trim().length() == 0) {
            name = "Player";
        }
        int elapsed;
        try {
            elapsed = level.getPassedTime();
        } catch (RuntimeException ex) {
            return;
        }
        if (elapsed < 0) {
            elapsed = 0;
        }
        if (storeRun(best.getPoints(), elapsed, name)) {
            recordedLevels.put(level, Boolean.TRUE);
        }
    }

    public synchronized List<String> getPlayersNames() {
        ArrayList<String> result = new ArrayList<String>();
        for (Run run : runs) result.add(run.name);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getPlayersScores() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Run run : runs) result.add(run.score);
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Integer> getSurvivalTimes() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Run run : runs) result.add(run.time);
        return Collections.unmodifiableList(result);
    }

    /** Starts a fresh lifecycle for the supplied level object. */
    public synchronized void resetRun(ApoMarioLevel level) {
        if (level != null) {
            recordedLevels.remove(level);
        }
    }

    public synchronized void persistAcrossRuns() {
        persistFile();
    }

    /** Renders milliseconds as mm:ss; the stored value is never changed. */
    public synchronized void renderBoard(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(new Color(255, 255, 255, 235));
        g.fillRoundRect(x, y, width, height, 16, 16);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 16, 16);
        g.setFont(new Font("Dialog", Font.BOLD, 16));
        g.drawString("HIGHSCORES", x + 12, y + 24);
        g.setFont(new Font("Dialog", Font.PLAIN, 12));
        int row = y + 45;
        int limit = Math.min(runs.size(), Math.max(0, (height - 52) / 18));
        for (int i = 0; i < limit; i++) {
            Run run = runs.get(i);
            int seconds = run.time / 1000;
            String time = String.format("%02d:%02d", seconds / 60, seconds % 60);
            g.drawString(String.valueOf(i + 1), x + 12, row);
            g.drawString(run.name, x + 38, row);
            g.drawString(String.valueOf(run.score), x + width - 105, row);
            g.drawString(time, x + width - 55, row);
            row += 18;
        }
    }

    private boolean validName(String name) {
        if (name == null || name.trim().length() == 0 || name.trim().length() > MAX_NAME) return false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c < 32 || c == 127) return false;
        }
        return true;
    }

    private void sort() {
        Collections.sort(runs, new Comparator<Run>() {
            public int compare(Run a, Run b) {
                return a.score == b.score ? 0 : (a.score < b.score ? 1 : -1);
            }
        });
    }

    private synchronized void load() {
        if (store == null || !Files.isRegularFile(store)) return;
        try {
            if (Files.size(store) > 256 * 1024) return;
            ArrayList<Run> loaded = new ArrayList<Run>();
            DataInputStream in = new DataInputStream(Files.newInputStream(store));
            if (in.readInt() != MAGIC || in.readInt() != VERSION) { in.close(); return; }
            int count = in.readInt();
            if (count < 0 || count > MAX_ENTRIES) { in.close(); return; }
            for (int i = 0; i < count; i++) {
                int score = in.readInt();
                int time = in.readInt();
                int length = in.readUnsignedShort();
                if (length > MAX_NAME) { in.close(); return; }
                byte[] bytes = new byte[length];
                in.readFully(bytes);
                String name = new String(bytes, StandardCharsets.UTF_8);
                if (!validName(name) || time < 0) { in.close(); return; }
                loaded.add(new Run(score, time, name));
            }
            in.close();
            runs.clear();
            runs.addAll(loaded);
            sort();
        } catch (IOException ex) {
            // A corrupt or unavailable store must not prevent the game from starting.
        }
    }

    private synchronized boolean persistFile() {
        if (store == null) return false;
        Path parent = store.toAbsolutePath().getParent();
        Path temporary = null;
        try {
            if (parent != null) Files.createDirectories(parent);
            temporary = Files.createTempFile(parent, store.getFileName().toString(), ".tmp");
            DataOutputStream out = new DataOutputStream(Files.newOutputStream(temporary));
            out.writeInt(MAGIC);
            out.writeInt(VERSION);
            out.writeInt(runs.size());
            for (Run run : runs) {
                byte[] bytes = run.name.getBytes(StandardCharsets.UTF_8);
                if (bytes.length > MAX_NAME) { out.close(); Files.deleteIfExists(temporary); return false; }
                out.writeInt(run.score);
                out.writeInt(run.time);
                out.writeShort(bytes.length);
                out.write(bytes);
            }
            out.flush();
            out.close();
            try {
                Files.move(temporary, store, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporary, store, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException ex) {
            try { if (temporary != null) Files.deleteIfExists(temporary); } catch (IOException ignored) { }
            return false;
        }
    }
}