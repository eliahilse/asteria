package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apogames.help.ApoHelp;

import apoMario.ApoMarioConstants;
import apoMario.ApoMarioImageContainer;

/**
 * Highscore board for ApoMario.
 */
public class ApoMarioHighscore {

	private static final int MAX_ENTRIES = 50;
	private static final int MAX_NAME_LENGTH = 24;

	private final Path store;
	private final ArrayList<String> playersNames;
	private final ArrayList<Integer> playersScores;
	private final ArrayList<Integer> survivalTimes;

	private BufferedImage iBackground;
	private final Font fontTitle;
	private final Font fontEntries;

	public ApoMarioHighscore(Path store) {
		this.store = store;
		this.playersNames = new ArrayList<String>();
		this.playersScores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.fontTitle = ApoMarioConstants.FONT_MENU != null ? ApoMarioConstants.FONT_MENU : new Font(Font.SANS_SERIF, Font.BOLD, 18);
		this.fontEntries = ApoMarioConstants.FONT_STATISTICS != null ? ApoMarioConstants.FONT_STATISTICS : new Font(Font.SANS_SERIF, Font.PLAIN, 14);
		this.load();
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		if (score < 0 || survivalTime < 0 || playerName == null) {
			return false;
		}
		String name = playerName.trim();
		if (name.length() <= 0 || name.length() > MAX_NAME_LENGTH) {
			return false;
		}
		this.playersNames.add(name);
		this.playersScores.add(score);
		this.survivalTimes.add(survivalTime);
		this.sortDescending();
		this.trim();
		this.save();
		return true;
	}

	public List<String> getPlayersNames() {
		return this.playersNames;
	}

	public List<Integer> getPlayersScores() {
		return this.playersScores;
	}

	public List<Integer> getSurvivalTimes() {
		return this.survivalTimes;
	}

	public void persistAcrossRuns() {
		this.save();
	}

	public void render(Graphics2D g) {
		if (this.iBackground == null) {
			this.makeBackground();
		}
		if (this.iBackground != null) {
			g.drawImage(this.iBackground, 0, 0, null);
		}
	}

	private void makeBackground() {
		this.iBackground = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)this.iBackground.getGraphics();

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
			g.drawImage(iMenuTile.getSubimage(11 * size, 4 * size, size / 2, size / 2), x * size, waterHeight * size, null);
			g.drawImage(iMenuTile.getSubimage(11 * size, 4 * size, size / 2, size / 2), x * size + size / 2, waterHeight * size, null);
		}

		g.setColor(new Color(255, 255, 255, 200));
		g.fillRoundRect(ApoMarioConstants.GAME_WIDTH / 8 - size, (int)(ApoMarioConstants.GAME_HEIGHT * 1 / 4 - 3 * size), ApoMarioConstants.GAME_WIDTH * 3 / 4 + size * 2, (int)(size * 8.4), 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(ApoMarioConstants.GAME_WIDTH / 8 - size, (int)(ApoMarioConstants.GAME_HEIGHT * 1 / 4 - 3 * size), ApoMarioConstants.GAME_WIDTH * 3 / 4 + size * 2, (int)(size * 8.4), 10, 10);

		g.setFont(this.fontTitle);
		String title = "Highscore";
		int w = g.getFontMetrics().stringWidth(title);
		g.drawString(title, ApoMarioConstants.GAME_WIDTH / 2 - w / 2, (int)(ApoMarioConstants.GAME_HEIGHT * 1 / 4 - 1.5f * size));

		g.setFont(this.fontEntries);
		int startY = (int)(ApoMarioConstants.GAME_HEIGHT * 1 / 4 + 10);
		int rowHeight = g.getFontMetrics().getHeight() + 4;

		String header = "Points   Time     Name";
		g.drawString(header, ApoMarioConstants.GAME_WIDTH / 8 + size, startY);

		for (int i = 0; i < this.playersNames.size(); i++) {
			int y = startY + (i + 1) * rowHeight;
			String score = String.valueOf(this.playersScores.get(i));
			String time = ApoHelp.getTimeToDraw(this.survivalTimes.get(i));
			String name = this.playersNames.get(i);

			g.drawString(String.valueOf(i + 1) + ".", ApoMarioConstants.GAME_WIDTH / 8 + size, y);
			g.drawString(score, ApoMarioConstants.GAME_WIDTH / 8 + size + 80, y);
			g.drawString(time, ApoMarioConstants.GAME_WIDTH / 8 + size + 170, y);
			g.drawString(name, ApoMarioConstants.GAME_WIDTH / 8 + size + 280, y);
			if (i >= 11) {
				break;
			}
		}

		g.dispose();
	}

	private void load() {
		this.playersNames.clear();
		this.playersScores.clear();
		this.survivalTimes.clear();
		if (this.store == null || !Files.exists(this.store)) {
			return;
		}
		try (BufferedReader reader = Files.newBufferedReader(this.store)) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() <= 0) {
					continue;
				}
				String[] parts = line.split("\t", -1);
				if (parts.length != 3) {
					continue;
				}
				int score;
				int time;
				try {
					score = Integer.parseInt(parts[0].trim());
					time = Integer.parseInt(parts[1].trim());
				} catch (NumberFormatException ex) {
					continue;
				}
				String name = parts[2].trim();
				if (score < 0 || time < 0 || name.length() <= 0 || name.length() > MAX_NAME_LENGTH) {
					continue;
				}
				this.playersScores.add(score);
				this.survivalTimes.add(time);
				this.playersNames.add(name);
				if (this.playersNames.size() >= MAX_ENTRIES) {
					break;
				}
			}
			this.sortDescending();
			this.trim();
		} catch (IOException ex) {
			this.playersNames.clear();
			this.playersScores.clear();
			this.survivalTimes.clear();
		}
	}

	private void save() {
		if (this.store == null) {
			return;
		}
		try {
			Path parent = this.store.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			try (BufferedWriter writer = Files.newBufferedWriter(this.store)) {
				for (int i = 0; i < this.playersNames.size(); i++) {
					writer.write(String.valueOf(this.playersScores.get(i)));
					writer.write('\t');
					writer.write(String.valueOf(this.survivalTimes.get(i)));
					writer.write('\t');
					writer.write(this.playersNames.get(i));
					writer.newLine();
				}
			}
		} catch (IOException ex) {
		}
	}

	private void sortDescending() {
		for (int i = 1; i < this.playersScores.size(); i++) {
			int score = this.playersScores.get(i);
			int time = this.survivalTimes.get(i);
			String name = this.playersNames.get(i);
			int j = i - 1;
			while (j >= 0 && this.playersScores.get(j) < score) {
				if (j + 1 < this.playersScores.size()) {
					this.playersScores.set(j + 1, this.playersScores.get(j));
					this.survivalTimes.set(j + 1, this.survivalTimes.get(j));
					this.playersNames.set(j + 1, this.playersNames.get(j));
				}
				j--;
			}
			this.playersScores.set(j + 1, score);
			this.survivalTimes.set(j + 1, time);
			this.playersNames.set(j + 1, name);
		}
	}

	private void trim() {
		while (this.playersNames.size() > MAX_ENTRIES) {
			int last = this.playersNames.size() - 1;
			this.playersNames.remove(last);
			this.playersScores.remove(last);
			this.survivalTimes.remove(last);
		}
	}
}