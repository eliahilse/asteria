package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import apoDefence.ApoDefencePoint;

public class ApoDefenceShootEnemy extends ApoDefenceShoot
{
	private float 		speedX, speedY;
	private Color		shootColor;
	
	public ApoDefenceShootEnemy( ApoDefenceEnemy enemy, double x, double y, int width, int height, float speedX, float speedY, ApoDefencePoint damage, Color shootColor )
	{
		super( enemy, x, y, width, height );
		
		this.speedX		= speedX;
		this.speedY		= speedY;
		this.shootColor	= shootColor;
		this.setValues( this.speedX, this.speedY, damage );
	}
	
	public void think( ApoDefenceGame game )
	{
		if ( ( !this.isBVisible() ) || ( this.getEnemy() == null ) )
			return;
		this.setRec( new Rectangle2D.Double( this.getX(), this.getY(), this.getWidth(), this.getHeight() ) );
		
		this.setX( this.getX() + this.getSpeedX() );
		this.setY( this.getY() + this.getSpeedY() );
		
		ArrayList<ApoDefenceTower>	tower	= game.getGameTower().getAllTower();
		for ( int i = 0; i < tower.size(); i++ )
		{
			if ( tower.get( i ).getRec().intersects( this.getRec() ) )
			{
				int damage	= (int)( Math.random() * ( this.getDamage().y - this.getDamage().x ) + this.getDamage().x - tower.get( i ).getArmor() );
				if ( damage > 0  )
					tower.get( i ).setHealth( tower.get( i ).getHealth() - damage );
				this.setBVisible( false );
				if ( tower.get( i ).getHealth() < 1 )
				{
					game.setPoints( game.getPoints() - tower.get( i ).getPrice() * tower.get( i ).getLevel() );
					game.setTower( game.getTower() - 1 );
					tower.remove( i );
				}
				break;
			}
		}
	}
	
	/**
	 * malt das Objekt
	 * @param g
	 */
	public void render( Graphics2D g, int x, int y )
	{
		if ( this.isBVisible() )
		{
			g.setColor( this.shootColor );
			g.fillOval( (int)this.getX() + x, (int)this.getY() + y, (int)( this.getWidth() ), (int)( this.getHeight() ) );
		}
	}
	
	/**
	 * malt das Objekt
	 * @param g
	 */
	public void render( Graphics g, int x, int y )
	{
		this.render( (Graphics2D)g, x, y );
	}

}
