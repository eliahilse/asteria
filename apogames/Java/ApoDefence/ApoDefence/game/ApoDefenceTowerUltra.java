package apoDefence.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefencePoint;

public class ApoDefenceTowerUltra extends ApoDefenceTower
{
	private BufferedImage iShoot;
	
	public ApoDefenceTowerUltra( BufferedImage iTower, int x, int y )
	{
		super( ApoDefenceConstants.TOWER_ULTRA, iTower, x, y, "Tower Ultra" );
		
		this.setMaxLevel( ApoDefenceConstants.TOWER_ULTRA_MAXLEVEL );
		
		this.setValues( ApoDefenceConstants.TOWER_ULTRA_RANGE, ApoDefenceConstants.TOWER_ULTRA_SPEED, 
						ApoDefenceConstants.TOWER_ULTRA_HEALTH, ApoDefenceConstants.TOWER_ULTRA_AMOR,
						ApoDefenceConstants.TOWER_ULTRA_LEVEL, ApoDefenceConstants.TOWER_ULTRA_PRICE,
						ApoDefenceConstants.TOWER_ULTRA_POINTATTACK );
		
		this.setUpgradeValues( 	ApoDefenceConstants.TOWER_ULTRA_PRICE_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_PRICE_UPGRADE_PLUS,
								ApoDefenceConstants.TOWER_ULTRA_RANGE_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_SPEED_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_HEALTH_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_AMOR_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_POINTATTACK_UPGRADE );
	}
	
	public ApoDefenceTowerUltra( BufferedImage iTower, BufferedImage iShoot, int x, int y )
	{
		super( ApoDefenceConstants.TOWER_ULTRA, iTower, x, y, "Tower Ultra" );
		
		this.iShoot	= iShoot;
		
		this.setMaxLevel( ApoDefenceConstants.TOWER_ULTRA_MAXLEVEL );
		
		this.setValues( ApoDefenceConstants.TOWER_ULTRA_RANGE, ApoDefenceConstants.TOWER_ULTRA_SPEED, 
						ApoDefenceConstants.TOWER_ULTRA_HEALTH, ApoDefenceConstants.TOWER_ULTRA_AMOR,
						ApoDefenceConstants.TOWER_ULTRA_LEVEL, ApoDefenceConstants.TOWER_ULTRA_PRICE,
						ApoDefenceConstants.TOWER_ULTRA_POINTATTACK );
		
		this.setUpgradeValues( 	ApoDefenceConstants.TOWER_ULTRA_PRICE_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_PRICE_UPGRADE_PLUS,
								ApoDefenceConstants.TOWER_ULTRA_RANGE_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_SPEED_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_HEALTH_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_AMOR_UPGRADE,
								ApoDefenceConstants.TOWER_ULTRA_POINTATTACK_UPGRADE );
	}
	
	public void think( ApoDefenceGame game )
	{
		super.think( game );
		
		if ( ( this.isBShoot() ) && ( !this.isBBuildUp() ) && ( !this.isBRepair() ) )
		{
			ArrayList<ApoDefenceEnemy>	enemy	= game.getGameEnemy().getAllEnemy();
			for ( int i = 0; i < enemy.size(); i++ )
			{
				if ( enemy.get( i ).getRec().intersects( this.getRangeRec() ) )
				{
					ApoDefencePoint point	= enemy.get( i ).getShootPoint();
					double enemyX	= point.x;
					double enemyY	= point.y;
					double towerX	= this.getX() + this.getWidth()/2;
					double towerY	= this.getY() + this.getHeight()/2;
					if ( this.isIn( enemyX, enemyY, towerX, towerY ) )
					{
						this.setBShoot( false );
						this.setShootTime( (float)( 10000 / this.getSpeed() ) );
						float diffX		= (float)( enemyX - towerX );
						float diffY		= (float)( enemyY - towerY );
						float diffLevelX	= 0;//enemy.get( i ).getLevel() / 8;
						//if ( diffX < 0 )
						//	diffLevelX		= -diffLevelX;
						float diffLevelY	= 0;//enemy.get( i ).getLevel() / 8;
						//if ( diffY < 0 )
						//	diffLevelY		= -diffLevelY;
						float speedX	= (float)( ApoDefenceConstants.SHOOT_SPEED * diffX + diffLevelX );
						float speedY	= (float)( ApoDefenceConstants.SHOOT_SPEED * diffY + diffLevelY );
						if ( this.iShoot == null )
							this.getShoot().add( new ApoDefenceShootUltra( this, this.getX() + this.getWidth()/2 - 15, this.getY() + this.getHeight()/2 - 15, 30, 30, speedX, speedY, this.getAttack() ) );
						else
							this.getShoot().add( new ApoDefenceShootUltra( this, this.getX() + this.getWidth()/2 - 15, this.getY() + this.getHeight()/2 - 15, 30, 30, speedX, speedY, this.getAttack(), this.iShoot ) );
						break;
					}
				}
			}
		}
	}

}
