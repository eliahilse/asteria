package apoDefence;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse, die sich mit dem Input/Output von den Levels befasst
 * @author Dirk Aporius
 *
 */
public class ApoDefenceLoadSave
{
	private int					x			= 20;
	private int					y			= 15;
	private int					money		= 30;
	private int					towerCount	= 15;
	private int					waves		= 40;
	
	private ArrayList<Integer>	enemies;
	
	private int[][]				aPlayground;
	
	private Point				pointDefend, pointUpgrade;
	
	private ArrayList<ApoDefenceDragObject>	enemyWay;
	
	private String				imageString;
	
	private String 				level;
	
	public ApoDefenceLoadSave()
	{
		super();

		this.aPlayground		= new int[this.y][this.x];
		
		this.pointDefend		= new Point( -1, -1 );
		this.pointUpgrade		= new Point( -1, -1 );
		
		this.imageString		= "";
		this.level				= "";
		this.enemies			= new ArrayList<Integer>();
		this.enemies.add( ApoDefenceConstants.ENEMY_BIRD );
		
		this.enemyWay			= new ArrayList<ApoDefenceDragObject>();
	}
	
	/**
	 * setzt alle Werte zum Speichern, auf den übergebenen Wert
	 * @param x: x-Größe des Spielfeldes
	 * @param y: y-Größe des Spielfedes
	 * @param aPlayground: der eigentliche Untergrund aus dem nachher das Spielfeld erstellt wird
	 * @param pointDefend: the castlepoint
	 * @param pointUpgrade: the researchbuildingpoint
	 * @param enemyWay: Wegpunkte, die die Gegner gehen werden
	 * @param imageString: gibt an, welchen Untergrund zum malen bentutzt werden soll
	 * @param money: wieviel Geld hat der Spieler am Anfang
	 * @param towerCount: wieviel Tower darf der Spieler bauen
	 * @param waves: wieviel Waves von Gegnern sollen auf den Spieler rasseln ;) 
	 * @param enemies: gibt an, welche Enemies, wann kommen
	 */
	public void setAll( int x, int y, int[][] aPlayground, Point pointDefend, Point pointUpgrade, ArrayList<ApoDefenceDragObject> enemyWay, String imageString, int money, int towerCount, int waves, ArrayList<Integer> enemies )
	{
		this.x				= x;
		this.y				= y;
		this.aPlayground	= aPlayground;
		this.pointDefend	= pointDefend;
		this.pointUpgrade	= pointUpgrade;
		this.enemyWay		= enemyWay;
		this.imageString	= imageString;
		this.money			= money;
		this.towerCount		= towerCount;
		this.waves			= waves;
		this.enemies		= enemies;
	}
	
	/**
	 * schreibt eine Datei in das übergebene String Filesystem
	 * @param fileName = wo wird hingespeichert
	 */
	public void writeLevel( String fileName )
	{
		try
		{
			FileOutputStream file		= new FileOutputStream( fileName );
			BufferedOutputStream buff	= new BufferedOutputStream( file );
			DataOutputStream data		= new DataOutputStream( buff );
			
			data.writeInt( this.x );
			data.writeInt( this.y );
			
			for ( int i = 0; i < this.y; i++ )
			{
				for ( int j = 0; j < this.x; j++ )
				{
					data.writeInt( this.aPlayground[i][j] );
				}
			}
			
			data.writeInt( this.pointDefend.x );
			data.writeInt( this.pointDefend.y );
			
			data.writeInt( this.pointUpgrade.x );
			data.writeInt( this.pointUpgrade.y );
			
			data.writeInt( this.enemyWay.size() );
			for ( int i = 0; i < this.enemyWay.size(); i++ )
			{
				data.writeInt( (int)this.enemyWay.get( i ).getX() );
				data.writeInt( (int)this.enemyWay.get( i ).getY() );
			}
			
			data.writeUTF( this.imageString );
			
			data.writeInt( this.money );
			data.writeInt( this.towerCount );
			data.writeInt( this.waves );
			
			data.writeInt( this.enemies.size() );
			for ( int i = 0; i < this.enemies.size(); i++ )
			{
				data.writeInt( this.enemies.get( i ) );
			}
			
			data.close();
		} catch ( IOException e )
		{
			System.out.println("Error: "+e);
		}
	}
	
	/**
	 * liest eine Datei in das übergebene String Filesystem
	 * @param fileName = von wo wird gelesen
	 */
	public void readLevel( String fileName )
	{
		try
		{
			FileInputStream file		= new FileInputStream( fileName );
			BufferedInputStream buff	= new BufferedInputStream( file );
			DataInputStream data		= new DataInputStream( buff );
			try
			{
				this.level		= fileName.substring( fileName.lastIndexOf( File.separator ) + 1, fileName.lastIndexOf( "." ) );
				
				this.x			= data.readInt();
				this.y			= data.readInt();
				
				this.aPlayground	= new int[this.y][this.x];
				for ( int i = 0; i < this.y; i++ )
				{
					for ( int j = 0; j < this.x; j++ )
					{
						this.aPlayground[i][j]	= data.readInt();
					}
				}
				
				int x		= data.readInt();
				int y		= data.readInt();
				this.pointDefend		= new Point( x, y );
				
				x			= data.readInt();
				y			= data.readInt();
				this.pointUpgrade		= new Point( x, y );
				
				this.enemyWay		= new ArrayList<ApoDefenceDragObject>();
				int size			= data.readInt();
				for ( int i = 0; i < size; i++ )
				{
					x			= data.readInt();
					y			= data.readInt();
					this.enemyWay.add( new ApoDefenceDragObject( x, y, 16, 16 ) );
				}
				
				this.imageString	= data.readUTF();
				
				this.money			= data.readInt();
				this.towerCount		= data.readInt();
				this.waves			= data.readInt();
				
				this.enemies		= new ArrayList<Integer>();
				size			= data.readInt();
				for ( int i = 0; i < size; i++ )
				{
					this.enemies.add( data.readInt() );
				}
				
				data.close();
			} catch ( EOFException e )
			{
				System.out.println("Error: "+e);
				buff.close();
			}
			data.close();
		} catch ( IOException e )
		{
			System.out.println("Error: "+e);
		}
	}

	/**
	 * gibt das eigentliche Spielfeld zurück
	 * @return gibt das eigentliche Spielfeld zurück
	 */
	public int[][] getAPlayground()
	{
		return this.aPlayground;
	}

	/**
	 * gibt die Wegpunkte der Gegner zurück
	 * @return gibt die Wegpunkte der Gegner zurück
	 */
	public ArrayList<ApoDefenceDragObject> getEnemyWay()
	{
		return this.enemyWay;
	}

	/**
	 * gibt das Schloß zurück
	 * @return gibt das Schloß zurück
	 */
	public Point getPointDefend()
	{
		return this.pointDefend;
	}

	/**
	 * gibt das Researchbuilding zurück
	 * @return gibt das Researchbuilding zurück
	 */
	public Point getPointUpgrade()
	{
		return this.pointUpgrade;
	}

	/**
	 * gibt den x-Wert der Spielfeldgröße zurück
	 * @return gibt den x-Wert der Spielfeldgröße zurück
	 */
	public int getX()
	{
		return this.x;
	}

	/**
	 * gibt den y-Wert der Spielfeldgröße zurück
	 * @return gibt den y-Wert der Spielfeldgröße zurück
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * gibt den String zurück (also welches Bild soll für das Spielfeld verwendet werden)
	 * @return gibt den String zurück (also welches Bild soll für das Spielfeld verwendet werden)
	 */
	public String getImageString()
	{
		return this.imageString;
	}

	/**
	 * gibt zurück, wieviel Geld der Spieler am Anfang hat
	 * @return gibt zurück, wieviel Geld der Spieler am Anfang hat
	 */
	public int getMoney()
	{
		return this.money;
	}

	/**
	 * gibt zurück, wieviel Tower der Spieler im Spiel bauen darf
	 * @return gibt zurück, wieviel Tower der Spieler im Spiel bauen darf
	 */
	public int getTowerCount()
	{
		return this.towerCount;
	}

	/**
	 * gibt zurück, wann welche Gegner kommen
	 * @return gibt zurück, wann welche Gegner kommen
	 */
	public ArrayList<Integer> getEnemies()
	{
		return this.enemies;
	}

	/**
	 * gibt zurück, wieviel Waves im Level gespielt werden soll
	 * @return gibt zurück, wieviel Waves im Level gespielt werden soll
	 */
	public int getWaves()
	{
		return this.waves;
	}

	/**
	 * gibt den Levelstring zurück
	 * @return gibt den Levelstring zurück
	 */
	public String getLevel()
	{
		return this.level;
	}
	
}
