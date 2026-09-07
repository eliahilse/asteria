package apoDefence.game;

import java.awt.Graphics2D;

public class ApoDefenceHighscoreEntity
{
	private String	nickname;
	private int		points;
	private int		level;
	private String	map;
	
	
	public ApoDefenceHighscoreEntity()
	{
		super();

		this.nickname		= "";
		this.points			= 0;
		this.level			= 0;
		this.map			= "";
	}
	
	public ApoDefenceHighscoreEntity( String nickname, String map, int level, int points )
	{
		super();

		this.nickname		= nickname;
		this.points			= points;
		this.level			= level;
		this.map			= map;
	}
	
	public int getLevel()
	{
		return this.level;
	}

	public String getMap()
	{
		return this.map;
	}

	public String getNickname()
	{
		return this.nickname;
	}

	public int getPoints()
	{
		return this.points;
	}

	public void render( Graphics2D g, int x, int y, int place )
	{
		String s	= ""+(place+1);
		int w		= g.getFontMetrics().stringWidth( s );
		g.drawString( ""+(place+1), x + 10 - w, y );
		s	= ""+this.nickname;
		w	= g.getFontMetrics().stringWidth( s );
		g.drawString( s, x + 125 - w/2, y );
		s	= ""+this.map;
		w	= g.getFontMetrics().stringWidth( s );
		g.drawString( s, x + 293 - w/2, y );
		s	= ""+this.level;
		w	= g.getFontMetrics().stringWidth( s );
		g.drawString( s, x + 395 - w/2, y );
		s	= ""+this.points;
		w	= g.getFontMetrics().stringWidth( s );
		g.drawString( s, x + 482 - w/2, y );
	}

}
