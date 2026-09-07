package apoDefence.game;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;

public class ApoDefenceEnemyHorse extends ApoDefenceEnemy
{

	public ApoDefenceEnemyHorse( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level )
	{
		super( ApoDefenceConstants.ENEMY_HORSE, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_HORSE_NAME,
						level, 
						ApoDefenceConstants.ENEMY_HORSE_HEALTH, 
						ApoDefenceConstants.ENEMY_HORSE_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_HORSE_SPEED, 
						ApoDefenceConstants.ENEMY_HORSE_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_HORSE_ARMOR, 
						ApoDefenceConstants.ENEMY_HORSE_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_HORSE_DAMAGE,
						ApoDefenceConstants.ENEMY_HORSE_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_HORSE_MONEY * level / 2 );
	}
	
	public ApoDefenceEnemyHorse( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level, int startX, int startY )
	{
		super( ApoDefenceConstants.ENEMY_HORSE, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_HORSE_NAME,
						level, 
						ApoDefenceConstants.ENEMY_HORSE_HEALTH, 
						ApoDefenceConstants.ENEMY_HORSE_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_HORSE_SPEED, 
						ApoDefenceConstants.ENEMY_HORSE_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_HORSE_ARMOR, 
						ApoDefenceConstants.ENEMY_HORSE_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_HORSE_DAMAGE,
						ApoDefenceConstants.ENEMY_HORSE_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_HORSE_MONEY * level / 2 );
		
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
				this.setCountWay( this.getCountWay() - ApoDefenceConstants.ENEMY_ICE_HORSE );
			else if ( this.isBLight() )
				this.setCountWay( this.getCountWay() );
			float speedX	= this.getSpeedX();
			float speedY	= this.getSpeedY();
			if ( this.getExtraTime() != -1 )
			{
				if  ( this.isBIce() )
				{
					speedX		= speedX	* ApoDefenceConstants.ENEMY_ICE_HORSE;
					speedY		= speedY	* ApoDefenceConstants.ENEMY_ICE_HORSE;
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
