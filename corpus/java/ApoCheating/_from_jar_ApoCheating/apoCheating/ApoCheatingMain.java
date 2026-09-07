package apoCheating;

import java.awt.Dimension;

import javax.swing.JFrame;

public class ApoCheatingMain extends JFrame
{
	private static final long serialVersionUID = 1L;

	private ApoCheatingPanel		apoCheatingPanel;
	public static ApoCheatingMain	apoCheatingMain;
	
	public ApoCheatingMain()
	{
		super();
		
		System.setProperty("sun.java2d.ddscale","true");
		
		apoCheatingMain		= this;
		
		this.setTitle( "=== ApoCheating ===" );
		
		this.setLayout( null );
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.apoCheatingPanel	= new ApoCheatingPanel();
		this.apoCheatingPanel.setSize( 640, 480 );
		this.apoCheatingPanel.setLocation( 0, 0 );
		
		this.add( this.apoCheatingPanel );
		
		this.setUndecorated( false );
		this.setResizable( false );
		
		this.setIconImage( new ApoCheatingImage().setPicsMain( "/images/f.png", false ) );
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public Dimension getPreferredSize()
	{
		return new Dimension( 640 + 6, 480 + 32);
	}
	
	public static void main(String[] args)
	{
		new ApoCheatingMain();
	}

}
