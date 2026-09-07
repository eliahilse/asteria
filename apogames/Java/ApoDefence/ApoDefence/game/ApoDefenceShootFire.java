package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import apoDefence.ApoDefencePoint;

public class ApoDefenceShootFire extends ApoDefenceShoot
{
	private float 		speedX, speedY;
	private Color		fire;
	
	public ApoDefenceShootFire( ApoDefenceTower tower, double x, double y, int width, int height, float speedX, float speedY, ApoDefencePoint damage )
	{
		super( tower, x, y, width, height );
		
		this.fire		= new Color( 255, 132, 0 );
		
		this.speedX		= speedX;
		this.speedY		= speedY;
		this.setValues( this.speedX, this.speedY, damage );
		this.setBFire( true );
	}
	
	/**
	 * malt das Objekt
	 * @param g
	 */
	public void render( Graphics2D g, int x, int y )
	{
		if ( this.isBVisible() )
		{
			g.setColor( this.fire );
			g.fillOval( (int)this.getX() + x, (int)this.getY() + y, this.getWidth(), this.getHeight() );
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
