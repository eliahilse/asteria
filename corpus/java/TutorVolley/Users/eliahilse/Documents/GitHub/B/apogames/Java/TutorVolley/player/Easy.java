import tutorVolley.TutorVolleyBall;
import tutorVolley.TutorVolleyPlayer;
import tutorVolley.TutorVolleyAI;

public class Easy implements TutorVolleyAI
{

	public Easy()
	{
	}

	public boolean think(TutorVolleyBall ball, TutorVolleyPlayer player)
	{
		boolean bLeft	= true;
		if ( player.getXMiddle() > 200 )
			bLeft	= false;
		if ( ball.getBFirst() == false)
		{
			if ( player.getXMiddle() + 15 < ball.getXMiddle() )
			{
				player.setDirection( TutorVolleyPlayer.DIR_RIGHT );
				return false;
			} else if ( player.getXMiddle() - 15 > ball.getXMiddle() )
			{
				player.setDirection( TutorVolleyPlayer.DIR_LEFT );
				return false;
			}
			return true;
		}
		if ( ball.getXMiddle() > player.getXMiddle() - 15 )
		{
			player.setDirection( TutorVolleyPlayer.DIR_RIGHT );
		} else if ( ( ball.getXMiddle() < player.getXMiddle() + 15 ) )
		{
			player.setDirection( TutorVolleyPlayer.DIR_LEFT );
		}
		if ( ( ( bLeft ) && ( ball.getXMiddle() < 200 ) ) ||
			 ( ( !bLeft ) && ( ball.getXMiddle() > 200 ) ) )
				return true;
		if ( !bLeft )
			player.setDirection( TutorVolleyPlayer.DIR_LEFT );
		else
			player.setDirection( TutorVolleyPlayer.DIR_RIGHT );
		return false;
	}

	public String getName()
	{
		return "Easy";
	}

	public String getAuthorName()
	{
		return "Apo und Baschan";
	}

}
