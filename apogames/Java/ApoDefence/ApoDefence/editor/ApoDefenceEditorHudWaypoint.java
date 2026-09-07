package apoDefence.editor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JTextField;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceDragObject;
import apoDefence.ApoDefenceImage;

public class ApoDefenceEditorHudWaypoint
{
	private ApoDefenceEditorHud 		hud;
	private BufferedImage				iBackground;
	private ApoDefenceButton[]			buttons;
	private JTextField					fieldX, fieldY;
	private ArrayList<ApoDefenceDragObject>	waypoints;
	private int							currentWaypoint;
	
	public ApoDefenceEditorHudWaypoint( ApoDefenceEditorHud hud )
	{
		super();

		this.hud		= hud;
		
		this.fieldX			= new JTextField( "0" );
		this.fieldX.setSize( 40, 20 );
		this.fieldX.setLocation( 50, 75 );
		
		this.hud.add( this.fieldX );
		
		this.fieldY			= new JTextField( "0" );
		this.fieldY.setSize( 40, 20 );
		this.fieldY.setLocation( 50, 97 );
		
		this.waypoints		= new ArrayList<ApoDefenceDragObject>();
		this.currentWaypoint	= -1;
		
		this.hud.add( this.fieldY );
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		
		BufferedImage	button_up	= image.getPic( "/images/button/button_options_up.png", false );
		BufferedImage	button_down	= image.getPic( "/images/button/button_options_down.png", false );
		
		this.buttons				= new ApoDefenceButton[5];
		this.buttons[0]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_set.png", false ),   95,  80, 50, 30, "Set" );
		this.buttons[1]				= new ApoDefenceButton( button_up,       												 25,  91, 20, 20, "UpWaypoint" );
		this.buttons[2]				= new ApoDefenceButton( button_down,     												150,  91, 20, 20, "DownWaypoint" );
		this.buttons[3]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_add.png", false ),  200,  60, 50, 30, "AddWaypoint" );
		this.buttons[4]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_del.png", false ),  200, 100, 50, 30, "DelWaypoint" );
	}
	
	public void setVisible( boolean bVisible )
	{
		this.fieldX.setVisible( bVisible );
		this.fieldY.setVisible( bVisible );
	}

	public ArrayList<ApoDefenceDragObject> getWaypoints()
	{
		return this.waypoints;
	}

	public void setWaypoints( ArrayList<ApoDefenceDragObject> waypoints )
	{
		this.waypoints	= waypoints;
		if ( this.waypoints.size() <= 0 )
		{
			this.fieldX.setText( "-1" );
			this.fieldY.setText( "-1" );
		}
	}
	
	public void setCurrentWaypoint( int i )
	{
		this.currentWaypoint	= i;
		this.fieldX.setText( ""+(int)this.waypoints.get( i ).getX() );
		this.fieldY.setText( ""+(int)this.waypoints.get( i ).getY() );
	}
	
	private void setSet()
	{
		if ( ( this.hud.getEditor() != null ) && ( this.currentWaypoint != -1 ) )
		{
			int x	= 0;
			try
			{
				x = Integer.parseInt( this.fieldX.getText());
				if ( x < 0 )
					x	= 0;
				else if ( x >= this.hud.getEditor().getXLevel() * 32 )
					x	= this.hud.getEditor().getXLevel() * 32 - 1;
				this.fieldX.setText( ""+x );
			}
			catch (NumberFormatException ex)
			{
				x	= 0;
				this.fieldX.setText( ""+x );
			}
			int y	= 0;
			try
			{
				y = Integer.parseInt( this.fieldY.getText());
				if ( y < 0 )
					y	= 0;
				else if ( y >= this.hud.getEditor().getYLevel() * 32 )
					y	= this.hud.getEditor().getYLevel() * 32 - 1;
				this.fieldY.setText( ""+y );
			}
			catch (NumberFormatException ex)
			{
				y	= 0;
				this.fieldY.setText( ""+y );
			}
			if ( this.currentWaypoint != -1 )
			{
				this.waypoints.get( this.currentWaypoint ).setX( x );
				this.waypoints.get( this.currentWaypoint ).setY( y );
				this.hud.getEditor().repaint();
			}
		}
	}
	
	public void mouseMoved( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getMove( x, y ) )
			{
				this.hud.repaint();
				return;
			}
		}
	}
	
	public void mouseReleased( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getReleased( x, y ) )
			{
				String function	= this.buttons[i].getFunction();
				if ( "Set".equals( function ) )
				{
					this.setSet();
				} else if ( "AddWaypoint".equals( function ) )
				{
					this.hud.getEditor().makeWayPoint( 100, 100, 16, 16 );
				} else if ( "DelWaypoint".equals( function ) )
				{
					if ( this.currentWaypoint != -1 )
					{
						this.hud.getEditor().deleteWayPoint( this.currentWaypoint );
						if ( this.currentWaypoint == i )
						{
							this.currentWaypoint--;
							if ( ( this.currentWaypoint < 0 ) && ( this.waypoints.size() > 0 ) )
								this.currentWaypoint	= 0;
						}
						this.hud.getEditor().repaint();
					}
				} else if ( "UpWaypoint".equals( function ) )
				{
					this.currentWaypoint++;
					if ( this.currentWaypoint >= this.waypoints.size() )
						this.currentWaypoint	= 0;
					this.hud.getEditor().setCurrentWay( this.currentWaypoint );
					this.setCurrentWaypoint( this.currentWaypoint );
				} else if ( "DownWaypoint".equals( function ) )
				{
					this.currentWaypoint--;
					if ( this.currentWaypoint < 0 )
						this.currentWaypoint	= this.waypoints.size() - 1;
					this.hud.getEditor().setCurrentWay( this.currentWaypoint );
					this.setCurrentWaypoint( this.currentWaypoint );
				}
				this.hud.repaint();
			}
		}
	}
	
	public void mousePressed( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getPressed( x, y ) )
			{
				this.hud.repaint();
				return;
			}
		}
	}
	
	public void render( Graphics2D g )
	{
		g.drawImage( this.iBackground, 8, 55, null );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
	}

}
