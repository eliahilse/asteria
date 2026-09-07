package apoMario.game.panels;

import apoMario.game.panels.ApoMarioAnalysis;
import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioModelMenu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.apogames.ApoConstants;
import org.apogames.entity.ApoAnimation;
import org.apogames.help.ApoHelp;

import apoMario.ApoMarioConstants;
import apoMario.ApoMarioImageContainer;
import apoMario.entity.ApoMarioPlayer;
import apoMario.game.ApoMarioPanel;
import apoMario.game.ApoMarioReplay;
import apoMario.game.ApoMarioSearch;
import apoMario.game.ApoMarioSearchNode;
import apoMario.game.ApoMarioSearchRunner;


/**
 * @author Dirk Aporius
 */
public class ApoMarioAnalysis extends ApoMarioModelMenu {

	public static final String FUNCTION_ANALYSIS_BACK = "backAnalysis";
	public static final String FUNCTION_ANALYSIS_RESTART = "restartAnalysis";
	public static final String FUNCTION_ANALYSIS_NEWLEVEL = "newLevelAnalysis";
	public static final String FUNCTION_ANALYSIS_REPLAYSAVE = "saveReplay";
	public static final String FUNCTION_ANALYSIS_REPLAYLOAD = "loadReplay";
	public static final String FUNCTION_ANALYSIS_REPLAYPLAY = "playReplay";
	
	private final int FADE_TIME = 500;
	
	private boolean bFade;
	private BufferedImage iLevel;
	private int fadeTime;
	
	public ApoMarioAnalysis(ApoMarioPanel game) {
		super(game);
	}
	
	@Override
	public void init() {
		super.init();
		this.bFade = true;
		this.iLevel = this.getGame().getBackgroundImage();
		this.fadeTime = 0;
		if (this.getGame().getLevel() != null) {
			ApoMarioHighscore highscore = new ApoMarioHighscore(java.nio.file.Paths.get("highscore.dat"));
			highscore.recordRunEnd(this.getGame().getLevel());
		}
	}

	@Override
	public void makeBackground() {
		//if (this.getIBackground() == null) {
			this.setIBackground(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB));
			Graphics2D g = (Graphics2D)(this.getIBackground().getGraphics());
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			BufferedImage iMenuTile = ApoMarioImageContainer.MENU;
			int size = ApoMarioConstants.TILE_SIZE * ApoMarioConstants.APP_SIZE;
			int waterHeight = 4;
			for (int x = 0; x < 20; x++) {
				for (int y = 0; y < waterHeight; y++) {
					g.drawImage(iMenuTile.getSubimage(14 * size, 0 * size, size, size), x * size, y * size, null);
				}
				for (int y = waterHeight; y < 15; y++) {
					g.drawImage(iMenuTile.getSubimage(0 * size, 0 * size, size, size), x * size, y * size, null);
				}

				g.drawImage(iMenuTile.getSubimage(11 * size, 4 * size, size/2, size/2), x * size, waterHeight * size, null);
				g.drawImage(iMenuTile.getSubimage(11 * size, 4 * size, size/2, size/2), x * size + size/2, waterHeight * size, null);
			}
			
			g.dispose();
		//}
	}

	@Override
	public void makeBackgroundAnimation() {
		this.setBackgroundAnimation(new ArrayList<ApoAnimation>());
	}

	@Override
	public void makeRunner() {
	}

	@Override
	public void makeSearch() {
		this.setSearch(new ApoMarioSearch());
		ApoMarioSearchNode node = new ApoMarioSearchNode(0, 0, 0);
		this.getSearch().addNode(node);
	}

	@Override
	public void keyButtonReleased(int button, char character) {
		if (button == KeyEvent.VK_ESCAPE) {
			this.getGame().setMenu();
		} else if (button == KeyEvent.VK_SPACE) {
			this.playNewLevel();
		} else if (button == KeyEvent.VK_R) {
			this.getGame().restartLevel();
		} else if (button == KeyEvent.VK_T) {
			if (ApoMarioConstants.TILE_SET == ApoMarioConstants.TILE_SET_EAT) {
				ApoMarioConstants.TILE_SET = ApoMarioConstants.TILE_SET_MARIO;
			} else if (ApoMarioConstants.TILE_SET == ApoMarioConstants.TILE_SET_EAT) {
				ApoMarioConstants.TILE_SET = ApoMarioConstants.TILE_SET_MARIO;
			} else if (ApoMarioConstants.TILE_SET == ApoMarioConstants.TILE_SET_MARIO) {
				ApoMarioConstants.TILE_SET = ApoMarioConstants.TILE_SET_EAT;
			}
			ApoMarioImageContainer.setTileSet(ApoMarioConstants.TILE_SET);
			this.getGame().restartLevel();
			this.getGame().setMenu();
		}
		this.bFade = false;
	}

	@Override
	public void mouseButtonFunction(String function) {
		if (function.equals(ApoMarioAnalysis.FUNCTION_ANALYSIS_BACK)) {
			this.getGame().setMenu();
		} else if (function.equals(ApoMarioAnalysis.FUNCTION_ANALYSIS_RESTART)) {
			this.getGame().restartLevel();
		} else if (function.equals(ApoMarioAnalysis.FUNCTION_ANALYSIS_NEWLEVEL)) {
			this.playNewLevel();
		} else if (function.equals(ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYSAVE)) {
			this.saveReplay();
		} else if (function.equals(ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYLOAD)) {
			if (!ApoConstants.B_APPLET) {
				this.getGame().loadReplay();
			}
		} else if (function.equals(ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYPLAY)) {
			this.getGame().setReplay(this.getGame().getLevel().getReplay());
		}
	}

	@Override
	public void releasedEnter() {
	}

	@Override
	public void excecuteFunction() {
	}

	private void playNewLevel() {
		this.getGame().newLevel();
	}
	
	private void saveReplay() {
		if (super.getGame().getFileChooserReplay().showSaveDialog(super.getGame()) == JFileChooser.APPROVE_OPTION) {
			String path = super.getGame().getFileChooserReplay().getSelectedFile().getPath();
			if (path.indexOf(".rep") == -1) {
				path = path + ".rep";
			}
			this.getGame().getLevel().getReplay().save(path);
		}
	}

	@Override
	public void mouseButtonReleased(int x, int y) {
	}

	@Override
	public boolean mouseDragged(int x, int y) {
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean mousePressed(int x, int y, boolean bRight) {
		return false;
	}

	@Override
	public void think(int delta) {
		if (this.bFade) {
			this.fadeTime += delta;
			if (this.fadeTime > this.FADE_TIME) {
				this.bFade = false;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		if (this.bFade) {
			g.drawImage(this.iLevel, 0, 0, null);
			float alpha = (float)this.fadeTime / (float)this.FADE_TIME;
			if (alpha > 1f) {
				alpha = 1f;
			}
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g.drawImage(this.getGame().getIMenuBackground(), 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		} else {
			g.drawImage(this.getGame().getIMenuBackground(), 0, 0, null);			
		}
		
		g.setFont(ApoMarioConstants.FONT_STATISTICS);
		g.setColor(Color.BLACK);
		
		ApoMarioPlayer playerOne = this.getGame().getLevel().getPlayers().get(0);
		ApoMarioPlayer playerTwo = this.getGame().getLevel().getPlayers().get(1);
		
		String one = this.getName(playerOne);
		String two = this.getName(playerTwo);
		
		this.renderPlayerStats(g, playerOne, playerTwo, one, two);
	}
	
	private boolean isOnePlayer() {
		if (this.getGame().getLevel().getPlayers().get(1).getAi() == null) {
			return true;
		}
		return false;
	}
	
	private String getName(ApoMarioPlayer player) {
		String aiName = "Human";
		if ((player.getAi() != null) && (player.getAi().getTeamName() != null)) {
			aiName = player.getAi().getTeamName();
		}
		return aiName;
	}
	
	private void renderPlayerStats(Graphics2D g, ApoMarioPlayer playerOne, ApoMarioPlayer playerTwo, String one, String two) {
		int startX = 60 * ApoMarioConstants.SIZE;
		int width = ApoMarioConstants.GAME_WIDTH - 2 * startX;
		int startY = 60 * ApoMarioConstants.SIZE;
		
		int w = g.getFontMetrics().stringWidth("Statistics");
		g.drawString("Statistics", ApoMarioConstants.GAME_WIDTH/2 - w/2, startY);
		
		if (this.isOnePlayer()) {
			this.renderWinPlayer(g, playerOne);
		} else {
			startY += 30 * ApoMarioConstants.SIZE;
			int xOne = startX + width / 4;
			int xTwo = startX + width * 3 / 4;
			
			w = g.getFontMetrics().stringWidth(one);
			g.drawString(one, xOne - w/2, startY);
			
			w = g.getFontMetrics().stringWidth(two);
			g.drawString(two, xTwo - w/2, startY);
			
			startY += 25 * ApoMarioConstants.SIZE;
			
			String pointsOne = "Points: " + playerOne.getPoints();
			w = g.getFontMetrics().stringWidth(pointsOne);
			g.drawString(pointsOne, xOne - w/2, startY);

			String pointsTwo = "Points: " + playerTwo.getPoints();
			w = g.getFontMetrics().stringWidth(pointsTwo);
			g.drawString(pointsTwo, xTwo - w/2, startY);

			startY += 25 * ApoMarioConstants.SIZE;
			
			String coinsOne = "Coins: " + playerOne.getCoins();
			w = g.getFontMetrics().stringWidth(coinsOne);
			g.drawString(coinsOne, xOne - w/2, startY);

			String coinsTwo = "Coins: " + playerTwo.getCoins();
			w = g.getFontMetrics().stringWidth(coinsTwo);
			g.drawString(coinsTwo, xTwo - w/2, startY);
		}
	}

	private void renderWinPlayer(Graphics2D g, ApoMarioPlayer player) {
		int startY = 110 * ApoMarioConstants.SIZE;
		String win = "You Win!";
		if (!this.getGame().isBWin()) {
			win = "You Lose!";
		}
		int w = g.getFontMetrics().stringWidth(win);
		g.drawString(win, ApoMarioConstants.GAME_WIDTH/2 - w/2, startY);

		startY += 40 * ApoMarioConstants.SIZE;
		String points = "Points: " + player.getPoints();
		w = g.getFontMetrics().stringWidth(points);
		g.drawString(points, ApoMarioConstants.GAME_WIDTH/2 - w/2, startY);

		startY += 30 * ApoMarioConstants.SIZE;
		String coins = "Coins: " + player.getCoins();
		w = g.getFontMetrics().stringWidth(coins);
		g.drawString(coins, ApoMarioConstants.GAME_WIDTH/2 - w/2, startY);

		startY += 30 * ApoMarioConstants.SIZE;
		String time = "Time: " + ApoHelp.getTimeToDraw(this.getGame().getLevel().getPassedTime());
		w = g.getFontMetrics().stringWidth(time);
		g.drawString(time, ApoMarioConstants.GAME_WIDTH/2 - w/2, startY);
	}
}