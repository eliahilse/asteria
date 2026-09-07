package tutorVolley;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Klasse, die am Anfang aufgerufen wird und das OptionsPanel darstellt
 * @author Dirk Aporius
 *
 */
public class TutorVolleyPanelOptions extends JPanel implements MouseMotionListener, MouseListener
{
	private static final long 	serialVersionUID = 1L;

	private TutorVolleyMain 	tutorVolleyMain;
	
	private BufferedImage		iBackground;
	
	private TutorVolleyButton[]	tutorVolleyButton;
	
	private String				playerName	= "";
	
	private TutorVolleyAI			tutorVolleyAI;
	
	private TutorVolleyPanelGame	tutorVolleyPanelGame;
	
	private TutorVolleyPlayer[]	tutorVolleyPlayer;
	
	public TutorVolleyPanelOptions( TutorVolleyMain tutorVolleyMain, BufferedImage iBackground )
	{
		super( true );
		
		this.tutorVolleyMain				= tutorVolleyMain;
		this.iBackground				= iBackground;
		
		TutorVolleyImage	tutorVolleyImage	= new TutorVolleyImage();
		
		this.tutorVolleyButton			= new TutorVolleyButton[4];
		this.tutorVolleyButton[0]			= new TutorVolleyButton( tutorVolleyImage.setPicsMain( "/images/buttons/button_quit.png", false ), 370, 270,  25, 25, "Quit" );
		this.tutorVolleyButton[1]			= new TutorVolleyButton( tutorVolleyImage.setPicsMain( "/images/buttons/button_play.png", false ), 100,  50, 200, 50, "Play" );
		this.tutorVolleyButton[2]			= new TutorVolleyButton( tutorVolleyImage.setPicsMain( "/images/buttons/button_load.png", false ),  80, 150,  40, 25, "LoadPlayerOne" );
		this.tutorVolleyButton[3]			= new TutorVolleyButton( tutorVolleyImage.setPicsMain( "/images/buttons/button_load.png", false ), 280, 150,  40, 25, "LoadPlayerTwo" );
		
		this.addMouseListener( this );
		this.addMouseMotionListener( this );
	}
	
	/**
	 * setzt die AI der beiden Spieler am Anfang auf eine bestimmt AI
	 */
	protected void setAI()
	{
		try
		{
			this.loadPlayer( 1, "Human" );
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		try {
			this.loadPlayer( 2, "Easy" );
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * gibt das SpielfeldPanel an das Optionspanel, damit die beide untereinander
	 * kommunizieren können
	 * @param tutorVolleyPanelGame
	 */
	protected void setApoVolleyPanelGame( TutorVolleyPanelGame tutorVolleyPanelGame )
	{
		this.tutorVolleyPanelGame		= tutorVolleyPanelGame;
		this.tutorVolleyPlayer		= this.tutorVolleyPanelGame.getPlayer();
	}
	
	/**
	 * wird aufgerufen, wenn auf den Play-Button gedrückt wurde
	 */
	private void setGame()
	{
		this.tutorVolleyMain.setGame();
	}
	
	/**
	 * lädt eine PlayerAI
	 * @param player = für welchen Spieler
	 * @param s = falls != NULL dann lade den übergeben String
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void loadPlayer( int player, String s ) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		String res;
		if ( s == null )
		{
			res = (String) JOptionPane.showInputDialog(this,
					"Class name:", "Load Player",
					JOptionPane.QUESTION_MESSAGE, null, null,
					this.playerName);
			if (res==null || res.length()<=0)
				return;
		} else
			res	= s;
        try
        {
        	this.playerName	= res;
        	Class c = new TutorVolleyClassLoader("player").loadClass(res);
    		Object o = c.newInstance();
    		this.tutorVolleyAI = (TutorVolleyAI)o;
    		//if ( s == null )
    			this.setAI( this.tutorVolleyAI, player );
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * setzt die PlayerAI im Spielfeld
	 * @param tutorVolleyAI = SpielerAI
	 * @param player = welcher Spieler
	 */
	private void setAI( TutorVolleyAI tutorVolleyAI, int player )
	{
		if ( this.tutorVolleyPanelGame == null )
			return;
		this.tutorVolleyPanelGame.setAI( player, tutorVolleyAI );
	}
	
	public void mouseDragged(MouseEvent arg0) {}

	/**
	 * was passiert, wenn die Maus bewegt wurde
	 */
	public void mouseMoved(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		for ( int i = 0; i < this.tutorVolleyButton.length; i++ )
		{
			if ( this.tutorVolleyButton[i].getMove( x, y ) )
			{
				this.repaint();
				return;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {}

	/**
	 * was passiert, wenn eine Maustaste gedrückt wurde
	 */
	public void mousePressed(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		for ( int i = 0; i < this.tutorVolleyButton.length; i++ )
		{
			if ( this.tutorVolleyButton[i].getPressed( x, y ) )
			{
				this.repaint();
				return;
			}
		}
	}

	/**
	 * was passiert, wenn eine Maustaste losgelassen wurde
	 */
	public void mouseReleased(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		for ( int i = 0; i < this.tutorVolleyButton.length; i++ )
		{
			if ( this.tutorVolleyButton[i].getReleased( x, y ) )
			{
				String function	= this.tutorVolleyButton[i].getFunction();
				if ( "Quit".equals( function ) )
				{
					System.exit( 0 );
				} else if ( "Play".equals( function ) )
				{
					this.setGame();
					return;
				} else if ( "LoadPlayerOne".equals( function ) )
				{
					try
					{
						this.loadPlayer( 1, null );
					} catch (ClassNotFoundException ex)
					{
						ex.printStackTrace();
					} catch (NoSuchMethodException ex)
					{
						ex.printStackTrace();
					} catch (InstantiationException ex)
					{
						ex.printStackTrace();
					} catch (IllegalAccessException ex)
					{
						ex.printStackTrace();
					} catch (InvocationTargetException ex)
					{
						ex.printStackTrace();
					}
				} else if ( "LoadPlayerTwo".equals( function ) )
				{
					try
					{
						this.loadPlayer( 2, null );
					} catch (ClassNotFoundException ex)
					{
						ex.printStackTrace();
					} catch (NoSuchMethodException ex)
					{
						ex.printStackTrace();
					} catch (InstantiationException ex)
					{
						ex.printStackTrace();
					} catch (IllegalAccessException ex)
					{
						ex.printStackTrace();
					} catch (InvocationTargetException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		this.repaint();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	


	/**
	 * malt das eigentliche OptionsPanel
	 */
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		Graphics2D gfx	= (Graphics2D)g;
		
		gfx.setColor( Color.BLACK );
		gfx.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		gfx.drawImage( this.iBackground,  0,   0, this );
		if ( this.tutorVolleyPlayer != null )
		{
			String s	= "empty";
			if ( this.tutorVolleyPlayer[0].getName() != null )
				s	= this.tutorVolleyPlayer[0].getName();
			int w = g.getFontMetrics().stringWidth( s );
			gfx.drawString( s, 100 - w/2, 205 );
			s	= "empty";
			if ( this.tutorVolleyPlayer[0].getAuthorName() != null )
				s	= this.tutorVolleyPlayer[0].getAuthorName();
			w = g.getFontMetrics().stringWidth( s );
			gfx.drawString( s, 100 - w/2, 290 );
			gfx.drawImage( this.tutorVolleyPlayer[0].getIBackground(),  84, 215, this );
			
			s	= "empty";
			if ( this.tutorVolleyPlayer[1].getName() != null )
				s	= this.tutorVolleyPlayer[1].getName();
			w = g.getFontMetrics().stringWidth( s );
			gfx.drawString( s, 300 - w/2, 205 );
			s	= "empty";
			if ( this.tutorVolleyPlayer[1].getAuthorName() != null )
				s	= this.tutorVolleyPlayer[1].getAuthorName();
			w = g.getFontMetrics().stringWidth( s );
			gfx.drawString( s, 300 - w/2, 290 );
			gfx.drawImage( this.tutorVolleyPlayer[1].getIBackground(), 284, 215, this );
		}
		
		for ( int i = 0; i < this.tutorVolleyButton.length; i++ )
		{
			this.tutorVolleyButton[i].render( gfx );
		}
	}

}
