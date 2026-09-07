package apoDefence.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceImage;

public class ApoDefenceEditorHudEnemy
{
	private ApoDefenceEditorHud 		hud;
	private BufferedImage				iEnemies;
	private ApoDefenceButton[]			buttons;
	private int							currentEnemy;
	private ArrayList<Integer>			enemies;
	
	public ApoDefenceEditorHudEnemy( ApoDefenceEditorHud hud )
	{
		super();

		this.hud		= hud;
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		this.iEnemies				= image.getPic( "/images/editor_enemies.png", false );
		
		BufferedImage	button_up	= image.getPic( "/images/button/button_options_up.png", false );
		BufferedImage	button_down	= image.getPic( "/images/button/button_options_down.png", false );
		
		this.buttons				= new ApoDefenceButton[6];
		this.buttons[0]				= new ApoDefenceButton( button_up,       												 52,  58, 20, 20, "UpEnemy" );
		this.buttons[1]				= new ApoDefenceButton( button_down,     												 52, 120, 20, 20, "DownEnemy" );
		this.buttons[2]				= new ApoDefenceButton( button_up,      												535,  91, 20, 20, "NextEnemy" );
		this.buttons[3]				= new ApoDefenceButton( button_down,     												 25,  91, 20, 20, "PreviousEnemy" );
		this.buttons[4]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_add.png", false ),  535,  55, 50, 30, "AddEnemy" );
		this.buttons[5]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_del.png", false ),  535, 115, 50, 30, "DelEnemy" );
		
		this.currentEnemy			= 0;
		
		this.enemies				= new ArrayList<Integer>();
		this.enemies.add( ApoDefenceConstants.ENEMY_BIRD );
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
				if ( "UpEnemy".equals( function ) )
				{
					int value	= this.enemies.get( this.currentEnemy );
					value++;
					if ( value > ApoDefenceConstants.ENEMY_WEREWOLF )
						value	= ApoDefenceConstants.ENEMY_BIRD;
					this.enemies.set( this.currentEnemy, value );
				} else if ( "DownEnemy".equals( function ) )
				{
					int value	= this.enemies.get( this.currentEnemy );
					value--;
					if ( value < ApoDefenceConstants.ENEMY_BIRD )
						value	= ApoDefenceConstants.ENEMY_WEREWOLF;
					this.enemies.set( this.currentEnemy, value );
				} else if ( "NextEnemy".equals( function ) )
				{
					this.currentEnemy++;
					this.buttons[0].setX( this.buttons[0].getX() + 24 );
					this.buttons[1].setX( this.buttons[1].getX() + 24 );
					if ( this.currentEnemy >= this.enemies.size() )
					{
						this.currentEnemy	= 0;
						this.buttons[0].setX( 52 );
						this.buttons[1].setX( 52 );
					}
					if ( ( this.currentEnemy - 10 > 0 ) &&
						 ( this.currentEnemy + 9 < this.enemies.size() ) )
					{
						this.buttons[0].setX( this.buttons[0].getX() - 24 );
						this.buttons[1].setX( this.buttons[1].getX() - 24 );
					}
				} else if ( "PreviousEnemy".equals( function ) )
				{
					this.currentEnemy--;
					this.buttons[0].setX( this.buttons[0].getX() - 24 );
					this.buttons[1].setX( this.buttons[1].getX() - 24 );
					if ( this.currentEnemy < 0 )
					{
						this.currentEnemy	= this.enemies.size() - 1;
						int value			= 0;
						if ( this.currentEnemy >= 19 )
							value			= 52 + 19 * 24;
						else
							value			= 52 + this.currentEnemy * 24;
						this.buttons[0].setX( value );
						this.buttons[1].setX( value );
					}
					if ( 	( this.currentEnemy - 10 >= 0 ) &&
							( this.currentEnemy + 10 < this.enemies.size() ) )
					{
						this.buttons[0].setX( this.buttons[0].getX() + 24 );
						this.buttons[1].setX( this.buttons[1].getX() + 24 );
					}
				} else if ( ( "DelEnemy".equals( function ) ) && ( this.enemies.size() > 1 ) )
				{
					
					if ( ( this.currentEnemy + 10 >= this.enemies.size() ) &&
						 ( this.enemies.size() > 20 ) &&
						 ( this.currentEnemy != this.enemies.size() - 1 ) )
					{
							this.buttons[0].setX( this.buttons[0].getX() + 24 );
							this.buttons[1].setX( this.buttons[1].getX() + 24 );
					}
					if ( this.currentEnemy == this.enemies.size() - 1 )
					{
						this.currentEnemy = this.enemies.size() - 2;
					}
					if ( this.enemies.size() <= 20 )
					{
						this.buttons[0].setX( this.currentEnemy * 24 + 52 );
						this.buttons[1].setX( this.currentEnemy * 24 + 52 );
					}
					this.enemies.remove( this.enemies.size() - 1 );
				} else if ( "AddEnemy".equals( function ) )
				{
					this.enemies.add( ApoDefenceConstants.ENEMY_BIRD );
					if ( this.enemies.size() > 20 )
					{
						if ( this.currentEnemy + 11 > this.enemies.size() )
						{
							this.buttons[0].setX( this.buttons[0].getX() - 24 );
							this.buttons[1].setX( this.buttons[1].getX() - 24 );
						}
					}
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
	
	public ArrayList<Integer> getEnemies()
	{
		return this.enemies;
	}
	
	public void setEnemies(ArrayList<Integer> enemies)
	{
		this.enemies = enemies;
	}

	public void render( Graphics2D g )
	{
		int x		= 0;
		int startValue	= this.currentEnemy - 10;
		if ( startValue + 19 >= this.enemies.size() )
			startValue	= this.enemies.size() - 20;
		if ( startValue < 0 )
			startValue	= 0;
		g.setColor( Color.WHITE );
		for ( int i = startValue; i < this.enemies.size() && i < startValue + 20; i++ )
		{
			int value	= this.enemies.get( i );
			g.drawImage( this.iEnemies, 50 + x, 55 + 30, 50 + x + 24, 55 + 30 + 32, 0 + ( value - 1 ) * 24, 0, 0 + ( value + 0 ) * 24, 32, null );
			g.drawString( ""+(i+1), 53 + x, 140 );
			if ( i == this.currentEnemy )
			{
				g.drawRect( 49 + x, 55, 25, 88 );
			}
			x			+= 24;
		}
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
	}

}
