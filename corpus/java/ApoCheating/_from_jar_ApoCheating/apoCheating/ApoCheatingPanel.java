package apoCheating;

import javax.swing.JPanel;

public class ApoCheatingPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private ApoCheatingGamePanel	apoCheatingGamePanel;
	private ApoCheatingHudPanel		apoCheatingHudPanel;
	
	public ApoCheatingPanel()
	{
		super();
		
		this.setLayout( null );
		
		this.apoCheatingGamePanel	= new ApoCheatingGamePanel();
		this.apoCheatingGamePanel.setSize( 480, 480 );
		this.apoCheatingGamePanel.setLocation( 0, 0 );
		this.apoCheatingGamePanel.init();
		
		this.add( this.apoCheatingGamePanel );
		
		this.apoCheatingHudPanel	= new ApoCheatingHudPanel();
		this.apoCheatingHudPanel.setSize( 160, 480 );
		this.apoCheatingHudPanel.setLocation( 480, 0 );
		this.apoCheatingHudPanel.init();
		
		this.add( this.apoCheatingHudPanel );
		
		this.apoCheatingGamePanel.setApoCheatingHudPanel( this.apoCheatingHudPanel );
		this.apoCheatingHudPanel.setApoCheatingGamePanel( this.apoCheatingGamePanel );
	}

}
