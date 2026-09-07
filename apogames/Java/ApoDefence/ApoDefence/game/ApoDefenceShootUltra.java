package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import apoDefence.ApoDefencePoint;

public class ApoDefenceShootUltra extends ApoDefenceShoot
{
	private float 		speedX, speedY;
	private Color		ultra;
	private BufferedImage iShoot;
	
	public ApoDefenceShootUltra( ApoDefenceTower tower, double x, double y, int width, int height, float speedX, float speedY, ApoDefencePoint damage )
	{
		super( tower, x, y, width, height );
		
		this.ultra		= new Color( 157, 34, 176 );
		this.iShoot		= null;
		
		this.speedX		= speedX;
		this.speedY		= speedY;
		this.setValues( this.speedX, this.speedY, damage );
	}
	
	public ApoDefenceShootUltra( ApoDefenceTower tower, double x, double y, int width, int height, float speedX, float speedY, ApoDefencePoint damage, BufferedImage iShoot )
	{
		super( tower, x, y, width, height );
		
		this.ultra		= new Color( 157, 34, 176 );
		this.iShoot		= iShoot;
		
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
			if ( this.iShoot == null )
			{
				g.setColor( this.ultra );
				g.fillOval( (int)this.getX() + x, (int)this.getY() + y, this.getWidth(), this.getHeight() );
			} else
			{
				g.drawImage( this.iShoot, (int)this.getX() + x - 1, (int)this.getY() + y - 1, null );
			}
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
