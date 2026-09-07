package apoMario.game.panels;

import apoMario.game.ApoMarioSearch;
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
import apoMario.level.ApoMarioLevel;
import apoMario.game.ApoMarioPanel;

public class ApoMarioHighscore extends ApoMarioModelMenu {

	public static final String FUNCTION_HIGHSCORE_BACK = "backHighscore";
	public static final String FUNCTION_HIGHSCORE_CLICK = "highscoreClick";

	private Path storeFile;
	private List<HighScoreEntry> entries;

	public ApoMarioHighscore(ApoMarioPanel game) {
		super(game);
		this.entries = new ArrayList<HighScoreEntry>();
		this.storeFile = new File("apomario_highscore.txt").toPath();
		loadPersistedData();
	}

	public ApoMarioHighscore(Path store) {
		super(null);
		this.storeFile = store;
		this.entries = new ArrayList<HighScoreEntry>();
		loadPersistedData();
	}

	private void loadPersistedData() {
		if (storeFile == null) {
			return;
		}
		File file = storeFile.toFile();
		if (!file.exists()) {
			return;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");
				if (parts.length >= 3) {
					String name = parts[0];
					int score = Integer.parseInt(parts[1]);
					int time = Integer.parseInt(parts[2]);
					entries.add(new HighScoreEntry(name, score, time));
				}
			}
		} catch (Exception e) {
			// ignore or reset
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		sortEntries();
	}

	public void persistAcrossRuns() {
		if (storeFile == null) {
			return;
		}
		File file = storeFile.toFile();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for (HighScoreEntry entry : entries) {
				writer.write(entry.name + ";" + entry.score + ";" + entry.survivalTime);
				writer.newLine();
			}
		} catch (Exception e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void sortEntries() {
		Collections.sort(entries, new Comparator<HighScoreEntry>() {
			@Override
			public int compare(HighScoreEntry o1, HighScoreEntry o2) {
				return Integer.compare(o2.score, o1.score);
			}
		});
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		if (playerName == null || playerName.trim().isEmpty()) {
			playerName = "Player";
		}
		entries.add(new HighScoreEntry(playerName.trim(), score, survivalTime));
		sortEntries();
		persistAcrossRuns();
		return true;
	}

	public void recordRunEnd(ApoMarioLevel level) {
		if (level == null || level.getPlayers() == null || level.getPlayers().isEmpty()) {
			return;
		}
		ApoMarioPlayer player = level.getPlayers().get(0);
		int score = player.getPoints();
		int passedTimeSeconds = level.getPassedTime() / 1000;
		if (passedTimeSeconds < 0) {
			passedTimeSeconds = 0;
		}
		String name = "Player";
		if (player.getAi() != null && player.getAi().getTeamName() != null) {
			name = player.getAi().getTeamName();
		}
		storeRun(score, passedTimeSeconds, name);
	}

	public List<String> getPlayersNames() {
		List<String> list = new ArrayList<String>();
		for (HighScoreEntry entry : entries) {
			list.add(entry.name);
		}
		return list;
	}

	public List<Integer> getPlayersScores() {
		List<Integer> list = new ArrayList<Integer>();
		for (HighScoreEntry entry : entries) {
			list.add(entry.score);
		}
		return list;
	}

	public List<Integer> getSurvivalTimes() {
		List<Integer> list = new ArrayList<Integer>();
		for (HighScoreEntry entry : entries) {
			list.add(entry.survivalTime);
		}
		return list;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void makeBackground() {
		this.setIBackground(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB));
		Graphics2D g = (Graphics2D)(this.getIBackground().getGraphics());
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT);
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
	public void keyButtonReleased(int button, char character) {
		if (button == KeyEvent.VK_ESCAPE) {
			if (this.getGame() != null) {
				this.getGame().setMenu();
			}
		}
	}

	@Override
	public void mouseButtonFunction(String function) {
		if (function != null && function.equals(FUNCTION_HIGHSCORE_BACK)) {
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
		super.thinkRunnerAndAnimation(delta);
	}

	@Override
	public void render(Graphics2D g) {
		super.renderBackgroundAndAnimation(g);
		g.setFont(ApoMarioConstants.FONT_STATISTICS);
		g.setColor(Color.WHITE);
		String title = "HIGHSCORES";
		int w = g.getFontMetrics().stringWidth(title);
		g.drawString(title, ApoMarioConstants.GAME_WIDTH / 2 - w / 2, 30);

		g.setFont(ApoMarioConstants.FONT_FPS);
		int startY = 60;
		int rank = 1;
		for (int i = 0; i < entries.size() && i < 8; i++) {
			HighScoreEntry entry = entries.get(i);
			int minutes = entry.survivalTime / 60;
			int seconds = entry.survivalTime % 60;
			String timeFormatted = String.format("%02d:%02d", minutes, seconds);
			String line = rank + ". " + entry.name + " - Score: " + entry.score + " - Time: " + timeFormatted;
			g.drawString(line, 50, startY + i * 20);
			rank++;
		}
		super.renderButtonsAndRunner(g);
	}

	private static class HighScoreEntry {
		String name;
		int score;
		int survivalTime;

		public HighScoreEntry(String name, int score, int survivalTime) {
			this.name = name;
			this.score = score;
			this.survivalTime = survivalTime;
		}
	}
}