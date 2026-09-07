package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioHighscorePanel;
import apoMario.game.panels.ApoMarioModelMenu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import apoMario.ApoMarioConstants;
import apoMario.game.ApoMarioPanel;

public class ApoMarioHighscorePanel extends ApoMarioModelMenu {
    private ApoMarioHighscore highscore;

    public ApoMarioHighscorePanel(ApoMarioPanel game, ApoMarioHighscore highscore) {
        super(game);
        this.highscore = highscore;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void makeBackground() {
    }

    @Override
    public void makeBackgroundAnimation() {
    }

    @Override
    public void makeRunner() {
    }

    @Override
    public void makeSearch() {
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
    public void excecuteFunction() {
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

    @Override
    public void think(int delta) {
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT);
        g.setColor(Color.BLACK);
        g.setFont(ApoMarioConstants.FONT_MENU);
        
        g.drawString("Highscores", 100, 50);
        
        List<String> names = highscore.getPlayersNames();
        List<Integer> scores = highscore.getPlayersScores();
        List<Integer> times = highscore.getSurvivalTimes();
        
        for (int i = 0; i < names.size() && i < 10; i++) {
            g.drawString((i + 1) + ". " + names.get(i) + " - Score: " + scores.get(i) + " - Time: " + times.get(i) + "s", 100, 100 + i * 30);
        }
        
        g.drawString("Press ESC to return", 100, 500);
    }
}