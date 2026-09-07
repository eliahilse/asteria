package apoDefence.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefencePoint;

public class ApoDefenceTowerFire extends ApoDefenceTower
{

	public ApoDefenceTowerFire( BufferedImage iTower, int x, int y )
	{
		super( ApoDefenceConstants.TOWER_FIRE, iTower, x, y, "Tower Fire" );
		
		this.setMaxLevel( ApoDefenceConstants.TOWER_FIRE_MAXLEVEL );
		
		this.setValues( ApoDefenceConstants.TOWER_FIRE_RANGE, ApoDefenceConstants.TOWER_FIRE_SPEED, 
						ApoDefenceConstants.TOWER_FIRE_HEALTH, ApoDefenceConstants.TOWER_FIRE_AMOR,
				        ApoDefenceConstants.TOWER_FIRE_LEVEL, ApoDefenceConstants.TOWER_FIRE_PRICE,
				        ApoDefenceConstants.TOWER_FIRE_POINTATTACK );
		
		this.setUpgradeValues( 	ApoDefenceConstants.TOWER_FIRE_PRICE_UPGRADE,
								ApoDefenceConstants.TOWER_FIRE_PRICE_UPGRADE_PLUS,
								ApoDefenceConstants.TOWER_FIRE_RANGE_UPGRADE,
								ApoDefenceConstants.TOWER_FIRE_SPEED_UPGRADE,
								ApoDefenceConstants.TOWER_FIRE_HEALTH_UPGRADE,
								ApoDefenceConstants.TOWER_FIRE_AMOR_UPGRADE,
								ApoDefenceConstants.TOWER_FIRE_POINTATTACK_UPGRADE );
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
						this.getShoot().add( new ApoDefenceShootFire( this, this.getX() + this.getWidth()/2 - 4, this.getY() + this.getHeight()/2 - 4, 8, 8, speedX, speedY, this.getAttack() ) );
						break;
					}
				}
			}
		}
	}

}
