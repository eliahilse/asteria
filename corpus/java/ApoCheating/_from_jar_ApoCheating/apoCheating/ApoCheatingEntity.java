package apoCheating;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

/**
 * Klasse von der der Techer, Statisten, Spicker und Player erben und einige
 * grundlegene Sachen zur Verfügung stellt 
 * @author Dirk Aporius
 *
 */
public class ApoCheatingEntity
{
	private double 			x, y, oldX, oldY;
	private int				width, height, value;
	private Color			color;
	private boolean			bDetect, bOldDetect;
	private BufferedImage	iDetect, iEntity;
	
	public ApoCheatingEntity( Color color, double x, double y, int width, int height, int value )
	{
		super();
		
		this.color				= color;
		this.oldX				= x;
		this.oldY				= y;
		this.width				= width;
		this.height				= height;
		this.bOldDetect			= true;
		this.value				= value;
		
		this.init();
	}
	
	public ApoCheatingEntity( BufferedImage iDetect, BufferedImage iEntity, double x, double y, int width, int height, int value )
	{
		super();
		
		this.iDetect			= iDetect;
		this.iEntity			= iEntity;
		this.oldX				= x;
		this.oldY				= y;
		this.width				= width;
		this.height				= height;
		this.bOldDetect			= true;
		this.value				= value;
		
		this.init();
	}
	
	public ApoCheatingEntity( Color color, double x, double y, int width, int height, boolean bDetect, int value )
	{
		super();
		
		this.color				= color;
		this.oldX				= x;
		this.oldY				= y;
		this.width				= width;
		this.height				= height;
		this.bOldDetect			= bDetect;
		this.value				= value;
		
		this.init();
	}
	
	public ApoCheatingEntity( BufferedImage iDetect, BufferedImage iEntity, double x, double y, int width, int height, boolean bDetect, int value )
	{
		super();
		
		this.iDetect			= iDetect;
		this.iEntity			= iEntity;
		this.oldX				= x;
		this.oldY				= y;
		this.width				= width;
		this.height				= height;
		this.bOldDetect			= bDetect;
		this.value				= value;
		
		this.init();
	}

	protected void init()
	{
		this.x					= oldX;
		this.y					= oldY;
		this.bDetect			= this.bOldDetect;
	}
	
	protected void setBOldDetect(boolean bOldDetect)
	{
		this.bOldDetect = bOldDetect;
		this.bDetect	= this.bOldDetect;
	}

	/**
	 * gibt die Weite des Objektes zurück
	 * @return Weite des Objektes
	 */
	protected int getWidth()
	{
		return this.width;
	}

	/**
	 * setzt die Weite des Objektes auf den übergebenen Wert
	 * @param width
	 */
	protected void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * gibt die Höhe des Objektes zurück
	 * @return Höhe des Objektes
	 */
	protected int getHeight()
	{
		return this.height;
	}

	/**
	 * setzt die Höhe des Objektes auf den übergebenen Wert
	 * @param height
	 */
	protected void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * gibt den x-Wert des Objektes zurück
	 * @return x-Wert des Objektes
	 */
	public double getX()
	{
		return this.x;
	}

	/**
	 * setzt den X-Wert auf den übergebenen Wert
	 * @param x
	 */
	protected void setX(double x)
	{
		this.x = x;
	}

	/**
	 * gibt den y-Wert des Objektes zurück
	 * @return y-Wert des Objektes
	 */
	public double getY()
	{
		return this.y;
	}

	/**
	 * setzt den y-Wert des Objektes auf den Übergebenen
	 * @param y
	 */
	protected void setY(double y)
	{
		this.y = y;
	}
	
	/**
	 * gibt zurück, ob die Entity ein Ziel zum Abschreiben ist
	 * @return TRUE falls die Entity ein Ziel zum Abschreiben ist, sonst FALSE
	 */
	public boolean isGoal()
	{
		if ( ( this.value == ApoCheatingConstants.GOAL ) && ( this.bDetect ) )
			return true;
		else
			return false;
	}
	
	/**
	 * gibt zurück, ob die Entity ein Extra ist, der nur rumsitzt und von
	 * denen man nicht abschreiben kann
	 * @return TRUE falls die Entity ein Extra ist, sonst FALSE
	 */
	public boolean isExtra()
	{
		if ( ( this.value == ApoCheatingConstants.EXTRA ) && ( this.bDetect ) )
			return true;
		else
			return false;
	}
	
	/**
	 * gibt zurück, ob die Entity ein Lehrer ist
	 * @return TRUE falls die Entity ein Lehrer ist, sonst FALSE
	 */
	public boolean isTeacher()
	{
		if ( ( this.value == ApoCheatingConstants.GOAL ) && ( this.bDetect ) )
			return true;
		else
			return false;
	}
	
	/**
	 * gibt zurück, ob die Entity ein Endziel ist auf das man zurückkehren muss,
	 * wenn man 100 % abgeschrieben hat
	 * @return TRUE falls die Entity ein Endziel ist, sonst FALSE
	 */
	public boolean isFinish()
	{
		if ( ( this.value == ApoCheatingConstants.FINISH ) && ( this.bDetect ) )
			return true;
		else
			return false;
	}
	
	/**
	 * gibt zurück, ob die Entity schon entdeckt wurd und man somit weiß,
	 * was sie darstellen
	 * @return TRUE falls die Entity schon entdeckt wurde
	 */
	public boolean isBDetect()
	{
		return this.bDetect;
	}

	protected void setBDetect(boolean bDetect)
	{
		this.bDetect = bDetect;
	}
	
	protected boolean isIn( ApoCheatingPlayer player )
	{
		return (new Rectangle( (int)this.getX() - 1, (int)this.getY() - 1, this.getWidth() + 2, this.getHeight() + 2 )).intersects( new Rectangle( (int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight() ) ); 
	}
	
	protected boolean isHitting( ApoCheatingEntity entity, int x, int y )
	{
		return (new Rectangle( (int)this.getX() + 4, (int)this.getY() + 4, this.getWidth() - 8, this.getHeight() - 8 )).intersects( new Rectangle( x + 4, y + 4, entity.getWidth() - 8, entity.getHeight() - 8 ) );
	}
	
	protected void action( ApoCheatingPlayer player )
	{
		if ( this.isIn( player ) )
		{
			if ( !this.isBDetect() )
				this.setBDetect( true );
		}
	}
	
	/**
	 * malt das Objekt
	 * @param g
	 */
	protected void render( Graphics2D g )
	{
		if ( this.iEntity == null )
		{
			if ( this.bDetect )
				g.setColor( this.color );
			else
				g.setColor( Color.LIGHT_GRAY );
			g.fillOval( (int)this.x, (int)this.y, this.width+1, this.height+1 );
			g.setColor( Color.BLACK );
		
			Stroke clone = g.getStroke();
		
			g.setStroke( new BasicStroke( 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER ) );
			g.drawOval( (int)this.x - 1, (int)this.y - 1, this.width + 2, this.height + 2 );
		
			g.setStroke( clone );
		} else
		{
			if ( this.bDetect )
				g.drawImage( this.iEntity, (int)this.x, (int)this.y, null );
			else
				g.drawImage( this.iDetect, (int)this.x, (int)this.y, null );
		}
	}

}
