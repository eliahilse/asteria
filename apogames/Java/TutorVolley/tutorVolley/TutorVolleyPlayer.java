package tutorVolley;

import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Klasse, die einen Spieler repräsentiert
 * @author Dirk Aporius
 *
 */
public class TutorVolleyPlayer extends TutorVolleyEntity
{
	public static final double	DIR_LEFT	= -1;
	public static final double	DIR_RIGHT	= 1;
	
	private TutorVolleyAI		playerAI;
	private double				xSpeed, ySpeed;
	private int					points;
	private boolean				bHigh;
	private TutorVolleyEntity	enemy;
	private Vector				checkTime;
	private int					count;
	
	public TutorVolleyPlayer( BufferedImage iPlayer, int x, int y, int width, int height )
	{
		super( iPlayer, x, y, width, height );
		
		this.init();
	}
	
	/**
	 * initialisiert den Startwerte für den Ball
	 */
	protected void init()
	{
		this.xSpeed			= 0.0D;
		this.ySpeed			= 0.0D;
		
		this.count			= 0;
		this.points			= 0;
		
		this.bHigh			= false;
		
		this.checkTime		= new Vector();
	}
	
	/**
	 * gibt den EnemyEntity zurück
	 * @return EnemyEntity
	 */
	public TutorVolleyEntity getEnemy()
	{
		return this.enemy;
	}

	/**
	 * setzt den Enemy auf den übergebenen Entity
	 * @param enemy = GegnerEntity
	 */
	protected void setEnemy(TutorVolleyEntity enemy)
	{
		this.enemy = enemy;
	}
	
	/**
	 * gibt den x-Speed des Spielers zurück
	 * @return Geschwindigkeit nach links oder rechts
	 */
	protected double getXSpeed()
	{
		return this.xSpeed;
	}
	
	/**
	 * gibt den y-Speed des Spielers zurück
	 * @return Geschwindigkeit nach oben oder unten
	 */
	protected double getYSpeed()
	{
		return this.ySpeed;
	}
	
	/**
	 * übergibt die Punkte des Spielers, die er gerade hat
	 * @return Punkte
	 */
	public int getPoints()
	{
		return this.points;
	}
	
	/**
	 * erhöht die Punkteanzahl um eins
	 */
	protected void setPoints()
	{
		this.points++;
	}
	
	/**
	 * setzt die Punkteanzahl auf den übergebenen Wert
	 */
	protected void setPoints( int points )
	{
		this.points = points;
	}
	
	/**
	 * gibt den Namen des Spielers zurück
	 * @return name
	 */
	protected String getName()
	{
		return this.playerAI.getName();
	}
	
	/**
	 * gibt den Autorennamen zurück
	 * @return Autorenname
	 */
	protected String getAuthorName()
	{
		return this.playerAI.getAuthorName();
	}
	
	/**
	 * setzt die x-Richtung in die der Spieler gehen soll
	 * es muss ein Wert zwischen -1 und 1 sein
	 * -1 bedeutet schnell nach links
	 * 0 bedeutet stehen bleiben
	 * +1 bedeutet schnell nach rechts
	 * @param x_speed: falls größer 1 dann wird der Wert auf 1 gesetzt 
	 * und der Spieler geht nach rechts
	 * falls auf kleiner -1 dann wird der Wert auf -1 gesetzt
	 * und der Spieler geht nach links
	 * sonst auf 0 und bleibt stehen
	 */
	public void setDirection( double xSpeed )
	{
		if ( xSpeed > 1 )
			xSpeed = 1;
		else if ( xSpeed < -1 )
			xSpeed	= -1;
		this.xSpeed	= xSpeed * 1.5;
	}
	
	/**
	 * setzt die x-Richtung in die der Spieler gehen soll
	 * @param x_speed: falls größer 0 dann wird der Wert auf 1 gesetzt 
	 * und der Spieler geht nach rechts
	 * falls auf kleiner 0 dann wird der Wert auf -1 gesetzt
	 * und der Spieler geht nach links
	 * sonst auf 0 und bleibt stehen
	 */
	public void setDirection( int xSpeed )
	{
		if ( xSpeed > 1 )
			xSpeed = 1;
		else if ( xSpeed < -1 )
			xSpeed	= -1;
		this.xSpeed	= xSpeed * 1.5;
	}
	
	/**
	 * setzt die AI des Spielers auf den übergebenen Wert
	 * @param apoVolleyAI = die AI
	 */
	protected void setAI( TutorVolleyAI apoVolleyAI )
	{
		this.playerAI	= apoVolleyAI;
	}
	
	protected double getAverageTime()
	{
		if ( this.checkTime.size() < 20 )
			return 0;
		double value		= 0;
		for ( int i = 0; i < this.checkTime.size(); i++ )
		{
			value	+= ((Integer)this.checkTime.get( i )).intValue();
		}
		value	= value / this.checkTime.size();
		return value;
	}
	
	/**
	 * wird jede Runde aufgerufen und veranlasst die AI zu "denken"
	 * undn führt einen Schritt dann aus
	 * @param ball = der Spielball
	 */
	@SuppressWarnings("unchecked")
	protected boolean think( TutorVolleyBall ball )
	{
		if ( this.playerAI == null )
			return false;
	
		this.count++;
		int time		= ((Long)(System.nanoTime())).intValue();
		boolean bJump	= false;
		bJump = this.playerAI.think( ball, this );
		if ( bJump )
		{
			this.setNewSpeed( -1.5 );
		} else
		{
			this.setNewSpeed( 0.0 );
		}
		if ( this.checkTime.size() < 40 )
		{
			int t	= (((Long)(System.nanoTime())).intValue() - time) / 1000000;
			this.checkTime.add( t );
		} else
		{
			int t	= (((Long)(System.nanoTime())).intValue() - time) / 1000000;
			this.checkTime.add( t );
			this.checkTime.remove( 0 );
			if ( this.getAverageTime() > 10 )
				return false;
		}
		if ( this.count >= 5 )
			return false;
		this.count	= 0;
		return true;
	}
	
	/**
	 * berechnet die neuen Werte für die X-Position und Y-Position
	 * @param jump: true wenn er springen soll sonst false
	 */
	private void setNewSpeed( double jump )
	{
		if ( ( this.ySpeed >= 0 ) && ( !this.bHigh ) && ( jump < 0 ) )
		{
			this.ySpeed	= jump;
		}
		if ( ( jump == 0 ) && ( this.ySpeed < 0 ) )
		{
			this.bHigh		= true;
		}
		this.setX( this.getX() + this.xSpeed * 2 * 1 );
		this.setY( this.getY() + this.ySpeed * 4 * 1 );
		if ( this.bHigh )
			this.ySpeed	= this.ySpeed * 0.9 + ( TutorVolleyBall.GRAVITATION * 0.02 );
		if ( this.getX() < 0 )
			this.setX( 0 );
		else if ( this.getX() + this.getWidth() > 400 )
			this.setX( 400 - this.getWidth() );
		else if ( ( this.getX() + this.getWidth() > 195 ) && ( this.getX() + this.getWidth() < 206 ) )
			this.setX( 195 - this.getWidth() );
		else if ( ( this.getX() < 205 ) && ( this.getX() >= 192 ) )
			this.setX( 205 );
		
		if ( this.getY() > 215 )
		{
			this.setY( 215 );
			this.ySpeed	= 0;
			this.bHigh		= false;
		} else if ( this.getY() < 100 )
		{
			this.bHigh		= true;
		}
		
	}
	
}
