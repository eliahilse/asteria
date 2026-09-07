package apoCheating;

import java.util.ArrayList;

public class ApoCheatingLevel
{
	private int[][]							aPlayground;
	private ArrayList<ApoCheatingTeacher>	teacher;
	private ArrayList<ApoCheatingPlayer>	player;
	private ArrayList<ApoCheatingGoal>		goal;
	private ArrayList<ApoCheatingExtra>		extra;
	private ArrayList<ApoCheatingFinish>	finish;
	private String							message;
	
	public ApoCheatingLevel( int[][] aPlayground, ArrayList<ApoCheatingTeacher> teacher, ArrayList<ApoCheatingPlayer> player, ArrayList<ApoCheatingGoal> goal, ArrayList<ApoCheatingExtra> extra, ArrayList<ApoCheatingFinish> finish, String message )
	{
		super();
		
		this.aPlayground	= aPlayground;
		this.teacher		= teacher;
		this.player			= player;
		this.goal			= goal;
		this.extra			= extra;
		this.finish			= finish;
		this.message		= message;
	}

	protected ArrayList<ApoCheatingExtra> getExtra()
	{
		return this.extra;
	}

	protected void setExtra(ArrayList<ApoCheatingExtra> extra)
	{
		this.extra = extra;
	}

	protected ArrayList<ApoCheatingFinish> getFinish()
	{
		return this.finish;
	}

	protected void setFinish(ArrayList<ApoCheatingFinish> finish)
	{
		this.finish = finish;
	}

	protected ArrayList<ApoCheatingGoal> getGoal()
	{
		return this.goal;
	}

	protected void setGoal(ArrayList<ApoCheatingGoal> goal)
	{
		this.goal = goal;
	}

	protected String getMessage()
	{
		return this.message;
	}

	protected void setMessage(String message)
	{
		this.message = message;
	}

	protected ArrayList<ApoCheatingPlayer> getPlayer()
	{
		return this.player;
	}

	protected void setPlayer(ArrayList<ApoCheatingPlayer> player)
	{
		this.player = player;
	}

	protected ArrayList<ApoCheatingTeacher> getTeacher()
	{
		return this.teacher;
	}

	protected void setTeacher(ArrayList<ApoCheatingTeacher> teacher)
	{
		this.teacher = teacher;
	}

	protected int[][] getAPlayground()
	{
		return this.aPlayground;
	}

	protected void setAPlayground(int[][] playground)
	{
		aPlayground = playground;
	}

}
