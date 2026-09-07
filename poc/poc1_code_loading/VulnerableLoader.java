// Distilled from the Apo-Games corpus, verbatim mechanism:
//   corpus/java/ApoIcejump/.../apoIcejump/ApoIcejumpClassLoader.java  (loadClass -> defineClass)
//   corpus/java/ApoIcejump/.../apoIcejump/ApoIcejumpPanel.java:647    (loadPlayer -> getAI -> newInstance)
//   cloned equivalent: org/apogames/help/ApoClassLoader.java (URLClassLoader from an arbitrary URL/path)
//
// The game's "load AI player" feature takes a directory path + class name from
// the player, loads that .class with a custom loader, and instantiates it. The
// attacker controls the .class file. newInstance() runs attacker code.

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class VulnerableLoader extends ClassLoader {
    private final String root;
    private final String name;

    public VulnerableLoader(String rootDir, String name) {
        this.root = rootDir;
        this.name = name;
    }

    // Mirrors ApoIcejumpClassLoader.loadClass: reads raw bytes from a file under
    // an attacker-influenced root and calls defineClass with no verification.
    public Class<?> load() throws Exception {
        String filename = name.replace('.', File.separatorChar) + ".class";
        File f = new File(this.root, filename);
        byte[] buff = new byte[(int) f.length()];
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            dis.readFully(buff);
        }
        return defineClass(name, buff, 0, buff.length);
    }

    // Mirrors ApoIcejumpPanel.loadPlayer(...).getAI(): instantiate the loaded
    // class. This is where attacker code runs.
    public Object getAI() throws Exception {
        return load().getDeclaredConstructor().newInstance();
    }

    public static void main(String[] args) throws Exception {
        String root = args.length > 0 ? args[0] : ".";
        String cls  = args.length > 1 ? args[1] : "MaliciousAI";
        System.out.println("[game] loading AI player: root=" + root + " class=" + cls);
        Object ai = new VulnerableLoader(root, cls).getAI();
        System.out.println("[game] AI instance created: " + ai);
    }
}
