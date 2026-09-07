package apoDefence.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefencePoint;

public class ApoDefenceTowerArch extends ApoDefenceTower
{
	private ArrayList<ApoDefenceShoot>	shoot;
	
	public ApoDefenceTowerArch( BufferedImage iTower, int x, int y )
	{
		super( ApoDefenceConstants.TOWER_ARCH, iTower, x, y, "Tower Arch" );
		
		this.setMaxLevel( ApoDefenceConstants.TOWER_ARCH_MAXLEVEL );
		
		this.setValues( ApoDefenceConstants.TOWER_ARCH_RANGE, ApoDefenceConstants.TOWER_ARCH_SPEED, 
				        ApoDefenceConstants.TOWER_ARCH_HEALTH, ApoDefenceConstants.TOWER_ARCH_AMOR,
				        1, ApoDefenceConstants.TOWER_ARCH_PRICE, ApoDefenceConstants.TOWER_ARCH_POINTATTACK );
		
		this.setUpgradeValues( 	ApoDefenceConstants.TOWER_ARCH_PRICE_UPGRADE,
								ApoDefenceConstants.TOWER_ARCH_PRICE_UPGRADE_PLUS,
								ApoDefenceConstants.TOWER_ARCH_RANGE_UPGRADE,
								ApoDefenceConstants.TOWER_ARCH_SPEED_UPGRADE,
								ApoDefenceConstants.TOWER_ARCH_HEALTH_UPGRADE,
								ApoDefenceConstants.TOWER_ARCH_AMOR_UPGRADE,
								ApoDefenceConstants.TOWER_ARCH_POINTATTACK_UPGRADE );
		
		this.shoot		= new ArrayList<ApoDefenceShoot>();
		this.setShoot( this.shoot );
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
						this.getShoot().add( new ApoDefenceShootArch( this, this.getX() + this.getWidth()/2 - 2, this.getY() + this.getHeight()/2 - 2, 4, 4, speedX, speedY, this.getAttack() ) );
						break;
					}
				}
			}
		}
	}

}
