package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioModelMenu;
import org.apogames.entity.ApoAnimation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import apoMario.ApoMarioConstants;
import apoMario.entity.ApoMarioPlayer;
import apoMario.game.ApoMarioPanel;
import apoMario.level.ApoMarioLevel;

public class ApoMarioHighscore extends ApoMarioModelMenu {

	public static final String FUNCTION_HIGHSCORE_BACK = "backHighscore";
	public static final String FUNCTION_HIGHSCORE_MENU = "highscore";

	private Path storePath;
	private List<String> playerNames;
	private List<Integer> playerScores;
	private List<Integer> survivalTimes;

	private static final class ScoreEntry {
		String name;
		int score;
		int survivalTime;

		ScoreEntry(String name, int score, int survivalTime) {
			this.name = name;
			this.score = score;
			this.survivalTime = survivalTime;
		}
	}

	private List<ScoreEntry> entries;

	public ApoMarioHighscore(ApoMarioPanel game) {
		super(game);
		this.storePath = new File("apomario_highscores.txt").toPath();
		this.playerNames = new ArrayList<String>();
		this.playerScores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.entries = new ArrayList<ScoreEntry>();
		loadHighscores();
	}

	public ApoMarioHighscore(Path store) {
		this(null);
		this.storePath = store;
		loadHighscores();
	}

	private void loadHighscores() {
		this.entries.clear();
		if (this.storePath != null) {
			File file = this.storePath.toFile();
			if (file.exists()) {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(file));
					String line;
					while ((line = reader.readLine()) != null) {
						String[] parts = line.split(";", 3);
						if (parts.length == 3) {
							String name = parts[0];
							int score = Integer.parseInt(parts[1]);
							int time = Integer.parseInt(parts[2]);
							this.entries.add(new ScoreEntry(name, score, time));
						}
					}
				} catch (Exception e) {
					// Fallback to clean state if corrupt
					this.entries.clear();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}
		sortAndSyncLists();
	}

	private void sortAndSyncLists() {
		Collections.sort(this.entries, new Comparator<ScoreEntry>() {
			@Override
			public int compare(ScoreEntry o1, ScoreEntry o2) {
				return Integer.compare(o2.score, o1.score);
			}
		});
		this.playerNames.clear();
		this.playerScores.clear();
		this.survivalTimes.clear();
		for (ScoreEntry entry : this.entries) {
			this.playerNames.add(entry.name);
			this.playerScores.add(entry.score);
			this.survivalTimes.add(entry.survivalTime);
		}
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		if (playerName == null || playerName.trim().isEmpty()) {
			playerName = "Player";
		}
		this.entries.add(new ScoreEntry(playerName, score, survivalTime));
		sortAndSyncLists();
		persistAcrossRuns();
		return true;
	}

	public List<String> getPlayersNames() {
		return this.playerNames;
	}

	public List<Integer> getPlayersScores() {
		return this.playerScores;
	}

	public List<Integer> getSurvivalTimes() {
		return this.survivalTimes;
	}

	public void persistAcrossRuns() {
		if (this.storePath != null) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(this.storePath.toFile()));
				for (ScoreEntry entry : this.entries) {
					writer.write(entry.name + ";" + entry.score + ";" + entry.survivalTime);
					writer.newLine();
				}
			} catch (IOException e) {
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	public void recordRunEnd(ApoMarioLevel level) {
		if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) {
			return;
		}
		ApoMarioPlayer player = level.getPlayers().get(0);
		int score = player.getPoints();
		int totalSecs = level.getPassedTime() / 1000;
		if (totalSecs < 0) {
			totalSecs = 0;
		}
		int survivalTimeSecs = totalSecs;
		String name = "Player";
		if (player.getAi() != null && player.getAi().getTeamName() != null) {
			name = player.getAi().getTeamName();
		}
		storeRun(score, survivalTimeSecs, name);
	}

	@Override
	public void init() {
		super.init();
		loadHighscores();
	}

	@Override
	public void makeBackground() {
		this.setIBackground(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB));
		Graphics2D g = (Graphics2D)(this.getIBackground().getGraphics());
		g.setColor(new Color(30, 30, 60));
		g.fillRect(0, 0, ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT);
		g.dispose();
	}

	@Override
	public void makeBackgroundAnimation() {
		this.setBackgroundAnimation(new ArrayList<org.apogames.entity.ApoAnimation>());
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
			if (this.getGame() != null) {
				this.getGame().setMenu();
			}
		}
		this.keyButtonReleasedArrowAndEnter(button, character);
	}

	@Override
	public void mouseButtonFunction(String function) {
		if (function.equals(ApoMarioHighscore.FUNCTION_HIGHSCORE_BACK)) {
			if (this.getGame() != null) {
				this.getGame().setMenu();
			}
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
		if (this.getGame() != null) {
			this.getGame().setMenu();
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
		g.setFont(ApoMarioConstants.FONT_MENU);
		g.setColor(Color.WHITE);
		String title = "HIGHSCORES";
		int tw = g.getFontMetrics().stringWidth(title);
		g.drawString(title, ApoMarioConstants.GAME_WIDTH / 2 - tw / 2, 40);

		g.setFont(ApoMarioConstants.FONT_STATISTICS);
		int startY = 80;
		int rowHeight = 22;
		int maxDisplay = 8;
		for (int i = 0; i < Math.min(this.entries.size(), maxDisplay); i++) {
			ScoreEntry entry = this.entries.get(i);
			int mm = entry.survivalTime / 60;
			int ss = entry.survivalTime % 60;
			String timeStr = String.format("%02d:%02d", mm, ss);
			String line = (i + 1) + ". " + entry.name + " - " + entry.score + " pts (" + timeStr + ")";
			g.drawString(line, 40, startY + i * rowHeight);
		}

		super.renderButtonsAndRunner(g);
	}
}