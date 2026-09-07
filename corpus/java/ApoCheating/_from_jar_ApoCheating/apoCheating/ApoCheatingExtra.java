package apoCheating;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ApoCheatingExtra extends ApoCheatingEntityHand
{
	public ApoCheatingExtra( BufferedImage iPaper, int x, int y, int width, int height )
	{
		super( iPaper, Color.YELLOW, x, y, width, height, ApoCheatingConstants.EXTRA );
	}
	
	public ApoCheatingExtra( BufferedImage iPaper, int x, int y, int width, int height, boolean bDetect )
	{
		super( iPaper, Color.YELLOW, x, y, width, height, bDetect, ApoCheatingConstants.EXTRA );
	}
	
	public ApoCheatingExtra( BufferedImage iPaper, BufferedImage iDetect, BufferedImage iEntity, int x, int y, int width, int height, boolean bDetect )
	{
		super( iPaper, iDetect, iEntity, x, y, width, height, bDetect, ApoCheatingConstants.EXTRA );
	}

}
