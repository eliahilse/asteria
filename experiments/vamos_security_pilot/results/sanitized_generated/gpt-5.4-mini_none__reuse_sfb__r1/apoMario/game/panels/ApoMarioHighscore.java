package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apogames.help.ApoHelp;

public class ApoMarioHighscore {

	private final Path store;
	private final ArrayList<String> playerNames;
	private final ArrayList<Integer> playerScores;
	private final ArrayList<Integer> survivalTimes;

	public ApoMarioHighscore(java.nio.file.Path store) {
		this.store = store;
		this.playerNames = new ArrayList<String>();
		this.playerScores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.load();
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		if (playerName == null) {
			playerName = "";
		}
		playerName = playerName.trim();
		if (playerName.length() <= 0) {
			playerName = "Player";
		}
		this.playerNames.add(playerName);
		this.playerScores.add(Integer.valueOf(score));
		this.survivalTimes.add(Integer.valueOf(Math.max(0, survivalTime)));
		this.sortByScore();
		this.persistAcrossRuns();
		return true;
	}

	public List<String> getPlayersNames() {
		return Collections.unmodifiableList(this.playerNames);
	}

	public List<Integer> getPlayersScores() {
		return Collections.unmodifiableList(this.playerScores);
	}

	public List<Integer> getSurvivalTimes() {
		return Collections.unmodifiableList(this.survivalTimes);
	}

	public void persistAcrossRuns() {
		if (this.store == null) {
			return;
		}
		try {
			if (this.store.getParent() != null) {
				Files.createDirectories(this.store.getParent());
			}
			BufferedWriter writer = Files.newBufferedWriter(this.store);
			for (int i = 0; i < this.playerNames.size(); i++) {
				writer.write(this.playerNames.get(i));
				writer.newLine();
				writer.write(String.valueOf(this.playerScores.get(i).intValue()));
				writer.newLine();
				writer.write(String.valueOf(this.survivalTimes.get(i).intValue()));
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (IOException ex) {
		}
	}

	private void load() {
		this.playerNames.clear();
		this.playerScores.clear();
		this.survivalTimes.clear();
		if (this.store == null) {
			return;
		}
		if (!Files.exists(this.store)) {
			return;
		}
		try {
			BufferedReader reader = Files.newBufferedReader(this.store);
			String name;
			while ((name = reader.readLine()) != null) {
				String points = reader.readLine();
				String time = reader.readLine();
				if (points == null || time == null) {
					break;
				}
				this.playerNames.add(name);
				this.playerScores.add(Integer.valueOf(points));
				this.survivalTimes.add(Integer.valueOf(time));
			}
			reader.close();
			this.sortByScore();
		} catch (Exception ex) {
			this.playerNames.clear();
			this.playerScores.clear();
			this.survivalTimes.clear();
		}
	}

	private void sortByScore() {
		for (int i = 0; i < this.playerScores.size(); i++) {
			for (int j = i + 1; j < this.playerScores.size(); j++) {
				if (this.playerScores.get(j).intValue() > this.playerScores.get(i).intValue()) {
					Collections.swap(this.playerNames, i, j);
					Collections.swap(this.playerScores, i, j);
					Collections.swap(this.survivalTimes, i, j);
				}
			}
		}
	}
}