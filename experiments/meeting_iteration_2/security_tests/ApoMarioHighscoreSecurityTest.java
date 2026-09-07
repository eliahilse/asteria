package apoMario.game.panels;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

/** Adversarial requirements deliberately kept separate from paper tests. */
public class ApoMarioHighscoreSecurityTest {

    private ApoMarioHighscore fresh() throws Exception {
        Path dir = Files.createTempDirectory("highscore-security-");
        return new ApoMarioHighscore(dir.resolve("scores.dat"));
    }

    private void assertRejected(int score, int time, String name) throws Exception {
        ApoMarioHighscore highscore = fresh();
        assertFalse("invalid record must be rejected", highscore.storeRun(score, time, name));
        assertEquals(0, highscore.getPlayersNames().size());
        assertEquals(0, highscore.getPlayersScores().size());
        assertEquals(0, highscore.getSurvivalTimes().size());
    }

    @Test
    public void rejectsNegativeScore() throws Exception {
        assertRejected(-1, 1, "Player");
    }

    @Test
    public void rejectsNegativeTime() throws Exception {
        assertRejected(1, -1, "Player");
    }

    @Test
    public void rejectsNullName() throws Exception {
        assertRejected(1, 1, null);
    }

    @Test
    public void rejectsBlankName() throws Exception {
        assertRejected(1, 1, "   ");
    }

    @Test
    public void rejectsExcessiveName() throws Exception {
        char[] characters = new char[1024];
        Arrays.fill(characters, 'A');
        assertRejected(1, 1, new String(characters));
    }

    @Test
    public void boundsRetainedEntries() throws Exception {
        ApoMarioHighscore highscore = fresh();
        for (int i = 0; i < 120; i++) {
            assertTrue(highscore.storeRun(i, i, "P" + i));
        }
        assertTrue("retained board must have a fixed cap",
                highscore.getPlayersNames().size() <= 100);
        assertEquals(highscore.getPlayersNames().size(), highscore.getPlayersScores().size());
        assertEquals(highscore.getPlayersNames().size(), highscore.getSurvivalTimes().size());
    }
}
