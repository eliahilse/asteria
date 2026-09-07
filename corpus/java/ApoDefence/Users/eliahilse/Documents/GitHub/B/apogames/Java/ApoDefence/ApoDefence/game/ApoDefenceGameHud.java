package apoDefence.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceImage;

public class ApoDefenceGameHud
{
	private ApoDefenceGame 			game;
	
	private ApoDefenceButton[]		buttons;
	
	private BufferedImage			iHudAbove, iHudDown, iHudNotGold, iHudNotTech,
									iHudTooManyTowers;
	
	private boolean					bResearch, bCastle, bNotGold, bNotTech, bTooManyTowers;
	
	private long					notTime;
	
	private ApoDefenceTower			tower;
	private ApoDefenceEnemy			enemy;
	
	private Font					fontCaption, fontNormal;
	
	public ApoDefenceGameHud( ApoDefenceGame game )
	{
		super();
		
		this.game					= game;
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		
		this.iHudAbove				= image.getPic( "/images/hud_above.png", false );
		this.iHudDown				= image.getPic( "/images/hud_down.png", false );
		this.iHudNotGold			= image.getPic( "/images/hud_notgold.png", false );
		this.iHudNotTech			= image.getPic( "/images/hud_nottech.png", false );
		this.iHudTooManyTowers		= image.getPic( "/images/hud_toomanytowers.png", false );
		BufferedImage	iDetails	= image.getPic( "/images/button/button_build_details.png", false );
		
		this.bNotGold				= false;
		this.bTooManyTowers			= false;
		this.bNotTech				= false;
		this.notTime				= -1;
		
		this.tower					= null;
		this.enemy					= null;
		
		this.buttons	= new ApoDefenceButton[14];
		this.buttons[0]	= new ApoDefenceButton( image.getPic( "/images/button/button_menu.png" , false),        137,   3, 114, 25, "Menu" );
		this.buttons[1]	= new ApoDefenceButton( image.getPic( "/images/button/button_quest.png" , false),        14,   3, 114, 25, "Quest" );
		
		this.buttons[2]	= new ApoDefenceButton( image.getPic( "/images/button/button_build_arch.png" , false), iDetails,  493, 375,  65, 30, "Build_Arch", ApoDefenceConstants.TOWER_ARCH_PRICE );
		this.buttons[2].setDetails( new String[] { "Tower Arch", "Cost: "+ApoDefenceConstants.TOWER_ARCH_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_ARCH_HEALTH, "Range: "+ApoDefenceConstants.TOWER_ARCH_RANGE, "Damage: "+ApoDefenceConstants.TOWER_ARCH_POINTATTACK.x+" / "+ApoDefenceConstants.TOWER_ARCH_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_ARCH_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_ARCH_LEVEL } );
		this.buttons[3]	= new ApoDefenceButton( image.getPic( "/images/button/button_build_fire.png" , false), iDetails,  566, 375,  65, 30, "Build_Fire", ApoDefenceConstants.TOWER_FIRE_PRICE );
		this.buttons[3].setDetails( new String[] { "Tower Fire", "Cost: "+ApoDefenceConstants.TOWER_FIRE_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_FIRE_HEALTH, "Range: "+ApoDefenceConstants.TOWER_FIRE_RANGE, "Damage: "+ApoDefenceConstants.TOWER_FIRE_POINTATTACK.x+" / "+ApoDefenceConstants.TOWER_FIRE_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_FIRE_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_FIRE_LEVEL } );
		this.buttons[4]	= new ApoDefenceButton( image.getPic( "/images/button/button_build_ice.png" , false), iDetails,   493, 408,  65, 30, "Build_Ice", ApoDefenceConstants.TOWER_ICE_PRICE );
		this.buttons[4].setDetails( new String[] { "Tower Ice", "Cost: "+ApoDefenceConstants.TOWER_ICE_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_ICE_HEALTH, "Range: "+ApoDefenceConstants.TOWER_ICE_RANGE, "Damage: "+ApoDefenceConstants.TOWER_ICE_POINTATTACK.x+" / "+ApoDefenceConstants.TOWER_ICE_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_ICE_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_ICE_LEVEL } );
		this.buttons[5]	= new ApoDefenceButton( image.getPic( "/images/button/button_build_light.png" , false), iDetails, 566, 408,  65, 30, "Build_Light", ApoDefenceConstants.TOWER_LIGHT_PRICE );
		this.buttons[5].setDetails( new String[] { "Tower Light", "Cost: "+ApoDefenceConstants.TOWER_LIGHT_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_LIGHT_HEALTH, "Range: "+ApoDefenceConstants.TOWER_LIGHT_RANGE, "Damage: "+ApoDefenceConstants.TOWER_LIGHT_POINTATTACK.x+" / "+ApoDefenceConstants.TOWER_LIGHT_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_LIGHT_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_LIGHT_LEVEL } );
		this.buttons[6]	= new ApoDefenceButton( image.getPic( "/images/button/button_build_ultra.png" , false), iDetails, 493, 441,  65, 30, "Build_Ultra", ApoDefenceConstants.TOWER_ULTRA_PRICE );
		this.buttons[6].setDetails( new String[] { "Tower Ultra", "Cost: "+ApoDefenceConstants.TOWER_ULTRA_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_ULTRA_HEALTH, "Range: "+ApoDefenceConstants.TOWER_ULTRA_RANGE, "Damage: "+ApoDefenceConstants.TOWER_ULTRA_POINTATTACK.x+" / "+ApoDefenceConstants.TOWER_ULTRA_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_ULTRA_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_ULTRA_LEVEL } );
		
		this.buttons[7]	= new ApoDefenceButton( image.getPic( "/images/button/button_upgrade.png" , false), iDetails, 498, 375,  130, 30, "Upgrade", -1 );
		this.buttons[7].setBVisible( false );
		this.buttons[8]	= new ApoDefenceButton( image.getPic( "/images/button/button_repair.png" , false), iDetails, 498, 408,  130, 30, "Repair", -1 );
		this.buttons[8].setDetails( new String[] { "Repair", "", "Repairs the tower", "if it is nessecary.", "", "Per "+ApoDefenceConstants.REPAIR_HEALTH+" Health it ", "will cost "+ApoDefenceConstants.REPAIR_COST+" Coin(s)" } );
		this.buttons[8].setBVisible( false );
		this.buttons[9]	= new ApoDefenceButton( image.getPic( "/images/button/button_sell.png" , false), iDetails, 498, 441,  130, 30, "Sell", -1 );
		this.buttons[9].setDetails( new String[] { "Sell", "Sale: ", "Sell the tower.", "", "You will get back", "half of the value" } );
		this.buttons[9].setBVisible( false );
		
		this.buttons[10]	= new ApoDefenceButton( image.getPic( "/images/button/button_tech_level_up.png" , false), iDetails, 498, 375,  130, 30, "TechLevelUp", ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL );
		this.buttons[10].setBVisible( false );
		this.buttons[11]	= new ApoDefenceButton( image.getPic( "/images/button/button_speed.png" , false), iDetails, 493, 408,  65, 30, "SpeedLevel", ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL );
		this.buttons[11].setBVisible( false );
		this.buttons[12]	= new ApoDefenceButton( image.getPic( "/images/button/button_armor.png" , false), iDetails, 566, 408,  65, 30, "ArmorLevel", ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL );
		this.buttons[12].setBVisible( false );
		this.buttons[13]	= new ApoDefenceButton( image.getPic( "/images/button/button_armor_piercing.png" , false), iDetails, 498, 441,  130, 30, "ArmorPiercingLevel", ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL );
		this.buttons[13].setBVisible( false );
		
		this.fontCaption			= new Font( "Dialog", Font.BOLD, 17 );
		this.fontNormal				= new Font( "Dialog", Font.BOLD, 12 );
	}
	
	public void setStart()
	{
		this.buttons[2].setPrice( ApoDefenceConstants.TOWER_ARCH_PRICE );
		this.buttons[2].setDetails( new String[] { "Tower Arch", "Cost: "+ApoDefenceConstants.TOWER_ARCH_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_ARCH_HEALTH, "Range: "+ApoDefenceConstants.TOWER_ARCH_RANGE, "Damage: "+(int)ApoDefenceConstants.TOWER_ARCH_POINTATTACK.x+" / "+(int)ApoDefenceConstants.TOWER_ARCH_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_ARCH_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_ARCH_LEVEL } );
		
		this.buttons[3].setPrice( ApoDefenceConstants.TOWER_FIRE_PRICE );
		this.buttons[3].setDetails( new String[] { "Tower Fire", "Cost: "+ApoDefenceConstants.TOWER_FIRE_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_FIRE_HEALTH, "Range: "+ApoDefenceConstants.TOWER_FIRE_RANGE, "Damage: "+(int)ApoDefenceConstants.TOWER_FIRE_POINTATTACK.x+" / "+(int)ApoDefenceConstants.TOWER_FIRE_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_FIRE_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_FIRE_LEVEL } );
		
		this.buttons[4].setPrice( ApoDefenceConstants.TOWER_ICE_PRICE );
		this.buttons[4].setDetails( new String[] { "Tower Ice", "Cost: "+ApoDefenceConstants.TOWER_ICE_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_ICE_HEALTH, "Range: "+ApoDefenceConstants.TOWER_ICE_RANGE, "Damage: "+(int)ApoDefenceConstants.TOWER_ICE_POINTATTACK.x+" / "+(int)ApoDefenceConstants.TOWER_ICE_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_ICE_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_ICE_LEVEL } );
		
		this.buttons[5].setPrice( ApoDefenceConstants.TOWER_LIGHT_PRICE );
		this.buttons[5].setDetails( new String[] { "Tower Light", "Cost: "+ApoDefenceConstants.TOWER_LIGHT_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_LIGHT_HEALTH, "Range: "+ApoDefenceConstants.TOWER_LIGHT_RANGE, "Damage: "+(int)ApoDefenceConstants.TOWER_LIGHT_POINTATTACK.x+" / "+(int)ApoDefenceConstants.TOWER_LIGHT_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_LIGHT_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_LIGHT_LEVEL } );
		
		this.buttons[6].setPrice( ApoDefenceConstants.TOWER_ULTRA_PRICE );
		this.buttons[6].setDetails( new String[] { "Tower Ultra", "Cost: "+ApoDefenceConstants.TOWER_ULTRA_PRICE, "", "Health: "+ApoDefenceConstants.TOWER_ULTRA_HEALTH, "Range: "+ApoDefenceConstants.TOWER_ULTRA_RANGE, "Damage: "+(int)ApoDefenceConstants.TOWER_ULTRA_POINTATTACK.x+" / "+(int)ApoDefenceConstants.TOWER_ULTRA_POINTATTACK.y, "Speed: "+ApoDefenceConstants.TOWER_ULTRA_SPEED, "TechLevel: "+ApoDefenceConstants.TOWER_ULTRA_LEVEL } );
	}
	
	public boolean isDeselect()
	{
		if ( 	( this.bResearch ) || ( this.bCastle ) ||
				( this.tower != null ) || ( this.enemy != null ) )
			return false;
		return true;
	}
	
	public boolean isTower()
	{
		if ( this.tower == null )
			return false;
		else
			return true;
	}
	
	public boolean isBResearch()
	{
		return this.bResearch;
	}

	public void setBResearch( boolean bResearch )
	{
		this.bResearch = bResearch;
		
		if ( bResearch )
		{
			if ( this.tower != null )
				this.tower.setBSelect( false );
			else if ( this.enemy != null )
				this.enemy.setBSelect( false );
			this.setResearchButtons( true );
			this.setTowerButtons( false );
			this.setBuildButtons( false );
			this.bCastle	= false;
			this.tower = this.game.getResearch();
			this.tower.setBSelect( true );
			this.buttons[10].setDetails( new String[] { "TechLevelUp", "Cost: "+this.game.getResearch().getPriceTechLevel(), "technology to next", "level allowing you", "to build new", "towers", "cur. Level: "+this.game.getResearch().getTechLevel(), "Max. Level: "+this.game.getResearch().getMaxTechLevel() } );
			this.buttons[10].setPrice( this.game.getResearch().getPriceTechLevel() );
			this.buttons[11].setDetails( new String[] { "BuildSpeed", "Cost: "+this.game.getResearch().getPriceSpeedLevel(), "Allows you to", "build all towers &", "upgrades faster", "", "cur. Level: "+(int)this.game.getResearch().getSpeedLevel(), "Max. Level: "+this.game.getResearch().getMaxSpeedLevel() } );
			this.buttons[11].setPrice( this.game.getResearch().getPriceSpeedLevel() );
			this.buttons[12].setDetails( new String[] { "Armor", "Cost: "+this.game.getResearch().getPriceArmorLevel(), "Increases armor", "of all towers", "", "", "cur. Level: "+this.game.getResearch().getArmorLevel(), "Max. Level: "+this.game.getResearch().getMaxArmorLevel() } );
			this.buttons[12].setPrice( this.game.getResearch().getPriceArmorLevel() );
			this.buttons[13].setDetails( new String[] { "Armor Piercing", "Cost: "+this.game.getResearch().getPriceAttackLevel(), "makes all towers", "penetrate "+((this.game.getResearch().getAttackLevel()+1)*10) + "%", "of enemy armor", "", "cur. Level: "+this.game.getResearch().getAttackLevel(), "Max. Level: "+this.game.getResearch().getMaxAttackLevel() } );
			this.buttons[13].setPrice( this.game.getResearch().getPriceAttackLevel() );
		}
	}
	
	public boolean isBCastle()
	{
		return this.bCastle;
	}

	public void setBCastle(boolean bCastle)
	{
		this.bCastle = bCastle;
		
		if ( bCastle )
		{
			if ( this.tower != null )
				this.tower.setBSelect( false );
			else if ( this.enemy != null )
				this.enemy.setBSelect( false );
			this.setTowerButtons( false );
			this.setResearchButtons( false );
			this.setBuildButtons( false );
			this.bResearch	= false;
			this.tower = this.game.getCastle();
			this.tower.setBSelect( true );
			this.buttons[7].setBVisible( true );
			this.buttons[7].setDetails( new String[] { this.tower.getName(), "Cost: "+(int)this.tower.getUpgradePrice(), "", "Health: "+(this.tower.getHealth()+ApoDefenceConstants.TOWER_CASTLE_HEALTH_UPGRADE), "", "current Level: "+this.tower.getLevel(), "max. Level: "+this.tower.getMaxLevel() } );
		}
	}
	
	public void deselectAll()
	{
		this.bResearch	= false;
		this.bCastle	= false;
		if ( this.tower != null )
		{
			this.tower.setBSelect( false );
			this.tower		= null;
		}
		if ( this.enemy != null )
		{
			this.enemy.setBSelect( false );
			this.enemy		= null;
		}
		this.setBuildButtons( true );
		this.setTowerButtons( false );
		this.setResearchButtons( false );
		this.game.getResearch().setBSelect( false );
		this.game.getCastle().setBSelect( false );
	}
	
	private void setBuildButtons( boolean bVisible )
	{
		for ( int i = 2; i < 7; i++ )
		{
			this.buttons[i].setBVisible( bVisible );
		}
	}
	
	private void setTowerButtons( boolean bVisible )
	{
		for ( int i = 7; i < 10; i++ )
		{
			this.buttons[i].setBVisible( bVisible );
		}
	}
	
	private void setResearchButtons( boolean bVisible )
	{
		for ( int i = 10; i < 14; i++ )
		{
			this.buttons[i].setBVisible( bVisible );
		}
	}
	
	protected void setOver( boolean bOver )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].isBOver() != bOver )
				this.buttons[i].setBOver( bOver );
		}
	}
	
	protected void mouseMoved(int x, int y)
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getMove( x, y ) )
			{
				return;
			}
		}
	}

	protected void mousePressed(int x, int y)
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getPressed( x, y ) )
			{
				return;
			}
		}
	}

	protected void setBuildArch()
	{
		if ( !this.game.canPay( ApoDefenceConstants.TOWER_ARCH_PRICE, false ) )
		{
			if ( this.game.getMoney() - ApoDefenceConstants.TOWER_ARCH_PRICE < 0 )
				this.bNotGold	= true;
			else
				this.bTooManyTowers	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.isTechLevel( ApoDefenceConstants.TOWER_ARCH_LEVEL ) )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
			this.game.setBuildMode( ApoDefenceConstants.TOWER_ARCH );
	}
	
	protected void setBuildFire()
	{
		if ( !this.game.canPay( ApoDefenceConstants.TOWER_FIRE_PRICE, false ) )
		{
			if ( this.game.getMoney() - ApoDefenceConstants.TOWER_FIRE_PRICE < 0 )
				this.bNotGold	= true;
			else
				this.bTooManyTowers	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.isTechLevel( ApoDefenceConstants.TOWER_FIRE_LEVEL ) )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
			this.game.setBuildMode( ApoDefenceConstants.TOWER_FIRE );
	}
	
	protected void setBuildIce()
	{
		if ( !this.game.canPay( ApoDefenceConstants.TOWER_ICE_PRICE, false ) )
		{
			if ( this.game.getMoney() - ApoDefenceConstants.TOWER_ICE_PRICE < 0 )
				this.bNotGold	= true;
			else
				this.bTooManyTowers	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.isTechLevel( ApoDefenceConstants.TOWER_ICE_LEVEL ) )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
			this.game.setBuildMode( ApoDefenceConstants.TOWER_ICE );
	}
	
	protected void setBuildLight()
	{
		if ( !this.game.canPay( ApoDefenceConstants.TOWER_LIGHT_PRICE, false ) )
		{
			if ( this.game.getMoney() - ApoDefenceConstants.TOWER_LIGHT_PRICE < 0 )
				this.bNotGold	= true;
			else
				this.bTooManyTowers	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.isTechLevel( ApoDefenceConstants.TOWER_LIGHT_LEVEL ) )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
			this.game.setBuildMode( ApoDefenceConstants.TOWER_LIGHT );
	}
	
	protected void setBuildUltra()
	{
		if ( !this.game.canPay( ApoDefenceConstants.TOWER_ULTRA_PRICE, false ) )
		{
			if ( this.game.getMoney() - ApoDefenceConstants.TOWER_ULTRA_PRICE < 0 )
				this.bNotGold	= true;
			else
				this.bTooManyTowers	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.isTechLevel( ApoDefenceConstants.TOWER_ULTRA_LEVEL ) )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
			this.game.setBuildMode( ApoDefenceConstants.TOWER_ULTRA );
	}
	
	protected void setTechLevelUp()
	{
		if ( this.game.getMoney() - this.game.getResearch().getPriceTechLevel() < 0 )
		{
			this.bNotGold	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.getResearch().canTechLevelUp() )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
		{
			this.game.setPoints( this.game.getPoints() + this.game.getResearch().getPriceTechLevel() );
			this.game.setMoney( this.game.getMoney() - this.game.getResearch().getPriceTechLevel() );
			this.game.getResearch().setTechLevel( this.game.getResearch().getTechLevel() + 1 );
			this.game.getResearch().setPriceTechLevel( this.game.getResearch().getPriceTechLevel() + ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL );
			this.buttons[10].setDetails( new String[] { "TechLevelUp", "Cost: "+this.game.getResearch().getPriceTechLevel(), "technology to next", "level allowing you", "to build new", "towers", "cur. Level: "+this.game.getResearch().getTechLevel(), "Max. Level: "+this.game.getResearch().getMaxTechLevel() } );
			this.buttons[10].setPrice( this.game.getResearch().getPriceTechLevel() );
		}
	}
	
	public void setSpeedLevelUp()
	{
		if ( this.game.getMoney() - this.game.getResearch().getPriceSpeedLevel() < 0 )
		{
			this.bNotGold	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.getResearch().canSpeedLevelUp() )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
		{
			this.game.setPoints( this.game.getPoints() + this.game.getResearch().getPriceSpeedLevel() );
			this.game.setMoney( this.game.getMoney() - this.game.getResearch().getPriceSpeedLevel() );
			this.game.getResearch().setSpeedLevel( (int)(this.game.getResearch().getSpeedLevel() + 1) );
			this.buttons[11].setDetails( new String[] { "BuildSpeed", "Cost: "+ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL, "Allows you to", "build all towers &", "upgrades faster", "", "cur. Level: "+(int)this.game.getResearch().getSpeedLevel(), "Max. Level: "+this.game.getResearch().getMaxSpeedLevel() } );
		}
	}
	
	protected void setArmorLevelUp()
	{
		if ( this.game.getMoney() - this.game.getResearch().getPriceArmorLevel() < 0 )
		{
			this.bNotGold	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.getResearch().canArmorLevelUp() )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
		{
			this.game.setPoints( this.game.getPoints() + this.game.getResearch().getPriceArmorLevel() );
			this.game.setMoney( this.game.getMoney() - this.game.getResearch().getPriceArmorLevel() );
			this.game.getResearch().upgradeArmorLevel();
			this.game.getGameTower().addAmor();
			this.buttons[12].setDetails( new String[] { "Armor", "Cost: "+this.game.getResearch().getPriceArmorLevel(), "Increases armor", "of all towers and", "buildings", "", "cur. Level: "+this.game.getResearch().getArmorLevel(), "Max. Level: "+this.game.getResearch().getMaxArmorLevel() } );
			this.buttons[12].setPrice( this.game.getResearch().getPriceArmorLevel() );
		}
	}
	
	protected void setArmorPiercingLevelUp()
	{
		if ( this.game.getMoney() - this.game.getResearch().getPriceAttackLevel() < 0 )
		{
			this.bNotGold	= true;
			this.bNotTech	= false;
			this.notTime	= System.nanoTime();
		} else if ( !this.game.getResearch().canArmorPiercingLevelUp() )
		{
			this.bNotGold	= false;
			this.bNotTech	= true;
			this.notTime	= System.nanoTime();
		} else
		{
			this.game.setPoints( this.game.getPoints() + this.game.getResearch().getPriceAttackLevel() );
			this.game.setMoney( this.game.getMoney() - this.game.getResearch().getPriceAttackLevel() );
			this.game.getResearch().setAttackLevel( this.game.getResearch().getAttackLevel() + 1 );
			this.buttons[13].setDetails( new String[] { "Armor Piercing", "Cost: "+ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL, "makes all towers", "penetrate "+((this.game.getResearch().getAttackLevel()+1)*10) + "%", "of enemy armor", "", "cur. Level: "+this.game.getResearch().getAttackLevel(), "Max. Level: "+this.game.getResearch().getMaxAttackLevel() } );
		}
	}
	
	protected void setUpgrade()
	{
		if ( this.tower != null )
		{
			if ( !this.game.canPay( (int)this.tower.getUpgradePrice(), true ) )
			{
				this.bNotGold	= true;
				this.bNotTech	= false;
				this.notTime	= System.nanoTime();
			} else if ( !this.game.canUpgrade( this.tower ) )
			{
				this.bNotGold	= false;
				this.bNotTech	= true;
				this.notTime	= System.nanoTime();
			} else
			{
				this.game.buildUpgrade( this.tower );
				this.tower.upgrade();
				if ( this.bCastle )
					this.buttons[7].setDetails( new String[] { this.tower.getName(), "Cost: "+(int)this.tower.getUpgradePrice(), "", "Health: "+(this.tower.getMaxHealth()+ApoDefenceConstants.TOWER_CASTLE_HEALTH_UPGRADE), "", "current Level: "+this.tower.getLevel(), "max. Level: "+this.tower.getMaxLevel() } );
				else
				{
					this.buttons[7].setDetails( new String[] { this.tower.getName(), "Cost: "+(int)this.tower.getUpgradePrice(), "", "Health: "+this.tower.getNewUpgradeHealth(), "Range: "+this.tower.getNewUpgradeRange(), "Damage: "+(int)this.tower.getNewUpgradePoint().x+" / "+(int)this.tower.getNewUpgradePoint().y, "Speed: "+this.tower.getNewUpgradeSpeed(), "Armor: "+this.tower.getNewUpgradeArmor() } );
					this.buttons[9].setDetails( new String[] { "Sell", "Sale: "+this.tower.getSellPrice(), "Sell the tower.", "", "You will get back", "half of the value" } );
				}
			}
		}
	}
	
	protected void setRepair()
	{
		if ( this.tower != null )
			this.tower.setBRepair( true );
	}
	
	protected void setSell()
	{
		if ( this.tower != null )
		{
			this.game.setSell( this.tower );
			this.deselectAll();
		}
	}
	
	protected void mouseReleased(int x, int y)
	{
		if ( 	( x >= 12 ) && ( x <= 117 ) &&
				( y >= 368 ) && ( x <= 473 ) )
		{
			float newX	= x - 12 - ( this.game.getMiniMapPercentX() * (float)this.game.getMiniMapX() / 2 );
			float newY	= y - 368 - ( this.game.getMiniMapPercentY() * (float)this.game.getMiniMapY() / 2 );
			if ( newX < 0 )
				newX	= 0;
			else if ( newX + ( this.game.getMiniMapPercentX() * (float)this.game.getMiniMapX() ) > this.game.getMiniMapX() )
				newX	= this.game.getMiniMapX() - ( this.game.getMiniMapPercentX() * (float)this.game.getMiniMapX() );
			if ( newY < 0 )
				newY	= 0;
			else if ( newY + ( this.game.getMiniMapPercentY() * (float)this.game.getMiniMapY() ) > this.game.getMiniMapY() )
				newY	= this.game.getMiniMapY() - ( this.game.getMiniMapPercentY() * (float)this.game.getMiniMapY() );
			
			this.game.setCurrentX( newX * this.game.getIBackground().getWidth() / this.game.getMiniMapX() );
			this.game.setCurrentY( newY * this.game.getIBackground().getHeight() / this.game.getMiniMapY() );
		} else
		{
			for ( int i = 0; i < this.buttons.length; i++ )
			{
				if ( this.buttons[i].getReleased( x, y ) )
				{
					//this.buttons[i].setBOver( false );
					String function	= this.buttons[i].getFunction();
					if ( "Menu".equals( function ) )
					{
						this.game.setBMenu( true );
					} else if ( "Quest".equals( function ) )
					{
						this.game.setBQuest( true );
					} else if ( "Build_Arch".equals( function ) )
					{
						this.setBuildArch();
					} else if ( "Build_Fire".equals( function ) )
					{
						this.setBuildFire();
					} else if ( "Build_Ice".equals( function ) )
					{
						this.setBuildIce();
					} else if ( "Build_Light".equals( function ) )
					{
						this.setBuildLight();
					} else if ( "Build_Ultra".equals( function ) )
					{
						this.setBuildUltra();
					} else if ( "Upgrade".equals( function ) )
					{
						this.setUpgrade();
					} else if ( "Sell".equals( function ) )
					{
						this.setSell();
					} else if ( "Repair".equals( function ) )
					{
						this.setRepair();
					} else if ( "TechLevelUp".equals( function ) )
					{
						this.setTechLevelUp();
					} else if ( "SpeedLevel".equals( function ) )
					{
						this.setSpeedLevelUp();
					} else if ( "ArmorLevel".equals( function ) )
					{
						this.setArmorLevelUp();
					} else if ( "ArmorPiercingLevel".equals( function ) )
					{
						this.setArmorPiercingLevelUp();
					}
					break;
				}
			}
		}
	}
	
	protected void setTower(ApoDefenceTower tower)
	{
		this.bResearch	= false;
		this.bCastle	= false;
		if ( this.tower != null )
			this.tower.setBSelect( false );
		if ( this.enemy != null )
		{
			this.enemy.setBSelect( false );
			this.enemy		= null;
		}
		this.setBuildButtons( false );
		this.setResearchButtons( false );
		this.setTowerButtons( true );
		this.tower = tower;
		this.tower.setBSelect( true );
		this.buttons[7].setDetails( new String[] { this.tower.getName(), "Cost: "+(int)this.tower.getUpgradePrice(), "", "Health: "+this.tower.getNewUpgradeHealth(), "Range: "+this.tower.getNewUpgradeRange(), "Damage: "+(int)this.tower.getNewUpgradePoint().x+" / "+(int)this.tower.getNewUpgradePoint().y, "Speed: "+this.tower.getNewUpgradeSpeed(), "Armor: "+this.tower.getNewUpgradeArmor() } );
		this.buttons[9].setDetails( new String[] { "Sell", "Sale: "+this.tower.getSellPrice(), "Sell the tower.", "", "You will get back", "half of the value" } );
	}
	
	protected void setEnemy( ApoDefenceEnemy enemy )
	{
		this.bResearch	= false;
		this.bCastle	= false;
		if ( this.tower != null )
		{
			this.tower.setBSelect( false );
			this.tower		= null;
		}
		if ( this.enemy != null )
			this.enemy.setBSelect( false );
		this.setBuildButtons( false );
		this.setResearchButtons( false );
		this.setTowerButtons( false );
		this.enemy = enemy;
		this.enemy.setBSelect( true );
	}
	
	protected void render( Graphics2D g )
	{
		g.drawImage( this.iHudAbove, 0, 0, null );
		g.drawImage( this.iHudDown, 0, 352, null );
		
		if ( ( this.bResearch ) || ( this.bCastle ) || ( this.tower != null ) || ( this.enemy != null ) )
		{
			if ( this.tower != null )
			{
				this.drawTowerDetails( g, this.tower );
			} else if ( this.enemy != null )
			{
				this.drawEnemyDetails( g, this.enemy );
			}

		} else
		{
			this.drawResearchDetails( g );
		}
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
		
		if ( ( this.bNotGold ) || ( this.bTooManyTowers ) )
		{
			if ( System.nanoTime() - this.notTime > 1500000000 )
			{
				this.bNotGold		= false;
				this.bTooManyTowers	= false;
				this.notTime	= -1;
			} else
			{
				if ( this.bNotGold )
					g.drawImage( this.iHudNotGold, 320 - this.iHudNotGold.getWidth()/2, 350 - this.iHudNotGold.getHeight(), null );
				else if ( this.bTooManyTowers )
					g.drawImage( this.iHudTooManyTowers, 320 - this.iHudTooManyTowers.getWidth()/2, 350 - this.iHudTooManyTowers.getHeight(), null );
			}
		} else if ( this.bNotTech )
		{
			if ( System.nanoTime() - this.notTime > 1500000000 )
			{
				this.bNotTech	= false;
				this.notTime	= -1;
			} else
				g.drawImage( this.iHudNotTech, 320 - this.iHudNotTech.getWidth()/2, 350 - this.iHudNotTech.getHeight(), null );
		}
		
	}
	
	private void drawTowerDetails( Graphics2D g, ApoDefenceTower tower )
	{
		g.setColor( Color.WHITE );
		g.drawImage( tower.getIMiniTower(), 165, 386, null );
		g.drawLine( 260, 386, 260, 475 );
		g.setFont( this.fontCaption );
		String name		= tower.getName();
		int w	= g.getFontMetrics().stringWidth( name );
		g.drawString( name, 370 - w/2, 403 );
		
		g.setFont( this.fontNormal );
		g.drawString( "Health:", 265, 425 );
		g.drawString( tower.getHealth() + " / " + tower.getMaxHealth(), 320, 425 );
		g.drawString( "Damage:", 265, 445 );
		g.drawString( (int)tower.getAttack().x + " / " + (int)tower.getAttack().y, 320, 445 );
		g.drawString( "Armor:", 265, 465 );
		g.drawString( tower.getArmor() + "", 320, 465 );
		if ( !this.bResearch )
		{
			g.drawString( "Speed:", 390, 425 );
			g.drawString( tower.getSpeed() + "", 435, 425 );
			g.drawString( "Range:", 390, 445 );
			g.drawString( tower.getRange() + "", 435, 445 );
			g.drawString( "Level:", 390, 465 );
			if ( this.bResearch )
				g.drawString( tower.getLevel() + "", 435, 465 );
			else
				g.drawString( tower.getLevel() + " / " + tower.getMaxLevel(), 435, 465 );
		} else
		{
			if ( !this.game.getResearch().canUpgrade() )
			{
				g.setColor( Color.YELLOW );
				g.drawString( "is upgrading", 390, 430 );
				g.fillRect( 390, 450, 60 - (int)( (float)this.game.getResearch().getWaitTime() / (float)this.game.getResearch().getMaxWaitTime() * 60.0f ), 20 );
				g.setColor( Color.WHITE );
				g.drawRect( 390, 449, 60, 21 );
			}
		}
	}
	
	private void drawEnemyDetails( Graphics2D g, ApoDefenceEnemy enemy )
	{
		g.setColor( Color.WHITE );
		g.drawImage( enemy.getIMiniEnemy(), 165, 386, null );
		g.drawLine( 260, 386, 260, 475 );
		g.setFont( this.fontCaption );
		String name		= enemy.getName();
		if ( enemy.isBUndead() )
			name	+= " (undead)";
		int w	= g.getFontMetrics().stringWidth( name );
		g.drawString( name, 370 - w/2, 403 );
		
		g.setFont( this.fontNormal );
		g.drawString( "Health:", 265, 425 );
		g.drawString( (int)enemy.getHealth() + " / " + (int)enemy.getMaxHealth(), 320, 425 );
		g.drawString( "Damage:", 265, 445 );
		g.drawString( (int)enemy.getDamage().x + " / " + (int)enemy.getDamage().y, 320, 445 );
		g.drawString( "Armor:", 265, 465 );
		g.drawString( (int)enemy.getArmor() + "", 320, 465 );
		g.drawString( "Speed:", 390, 445 );
		g.drawString( (int)enemy.getSpeed() + "", 435, 445 );
		g.drawString( "Level:", 390, 465 );
		g.drawString( enemy.getLevel() + "", 435, 465 );
		if ( enemy.isExtra() )
		{
			if ( enemy.isBFire() )
			{
				g.setColor( Color.RED );
				g.drawString( "On Fire", 390, 425 );
			} else if ( enemy.isBIce() )
			{
				g.setColor( Color.BLUE );
				g.drawString( "is frozen", 390, 425 );
			} else if ( enemy.isBLight() )
			{
				g.setColor( Color.YELLOW );
				g.drawString( "is lighten", 390, 425 );
			}
		}
	}
	
	private void drawResearchDetails( Graphics2D g )
	{
		ApoDefenceResearch research	= this.game.getResearch();
		g.setColor( Color.WHITE );
		g.setFont( this.fontNormal );
		g.drawString( "Technology level:", 167, 402 );
		g.drawString( research.getTechLevel() + " / " + research.getMaxTechLevel(), 345, 402 );
		
		g.drawString( "Wall Armor level:", 167, 422 );
		g.drawString( research.getArmorLevel() + " / " + research.getMaxArmorLevel(), 345, 422 );
		
		g.drawString( "Build Speed level:", 167, 442 );
		g.drawString( (int)research.getSpeedLevel() + " / " + research.getMaxSpeedLevel(), 345, 442 );
		
		g.drawString( "Amor Piercing Level:", 167, 462 );
		g.drawString( research.getAttackLevel() + " / " + research.getMaxAttackLevel(), 345, 462 );
	}

}
