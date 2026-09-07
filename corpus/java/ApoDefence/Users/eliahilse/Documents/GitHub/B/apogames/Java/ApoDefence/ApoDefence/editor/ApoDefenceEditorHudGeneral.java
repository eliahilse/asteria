package apoDefence.editor;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceImage;

public class ApoDefenceEditorHudGeneral
{
	private ApoDefenceEditorHud 		hud;
	private BufferedImage				iBackground;
	private ApoDefenceButton[]			buttons;
	private JTextField					fieldX, fieldY, fieldMoney, fieldTower, fieldWaves;
	
	public ApoDefenceEditorHudGeneral( ApoDefenceEditorHud hud )
	{
		super();

		this.hud		= hud;
		
		this.fieldX			= new JTextField( "20" );
		this.fieldX.setSize( 40, 20 );
		this.fieldX.setLocation( 30, 75 );
		
		this.hud.add( this.fieldX );
		
		this.fieldY			= new JTextField( "15" );
		this.fieldY.setSize( 40, 20 );
		this.fieldY.setLocation( 30, 97 );
		
		this.hud.add( this.fieldY );
		
		this.fieldMoney			= new JTextField( "30" );
		this.fieldMoney.setSize( 40, 20 );
		this.fieldMoney.setLocation( 192, 64 );
		
		this.hud.add( this.fieldMoney );
		
		this.fieldTower			= new JTextField( "15" );
		this.fieldTower.setSize( 40, 20 );
		this.fieldTower.setLocation( 192, 86 );
		
		this.hud.add( this.fieldTower );
		
		this.fieldWaves			= new JTextField( "40" );
		this.fieldWaves.setSize( 40, 20 );
		this.fieldWaves.setLocation( 192, 108 );
		
		this.hud.add( this.fieldWaves );
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		this.iBackground			= image.getPic( "/images/editor_general.png", false );
		
		this.buttons				= new ApoDefenceButton[6];
		this.buttons[0]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_set.png", false ),       75,  80, 50, 30, "Set" );
		this.buttons[1]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_way.png", false ),      270, 100, 50, 30, "Way" );
		this.buttons[2]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_castle.png", false ),   330, 100, 50, 30, "Defend" );
		this.buttons[3]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_research.png", false ), 390, 100, 50, 30, "Upgrade" );
		this.buttons[4]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_load.png", false ),     530,  70, 50, 30, "Load" );
		this.buttons[5]				= new ApoDefenceButton( image.getPic( "/images/button/button_editor_save.png", false ),     530, 110, 50, 30, "Save" );
	}
	
	public void setVisible( boolean bVisible )
	{
		this.fieldMoney.setVisible( bVisible );
		this.fieldTower.setVisible( bVisible );
		this.fieldWaves.setVisible( bVisible );
		this.fieldX.setVisible( bVisible );
		this.fieldY.setVisible( bVisible );
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
				} else if ( "Way".equals( function ) )
				{
					this.setButton( ApoDefenceConstants.WAY );
				} else if ( "Defend".equals( function ) )
				{
					this.setButton( ApoDefenceConstants.CASTLE_DEFEND );
				} else if ( "Upgrade".equals( function ) )
				{
					this.setButton( ApoDefenceConstants.CASTLE_UPGRADE );
				} else if ( "Load".equals( function ) )
				{
					this.setLoad();
				} else if ( "Save".equals( function ) )
				{
					this.setSave();
				} else if ( "Waypoint".equals( function ) )
				{
					this.setButton( ApoDefenceConstants.WAYPOINT );
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
	
	private void setButton( int button )
	{
		if ( this.hud.getEditor() != null )
		{
			this.hud.getEditor().setButton( button );
		}
	}
	
	private void setSet()
	{
		if ( this.hud.getEditor() != null )
		{
			int x	= 20;
			try
			{
				x = Integer.parseInt( this.fieldX.getText());
				if ( x < 20 )
					x	= 20;
				else if ( x > 100 )
					x	= 100;
				this.fieldX.setText( ""+x );
			}
			catch (NumberFormatException ex)
			{
				x	= 20;
				this.fieldX.setText( ""+x );
			}
			int y	= 15;
			try
			{
				y = Integer.parseInt( this.fieldY.getText());
				if ( y < 15 )
					y	= 15;
				else if ( y > 100 )
					y	= 100;
				this.fieldY.setText( ""+y );
			}
			catch (NumberFormatException ex)
			{
				y	= 20;
				this.fieldY.setText( ""+y );
			}
			this.hud.getEditor().makeNewXAndY( x, y, true );
		}
	}
	
	private void setLoad()
	{
		if ( this.hud.getEditor() != null )
		{
			this.hud.getEditor().setLoad();
			this.fieldMoney.setText( ""+this.hud.getEditor().getLoadSave().getMoney() );
			this.fieldTower.setText( ""+this.hud.getEditor().getLoadSave().getTowerCount() );
			this.fieldX.setText( ""+this.hud.getEditor().getLoadSave().getX() );
			this.fieldY.setText( ""+this.hud.getEditor().getLoadSave().getY() );
			this.fieldWaves.setText( ""+this.hud.getEditor().getLoadSave().getWaves() );
		}
	}
	
	private void setSave()
	{
		if ( this.hud.getEditor() != null )
		{
			if ( ( this.hud.getWaypoint().getWaypoints() == null ) || ( this.hud.getWaypoint().getWaypoints().size() <= 1 ) )
				JOptionPane.showMessageDialog( this.hud.getEditor(), "Some Waypoints are missing.\nYou have to build more than 1 waypoint!" );
			else if ( this.hud.getEditor().getPointDefend().x < 0 )
				JOptionPane.showMessageDialog( this.hud.getEditor(), "The castle is missing!" );
			else if ( this.hud.getEditor().getPointUpgrade().x < 0 )
				JOptionPane.showMessageDialog( this.hud.getEditor(), "The research building is missing!" );
			else if ( !new Rectangle2D.Double( this.hud.getEditor().getPointDefend().x, this.hud.getEditor().getPointDefend().y, 100, 100 ).intersects( new Rectangle2D.Double( this.hud.getWaypoint().getWaypoints().get( this.hud.getWaypoint().getWaypoints().size() - 1 ).getX(), this.hud.getWaypoint().getWaypoints().get( this.hud.getWaypoint().getWaypoints().size() - 1 ).getY(), 1, 1 ) ) )
				JOptionPane.showMessageDialog( this.hud.getEditor(), "The last waypoint has to be in the castle!" );
			else
				this.hud.getEditor().setSave();
		}
	}
	
	public int getMoney()
	{
		int money	= 50;
		try
		{
			money = Integer.parseInt( this.fieldMoney.getText());
			if ( money < 50 )
				money	= 50;
			else if ( money > 10000 )
				money	= 10000;
			this.fieldMoney.setText( ""+money );
		}
		catch (NumberFormatException ex)
		{
			money	= 30;
			this.fieldMoney.setText( ""+money );
		}
		return money;
	}
	
	public JTextField getFieldMoney()
	{
		return this.fieldMoney;
	}

	public int getWaves()
	{
		int waves	= 40;
		try
		{
			waves = Integer.parseInt( this.fieldWaves.getText());
			if ( waves < 1 )
				waves	= 1;
			else if ( waves > 1000 )
				waves	= 1000;
			this.fieldTower.setText( ""+waves );
		}
		catch (NumberFormatException ex)
		{
			waves	= 40;
			this.fieldTower.setText( ""+waves );
		}
		return waves;
	}
	
	public void setWaves( int waves )
	{
		this.fieldWaves.setText( ""+waves );
	}
	
	public int getTowerCount()
	{
		int tower	= 15;
		try
		{
			tower = Integer.parseInt( this.fieldTower.getText());
			if ( tower < 5 )
				tower	= 5;
			else if ( tower > 300 )
				tower	= 300;
			this.fieldTower.setText( ""+tower );
		}
		catch (NumberFormatException ex)
		{
			tower	= 15;
			this.fieldTower.setText( ""+tower );
		}
		return tower;
	}
	
	public JTextField getFieldTower()
	{
		return this.fieldTower;
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
