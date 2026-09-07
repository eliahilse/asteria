package apoDefence;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Klasse, die ein BufferedImage lädt
 * @author Dirk Aporius
 *
 */
public class ApoDefenceImage extends Component implements ImageObserver
{
	private static final long serialVersionUID = 1L;
	
	private Component 	C;
	private int			width, height;
	private GraphicsConfiguration gc;
	
	public ApoDefenceImage() 
	{
		this.C					= this;
		this.gc					= GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * gibt die Weite des Bildes zurück
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 * gibt die Höhe des Bildes zurück
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	/**
	 * lädt das Bild aus dem übergbenen String und wartet bis es fertig geladen ist
	 * @param BildString = wo steckt das Bild
	 * @param bLoad
	 * @return gibt das geladenen Bild zurück
	 */
	public BufferedImage getPic( String BildString, boolean bLoad ) 
	{
		BufferedImage i;
		MediaTracker mt;
		mt = new MediaTracker( this.C );// damit alles mit einmal angezeigt wird
		
		i = getImage( BildString, bLoad );

		mt.addImage(i, 0);

		try
		{
			// Wait until all pic are load,
			mt.waitForAll();
		} catch (InterruptedException e) 
		{
			// nothing
		}
		this.width	= i.getWidth( null );
		this.height	= i.getHeight( null );
		return i;
	}
	
	/**
	 * lädt das Bild aus einem übergebenen String
	 * @param BildName = wo steckt das Bild
	 * @param bLoad
	 * @return das Bild
	 */
	private BufferedImage getImage(String BildName, boolean bLoad )
	{
		BufferedImage icon;
		
		BufferedImage result	= null;
		BufferedImage img		= null;
		
		if ( !bLoad ) 
		{
			try
			{
				img = ImageIO.read( ApoDefenceImage.class.getResource( BildName ) );
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			result = this.gc.createCompatibleImage(img.getWidth(),img.getHeight(), Transparency.TRANSLUCENT);
			result.createGraphics().drawImage(img,0,0,null);
			icon	= result;
			
		} else
		{
			img = getBufferedImage( new ImageIcon(BildName).getImage() );
			result = this.gc.createCompatibleImage(img.getWidth(),img.getHeight(), Transparency.TRANSLUCENT);
			result.createGraphics().drawImage(img,0,0,null);
			icon	= result;
		}
		return (icon);
	}	
	
	/**
	 * wandelt ein Image in ein BufferedImage um
	 * @param img Ausgangsimage
	 * @return das BufferedImage
	 */
	public BufferedImage getBufferedImage(Image img)
	{
		return toBufferedImage(img);
	}
	
    /**
     * Hilfsmethode für Umwandlung des Bildes
     * @param image
     * @return ob das Bild einen Alpha-Wert hat
     */
    private boolean hasAlpha(Image image) 
    {
        if (image instanceof BufferedImage) 
        {
            BufferedImage bImage = (BufferedImage) image;
            return bImage.getColorModel().hasAlpha();
        }

        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, 1, 1, false);
        try 
        {
            pixelGrabber.grabPixels();
        } catch (InterruptedException e) 
        {}

        ColorModel colorModel = pixelGrabber.getColorModel();
        return colorModel.hasAlpha();
    }

    /**
     *  Umwandlung eines Image in ein BufferedImage
     * @param image
     * @return das BufferedImage
     */
    private BufferedImage toBufferedImage(Image image) 
    {
        if (image instanceof BufferedImage) 
        {
            return (BufferedImage) image;
        }

        image = new ImageIcon(image).getImage();

        boolean hasAlpha = hasAlpha(image);

        BufferedImage bImage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try 
        {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha)
                transparency = Transparency.BITMASK;

            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bImage = gc.createCompatibleImage(image.getWidth(null), image
                    .getHeight(null), transparency);
        } catch (HeadlessException e) 
        {}

        if (bImage == null) 
        {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha)
                type = BufferedImage.TYPE_INT_ARGB;
            bImage = new BufferedImage(image.getWidth(null), image
                    .getHeight(null), type);
        }

        Graphics g = bImage.createGraphics();

        g.drawImage(image, 0, 0, this);
        g.dispose();
        return bImage;
    }

	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
	{
		return false;
	}

}
