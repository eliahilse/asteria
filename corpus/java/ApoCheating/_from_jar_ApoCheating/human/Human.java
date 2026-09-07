package human;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import apoCheating.ApoCheatingAI;
import apoCheating.ApoCheatingEnemy;
import apoCheating.ApoCheatingEntity;
import apoCheating.ApoCheatingMain;
import apoCheating.ApoCheatingPlayer;
import apoCheating.ApoCheatingTeacher;

public class Human implements ApoCheatingAI, KeyListener
{
	private final int		empty	= 0;
	private final int		up		= -1;
	private final int		right	= 1;
	private final int		down	= 1;
	private final int		left	= -1;
	private int 			leftDirection, upDirection;
	private boolean			bAction;
	private boolean			bThrow;
	private int				player;
	
	public Human()
	{
		super();
		
		this.player		= 1;
		
		ApoCheatingMain.apoCheatingMain.addKeyListener( this );
	}

	public void setPlayer( int player )
	{
		this.player		= player;
	}
	
	public void keyTyped(KeyEvent e)
	{}

	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		int keyLoc	= e.getKeyLocation();
		if ( ( this.player == 1 ) && ( ( keyLoc == 1 ) || ( keyLoc == 3 ) ) )
		{
			if ( ( keyCode == KeyEvent.VK_SPACE ) && ( !this.bAction ) )
				this.bAction	= true;
			if ( keyCode == KeyEvent.VK_UP )
				this.upDirection	= this.up;
			if ( keyCode == KeyEvent.VK_RIGHT )
				this.leftDirection	= this.right;
			if ( keyCode == KeyEvent.VK_DOWN )
				this.upDirection	= this.down;
			if ( keyCode == KeyEvent.VK_LEFT )
				this.leftDirection	= this.left;
			if ( keyCode == KeyEvent.VK_B )
				this.bThrow	= true;
		} else if ( ( this.player == 2 ) && ( ( keyLoc == 1 ) || ( keyLoc == 2 ) ) )
		{
			if ( ( keyCode == KeyEvent.VK_SHIFT ) && ( !this.bAction ) )
				this.bAction	= true;
			if ( keyCode == KeyEvent.VK_W )
				this.upDirection	= this.up;
			if ( keyCode == KeyEvent.VK_D )
				this.leftDirection	= this.right;
			if ( keyCode == KeyEvent.VK_S )
				this.upDirection	= this.down;
			if ( keyCode == KeyEvent.VK_A )
				this.leftDirection	= this.left;
			if ( keyCode == 153 )
				this.bThrow	= true;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		int keyLoc	= e.getKeyLocation();
		if ( ( this.player == 1 ) && ( ( keyLoc == 1 ) || ( keyLoc == 3 ) ) )
		{
			if ( ( keyCode == KeyEvent.VK_SPACE ) && ( this.bAction ) )
				this.bAction	= false;
			if ( keyCode == KeyEvent.VK_UP )
				this.upDirection	= this.empty;
			if ( keyCode == KeyEvent.VK_RIGHT )
				this.leftDirection	= this.empty;
			if ( keyCode == KeyEvent.VK_DOWN )
				this.upDirection	= this.empty;
			if ( keyCode == KeyEvent.VK_LEFT )
				this.leftDirection	= this.empty;
			if ( keyCode == KeyEvent.VK_B )
				this.bThrow	= false;
		} else if ( ( this.player == 2 ) && ( ( keyLoc == 1 ) || ( keyLoc == 2 ) ) )
		{
			if ( ( keyCode == KeyEvent.VK_SHIFT ) && ( this.bAction ) )
				this.bAction	= false;
			if ( keyCode == KeyEvent.VK_W )
				this.upDirection	= this.empty;
			if ( keyCode == KeyEvent.VK_D )
				this.leftDirection	= this.empty;
			if ( keyCode == KeyEvent.VK_S )
				this.upDirection	= this.empty;
			if ( keyCode == KeyEvent.VK_A )
				this.leftDirection	= this.empty;
			if ( keyCode == 153 )
				this.bThrow	= false;
		}
	}
	
	public String getName()
	{
		return "Mensch";
	}

	public String getAuthorName()
	{
		return "Dirk Aporius";
	}

	public void think(int[][] aPlayground, ApoCheatingPlayer player, ArrayList<ApoCheatingEntity> entity, ApoCheatingEnemy enemy, ArrayList<ApoCheatingTeacher> teacher)
	{
		player.setAction( this.bAction );
		player.setDirection( this.upDirection, this.leftDirection );
		if ( this.bThrow )
			player.throwCoin();
	}

}
