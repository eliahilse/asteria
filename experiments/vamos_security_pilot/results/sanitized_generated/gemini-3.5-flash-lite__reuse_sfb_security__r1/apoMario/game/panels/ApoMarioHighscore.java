package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ApoMarioHighscore {

	private static final int MAX_RECORDS = 100;
	private static final int MAX_NAME_LENGTH = 32;

	private final Path store;
	private final List<String> playerNames;
	private final List<Integer> playerScores;
	private final List<Integer> survivalTimes;

	public ApoMarioHighscore(Path store) {
		this.store = store != null ? store : Paths.get("apomario_highscore.txt");
		this.playerNames = new ArrayList<>();
		this.playerScores = new ArrayList<>();
		this.survivalTimes = new ArrayList<>();
		load();
	}

	public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
		if (score < 0 || survivalTime < 0) {
			return false;
		}
		if (playerName == null) {
			return false;
		}
		String trimmed = playerName.trim();
		if (trimmed.isEmpty() || trimmed.length() > MAX_NAME_LENGTH) {
			return false;
		}
		// Sanitize name to prevent structural corruption in text file
		String sanitized = trimmed.replace("|", "_").replace("\n", "").replace("\r", "");
		if (sanitized.isEmpty()) {
			return false;
		}

		this.playerNames.add(sanitized);
		this.playerScores.add(score);
		this.survivalTimes.add(survivalTime);

		sortAndTrim();
		persistAcrossRuns();
		return true;
	}

	public synchronized List<String> getPlayersNames() {
		return new ArrayList<>(this.playerNames);
	}

	public synchronized List<Integer> getPlayersScores() {
		return new ArrayList<>(this.playerScores);
	}

	public synchronized List<Integer> getSurvivalTimes() {
		return new ArrayList<>(this.survivalTimes);
	}

	public synchronized void persistAcrossRuns() {
		if (this.store == null) {
			return;
		}
		try {
			Path parent = this.store.getParent();
			if (parent != null && !Files.exists(parent)) {
				Files.createDirectories(parent);
			}
			try (BufferedWriter writer = Files.newBufferedWriter(this.store)) {
				int size = Math.min(this.playerNames.size(), MAX_RECORDS);
				for (int i = 0; i < size; i++) {
					writer.write(this.playerScores.get(i) + "|" + this.survivalTimes.get(i) + "|" + this.playerNames.get(i));
					writer.newLine();
				}
			}
		} catch (IOException e) {
			// Fail gracefully without crashing
		}
	}

	private synchronized void load() {
		this.playerNames.clear();
		this.playerScores.clear();
		this.survivalTimes.clear();

		if (this.store == null || !Files.exists(this.store)) {
			return;
		}

		try (BufferedReader reader = Files.newBufferedReader(this.store)) {
			String line;
			int count = 0;
			while ((line = reader.readLine()) != null && count < MAX_RECORDS) {
				String[] parts = line.split("\\|", 3);
				if (parts.length == 3) {
					try {
						int score = Integer.parseInt(parts[0].trim());
						int time = Integer.parseInt(parts[1].trim());
						String name = parts[2].trim();
						if (score >= 0 && time >= 0 && !name.isEmpty() && name.length() <= MAX_NAME_LENGTH) {
							this.playerScores.add(score);
							this.survivalTimes.add(time);
							this.playerNames.add(name);
							count++;
						}
					} catch (NumberFormatException e) {
						// Skip malformed line
					}
				}
			}
			sortAndTrim();
		} catch (IOException e) {
			// Reset on corrupt or unreadable file
			this.playerNames.clear();
			this.playerScores.clear();
			this.survivalTimes.clear();
		}
	}

	private void sortAndTrim() {
		List<Record> records = new ArrayList<>();
		int size = Math.min(this.playerNames.size(), Math.min(this.playerScores.size(), this.survivalTimes.size()));
		for (int i = 0; i < size; i++) {
			records.add(new Record(this.playerNames.get(i), this.playerScores.get(i), this.survivalTimes.get(i)));
		}

		Collections.sort(records, new Comparator<Record>() {
			@Override
			public int compare(Record r1, Record r2) {
				return Integer.compare(r2.score, r1.score);
			}
		});

		this.playerNames.clear();
		this.playerScores.clear();
		this.survivalTimes.clear();

		int limit = Math.min(records.size(), MAX_RECORDS);
		for (int i = 0; i < limit; i++) {
			Record r = records.get(i);
			this.playerNames.add(r.name);
			this.playerScores.add(r.score);
			this.survivalTimes.add(r.time);
		}
	}

	private static class Record {
		String name;
		int score;
		int time;

		Record(String name, int score, int time) {
			this.name = name;
			this.score = score;
			this.time = time;
		}
	}
}