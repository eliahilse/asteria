package apoDefence.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefencePoint;

public class ApoDefenceTowerIce extends ApoDefenceTower
{

	public ApoDefenceTowerIce( BufferedImage iTower, int x, int y )
	{
		super( ApoDefenceConstants.TOWER_ICE, iTower, x, y, "Tower Ice" );
		
		this.setMaxLevel( ApoDefenceConstants.TOWER_ICE_MAXLEVEL );
		
		this.setValues( ApoDefenceConstants.TOWER_ICE_RANGE, ApoDefenceConstants.TOWER_ICE_SPEED, 
						ApoDefenceConstants.TOWER_ICE_HEALTH, ApoDefenceConstants.TOWER_ICE_AMOR,
						ApoDefenceConstants.TOWER_ICE_LEVEL, ApoDefenceConstants.TOWER_ICE_PRICE,
						ApoDefenceConstants.TOWER_ICE_POINTATTACK );
		
		this.setUpgradeValues( 	ApoDefenceConstants.TOWER_ICE_PRICE_UPGRADE,
								ApoDefenceConstants.TOWER_ICE_PRICE_UPGRADE_PLUS,
								ApoDefenceConstants.TOWER_ICE_RANGE_UPGRADE,
								ApoDefenceConstants.TOWER_ICE_SPEED_UPGRADE,
								ApoDefenceConstants.TOWER_ICE_HEALTH_UPGRADE,
								ApoDefenceConstants.TOWER_ICE_AMOR_UPGRADE,
								ApoDefenceConstants.TOWER_ICE_POINTATTACK_UPGRADE );
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
						/*if ( diffX < 0 )
							diffLevelX		= -diffLevelX;*/
						float diffLevelY	= 0;//enemy.get( i ).getLevel() / 8;
						/*if ( diffY < 0 )
							diffLevelY		= -diffLevelY;*/
						float speedX	= (float)( ApoDefenceConstants.SHOOT_SPEED * diffX + diffLevelX );
						float speedY	= (float)( ApoDefenceConstants.SHOOT_SPEED * diffY + diffLevelY );
						this.getShoot().add( new ApoDefenceShootIce( this, this.getX() + this.getWidth()/2 - 5, this.getY() + this.getHeight()/2 - 5, 10, 10, speedX, speedY, this.getAttack() ) );
						break;
					}
				}
			}
		}
	}

}
