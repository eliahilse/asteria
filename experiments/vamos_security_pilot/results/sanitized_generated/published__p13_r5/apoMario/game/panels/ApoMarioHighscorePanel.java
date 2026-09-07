package apoMario.game.panels;

import apoMario.game.ApoMarioPanel;
import apoMario.game.panels.ApoMarioHighscorePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import apoMario.ApoMarioConstants;
import apoMario.game.panels.ApoMarioModel;
import org.apogames.help.ApoHelp;

public class ApoMarioHighscorePanel extends ApoMarioModel {

    public ApoMarioHighscorePanel(apoMario.game.ApoMarioPanel game) {
        super(game);
    }

    @Override
    public void init() {
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT);
        g.setColor(Color.BLACK);
        g.setFont(ApoMarioConstants.FONT_MENU);
        g.drawString("Highscore", 50, 50);

        List<String> names = getGame().getHighscore().getPlayersNames();
        List<Integer> scores = getGame().getHighscore().getPlayersScores();
        List<Integer> times = getGame().getHighscore().getSurvivalTimes();

        int y = 100;
        for (int i = 0; i < Math.min(names.size(), 10); i++) {
            String timeStr = ApoHelp.getTimeToDraw(times.get(i));
            g.drawString((i + 1) + ". " + names.get(i) + " - " + scores.get(i) + " pts - " + timeStr, 50, y);
            y += 30;
        }

        g.drawString("Press ESC to return", 50, ApoMarioConstants.GAME_HEIGHT - 50);
    }

    @Override
    public void think(int delta) {
    }

    @Override
    public void keyButtonReleased(int button, char character) {
        if (button == KeyEvent.VK_ESCAPE) {
            getGame().setMenu();
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
    }

    @Override
    public void mouseButtonReleased(int x, int y) {
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }

    @Override
    public boolean mouseDragged(int x, int y) {
        return false;
    }

    @Override
    public boolean mousePressed(int x, int y, boolean bRight) {
        return false;
    }
}