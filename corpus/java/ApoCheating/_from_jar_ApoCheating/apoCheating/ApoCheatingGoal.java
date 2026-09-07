package apoCheating;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ApoCheatingGoal extends ApoCheatingEntityHand
{
	//private float	currentRed, currentGreen, currentBlue;
	private double	plus, oldPlus;
	
	public ApoCheatingGoal( BufferedImage iPaper, int x, int y, int width, int height )
	{
		super( iPaper, Color.GREEN, x, y, width, height, ApoCheatingConstants.GOAL );

		this.oldPlus	= 0.15D;
		
		this.init( this.oldPlus );
	}
	
	public ApoCheatingGoal( BufferedImage iPaper, int x, int y, int width, int height, boolean bDetect )
	{
		super( iPaper, Color.GREEN, x, y, width, height, bDetect, ApoCheatingConstants.GOAL );
		
		this.oldPlus	= 0.3D;
		
		this.init( this.oldPlus );
	}
	
	public ApoCheatingGoal( BufferedImage iPaper, int x, int y, int width, int height, boolean bDetect, double plus )
	{
		super( iPaper, Color.GREEN, x, y, width, height, bDetect, ApoCheatingConstants.GOAL );
		
		this.oldPlus	= plus;
		
		this.init( this.oldPlus );
	}
	
	public ApoCheatingGoal( BufferedImage iPaper, BufferedImage iDetect, BufferedImage iEntity, int x, int y, int width, int height, boolean bDetect, double plus )
	{
		super( iPaper, iDetect, iEntity, x, y, width, height, bDetect, ApoCheatingConstants.GOAL );
		
		this.oldPlus	= plus;
		
		this.init( this.oldPlus );
	}
	
	protected void init()
	{
		super.init();
		this.init( this.oldPlus );
	}
	
	private void init( double plus )
	{
		//this.currentRed			= Color.RED.getRed();
		//this.currentGreen		= Color.RED.getGreen();
		//this.currentBlue		= Color.RED.getBlue();
		
		this.plus				= plus;
	}
	
	public void action( ApoCheatingPlayer player )
	{
		if ( super.isIn( player ) )
		{
			if ( !super.isBDetect() )
				super.setBDetect( true );
			if ( player.getCheated() < 100 )
			{
				//System.out.println("player = "+player.getPlayer()+" cheat = "+player.getCheated());
				player.setCheat( player.getCheated() + this.plus );
				/*double	value		= (255.0D*this.plus)/100.0D;
				this.currentRed		= player.getPercentColor().getRed();
				this.currentRed		-= value;
				if ( this.currentRed < 0 )
					this.currentRed	= 0;
				this.currentGreen	= player.getPercentColor().getGreen();
				this.currentGreen	+= value;
				if ( this.currentGreen > 255 )
					this.currentGreen	= 255;
				System.out.println("value = "+value+" red = "+this.currentRed+" green = "+this.currentGreen);
				player.setPercentColor( new Color( this.currentRed/255, this.currentGreen/255, this.currentBlue/255 ) );*/
			}
		}
	}

	protected double getOldPlus()
	{
		return this.oldPlus;
	}

}
