package apoDefence.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefencePoint;

public class ApoDefenceTowerLight extends ApoDefenceTower
{

	public ApoDefenceTowerLight( BufferedImage iTower, int x, int y )
	{
		super( ApoDefenceConstants.TOWER_LIGHT, iTower, x, y, "Tower Light" );
		
		this.setMaxLevel( ApoDefenceConstants.TOWER_LIGHT_MAXLEVEL );
		
		this.setValues( ApoDefenceConstants.TOWER_LIGHT_RANGE, ApoDefenceConstants.TOWER_LIGHT_SPEED, 
						ApoDefenceConstants.TOWER_LIGHT_HEALTH, ApoDefenceConstants.TOWER_LIGHT_AMOR,
						ApoDefenceConstants.TOWER_LIGHT_LEVEL, ApoDefenceConstants.TOWER_LIGHT_PRICE,
						ApoDefenceConstants.TOWER_LIGHT_POINTATTACK );
		
		this.setUpgradeValues( 	ApoDefenceConstants.TOWER_LIGHT_PRICE_UPGRADE,
								ApoDefenceConstants.TOWER_LIGHT_PRICE_UPGRADE_PLUS,
								ApoDefenceConstants.TOWER_LIGHT_RANGE_UPGRADE,
								ApoDefenceConstants.TOWER_LIGHT_SPEED_UPGRADE,
								ApoDefenceConstants.TOWER_LIGHT_HEALTH_UPGRADE,
								ApoDefenceConstants.TOWER_LIGHT_AMOR_UPGRADE,
								ApoDefenceConstants.TOWER_LIGHT_POINTATTACK_UPGRADE );
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
						this.getShoot().add( new ApoDefenceShootLight( this, this.getX() + this.getWidth()/2 - 1, this.getY() + this.getHeight()/2 - 1, 2, 2, speedX, speedY, this.getAttack() ) );
						break;
					}
				}
			}
		}
	}

}
