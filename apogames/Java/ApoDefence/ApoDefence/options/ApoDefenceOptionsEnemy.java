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

public class ApoDefenceOptionsEnemy
{
	private BufferedImage			iBackground;
	private ApoDefenceButton[]		buttons;
	private ApoDefenceOptions		options;
	private int						currentEnemy;
	
	public ApoDefenceOptionsEnemy( ApoDefenceOptions options )
	{
		super();
	
		this.options				= options;
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		this.iBackground			= image.getPic( "/images/options_enemy.png", false );
		
		BufferedImage	button_up	= image.getPic( "/images/button/button_options_up.png", false );
		BufferedImage	button_down	= image.getPic( "/images/button/button_options_down.png", false );
		
		this.buttons				= new ApoDefenceButton[23];
		this.buttons[0]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_original.png", false ),   156 + 15,  172 + 244, 100, 45, "Original" );
		this.buttons[1]				= new ApoDefenceButton( button_down,   140 + 15,    2 + 244,  20, 20, "ChooseEnemyDown" );
		this.buttons[2]				= new ApoDefenceButton( button_up,     260 + 15,    2 + 244,  20, 20, "ChooseEnemyUp" );
		this.buttons[3]				= new ApoDefenceButton( button_down,   105 + 15,   24 + 244,  20, 20, "DamageXDown" );
		this.buttons[4]				= new ApoDefenceButton( button_up,     175 + 15,   24 + 244,  20, 20, "DamageXUp" );
		this.buttons[5]				= new ApoDefenceButton( button_down,   315 + 15,   24 + 244,  20, 20, "DamageYDown" );
		this.buttons[6]				= new ApoDefenceButton( button_up,     385 + 15,   24 + 244,  20, 20, "DamageYUp" );
		this.buttons[7]				= new ApoDefenceButton( button_down,   105 + 15,   46 + 244,  20, 20, "SpeedDown" );
		this.buttons[8]				= new ApoDefenceButton( button_up,     175 + 15,   46 + 244,  20, 20, "SpeedUp" );
		this.buttons[9]				= new ApoDefenceButton( button_down,   315 + 15,   46 + 244,  20, 20, "CoinsDown" );
		this.buttons[10]			= new ApoDefenceButton( button_up,     385 + 15,   46 + 244,  20, 20, "CoinsUp" );
		this.buttons[11]			= new ApoDefenceButton( button_down,   105 + 15,   68 + 244,  20, 20, "HealthDown" );
		this.buttons[12]			= new ApoDefenceButton( button_up,     175 + 15,   68 + 244,  20, 20, "HealthUp" );
		this.buttons[13]			= new ApoDefenceButton( button_down,   315 + 15,   68 + 244,  20, 20, "ArmorDown" );
		this.buttons[14]			= new ApoDefenceButton( button_up,     385 + 15,   68 + 244,  20, 20, "ArmorUp" );
		this.buttons[15]			= new ApoDefenceButton( button_down,   105 + 15,   90 + 244,  20, 20, "UpDamageDown" );
		this.buttons[16]			= new ApoDefenceButton( button_up,     175 + 15,   90 + 244,  20, 20, "UpDamageUp" );
		this.buttons[17]			= new ApoDefenceButton( button_down,   315 + 15,   90 + 244,  20, 20, "UpSpeedDown" );
		this.buttons[18]			= new ApoDefenceButton( button_up,     385 + 15,   90 + 244,  20, 20, "UpSpeedUp" );
		this.buttons[19]			= new ApoDefenceButton( button_down,   105 + 15,  112 + 244,  20, 20, "UpHealthDown" );
		this.buttons[20]			= new ApoDefenceButton( button_up,     175 + 15,  112 + 244,  20, 20, "UpHealthUp" );
		this.buttons[21]			= new ApoDefenceButton( button_down,   315 + 15,  112 + 244,  20, 20, "UpArmorDown" );
		this.buttons[22]			= new ApoDefenceButton( button_up,     385 + 15,  112 + 244,  20, 20, "UpArmorUp" );
		
		this.currentEnemy			= ApoDefenceConstants.ENEMY_BIRD;
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
				} else if ( "ChooseEnemyUp".equals( function ) )
				{
					this.currentEnemy++;
					if ( this.currentEnemy > ApoDefenceConstants.ENEMY_WEREWOLF )
						this.currentEnemy	= ApoDefenceConstants.ENEMY_BIRD;
				} else if ( "ChooseEnemyDown".equals( function ) )
				{
					this.currentEnemy--;
					if ( this.currentEnemy < ApoDefenceConstants.ENEMY_BIRD  )
						this.currentEnemy	= ApoDefenceConstants.ENEMY_WEREWOLF;
				} else if ( "DamageXUp".equals( function ) )
				{
					float	newDamageX	= this.getRound( this.getDamageX() + 0.1f, 3);
					if ( ( newDamageX <= this.getDamageY() ) &&
						 ( newDamageX <= 100 ) )
					{
						this.setDamage( newDamageX, this.getDamageY() );
					}
				} else if ( "DamageXDown".equals( function ) )
				{
					float	newDamageX	= this.getRound( this.getDamageX() - 0.1f, 3);
					if ( newDamageX >= 0.1f )
						this.setDamage( newDamageX, this.getDamageY() );
				} else if ( "DamageYUp".equals( function ) )
				{
					float	newDamageY	= this.getRound( this.getDamageY() + 0.1f, 3);
					if ( newDamageY <= 100 )
						this.setDamage( this.getDamageX(), newDamageY );
				} else if ( "DamageYDown".equals( function ) )
				{
					float	newDamageY	= this.getRound( this.getDamageY() - 0.1f, 3);
					if ( newDamageY >= this.getDamageX() )
						this.setDamage( this.getDamageX(), newDamageY );
				} else if ( "SpeedUp".equals( function ) )
				{
					float	newSpeed	= this.getSpeed() + 0.5f;
					if ( newSpeed <= 100 )
						this.setSpeed( newSpeed );
				} else if ( "SpeedDown".equals( function ) )
				{
					float	newSpeed	= this.getSpeed() - 0.5f;
					if ( newSpeed >= 5 )
						this.setSpeed( newSpeed );
				} else if ( "CoinsUp".equals( function ) )
				{
					float	newCoins	= this.getCoins() + 0.1f;
					if ( newCoins <= 20 )
						this.setCoins( newCoins );
				} else if ( "CoinsDown".equals( function ) )
				{
					float	newCoins	= this.getCoins() - 0.1f;
					if ( newCoins >= 0.3f )
						this.setCoins( newCoins );
				} else if ( "HealthUp".equals( function ) )
				{
					float	newHealth	= this.getHealth() + 0.5f;
					if ( newHealth <= 150 )
						this.setHealth( newHealth );
				} else if ( "HealthDown".equals( function ) )
				{
					float	newHealth	= this.getHealth() - 0.5f;
					if ( newHealth >= 1 )
						this.setHealth( newHealth );
				} else if ( "ArmorUp".equals( function ) )
				{
					float	newArmor	= this.getArmor() + 0.25f;
					if ( newArmor <= 50 )
						this.setArmor( newArmor );
				} else if ( "ArmorDown".equals( function ) )
				{
					float	newArmor	= this.getArmor() - 0.25f;
					if ( newArmor >= 1 )
						this.setArmor( newArmor );
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
				} else if ( "UpSpeedDown".equals( function ) )
				{
					float	newUpSpeed	= this.getRound( this.getUpSpeed() - 0.002f, 3);
					if ( newUpSpeed >= 0.005f )
						this.setUpSpeed( newUpSpeed );
				} else if ( "UpSpeedUp".equals( function ) )
				{
					float	newUpSpeed	= this.getRound( this.getUpSpeed() + 0.002f, 3);
					if ( newUpSpeed <= 0.5 )
						this.setUpSpeed( newUpSpeed );
				} else if ( "UpHealthDown".equals( function ) )
				{
					float	newUpHealth	= this.getRound( this.getUpHealth() - 1f, 3);
					if ( newUpHealth >= 1f )
						this.setUpHealth( newUpHealth );
				} else if ( "UpHealthUp".equals( function ) )
				{
					float	newUpHealth	= this.getRound( this.getUpHealth() + 1f, 3);
					if ( newUpHealth <= 50.0 )
						this.setUpHealth( newUpHealth );
				} else if ( "UpArmorDown".equals( function ) )
				{
					float	newUpArmor	= this.getRound( this.getUpArmor() - 0.025f, 3);
					if ( newUpArmor >= 0.05f )
						this.setUpArmor( newUpArmor );
				} else if ( "UpArmorUp".equals( function ) )
				{
					float	newUpArmor	= this.getRound( this.getUpArmor() + 0.025f, 3);
					if ( newUpArmor <= 3.0 )
						this.setUpArmor( newUpArmor );
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
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
		{
			ApoDefenceConstants.ENEMY_BIRD_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_BIRD_ARMOR;
			ApoDefenceConstants.ENEMY_BIRD_SPEED			= ApoDefenceConstantsOriginal.ENEMY_BIRD_SPEED;
			ApoDefenceConstants.ENEMY_BIRD_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_BIRD_HEALTH;
			ApoDefenceConstants.ENEMY_BIRD_MONEY			= ApoDefenceConstantsOriginal.ENEMY_BIRD_MONEY;
			ApoDefenceConstants.ENEMY_BIRD_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_BIRD_DAMAGE;
			ApoDefenceConstants.ENEMY_BIRD_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_BIRD_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_BIRD_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_BIRD_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_BIRD_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_BIRD_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_BIRD_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_BIRD_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
		{
			ApoDefenceConstants.ENEMY_GHOST_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_GHOST_ARMOR;
			ApoDefenceConstants.ENEMY_GHOST_SPEED			= ApoDefenceConstantsOriginal.ENEMY_GHOST_SPEED;
			ApoDefenceConstants.ENEMY_GHOST_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_GHOST_HEALTH;
			ApoDefenceConstants.ENEMY_GHOST_MONEY			= ApoDefenceConstantsOriginal.ENEMY_GHOST_MONEY;
			ApoDefenceConstants.ENEMY_GHOST_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_GHOST_DAMAGE;
			ApoDefenceConstants.ENEMY_GHOST_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GHOST_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_GHOST_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GHOST_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_GHOST_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GHOST_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_GHOST_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GHOST_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
		{
			ApoDefenceConstants.ENEMY_DOG_ARMOR				= ApoDefenceConstantsOriginal.ENEMY_DOG_ARMOR;
			ApoDefenceConstants.ENEMY_DOG_SPEED				= ApoDefenceConstantsOriginal.ENEMY_DOG_SPEED;
			ApoDefenceConstants.ENEMY_DOG_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_DOG_HEALTH;
			ApoDefenceConstants.ENEMY_DOG_MONEY				= ApoDefenceConstantsOriginal.ENEMY_DOG_MONEY;
			ApoDefenceConstants.ENEMY_DOG_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_DOG_DAMAGE;
			ApoDefenceConstants.ENEMY_DOG_ARMOR_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_DOG_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_DOG_SPEED_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_DOG_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_DOG_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DOG_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_DOG_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DOG_DAMAGE_UPGRADE;
		}  else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
		{
			ApoDefenceConstants.ENEMY_LION_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_LION_ARMOR;
			ApoDefenceConstants.ENEMY_LION_SPEED			= ApoDefenceConstantsOriginal.ENEMY_LION_SPEED;
			ApoDefenceConstants.ENEMY_LION_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_LION_HEALTH;
			ApoDefenceConstants.ENEMY_LION_MONEY			= ApoDefenceConstantsOriginal.ENEMY_LION_MONEY;
			ApoDefenceConstants.ENEMY_LION_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_LION_DAMAGE;
			ApoDefenceConstants.ENEMY_LION_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_LION_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_LION_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_LION_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_LION_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_LION_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_LION_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_LION_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
		{
			ApoDefenceConstants.ENEMY_HORSE_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_HORSE_ARMOR;
			ApoDefenceConstants.ENEMY_HORSE_SPEED			= ApoDefenceConstantsOriginal.ENEMY_HORSE_SPEED;
			ApoDefenceConstants.ENEMY_HORSE_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_HORSE_HEALTH;
			ApoDefenceConstants.ENEMY_HORSE_MONEY			= ApoDefenceConstantsOriginal.ENEMY_HORSE_MONEY;
			ApoDefenceConstants.ENEMY_HORSE_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_HORSE_DAMAGE;
			ApoDefenceConstants.ENEMY_HORSE_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_HORSE_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_HORSE_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_HORSE_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_HORSE_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_HORSE_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_HORSE_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_HORSE_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
		{
			ApoDefenceConstants.ENEMY_DEVIL_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_ARMOR;
			ApoDefenceConstants.ENEMY_DEVIL_SPEED			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_SPEED;
			ApoDefenceConstants.ENEMY_DEVIL_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_HEALTH;
			ApoDefenceConstants.ENEMY_DEVIL_MONEY			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_MONEY;
			ApoDefenceConstants.ENEMY_DEVIL_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_DAMAGE;
			ApoDefenceConstants.ENEMY_DEVIL_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DEVIL_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_DEVIL_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DEVIL_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_DEVIL_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DEVIL_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_DEVIL_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DEVIL_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
		{
			ApoDefenceConstants.ENEMY_AQUATIC_ARMOR				= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_ARMOR;
			ApoDefenceConstants.ENEMY_AQUATIC_SPEED				= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_SPEED;
			ApoDefenceConstants.ENEMY_AQUATIC_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_HEALTH;
			ApoDefenceConstants.ENEMY_AQUATIC_MONEY				= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_MONEY;
			ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_DAMAGE;
			ApoDefenceConstants.ENEMY_AQUATIC_ARMOR_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_AQUATIC_SPEED_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_AQUATIC_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
		{
			ApoDefenceConstants.ENEMY_DRACULA_ARMOR				= ApoDefenceConstantsOriginal.ENEMY_DRACULA_ARMOR;
			ApoDefenceConstants.ENEMY_DRACULA_SPEED				= ApoDefenceConstantsOriginal.ENEMY_DRACULA_SPEED;
			ApoDefenceConstants.ENEMY_DRACULA_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_DRACULA_HEALTH;
			ApoDefenceConstants.ENEMY_DRACULA_MONEY				= ApoDefenceConstantsOriginal.ENEMY_DRACULA_MONEY;
			ApoDefenceConstants.ENEMY_DRACULA_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_DRACULA_DAMAGE;
			ApoDefenceConstants.ENEMY_DRACULA_ARMOR_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_DRACULA_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_DRACULA_SPEED_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_DRACULA_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_DRACULA_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DRACULA_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_DRACULA_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DRACULA_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
		{
			ApoDefenceConstants.ENEMY_FIRE_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_FIRE_ARMOR;
			ApoDefenceConstants.ENEMY_FIRE_SPEED			= ApoDefenceConstantsOriginal.ENEMY_FIRE_SPEED;
			ApoDefenceConstants.ENEMY_FIRE_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_FIRE_HEALTH;
			ApoDefenceConstants.ENEMY_FIRE_MONEY			= ApoDefenceConstantsOriginal.ENEMY_FIRE_MONEY;
			ApoDefenceConstants.ENEMY_FIRE_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_FIRE_DAMAGE;
			ApoDefenceConstants.ENEMY_FIRE_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_FIRE_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_FIRE_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_FIRE_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_FIRE_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_FIRE_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_FIRE_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_FIRE_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
		{
			ApoDefenceConstants.ENEMY_DRAGON_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_ARMOR;
			ApoDefenceConstants.ENEMY_DRAGON_SPEED			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_SPEED;
			ApoDefenceConstants.ENEMY_DRAGON_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_HEALTH;
			ApoDefenceConstants.ENEMY_DRAGON_MONEY			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_MONEY;
			ApoDefenceConstants.ENEMY_DRAGON_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_DAMAGE;
			ApoDefenceConstants.ENEMY_DRAGON_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DRAGON_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_DRAGON_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DRAGON_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_DRAGON_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DRAGON_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_DRAGON_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_DRAGON_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
		{
			ApoDefenceConstants.ENEMY_GOBLIN_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_ARMOR;
			ApoDefenceConstants.ENEMY_GOBLIN_SPEED			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_SPEED;
			ApoDefenceConstants.ENEMY_GOBLIN_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_HEALTH;
			ApoDefenceConstants.ENEMY_GOBLIN_MONEY			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_MONEY;
			ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_DAMAGE;
			ApoDefenceConstants.ENEMY_GOBLIN_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_GOBLIN_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_GOBLIN_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
		{
			ApoDefenceConstants.ENEMY_KNIGHT_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_ARMOR;
			ApoDefenceConstants.ENEMY_KNIGHT_SPEED			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_SPEED;
			ApoDefenceConstants.ENEMY_KNIGHT_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_HEALTH;
			ApoDefenceConstants.ENEMY_KNIGHT_MONEY			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_MONEY;
			ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_DAMAGE;
			ApoDefenceConstants.ENEMY_KNIGHT_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_KNIGHT_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_KNIGHT_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
		{
			ApoDefenceConstants.ENEMY_MONK_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_MONK_ARMOR;
			ApoDefenceConstants.ENEMY_MONK_SPEED			= ApoDefenceConstantsOriginal.ENEMY_MONK_SPEED;
			ApoDefenceConstants.ENEMY_MONK_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_MONK_HEALTH;
			ApoDefenceConstants.ENEMY_MONK_MONEY			= ApoDefenceConstantsOriginal.ENEMY_MONK_MONEY;
			ApoDefenceConstants.ENEMY_MONK_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_MONK_DAMAGE;
			ApoDefenceConstants.ENEMY_MONK_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MONK_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_MONK_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MONK_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_MONK_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MONK_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_MONK_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MONK_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
		{
			ApoDefenceConstants.ENEMY_MUMMY_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_ARMOR;
			ApoDefenceConstants.ENEMY_MUMMY_SPEED			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_SPEED;
			ApoDefenceConstants.ENEMY_MUMMY_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_HEALTH;
			ApoDefenceConstants.ENEMY_MUMMY_MONEY			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_MONEY;
			ApoDefenceConstants.ENEMY_MUMMY_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_DAMAGE;
			ApoDefenceConstants.ENEMY_MUMMY_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MUMMY_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_MUMMY_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MUMMY_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_MUMMY_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MUMMY_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_MUMMY_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_MUMMY_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
		{
			ApoDefenceConstants.ENEMY_SKELETON_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_ARMOR;
			ApoDefenceConstants.ENEMY_SKELETON_SPEED			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_SPEED;
			ApoDefenceConstants.ENEMY_SKELETON_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_HEALTH;
			ApoDefenceConstants.ENEMY_SKELETON_MONEY			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_MONEY;
			ApoDefenceConstants.ENEMY_SKELETON_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_DAMAGE;
			ApoDefenceConstants.ENEMY_SKELETON_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKELETON_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_SKELETON_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKELETON_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_SKELETON_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKELETON_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_SKELETON_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKELETON_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
		{
			ApoDefenceConstants.ENEMY_SKULL_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_SKULL_ARMOR;
			ApoDefenceConstants.ENEMY_SKULL_SPEED			= ApoDefenceConstantsOriginal.ENEMY_SKULL_SPEED;
			ApoDefenceConstants.ENEMY_SKULL_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_SKULL_HEALTH;
			ApoDefenceConstants.ENEMY_SKULL_MONEY			= ApoDefenceConstantsOriginal.ENEMY_SKULL_MONEY;
			ApoDefenceConstants.ENEMY_SKULL_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_SKULL_DAMAGE;
			ApoDefenceConstants.ENEMY_SKULL_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKULL_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_SKULL_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKULL_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_SKULL_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKULL_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_SKULL_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SKULL_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
		{
			ApoDefenceConstants.ENEMY_SNAKE_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_ARMOR;
			ApoDefenceConstants.ENEMY_SNAKE_SPEED			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_SPEED;
			ApoDefenceConstants.ENEMY_SNAKE_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_HEALTH;
			ApoDefenceConstants.ENEMY_SNAKE_MONEY			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_MONEY;
			ApoDefenceConstants.ENEMY_SNAKE_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_DAMAGE;
			ApoDefenceConstants.ENEMY_SNAKE_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SNAKE_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_SNAKE_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SNAKE_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_SNAKE_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SNAKE_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_SNAKE_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_SNAKE_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
		{
			ApoDefenceConstants.ENEMY_UNDEAD_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_ARMOR;
			ApoDefenceConstants.ENEMY_UNDEAD_SPEED			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_SPEED;
			ApoDefenceConstants.ENEMY_UNDEAD_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_HEALTH;
			ApoDefenceConstants.ENEMY_UNDEAD_MONEY			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_MONEY;
			ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_DAMAGE;
			ApoDefenceConstants.ENEMY_UNDEAD_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_UNDEAD_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_UNDEAD_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
		{
			ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_ARMOR;
			ApoDefenceConstants.ENEMY_UNDEFINED_SPEED			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_SPEED;
			ApoDefenceConstants.ENEMY_UNDEFINED_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_HEALTH;
			ApoDefenceConstants.ENEMY_UNDEFINED_MONEY			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_MONEY;
			ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_DAMAGE;
			ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_UNDEFINED_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_UNDEFINED_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_DAMAGE_UPGRADE;
		} else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
		{
			ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_ARMOR;
			ApoDefenceConstants.ENEMY_WEREWOLF_SPEED			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_SPEED;
			ApoDefenceConstants.ENEMY_WEREWOLF_HEALTH			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_HEALTH;
			ApoDefenceConstants.ENEMY_WEREWOLF_MONEY			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_MONEY;
			ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_DAMAGE;
			ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_ARMOR_UPGRADE;
			ApoDefenceConstants.ENEMY_WEREWOLF_SPEED_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_SPEED_UPGRADE;
			ApoDefenceConstants.ENEMY_WEREWOLF_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_HEALTH_UPGRADE;
			ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE_UPGRADE	= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_DAMAGE_UPGRADE;
		}
	}
	
	public boolean isBOriginal()
	{
		if (	( ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_SPEED == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_SPEED ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_HEALTH == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_MONEY == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_MONEY ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_BIRD_ARMOR == ApoDefenceConstantsOriginal.ENEMY_BIRD_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_BIRD_SPEED == ApoDefenceConstantsOriginal.ENEMY_BIRD_SPEED ) &&
				( ApoDefenceConstants.ENEMY_BIRD_HEALTH == ApoDefenceConstantsOriginal.ENEMY_BIRD_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_BIRD_MONEY == ApoDefenceConstantsOriginal.ENEMY_BIRD_MONEY ) &&
				( ApoDefenceConstants.ENEMY_BIRD_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_BIRD_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_BIRD_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_BIRD_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_BIRD_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_BIRD_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_BIRD_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_BIRD_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_BIRD_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_BIRD_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GHOST_ARMOR == ApoDefenceConstantsOriginal.ENEMY_GHOST_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_GHOST_SPEED == ApoDefenceConstantsOriginal.ENEMY_GHOST_SPEED ) &&
				( ApoDefenceConstants.ENEMY_GHOST_HEALTH == ApoDefenceConstantsOriginal.ENEMY_GHOST_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_GHOST_MONEY == ApoDefenceConstantsOriginal.ENEMY_GHOST_MONEY ) &&
				( ApoDefenceConstants.ENEMY_GHOST_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_GHOST_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_GHOST_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GHOST_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GHOST_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GHOST_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GHOST_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GHOST_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GHOST_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GHOST_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DOG_ARMOR == ApoDefenceConstantsOriginal.ENEMY_DOG_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_DOG_SPEED == ApoDefenceConstantsOriginal.ENEMY_DOG_SPEED ) &&
				( ApoDefenceConstants.ENEMY_DOG_HEALTH == ApoDefenceConstantsOriginal.ENEMY_DOG_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_DOG_MONEY == ApoDefenceConstantsOriginal.ENEMY_DOG_MONEY ) &&
				( ApoDefenceConstants.ENEMY_DOG_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_DOG_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_DOG_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DOG_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DOG_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DOG_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DOG_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DOG_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DOG_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DOG_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_LION_ARMOR == ApoDefenceConstantsOriginal.ENEMY_LION_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_LION_SPEED == ApoDefenceConstantsOriginal.ENEMY_LION_SPEED ) &&
				( ApoDefenceConstants.ENEMY_LION_HEALTH == ApoDefenceConstantsOriginal.ENEMY_LION_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_LION_MONEY == ApoDefenceConstantsOriginal.ENEMY_LION_MONEY ) &&
				( ApoDefenceConstants.ENEMY_LION_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_LION_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_LION_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_LION_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_LION_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_LION_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_LION_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_LION_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_LION_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_LION_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_HORSE_ARMOR == ApoDefenceConstantsOriginal.ENEMY_HORSE_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_HORSE_SPEED == ApoDefenceConstantsOriginal.ENEMY_HORSE_SPEED ) &&
				( ApoDefenceConstants.ENEMY_HORSE_HEALTH == ApoDefenceConstantsOriginal.ENEMY_HORSE_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_HORSE_MONEY == ApoDefenceConstantsOriginal.ENEMY_HORSE_MONEY ) &&
				( ApoDefenceConstants.ENEMY_HORSE_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_HORSE_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_HORSE_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_HORSE_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_HORSE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_HORSE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_HORSE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_HORSE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_HORSE_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_HORSE_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_ARMOR == ApoDefenceConstantsOriginal.ENEMY_DEVIL_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_SPEED == ApoDefenceConstantsOriginal.ENEMY_DEVIL_SPEED ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_HEALTH == ApoDefenceConstantsOriginal.ENEMY_DEVIL_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_MONEY == ApoDefenceConstantsOriginal.ENEMY_DEVIL_MONEY ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_DEVIL_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DEVIL_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DEVIL_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DEVIL_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DEVIL_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DEVIL_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_ARMOR == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_SPEED == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_SPEED ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_HEALTH == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_MONEY == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_MONEY ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_AQUATIC_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_ARMOR == ApoDefenceConstantsOriginal.ENEMY_DRACULA_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_SPEED == ApoDefenceConstantsOriginal.ENEMY_DRACULA_SPEED ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_HEALTH == ApoDefenceConstantsOriginal.ENEMY_DRACULA_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_MONEY == ApoDefenceConstantsOriginal.ENEMY_DRACULA_MONEY ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_DRACULA_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRACULA_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRACULA_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRACULA_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRACULA_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRACULA_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_FIRE_ARMOR == ApoDefenceConstantsOriginal.ENEMY_FIRE_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_FIRE_SPEED == ApoDefenceConstantsOriginal.ENEMY_FIRE_SPEED ) &&
				( ApoDefenceConstants.ENEMY_FIRE_HEALTH == ApoDefenceConstantsOriginal.ENEMY_FIRE_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_FIRE_MONEY == ApoDefenceConstantsOriginal.ENEMY_FIRE_MONEY ) &&
				( ApoDefenceConstants.ENEMY_FIRE_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_FIRE_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_FIRE_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_FIRE_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_FIRE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_FIRE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_FIRE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_FIRE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_FIRE_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_FIRE_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_ARMOR == ApoDefenceConstantsOriginal.ENEMY_DRAGON_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_SPEED == ApoDefenceConstantsOriginal.ENEMY_DRAGON_SPEED ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_HEALTH == ApoDefenceConstantsOriginal.ENEMY_DRAGON_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_MONEY == ApoDefenceConstantsOriginal.ENEMY_DRAGON_MONEY ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_DRAGON_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRAGON_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRAGON_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRAGON_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_DRAGON_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_DRAGON_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_ARMOR == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_SPEED == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_SPEED ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_HEALTH == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_MONEY == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_MONEY ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_GOBLIN_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_ARMOR == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_SPEED == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_SPEED ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_HEALTH == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_MONEY == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_MONEY ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_KNIGHT_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MONK_ARMOR == ApoDefenceConstantsOriginal.ENEMY_MONK_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_MONK_SPEED == ApoDefenceConstantsOriginal.ENEMY_MONK_SPEED ) &&
				( ApoDefenceConstants.ENEMY_MONK_HEALTH == ApoDefenceConstantsOriginal.ENEMY_MONK_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_MONK_MONEY == ApoDefenceConstantsOriginal.ENEMY_MONK_MONEY ) &&
				( ApoDefenceConstants.ENEMY_MONK_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_MONK_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_MONK_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MONK_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MONK_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MONK_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MONK_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MONK_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MONK_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MONK_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_ARMOR == ApoDefenceConstantsOriginal.ENEMY_MUMMY_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_SPEED == ApoDefenceConstantsOriginal.ENEMY_MUMMY_SPEED ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_HEALTH == ApoDefenceConstantsOriginal.ENEMY_MUMMY_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_MONEY == ApoDefenceConstantsOriginal.ENEMY_MUMMY_MONEY ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_MUMMY_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MUMMY_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MUMMY_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MUMMY_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_MUMMY_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_MUMMY_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_ARMOR == ApoDefenceConstantsOriginal.ENEMY_SKELETON_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_SPEED == ApoDefenceConstantsOriginal.ENEMY_SKELETON_SPEED ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_HEALTH == ApoDefenceConstantsOriginal.ENEMY_SKELETON_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_MONEY == ApoDefenceConstantsOriginal.ENEMY_SKELETON_MONEY ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_SKELETON_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKELETON_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKELETON_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKELETON_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKELETON_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKELETON_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKULL_ARMOR == ApoDefenceConstantsOriginal.ENEMY_SKULL_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_SKULL_SPEED == ApoDefenceConstantsOriginal.ENEMY_SKULL_SPEED ) &&
				( ApoDefenceConstants.ENEMY_SKULL_HEALTH == ApoDefenceConstantsOriginal.ENEMY_SKULL_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_SKULL_MONEY == ApoDefenceConstantsOriginal.ENEMY_SKULL_MONEY ) &&
				( ApoDefenceConstants.ENEMY_SKULL_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_SKULL_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_SKULL_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKULL_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKULL_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKULL_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKULL_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKULL_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SKULL_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SKULL_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_ARMOR == ApoDefenceConstantsOriginal.ENEMY_SNAKE_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_SPEED == ApoDefenceConstantsOriginal.ENEMY_SNAKE_SPEED ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_HEALTH == ApoDefenceConstantsOriginal.ENEMY_SNAKE_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_MONEY == ApoDefenceConstantsOriginal.ENEMY_SNAKE_MONEY ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_SNAKE_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SNAKE_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SNAKE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SNAKE_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_SNAKE_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_SNAKE_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_ARMOR == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_SPEED == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_SPEED ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_HEALTH == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_MONEY == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_MONEY ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEAD_DAMAGE_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_ARMOR ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_SPEED == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_SPEED ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_HEALTH == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_HEALTH ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_MONEY == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_MONEY ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_DAMAGE ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_ARMOR_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_SPEED_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_SPEED_UPGRADE ) &&
				( ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE_UPGRADE == ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_DAMAGE_UPGRADE ) )
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
		g.drawString( this.getEnemy(),			180 + 15,  18 + 244 );
		g.drawString( this.getDamageX()+"",		135 + 15,  39 + 244 );
		g.drawString( this.getDamageY()+"",		345 + 15,  39 + 244 );
		g.drawString( this.getSpeed()+"",		135 + 15,  61 + 244 );
		g.drawString( this.getCoins()+"",		345 + 15,  61 + 244 );
		g.drawString( this.getHealth()+"",		135 + 15,  83 + 244 );
		g.drawString( this.getArmor()+"",		345 + 15,  83 + 244 );
		g.drawString( this.getUpDamage()+"",	135 + 15, 105 + 244 );
		g.drawString( this.getUpSpeed()+"",		345 + 15, 105 + 244 );
		g.drawString( this.getUpHealth()+"",	135 + 15, 127 + 244 );
		g.drawString( this.getUpArmor()+"",		345 + 15, 127 + 244 );
	}
	
	private String getEnemy()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return ApoDefenceConstants.ENEMY_BIRD_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return ApoDefenceConstants.ENEMY_GHOST_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return ApoDefenceConstants.ENEMY_DOG_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return ApoDefenceConstants.ENEMY_LION_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return ApoDefenceConstants.ENEMY_HORSE_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return ApoDefenceConstants.ENEMY_AQUATIC_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return ApoDefenceConstants.ENEMY_DEVIL_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return ApoDefenceConstants.ENEMY_DRACULA_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return ApoDefenceConstants.ENEMY_FIRE_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
				return ApoDefenceConstants.ENEMY_DRAGON_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return ApoDefenceConstants.ENEMY_GOBLIN_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return ApoDefenceConstants.ENEMY_KNIGHT_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return ApoDefenceConstants.ENEMY_MONK_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return ApoDefenceConstants.ENEMY_MUMMY_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return ApoDefenceConstants.ENEMY_SKELETON_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return ApoDefenceConstants.ENEMY_SKULL_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return ApoDefenceConstants.ENEMY_SNAKE_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return ApoDefenceConstants.ENEMY_UNDEAD_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return ApoDefenceConstants.ENEMY_UNDEFINED_NAME;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return ApoDefenceConstants.ENEMY_WEREWOLF_NAME;
		return "";
	}
	
	private float getDamageX()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE.x, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE.x, 3);
		return 0;
	}
	
	private float getDamageY()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE.y, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE.y, 3);
		return 0;
	}
	
	private void setDamage( float x, float y )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_DAMAGE	= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE= new ApoDefencePoint( x, y );
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE= new ApoDefencePoint( x, y );
	}
	
	private float getSpeed()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_SPEED, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_SPEED, 3);
		return 0;
	}
	
	private void setSpeed( float speed )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_SPEED	= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_SPEED= speed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_SPEED= speed;
	}
	
	private float getCoins()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_MONEY, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_MONEY, 3);
		return 0;
	}
	
	private void setCoins( float coins )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_MONEY	= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_MONEY= coins;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_MONEY= coins;
	}
	
	private float getHealth()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_HEALTH, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_HEALTH, 3);
		return 0;
	}
	
	private void setHealth( float health )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_HEALTH	= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_HEALTH= health;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_HEALTH= health;
	}
	
	private float getArmor()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR, 3);
		return 0;
	}
	
	private void setArmor( float armor )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_ARMOR	= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR= armor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR= armor;
	}
	
	private float getUpDamage()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE_UPGRADE, 3);
		return 0;
	}
	
	private void setUpDamage( float upDamage )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_DAMAGE_UPGRADE	= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_DAMAGE_UPGRADE= upDamage;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_DAMAGE_UPGRADE= upDamage;
	}
	
	private float getUpSpeed()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_SPEED_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_SPEED_UPGRADE, 3);
		return 0;
	}
	
	private void setUpSpeed( float upSpeed )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_SPEED_UPGRADE	= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_SPEED_UPGRADE= upSpeed;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_SPEED_UPGRADE= upSpeed;
	}
	
	private float getUpHealth()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_HEALTH_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_HEALTH_UPGRADE, 3);
		return 0;
	}
	
	private void setUpHealth( float upHealth )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_HEALTH_UPGRADE	= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_HEALTH_UPGRADE= upHealth;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_HEALTH_UPGRADE= upHealth;
	}
	
	private float getUpArmor()
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			return this.getRound( ApoDefenceConstants.ENEMY_BIRD_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			return this.getRound( ApoDefenceConstants.ENEMY_GHOST_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			return this.getRound( ApoDefenceConstants.ENEMY_DOG_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			return this.getRound( ApoDefenceConstants.ENEMY_LION_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			return this.getRound( ApoDefenceConstants.ENEMY_HORSE_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			return this.getRound( ApoDefenceConstants.ENEMY_DEVIL_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			return this.getRound( ApoDefenceConstants.ENEMY_AQUATIC_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			return this.getRound( ApoDefenceConstants.ENEMY_DRACULA_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			return this.getRound( ApoDefenceConstants.ENEMY_FIRE_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			return this.getRound( ApoDefenceConstants.ENEMY_DRAGON_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			return this.getRound( ApoDefenceConstants.ENEMY_GOBLIN_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			return this.getRound( ApoDefenceConstants.ENEMY_KNIGHT_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			return this.getRound( ApoDefenceConstants.ENEMY_MONK_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			return this.getRound( ApoDefenceConstants.ENEMY_MUMMY_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			return this.getRound( ApoDefenceConstants.ENEMY_SKELETON_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			return this.getRound( ApoDefenceConstants.ENEMY_SKULL_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			return this.getRound( ApoDefenceConstants.ENEMY_SNAKE_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEAD_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			return this.getRound( ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR_UPGRADE, 3);
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			return this.getRound( ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR_UPGRADE, 3);
		return 0;
	}
	
	private void setUpArmor( float upArmor )
	{
		if ( this.currentEnemy == ApoDefenceConstants.ENEMY_BIRD )
			ApoDefenceConstants.ENEMY_BIRD_ARMOR_UPGRADE	= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GHOST )
			ApoDefenceConstants.ENEMY_GHOST_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DOG )
			ApoDefenceConstants.ENEMY_DOG_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_LION )
			ApoDefenceConstants.ENEMY_LION_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_HORSE )
			ApoDefenceConstants.ENEMY_HORSE_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DEVIL )
			ApoDefenceConstants.ENEMY_DEVIL_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_AQUATIC )
			ApoDefenceConstants.ENEMY_AQUATIC_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRACULA )
			ApoDefenceConstants.ENEMY_DRACULA_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_FIRE )
			ApoDefenceConstants.ENEMY_FIRE_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_DRAGON )
			ApoDefenceConstants.ENEMY_DRAGON_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_GOBLIN )
			ApoDefenceConstants.ENEMY_GOBLIN_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_KNIGHT )
			ApoDefenceConstants.ENEMY_KNIGHT_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MONK )
			ApoDefenceConstants.ENEMY_MONK_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_MUMMY )
			ApoDefenceConstants.ENEMY_MUMMY_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKELETON )
			ApoDefenceConstants.ENEMY_SKELETON_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SKULL )
			ApoDefenceConstants.ENEMY_SKULL_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_SNAKE )
			ApoDefenceConstants.ENEMY_SNAKE_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEAD )
			ApoDefenceConstants.ENEMY_UNDEAD_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_UNDEFINED )
			ApoDefenceConstants.ENEMY_UNDEFINED_ARMOR_UPGRADE= upArmor;
		else if ( this.currentEnemy == ApoDefenceConstants.ENEMY_WEREWOLF )
			ApoDefenceConstants.ENEMY_WEREWOLF_ARMOR_UPGRADE= upArmor;
	}
	
	private float getRound( float value, int comma )
	{
		BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(comma,BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}

}
