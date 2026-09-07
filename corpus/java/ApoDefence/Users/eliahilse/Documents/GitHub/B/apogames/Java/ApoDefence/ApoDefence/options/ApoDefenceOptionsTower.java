package apoDefence.options;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceConstantsOriginal;
import apoDefence.ApoDefenceImage;
import apoDefence.ApoDefencePoint;

public class ApoDefenceOptionsTower
{
	private final int				TOWER_ARCH	= 0;
	private final int				TOWER_FIRE	= 1;
	private final int				TOWER_ICE	= 2;
	private final int				TOWER_LIGHT	= 3;
	private final int				TOWER_ULTRA	= 4;
	
	private BufferedImage			iBackground;
	private ApoDefenceButton[]		buttons;
	private ApoDefenceOptions		options;
	private int						currentTower;
	
	public ApoDefenceOptionsTower( ApoDefenceOptions options )
	{
		super();
	
		this.options				= options;
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		this.iBackground			= image.getPic( "/images/options_tower.png", false );
		
		BufferedImage	button_up	= image.getPic( "/images/button/button_options_up.png", false );
		BufferedImage	button_down	= image.getPic( "/images/button/button_options_down.png", false );
		
		this.buttons				= new ApoDefenceButton[31];
		this.buttons[0]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_original.png", false ),   156 + 15,  172 + 244, 100, 45, "Original" );
		this.buttons[1]				= new ApoDefenceButton( button_down,   140 + 15,    2 + 244,  20, 20, "ChooseTowerDown" );
		this.buttons[2]				= new ApoDefenceButton( button_up,     260 + 15,    2 + 244,  20, 20, "ChooseTowerUp" );
		this.buttons[3]				= new ApoDefenceButton( button_down,   105 + 15,   24 + 244,  20, 20, "DamageXDown" );
		this.buttons[4]				= new ApoDefenceButton( button_up,     175 + 15,   24 + 244,  20, 20, "DamageXUp" );
		this.buttons[5]				= new ApoDefenceButton( button_down,   315 + 15,   24 + 244,  20, 20, "DamageYDown" );
		this.buttons[6]				= new ApoDefenceButton( button_up,     385 + 15,   24 + 244,  20, 20, "DamageYUp" );
		this.buttons[7]				= new ApoDefenceButton( button_down,   105 + 15,   46 + 244,  20, 20, "RangeDown" );
		this.buttons[8]				= new ApoDefenceButton( button_up,     175 + 15,   46 + 244,  20, 20, "RangeUp" );
		this.buttons[9]				= new ApoDefenceButton( button_down,   315 + 15,   46 + 244,  20, 20, "SpeedDown" );
		this.buttons[10]			= new ApoDefenceButton( button_up,     385 + 15,   46 + 244,  20, 20, "SpeedUp" );
		this.buttons[11]			= new ApoDefenceButton( button_down,   105 + 15,   68 + 244,  20, 20, "HealthDown" );
		this.buttons[12]			= new ApoDefenceButton( button_up,     175 + 15,   68 + 244,  20, 20, "HealthUp" );
		this.buttons[13]			= new ApoDefenceButton( button_down,   315 + 15,   68 + 244,  20, 20, "ArmorDown" );
		this.buttons[14]			= new ApoDefenceButton( button_up,     385 + 15,   68 + 244,  20, 20, "ArmorUp" );
		this.buttons[15]			= new ApoDefenceButton( button_down,   105 + 15,   90 + 244,  20, 20, "PriceDown" );
		this.buttons[16]			= new ApoDefenceButton( button_up,     175 + 15,   90 + 244,  20, 20, "PriceUp" );
		this.buttons[17]			= new ApoDefenceButton( button_down,   315 + 15,   90 + 244,  20, 20, "UpDamageDown" );
		this.buttons[18]			= new ApoDefenceButton( button_up,     385 + 15,   90 + 244,  20, 20, "UpDamageUp" );
		this.buttons[19]			= new ApoDefenceButton( button_down,   105 + 15,  112 + 244,  20, 20, "UpRangeDown" );
		this.buttons[20]			= new ApoDefenceButton( button_up,     175 + 15,  112 + 244,  20, 20, "UpRangeUp" );
		this.buttons[21]			= new ApoDefenceButton( button_down,   315 + 15,  112 + 244,  20, 20, "UpSpeedDown" );
		this.buttons[22]			= new ApoDefenceButton( button_up,     385 + 15,  112 + 244,  20, 20, "UpSpeedUp" );
		this.buttons[23]			= new ApoDefenceButton( button_down,   105 + 15,  134 + 244,  20, 20, "UpHealthDown" );
		this.buttons[24]			= new ApoDefenceButton( button_up,     175 + 15,  134 + 244,  20, 20, "UpHealthUp" );
		this.buttons[25]			= new ApoDefenceButton( button_down,   315 + 15,  134 + 244,  20, 20, "UpArmorDown" );
		this.buttons[26]			= new ApoDefenceButton( button_up,     385 + 15,  134 + 244,  20, 20, "UpArmorUp" );
		this.buttons[27]			= new ApoDefenceButton( button_down,   105 + 15,  156 + 244,  20, 20, "MaxLevelDown" );
		this.buttons[28]			= new ApoDefenceButton( button_up,     175 + 15,  156 + 244,  20, 20, "MaxLevelUp" );
		this.buttons[29]			= new ApoDefenceButton( button_down,   315 + 15,  156 + 244,  20, 20, "LevelDown" );
		this.buttons[30]			= new ApoDefenceButton( button_up,     385 + 15,  156 + 244,  20, 20, "LevelUp" );
		
		this.currentTower			= this.TOWER_ARCH;
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
				} else if ( "ChooseTowerUp".equals( function ) )
				{
					this.currentTower++;
					if ( this.currentTower > this.TOWER_ULTRA )
						this.currentTower	= this.TOWER_ARCH;
				} else if ( "ChooseTowerDown".equals( function ) )
				{
					this.currentTower--;
					if ( this.currentTower < this.TOWER_ARCH  )
						this.currentTower	= this.TOWER_ULTRA;
				} else if ( "DamageXUp".equals( function ) )
				{
					float	newDamageX	= this.getRound( this.getDamageX() + 0.1f, 1);
					if ( ( newDamageX <= this.getDamageY() ) &&
						 ( newDamageX <= 100 ) )
					{
						this.setDamage( newDamageX, this.getDamageY() );
					}
				} else if ( "DamageXDown".equals( function ) )
				{
					float	newDamageX	= this.getRound( this.getDamageX() - 0.1f, 1);
					if ( newDamageX >= 0.1f )
						this.setDamage( newDamageX, this.getDamageY() );
				} else if ( "DamageYUp".equals( function ) )
				{
					float	newDamageY	= this.getRound( this.getDamageY() + 0.1f, 1);
					if ( newDamageY <= 100 )
						this.setDamage( this.getDamageX(), newDamageY );
				} else if ( "DamageYDown".equals( function ) )
				{
					float	newDamageY	= this.getRound( this.getDamageY() - 0.1f, 1);
					if ( newDamageY >= this.getDamageX() )
						this.setDamage( this.getDamageX(), newDamageY );
				} else if ( "RangeUp".equals( function ) )
				{
					int	newRange	= this.getRange() + 1;
					if ( newRange <= 350 )
						this.setRange( newRange );
				} else if ( "RangeDown".equals( function ) )
				{
					int	newRange	= this.getRange() - 1;
					if ( newRange > 0 )
						this.setRange( newRange );
				} else if ( "SpeedUp".equals( function ) )
				{
					int	newSpeed	= this.getSpeed() + 1;
					if ( newSpeed <= 150 )
						this.setSpeed( newSpeed );
				} else if ( "SpeedDown".equals( function ) )
				{
					int	newSpeed	= this.getSpeed() - 1;
					if ( newSpeed > 0 )
						this.setSpeed( newSpeed );
				} else if ( "HealthUp".equals( function ) )
				{
					int	newHealth	= this.getHealth() + 1;
					if ( newHealth <= 1000 )
						this.setHealth( newHealth );
				} else if ( "HealthDown".equals( function ) )
				{
					int	newHealth	= this.getHealth() - 1;
					if ( newHealth > 0 )
						this.setHealth( newHealth );
				} else if ( "ArmorUp".equals( function ) )
				{
					int	newArmor	= this.getArmor() + 1;
					if ( newArmor <= 100 )
						this.setArmor( newArmor );
				} else if ( "ArmorDown".equals( function ) )
				{
					int	newArmor	= this.getArmor() - 1;
					if ( newArmor > 0 )
						this.setArmor( newArmor );
				} else if ( "PriceUp".equals( function ) )
				{
					int	newPrice	= this.getPrice() + 10;
					if ( newPrice <= 1000 )
						this.setPrice( newPrice );
				} else if ( "PriceDown".equals( function ) )
				{
					int	newPrice	= this.getPrice() - 10;
					if ( newPrice >= 10 )
						this.setPrice( newPrice );
				} else if ( "UpDamageDown".equals( function ) )
				{
					float	newUpDamage	= this.getRound( this.getUpDamage() - 0.01f, 3);
					if ( newUpDamage >= 0.01f )
						this.setUpDamage( newUpDamage );
				} else if ( "UpDamageUp".equals( function ) )
				{
					float	newUpDamage	= this.getRound( this.getUpDamage() + 0.01f, 3);
					if ( newUpDamage <= 1.0 )
						this.setUpDamage( newUpDamage );
				} else if ( "UpRangeDown".equals( function ) )
				{
					float	newUpRange	= this.getRound( this.getUpRange() - 0.01f, 2);
					if ( newUpRange >= 0.01f )
						this.setUpRange( newUpRange );
				} else if ( "UpRangeUp".equals( function ) )
				{
					float	newUpRange	= this.getRound( this.getUpRange() + 0.01f, 2);
					if ( newUpRange <= 0.5 )
						this.setUpRange( newUpRange );
				} else if ( "UpSpeedDown".equals( function ) )
				{
					float	newUpSpeed	= this.getRound( this.getUpSpeed() - 0.002f, 3);
					if ( newUpSpeed >= 0.001f )
						this.setUpSpeed( newUpSpeed );
				} else if ( "UpSpeedUp".equals( function ) )
				{
					float	newUpSpeed	= this.getRound( this.getUpSpeed() + 0.002f, 3);
					if ( newUpSpeed <= 0.5 )
						this.setUpSpeed( newUpSpeed );
				} else if ( "UpHealthDown".equals( function ) )
				{
					float	newUpHealth	= this.getRound( this.getUpHealth() - 0.01f, 2);
					if ( newUpHealth >= 0.01f )
						this.setUpHealth( newUpHealth );
				} else if ( "UpHealthUp".equals( function ) )
				{
					float	newUpHealth	= this.getRound( this.getUpHealth() + 0.01f, 2);
					if ( newUpHealth <= 1.0 )
						this.setUpHealth( newUpHealth );
				} else if ( "UpArmorDown".equals( function ) )
				{
					float	newUpArmor	= this.getRound( this.getUpArmor() - 0.1f, 1);
					if ( newUpArmor >= 0.1f )
						this.setUpArmor( newUpArmor );
				} else if ( "UpArmorUp".equals( function ) )
				{
					float	newUpArmor	= this.getRound( this.getUpArmor() + 0.1f, 1);
					if ( newUpArmor <= 3.0 )
						this.setUpArmor( newUpArmor );
				} else if ( "MaxLevelUp".equals( function ) )
				{
					int	newMaxLevel	= this.getMaxLevel() + 1;
					if ( newMaxLevel <= 99 )
						this.setMaxLevel( newMaxLevel );
				} else if ( "MaxLevelDown".equals( function ) )
				{
					int	newMaxLevel	= this.getMaxLevel() - 1;
					if ( newMaxLevel > 0 )
						this.setMaxLevel( newMaxLevel );
				} else if ( "LevelUp".equals( function ) )
				{
					int	newLevel	= this.getTechLevel() + 1;
					if ( newLevel <= ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL )
						this.setTechLevel( newLevel );
				} else if ( "LevelDown".equals( function ) )
				{
					int	newLevel	= this.getTechLevel() - 1;
					if ( newLevel > 0 )
						this.setTechLevel( newLevel );
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
		if ( this.currentTower == this.TOWER_ARCH )
		{
			ApoDefenceConstants.TOWER_ARCH_AMOR						= ApoDefenceConstantsOriginal.TOWER_ARCH_AMOR;
			ApoDefenceConstants.TOWER_ARCH_AMOR_UPGRADE				= ApoDefenceConstantsOriginal.TOWER_ARCH_AMOR_UPGRADE;
			ApoDefenceConstants.TOWER_ARCH_HEALTH					= ApoDefenceConstantsOriginal.TOWER_ARCH_HEALTH;
			ApoDefenceConstants.TOWER_ARCH_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ARCH_HEALTH_UPGRADE;
			ApoDefenceConstants.TOWER_ARCH_MAXLEVEL					= ApoDefenceConstantsOriginal.TOWER_ARCH_MAXLEVEL;
			ApoDefenceConstants.TOWER_ARCH_LEVEL					= ApoDefenceConstantsOriginal.TOWER_ARCH_LEVEL;
			ApoDefenceConstants.TOWER_ARCH_POINTATTACK				= ApoDefenceConstantsOriginal.TOWER_ARCH_POINTATTACK;
			ApoDefenceConstants.TOWER_ARCH_POINTATTACK_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ARCH_POINTATTACK_UPGRADE;
			ApoDefenceConstants.TOWER_ARCH_PRICE					= ApoDefenceConstantsOriginal.TOWER_ARCH_PRICE;
			ApoDefenceConstants.TOWER_ARCH_PRICE_UPGRADE_PLUS		= ApoDefenceConstantsOriginal.TOWER_ARCH_PRICE_UPGRADE_PLUS;
			ApoDefenceConstants.TOWER_ARCH_SPEED					= ApoDefenceConstantsOriginal.TOWER_ARCH_SPEED;
			ApoDefenceConstants.TOWER_ARCH_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ARCH_SPEED_UPGRADE;
			ApoDefenceConstants.TOWER_ARCH_RANGE					= ApoDefenceConstantsOriginal.TOWER_ARCH_RANGE;
			ApoDefenceConstants.TOWER_ARCH_RANGE_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ARCH_RANGE_UPGRADE;
		} else if ( this.currentTower == this.TOWER_FIRE )
		{
			ApoDefenceConstants.TOWER_FIRE_AMOR						= ApoDefenceConstantsOriginal.TOWER_FIRE_AMOR;
			ApoDefenceConstants.TOWER_FIRE_AMOR_UPGRADE				= ApoDefenceConstantsOriginal.TOWER_FIRE_AMOR_UPGRADE;
			ApoDefenceConstants.TOWER_FIRE_HEALTH					= ApoDefenceConstantsOriginal.TOWER_FIRE_HEALTH;
			ApoDefenceConstants.TOWER_FIRE_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_FIRE_HEALTH_UPGRADE;
			ApoDefenceConstants.TOWER_FIRE_MAXLEVEL					= ApoDefenceConstantsOriginal.TOWER_FIRE_MAXLEVEL;
			ApoDefenceConstants.TOWER_FIRE_LEVEL					= ApoDefenceConstantsOriginal.TOWER_FIRE_LEVEL;
			ApoDefenceConstants.TOWER_FIRE_POINTATTACK				= ApoDefenceConstantsOriginal.TOWER_FIRE_POINTATTACK;
			ApoDefenceConstants.TOWER_FIRE_POINTATTACK_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_FIRE_POINTATTACK_UPGRADE;
			ApoDefenceConstants.TOWER_FIRE_PRICE					= ApoDefenceConstantsOriginal.TOWER_FIRE_PRICE;
			ApoDefenceConstants.TOWER_FIRE_PRICE_UPGRADE_PLUS		= ApoDefenceConstantsOriginal.TOWER_FIRE_PRICE_UPGRADE_PLUS;
			ApoDefenceConstants.TOWER_FIRE_SPEED					= ApoDefenceConstantsOriginal.TOWER_FIRE_SPEED;
			ApoDefenceConstants.TOWER_FIRE_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_FIRE_SPEED_UPGRADE;
			ApoDefenceConstants.TOWER_FIRE_RANGE					= ApoDefenceConstantsOriginal.TOWER_FIRE_RANGE;
			ApoDefenceConstants.TOWER_FIRE_RANGE_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_FIRE_RANGE_UPGRADE;
		} else if ( this.currentTower == this.TOWER_ICE )
		{
			ApoDefenceConstants.TOWER_ICE_AMOR						= ApoDefenceConstantsOriginal.TOWER_ICE_AMOR;
			ApoDefenceConstants.TOWER_ICE_AMOR_UPGRADE				= ApoDefenceConstantsOriginal.TOWER_ICE_AMOR_UPGRADE;
			ApoDefenceConstants.TOWER_ICE_HEALTH					= ApoDefenceConstantsOriginal.TOWER_ICE_HEALTH;
			ApoDefenceConstants.TOWER_ICE_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ICE_HEALTH_UPGRADE;
			ApoDefenceConstants.TOWER_ICE_MAXLEVEL					= ApoDefenceConstantsOriginal.TOWER_ICE_MAXLEVEL;
			ApoDefenceConstants.TOWER_ICE_LEVEL						= ApoDefenceConstantsOriginal.TOWER_ICE_LEVEL;
			ApoDefenceConstants.TOWER_ICE_POINTATTACK				= ApoDefenceConstantsOriginal.TOWER_ICE_POINTATTACK;
			ApoDefenceConstants.TOWER_ICE_POINTATTACK_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ICE_POINTATTACK_UPGRADE;
			ApoDefenceConstants.TOWER_ICE_PRICE						= ApoDefenceConstantsOriginal.TOWER_ICE_PRICE;
			ApoDefenceConstants.TOWER_ICE_PRICE_UPGRADE_PLUS		= ApoDefenceConstantsOriginal.TOWER_ICE_PRICE_UPGRADE_PLUS;
			ApoDefenceConstants.TOWER_ICE_SPEED						= ApoDefenceConstantsOriginal.TOWER_ICE_SPEED;
			ApoDefenceConstants.TOWER_ICE_SPEED_UPGRADE				= ApoDefenceConstantsOriginal.TOWER_ICE_SPEED_UPGRADE;
			ApoDefenceConstants.TOWER_ICE_RANGE						= ApoDefenceConstantsOriginal.TOWER_ICE_RANGE;
			ApoDefenceConstants.TOWER_ICE_RANGE_UPGRADE				= ApoDefenceConstantsOriginal.TOWER_ICE_RANGE_UPGRADE;
		} else if ( this.currentTower == this.TOWER_LIGHT )
		{
			ApoDefenceConstants.TOWER_LIGHT_AMOR					= ApoDefenceConstantsOriginal.TOWER_LIGHT_AMOR;
			ApoDefenceConstants.TOWER_LIGHT_AMOR_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_LIGHT_AMOR_UPGRADE;
			ApoDefenceConstants.TOWER_LIGHT_HEALTH					= ApoDefenceConstantsOriginal.TOWER_LIGHT_HEALTH;
			ApoDefenceConstants.TOWER_LIGHT_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_LIGHT_HEALTH_UPGRADE;
			ApoDefenceConstants.TOWER_LIGHT_MAXLEVEL				= ApoDefenceConstantsOriginal.TOWER_LIGHT_MAXLEVEL;
			ApoDefenceConstants.TOWER_LIGHT_LEVEL					= ApoDefenceConstantsOriginal.TOWER_LIGHT_LEVEL;
			ApoDefenceConstants.TOWER_LIGHT_POINTATTACK				= ApoDefenceConstantsOriginal.TOWER_LIGHT_POINTATTACK;
			ApoDefenceConstants.TOWER_LIGHT_POINTATTACK_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_LIGHT_POINTATTACK_UPGRADE;
			ApoDefenceConstants.TOWER_LIGHT_PRICE					= ApoDefenceConstantsOriginal.TOWER_LIGHT_PRICE;
			ApoDefenceConstants.TOWER_LIGHT_PRICE_UPGRADE_PLUS		= ApoDefenceConstantsOriginal.TOWER_LIGHT_PRICE_UPGRADE_PLUS;
			ApoDefenceConstants.TOWER_LIGHT_SPEED					= ApoDefenceConstantsOriginal.TOWER_LIGHT_SPEED;
			ApoDefenceConstants.TOWER_LIGHT_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_LIGHT_SPEED_UPGRADE;
			ApoDefenceConstants.TOWER_LIGHT_RANGE					= ApoDefenceConstantsOriginal.TOWER_LIGHT_RANGE;
			ApoDefenceConstants.TOWER_LIGHT_RANGE_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_LIGHT_RANGE_UPGRADE;
		} else if ( this.currentTower == this.TOWER_ULTRA )
		{
			ApoDefenceConstants.TOWER_ULTRA_AMOR					= ApoDefenceConstantsOriginal.TOWER_ULTRA_AMOR;
			ApoDefenceConstants.TOWER_ULTRA_AMOR_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ULTRA_AMOR_UPGRADE;
			ApoDefenceConstants.TOWER_ULTRA_HEALTH					= ApoDefenceConstantsOriginal.TOWER_ULTRA_HEALTH;
			ApoDefenceConstants.TOWER_ULTRA_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ULTRA_HEALTH_UPGRADE;
			ApoDefenceConstants.TOWER_ULTRA_MAXLEVEL				= ApoDefenceConstantsOriginal.TOWER_ULTRA_MAXLEVEL;
			ApoDefenceConstants.TOWER_ULTRA_LEVEL					= ApoDefenceConstantsOriginal.TOWER_ULTRA_LEVEL;
			ApoDefenceConstants.TOWER_ULTRA_POINTATTACK				= ApoDefenceConstantsOriginal.TOWER_ULTRA_POINTATTACK;
			ApoDefenceConstants.TOWER_ULTRA_POINTATTACK_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ULTRA_POINTATTACK_UPGRADE;
			ApoDefenceConstants.TOWER_ULTRA_PRICE					= ApoDefenceConstantsOriginal.TOWER_ULTRA_PRICE;
			ApoDefenceConstants.TOWER_ULTRA_PRICE_UPGRADE_PLUS		= ApoDefenceConstantsOriginal.TOWER_ULTRA_PRICE_UPGRADE_PLUS;
			ApoDefenceConstants.TOWER_ULTRA_SPEED					= ApoDefenceConstantsOriginal.TOWER_ULTRA_SPEED;
			ApoDefenceConstants.TOWER_ULTRA_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ULTRA_SPEED_UPGRADE;
			ApoDefenceConstants.TOWER_ULTRA_RANGE					= ApoDefenceConstantsOriginal.TOWER_ULTRA_RANGE;
			ApoDefenceConstants.TOWER_ULTRA_RANGE_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ULTRA_RANGE_UPGRADE;
		}
	}
	
	public boolean isBOriginal()
	{
		if ( 	( ApoDefenceConstants.TOWER_ULTRA_AMOR == ApoDefenceConstantsOriginal.TOWER_ULTRA_AMOR ) &&
				( ApoDefenceConstants.TOWER_ULTRA_AMOR_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ULTRA_AMOR_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ULTRA_HEALTH == ApoDefenceConstantsOriginal.TOWER_ULTRA_HEALTH ) &&
				( ApoDefenceConstants.TOWER_ULTRA_HEALTH_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ULTRA_HEALTH_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ULTRA_MAXLEVEL == ApoDefenceConstantsOriginal.TOWER_ULTRA_MAXLEVEL ) &&
				( ApoDefenceConstants.TOWER_ULTRA_LEVEL == ApoDefenceConstantsOriginal.TOWER_ULTRA_LEVEL ) &&
				( ApoDefenceConstants.TOWER_ULTRA_POINTATTACK == ApoDefenceConstantsOriginal.TOWER_ULTRA_POINTATTACK ) &&
				( ApoDefenceConstants.TOWER_ULTRA_POINTATTACK_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ULTRA_POINTATTACK_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ULTRA_PRICE == ApoDefenceConstantsOriginal.TOWER_ULTRA_PRICE ) &&
				( ApoDefenceConstants.TOWER_ULTRA_PRICE_UPGRADE_PLUS == ApoDefenceConstantsOriginal.TOWER_ULTRA_PRICE_UPGRADE_PLUS ) &&
				( ApoDefenceConstants.TOWER_ULTRA_SPEED == ApoDefenceConstantsOriginal.TOWER_ULTRA_SPEED ) &&
				( ApoDefenceConstants.TOWER_ULTRA_SPEED_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ULTRA_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ULTRA_RANGE == ApoDefenceConstantsOriginal.TOWER_ULTRA_RANGE ) &&
				( ApoDefenceConstants.TOWER_ULTRA_RANGE_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ULTRA_RANGE_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ARCH_AMOR == ApoDefenceConstantsOriginal.TOWER_ARCH_AMOR ) &&
				( ApoDefenceConstants.TOWER_ARCH_AMOR_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ARCH_AMOR_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ARCH_HEALTH == ApoDefenceConstantsOriginal.TOWER_ARCH_HEALTH ) &&
				( ApoDefenceConstants.TOWER_ARCH_HEALTH_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ARCH_HEALTH_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ARCH_MAXLEVEL == ApoDefenceConstantsOriginal.TOWER_ARCH_MAXLEVEL ) &&
				( ApoDefenceConstants.TOWER_ARCH_LEVEL == ApoDefenceConstantsOriginal.TOWER_ARCH_LEVEL ) &&
				( ApoDefenceConstants.TOWER_ARCH_POINTATTACK == ApoDefenceConstantsOriginal.TOWER_ARCH_POINTATTACK ) &&
				( ApoDefenceConstants.TOWER_ARCH_POINTATTACK_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ARCH_POINTATTACK_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ARCH_PRICE == ApoDefenceConstantsOriginal.TOWER_ARCH_PRICE ) &&
				( ApoDefenceConstants.TOWER_ARCH_PRICE_UPGRADE_PLUS == ApoDefenceConstantsOriginal.TOWER_ARCH_PRICE_UPGRADE_PLUS ) &&
				( ApoDefenceConstants.TOWER_ARCH_SPEED == ApoDefenceConstantsOriginal.TOWER_ARCH_SPEED ) &&
				( ApoDefenceConstants.TOWER_ARCH_SPEED_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ARCH_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ARCH_RANGE == ApoDefenceConstantsOriginal.TOWER_ARCH_RANGE ) &&
				( ApoDefenceConstants.TOWER_ARCH_RANGE_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ARCH_RANGE_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_FIRE_AMOR == ApoDefenceConstantsOriginal.TOWER_FIRE_AMOR ) &&
				( ApoDefenceConstants.TOWER_FIRE_AMOR_UPGRADE == ApoDefenceConstantsOriginal.TOWER_FIRE_AMOR_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_FIRE_HEALTH == ApoDefenceConstantsOriginal.TOWER_FIRE_HEALTH ) &&
				( ApoDefenceConstants.TOWER_FIRE_HEALTH_UPGRADE == ApoDefenceConstantsOriginal.TOWER_FIRE_HEALTH_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_FIRE_MAXLEVEL == ApoDefenceConstantsOriginal.TOWER_FIRE_MAXLEVEL ) &&
				( ApoDefenceConstants.TOWER_FIRE_LEVEL == ApoDefenceConstantsOriginal.TOWER_FIRE_LEVEL ) &&
				( ApoDefenceConstants.TOWER_FIRE_POINTATTACK == ApoDefenceConstantsOriginal.TOWER_FIRE_POINTATTACK ) &&
				( ApoDefenceConstants.TOWER_FIRE_POINTATTACK_UPGRADE == ApoDefenceConstantsOriginal.TOWER_FIRE_POINTATTACK_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_FIRE_PRICE == ApoDefenceConstantsOriginal.TOWER_FIRE_PRICE ) &&
				( ApoDefenceConstants.TOWER_FIRE_PRICE_UPGRADE_PLUS == ApoDefenceConstantsOriginal.TOWER_FIRE_PRICE_UPGRADE_PLUS ) &&
				( ApoDefenceConstants.TOWER_FIRE_SPEED == ApoDefenceConstantsOriginal.TOWER_FIRE_SPEED ) &&
				( ApoDefenceConstants.TOWER_FIRE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.TOWER_FIRE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_FIRE_RANGE == ApoDefenceConstantsOriginal.TOWER_FIRE_RANGE ) &&
				( ApoDefenceConstants.TOWER_FIRE_RANGE_UPGRADE == ApoDefenceConstantsOriginal.TOWER_FIRE_RANGE_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ICE_AMOR == ApoDefenceConstantsOriginal.TOWER_ICE_AMOR ) &&
				( ApoDefenceConstants.TOWER_ICE_AMOR_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ICE_AMOR_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ICE_HEALTH == ApoDefenceConstantsOriginal.TOWER_ICE_HEALTH ) &&
				( ApoDefenceConstants.TOWER_ICE_HEALTH_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ICE_HEALTH_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ICE_MAXLEVEL == ApoDefenceConstantsOriginal.TOWER_ICE_MAXLEVEL ) &&
				( ApoDefenceConstants.TOWER_ICE_LEVEL == ApoDefenceConstantsOriginal.TOWER_ICE_LEVEL ) &&
				( ApoDefenceConstants.TOWER_ICE_POINTATTACK == ApoDefenceConstantsOriginal.TOWER_ICE_POINTATTACK ) &&
				( ApoDefenceConstants.TOWER_ICE_POINTATTACK_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ICE_POINTATTACK_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ICE_PRICE == ApoDefenceConstantsOriginal.TOWER_ICE_PRICE ) &&
				( ApoDefenceConstants.TOWER_ICE_PRICE_UPGRADE_PLUS == ApoDefenceConstantsOriginal.TOWER_ICE_PRICE_UPGRADE_PLUS ) &&
				( ApoDefenceConstants.TOWER_ICE_SPEED == ApoDefenceConstantsOriginal.TOWER_ICE_SPEED ) &&
				( ApoDefenceConstants.TOWER_ICE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ICE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_ICE_RANGE == ApoDefenceConstantsOriginal.TOWER_ICE_RANGE ) &&
				( ApoDefenceConstants.TOWER_ICE_RANGE_UPGRADE == ApoDefenceConstantsOriginal.TOWER_ICE_RANGE_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_LIGHT_AMOR == ApoDefenceConstantsOriginal.TOWER_LIGHT_AMOR ) &&
				( ApoDefenceConstants.TOWER_LIGHT_AMOR_UPGRADE == ApoDefenceConstantsOriginal.TOWER_LIGHT_AMOR_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_LIGHT_HEALTH == ApoDefenceConstantsOriginal.TOWER_LIGHT_HEALTH ) &&
				( ApoDefenceConstants.TOWER_LIGHT_HEALTH_UPGRADE == ApoDefenceConstantsOriginal.TOWER_LIGHT_HEALTH_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_LIGHT_MAXLEVEL == ApoDefenceConstantsOriginal.TOWER_LIGHT_MAXLEVEL ) &&
				( ApoDefenceConstants.TOWER_LIGHT_LEVEL == ApoDefenceConstantsOriginal.TOWER_LIGHT_LEVEL ) &&
				( ApoDefenceConstants.TOWER_LIGHT_POINTATTACK == ApoDefenceConstantsOriginal.TOWER_LIGHT_POINTATTACK ) &&
				( ApoDefenceConstants.TOWER_LIGHT_POINTATTACK_UPGRADE == ApoDefenceConstantsOriginal.TOWER_LIGHT_POINTATTACK_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_LIGHT_PRICE == ApoDefenceConstantsOriginal.TOWER_LIGHT_PRICE ) &&
				( ApoDefenceConstants.TOWER_LIGHT_PRICE_UPGRADE_PLUS == ApoDefenceConstantsOriginal.TOWER_LIGHT_PRICE_UPGRADE_PLUS ) &&
				( ApoDefenceConstants.TOWER_LIGHT_SPEED == ApoDefenceConstantsOriginal.TOWER_LIGHT_SPEED ) &&
				( ApoDefenceConstants.TOWER_LIGHT_SPEED_UPGRADE == ApoDefenceConstantsOriginal.TOWER_LIGHT_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.TOWER_LIGHT_RANGE == ApoDefenceConstantsOriginal.TOWER_LIGHT_RANGE ) &&
				( ApoDefenceConstants.TOWER_LIGHT_RANGE_UPGRADE == ApoDefenceConstantsOriginal.TOWER_LIGHT_RANGE_UPGRADE ) )
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
		g.drawString( this.getTower(),			180 + 15,  18 + 244 );
		g.drawString( this.getDamageX()+"",		135 + 15,  39 + 244 );
		g.drawString( this.getDamageY()+"",		345 + 15,  39 + 244 );
		g.drawString( this.getRange()+"",		135 + 15,  61 + 244 );
		g.drawString( this.getSpeed()+"",		345 + 15,  61 + 244 );
		g.drawString( this.getHealth()+"",		135 + 15,  83 + 244 );
		g.drawString( this.getArmor()+"",		345 + 15,  83 + 244 );
		g.drawString( this.getPrice()+"",		135 + 15, 105 + 244 );
		g.drawString( this.getUpDamage()+"",	345 + 15, 105 + 244 );
		g.drawString( this.getUpRange()+"",		135 + 15, 127 + 244 );
		g.drawString( this.getUpSpeed()+"",		345 + 15, 127 + 244 );
		g.drawString( this.getUpHealth()+"",	135 + 15, 149 + 244 );
		g.drawString( this.getUpArmor()+"",		345 + 15, 149 + 244 );
		g.drawString( this.getMaxLevel()+"",	135 + 15, 171 + 244 );
		g.drawString( this.getTechLevel()+"",	345 + 15, 171 + 244 );
	}
	
	private String getTower()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return "Tower Arch";
		else if ( this.currentTower == this.TOWER_FIRE )
			return "Tower Fire";
		else if ( this.currentTower == this.TOWER_ICE )
			return "Tower Ice";
		else if ( this.currentTower == this.TOWER_LIGHT )
			return "Tower Light";
		else if ( this.currentTower == this.TOWER_ULTRA )
			return "Tower Ultra";
		return "";
	}
	
	private int getArmor()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return ApoDefenceConstants.TOWER_ARCH_AMOR;
		else if ( this.currentTower == this.TOWER_FIRE )
			return ApoDefenceConstants.TOWER_FIRE_AMOR;
		else if ( this.currentTower == this.TOWER_ICE )
			return ApoDefenceConstants.TOWER_ICE_AMOR;
		else if ( this.currentTower == this.TOWER_LIGHT )
			return ApoDefenceConstants.TOWER_LIGHT_AMOR;
		else if ( this.currentTower == this.TOWER_ULTRA )
			return ApoDefenceConstants.TOWER_ULTRA_AMOR;
		return 0;
	}
	
	private void setArmor( int armor )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_AMOR	= armor;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_AMOR	= armor;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_AMOR	= armor;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_AMOR	= armor;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_AMOR	= armor;
	}
	
	private int getHealth()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return ApoDefenceConstants.TOWER_ARCH_HEALTH;
		else if ( this.currentTower == this.TOWER_FIRE )
			return ApoDefenceConstants.TOWER_FIRE_HEALTH;
		else if ( this.currentTower == this.TOWER_ICE )
			return ApoDefenceConstants.TOWER_ICE_HEALTH;
		else if ( this.currentTower == this.TOWER_LIGHT )
			return ApoDefenceConstants.TOWER_LIGHT_HEALTH;
		else if ( this.currentTower == this.TOWER_ULTRA )
			return ApoDefenceConstants.TOWER_ULTRA_HEALTH;
		return 0;
	}
	
	private void setHealth( int speed )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_HEALTH	= speed;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_HEALTH	= speed;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_HEALTH	= speed;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_HEALTH	= speed;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_HEALTH	= speed;
	}
	
	private int getRange()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return ApoDefenceConstants.TOWER_ARCH_RANGE;
		else if ( this.currentTower == this.TOWER_FIRE )
			return ApoDefenceConstants.TOWER_FIRE_RANGE;
		else if ( this.currentTower == this.TOWER_ICE )
			return ApoDefenceConstants.TOWER_ICE_RANGE;
		else if ( this.currentTower == this.TOWER_LIGHT )
			return ApoDefenceConstants.TOWER_LIGHT_RANGE;
		else if ( this.currentTower == this.TOWER_ULTRA )
			return ApoDefenceConstants.TOWER_ULTRA_RANGE;
		return 0;
	}
	
	private void setRange( int range )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_RANGE	= range;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_RANGE	= range;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_RANGE	= range;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_RANGE	= range;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_RANGE	= range;
	}
	
	private int getSpeed()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return ApoDefenceConstants.TOWER_ARCH_SPEED;
		else if ( this.currentTower == this.TOWER_FIRE )
			return ApoDefenceConstants.TOWER_FIRE_SPEED;
		else if ( this.currentTower == this.TOWER_ICE )
			return ApoDefenceConstants.TOWER_ICE_SPEED;
		else if ( this.currentTower == this.TOWER_LIGHT )
			return ApoDefenceConstants.TOWER_LIGHT_SPEED;
		else if ( this.currentTower == this.TOWER_ULTRA )
			return ApoDefenceConstants.TOWER_ULTRA_SPEED;
		return 0;
	}
	
	private void setSpeed( int speed )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_SPEED	= speed;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_SPEED	= speed;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_SPEED	= speed;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_SPEED	= speed;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_SPEED	= speed;
	}
	
	private float getDamageX()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return this.getRound( ApoDefenceConstants.TOWER_ARCH_POINTATTACK.x, 1);
		else if ( this.currentTower == this.TOWER_FIRE )
			return this.getRound( ApoDefenceConstants.TOWER_FIRE_POINTATTACK.x, 1);
		else if ( this.currentTower == this.TOWER_ICE )
			return this.getRound( ApoDefenceConstants.TOWER_ICE_POINTATTACK.x, 1);
		else if ( this.currentTower == this.TOWER_LIGHT )
			return this.getRound( ApoDefenceConstants.TOWER_LIGHT_POINTATTACK.x, 1);
		else if ( this.currentTower == this.TOWER_ULTRA )
			return this.getRound( ApoDefenceConstants.TOWER_ULTRA_POINTATTACK.x, 1);
		return 0;
	}
	
	private float getDamageY()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return this.getRound( ApoDefenceConstants.TOWER_ARCH_POINTATTACK.y, 1);
		else if ( this.currentTower == this.TOWER_FIRE )
			return this.getRound( ApoDefenceConstants.TOWER_FIRE_POINTATTACK.y, 1);
		else if ( this.currentTower == this.TOWER_ICE )
			return this.getRound( ApoDefenceConstants.TOWER_ICE_POINTATTACK.y, 1);
		else if ( this.currentTower == this.TOWER_LIGHT )
			return this.getRound( ApoDefenceConstants.TOWER_LIGHT_POINTATTACK.y, 1);
		else if ( this.currentTower == this.TOWER_ULTRA )
			return this.getRound( ApoDefenceConstants.TOWER_ULTRA_POINTATTACK.y, 1);
		return 0;
	}
	
	private void setDamage( float x, float y )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_POINTATTACK	= new ApoDefencePoint( x, y );
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_POINTATTACK	= new ApoDefencePoint( x, y );
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_POINTATTACK	= new ApoDefencePoint( x, y );
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_POINTATTACK	= new ApoDefencePoint( x, y );
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_POINTATTACK	= new ApoDefencePoint( x, y );
	}

	private int getPrice()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return ApoDefenceConstants.TOWER_ARCH_PRICE;
		else if ( this.currentTower == this.TOWER_FIRE )
			return ApoDefenceConstants.TOWER_FIRE_PRICE;
		else if ( this.currentTower == this.TOWER_ICE )
			return ApoDefenceConstants.TOWER_ICE_PRICE;
		else if ( this.currentTower == this.TOWER_LIGHT )
			return ApoDefenceConstants.TOWER_LIGHT_PRICE;
		else if ( this.currentTower == this.TOWER_ULTRA )
			return ApoDefenceConstants.TOWER_ULTRA_PRICE;
		return 0;
	}
	
	private void setPrice( int price )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_PRICE	= price;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_PRICE	= price;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_PRICE	= price;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_PRICE	= price;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_PRICE	= price;
	}
	
	private int getMaxLevel()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return ApoDefenceConstants.TOWER_ARCH_MAXLEVEL;
		else if ( this.currentTower == this.TOWER_FIRE )
			return ApoDefenceConstants.TOWER_FIRE_MAXLEVEL;
		else if ( this.currentTower == this.TOWER_ICE )
			return ApoDefenceConstants.TOWER_ICE_MAXLEVEL;
		else if ( this.currentTower == this.TOWER_LIGHT )
			return ApoDefenceConstants.TOWER_LIGHT_MAXLEVEL;
		else if ( this.currentTower == this.TOWER_ULTRA )
			return ApoDefenceConstants.TOWER_ULTRA_MAXLEVEL;
		return 0;
	}
	
	private void setMaxLevel( int level )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_MAXLEVEL	= level;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_MAXLEVEL	= level;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_MAXLEVEL	= level;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_MAXLEVEL	= level;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_MAXLEVEL	= level;
	}
	
	private int getTechLevel()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return ApoDefenceConstants.TOWER_ARCH_LEVEL;
		else if ( this.currentTower == this.TOWER_FIRE )
			return ApoDefenceConstants.TOWER_FIRE_LEVEL;
		else if ( this.currentTower == this.TOWER_ICE )
			return ApoDefenceConstants.TOWER_ICE_LEVEL;
		else if ( this.currentTower == this.TOWER_LIGHT )
			return ApoDefenceConstants.TOWER_LIGHT_LEVEL;
		else if ( this.currentTower == this.TOWER_ULTRA )
			return ApoDefenceConstants.TOWER_ULTRA_LEVEL;
		return 0;
	}
	
	private void setTechLevel( int level )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_LEVEL	= level;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_LEVEL	= level;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_LEVEL	= level;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_LEVEL	= level;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_LEVEL	= level;
	}
	
	private float getUpDamage()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return this.getRound( ApoDefenceConstants.TOWER_ARCH_POINTATTACK_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_FIRE )
			return this.getRound( ApoDefenceConstants.TOWER_FIRE_POINTATTACK_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ICE )
			return this.getRound( ApoDefenceConstants.TOWER_ICE_POINTATTACK_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_LIGHT )
			return this.getRound( ApoDefenceConstants.TOWER_LIGHT_POINTATTACK_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ULTRA )
			return this.getRound( ApoDefenceConstants.TOWER_ULTRA_POINTATTACK_UPGRADE, 3);
		return 0;
	}
	
	private void setUpDamage( float upDamage )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_POINTATTACK_UPGRADE	= upDamage;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_POINTATTACK_UPGRADE	= upDamage;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_POINTATTACK_UPGRADE	= upDamage;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_POINTATTACK_UPGRADE	= upDamage;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_POINTATTACK_UPGRADE	= upDamage;
	}
	
	private float getUpRange()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return this.getRound( ApoDefenceConstants.TOWER_ARCH_RANGE_UPGRADE, 2);
		else if ( this.currentTower == this.TOWER_FIRE )
			return this.getRound( ApoDefenceConstants.TOWER_FIRE_RANGE_UPGRADE, 2);
		else if ( this.currentTower == this.TOWER_ICE )
			return this.getRound( ApoDefenceConstants.TOWER_ICE_RANGE_UPGRADE, 2);
		else if ( this.currentTower == this.TOWER_LIGHT )
			return this.getRound( ApoDefenceConstants.TOWER_LIGHT_RANGE_UPGRADE, 2);
		else if ( this.currentTower == this.TOWER_ULTRA )
			return this.getRound( ApoDefenceConstants.TOWER_ULTRA_RANGE_UPGRADE, 2);
		return 0;
	}
	
	private void setUpRange( float upRange )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_RANGE_UPGRADE	= upRange;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_RANGE_UPGRADE	= upRange;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_RANGE_UPGRADE		= upRange;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_RANGE_UPGRADE	= upRange;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_RANGE_UPGRADE	= upRange;
	}
	
	private float getUpSpeed()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return this.getRound( ApoDefenceConstants.TOWER_ARCH_SPEED_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_FIRE )
			return this.getRound( ApoDefenceConstants.TOWER_FIRE_SPEED_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ICE )
			return this.getRound( ApoDefenceConstants.TOWER_ICE_SPEED_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_LIGHT )
			return this.getRound( ApoDefenceConstants.TOWER_LIGHT_SPEED_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ULTRA )
			return this.getRound( ApoDefenceConstants.TOWER_ULTRA_SPEED_UPGRADE, 3);
		return 0;
	}
	
	private void setUpSpeed( float upSpeed )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_SPEED_UPGRADE	= upSpeed;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_SPEED_UPGRADE	= upSpeed;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_SPEED_UPGRADE		= upSpeed;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_SPEED_UPGRADE	= upSpeed;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_SPEED_UPGRADE	= upSpeed;
	}
	
	private float getUpHealth()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return  this.getRound(ApoDefenceConstants.TOWER_ARCH_HEALTH_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_FIRE )
			return this.getRound( ApoDefenceConstants.TOWER_FIRE_HEALTH_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ICE )
			return this.getRound( ApoDefenceConstants.TOWER_ICE_HEALTH_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_LIGHT )
			return this.getRound( ApoDefenceConstants.TOWER_LIGHT_HEALTH_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ULTRA )
			return this.getRound( ApoDefenceConstants.TOWER_ULTRA_HEALTH_UPGRADE, 3);
		return 0;
	}
	
	private void setUpHealth( float upHealth )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_HEALTH_UPGRADE	= upHealth;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_HEALTH_UPGRADE	= upHealth;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_HEALTH_UPGRADE		= upHealth;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_HEALTH_UPGRADE	= upHealth;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_HEALTH_UPGRADE	= upHealth;
	}
	
	private float getUpArmor()
	{
		if ( this.currentTower == this.TOWER_ARCH )
			return this.getRound(ApoDefenceConstants.TOWER_ARCH_AMOR_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_FIRE )
			return this.getRound(ApoDefenceConstants.TOWER_FIRE_AMOR_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ICE )
			return this.getRound(ApoDefenceConstants.TOWER_ICE_AMOR_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_LIGHT )
			return this.getRound(ApoDefenceConstants.TOWER_LIGHT_AMOR_UPGRADE, 3);
		else if ( this.currentTower == this.TOWER_ULTRA )
			return this.getRound(ApoDefenceConstants.TOWER_ULTRA_AMOR_UPGRADE, 3);
		return 0;
	}
	
	private void setUpArmor( float upArmor )
	{
		if ( this.currentTower == this.TOWER_ARCH )
			ApoDefenceConstants.TOWER_ARCH_AMOR_UPGRADE		= upArmor;
		else if ( this.currentTower == this.TOWER_FIRE )
			ApoDefenceConstants.TOWER_FIRE_AMOR_UPGRADE		= upArmor;
		else if ( this.currentTower == this.TOWER_ICE )
			ApoDefenceConstants.TOWER_ICE_AMOR_UPGRADE		= upArmor;
		else if ( this.currentTower == this.TOWER_LIGHT )
			ApoDefenceConstants.TOWER_LIGHT_AMOR_UPGRADE	= upArmor;
		else if ( this.currentTower == this.TOWER_ULTRA )
			ApoDefenceConstants.TOWER_ULTRA_AMOR_UPGRADE	= upArmor;
	}
	
	private float getRound( float value, int comma )
	{
		BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(comma,BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
	
}
