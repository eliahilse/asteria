package apoCheating;

import java.util.TimerTask;

public class ApoCheatingTimer extends TimerTask
{
	private boolean					bRunning;
	private ApoCheatingGamePanel 	apoCheatingGamePanel;
	
	public ApoCheatingTimer( ApoCheatingGamePanel apoCheatingGamePanel )
	{
		super();
		
		this.bRunning				= false;
		this.apoCheatingGamePanel	= apoCheatingGamePanel;
	}

	@Override
	public void run()
	{
		if ( bRunning )
		{
			this.apoCheatingGamePanel.think();
			this.apoCheatingGamePanel.repaint();
		}	
	}

	protected boolean isBRunning()
	{
		return this.bRunning;
	}

	protected void setBRunning( boolean bRunning )
	{
		this.bRunning = bRunning;
	}

}
