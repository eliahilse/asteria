package apoDefence.options;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceConstantsOriginal;
import apoDefence.ApoDefenceImage;

public class ApoDefenceOptionsResearch {

	private BufferedImage			iBackground;
	private ApoDefenceButton[]		buttons;
	private ApoDefenceOptions		options;
	
	public ApoDefenceOptionsResearch( ApoDefenceOptions options )
	{
		super();
	
		this.options				= options;
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		this.iBackground			= image.getPic( "/images/options_research.png", false );
		
		BufferedImage	button_up	= image.getPic( "/images/button/button_options_up.png", false );
		BufferedImage	button_down	= image.getPic( "/images/button/button_options_down.png", false );
		
		this.buttons				= new ApoDefenceButton[25];
		this.buttons[0]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_original.png", false ),   156 + 15,  172 + 244, 100, 45, "Original" );
		this.buttons[1]				= new ApoDefenceButton( button_down,   105 + 15,    2 + 244,  20, 20, "TechLevelDown" );
		this.buttons[2]				= new ApoDefenceButton( button_up,     165 + 15,    2 + 244,  20, 20, "TechLevelUp" );
		this.buttons[3]				= new ApoDefenceButton( button_down,   315 + 15,    2 + 244,  20, 20, "MaxTechLevelDown" );
		this.buttons[4]				= new ApoDefenceButton( button_up,     375 + 15,    2 + 244,  20, 20, "MaxTechLevelUp" );
		this.buttons[5]				= new ApoDefenceButton( button_down,   105 + 15,   24 + 244,  20, 20, "PriceTechLevelDown" );
		this.buttons[6]				= new ApoDefenceButton( button_up,     165 + 15,   24 + 244,  20, 20, "PriceTechLevelUp" );
		this.buttons[7]				= new ApoDefenceButton( button_down,   315 + 15,   24 + 244,  20, 20, "SpeedLevelDown" );
		this.buttons[8]				= new ApoDefenceButton( button_up,     375 + 15,   24 + 244,  20, 20, "SpeedLevelUp" );
		this.buttons[9]				= new ApoDefenceButton( button_down,   105 + 15,   46 + 244,  20, 20, "MaxSpeedLevelDown" );
		this.buttons[10]			= new ApoDefenceButton( button_up,     165 + 15,   46 + 244,  20, 20, "MaxSpeedLevelUp" );
		this.buttons[11]			= new ApoDefenceButton( button_down,   315 + 15,   46 + 244,  20, 20, "PriceSpeedLevelDown" );
		this.buttons[12]			= new ApoDefenceButton( button_up,     375 + 15,   46 + 244,  20, 20, "PriceSpeedLevelUp" );
		this.buttons[13]			= new ApoDefenceButton( button_down,   105 + 15,   68 + 244,  20, 20, "ArmorLevelDown" );
		this.buttons[14]			= new ApoDefenceButton( button_up,     165 + 15,   68 + 244,  20, 20, "ArmorLevelUp" );
		this.buttons[15]			= new ApoDefenceButton( button_down,   315 + 15,   68 + 244,  20, 20, "MaxArmorLevelDown" );
		this.buttons[16]			= new ApoDefenceButton( button_up,     375 + 15,   68 + 244,  20, 20, "MaxArmorLevelUp" );
		this.buttons[17]			= new ApoDefenceButton( button_down,   105 + 15,   90 + 244,  20, 20, "PriceArmorLevelDown" );
		this.buttons[18]			= new ApoDefenceButton( button_up,     165 + 15,   90 + 244,  20, 20, "PriceArmorLevelUp" );
		this.buttons[19]			= new ApoDefenceButton( button_down,   315 + 15,   90 + 244,  20, 20, "ArmorPiercingLevelDown" );
		this.buttons[20]			= new ApoDefenceButton( button_up,     375 + 15,   90 + 244,  20, 20, "ArmorPiercingLevelUp" );
		this.buttons[21]			= new ApoDefenceButton( button_down,   105 + 15,  112 + 244,  20, 20, "MaxArmorPiercingLevelDown" );
		this.buttons[22]			= new ApoDefenceButton( button_up,     165 + 15,  112 + 244,  20, 20, "MaxArmorPiercingLevelUp" );
		this.buttons[23]			= new ApoDefenceButton( button_down,   315 + 15,  112 + 244,  20, 20, "PriceArmorPiercingLevelDown" );
		this.buttons[24]			= new ApoDefenceButton( button_up,     375 + 15,  112 + 244,  20, 20, "PriceArmorPiercingLevelUp" );
	}
	
	public void mouseMoved( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getMove( x, y ) )
			{
				this.options.repaint();
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
				if ( "Original".equals( function ) )
				{
					this.setOriginal();
				} else if ( "TechLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL++;
					if ( ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL > ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL;
				} else if ( "TechLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL--;
					if ( ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL <= 0  )
						ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL	= 1;
				} else if ( "MaxTechLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL++;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL > 10 )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL	= 10;
				} else if ( "MaxTechLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL--;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL < ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL;
				} else if ( "PriceTechLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL	+= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL > 700 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL	= 700;
				} else if ( "PriceTechLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL	-= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL < 50 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL	= 50;
				} else if ( "SpeedLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL	+= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL > ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL;
				} else if ( "SpeedLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL	-= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL < 1 )
						ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL	= 1;
				} else if ( "MaxSpeedLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL	+= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL > 30 )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL	= 30;
				} else if ( "MaxSpeedLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL	-= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL < ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL;
				} else if ( "PriceSpeedLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL	+= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL > 1000 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL	= 1000;
				} else if ( "PriceSpeedLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL	-= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL < 50 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL	= 50;
				} else if ( "ArmorLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL	+= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL > ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL;
				} else if ( "ArmorLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL	-= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL < 1 )
						ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL	= 1;
				} else if ( "MaxArmorLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL	+= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL > 100 )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL	= 100;
				} else if ( "MaxArmorLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL	-= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL < ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL;
				} else if ( "PriceArmorLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL	+= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL > 800 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL	= 800;
				} else if ( "PriceArmorLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL	-= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL < 50 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL	= 50;
				} else if ( "ArmorPiercingLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL	+= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL > ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL;
				} else if ( "ArmorPiercingLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL	-= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL < 0 )
						ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL	= 0;
				} else if ( "MaxArmorPiercingLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL	+= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL > 7 )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL	= 7;
				} else if ( "MaxArmorPiercingLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL	-= 1;
					if ( ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL < ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL )
						ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL	= ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL;
				} else if ( "PriceArmorPiercingLevelUp".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL	+= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL > 2000 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL	= 2000;
				} else if ( "PriceArmorPiercingLevelDown".equals( function ) )
				{
					ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL	-= 50;
					if ( ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL < 50 )
						ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL	= 50;
				}
				this.options.repaint();
			}
		}
	}
	
	public void mousePressed( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getPressed( x, y ) )
			{
				this.options.repaint();
				return;
			}
		}
	}
	
	private void setOriginal()
	{
		ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_ARMORLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_ARMORLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_ARMORLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_ATTACKLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_ATTACKLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL	= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_ATTACKLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_SPEEDLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_SPEEDLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_SPEEDLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_TECHLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_TECHLEVEL;
		ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_TECHLEVEL;
	}
	
	public boolean isBOriginal()
	{
		if ( 	( ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_ARMORLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_ARMORLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_ARMORLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_ATTACKLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_ATTACKLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_ATTACKLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_SPEEDLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_SPEEDLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_SPEEDLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_TECHLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_TECHLEVEL ) &&
				( ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL == ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_TECHLEVEL ) )
			return true;
		else
			return false;
	}
	
	public void render( Graphics2D g )
	{
		g.drawImage( this.iBackground, 15, 244, null );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
		
		g.setColor( Color.WHITE );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL,           135 + 15,  17 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL,       345 + 15,  17 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL,     135 + 15,  39 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL,          345 + 15,  39 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL,      135 + 15,  61 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL,    345 + 15,  61 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL,          135 + 15,  83 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL,      345 + 15,  83 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL,    135 + 15, 105 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL,         345 + 15, 105 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL,     135 + 15, 127 + 244 );
		g.drawString( ""+ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL,   345 + 15, 127 + 244 );
	}

}
