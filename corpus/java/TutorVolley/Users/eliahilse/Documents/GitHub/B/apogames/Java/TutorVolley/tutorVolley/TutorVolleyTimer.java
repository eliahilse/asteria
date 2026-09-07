package tutorVolley;

import java.util.TimerTask;

/**
 * Klasse, die sich um den Timer kümmert und somit dass es flüssig alles in einer
 * bestimmten Reihenfolge abläuft
 * @author Dirk Aporius
 *
 */
public class TutorVolleyTimer extends TimerTask
{
	private TutorVolleyPanelGame	tutorVolleyPanelGame;
	private boolean					bRunning;
	
	public TutorVolleyTimer( TutorVolleyPanelGame tutorVolleyPanelGame )
	{
		super();
		
		this.tutorVolleyPanelGame	= tutorVolleyPanelGame;
		
		this.bRunning				= false;
	}

	/**
	 * gibt zurück, ob es gerade gewollt ist den Timer zu nutzen
	 * @return TRUE, falls Spiel grad läuft sonst FALSE
	 */
	protected boolean isBRunning()
	{
		return this.bRunning;
	}

	protected void setBRunning( boolean bRunning )
	{
		this.bRunning = bRunning;
	}
	
	@Override
	public void run()
	{
		if ( bRunning )
		{
			this.tutorVolleyPanelGame.think();
			this.tutorVolleyPanelGame.repaint();
		}
	}

}
