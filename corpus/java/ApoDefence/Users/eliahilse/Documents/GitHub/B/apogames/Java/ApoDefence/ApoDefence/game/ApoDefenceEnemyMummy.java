package apoDefence.game;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;

public class ApoDefenceEnemyMummy extends ApoDefenceEnemy
{

	public ApoDefenceEnemyMummy( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level )
	{
		super( ApoDefenceConstants.ENEMY_MUMMY, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_MUMMY_NAME,
						level, 
						ApoDefenceConstants.ENEMY_MUMMY_HEALTH, 
						ApoDefenceConstants.ENEMY_MUMMY_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_MUMMY_SPEED, 
						ApoDefenceConstants.ENEMY_MUMMY_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_MUMMY_ARMOR, 
						ApoDefenceConstants.ENEMY_MUMMY_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_MUMMY_DAMAGE,
						ApoDefenceConstants.ENEMY_MUMMY_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_MUMMY_MONEY * level / 2 );
		
		this.setBUndead( true );
	}
	
	public ApoDefenceEnemyMummy( BufferedImage iEnemy, ArrayList<Point> enemyWay, int level, int startX, int startY )
	{
		super( ApoDefenceConstants.ENEMY_MUMMY, iEnemy, enemyWay );
		
		this.setValues( ApoDefenceConstants.ENEMY_MUMMY_NAME,
						level, 
						ApoDefenceConstants.ENEMY_MUMMY_HEALTH, 
						ApoDefenceConstants.ENEMY_MUMMY_HEALTH_UPGRADE,
						ApoDefenceConstants.ENEMY_MUMMY_SPEED, 
						ApoDefenceConstants.ENEMY_MUMMY_SPEED_UPGRADE, 
						ApoDefenceConstants.ENEMY_MUMMY_ARMOR, 
						ApoDefenceConstants.ENEMY_MUMMY_ARMOR_UPGRADE,
						ApoDefenceConstants.ENEMY_MUMMY_DAMAGE,
						ApoDefenceConstants.ENEMY_MUMMY_DAMAGE_UPGRADE,
						ApoDefenceConstants.ENEMY_MUMMY_MONEY * level / 2 );
		
		this.setCurrentWay( 0 );
		this.setX( startX );
		this.setY( startY );
		this.getNewWay( startX, startY );
		this.setBUndead( true );
	}
	
	public void think( ApoDefenceGame game )
	{
		super.think( game );
		
		if ( this.isBLight() )
			this.setBLight( false );
		else if ( this.isBIce() )
			this.setBIce( false );
		
		if ( this.isNextStep() )
		{
			this.getNewWay( this.getEnemyWay().get( this.getCurrentWay() ).x, this.getEnemyWay().get( this.getCurrentWay() ).y );
		} else
		{
			this.setCountWay( this.getCountWay() - 1 );
			float speedX	= this.getSpeedX();
			float speedY	= this.getSpeedY();
			if ( this.getExtraTime() != -1 )
			{
				if ( this.isBFire() )
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
