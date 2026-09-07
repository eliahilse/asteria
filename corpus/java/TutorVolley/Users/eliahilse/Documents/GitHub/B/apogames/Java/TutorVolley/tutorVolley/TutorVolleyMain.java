package tutorVolley;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Startklasse, die das eigentliche Frame handelt
 * @author Dirk Aporius
 *
 */
public class TutorVolleyMain extends JFrame
{
	private static final long serialVersionUID = 1L;

	private TutorVolleyPanelGame	tutorVolleyPanelGame;
	private TutorVolleyPanelOptions	tutorVolleyPanelOptions;
	
	public static TutorVolleyMain	tutorVolleyMain;
	
	private JPanel					contentPanel;
	
	public TutorVolleyMain()
	{
		super();

		tutorVolleyMain	= this;
		
		this.setTitle( "=== TutorVolley ===" );
		
		this.setLayout( null );
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		TutorVolleyImage tutorVolleyImage	= new TutorVolleyImage();
		BufferedImage	iBackground			= tutorVolleyImage.setPicsMain( "/images/background_christmas.jpg", false );
		
		this.tutorVolleyPanelOptions		= new TutorVolleyPanelOptions( this, iBackground );
		this.tutorVolleyPanelOptions.setLocation( 0, 0 );
		this.tutorVolleyPanelOptions.setSize( 400, 300 );

		this.tutorVolleyPanelGame			= new TutorVolleyPanelGame( this, iBackground );
		this.tutorVolleyPanelGame.setLocation( 0, 0 );
		this.tutorVolleyPanelGame.setSize( 400, 300 );
		
		this.tutorVolleyPanelOptions.setApoVolleyPanelGame( this.tutorVolleyPanelGame );
		this.tutorVolleyPanelOptions.setAI();
		
		this.contentPanel		= this.tutorVolleyPanelOptions;
		
		this.add( this.contentPanel );
		
		this.setUndecorated( false );
		this.setResizable( false );
		
		this.setIconImage( tutorVolleyImage.setPicsMain( "/images/ball.png", false ) );
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension( 400 + 6, 300 + 32);
	}
	
	/**
	 *	entfernt das jetztige JPanel und sagt, dass das aktuelle JPanel nun das Spielfeld ist
	 */
	public void setGame()
	{
		super.remove(this.contentPanel);
		
		this.contentPanel	= this.tutorVolleyPanelGame;
		super.add(this.contentPanel);
		super.validate();
		this.tutorVolleyPanelGame.init();
		this.contentPanel.repaint();	
	}
	
	/**
	 *	entfernt das jetztige JPanel und sagt, dass das aktuelle JPanel nun die Optionen darstellen
	 */
	public void setOptions()
	{
		this.tutorVolleyPanelGame.stop();
		super.remove(this.contentPanel);
		this.contentPanel	= this.tutorVolleyPanelOptions;
		super.add(this.contentPanel);
		super.validate();
		this.contentPanel.repaint();
	}
	
	public static void main( String[] args )
	{
		new TutorVolleyMain();
	}

}
