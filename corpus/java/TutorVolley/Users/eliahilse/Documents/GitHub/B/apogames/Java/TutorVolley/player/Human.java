import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import tutorVolley.TutorVolleyBall;
import tutorVolley.TutorVolleyMain;
import tutorVolley.TutorVolleyPlayer;
import tutorVolley.TutorVolleyAI;


public class Human implements TutorVolleyAI, KeyListener
{
	private boolean bJump, bPlayer;
	private int		direction;

	public Human()
	{
		this.bJump		= false;
		this.bPlayer	= false;
		this.direction	= 0;
		
		TutorVolleyMain.tutorVolleyMain.addKeyListener( this );
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e)
	{
		if ( !this.bPlayer )
		{
			if ( e.getKeyCode() == KeyEvent.VK_UP )
			{	
				this.bJump = true;
			}
			if ( e.getKeyCode() == KeyEvent.VK_LEFT )
			{
				this.direction = -1;
			} else if ( e.getKeyCode() == KeyEvent.VK_RIGHT )
			{
				this.direction = 1;
			}
		} else
		{
			if ( e.getKeyCode() == KeyEvent.VK_W )
			{	
				this.bJump = true;
			}
			if ( e.getKeyCode() == KeyEvent.VK_A )
			{
				this.direction = -1;
			} else if ( e.getKeyCode() == KeyEvent.VK_D )
			{
				this.direction = 1;
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
		if ( !this.bPlayer )
		{
			if ( e.getKeyCode() == KeyEvent.VK_UP )
			{	
				this.bJump = false;
			}
			if ( e.getKeyCode() == KeyEvent.VK_LEFT )
			{
				this.direction = 0;
			} else if ( e.getKeyCode() == KeyEvent.VK_RIGHT )
			{
				this.direction = 0;
			}
		} else
		{
			if ( e.getKeyCode() == KeyEvent.VK_W )
			{	
				this.bJump = false;
			}
			if ( e.getKeyCode() == KeyEvent.VK_A )
			{
				this.direction = 0;
			} else if ( e.getKeyCode() == KeyEvent.VK_D )
			{
				this.direction = 0;
			}
		}
	}
	
	public boolean think(TutorVolleyBall ball, TutorVolleyPlayer player)
	{
		if ( player.getX() < 200 )
			this.bPlayer	= false;
		else
			this.bPlayer	= true;
		player.setDirection( this.direction );
		return this.bJump;
	}

	public String getName()
	{
		return "Mensch";
	}

	public String getAuthorName()
	{
		return "Euer Lieblingstutor";
	}

}
