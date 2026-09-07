// Distilled from corpus/java/ApoCheating/.../apoCheating/ApoCheatingLoadSave.java
//   line 368: ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
//   line 371: int y = in.readInt();  int x = in.readInt();
//   line 373: this.aPlayground = new int[y][x];       // <-- sizes come from the file
//   line 382: int size = in.readInt(); ... loops `size` times
//
// NOTE: the corpus wraps the file in ObjectInputStream but only ever calls
// readInt(), never readObject(). So this is NOT a Java-serialization gadget RCE
// (there is no readObject sink anywhere in the corpus). The real defect is that
// allocation sizes are read straight from an untrusted file with no bounds
// check -> memory exhaustion / negative-size crash from a tiny crafted file.
// Level/save files are exactly the kind of content shared between players.

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class VulnerableLevelParser {
    int[][] playground;

    public void readLevel(String fileName) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            int y = in.readInt();
            int x = in.readInt();
            System.out.println("[game] level dimensions from file: y=" + y + " x=" + x);
            this.playground = new int[y][x];   // unchecked allocation
            for (int i = 0; i < y; i++)
                for (int j = 0; j < x; j++)
                    this.playground[i][j] = in.readInt();
            System.out.println("[game] level loaded ok");
        }
    }

    public static void main(String[] args) throws Exception {
        new VulnerableLevelParser().readLevel(args[0]);
    }
}
