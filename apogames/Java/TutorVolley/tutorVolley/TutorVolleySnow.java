package tutorVolley;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * diese Klasse dient zur Darstellung des Schnee und beinhaltet alle Angaben,
 * wo der Schnee ist usw
 * @author Dirk Aporius
 *
 */
public class TutorVolleySnow extends TutorVolleyEntity
{
	private double 			veloX, veloY;
	private BufferedImage 	snow;
	
	public TutorVolleySnow( BufferedImage snow, double x, double y, int width, int height )
	{
		super( snow, x, y, width, height );
		
		this.veloX	= Math.random() * 0.05 + 0.05;
		this.veloY	= Math.random() * 0.3 + 0.1;
		
		this.snow	= snow;
	}
	
	/**
	 * wird jeden Zyklus einmal aufgerufen und lässt den Schnee sachte nach unten gleiten ;)
	 */
	protected void think()
	{
		this.setX( this.getX() + this.veloX );
		this.setY( this.getY() + this.veloY );
	}
	
	/**
	 * Methode, um den Schnee zu malen
	 */
	protected void render( Graphics2D g )
	{
		if ( this.snow == null )
		{
			g.fillRoundRect( (int)this.getX(), (int)this.getY(), this.getWidth(), this.getHeight(), 2, 2 );
		} else
		{
			super.render( g );
		}
	}
	
	/**
	 * Methode, um den Schnee zu malen
	 */
	protected void render( Graphics g )
	{
		if ( this.snow == null )
		{
			g.fillRoundRect( (int)this.getX(), (int)this.getY(), this.getWidth(), this.getHeight(), 2, 2 );
		} else
		{
			super.render( g );
		}
	}

}
