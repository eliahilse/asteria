package apoDefence.game;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;

public class ApoDefenceEnemyMonk extends ApoDefenceEnemy
{

	public ApoDefenceEnemyMonk( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level )
	{
		super( ApoDefenceConstants.ENEMY_MONK, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_MONK_NAME,
						level, 
						ApoDefenceConstants.ENEMY_MONK_HEALTH, 
						ApoDefenceConstants.ENEMY_MONK_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_MONK_SPEED, 
						ApoDefenceConstants.ENEMY_MONK_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_MONK_ARMOR, 
						ApoDefenceConstants.ENEMY_MONK_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_MONK_DAMAGE,
						ApoDefenceConstants.ENEMY_MONK_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_MONK_MONEY * level / 2 );
	}
	
	public ApoDefenceEnemyMonk( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level, int startX, int startY )
	{
		super( ApoDefenceConstants.ENEMY_MONK, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_MONK_NAME,
						level, 
						ApoDefenceConstants.ENEMY_MONK_HEALTH, 
						ApoDefenceConstants.ENEMY_MONK_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_MONK_SPEED, 
						ApoDefenceConstants.ENEMY_MONK_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_MONK_ARMOR, 
						ApoDefenceConstants.ENEMY_MONK_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_MONK_DAMAGE,
						ApoDefenceConstants.ENEMY_MONK_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_MONK_MONEY * level / 2 );
		
		this.setCurrentWay( 0 );
		this.setX( startX );
		this.setY( startY );
		this.getNewWay( startX, startY );
	}
	
	public void think( ApoDefenceGame game )
	{
		super.think( game );
		
		if ( this.isBLight() )
			this.setBLight( false );
		
		if ( this.isNextStep() )
		{
			this.getNewWay( this.getEnemyWay().get( this.getCurrentWay() ).x, this.getEnemyWay().get( this.getCurrentWay() ).y );
		} else
		{
			if ( !this.isBIce() )
				this.setCountWay( this.getCountWay() - 1 );
			else if ( this.isBIce() )
				this.setCountWay( this.getCountWay() - ApoDefenceConstants.ENEMY_ICE );
			this.setCountWay( this.getCountWay() - 1 );
			float speedX	= this.getSpeedX();
			float speedY	= this.getSpeedY();
			if ( this.getExtraTime() != -1 )
			{
				if  ( this.isBIce() )
				{
					speedX		= speedX	* ApoDefenceConstants.ENEMY_ICE;
					speedY		= speedY	* ApoDefenceConstants.ENEMY_ICE;
				} else if ( this.isBFire() )
				{
					this.setHealth( this.getHealth() - this.getExtraFire() );
				}
			}
			this.setX( this.getX() + (double)speedX );
			this.setY( this.getY() + (double)speedY );
			this.setRec( new Rectangle2D.Double( this.getX() - this.getWidth()/2, this.getY() - this.getHeight()/2, this.getWidth(), this.getHeight() ) );
			if ( this.getRec().intersects( game.getCastle().getRec() ) )
			{
				int random		= (int)( Math.random() * ( this.getDamage().y - this.getDamage().x ) + this.getDamage().x );
				game.getCastle().setHealth( game.getCastle().getHealth() - random );
				this.setBVisible( false );
				if ( game.getPoints() - this.getMoney() > 0 )
					game.setPoints( game.getPoints() - this.getMoney() );
				else
					game.setPoints( 0 );
			}
		}
		
		if ( !this.isBShoot() )
			this.setShootTime( this.getShootTime() - ApoDefenceConstants.SHOOT_TIME );
		if ( this.getShootTime() < 0 )
			this.setBShoot( true );
		
		if ( this.isBShoot() )
		{
			ArrayList<ApoDefenceTower>	tower	= game.getGameTower().getAllTower();
			for ( int i = 0; i < tower.size(); i++ )
			{
				if ( tower.get( i ).getRec().intersects( this.getRangeRec() ) )
				{
					double enemyX	= this.getX();
					double enemyY	= this.getY();
					double towerX	= tower.get( i ).getX() + tower.get( i ).getWidth()/2;
					double towerY	= tower.get( i ).getY() + tower.get( i ).getHeight()/2;
					if ( this.isIn( enemyX, enemyY, towerX, towerY ) )
					{
						this.setBShoot( false );
						this.setShootTime( (float)( 50000 / this.getSpeed() ) );
						float diffX		= (float)( towerX - enemyX );
						float diffY		= (float)( towerY - enemyY );
						float diffLevelX	= tower.get( i ).getLevel() / 8;
						if ( diffX < 0 )
							diffLevelX		= -diffLevelX;
						float diffLevelY	= tower.get( i ).getLevel() / 8;
						if ( diffY < 0 )
							diffLevelY		= -diffLevelY;
						float speedX	= (float)( ApoDefenceConstants.SHOOT_SPEED * diffX + diffLevelX );
						float speedY	= (float)( ApoDefenceConstants.SHOOT_SPEED * diffY + diffLevelY );
						this.getShoot().add( new ApoDefenceShootEnemy( this, this.getX() - 4, this.getY() - 4, 8, 8, speedX, speedY, this.getDamage(), new Color( 148, 63, 0 ) ) );
						break;
					}
				}
			}
		}
	}

}
