package apoDefence.game;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;

public class ApoDefenceEnemyDog extends ApoDefenceEnemy
{

	public ApoDefenceEnemyDog( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level )
	{
		super( ApoDefenceConstants.ENEMY_DOG, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_DOG_NAME,
						level, 
						ApoDefenceConstants.ENEMY_DOG_HEALTH, 
						ApoDefenceConstants.ENEMY_DOG_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_DOG_SPEED, 
						ApoDefenceConstants.ENEMY_DOG_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_DOG_ARMOR, 
						ApoDefenceConstants.ENEMY_DOG_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_DOG_DAMAGE,
						ApoDefenceConstants.ENEMY_DOG_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_DOG_MONEY * level / 2 );
	}
	
	public ApoDefenceEnemyDog( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level, int startX, int startY )
	{
		super( ApoDefenceConstants.ENEMY_DOG, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_DOG_NAME,
						level, 
						ApoDefenceConstants.ENEMY_DOG_HEALTH, 
						ApoDefenceConstants.ENEMY_DOG_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_DOG_SPEED, 
						ApoDefenceConstants.ENEMY_DOG_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_DOG_ARMOR, 
						ApoDefenceConstants.ENEMY_DOG_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_DOG_DAMAGE,
						ApoDefenceConstants.ENEMY_DOG_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_DOG_MONEY * level / 2 );
		
		this.setCurrentWay( 0 );
		this.setX( startX );
		this.setY( startY );
		this.getNewWay( startX, startY );
	}
	
	public void think( ApoDefenceGame game )
	{
		super.think( game );
		
		if ( this.isNextStep() )
		{
			this.getNewWay( this.getEnemyWay().get( this.getCurrentWay() ).x, this.getEnemyWay().get( this.getCurrentWay() ).y );
		} else
		{
			if ( ( !this.isBIce() ) && ( !this.isBLight() ) )
				this.setCountWay( this.getCountWay() - 1 );
			else if ( this.isBIce() )
				this.setCountWay( this.getCountWay() - ApoDefenceConstants.ENEMY_ICE );
			else if ( this.isBLight() )
				this.setCountWay( this.getCountWay() );
			float speedX	= this.getSpeedX();
			float speedY	= this.getSpeedY();
			if ( this.getExtraTime() != -1 )
			{
				if  ( this.isBIce() )
				{
					speedX		= speedX	* ApoDefenceConstants.ENEMY_ICE;
					speedY		= speedY	* ApoDefenceConstants.ENEMY_ICE;
				} else if ( this.isBLight() )
				{
					speedX		= 0;
					speedY		= 0;
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
	}

}
