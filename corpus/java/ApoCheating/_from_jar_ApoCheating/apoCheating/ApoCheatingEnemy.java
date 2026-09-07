package apoCheating;

public class ApoCheatingEnemy
{
	private ApoCheatingPlayer	player;
	
	public ApoCheatingEnemy( ApoCheatingPlayer player )
	{
		this.player		= player;
	}
	
	public double getX()
	{
		return this.player.getX();
	}
	
	public double getY()
	{
		return this.player.getY();
	}
	
	public double getCheated()
	{
		return this.player.getCheated();
	}
	
	public double getDetected()
	{
		return this.player.getDetected();
	}

}
