package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import apoMario.level.ApoMarioLevel;
import apoMario.entity.ApoMarioPlayer;

public class ApoMarioHighscore {

	private static final int MAX_RECORDS = 50;
	private static final int MAX_NAME_LENGTH = 32;
	private static ApoMarioHighscore instance;

	private final Path storeFile;
	private final List<String> playerNames;
	private final List<Integer> playerScores;
	private final List<Integer> survivalTimes;

	public ApoMarioHighscore() {
		this(Paths.get("apomario_highscore.txt"));
	}

	public ApoMarioHighscore(Path store) {
		this.storeFile = store;
		this.playerNames = new ArrayList<String>();
		this.playerScores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		loadStore();
	}

	public static synchronized ApoMarioHighscore getInstance() {
		if (instance == null) {
			instance = new ApoMarioHighscore();
		}
		return instance;
	}

	private synchronized void loadStore() {
		this.playerNames.clear();
		this.playerScores.clear();
		this.survivalTimes.clear();

		if (storeFile == null || !Files.exists(storeFile)) {
			return;
		}

		try {
			List<String> lines = Files.readAllLines(storeFile, StandardCharsets.UTF_8);
			int count = 0;
			for (String line : lines) {
				if (count >= MAX_RECORDS) {
					break;
				}
				String trimmed = line.trim();
				if (trimmed.isEmpty()) {
					continue;
				}
				String[] parts = trimmed.split(",", 3);
				if (parts.length != 3) {
					continue;
				}
				String name = parts[0].trim();
				if (name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
					continue;
				}
				int score;
				int time;
				try {
					score = Integer.parseInt(parts[1].trim());
					time = Integer.parseInt(parts[2].trim());
				} catch (NumberFormatException e) {
					continue;
				}
				if (score < 0 || time < 0) {
					continue;
				}
				this.playerNames.add(name);
				this.playerScores.add(score);
				this.survivalTimes.add(time);
				count++;
			}
			sortScores();
		} catch (IOException e) {
		}
	}

	public synchronized void persistAcrossRuns() {
		if (storeFile == null) {
			return;
		}
		try {
			if (storeFile.getParent() != null && !Files.exists(storeFile.getParent())) {
				Files.createDirectories(storeFile.getParent());
			}
			try (BufferedWriter writer = Files.newBufferedWriter(storeFile, StandardCharsets.UTF_8)) {
				int limit = Math.min(this.playerNames.size(), MAX_RECORDS);
				for (int i = 0; i < limit; i++) {
					writer.write(this.playerNames.get(i) + "," + this.playerScores.get(i) + "," + this.survivalTimes.get(i));
					writer.newLine();
				}
			}
		} catch (IOException e) {
		}
	}

	public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
		if (score < 0 || survivalTime < 0 || playerName == null) {
			return false;
		}
		String name = playerName.trim();
		if (name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
			return false;
		}

		this.playerNames.add(name);
		this.playerScores.add(score);
		this.survivalTimes.add(survivalTime);

		sortScores();

		while (this.playerNames.size() > MAX_RECORDS) {
			int last = this.playerNames.size() - 1;
			this.playerNames.remove(last);
			this.playerScores.remove(last);
			this.survivalTimes.remove(last);
		}

		persistAcrossRuns();
		return true;
	}

	private void sortScores() {
		for (int i = 0; i < this.playerScores.size(); i++) {
			for (int j = i + 1; j < this.playerScores.size(); j++) {
				if (this.playerScores.get(j) > this.playerScores.get(i)) {
					int tempScore = this.playerScores.get(i);
					this.playerScores.set(i, this.playerScores.get(j));
					this.playerScores.set(j, tempScore);

					String tempName = this.playerNames.get(i);
					this.playerNames.set(i, this.playerNames.get(j));
					this.playerNames.set(j, tempName);

					int tempTime = this.survivalTimes.get(i);
					this.survivalTimes.set(i, this.survivalTimes.get(j));
					this.survivalTimes.set(j, tempTime);
				}
			}
		}
	}

	public synchronized List<String> getPlayersNames() {
		return new ArrayList<String>(this.playerNames);
	}

	public synchronized List<Integer> getPlayersScores() {
		return new ArrayList<Integer>(this.playerScores);
	}

	public synchronized List<Integer> getSurvivalTimes() {
		return new ArrayList<Integer>(this.survivalTimes);
	}

	public synchronized void recordRunEnd(ApoMarioLevel level) {
		if (level == null) {
			return;
		}
		int elapsedSeconds = level.getPassedTime() / 1000;
		if (elapsedSeconds < 0) {
			elapsedSeconds = 0;
		}
		ArrayList<ApoMarioPlayer> players = level.getPlayers();
		if (players != null && !players.isEmpty()) {
			ApoMarioPlayer p = players.get(0);
			if (p != null) {
				int score = p.getPoints();
				String name = "Player";
				if (p.getAi() != null && p.getAi().getTeamName() != null && !p.getAi().getTeamName().isEmpty()) {
					name = p.getAi().getTeamName();
				}
				storeRun(score, elapsedSeconds, name);
			}
		}
	}
}