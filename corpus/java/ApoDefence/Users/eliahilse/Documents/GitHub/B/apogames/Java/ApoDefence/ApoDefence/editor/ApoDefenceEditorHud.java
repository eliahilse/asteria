package apoDefence.editor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceImage;
import apoDefence.ApoDefenceMain;

public class ApoDefenceEditorHud extends JComponent implements MouseMotionListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	private ApoDefenceMain				main;
	private ApoDefenceButton[]			buttons;
	private ApoDefenceEditorGame		editor;
	private ApoDefenceEditorHudGeneral	general;
	private ApoDefenceEditorHudEnemy	enemy;
	private ApoDefenceEditorHudWaypoint	waypoint;
	private BufferedImage				iBackground;
	private boolean[]					aEditor;
	
	public ApoDefenceEditorHud( ApoDefenceMain main, ApoDefenceEditorGame editor )
	{
		super();
		
		this.setLayout( null );
		
		this.main		= main;
		this.editor		= editor;
		
		this.general			= new ApoDefenceEditorHudGeneral( this );
		this.enemy				= new ApoDefenceEditorHudEnemy( this );
		this.waypoint			= new ApoDefenceEditorHudWaypoint( this );
		this.waypoint.setVisible( false );
		
		this.aEditor			= new boolean[3];
		this.aEditor[0]			= true;
		
		ApoDefenceImage	image	= new ApoDefenceImage();
		
		this.iBackground		= image.getPic( "/images/editor_background.png", false );
		
		this.buttons			= new ApoDefenceButton[4];
		this.buttons[0]			= new ApoDefenceButton( image.getPic( "/images/button/button_quit.png", false ),             599, 110,  30, 30, "Quit" );
		this.buttons[1]			= new ApoDefenceButton( image.getPic( "/images/button/button_options_general.png", false ),  132,   9, 100, 45, "General" );
		this.buttons[1].setBVisible( false );
		this.buttons[2]			= new ApoDefenceButton( image.getPic( "/images/button/button_options_enemies.png", false ),  274,   9, 100, 45, "Enemies" );
		this.buttons[3]			= new ApoDefenceButton( image.getPic( "/images/button/button_options_waypoint.png", false ), 425,   9, 100, 45, "Waypoint" );
		
		this.addMouseListener( this );
		this.addMouseMotionListener( this );
	}
	
	public ApoDefenceEditorGame getEditor()
	{
		return this.editor;
	}

	public ApoDefenceEditorHudWaypoint getWaypoint()
	{
		return this.waypoint;
	}

	public void mouseDragged(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		if ( this.aEditor[0] )
			this.general.mouseMoved( x, y );
		else if ( this.aEditor[1] )
			this.enemy.mouseMoved( x, y );
		else if ( this.aEditor[2] )
			this.waypoint.mouseMoved( x, y );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getMove( x, y ) )
			{
				this.repaint();
				return;
			}
		}
	}

	public void mouseClicked(MouseEvent arg0) {}

	public void mousePressed(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		if ( this.aEditor[0] )
			this.general.mousePressed( x, y );
		else if ( this.aEditor[1] )
			this.enemy.mousePressed( x, y );
		else if ( this.aEditor[2] )
			this.waypoint.mousePressed( x, y );
		
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
		
		if ( this.aEditor[0] )
			this.general.mouseReleased( x, y );
		else if ( this.aEditor[1] )
			this.enemy.mouseReleased( x, y );
		else if ( this.aEditor[2] )
			this.waypoint.mouseReleased( x, y );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getReleased( x, y ) )
			{
				String function	= this.buttons[i].getFunction();
				if ( "Quit".equals( function ) )
				{
					this.main.setOptions();
				} else if ( "General".equals( function ) )
				{
					this.editor.setBWaypoint( false );
					this.editor.repaint();
					this.setButton( 1 );
					this.general.setVisible( true );
					this.waypoint.setVisible( false );
				}
				else if ( "Enemies".equals( function ) )
				{
					this.editor.setBWaypoint( false );
					this.editor.repaint();
					this.setButton( 2 );
					this.general.setVisible( false );
					this.waypoint.setVisible( false );
				}
				else if ( "Waypoint".equals( function ) )
				{
					this.editor.setBWaypoint( true );
					this.editor.repaint();
					this.setButton( 3 );
					this.general.setVisible( false );
					this.waypoint.setVisible( true );
				}
			}
		}
		this.repaint();
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}
	
	public void setButton( int button )
	{
		for ( int i = 0; i < 3; i++ )
		{
			if ( button == i+1 )
			{
				this.buttons[i+1].setBVisible( false );
				this.aEditor[i]		= true;
			}
			else
			{
				this.buttons[i+1].setBVisible( true );
				this.aEditor[i]		= false;
			}
		}
	}
	
	public int getMoney()
	{
		return this.general.getMoney();
	}

	public int getTowerCount()
	{
		return this.general.getTowerCount();
	}
	
	public int getWaves()
	{
		return this.general.getWaves();
	}
	
	public void setWaves( int waves )
	{
		this.general.setWaves( waves );
	}
	
	public ArrayList<Integer> getEnemies()
	{
		return this.enemy.getEnemies();
	}
	
	public void setEnemies(ArrayList<Integer> enemies)
	{
		this.enemy.setEnemies( enemies );
	}
	
	/**
	 * malt das eigentliche Spielfeld mit Spielern usw.
	 */
	public void paintComponent(Graphics gfx) 
	{
		super.paintComponent(gfx);
		
		Graphics2D g	= (Graphics2D)gfx;
		
		g.drawImage( this.iBackground, 0, 0, null );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
		
		if ( this.aEditor[0] )
			this.general.render( g );
		else if ( this.aEditor[1] )
			this.enemy.render( g );
		else if ( this.aEditor[2] )
			this.waypoint.render( g );
	}

}
