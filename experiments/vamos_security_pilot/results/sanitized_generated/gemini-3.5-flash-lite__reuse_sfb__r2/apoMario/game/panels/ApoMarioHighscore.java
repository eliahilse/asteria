package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioModelMenu;
import org.apogames.entity.ApoAnimation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apogames.help.ApoHelp;
import org.apogames.help.ApoHighscore;

import apoMario.ApoMarioConstants;
import apoMario.ApoMarioImageContainer;
import apoMario.entity.ApoMarioPlayer;
import apoMario.game.ApoMarioPanel;
import apoMario.game.ApoMarioSearch;
import apoMario.game.ApoMarioSearchNode;
import apoMario.game.ApoMarioSearchRunner;
import apoMario.level.ApoMarioLevel;

public class ApoMarioHighscore extends ApoMarioModelMenu {

	public static final String FUNCTION_BACK = "backHighscore";
	public static final String LEFT = "highscore_left";
	public static final String RIGHT = "highscore_right";
	public static final int MAX_X = 10;

	private Path storePath;
	private ArrayList<String> names;
	private ArrayList<Integer> scores;
	private ArrayList<Integer> survivalTimes;
	private int curPos;
	private BufferedImage iBackground;

	public ApoMarioHighscore(Path store) {
		super(null);
		this.storePath = store;
		this.names = new ArrayList<String>();
		this.scores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.curPos = 0;
		this.loadRuns();
	}

	public ApoMarioHighscore(ApoMarioPanel game, Path store) {
		super(game);
		this.storePath = store;
		this.names = new ArrayList<String>();
		this.scores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.curPos = 0;
		this.loadRuns();
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		if (playerName == null || playerName.trim().isEmpty()) {
			playerName = "Player";
		}
		this.scores.add(score);
		this.survivalTimes.add(survivalTime);
		this.names.add(playerName);
		this.sortList();
		this.persistAcrossRuns();
		return true;
	}

	public List<String> getPlayersNames() {
		return this.names;
	}

	public List<Integer> getPlayersScores() {
		return this.scores;
	}

	public List<Integer> getSurvivalTimes() {
		return this.survivalTimes;
	}

	public void persistAcrossRuns() {
		if (this.storePath == null) {
			return;
		}
		try {
			File file = this.storePath.toFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < this.scores.size(); i++) {
				writer.write(this.scores.get(i) + "\n");
				writer.write(this.survivalTimes.get(i) + "\n");
				writer.write(this.names.get(i) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadRuns() {
		if (this.storePath == null) {
			return;
		}
		File file = this.storePath.toFile();
		if (!file.exists()) {
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String lineScore;
			while ((lineScore = reader.readLine()) != null) {
				String lineTime = reader.readLine();
				String lineName = reader.readLine();
				if (lineScore != null && lineTime != null && lineName != null) {
					try {
						int s = Integer.parseInt(lineScore.trim());
						int t = Integer.parseInt(lineTime.trim());
						this.scores.add(s);
						this.survivalTimes.add(t);
						this.names.add(lineName);
					} catch (NumberFormatException e) {
					}
				}
			}
			reader.close();
			this.sortList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sortList() {
		if (this.scores.size() <= 1) {
			return;
		}
		for (int i = 0; i < this.scores.size() - 1; i++) {
			for (int j = i + 1; j < this.scores.size(); j++) {
				if (this.scores.get(j) > this.scores.get(i)) {
					int tempScore = this.scores.get(i);
					this.scores.set(i, this.scores.get(j));
					this.scores.set(j, tempScore);

					int tempTime = this.survivalTimes.get(i);
					this.survivalTimes.set(i, this.survivalTimes.get(j));
					this.survivalTimes.set(j, tempTime);

					String tempName = this.names.get(i);
					this.names.set(i, this.names.get(j));
					this.names.set(j, tempName);
				}
			}
		}
	}

	public void recordRunEnd(ApoMarioLevel level) {
		if (level == null) {
			return;
		}
		ArrayList<ApoMarioPlayer> players = level.getPlayers();
		if (players != null && !players.isEmpty()) {
			ApoMarioPlayer p = players.get(0);
			int score = p.getPoints();
			int survivalTime = level.getPassedTime();
			if (survivalTime < 0) {
				survivalTime = 0;
			}
			String name = "Player";
			if (p.getAi() != null && p.getAi().getTeamName() != null) {
				name = p.getAi().getTeamName();
			} else if (this.getGame() != null && this.getGame().getOptions() != null) {
				String customName = this.getGame().getOptions().getMyTextfield().getCurString();
				if (customName != null && !customName.trim().isEmpty()) {
					name = customName.trim();
				}
			}
			this.storeRun(score, survivalTime, name);
		}
	}

	@Override
	public void init() {
		super.init();
		this.curPos = 0;
		if (this.iBackground == null) {
			this.makeBackground();
		}
	}

	@Override
	public void makeBackground() {
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
		}

		g.setColor(new Color(255, 255, 255, 220));
		g.fillRoundRect(40, 40, ApoMarioConstants.GAME_WIDTH - 80, ApoMarioConstants.GAME_HEIGHT - 80, 20, 20);
		g.setColor(Color.BLACK);
		g.drawRoundRect(40, 40, ApoMarioConstants.GAME_WIDTH - 80, ApoMarioConstants.GAME_HEIGHT - 80, 20, 20);

		g.dispose();
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
	}

	private void nextHighscore(int plus) {
		int realPlus = plus * ApoMarioHighscore.MAX_X;
		this.curPos += realPlus;
		if (this.curPos >= this.scores.size()) {
			this.curPos -= realPlus;
		} else if (this.curPos < 0) {
			this.curPos = 0;
		}
	}

	@Override
	public void keyButtonReleased(int button, char character) {
		if (button == KeyEvent.VK_ESCAPE) {
			if (this.getGame() != null) {
				this.getGame().setMenu();
			}
		} else if (button == KeyEvent.VK_LEFT) {
			this.nextHighscore(-1);
		} else if (button == KeyEvent.VK_RIGHT) {
			this.nextHighscore(+1);
		}
	}

	@Override
	public void mouseButtonFunction(String function) {
		if (function.equals(ApoMarioHighscore.FUNCTION_BACK)) {
			if (this.getGame() != null) {
				this.getGame().setMenu();
			}
		} else if (function.equals(ApoMarioHighscore.LEFT)) {
			this.nextHighscore(-1);
		} else if (function.equals(ApoMarioHighscore.RIGHT)) {
			this.nextHighscore(+1);
		}
	}

	@Override
	public void releasedEnter() {
		if (this.getGame() != null) {
			this.getGame().setMenu();
		}
	}

	@Override
	public void excecuteFunction() {
		String function = super.getExcecuteFunction();
		if (function != null && function.equals(ApoMarioHighscore.FUNCTION_BACK)) {
			if (this.getGame() != null) {
				this.getGame().setMenu();
			}
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
	}

	@Override
	public void render(Graphics2D g) {
		if (this.getIBackground() != null) {
			g.drawImage(this.getIBackground(), 0, 0, null);
		}

		g.setFont(ApoMarioConstants.FONT_STATISTICS);
		g.setColor(Color.BLACK);
		String title = "Highscore Board";
		int w = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (int)(ApoMarioConstants.GAME_WIDTH/2 - w/2), 70);

		final Font font = ApoMarioConstants.FONT_FPS;
		g.setFont(font);

		for (int i = this.curPos; i < this.scores.size() && i < ApoMarioHighscore.MAX_X + this.curPos; i++) {
			int y = 110;
			int row = i - this.curPos;

			g.setColor(Color.BLACK);
			g.setFont(font);
			String s = String.valueOf(i + 1) + ".";
			g.drawString(s, 60, y + row * 22);

			s = this.names.get(i);
			g.drawString(s, 100, y + row * 22);

			s = String.valueOf(this.scores.get(i)) + " pts";
			g.drawString(s, 280, y + row * 22);

			s = ApoHelp.getTimeToDraw(this.survivalTimes.get(i));
			g.drawString(s, 420, y + row * 22);
		}
		super.renderButtonsAndRunner(g);
	}
}