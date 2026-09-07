package apoMario.game.panels;

import apoMario.game.ApoMarioSearch;
import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioModelMenu;
import org.apogames.entity.ApoAnimation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apogames.help.ApoHelp;

import apoMario.ApoMarioConstants;
import apoMario.entity.ApoMarioPlayer;
import apoMario.game.ApoMarioPanel;
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
	private BufferedImage iBackground;
	private int curPos;

	public ApoMarioHighscore(ApoMarioPanel game) {
		super(game);
		this.names = new ArrayList<String>();
		this.scores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.curPos = 0;
	}

	public ApoMarioHighscore(Path store) {
		super(null);
		this.storePath = store;
		this.names = new ArrayList<String>();
		this.scores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.curPos = 0;
		loadFromFile();
	}

	@Override
	public void init() {
		super.init();
		loadFromFile();
		if (this.iBackground == null) {
			this.makeBackground();
		}
	}

	private void loadFromFile() {
		this.names.clear();
		this.scores.clear();
		this.survivalTimes.clear();
		File file = null;
		if (this.storePath != null) {
			file = this.storePath.toFile();
		} else {
			file = new File("apomario_highscore.txt");
		}
		if (file.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					String name = line;
					String scoreStr = reader.readLine();
					String timeStr = reader.readLine();
					if (scoreStr != null && timeStr != null) {
						this.names.add(name);
						this.scores.add(Integer.valueOf(scoreStr));
						this.survivalTimes.add(Integer.valueOf(timeStr));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		sortScores();
	}

	public void persistAcrossRuns() {
		File file = null;
		if (this.storePath != null) {
			file = this.storePath.toFile();
		} else {
			file = new File("apomario_highscore.txt");
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < this.names.size(); i++) {
				writer.write(this.names.get(i));
				writer.newLine();
				writer.write(String.valueOf(this.scores.get(i)));
				writer.newLine();
				writer.write(String.valueOf(this.survivalTimes.get(i)));
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		if (playerName == null || playerName.trim().length() == 0) {
			playerName = "Player";
		}
		this.names.add(playerName);
		this.scores.add(score);
		this.survivalTimes.add(survivalTime);
		sortScores();
		persistAcrossRuns();
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

	private void sortScores() {
		for (int i = 0; i < this.scores.size(); i++) {
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
		if (level == null || level.getPlayers() == null || level.getPlayers().size() == 0) {
			return;
		}
		ApoMarioPlayer p = level.getPlayers().get(0);
		int score = p.getPoints();
		int timeMs = level.getPassedTime();
		int timeSec = timeMs / 1000;
		String name = "Mario";
		if (p.getTeamName() != null && p.getTeamName().trim().length() > 0) {
			name = p.getTeamName();
		} else if (p.getAuthor() != null && p.getAuthor().trim().length() > 0) {
			name = p.getAuthor();
		}
		storeRun(score, timeSec, name);
	}

	private void nextHighscore(int plus) {
		int realPlus = plus * ApoMarioHighscore.MAX_X;
		this.curPos += realPlus;
		if (this.curPos >= this.names.size()) {
			this.curPos -= realPlus;
		} else if (this.curPos < 0) {
			this.curPos = 0;
		}
	}

	@Override
	public void makeBackground() {
		this.setIBackground(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB));
		Graphics2D g = (Graphics2D)(this.getIBackground().getGraphics());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (this.getGame() != null) {
			this.getGame().renderBackground(g);
		}
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
		if (this.getIBackground() != null) {
			g.drawImage(this.getIBackground(), 0, 0, null);
		}
		g.setFont(ApoMarioConstants.FONT_STATISTICS);
		g.setColor(Color.BLACK);
		String s = "Highscores (" + this.names.size() + " total)";
		int w = g.getFontMetrics().stringWidth(s);
		g.drawString(s, (int)(ApoMarioConstants.GAME_WIDTH/2 - w/2), 40);

		for (int i = this.curPos; i < this.names.size() && i < ApoMarioHighscore.MAX_X + this.curPos; i++) {
			int y = 90;
			g.setColor(Color.BLACK);
			g.setFont(ApoMarioConstants.FONT_STATISTICS);
			s = String.valueOf(i + 1) + ".";
			g.drawString(s, 50, y + (i - this.curPos) * 25);

			s = this.names.get(i);
			g.drawString(s, 90, y + (i - this.curPos) * 25);

			s = String.valueOf(this.scores.get(i));
			g.drawString(s, 280, y + (i - this.curPos) * 25);

			int timeSec = this.survivalTimes.get(i);
			int mm = timeSec / 60;
			int ss = timeSec % 60;
			s = (mm < 10 ? "0" + mm : String.valueOf(mm)) + ":" + (ss < 10 ? "0" + ss : String.valueOf(ss));
			g.drawString(s, 400, y + (i - this.curPos) * 25);
		}
		super.renderButtonsAndRunner(g);
	}
}