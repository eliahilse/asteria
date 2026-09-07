package tutorVolley;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
//import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import java.util.Timer;

/**
 * Klasse, die das eigentliches Spielpanel darstellt
 * @author Dirk Aporius
 *
 */
public class TutorVolleyPanelGame extends JPanel implements KeyListener
{
	private static final long 			serialVersionUID = 1L;
	
	public final int					WAIT_TIME	= 25;
	public final static int				PLAYER_ONE	= 1;
	public final static int				PLAYER_TWO	= 2;
	public final static int				BALL		= 3;
	
	private TutorVolleyMain 			tutorVolleyMain;
	
	private BufferedImage				iBackground, iTime, iWin;
	//private VolatileImage				iVol;

	private long						time;//, timePoint;
	
	private boolean						bThread, bWin;
	
	private int							playerWin, thinkCount;
	
	private TutorVolleyPlayer[]			tutorVolleyPlayer;
	
	private TutorVolleyBall				tutorVolleyBall;
	
	private Font						fontBig;
	
	private ArrayList<TutorVolleySnow>	tutorVolleySnow;
	
	private TutorVolleyTimer			tutorVolleyTimer;
	
	public TutorVolleyPanelGame( TutorVolleyMain tutorVolleyMain, BufferedImage iBackground )
	{
		super( true );
		
		this.tutorVolleyMain		= tutorVolleyMain;
		this.iBackground			= iBackground;
		
		TutorVolleyImage	tutorVolleyImage	= new TutorVolleyImage();
		
		this.iTime						= tutorVolleyImage.setPicsMain( "/images/time.png", false );
		this.iWin						= tutorVolleyImage.setPicsMain( "/images/win.png", false );
		
		this.tutorVolleyPlayer			= new TutorVolleyPlayer[2];
		this.tutorVolleyPlayer[0]		= new TutorVolleyPlayer( tutorVolleyImage.setPicsMain( "/images/player_one.png", false ),  34, 215, 33, 45  );
		this.tutorVolleyPlayer[1]		= new TutorVolleyPlayer( tutorVolleyImage.setPicsMain( "/images/player_two.png", false ), 334, 215, 33, 45  );
		this.tutorVolleyPlayer[0].setEnemy( this.tutorVolleyPlayer[1] );
		this.tutorVolleyPlayer[1].setEnemy( this.tutorVolleyPlayer[0] );
		
		this.tutorVolleyBall			= new TutorVolleyBall( tutorVolleyImage.setPicsMain( "/images/ball.png", false ),  84, 150, 32, 32  );
		
		this.fontBig					= new Font( "Dialog", Font.BOLD, 20 );
		
		this.tutorVolleyTimer			= new TutorVolleyTimer( this );
		
		this.thinkCount					= 1;
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(this.tutorVolleyTimer,0,this.WAIT_TIME);
		
		this.tutorVolleyMain.addKeyListener( this );
	}
	
	/**
	 * initialisiert alles wichtige, wie die Spieler, den Ball und sagt dem Timer,
	 * dass er nun alle 25 Millisekunden die think Methode aufrufen soll
	 */
	protected void init()
	{
		if ( this.bThread )
			return;
		this.thinkCount					= 1;
		int random	= (int)(Math.random() * 100);
		if ( random > 50 )
		{
			this.tutorVolleyBall.setX( 84 );
			this.tutorVolleyBall.setY( 150 );
		} else
		{
			this.tutorVolleyBall.setX( 284 );
			this.tutorVolleyBall.setY( 150 );
		}
		this.tutorVolleyBall.setBDown( false );
		this.tutorVolleyBall.setBFirst( false );
		this.tutorVolleyPlayer[0].setX( 34 );
		this.tutorVolleyPlayer[0].setY( 215 );
		this.tutorVolleyPlayer[0].setPoints( 0 );
		this.tutorVolleyPlayer[0].init();
		this.tutorVolleyPlayer[1].setX( 334 );
		this.tutorVolleyPlayer[1].setY( 215 );
		this.tutorVolleyPlayer[1].setPoints( 0 );
		this.tutorVolleyPlayer[1].init();
		this.bWin			= false;
		//this.timePoint		= System.nanoTime();
		
		this.time		= 0;
		
		this.tutorVolleySnow	= new ArrayList<TutorVolleySnow>();
		int rand = (int)(Math.random() * 50 + 70);
		for ( int i = 0; i < rand; i++ )
		{
			int x	= (int)(Math.random() * (this.getWidth() + 50)) - 50;
			int y	= (int)(Math.random() * (this.getHeight() - 80) );
			this.tutorVolleySnow.add( new TutorVolleySnow( null, x, y, 3, 3 ) );
		}
		
		this.time			= System.nanoTime();
		this.tutorVolleyTimer.setBRunning( true );
		
		this.bThread	= true;
	}

	/**
	 * setzt die übergebene AI an den übergebenen Spieler
	 * @param player = welcher Spieler soll die AI bekommen
	 * @param tutorVolleyAI = die AI
	 */
	protected void setAI( int player, TutorVolleyAI tutorVolleyAI )
	{
		this.tutorVolleyPlayer[player-1].setAI( tutorVolleyAI );
	}
	
	/**
	 * gibt das eigentliche SpielerArray zurück
	 * @return SpielerArray
	 */
	protected TutorVolleyPlayer[] getPlayer()
	{
		return this.tutorVolleyPlayer;
	}
	
	/**
	 * sagt den Timer, dass er jetzt nicht mehr die Think Methode aufrufen muss!
	 */
	protected void stop()
	{
		this.tutorVolleyTimer.setBRunning( false );
		this.bThread	= false;
	}
	
	/**
	 * wird pro Zyklus einmal aufgerufen und gibt dem Ball und den Spielern
	 * einen neuen X und Y-Wert
	 */
	protected void think()
	{
		for ( int i = 0; i < this.thinkCount; i++ )
		{
			this.think( TutorVolleyPanelGame.PLAYER_ONE );
			this.think( TutorVolleyPanelGame.PLAYER_TWO );
			this.think( TutorVolleyPanelGame.BALL );
		}
	}
	
	/**
	 * wird pro Zyklus 3 mal mit verschiedenen values aufgerufen
	 * einmal für den Ball und dann noch für beide Spieler
	 * @param value = welche Methode soll aufgerufen werden
	 */
	protected void think( int value )
	{
		if ( value == TutorVolleyPanelGame.BALL )
			this.thinkBall();
		else
			this.thinkPlayer( value - 1 );
		for ( int i = this.tutorVolleySnow.size() - 1; i >= 0; i-- )
		{
			this.tutorVolleySnow.get( i ).think();
			if ( this.tutorVolleySnow.get( i ).getY() >= this.getHeight() - 50 )
			{
				this.tutorVolleySnow.remove( i );
				int x	= (int)(Math.random() * (this.getWidth()+50)) - 50;
				this.tutorVolleySnow.add( new TutorVolleySnow( null, x, 0.0D, 3, 3 ) );
			} else if ( this.tutorVolleySnow.get( i ).getX() > this.getWidth() )
			{
				this.tutorVolleySnow.remove( i );
				int x	= (int)(Math.random() * (this.getWidth()+50)) - 50;
				this.tutorVolleySnow.add( new TutorVolleySnow( null, x, 0.0D, 3, 3 ) );
			}
		}
	}
	
	/**
	 * setzt die Punkte bei einem Spieler hoch
	 * @param bPoint: true = Spieler 1 bekommt den Punkt sonst Spieler 2
	 */
	protected void setPoint( boolean bPoint )
	{
		//if ( TimeUnit.NANOSECONDS.toMillis( System.nanoTime() - this.timePoint ) < 1000 )
		//	return;
		//this.timePoint		= System.nanoTime();
		if ( bPoint == true )
		{
			this.tutorVolleyPlayer[0].setPoints();
			this.tutorVolleyBall.setX( 84 );
			this.tutorVolleyBall.setY( 150 );
		} else
		{
			this.tutorVolleyPlayer[1].setPoints();
			this.tutorVolleyBall.setX( 284 );
			this.tutorVolleyBall.setY( 150 );
		}
		this.checkWin();
	}
	
	/**
	 * überprüft, ob Spieler 1 oder 2 gewonnen haben und zeigt das Gewinnerbild
	 */
	private void checkWin()
	{
		if ( 	( this.tutorVolleyPlayer[0].getPoints() == 27 ) || 
				( ( this.tutorVolleyPlayer[0].getPoints() >= 25 ) && ( this.tutorVolleyPlayer[1].getPoints() + 2 <= this.tutorVolleyPlayer[0].getPoints() ) ) )
		{
			this.stop();
			this.bWin		= true;
			this.playerWin	= 1;
			this.repaint();
		} else if ( ( this.tutorVolleyPlayer[1].getPoints() == 27 ) || 
					( ( this.tutorVolleyPlayer[1].getPoints() >= 25 ) && ( this.tutorVolleyPlayer[0].getPoints() + 2 <= this.tutorVolleyPlayer[1].getPoints() ) ) )
		{
			this.stop();
			this.bWin		= true;
			this.playerWin	= 2;
			this.repaint();
		}
	}
	
	/**
	 * ruft die think-Methode des Balles auf
	 */
	private void thinkBall()
	{
		this.tutorVolleyBall.think( this.tutorVolleyPlayer[0], this.tutorVolleyPlayer[1], this );
	}
	
	/**
	 * ruft die think-Methode des übergebenen Spielers auf
	 * @param player = welcher Spieler soll denken
	 */
	private void thinkPlayer( int player )
	{
		if ( !this.tutorVolleyPlayer[player].think( this.tutorVolleyBall ) )
			this.stop();
	}
	
	/**
	 *	wird auf Druck der ESC-Taste aufgerufen und zeigt das OptionsPanel
	 */
	private void setOptions()
	{
		this.tutorVolleyMain.setOptions();
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {}

	/**
	 * was passiert, wenn eine Keyboardtaste losgelassen wird
	 */
	public void keyReleased(KeyEvent e)
	{
		if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
		{
			this.setOptions();
		} else if ( ( e.getKeyCode() == KeyEvent.VK_ENTER ) && ( this.bWin ) )
		{
			this.init();
		} else if ( ( e.getKeyCode() == KeyEvent.VK_PLUS ) && ( !this.bWin ) )
		{
			if ( 	( this.tutorVolleyPlayer[0].getName().equals( "Mensch" ) ) ||
					( this.tutorVolleyPlayer[1].getName().equals( "Mensch" ) ) )
				return;
			this.thinkCount++;
			if ( this.thinkCount > 50 )
				this.thinkCount	= 50;
		} else if ( ( e.getKeyCode() == KeyEvent.VK_MINUS ) && ( !this.bWin ) )
		{
			if ( 	( this.tutorVolleyPlayer[0].getName().equals( "Mensch" ) ) ||
					( this.tutorVolleyPlayer[1].getName().equals( "Mensch" ) ) )
				return;
			this.thinkCount--;
			if ( this.thinkCount < 1 )
				this.thinkCount	= 1;
		}
	}

	/**
	 * malt das eigentliche Spielfeld mit Spielern usw.
	 */
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		Graphics2D gfx	= (Graphics2D)g;
		
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		gfx.drawImage( this.iBackground, 0, 0, null );
		
		for ( int i = 0; i < this.tutorVolleyPlayer.length; i++ )
		{
			this.tutorVolleyPlayer[i].render( gfx );
		}
		
		this.tutorVolleyBall.render( gfx );
		
		gfx.setColor( Color.WHITE );
		for ( int i = 0; i < this.tutorVolleySnow.size(); i++ )
		{
			this.tutorVolleySnow.get( i ).render( gfx );
		}
		
		gfx.setColor( Color.BLACK );
		this.drawPoints( gfx );
		
		this.drawTime( gfx );
		
		this.drawThinkTime( gfx );
		
		gfx.setColor( Color.BLACK );
		if ( this.bWin )
			this.drawWin( gfx );
	}
	
	/*protected void drawVolatileImage()
	{
		long t	= System.nanoTime();
		
		this.iVol	= createVolatileImage(400, 300);
		
		Graphics gDraw = this.getGraphics();
		Graphics gVol  = this.iVol.getGraphics();
		Graphics2D gfx	= (Graphics2D)gVol;
		
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		gfx.drawImage( this.iBackground, 0, 0, this );
		
		for ( int i = 0; i < this.tutorVolleyPlayer.length; i++ )
		{
			this.tutorVolleyPlayer[i].render( gfx );
		}
		
		this.tutorVolleyBall.render( gfx );
		
		gfx.setColor( Color.WHITE );
		for ( int i = 0; i < this.tutorVolleySnow.size(); i++ )
		{
			this.tutorVolleySnow.get( i ).render( gfx );
		}
		
		gfx.setColor( Color.BLACK );
		this.drawPoints( gfx );
		
		this.drawTime( gfx );
		
		gfx.setColor( Color.BLACK );
		if ( this.bWin )
			this.drawWin( gfx );
		
		gDraw.drawImage( this.iVol, 0, 0, null );
		
		System.out.println( "time = "+( System.nanoTime() - t ) );
	}*/
	
	/**
	 * malt die Punkte und Namen der Spieler
	 * @param g
	 */
	private void drawPoints( Graphics2D g )
	{
		g.drawImage( this.iTime, 20, 7, this );
		g.drawString( "Points: "+this.tutorVolleyPlayer[0].getPoints(), 25, 20 );
		g.drawImage( this.iTime, 20, 27, this );
		String s	= "empty";
		if ( this.tutorVolleyPlayer[0].getName() != null )
			s	= this.tutorVolleyPlayer[0].getName();
		g.drawString( s, 25, 40 );
		g.drawImage( this.iTime, 300, 7, this );
		g.drawString( "Points: "+this.tutorVolleyPlayer[1].getPoints(), 305, 20 );
		g.drawImage( this.iTime, 300, 27, this );
		s	= "empty";
		if ( this.tutorVolleyPlayer[1].getName() != null )
			s	= this.tutorVolleyPlayer[1].getName();
		g.drawString( s, 305, 40 );
	}
	
	/**
	 * malt die Zeit
	 * @param g
	 */
	private void drawTime( Graphics2D g )
	{
		g.drawImage( this.iTime, 163, 7, this );
		long time		= TimeUnit.NANOSECONDS.toMillis( System.nanoTime() - this.time );
		String min = ""+( time/1000/60);
		String sec = ""+(( time/1000)%60);
		String msec = ""+(( time/10)%100);
		if (sec.length()<2) sec = "0"+sec;
		if (msec.length()<2) msec = "0"+msec;
		String timeString = min+":"+sec+":"+msec;
		int w	= g.getFontMetrics().stringWidth( timeString );
		g.drawString( timeString, 200 - w/2, 20 );
		if ( 	( !this.tutorVolleyPlayer[0].getName().equals( "Mensch" ) ) &&
				( !this.tutorVolleyPlayer[1].getName().equals( "Mensch" ) ) )
		{
			String s	= "ThinkCount = "+this.thinkCount;
			w	= g.getFontMetrics().stringWidth( s );
			g.drawString( s, 200 - w/2, 40 );
		}
	}
	
	/**
	 * malt die Sachen, wenn das Spiel beendet wurde
	 * @param g
	 */
	private void drawWin( Graphics2D g )
	{
		g.drawImage( this.iWin, 0, 40, this );
		g.setFont( this.fontBig );
		if ( this.playerWin == 1 )
		{
			g.drawImage( this.tutorVolleyPlayer[0].getIBackground(), 184, 85, this );
			String s	= "empty";
			if ( this.tutorVolleyPlayer[0].getName() != null )
				s	= this.tutorVolleyPlayer[0].getName();
			int w = g.getFontMetrics().stringWidth( s );
			g.drawString( s, 200 - w/2, 155 );
		}
		else if ( this.playerWin == 2 )
		{
			g.drawImage( this.tutorVolleyPlayer[1].getIBackground(), 184, 85, this );
			String s	= "empty";
			if ( this.tutorVolleyPlayer[1].getName() != null )
				s	= this.tutorVolleyPlayer[1].getName();
			int w = g.getFontMetrics().stringWidth( s );
			g.drawString( s, 200 - w/2, 155 );
		}
	}
	
	private void drawThinkTime( Graphics2D g )
	{
		g.setColor( Color.BLACK );
		if ( !this.tutorVolleyPlayer[0].getName().equals( "Mensch" ) )
			g.drawRect( 20, 48, 75, 15 );
		if ( !this.tutorVolleyPlayer[1].getName().equals( "Mensch" ) )
			g.drawRect( 300, 48, 75, 15 );
		
		g.setColor( Color.BLUE );
		if ( !this.tutorVolleyPlayer[0].getName().equals( "Mensch" ) )
			g.fillRect( 21, 49, (int)(this.tutorVolleyPlayer[0].getAverageTime() * 7), 14 );
		if ( !this.tutorVolleyPlayer[1].getName().equals( "Mensch" ) )
			g.fillRect( 301, 49, (int)(this.tutorVolleyPlayer[1].getAverageTime() * 7), 14 );
	}
	
	

}
