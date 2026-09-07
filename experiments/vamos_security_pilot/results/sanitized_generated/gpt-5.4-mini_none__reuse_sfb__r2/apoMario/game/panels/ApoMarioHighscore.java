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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apogames.help.ApoHelp;

import apoMario.ApoMarioConstants;

public class ApoMarioHighscore {

	private static final int DEFAULT_MAX_ENTRIES = 10;

	private final Path store;
	private final ArrayList<String> playersNames;
	private final ArrayList<Integer> playersScores;
	private final ArrayList<Integer> survivalTimes;

	private BufferedImage background;
	private ArrayList<HighscoreEntry> entries;

	public ApoMarioHighscore(Path store) {
		this.store = store;
		this.playersNames = new ArrayList<String>();
		this.playersScores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.entries = new ArrayList<HighscoreEntry>();
		this.load();
		this.syncLists();
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		String name = (playerName == null) ? "" : playerName.trim();
		if (name.length() <= 0) {
			name = "Player";
		}
		this.entries.add(new HighscoreEntry(score, survivalTime, name));
		Collections.sort(this.entries, new Comparator<HighscoreEntry>() {
			@Override
			public int compare(HighscoreEntry a, HighscoreEntry b) {
				if (a.score != b.score) {
					return Integer.valueOf(b.score).compareTo(Integer.valueOf(a.score));
				}
				if (a.survivalTime != b.survivalTime) {
					return Integer.valueOf(b.survivalTime).compareTo(Integer.valueOf(a.survivalTime));
				}
				return a.name.compareToIgnoreCase(b.name);
			}
		});
		if (this.entries.size() > DEFAULT_MAX_ENTRIES) {
			this.entries.subList(DEFAULT_MAX_ENTRIES, this.entries.size()).clear();
		}
		this.syncLists();
		this.save();
		return true;
	}

	public List<String> getPlayersNames() {
		return Collections.unmodifiableList(this.playersNames);
	}

	public List<Integer> getPlayersScores() {
		return Collections.unmodifiableList(this.playersScores);
	}

	public List<Integer> getSurvivalTimes() {
		return Collections.unmodifiableList(this.survivalTimes);
	}

	public void persistAcrossRuns() {
		this.save();
	}

	public void load() {
		this.entries.clear();
		if ((this.store == null) || (!Files.exists(this.store))) {
			return;
		}
		BufferedReader reader = null;
		try {
			reader = Files.newBufferedReader(this.store, StandardCharsets.UTF_8);
			String line;
			while ((line = reader.readLine()) != null) {
				String[] split = line.split("\t", 3);
				if (split.length < 3) {
					continue;
				}
				try {
					int score = Integer.parseInt(split[0]);
					int survivalTime = Integer.parseInt(split[1]);
					String name = split[2];
					this.entries.add(new HighscoreEntry(score, survivalTime, name));
				} catch (NumberFormatException ex) {
				}
			}
		} catch (IOException ex) {
			this.entries.clear();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}
		Collections.sort(this.entries, new Comparator<HighscoreEntry>() {
			@Override
			public int compare(HighscoreEntry a, HighscoreEntry b) {
				if (a.score != b.score) {
					return Integer.valueOf(b.score).compareTo(Integer.valueOf(a.score));
				}
				if (a.survivalTime != b.survivalTime) {
					return Integer.valueOf(b.survivalTime).compareTo(Integer.valueOf(a.survivalTime));
				}
				return a.name.compareToIgnoreCase(b.name);
			}
		});
		if (this.entries.size() > DEFAULT_MAX_ENTRIES) {
			this.entries.subList(DEFAULT_MAX_ENTRIES, this.entries.size()).clear();
		}
	}

	public void render(Graphics2D g) {
		if (this.background == null) {
			this.background = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ApoMarioConstants.GAME_WIDTH, ApoMarioConstants.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D bg = (Graphics2D)this.background.getGraphics();
			bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			bg.setColor(new Color(246, 246, 246));
			bg.fillRect(0, 0, this.background.getWidth(), this.background.getHeight());
			bg.setColor(new Color(255, 255, 255, 220));
			bg.fillRoundRect(20, 20, this.background.getWidth() - 40, this.background.getHeight() - 40, 20, 20);
			bg.setColor(Color.BLACK);
			bg.drawRoundRect(20, 20, this.background.getWidth() - 40, this.background.getHeight() - 40, 20, 20);
			bg.dispose();
		}
		g.drawImage(this.background, 0, 0, null);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(ApoMarioConstants.FONT_MENU);
		g.setColor(Color.BLACK);
		String title = "Highscore";
		int w = g.getFontMetrics().stringWidth(title);
		g.drawString(title, ApoMarioConstants.GAME_WIDTH / 2 - w / 2, 55);

		g.setFont(ApoMarioConstants.FONT_STATISTICS);
		int startY = 95;
		int lineHeight = g.getFontMetrics().getHeight() + 6;
		String header = "Rank   Score   Time   Name";
		g.drawString(header, 60, startY - lineHeight);

		for (int i = 0; i < this.entries.size(); i++) {
			HighscoreEntry entry = this.entries.get(i);
			int y = startY + i * lineHeight;
			String rank = String.valueOf(i + 1);
			String score = String.valueOf(entry.score);
			String time = ApoHelp.getTimeToDraw(entry.survivalTime);
			g.drawString(rank, 60, y);
			g.drawString(score, 120, y);
			g.drawString(time, 240, y);
			g.drawString(entry.name, 330, y);
		}
	}

	private void syncLists() {
		this.playersNames.clear();
		this.playersScores.clear();
		this.survivalTimes.clear();
		for (HighscoreEntry entry : this.entries) {
			this.playersNames.add(entry.name);
			this.playersScores.add(entry.score);
			this.survivalTimes.add(entry.survivalTime);
		}
	}

	private void save() {
		if (this.store == null) {
			return;
		}
		BufferedWriter writer = null;
		try {
			Path parent = this.store.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			writer = Files.newBufferedWriter(this.store, StandardCharsets.UTF_8);
			for (HighscoreEntry entry : this.entries) {
				writer.write(String.valueOf(entry.score));
				writer.write('\t');
				writer.write(String.valueOf(entry.survivalTime));
				writer.write('\t');
				writer.write(entry.name == null ? "" : entry.name);
				writer.newLine();
			}
			writer.flush();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	private static final class HighscoreEntry {
		private final int score;
		private final int survivalTime;
		private final String name;

		private HighscoreEntry(int score, int survivalTime, String name) {
			this.score = score;
			this.survivalTime = survivalTime;
			this.name = name;
		}
	}
}