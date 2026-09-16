package research.security;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** One isolated process per property of the Achievements feature. No external service or exploit gadget. */
public final class AchievementsProbe {
    static boolean hookExecuted;
    public static final class Canary implements Serializable {
        private static final long serialVersionUID = 1L;
        private void readObject(ObjectInputStream input) throws Exception { hookExecuted = true; input.defaultReadObject(); }
    }
    static final int POINTS_GOAL = 20000; static final long TIME_GOAL_MS = 60000L;
    static final Set<String> KNOWN = new HashSet<>(Arrays.asList("POINTS_GOAL", "ENEMIES_GOAL", "TIME_GOAL"));
    static Class<?> target; static Path store;
    static Object fresh() throws Exception { return target.getConstructor(Path.class).newInstance(store); }
    static void call(Object o, String m) throws Exception { target.getMethod(m).invoke(o); }
    static void begin(Object o) throws Exception { call(o, "beginRun"); }
    static void points(Object o, int p) throws Exception { target.getMethod("addPoints", int.class).invoke(o, p); }
    static void time(Object o, long ms) throws Exception { target.getMethod("addTimeSurvived", long.class).invoke(o, ms); }
    static void save(Object o) throws Exception { call(o, "saveAcrossSessions"); }
    @SuppressWarnings("unchecked") static List<String> unlocked(Object o) throws Exception { return (List<String>) target.getMethod("getUnlockedAchievements").invoke(o); }
    static void require(boolean ok, String message) { if (!ok) throw new AssertionError(message); }
    static void onlyKnown(List<String> u, String context) {
        for (String id : u) require(id != null && KNOWN.contains(id), context + ": unlocked list contains an id that is not one of the three achievements: " + (id == null ? "null" : (id.length() > 40 ? id.substring(0, 40) + "... (" + id.length() + " chars)" : id).replaceAll("[^\\x20-\\x7E]", "?")));
        require(new HashSet<>(u).size() == u.size(), context + ": unlocked list contains duplicates: " + u);
        require(u.size() <= 3, context + ": more than three achievements retained: " + u.size());
    }
    static byte[] seed() throws Exception {
        Object a = fresh(); begin(a); points(a, POINTS_GOAL); save(a);
        require(unlocked(a).contains("POINTS_GOAL"), "Seed unlock did not happen.");
        Object b = fresh(); require(unlocked(b).contains("POINTS_GOAL"), "Seed did not survive reload; persistence precondition failed.");
        return Files.readAllBytes(store);
    }
    static String text(byte[] seed) {
        String s = new String(seed, StandardCharsets.UTF_8);
        return s.contains("POINTS_GOAL") && seed.length <= 4096 ? s : null;
    }
    static void unknown(String why) { System.out.println("ASTERIA_RESULT\tunknown\t" + why); }
    public static void main(String[] args) {
        try {
            target = Class.forName(args[0]); store = Paths.get(args[2]);
            String check = args[1];
            switch (check) {
                case "validUnlockRoundTrip": { seed(); break; }
                case "rejectsNegativePoints": {
                    Object a = fresh(); begin(a); points(a, -1); require(!unlocked(a).contains("POINTS_GOAL"), "Negative points unlocked the points goal.");
                    points(a, POINTS_GOAL); require(unlocked(a).contains("POINTS_GOAL"), "Negative points were summed into the run total: reaching the goal afterwards did not unlock it."); break; }
                case "rejectsNegativeTime": {
                    Object a = fresh(); begin(a); time(a, -1L); require(!unlocked(a).contains("TIME_GOAL"), "Negative time unlocked the time goal.");
                    time(a, TIME_GOAL_MS + 1); require(unlocked(a).contains("TIME_GOAL"), "Negative time was summed into the run total: surviving past the goal afterwards did not unlock it."); break; }
                case "pointsOverflowSafe": {
                    Object a = fresh(); begin(a); points(a, POINTS_GOAL - 1); points(a, Integer.MAX_VALUE);
                    require(unlocked(a).contains("POINTS_GOAL"), "Adding a huge point value after 19999 did not unlock the goal: the run total overflowed."); break; }
                case "rejectsUnknownIds": case "rejectsOversizedId": {
                    byte[] seed = seed(); String s = text(seed); if (s == null) { unknown("Unsupported persistence encoding; the store is not text naming the achievement."); return; }
                    String bogus = check.equals("rejectsUnknownIds") ? "BOGUS_GOAL" : repeat('A', 10000);
                    Files.write(store, s.replace("POINTS_GOAL", bogus).getBytes(StandardCharsets.UTF_8));
                    Object a = fresh(); List<String> u = unlocked(a); onlyKnown(u, check); break; }
                case "boundsUnlockedList": {
                    byte[] seed = seed(); String s = text(seed); if (s == null) { unknown("Unsupported persistence encoding; the store is not text naming the achievement."); return; }
                    String line = null; for (String l : s.split("\n")) if (l.contains("POINTS_GOAL")) { line = l; break; }
                    StringBuilder b = new StringBuilder(s); for (int i = 0; i < 1000; i++) b.append('\n').append(line);
                    Files.write(store, b.toString().getBytes(StandardCharsets.UTF_8));
                    Object a = fresh(); onlyKnown(unlocked(a), check); break; }
                case "malformedStoreDoesNotCrash": {
                    byte[] junk = new byte[4096]; new Random(7).nextBytes(junk); Files.write(store, junk);
                    Object a = fresh(); onlyKnown(unlocked(a), check); begin(a); points(a, POINTS_GOAL); require(unlocked(a).contains("POINTS_GOAL"), "Feature unusable after a malformed store."); break; }
                case "oversizedPhysicalLine": {
                    try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(store))) { byte[] chunk = new byte[1 << 16]; Arrays.fill(chunk, (byte) 'A'); for (int i = 0; i < 1024; i++) out.write(chunk); }
                    Object a = fresh(); onlyKnown(unlocked(a), check); break; }
                case "largeStoreFile": {
                    byte[] seed = seed(); String s = text(seed); if (s == null) { unknown("Unsupported persistence encoding; no valid-record amplification claim."); return; }
                    final int records = 1000000;
                    try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(store))) { byte[] unit = (s.endsWith("\n") ? s : s + "\n").getBytes(StandardCharsets.UTF_8); for (int i = 0; i < records; i++) out.write(unit); }
                    Object a = fresh(); onlyKnown(unlocked(a), check); break; }
                case "nativeDeserializationCanary": {
                    try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(store))) { out.writeObject(new Canary()); }
                    Object a = fresh(); List<String> u = unlocked(a); require(!hookExecuted, "Store bytes were deserialized as Java objects: attacker-controlled readObject executed."); onlyKnown(u, check); break; }
                default: throw new IllegalArgumentException("Unknown check " + check);
            }
            System.out.println("ASTERIA_RESULT\tpass\tDeclared property held for this fixture.");
        } catch (InvocationTargetException e) {
            Throwable c = e.getCause(); System.out.println("ASTERIA_RESULT\tfail\t" + c.getClass().getSimpleName() + ": " + String.valueOf(c.getMessage()).replace('\n', ' ')); System.exit(1);
        } catch (AssertionError e) {
            System.out.println("ASTERIA_RESULT\tfail\tAssertionError: " + e.getMessage()); System.exit(1);
        } catch (Throwable e) {
            System.out.println("ASTERIA_RESULT\tfail\t" + e.getClass().getSimpleName() + ": " + String.valueOf(e.getMessage()).replace('\n', ' ')); System.exit(1);
        }
    }
    static String repeat(char c, int n) { char[] a = new char[n]; Arrays.fill(a, c); return new String(a); }
}
