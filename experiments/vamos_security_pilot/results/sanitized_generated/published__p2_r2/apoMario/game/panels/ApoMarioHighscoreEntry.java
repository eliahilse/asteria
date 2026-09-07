package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscoreEntry;

import java.io.Serializable;

public class ApoMarioHighscoreEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public int score;
    public int time;

    public ApoMarioHighscoreEntry(int score, int time, String name) {
        this.score = score;
        this.time = time;
        this.name = name;
    }
}