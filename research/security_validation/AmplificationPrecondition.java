package research.validation;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Audit the legacy adapter's necessary two-record precondition, not security. */
public final class AmplificationPrecondition {
    static Object call(Object object, String name) throws Exception {
        return object.getClass().getMethod(name).invoke(object);
    }
    static void result(String status, int records, String reason) {
        System.out.println("ASTERIA_PRECONDITION\t" + status + "\t" + records + "\t" + reason);
    }
    public static void main(String[] args) {
        try {
            Class<?> target = Class.forName(args[0]);
            Path directory = Paths.get(args[1]);
            Path store = directory.resolve("scores.dat");
            Object board = target.getConstructor(Path.class).newInstance(store);
            boolean accepted = (Boolean)target.getMethod("storeRun", int.class, int.class, String.class).invoke(board, 1, 1, "Probe");
            if (!accepted) { result("unknown", -1, "Valid seed was rejected."); return; }
            call(board, "persistAcrossRuns");
            byte[] seed = Files.readAllBytes(store);
            Files.write(directory.resolve("seed.bin"), seed);
            board = target.getConstructor(Path.class).newInstance(store);
            if (!((List<?>)call(board, "getPlayersNames")).contains("Probe")) {
                result("unknown", -1, "Seed did not survive reload."); return;
            }
            // Exact recognition and byte construction from SecurityProbe v1,
            // changing only the number of repetitions from 1,000,000 to two.
            int header = 0;
            if (seed.length > 12 && new DataInputStream(new ByteArrayInputStream(seed)).readInt() == 0x41504853) header = 12;
            else if (seed.length > 4 && new DataInputStream(new ByteArrayInputStream(seed)).readInt() == 1) header = 4;
            else if (seed.length > 1 && (seed[0] & 255) == 0xac && (seed[1] & 255) == 0xed) {
                result("unknown", -1, "Legacy adapter does not support object serialization."); return;
            } else if (!new String(seed, StandardCharsets.UTF_8).contains("Probe") || seed.length > 4096) {
                result("unknown", -1, "Legacy adapter does not support this encoding."); return;
            }
            try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(store))) {
                if (header == 12) out.write(seed, 0, 8);
                if (header > 0) out.writeInt(2);
                for (int i = 0; i < 2; i++) out.write(seed, header, seed.length - header);
            }
            Files.copy(store, directory.resolve("amplified-two.bin"));
            board = target.getConstructor(Path.class).newInstance(store);
            List<?> names = (List<?>)call(board, "getPlayersNames");
            int matches = 0;
            for (Object value : names) if ("Probe".equals(value)) matches++;
            result(matches == 2 ? "matched" : "not_demonstrated", matches,
                   matches == 2 ? "The legacy recipe reloads two seed records; this is a necessary format precondition, not a security pass."
                                : "The legacy recipe did not reload two seed records; a valid-million-record claim is not established.");
        } catch (Throwable error) {
            Throwable cause = error;
            while (cause.getCause() != null) cause = cause.getCause();
            result("unknown", -1, "Probe or target exception: " + cause.getClass().getSimpleName());
        }
    }
}
