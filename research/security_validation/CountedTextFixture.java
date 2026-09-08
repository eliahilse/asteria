package research.validation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Test-only counted text format demonstrating a legacy adapter blind spot. */
public final class CountedTextFixture {
    private final Path store;
    private final List<String> names = new ArrayList<String>();
    public CountedTextFixture(Path store) throws IOException {
        this.store = store;
        if (!Files.exists(store)) return;
        try (BufferedReader in = Files.newBufferedReader(store, StandardCharsets.UTF_8)) {
            if (!"SCORES".equals(in.readLine())) return;
            int count = Integer.parseInt(in.readLine());
            for (int i = 0; i < count; i++) {
                String line = in.readLine();
                if (line == null) throw new EOFException();
                names.add(line);
            }
        }
    }
    public boolean storeRun(int score, int time, String name) { names.add(name); return true; }
    public List<String> getPlayersNames() { return names; }
    public void persistAcrossRuns() throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(store, StandardCharsets.UTF_8)) {
            out.write("SCORES\n" + names.size() + "\n");
            for (String name : names) { out.write(name); out.newLine(); }
        }
    }
}
