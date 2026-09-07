package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import apoDefence.ApoDefencePoint;

public class ApoDefenceShootArch extends ApoDefenceShoot
{
	private float 		speedX, speedY;
	
	public ApoDefenceShootArch( ApoDefenceTower tower, double x, double y, int width, int height, float speedX, float speedY, ApoDefencePoint damage )
	{
		super( tower, x, y, width, height );
		
		this.speedX		= speedX;
		this.speedY		= speedY;
		this.setValues( this.speedX, this.speedY, damage );
	}
	
	/**
	 * malt das Objekt
	 * @param g
	 */
	public void render( Graphics2D g, int x, int y )
	{
		if ( this.isBVisible() )
		{
			g.setColor( Color.BLACK );
			g.fillRect( (int)this.getX() + x, (int)this.getY() + y, (int)( this.getWidth() ), (int)( this.getHeight() ) );
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
