package apoCheating;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ApoCheatingFinish extends ApoCheatingEntity
{
	private boolean		bFinish;
	private int			player;
	private BufferedImage iFinish, iFinishReady;
	
	public ApoCheatingFinish( int x, int y, int width, int height, int player )
	{
		super( Color.WHITE, x, y, width, height, ApoCheatingConstants.FINISH );
		
		this.bFinish		= false;
		this.player			= player;
	}
	
	public ApoCheatingFinish( BufferedImage iDetect, BufferedImage iEntity, int x, int y, int width, int height, int player )
	{
		super( iDetect, iEntity, x, y, width, height, ApoCheatingConstants.FINISH );
		
		this.bFinish		= false;
		this.player			= player;
	}
	
	private BufferedImage getIFinish( Color color )
	{
		BufferedImage iFinish = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( 22, 22, Transparency.TRANSLUCENT );
		
		Graphics2D g = (Graphics2D)iFinish.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Composite clone = g.getComposite();
		
		g.setColor( color );
		g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.45f ) );
		g.fillOval( 0, 0, iFinish.getWidth() - 1, iFinish.getHeight() - 1 );
		
		Stroke cloneStroke = g.getStroke();
		
		g.setColor( Color.BLACK );
		g.setStroke( new BasicStroke( 2 ) );
		g.drawOval( 0, 0, iFinish.getWidth() - 1, iFinish.getHeight() - 1 );
		
		g.setComposite( clone );
		g.setStroke( cloneStroke );
		
		g.dispose();
		
		return iFinish;
	}
	
	protected void init()
	{
		super.init();
		this.bFinish		= false;
		
		if ( this.iFinish == null ) {
			this.iFinish = this.getIFinish( Color.BLACK );
			this.iFinishReady = this.getIFinish( Color.BLUE );
		}
	}
	
	protected boolean isFinish( ApoCheatingPlayer player )
	{
		if ( ( player.getCheated() >= 100 ) && ( player.getPlayer() == this.player ) )
		{
			this.bFinish	= true;
			return (new Rectangle( (int)this.getX(), (int)this.getY(), 1, 1 )).intersects( new Rectangle( (int)player.getX() + player.getWidth()/2 - 3, (int)player.getY() + player.getHeight()/2 - 3, 7, 7 ) );
		}
		else
			return false;
	}
	
	protected boolean isOnFinish( ApoCheatingPlayer player )
	{
		if ( player.getPlayer() == this.player )
		{
			return (new Rectangle( (int)this.getX() - 10, (int)this.getY() - 10, 20, 20 )).intersects( new Rectangle( (int)player.getX() + player.getWidth()/2 - 5, (int)player.getY() + player.getHeight()/2 - 5, 10, 10 ) );
		}
		else
			return false;
	}
	
	protected void render( Graphics2D g )
	{
		if ( this.bFinish )
		{
			g.drawImage( this.iFinishReady, (int)super.getX() - 11, (int)super.getY() - 11, null );
		} else
		{
			g.drawImage( this.iFinish, (int)super.getX() - 11, (int)super.getY() - 11, null );
		}
	}

	protected int getPlayer()
	{
		return this.player;
	}

}
