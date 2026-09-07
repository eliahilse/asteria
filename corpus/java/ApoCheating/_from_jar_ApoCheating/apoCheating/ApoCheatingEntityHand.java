package apoCheating;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public class ApoCheatingEntityHand extends ApoCheatingEntity
{
	private int				up			= 0;
	private int				left		= 0;
	private boolean			bLeft		= false;
	private boolean			bFrame		= false;
	private double 			frame		= 0;
	private double			frameAdd	= 0;
	private BufferedImage	iPaper;
	
	public ApoCheatingEntityHand( BufferedImage iPaper, Color color, double x, double y, int width, int height, int value )
	{
		super( color, x, y, width, height, value );
		
		this.iPaper		= iPaper;
		this.init();
	}
	
	public ApoCheatingEntityHand( BufferedImage iPaper, Color color, double x, double y, int width, int height, boolean bDetect, int value )
	{
		super( color, x, y, width, height, bDetect, value );

		this.iPaper		= iPaper;
		this.init();
	}
	
	public ApoCheatingEntityHand( BufferedImage iPaper, BufferedImage iDetect, BufferedImage iEntity, double x, double y, int width, int height, boolean bDetect, int value )
	{
		super( iDetect, iEntity, x, y, width, height, bDetect, value );

		this.iPaper		= iPaper;
		this.init();
	}
	
	protected void init()
	{
		super.init();
		
		int random	= (int)(Math.random()*100);
		if ( random < 50 )
			this.bLeft		= true;
		else
			this.bLeft		= false;
		this.bFrame		= false;
		
		this.frame		= Math.random()*8;
		this.frameAdd	= Math.random()/5 + 0.1;
	}
	
	protected BufferedImage getIPaper()
	{
		return iPaper;
	}
	
	protected void setWayHand( int up, int left )
	{
		this.up		= up;
		this.left	= left;
	}
	
	protected void addFrame()
	{
		if ( !this.bFrame )
			this.frame += this.frameAdd;
		else
			this.frame -= this.frameAdd;
		if ( ( this.frame >= 8 ) || ( this.frame <= 0 ) )
			this.bFrame	= !this.bFrame;
	}
	
	protected void render( Graphics2D g )
	{
		Stroke clone = g.getStroke();
		
		g.setStroke( new BasicStroke( 2 ) );
		g.setColor( Color.BLACK );
		if ( this.up == ApoCheatingConstants.WRITE_UP )
		{
			if ( this.bLeft )
			{
				int x	= (int)this.getX() + this.getWidth()/2 - this.iPaper.getWidth()/4;
				int y	= (int)this.getY() - 4 - this.iPaper.getHeight();
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 0, 0, this.iPaper.getWidth()/2, this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX(), (int)this.getY() + this.getHeight()/2,  (int)(this.getX() + this.frame + 4), (int)this.getY() + this.getHeight()/2 - 22 );
				g.fillRect( (int)(this.getX() + this.frame) + 3, (int)this.getY() + this.getHeight()/2 - 22, 4, 4 );
			} else
			{
				int x	= (int)this.getX() + this.getWidth()/2 - this.iPaper.getWidth()/4;
				int y	= (int)this.getY() - 4 - this.iPaper.getHeight();
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 0, 0, this.iPaper.getWidth()/2, this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight()/2,  (int)(this.getX() - this.frame) - 6 + this.getWidth(), (int)this.getY() + this.getHeight()/2 - 22 );
				g.fillRect( (int)(this.getX() - this.frame) - 7 + this.getWidth(), (int)this.getY() + this.getHeight()/2 - 22, 4, 4 );
			}
		} else if ( this.up == ApoCheatingConstants.WRITE_DOWN )
		{
			if ( this.bLeft )
			{
				int x	= (int)this.getX() + this.getWidth()/2 - this.iPaper.getWidth()/4;
				int y	= (int)this.getY() + this.getHeight() + 7;
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 0, 0, 0 + this.iPaper.getWidth()/2, this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX(), (int)this.getY() + this.getHeight()/2,  (int)(this.getX() + this.frame + 4), (int)this.getY() + this.getHeight()/2 + 22 );
				g.fillRect( (int)(this.getX() + this.frame) + 3, (int)this.getY() + this.getHeight()/2 + 22, 4, 4 );
			} else
			{
				int x	= (int)this.getX() + this.getWidth()/2 - this.iPaper.getWidth()/4;
				int y	= (int)this.getY() + this.getHeight() + 7;
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 0, 0, 0 + this.iPaper.getWidth()/2, this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight()/2,  (int)(this.getX() - this.frame) - 6 + this.getWidth(), (int)this.getY() + this.getHeight()/2 + 22 );
				g.fillRect( (int)(this.getX() - this.frame) - 8 + this.getWidth(), (int)this.getY() + this.getHeight()/2 + 22, 4, 4 );
			}
		} else if ( this.left == ApoCheatingConstants.WRITE_LEFT )
		{
			if ( this.bLeft )
			{
				int x	= (int)this.getX() - 6 - this.iPaper.getWidth()/2;
				int y	= (int)this.getY() + 3;
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 1*this.iPaper.getWidth()/2, 0, (2 * this.iPaper.getWidth()/2), this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX() + this.getWidth()/2, (int)this.getY(),  (int)(this.getX() + this.getWidth()/2 - 22), (int)(this.getY() + this.frame) + 6 );
				g.fillRect( (int)(this.getX() + this.getWidth()/2 - 22), (int)(this.getY() + this.frame) + 5, 4, 4 );
			} else
			{
				int x	= (int)this.getX() - 6 - this.iPaper.getWidth()/2;
				int y	= (int)this.getY() + 4;
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 1*this.iPaper.getWidth()/2, 0, (2 * this.iPaper.getWidth()/2), this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX() + this.getWidth()/2, (int)this.getY() + this.getHeight(),  (int)(this.getX() + this.getWidth()/2 - 22), (int)(this.getY() - this.frame) - 6 + this.getHeight() );
				g.fillRect( (int)(this.getX() + this.getWidth()/2 - 22), (int)(this.getY() - this.frame) - 5 + this.getHeight(), 4, 4 );
			}
		} else if ( this.left == ApoCheatingConstants.WRITE_RIGHT )
		{
			if ( this.bLeft )
			{
				int x	= (int)this.getX() + this.getWidth() + 6;
				int y	= (int)this.getY() + 3;
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 1*this.iPaper.getWidth()/2, 0, (2 * this.iPaper.getWidth()/2), this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX() + this.getWidth()/2, (int)this.getY(),  (int)(this.getX() + this.getWidth()/2 + 22), (int)(this.getY() + this.frame) + 6 );
				g.fillRect( (int)(this.getX() + this.getWidth()/2 + 22), (int)(this.getY() + this.frame) + 5, 4, 4 );
			} else
			{
				int x	= (int)this.getX() + this.getWidth() + 6;
				int y	= (int)this.getY() + 4;
				g.drawImage( this.iPaper, x, y, x + this.iPaper.getWidth()/2, y + this.iPaper.getHeight(), 1*this.iPaper.getWidth()/2, 0, (2 * this.iPaper.getWidth()/2), this.iPaper.getHeight(), null );
				g.drawLine( (int)this.getX() + this.getWidth()/2, (int)this.getY() + this.getHeight(),  (int)(this.getX() + this.getWidth()/2 + 22), (int)(this.getY() - this.frame) - 6 + this.getHeight() );
				g.fillRect( (int)(this.getX() + this.getWidth()/2 + 22), (int)(this.getY() - this.frame) - 7 + this.getHeight(), 4, 4 );
			}
		}
		g.setStroke( clone );
		super.render( g );
	}

}
