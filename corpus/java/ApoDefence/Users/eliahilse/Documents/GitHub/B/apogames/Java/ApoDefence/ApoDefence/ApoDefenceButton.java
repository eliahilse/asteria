package apoDefence;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Diese Klasse handelt einen Button
 * @author Dirk Aporius
 *
 */
public class ApoDefenceButton extends ApoDefenceEntity
{
	private String			function;
	private boolean 		bOver, bPressed, bVisible;
	private BufferedImage	oldIBackground, oldIDetails, iDetails;
	private Font			fontDetail;
	
	public ApoDefenceButton( BufferedImage iBackground, int x, int y, int width, int height, String function )
	{
		super( iBackground, x, y, width, height );
		
		this.oldIBackground	= iBackground;
		
		this.function	= function;
		this.bOver		= false;
		this.bPressed	= false;
		this.bVisible	= true;
	}
	
	public ApoDefenceButton( BufferedImage iBackground, int x, int y, int width, int height, String function, int price )
	{
		super( null, x, y, width, height );
	
		this.fontDetail		= new Font( "Dialog", Font.BOLD, 15 );
		
		this.oldIBackground	= iBackground;
		
		if ( price > 0 )
		{
			this.setPrice( price );
		}
		
		this.function	= function;
		this.bOver		= false;
		this.bPressed	= false;
		this.bVisible	= true;
	}
	
	public ApoDefenceButton( BufferedImage iBackground, BufferedImage iDetails, int x, int y, int width, int height, String function, int price )
	{
		super( iBackground, x, y, width, height );
		
		this.fontDetail		= new Font( "Dialog", Font.BOLD, 15 );
		
		this.oldIBackground	= iBackground;
		
		if ( price > 0 )
		{
			this.setPrice( price );
		}
		
		this.oldIDetails	= iDetails;
		this.function		= function;
		this.bOver			= false;
		this.bPressed		= false;
		this.bVisible		= true;
	}
	
	/**
	 * setzt den Preis für den Button auf den übergebenen Wert
	 * und erstellt ein neues Bild dafür
	 * @param price
	 */
	public void setPrice( int price )
	{
		if ( this.oldIBackground == null )
			return;
		BufferedImage iNew = new BufferedImage( this.oldIBackground.getWidth(), this.oldIBackground.getHeight(), BufferedImage.TYPE_INT_RGB );
		Graphics g	= iNew.getGraphics();
		g.drawImage( this.oldIBackground, 0, 0, null );
		g.setColor( Color.YELLOW );
		g.setFont( this.fontDetail );
		int w	= g.getFontMetrics().stringWidth( "" + price );
		g.drawString( "" + price, iNew.getWidth()/3 - 4 - w, iNew.getHeight() / 2 + 7 );
		g.drawString( "" + price, 2 * iNew.getWidth()/3 - 4 - w, iNew.getHeight() / 2 + 7 );
		g.drawString( "" + price, 3 * iNew.getWidth()/3 - 4 - w, iNew.getHeight() / 2 + 7 );
		g.dispose();
		this.setIBackground( iNew );
	}

	/**
	 * erstellt ein Image aus den übergebenen DetailStrings
	 * @param sDetails
	 */
	public void setDetails( String[] sDetails )
	{
		if ( this.oldIDetails == null )
			return;
		this.iDetails		= new BufferedImage( this.oldIDetails.getWidth(), this.oldIDetails.getHeight(), BufferedImage.BITMASK );
		Graphics g			= this.iDetails.getGraphics();
		g.drawImage( this.oldIDetails, 0, 0, null );
		g.setFont( this.fontDetail );
		for ( int i = 0; i < sDetails.length; i++ )
		{
			int w	= this.iDetails.getWidth() - 10;;
			if ( i != 1 )
				g.setColor( Color.WHITE );
			else
				g.setColor( Color.YELLOW );
			if ( i < 2 )
				w	= g.getFontMetrics().stringWidth( sDetails[i] );
			g.drawString( sDetails[i], this.iDetails.getWidth() / 2 - w / 2, 20 + i * 20 );
		}
		g.dispose();
	}
	
	/**
	 * gibt zurück, ob die Maus über dem Button ist oder nicht
	 * @return TRUE, falls Maus drüber, sonst FALSE
	 */
	public boolean isBOver()
	{
		return this.bOver;
	}

	/**
	 * setzt den boolean-Wert für bOver auf den übergebenen Wert
	 * @param bOver
	 */
	public void setBOver(boolean bOver)
	{
		this.bOver = bOver;
	}

	/**
	 * gibt zurück, ob eine Maustaste über dem Button gedrückt ist oder nicht
	 * @return TRUE, falls Maustaste gedrückt, sonst FALSE
	 */
	public boolean isBPressed()
	{
		return this.bPressed;
	}

	/**
	 * setzt den boolean-Wert für bPressed auf den übergebenen Wert 
	 * @param bPressed
	 */
	public void setBPressed(boolean bPressed)
	{
		this.bPressed = bPressed;
	}

	/**
	 * gibt an, ob der Button sichtbar ist oder nicht
	 * @return TRUE, falls sichtbar, sonst FALSE
	 */
	public boolean isBVisible()
	{
		return this.bVisible;
	}

	/**
	 * setzt die Sichtbarkeit des Buttons auf den übergebenen Wert
	 * @param bVisible
	 */
	public void setBVisible(boolean bVisible)
	{
		this.bVisible = bVisible;
	}

	/**
	 * gibt die Funktion des Buttons zurück
	 * @return function
	 */
	public String getFunction()
	{
		return this.function;
	}

	/**
	 * sezt die Funktion des Buttons auf den übergebenen Wert
	 * @param function
	 */
	public void setFunction(String function)
	{
		this.function = function;
	}
	
	/**
	 * was passiert, wenn die Maus im Spielfeld bewegt wurde
	 * @param x: x-Koordinate der Maus
	 * @param y: y-Koordinate der Maus
	 * @return TRUE, falls Maus drüber, sonst FALSE
	 */
	public boolean getMove( int x, int y )
	{
		if ( ( this.bOver == false ) && ( getIn( x, y ) == true ) && ( this.bVisible == true ) )
		{
			this.bOver		= true;
			return true;
		} else if ( ( this.bOver == true ) && ( getIn( x, y ) == false ) )
		{
			this.bOver		= false;
			this.bPressed	= false;
			return true;
		}
		return false;
	}
	
	/**
	 * was passiert, wenn eine Maustaste im Spielfeld gedrückt wurde
	 * @param x: x-Koordinate der Maus
	 * @param y: y-Koordinate der Maus
	 * @return TRUE, falls über Button Maus gedrückt, sonst FALSE
	 */
	public boolean getPressed( int x, int y )
	{
		if ( ( this.bOver == true ) && ( getIn( x, y ) == true ) && ( this.bVisible == true ) )
		{
			this.bPressed	= true;
			return true;
		}
		return false;
	}
	
	/**
	 * was passiert, wenn eine Maustaste im Spielfeld losgelassen wurde
	 * @param x: x-Koordinate der Maus
	 * @param y: y-Koordinate der Maus
	 * @return TRUE, wenn die Maustaste losgelassen wurde und der Spieler auch diesen Button gedrückt hatte, sonst FALSE
	 */
	public boolean getReleased( int x, int y )
	{
		if ( ( this.bPressed == true ) && ( getIn( x, y ) == true ) && ( this.bVisible == true ) )
		{
			this.bPressed	= false;
			return true;
		}
		return false;
	}
	
	/**
	 * überprüft, ob die übergebenen Werte auf dem Button liegen
	 * @param x: x-Koordinate der Maus
	 * @param y: y-Koordinate der Maus
	 * @return: TRUE, falls drin, sonst FALSE
	 */
	public boolean getIn( int x, int y )
	{
		if ( 	( this.getX() < x ) && ( this.getX() + this.getWidth() >= x ) &&
				( this.getY() < y ) && ( this.getY() + this.getHeight() >= y ) )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * malt den Button hin
	 */
	public void render( Graphics g )
	{
		if ( this.bVisible == true )
		{
			if ( this.bPressed == true ) 
				g.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 2*this.getWidth(), 0, 3*this.getWidth(), this.getHeight(), null);
			else if ( this.bOver == true )
				g.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 1*this.getWidth(), 0, 2*this.getWidth(), this.getHeight(), null);
			else
				g.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 0*this.getWidth(), 0, 1*this.getWidth(), this.getHeight(), null);
		}
	}
	
	/**
	 * malt den Button hin
	 */
	public void render( Graphics2D g )
	{
		if ( this.bVisible == true )
		{
			if ( this.bPressed == true ) 
				g.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 2*this.getWidth(), 0, 3*this.getWidth(), this.getHeight(), null);
			else if ( this.bOver == true )
				g.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 1*this.getWidth(), 0, 2*this.getWidth(), this.getHeight(), null);
			else
				g.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 0*this.getWidth(), 0, 1*this.getWidth(), this.getHeight(), null);
			
			if ( ( this.iDetails != null ) && ( ( this.bPressed ) || ( this.bOver ) ) )
			{
				g.drawImage( this.iDetails, 490, 180, null );
			}
		}
	}

}
