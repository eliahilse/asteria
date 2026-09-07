package apoDefence.options;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceImage;
import apoDefence.ApoDefenceMain;
import apoDefence.game.ApoDefenceGame;

public class ApoDefenceOptions extends JComponent implements KeyListener, MouseListener, MouseMotionListener
{
	private static final long 		serialVersionUID = 1L;

	private BufferedImage			iBackground, iOptions;
	private ApoDefenceButton[]		buttons;
	
	private ApoDefenceOptionsGeneral	general;
	private ApoDefenceOptionsResearch	research;
	private ApoDefenceOptionsTower		tower;
	private ApoDefenceOptionsEnemy		enemy;
	
	private boolean					bOptions;
	
	private boolean[]				aOptions;
	
	private ApoDefenceMain			main;
	
	private ApoDefenceGame 			game;
	
	public ApoDefenceOptions( ApoDefenceMain main, ApoDefenceGame game )
	{
		super();
		
		this.main					= main;
		this.game					= game;
		
		this.bOptions				= false;
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		this.iBackground			= image.getPic( "/images/first.png", false );
		this.iOptions				= image.getPic( "/images/first_options.png", false );
		
		this.buttons				= new ApoDefenceButton[9];
		this.buttons[0]				= new ApoDefenceButton( image.getPic( "/images/button/button_play.png" , false),       457, 117, 166, 36, "Play" );
		this.buttons[1]				= new ApoDefenceButton( image.getPic( "/images/button/button_load_first.png" , false), 457, 166, 166, 36, "Load" );
		this.buttons[2]				= new ApoDefenceButton( image.getPic( "/images/button/button_options.png" , false),    457, 215, 166, 36, "Options" );
		this.buttons[3]				= new ApoDefenceButton( image.getPic( "/images/button/button_exit.png" , false),       457, 314, 166, 36, "Exit" );
		this.buttons[4]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor.png" , false),     457, 263, 166, 36, "Editor" );
		
		this.buttons[5]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_general.png" , false),      23, 199, 100, 45, "General" );
		this.buttons[5].setBVisible( false );
		this.buttons[6]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_research.png" , false),    123, 199, 100, 45, "Research" );
		this.buttons[6].setBVisible( false );
		this.buttons[7]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_towers.png" , false),      223, 199, 100, 45, "Towers" );
		this.buttons[7].setBVisible( false );
		this.buttons[8]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_enemies.png" , false),     323, 199, 100, 45, "Enemies" );
		this.buttons[8].setBVisible( false );
		
		this.aOptions				= new boolean[4];
		this.aOptions[0]			= true;
		
		this.general				= new ApoDefenceOptionsGeneral( this );
		this.research				= new ApoDefenceOptionsResearch( this );
		this.tower					= new ApoDefenceOptionsTower( this );
		this.enemy					= new ApoDefenceOptionsEnemy( this );
		
		this.addMouseListener( this );
		this.addMouseMotionListener( this );
	}
	
	public boolean isOriginal()
	{
		if ( 	( this.general.isBOriginal() ) && ( this.research.isBOriginal() ) && 
				( this.tower.isBOriginal() ) && ( this.enemy.isBOriginal() ) )
			return true;
		else
			return false;
	}
	
	public ApoDefenceGame getGame()
	{
		return this.game;
	}
	
	public void keyTyped(KeyEvent arg0) {}

	public void keyPressed(KeyEvent arg0)
	{
	}

	public void keyReleased(KeyEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent arg0) {}

	public void mousePressed(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		if ( 	( x > 5 ) && ( x < 439 ) &&
				( y > 200 ) && ( y < 480 ) && ( this.bOptions ) )
		{
			if ( this.aOptions[0] )
			{
				this.general.mousePressed( x, y );
			} else if ( this.aOptions[1] )
				this.research.mousePressed( x, y );
			else if ( this.aOptions[2] )
				this.tower.mousePressed( x, y );
			else if ( this.aOptions[3] )
				this.enemy.mousePressed( x, y );
		}
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getPressed( x, y ) )
			{
				this.repaint();
				return;
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		if ( 	( x > 5 ) && ( x < 439 ) &&
				( y > 200 ) && ( y < 480 ) && ( this.bOptions ) )
		{
			if ( this.aOptions[0] )
			{
				this.general.mouseReleased( x, y );
			}else if ( this.aOptions[1] )
				this.research.mouseReleased( x, y );
			else if ( this.aOptions[2] )
				this.tower.mouseReleased( x, y );
			else if ( this.aOptions[3] )
				this.enemy.mouseReleased( x, y );
		}
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getReleased( x, y ) )
			{
				this.buttons[i].setBOver( false );
				String function	= this.buttons[i].getFunction();
				if ( "Exit".equals( function ) )
				{
					System.exit( 0 );
				} else if ( "Play".equals( function ) )
				{
					this.main.setGame();
				} else if ( "Options".equals( function ) )
				{
					this.bOptions	= !this.bOptions;
					this.setOptions( this.bOptions );
				} else if ( "Load".equals( function ) )
				{
					this.main.getGame().setLoad( null );
				} else if ( "Editor".equals( function ) )
				{
					this.main.setEditor();
				} else if ( "General".equals( function ) )
				{
					this.setOptionsArray( ApoDefenceConstants.OPTIONS_GENERAL );
				} else if ( "Research".equals( function ) )
				{
					this.setOptionsArray( ApoDefenceConstants.OPTIONS_RESEARCH );
				} else if ( "Towers".equals( function ) )
				{
					this.setOptionsArray( ApoDefenceConstants.OPTIONS_TOWERS );
				} else if ( "Enemies".equals( function ) )
				{
					this.setOptionsArray( ApoDefenceConstants.OPTIONS_ENEMIES );
				}
				this.repaint();
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mouseDragged(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		if ( 	( x > 5 ) && ( x < 439 ) &&
				( y > 200 ) && ( y < 480 ) && ( this.bOptions ) )
		{
			if ( this.aOptions[0] )
				this.general.mouseMoved( x, y );
			else if ( this.aOptions[1] )
				this.research.mouseMoved( x, y );
			else if ( this.aOptions[2] )
				this.tower.mouseMoved( x, y );
			else if ( this.aOptions[3] )
				this.enemy.mouseMoved( x, y );
		}
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getMove( x, y ) )
			{
				this.repaint();
				return;
			}
		}
	}
	
	private void setOptions( boolean bOption )
	{
		for ( int i = 0; i < 4; i++ )
		{
			if ( ( bOption ) && ( this.aOptions[i] ) )
				this.buttons[i+5].setBVisible( !bOption );
			else
				this.buttons[i+5].setBVisible( bOption );
		}
	}
	
	private void setOptionsArray( int value )
	{
		for ( int i = 0; i < this.aOptions.length; i++ )
		{
			if ( i == value )
			{
				this.aOptions[i]	= true;
				this.buttons[i+5].setBVisible( false );
			} else
			{
				this.aOptions[i]	= false;
				this.buttons[i+5].setBVisible( true );
			}
		}
	}
	
	/**
	 * malt das eigentliche Spielfeld mit Spielern usw.
	 */
	public void paintComponent( Graphics gfx ) 
	{
		super.paintComponent(gfx);
		
		Graphics2D g	= (Graphics2D)gfx;
		
		g.drawImage( this.iBackground, 0, 0, null );
		
		if ( this.bOptions )
			g.drawImage( this.iOptions, 7, 144, null );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
		
		if ( this.bOptions )
		{
			if ( this.aOptions[0] )
			{
				this.general.render( g );
			} else if ( this.aOptions[1] )
				this.research.render( g );
			else if ( this.aOptions[2] )
				this.tower.render( g );
			else if ( this.aOptions[3] )
				this.enemy.render( g );
		}
	}

}
