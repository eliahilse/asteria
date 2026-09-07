package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import apoDefence.ApoDefencePoint;

public class ApoDefenceShootLight extends ApoDefenceShoot
{
	private float 		speedX, speedY;
	private Color		light;
	private int			startX, startY;
	
	public ApoDefenceShootLight( ApoDefenceTower tower, double x, double y, int width, int height, float speedX, float speedY, ApoDefencePoint damage )
	{
		super( tower, x, y, width, height );
		
		this.light		= new Color( 255, 255, 0 );
		
		this.speedX		= speedX;
		this.speedY		= speedY;
		this.setValues( this.speedX, this.speedY, damage );
		this.setBLight( true );
		//this.setWidth( (int)x );
		//this.setHeight( (int)y );
		
		this.startX		= (int)x;
		this.startY		= (int)y;
		
		this.setWidth( 5 );
		this.setHeight( 5 );
	}
	
	public void think( ApoDefenceGame game )
	{
		if ( Math.abs( this.startX - this.getX() ) + Math.abs( this.startY - this.getY() ) > this.getTower().getRange() )
			this.setBVisible( false );
		super.think( game );
	}
	
	/**
	 * malt das Objekt
	 * @param g
	 */
	public void render( Graphics2D g, int x, int y )
	{
		if ( this.isBVisible() )
		{
			g.setColor( this.light );
			//g.fillOval( (int)this.getX() + x, (int)this.getY() + y, 4, 4 );
			g.drawLine( (int)this.getX() + x, (int)this.getY() + y, this.startX + x, this.startY + y );
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
