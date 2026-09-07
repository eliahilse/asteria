package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ApoDefenceHighscore {
	private URL connection;

	private URL writeConnection;

	private InputStream is;

	private boolean bConnect;

	private ArrayList<ApoDefenceHighscoreEntity> entity;

	private String map;

	private int yourPlace;

	public ApoDefenceHighscore(String map) {
		super();

		this.entity = new ArrayList<ApoDefenceHighscoreEntity>();
		this.map = map;
		this.yourPlace = -1;
		this.bConnect = false;

		// this.send("Ole", "Original_First", 85, 4000);
	}

	public String getMap() {
		return this.map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public boolean isBConnect() {
		return this.bConnect;
	}

	public void setBConnect(boolean bConnect) {
		this.bConnect = bConnect;
	}

	public boolean canConnect() {
		try {
			BufferedImage iConnect = ImageIO.read(new URL(
					"http://home.arcor.de/newbielein/test.gif"));
			if (iConnect == null) {
				this.bConnect = false;
				return false;
			} else {
				this.bConnect = true;
				return true;
			}
		} catch (IOException e) {
			this.bConnect = false;
			return false;
		}
	}

	public void makeEntity() {
		if (this.isBConnect()) {
			try {
				connection = new URL(
						"http://daporius.drition.net/highscore.xml");
				is = connection.openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.makeEntity(new Scanner(is).useDelimiter("\\Z").next());
		}
	}

	private void makeEntity(String s) {
		s = s.replace("\t", "");
		s = s.replace("\n", "");
		// System.out.println( s );
		while (s.indexOf("<person>") != -1) {
			int i = s.indexOf("<person>") + 9;
			int h = s.indexOf("</person>");
			String sub = s.substring(i, h);
			String name = sub.substring(sub.indexOf("<nickname>") + 10, sub
					.indexOf("</nickname>"));
			// System.out.println( name );
			int level = Integer.parseInt(sub.substring(
					sub.indexOf("<level>") + 7, sub.indexOf("</level>")));
			// System.out.println( level );
			String map = sub.substring(sub.indexOf("<map>") + 5, sub
					.indexOf("</map>"));
			// System.out.println( map );
			int points = Integer.parseInt(sub.substring(
					sub.indexOf("<point>") + 7, sub.indexOf("</point>")));
			// System.out.println( points );
			if (map.equals(this.map))
				this.addEntity(name, map, level, points);
			if (h > 0)
				s = s.substring(h + 9);
		}

		/*
		 * for ( int i = 0; i < this.entity.size(); i++ ) { System.out.println(
		 * "Points = " + this.entity.get( i ).getPoints() ); }
		 */

	}

	public void addEntity(String name, String map, int level, int points) {
		// System.out.println( name );
		if (this.entity.size() == 0)
			this.entity.add(new ApoDefenceHighscoreEntity(name, map, level,
					points));
		else {
			boolean bAdd = false;
			for (int i = 0; i < this.entity.size(); i++) {
				if (this.entity.get(i).getPoints() < points) {
					bAdd = true;
					this.yourPlace = i;
					this.entity.add(i, new ApoDefenceHighscoreEntity(name, map,
							level, points));
					break;
				}
			}
			if (!bAdd) {
				this.yourPlace = this.entity.size();
				this.entity.add(new ApoDefenceHighscoreEntity(name, map, level,
						points));
			}
		}
	}

	public void render(Graphics2D g) {
		int x = 60;
		int y = 167;
		for (int i = 0; i < this.entity.size() && i < 5; i++) {
			if ((i == this.yourPlace) || (this.entity.size() == 1))
				g.setColor(Color.RED);
			else
				g.setColor(Color.WHITE);
			this.entity.get(i).render(g, x, y, i);
			y += 20;
		}
		if (this.yourPlace >= 5) {
			g.setColor(Color.RED);
			y += 20;
			this.entity.get(this.yourPlace).render(g, x, y, this.yourPlace);
		}
	}

	public void send(String name, String map, int level, int points) {
		ApoDefenceHighscoreEncrypt base = new ApoDefenceHighscoreEncrypt();
		String baseName = base.encodeString(name);
		baseName = base.encodeString(baseName);
		baseName = base.encodeString(baseName);

		String baseMap = base.encodeString(map);
		baseMap = base.encodeString(baseMap);
		baseMap = base.encodeString(baseMap);
		baseMap = base.encodeString(baseMap);

		String baseLevel = base.encodeString("" + level);
		baseLevel = base.encodeString(baseLevel);

		String basePoints = base.encodeString("" + points);
		basePoints = base.encodeString(basePoints);
		basePoints = base.encodeString(basePoints);
		basePoints = base.encodeString(basePoints);
		basePoints = base.encodeString(basePoints);

		String day = Integer.toString(Calendar.getInstance().get(
				Calendar.DAY_OF_MONTH));
		day = base.encodeString(day);
		day = base.encodeString(day);

		String url = "http://daporius.drition.net/highscore.php?";
		String getUrl = "name=" + baseName + "&map=" + baseMap + "&level="
				+ baseLevel + "&points=" + basePoints + "&cor=" + day;

		url = url + getUrl;
		// System.out.println(url);

		try {
			this.writeConnection = new URL(url);
			HttpURLConnection con = (HttpURLConnection) this.writeConnection
					.openConnection();
			con.setRequestMethod("GET");
			new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ApoDefenceHighscore("Original_First");
	}

}
