package apoCheating;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ApoCheatingHudPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long 	serialVersionUID = 1L;
	
	private Font							fontTitle, fontNormal, fontPlayer;
	private ApoCheatingButton[]				buttons;
	private ArrayList<ApoCheatingPlayer>	player;
	
	private BufferedImage					iF, iArrow, iBackground;

	private ApoCheatingGamePanel			apoCheatingGamePanel;
	
	private String							levelName;
	
	private boolean							bRandom, bDetect;

	public ApoCheatingHudPanel()
	{
		super( true );
		
		this.addMouseListener( this );
		this.addMouseMotionListener( this );
	}
	
	public void init()
	{
		this.fontPlayer			= new Font( "Dialog", Font.BOLD, 18 );
		this.fontNormal			= new Font( "Dialog", Font.BOLD, 13 );
		this.fontTitle			= new Font( "Dialog", Font.BOLD, 20 );
		
		ApoCheatingImage image	= new ApoCheatingImage();
		
		this.iF					= image.setPicsMain( "/images/f.png", false );
		this.iArrow				= image.setPicsMain( "/images/arrow.png", false );
		//this.iBackground		= image.setPicsMain( "/images/hud_background.png", false );
		this.iBackground		= this.getIBackground();
		
		this.buttons			= new ApoCheatingButton[13];
		this.buttons[0]			= new ApoCheatingButton( image.setPicsMain( "/images/button/button_quit.png", false ),     135, 455, 25, 25, "Quit" );
		this.buttons[1]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_load.png", false ),         60, 260, 41, 18, "Load" );
		this.buttons[2]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_play.png", false ),         30,  60, 42, 22, "Play" );
		this.buttons[3]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_stop.png", false ),         90,  60, 43, 21, "Stop" );
		this.buttons[4]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_reload.png", false ),       10,  61, 60, 18, "Reload" );
		this.buttons[4].setBVisible( false );
		this.buttons[5]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_arrow_left.png", false ),    5, 300, 20, 14, "previousLevel" );
		this.buttons[6]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_arrow_right.png", false ), 135, 300, 20, 14, "nextLevel" );
		this.buttons[7]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_next.png", false ),         92,  61, 48, 18, "Next" );
		this.buttons[7].setBVisible( false );
		this.buttons[8]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_random.png", false ),       47, 341, 66, 19, "Random" );
		this.buttons[9]			= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_original.png", false ),     43, 340, 74, 21, "Original" );
		this.buttons[9].setBVisible( false );
		this.buttons[10]		= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_new.png", false ),         110, 370, 35, 18, "New" );
		this.buttons[10].setBVisible( false );
		this.buttons[11]		= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_on.png", false ),          115, 400, 24, 18, "On" );
		this.buttons[11].setBVisible( false );
		this.buttons[12]		= new ApoCheatingButton( image.setPicsMain( "/images/button/hud_off.png", false ),         110, 400, 31, 18, "Off" );
		this.buttons[12].setBVisible( false );
		
		this.bRandom			= false;
		this.bDetect			= false;
	}
	
	private BufferedImage getIBackground()
	{
		BufferedImage iBackground = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( this.getWidth(), this.getHeight(), Transparency.TRANSLUCENT );
		
		Graphics2D g = (Graphics2D)iBackground.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor( Color.WHITE );
		g.fillRect( 0, 0, iBackground.getWidth(), iBackground.getHeight() );
		
		g.setColor( Color.BLUE );
		for ( int i = 0; i < iBackground.getHeight() / 16; i++ ) {
			if ( i * 16 < iBackground.getWidth() )
				g.drawLine( i * 16, 0, i * 16, iBackground.getHeight() );
			
			g.drawLine( 0, i * 16, iBackground.getWidth(), i * 16 );
		}
		
		g.setFont( this.fontTitle );
		String title	= "Apo-Cheating";
		int w			= g.getFontMetrics().stringWidth( title );
		g.setColor( Color.BLACK );
		g.drawString( title, this.getWidth()/2 - w/2 + 1, 31 );
		g.setColor( Color.RED );
		g.drawString( title, this.getWidth()/2 - w/2, 30 );
		
		Stroke clone = g.getStroke();
		
		g.setColor( Color.BLACK );
		g.setStroke( new BasicStroke( 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER ) );
		g.drawLine( 2,  47, this.getWidth() - 2, 47 );
		g.drawLine( 2, 330, this.getWidth() - 2, 330 );
		
		int y = 123;
		
		g.setColor( Color.BLACK );
		g.setStroke( new BasicStroke( 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER ) );
		g.drawLine( 2, 121 + y, this.getWidth() - 2, 121 + y );
		g.drawLine( 2, y - 33, this.getWidth() - 2, y - 33 );
		
		g.setStroke( clone );
		
		g.dispose();
		
		return iBackground;
	}
	
	protected ArrayList<ApoCheatingPlayer> getPlayer()
	{
		return this.player;
	}

	protected void setPlayer(ArrayList<ApoCheatingPlayer> player)
	{
		this.player = player;
	}

	protected ApoCheatingGamePanel getApoCheatingGamePanel()
	{
		return this.apoCheatingGamePanel;
	}

	protected void setApoCheatingGamePanel(ApoCheatingGamePanel apoCheatingGamePanel)
	{
		this.apoCheatingGamePanel = apoCheatingGamePanel;
		
		this.levelName	= this.apoCheatingGamePanel.getLevelName();
	}
	
	protected void setReloadButton( boolean bWin )
	{
		this.buttons[1].setBVisible( false );
		this.buttons[2].setBVisible( false );
		this.buttons[3].setBVisible( false );
		this.buttons[4].setBVisible( true );
		this.buttons[5].setBVisible( false );
		this.buttons[6].setBVisible( false );
		this.buttons[7].setBVisible( bWin );
	}
	
	private void save()
	{
		if ( this.apoCheatingGamePanel != null )
			this.apoCheatingGamePanel.save();
	}
	
	private void load()
	{
		if ( this.apoCheatingGamePanel != null )
		{
			this.apoCheatingGamePanel.load( null );
			this.levelName	= this.apoCheatingGamePanel.getLevelName();
		}
	}
	
	private void play()
	{
		if ( ( this.apoCheatingGamePanel != null ) && ( !this.apoCheatingGamePanel.isBThread() ) )
		{
			this.buttons[1].setBVisible( false );
			this.buttons[4].setBVisible( false );
			this.buttons[5].setBVisible( false );
			this.buttons[6].setBVisible( false );
			this.buttons[7].setBVisible( false );
			this.buttons[8].setBVisible( false );
			this.buttons[9].setBVisible( false );
			this.buttons[10].setBVisible( false );
			this.buttons[11].setBVisible( false );
			this.buttons[12].setBVisible( false );
			this.apoCheatingGamePanel.start();
		}
	}
	
	private void stop()
	{
		if ( ( this.apoCheatingGamePanel != null ) && ( this.apoCheatingGamePanel.isBThread() ) )
		{
			this.buttons[1].setBVisible( true );
			this.buttons[2].setBVisible( true );
			this.buttons[3].setBVisible( true );
			this.buttons[4].setBVisible( false );
			this.buttons[5].setBVisible( true );
			this.buttons[6].setBVisible( true );
			this.buttons[7].setBVisible( false );
			this.setRandomButton();
			this.apoCheatingGamePanel.stop();
		}
	}
	
	private void reload()
	{
		if ( ( this.apoCheatingGamePanel != null ) && ( !this.apoCheatingGamePanel.isBThread() ) )
		{
			this.buttons[1].setBVisible( true );
			this.buttons[2].setBVisible( true );
			this.buttons[3].setBVisible( true );
			this.buttons[4].setBVisible( false );
			this.buttons[5].setBVisible( true );
			this.buttons[6].setBVisible( true );
			this.buttons[7].setBVisible( false );
			this.setRandomButton();
			this.apoCheatingGamePanel.stop();
			this.levelName	= this.apoCheatingGamePanel.getLevelName();
		}
	}
	
	private void setRandomButton()
	{
		if ( bRandom )
		{
			this.buttons[8].setBVisible( false );
			this.buttons[9].setBVisible( true );
			this.buttons[10].setBVisible( true );
			this.buttons[11].setBVisible( !this.bDetect );
			this.buttons[12].setBVisible( this.bDetect );
		} else
		{
			this.buttons[8].setBVisible( true );
			this.buttons[9].setBVisible( false );
			this.buttons[10].setBVisible( false );
			this.buttons[11].setBVisible( false );
			this.buttons[12].setBVisible( false );
		}
	}
	
	private void next()
	{
		if ( ( this.apoCheatingGamePanel != null ) && ( !this.apoCheatingGamePanel.isBThread() ) )
		{
			this.apoCheatingGamePanel.next();
			this.bRandom	= false;
			this.bDetect	= false;
			this.setRandomButton();
			this.reload();
		}
	}
	
	private void previous()
	{
		if ( ( this.apoCheatingGamePanel != null ) && ( !this.apoCheatingGamePanel.isBThread() ) )
		{
			if ( this.apoCheatingGamePanel.previous() )
			{
				this.reload();
			}
		}
	}
	
	private void setRandom()
	{
		this.bRandom	= true;
		this.setRandomButton();
	}
	
	private void setOriginal()
	{
		this.bRandom	= false;
		this.setRandomButton();
		if ( ( this.apoCheatingGamePanel != null ) && ( !this.apoCheatingGamePanel.isBThread() ) )
		{
			this.apoCheatingGamePanel.setOriginal();
		}
	}
	
	private void setNew()
	{
		if ( ( this.apoCheatingGamePanel != null ) && ( !this.apoCheatingGamePanel.isBThread() ) )
		{
			this.apoCheatingGamePanel.setNewRandom( this.bDetect );
		}
	}
	
	private void setDetect( boolean bDetect )
	{
		if ( bDetect )
		{
			this.buttons[12].setBVisible( true );
			this.buttons[11].setBVisible( false );
		} else
		{
			this.buttons[12].setBVisible( false );
			this.buttons[11].setBVisible( true );
		}
		this.bDetect	= bDetect;
		if ( ( this.apoCheatingGamePanel != null ) && ( !this.apoCheatingGamePanel.isBThread() ) )
		{
			this.apoCheatingGamePanel.setDetect( this.bDetect );
		}
		this.repaint();
	}
	
	public void mouseClicked(MouseEvent arg0) {}

	public void mousePressed(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if (this.buttons[i].getPressed( x, y ))
			{
				this.repaint();
				break;
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if (this.buttons[i].getReleased( x, y ))
			{
				String function	= this.buttons[i].getFunction();
				if ( "Quit".equals( function ) )
				{
					System.exit( 0 );
				} else if ( "Save".equals( function ) )
				{
					this.save();
					this.buttons[i].setBOver( false );
				} else if ( "Load".equals( function ) )
				{
					this.load();
					this.buttons[i].setBOver( false );
				} else if ( "Play".equals( function ) )
				{
					this.play();
				} else if ( "Stop".equals( function ) )
				{
					this.stop();
				} else if ( "Reload".equals( function ) )
				{
					this.reload();
				} else if ( "Next".equals( function ) )
				{
					this.next();
				} else if ( "nextLevel".equals( function ) )
				{
					this.next();
				} else if ( "previousLevel".equals( function ) )
				{
					this.previous();
				} else if ( "Random".equals( function ) )
				{
					this.setRandom();
				} else if ( "Original".equals( function ) )
				{
					this.setOriginal();
				} else if ( "New".equals( function ) )
				{
					this.setNew();
				} else if ( "On".equals( function ) )
				{
					this.setDetect( true );
					break;
				} else if ( "Off".equals( function ) )
				{
					this.setDetect( false );
					break;
				}
			}
		}
		this.repaint();
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mouseDragged(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if (this.buttons[i].getMove( x, y ))
			{
				this.repaint();
				break;
			}
		}
	}
	
	/**
	 * malt das eigentliche Spielfeld mit Spielern usw.
	 */
	public void paintComponent(Graphics g) 
	{
		//long time	= System.nanoTime();
		
		super.paintComponent(g);
		
		Graphics2D gfx	= (Graphics2D)g;
		
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		this.drawBackground( gfx );
		
		this.drawPlayer( gfx );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( gfx );
		}
		
		g.setColor( Color.BLACK );
		g.setFont( this.fontPlayer );
		if ( 	( this.levelName != null ) &&
				( this.buttons[2].isBVisible() ) )
		{
			int w	= g.getFontMetrics().stringWidth( this.levelName );
			g.drawString( this.levelName, this.getWidth()/2-w/2, 312 );
		}
		if ( this.buttons[10].isBVisible() )
		{
			g.drawString( "Random", 5, 385 );
		}
		if ( ( this.buttons[11].isBVisible() ) ||
			 ( this.buttons[12].isBVisible() ) )
		{
			g.drawString( "Detect", 5, 415 );
		}
				
		//System.out.println("time = "+( System.nanoTime() - time ));
	}
	
	private void drawBackground( Graphics2D g )
	{
		g.drawImage( this.iBackground, 0, 0, this );
	}
	
	private void drawPlayer( Graphics2D g )
	{
		g.setFont( this.fontNormal );
		int x	= 3;
		if ( this.player.size() == 1 )
			x	= this.getWidth()/3;
		int y	= 123;
		for ( int i = 0; i < this.player.size(); i++ )
		{
			g.setColor( Color.BLACK );
			g.setFont( this.fontPlayer );
			g.drawString( this.player.get( i ).getName(), x, y - 13 );
			g.setFont( this.fontNormal );
			g.drawString( "Cheated:", 5 + x, y + 18 );
			g.drawRect( 4 + x, y + 24, 51, 11 );
			g.drawString( (int)this.player.get( i ).getCheated()+" %", 15 + x, y + 48 );
			g.setColor( this.player.get( i ).getPercentColor() );
			g.fillRect( 5 + x, y + 25, (int)this.player.get( i ).getCheated()/2, 10 );

			g.setColor( Color.BLACK );
			g.drawString( "Detected:", 5 + x, y + 66 );
			g.drawString( "Coins: "+this.player.get( i ).getCoins(), x + 2, y + 115 );
			g.drawRect( 4 + x, y + 72, 51, 11 );
			g.drawString( (int)this.player.get( i ).getDetected()+" %", 15 + x, y + 96 );
			g.setColor( this.player.get( i ).getCheatColor() );
			g.fillRect( 5 + x, y + 73, (int)this.player.get( i ).getDetected()/2, 10 );
			
			if ( this.player.size() > 1 )
			{
				g.setColor( Color.BLACK );
				g.fillRect( this.getWidth()/2 - 1, y - 30, 2, 147 );
			} else
			{
				g.drawImage( this.iF, 120, y + 3, this );
				g.drawImage( this.iArrow,  10, y + 11, this );
			}
			x		+= this.getWidth()/this.player.size() + 5;
		}
	}

}
