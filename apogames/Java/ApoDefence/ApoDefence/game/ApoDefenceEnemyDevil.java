package apoDefence.game;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;

public class ApoDefenceEnemyDevil extends ApoDefenceEnemy
{

	public ApoDefenceEnemyDevil( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level )
	{
		super( ApoDefenceConstants.ENEMY_DEVIL, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_DEVIL_NAME,
						level, 
						ApoDefenceConstants.ENEMY_DEVIL_HEALTH, 
						ApoDefenceConstants.ENEMY_DEVIL_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_DEVIL_SPEED, 
						ApoDefenceConstants.ENEMY_DEVIL_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_DEVIL_ARMOR, 
						ApoDefenceConstants.ENEMY_DEVIL_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_DEVIL_DAMAGE,
						ApoDefenceConstants.ENEMY_DEVIL_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_DEVIL_MONEY * level / 2 );
		
		this.setBUndead( true );
	}
	
	public ApoDefenceEnemyDevil( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level, int startX, int startY )
	{
		super( ApoDefenceConstants.ENEMY_DEVIL, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_DEVIL_NAME,
						level, 
						ApoDefenceConstants.ENEMY_DEVIL_HEALTH, 
						ApoDefenceConstants.ENEMY_DEVIL_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_DEVIL_SPEED, 
						ApoDefenceConstants.ENEMY_DEVIL_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_DEVIL_ARMOR, 
						ApoDefenceConstants.ENEMY_DEVIL_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_DEVIL_DAMAGE,
						ApoDefenceConstants.ENEMY_DEVIL_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_DEVIL_MONEY * level / 2 );
		
		this.setCurrentWay( 0 );
		this.setX( startX );
		this.setY( startY );
		this.getNewWay( startX, startY );
		this.setBUndead( true );
	}
	
	public void think( ApoDefenceGame game )
	{
		super.think( game );
		
		if ( this.isBFire() )
			this.setBFire( false );
		
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
					this.setHealth( this.getHealth() - ApoDefenceConstants.ENEMY_ICE_DAMAGE_DEVIL );
				} else if ( this.isBLight() )
				{
					speedX		= 0;
					speedY		= 0;
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
