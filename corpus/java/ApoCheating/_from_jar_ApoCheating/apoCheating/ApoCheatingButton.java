package apoCheating;

import java.awt.image.BufferedImage;

/**
 * Diese Klasse handelt einen Button
 * @author Dirk Aporius
 *
 */
public class ApoCheatingButton extends ApoCheatingButtonEntity
{
	private String		function;
	
	public ApoCheatingButton( BufferedImage iBackground, int x, int y, int width, int height, String function )
	{
		super( iBackground, x, y, width, height );
		
		this.function	= function;
	}

	/**
	 * gibt die Funktion des Buttons zurück
	 * @return function
	 */
	protected String getFunction()
	{
		return this.function;
	}

	/**
	 * sezt die Funktion des Buttons auf den übergebenen Wert
	 * @param function
	 */
	protected void setFunction(String function)
	{
		this.function = function;
	}

}
