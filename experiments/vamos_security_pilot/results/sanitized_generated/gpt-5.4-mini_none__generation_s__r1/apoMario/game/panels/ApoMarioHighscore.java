package apoMario.game.panels;

import apoMario.game.panels.ApoMarioHighscore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import apoMario.ApoMarioConstants;
import apoMario.level.ApoMarioLevel;
import apoMario.entity.ApoMarioPlayer;

public class ApoMarioHighscore {

	private static final int MAGIC = 0x41504853;
	private static final int VERSION = 1;
	private static final int MAX_ENTRIES = 10;

	private final Path store;
	private final ArrayList<String> playersNames;
	private final ArrayList<Integer> playersScores;
	private final ArrayList<Integer> survivalTimes;

	public ApoMarioHighscore(Path store) {
		this.store = store;
		this.playersNames = new ArrayList<String>();
		this.playersScores = new ArrayList<Integer>();
		this.survivalTimes = new ArrayList<Integer>();
		this.load();
	}

	public boolean storeRun(int score, int survivalTime, String playerName) {
		if (playerName == null || playerName.trim().length() <= 0) {
			playerName = "Player";
		}
		this.playersNames.add(playerName.trim());
		this.playersScores.add(score);
		this.survivalTimes.add(survivalTime);
		this.sortEntries();
		while (this.playersNames.size() > MAX_ENTRIES) {
			this.playersNames.remove(this.playersNames.size() - 1);
			this.playersScores.remove(this.playersScores.size() - 1);
			this.survivalTimes.remove(this.survivalTimes.size() - 1);
		}
		this.persistAcrossRuns();
		return true;
	}

	public List<String> getPlayersNames() {
		return Collections.unmodifiableList(new ArrayList<String>(this.playersNames));
	}

	public List<Integer> getPlayersScores() {
		return Collections.unmodifiableList(new ArrayList<Integer>(this.playersScores));
	}

	public List<Integer> getSurvivalTimes() {
		return Collections.unmodifiableList(new ArrayList<Integer>(this.survivalTimes));
	}

	public void persistAcrossRuns() {
		if (this.store == null) {
			return;
		}
		try {
			Path parent = this.store.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			DataOutputStream data = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(this.store)));
			try {
				data.writeInt(MAGIC);
				data.writeInt(VERSION);
				data.writeInt(this.playersNames.size());
				for (int i = 0; i < this.playersNames.size(); i++) {
					data.writeUTF(this.playersNames.get(i));
					data.writeInt(this.playersScores.get(i));
					data.writeInt(this.survivalTimes.get(i));
				}
			} finally {
				data.close();
			}
		} catch (IOException ex) {
		}
	}

	public void recordRunEnd(ApoMarioLevel level) {
		if (level == null || level.getPlayers() == null || level.getPlayers().size() <= 0) {
			return;
		}
		ApoMarioPlayer player = level.getPlayers().get(0);
		if (player == null) {
			return;
		}
		String name = player.getTeamName();
		if (name == null || name.trim().length() <= 0) {
			name = player.getAuthor();
		}
		if (name == null || name.trim().length() <= 0) {
			name = "Player";
		}
		int score = player.getPoints();
		int survivalTime = level.getPassedTime();
		this.storeRun(score, survivalTime, name);
	}

	private void load() {
		this.playersNames.clear();
		this.playersScores.clear();
		this.survivalTimes.clear();
		if (this.store == null || !Files.exists(this.store)) {
			return;
		}
		try {
			DataInputStream data = new DataInputStream(new BufferedInputStream(Files.newInputStream(this.store)));
			try {
				int magic = data.readInt();
				int version = data.readInt();
				if (magic != MAGIC || version != VERSION) {
					return;
				}
				int size = data.readInt();
				for (int i = 0; i < size; i++) {
					this.playersNames.add(data.readUTF());
					this.playersScores.add(data.readInt());
					this.survivalTimes.add(data.readInt());
				}
			} finally {
				data.close();
			}
			this.sortEntries();
		} catch (IOException ex) {
			this.playersNames.clear();
			this.playersScores.clear();
			this.survivalTimes.clear();
		}
	}

	private void sortEntries() {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for (int i = 0; i < this.playersNames.size(); i++) {
			order.add(i);
		}
		Collections.sort(order, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return Integer.compare(playersScores.get(b), playersScores.get(a));
			}
		});
		ArrayList<String> newNames = new ArrayList<String>();
		ArrayList<Integer> newScores = new ArrayList<Integer>();
		ArrayList<Integer> newTimes = new ArrayList<Integer>();
		for (Integer idx : order) {
			newNames.add(this.playersNames.get(idx));
			newScores.add(this.playersScores.get(idx));
			newTimes.add(this.survivalTimes.get(idx));
		}
		this.playersNames.clear();
		this.playersNames.addAll(newNames);
		this.playersScores.clear();
		this.playersScores.addAll(newScores);
		this.survivalTimes.clear();
		this.survivalTimes.addAll(newTimes);
	}
}