package tutorVolley;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Klasse, die den Ball repräsentiert
 * @author Dirk Aporius
 */
public class TutorVolleyBall extends TutorVolleyEntity
{
	public static final double		GRAVITATION		= 9.81;
	private final int				MAX_NO_HIT		= 600;
	private final int				MAX_POINT_TIME	= 60;
	
	private double					xSpeed, ySpeed, rotate;
	private boolean					bFirst, bDown, bCollision, bPoint;
	
	private int						time;
	
	private byte					playerOneHit, playerTwoHit;
	
	private int						noHit;
	
	public TutorVolleyBall( BufferedImage iBall, int x, int y, int width, int height )
	{
		super( iBall, x, y, width, height );
		
		this.init();
	}
	
	/**
	 * initialisiert den Startwerte für den Ball
	 */
	protected void init()
	{
		this.rotate			= 0.0D;
		this.xSpeed			= 0.0D;
		this.ySpeed			= 0.0D;
		
		this.bFirst			= false;
		this.bDown			= false;
		this.bCollision		= false;
		this.bPoint			= false;

		this.playerOneHit	= 0;
		this.playerTwoHit	= 0;
		this.noHit			= 0;
		
		this.time			= 0;
	}
	
	/**
	 * diese Methode gibt zurück, ob der Ball schon getroffen wurde oder
	 * noch in der Luft auf den Aufschlag wartet
	 * @return false falls der Ball noch wartet bzw true wenn der Ball schon im Spiel ist
	 */
	public boolean getBFirst()
	{
		return this.bFirst;
	}
	
	/**
	 * diese Methode setzt den boolean Wert ob der Ball getroffen wurde auf den
	 * übergebenen Wert 
	 * @param bFirst
	 */
	protected void setBFirst( boolean bFirst )
	{
		this.bFirst		= bFirst;
	}
	
	/**
	 * diese Methode setzt den boolean Wert ob der Ball auf dem Boden liegt, auf den
	 * übergebenen Wert 
	 * @param bDown
	 */
	protected void setBDown( boolean bDown )
	{
		this.bDown		= bDown;
	}
	
	/**
	 * wird pro Zyklus einmal aufgerufen und überprüft, ob der Ball von einem Spieler
	 * getroffen wurde oder auf dem Boden gelandet ist usw
	 * @param playerOne: Spieler ein
	 * @param playerTwo: Spieler zwei
	 * @param panel: Das Objekt um Methoden aus dem SpielPanel aufzurufen
	 */
	protected void think( TutorVolleyPlayer playerOne, TutorVolleyPlayer playerTwo, TutorVolleyPanelGame panel )
	{
		if ( this.bDown == false )
		{
			if ( this.collisionsCheck( playerOne ) )
			{
				if ( this.playerTwoHit != 0 )
					this.playerTwoHit	= 0;
				if ( !this.bCollision )
					this.playerOneHit++;
				if ( this.playerOneHit <= 3 )
					this.setNewSpeed( playerOne );
				this.noHit	= 0;
			} else if ( this.collisionsCheck( playerTwo ) )
			{
				if ( this.playerOneHit != 0 )
					this.playerOneHit	= 0;
				if ( !this.bCollision )
					this.playerTwoHit++;
				if ( this.playerTwoHit <= 3 )
					this.setNewSpeed( playerTwo );
				this.noHit	= 0;
			} else
			{
				this.bCollision	= false;
				this.noHit		= this.noHit + 1;
			}
			if ( ( this.playerOneHit >= 4 ) || ( this.playerTwoHit >= 4 ) || ( this.noHit >= this.MAX_NO_HIT ) )
				this.setPoint();
			else
				this.setNewXAndY();
		} else
		{
			if ( time > this.MAX_POINT_TIME )
			{
				this.bDown		= false;
				panel.setPoint( this.bPoint );
			} else
			{
				this.time	= this.time + 1;
			}
			this.setNewXAndY();
		}
	}
	
	/**
	 * testet, ob der ball mit dem Spieler kollidiert
	 * @param player: welcher Spieler
	 * @return true falls berührung sonst false
	 */
	private boolean collisionsCheck( TutorVolleyPlayer player )
	{
		TutorVolleyCircle2D	cBall	= new TutorVolleyCircle2D( this.getX(), this.getY(), this.getHeight() / 2, -1, -1, 0, 0  );
		TutorVolleyCircle2D	cPlayer	= new TutorVolleyCircle2D( player.getX() + 4, player.getY(), 11, player.getX(), player.getY(), 30, 40 );
		return cBall.intersects( cPlayer );
	}
	
	/**
	 * wird aufgerufen um den Ball nach einem Aufprall mit einem Spieler eine
	 * neue Geschwindigkeit in x und y Richtung zu geben
	 * @param player
	 */
	private void setNewSpeed( TutorVolleyPlayer player )
	{
		if ( !this.bFirst )
		{
			this.bFirst	= true;
		}
		if ( player.getX() < this.getX() )
		{
			this.xSpeed		= ( this.getX() - player.getX() ) * 0.19 + player.getXSpeed()*2;
			this.setX( this.getX() + 3 );
		} else
		{
			this.xSpeed		= ( this.getX() - player.getX() ) * 0.19 + player.getXSpeed()*2;
			this.setX( this.getX() - 3 );
		}
		if ( player.getY() > this.getY() )
		{
			this.ySpeed	= ( this.getY() - player.getY() ) * 0.25 + player.getYSpeed() * 0.75;
			this.setY( this.getY() - 3 );
		}
		this.bCollision		= true;
	}
	
	/**
	 * wird jeden Zyklus aufgerufen und gibt dem Ball den neuen x und y Wert
	 */
	private void setNewXAndY()
	{
		if ( ( !this.bFirst ) && ( !this.bDown ) )
		{
			return;
		}
		//System.out.println("Check x = "+this.getX()+" y = "+this.getY()+" width = "+this.getWidth()+" height = "+this.getHeight());
		this.rotate		+= 0.6 * this.xSpeed;
		this.setX( this.getX() + this.xSpeed * 1 );
		this.setY( this.getY() + this.ySpeed * 1 );
		this.xSpeed		= this.xSpeed * 0.99;
		this.ySpeed		= this.ySpeed + ( TutorVolleyBall.GRAVITATION * 0.0265 );
		if ( this.getX() < 0 )
		{
			this.setX( 0 );
			this.xSpeed	= -this.xSpeed;
		}
		else if ( this.getX() + this.getWidth() > 400 )
		{
			this.setX( 400 - this.getWidth() );
			this.xSpeed	= -this.xSpeed;
		}
		else if ( (new Rectangle2D.Double( this.getX(), this.getY(), this.getWidth(), this.getHeight() )).intersects( 196, 138, 8, 162 ) )
		{
			if ( ( this.getY() + this.getHeight() >= 138 ) && ( this.getY() + this.getHeight() < 148 ) && ( this.ySpeed > 0 ) )
			{
				this.setY( 138 - this.getHeight() );
				this.ySpeed		= -this.ySpeed*0.7;
				if ( ( this.xSpeed > 0 ) && ( this.xSpeed < 0.1 ) )
						this.xSpeed	= 0.1;
				else if ( ( this.xSpeed < 0 ) && ( this.xSpeed > -0.1 ) )
						this.xSpeed	= -0.1;
			}
			else if ( ( this.getX() + this.getWidth() >= 196 ) && ( this.getX() + this.getWidth() < 208 ) )
			{
				this.xSpeed	= -this.xSpeed;
				this.setX( 196 - this.getWidth() );
			} else if ( ( this.getX() <= 205 ) && ( this.getX() >= 192 ) )
			{
				this.xSpeed	= -this.xSpeed;
				this.setX( 205 );
			}
		}
		if ( this.getY() > 240 )
		{
			this.setY( 240 );
			if ( !this.bDown )
				this.setPoint();
		}
	}
	
	/**
	 * wird aufgerufen wenn der Ball auf den Boden gefallen ist oder 4 mal gespielt wurde
	 */
	private void setPoint()
	{
		if ( this.playerOneHit >= 4 )
			this.bPoint		= false;
		else if ( this.playerTwoHit >= 4 )
			this.bPoint		= true;
		else if ( this.getXMiddle() < 200 )
			this.bPoint		= false;
		else
			this.bPoint		= true;
		this.playerOneHit	= 0;
		this.playerTwoHit	= 0;
		this.noHit			= 0;
		this.ySpeed			= 0.0D;
		this.xSpeed			= 0.0D;
		this.rotate			= 0.0D;
		this.bFirst			= false;
		this.bDown			= true;
		this.time			= 0;
	}
	
	/**
	 * gibt die Geschwindigkeit in x-Richtung zurück
	 * falls diese > 0, bedeutet dass der Ball nach rechts -> fliegt
	 * falls diese < 0, beduetet dass der Ball nach links <- fliegt
	 * @return Geschwindigkeit in x-Richtung
	 */
	public double getXSpeed()
	{
		return this.xSpeed;
	}

	/**
	 * gibt die Geschwindigkeit in y-Richtung zurück
	 * falls diese > 0, bedeutet dass der Ball nach unten fliegt
	 * falls diese < 0, bedeutet dass der Ball nach oben fliegt
	 * @return Geschwindigkeit in y-Richtung
	 */
	public double getYSpeed()
	{
		return this.ySpeed;
	}
	
	/**
	 * malt den Ball
	 */
	protected void render( Graphics2D gfx )
	{
		gfx.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 0, 0, this.getWidth(), this.getHeight(), null );
		gfx.setColor( Color.BLACK );
		gfx.fillRect( (int)(this.getX() + this.getWidth()/2 - 2), 2, 4, 4 );
	}
	
	/**
	 * malt den Ball
	 */
	protected void render( Graphics gfx )
	{
		gfx.drawImage( this.getIBackground(), (int)this.getX(), (int)this.getY(), (int)this.getX() + this.getWidth(), (int)this.getY() + this.getHeight(), 0, 0, this.getWidth(), this.getHeight(), null );
		gfx.setColor( Color.BLACK );
		gfx.fillRect( (int)(this.getX() + this.getWidth()/2 - 2), 2, 4, 4 );
	}

}
