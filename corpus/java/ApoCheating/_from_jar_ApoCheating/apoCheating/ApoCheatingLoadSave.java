package apoCheating;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ApoCheatingLoadSave
{
	private int[][]							aPlayground;
	private ArrayList<ApoCheatingTeacher>	teacher;
	private ArrayList<ApoCheatingPlayer>	player;
	private ArrayList<ApoCheatingGoal>		goal;
	private ArrayList<ApoCheatingExtra>		extra;
	private ArrayList<ApoCheatingFinish>	finish;
	private String							message;
	private String							levelName;
	private int								levelNumber;
	
	private BufferedImage					iPaper;
	private BufferedImage					iPlayer;
	private BufferedImage					iExtra;
	private BufferedImage					iGoal;
	private BufferedImage					iTeacher;
	private BufferedImage					iUndetect;
	
	public ApoCheatingLoadSave()
	{
		ApoCheatingImage	image	= new ApoCheatingImage();
		
		this.iPaper					= image.setPicsMain( "/images/paper.png", false );
		this.iExtra					= this.getIPlayer( Color.YELLOW );
		this.iGoal					= this.getIPlayer( Color.GREEN );
		this.iTeacher				= this.getIPlayer( Color.RED );
		this.iPlayer				= this.getIPlayer( Color.BLUE );
		this.iUndetect				= this.getIPlayer( Color.GRAY );

		this.levelName		= "";
		this.message		= "";
		this.levelNumber	= 1;
	}

	private BufferedImage getIPlayer( Color color ) {
		BufferedImage iPlayer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( 22, 22, Transparency.TRANSLUCENT );
		
		Graphics2D g = (Graphics2D)iPlayer.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor( color );
		g.fillOval( 0, 0, iPlayer.getWidth() - 1, iPlayer.getHeight() - 1 );
		
		Stroke clone = g.getStroke();
		
		g.setColor( Color.BLACK );
		g.setStroke( new BasicStroke( 2 ) );
		g.drawOval( 0, 0, iPlayer.getWidth() - 2, iPlayer.getHeight() - 2 );
		
		g.setStroke( clone );
		
		g.dispose();
		
		return iPlayer;
	}
	
	protected void setAll( int[][] aPlayground, ArrayList<ApoCheatingTeacher> teacher, ArrayList<ApoCheatingPlayer> player, ArrayList<ApoCheatingGoal> goal, ArrayList<ApoCheatingExtra> extra, ArrayList<ApoCheatingFinish> finish, String message )
	{
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
		ArrayList<ApoCheatingExtra> extra	= new ArrayList<ApoCheatingExtra>();
		for ( int i = 0; i < this.extra.size(); i++ )
		{
			extra.add( new ApoCheatingExtra( this.extra.get( i ).getIPaper(), this.iUndetect, this.iExtra, (int)this.extra.get( i ).getX(), (int)this.extra.get( i ).getY(), this.extra.get( i ).getWidth(), this.extra.get( i ).getHeight(), this.extra.get( i ).isBDetect() ) );
		}
		return extra;
	}

	protected void setExtra(ArrayList<ApoCheatingExtra> extra)
	{
		this.extra = extra;
	}

	protected ArrayList<ApoCheatingGoal> getGoal()
	{
		ArrayList<ApoCheatingGoal> goal	= new ArrayList<ApoCheatingGoal>();
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			goal.add( new ApoCheatingGoal( this.goal.get( i ).getIPaper(), this.iUndetect, this.iGoal, (int)this.goal.get( i ).getX(), (int)this.goal.get( i ).getY(), this.goal.get( i ).getWidth(), this.goal.get( i ).getHeight(), this.goal.get( i ).isBDetect(), this.goal.get( i ).getOldPlus() ) );
		}
		return goal;
	}

	protected void setGoal(ArrayList<ApoCheatingGoal> goal)
	{
		this.goal = goal;
	}

	protected int[][] getAPlayground()
	{
		return this.aPlayground;
	}

	protected void setAPlayground(int[][] playground)
	{
		this.aPlayground = playground;
	}

	protected ArrayList<ApoCheatingTeacher> getTeacher()
	{
		return this.teacher;
	}

	protected void setTeacher(ArrayList<ApoCheatingTeacher> teacher)
	{
		this.teacher = teacher;
	}

	protected ArrayList<ApoCheatingPlayer> getPlayer()
	{
		return this.player;
	}

	protected void setPlayer(ArrayList<ApoCheatingPlayer> player)
	{
		this.player = player;
	}
	
	protected ArrayList<ApoCheatingFinish> getFinish()
	{
		return this.finish;
	}

	protected void setFinish(ArrayList<ApoCheatingFinish> finish)
	{
		this.finish = finish;
	}

	protected String getMessage()
	{
		return this.message;
	}

	protected void setMessage(String message)
	{
		this.message	= message;
	}
	
	protected String getLevelName()
	{
		return this.levelName;
	}

	protected void setLevelName(String levelName)
	{
		int count		= 0;
		String value	= "";
		for ( int i = levelName.length()-1; i >= 0; i-- )
		{
			char c	= levelName.charAt( i );
			if ( ( c >= 48 ) && ( c <= 57 ) )
			{
				value	= c + value;
				count++;
			}
		}
		try
		{
			this.levelNumber 	= Integer.parseInt ( value );
		} catch (Exception E)
		{
			this.levelNumber	= 1;
		}
		this.levelName = levelName.substring( 0, levelName.length() - count );
	}
	
	protected int getLevelNumber()
	{
		return this.levelNumber;
	}
	
	/**
	 * schreibt eine Datei in das übergebene String Filesystem
	 * @param fileName = wo wird hingespeichert
	 */
	protected void writeLevel( String fileName )
	{
		try
		{
			FileOutputStream file		= new FileOutputStream( fileName );
			ObjectOutputStream out 		= new ObjectOutputStream( file );
			
			out.writeInt( this.aPlayground.length );
			out.writeInt( this.aPlayground[0].length );
			for ( int i = 0; i < this.aPlayground.length; i++ )
			{
				for ( int j = 0; j < this.aPlayground[0].length; j++ )
				{
					out.writeInt( this.aPlayground[i][j] );
				}
			}
			
			out.writeInt( this.teacher.size() );
			for ( int i = 0; i < this.teacher.size(); i++ )
			{
				ArrayList<ApoCheatingTeacherView> view = this.teacher.get( i ).getView();
				out.writeInt( view.size() );
				for ( int j = 0; j < view.size(); j++ )
				{
					int width		= view.get( j ).getWidth();
					int height		= view.get( j ).getHeight();
					double startDir	= view.get( j ).getStartDir();
					double endDir	= view.get( j ).getEndDir();
					double speed	= view.get( j ).getSpeed();
					int maxTicks	= view.get( j ).getMaxTicks();
					out.writeInt( width );
					out.writeInt( height );
					out.writeDouble( startDir );
					out.writeDouble( endDir );
					out.writeDouble( speed );
					out.writeInt( maxTicks );
				}
				ArrayList<ApoCheatingTeacherWay> way = this.teacher.get( i ).getWay();
				out.writeInt( way.size() );
				for ( int j = 0; j < way.size(); j++ )
				{
					int startX		= way.get( j ).getStartX();
					int startY		= way.get( j ).getStartY();
					int finishX		= way.get( j ).getFinishX();
					int finishY		= way.get( j ).getFinishY();
					double speed	= way.get( j ).getSpeed();
					int maxTicks	= way.get( j ).getMaxTicks();
					out.writeInt( startX );
					out.writeInt( startY );
					out.writeInt( finishX );
					out.writeInt( finishY );
					out.writeDouble( speed );
					out.writeInt( maxTicks );
					//System.out.println("x = "+startX+" y = "+startY+" fX = "+finishX+" fY = "+finishY+" speed = "+speed+" ticks = "+maxTicks);
				}
				double x	= this.teacher.get( i ).getX();
				double y	= this.teacher.get( i ).getY();
				int width	= this.teacher.get( i ).getWidth();
				int height	= this.teacher.get( i ).getHeight();
				out.writeDouble( x );
				out.writeDouble( y );
				out.writeInt( width );
				out.writeInt( height );
				//System.out.println( "x = "+x+" y = "+y+" width = "+width+" height = "+height+" view = "+view.size()+" way = "+way.size());
			}
			
			out.writeInt( this.player.size() );
			for ( int i = 0; i < this.player.size(); i++ )
			{
				double x	= this.player.get( i ).getX();
				double y	= this.player.get( i ).getY();
				int width	= this.player.get( i ).getWidth();
				int height	= this.player.get( i ).getHeight();
				int player	= this.player.get( i ).getPlayer();
				int coins	= this.player.get( i ).getCoins();
				out.writeDouble( x );
				out.writeDouble( y );
				out.writeInt( width );
				out.writeInt( height );
				out.writeInt( player );
				out.writeInt( coins );
			}
			
			out.writeInt( this.goal.size() );
			for ( int i = 0; i < this.goal.size(); i++ )
			{
				double x		= this.goal.get( i ).getX();
				double y		= this.goal.get( i ).getY();
				int width		= this.goal.get( i ).getWidth();
				int height		= this.goal.get( i ).getHeight();
				boolean bDetect	= this.goal.get( i ).isBDetect();
				double plus		= this.goal.get( i ).getOldPlus();
				out.writeDouble( x );
				out.writeDouble( y );
				out.writeInt( width );
				out.writeInt( height );
				out.writeBoolean( bDetect );
				out.writeDouble( plus );
				//System.out.println(plus);
			}
			
			out.writeInt( this.extra.size() );
			for ( int i = 0; i < this.extra.size(); i++ )
			{
				double x		= this.extra.get( i ).getX();
				double y		= this.extra.get( i ).getY();
				int width		= this.extra.get( i ).getWidth();
				int height		= this.extra.get( i ).getHeight();
				boolean bDetect	= this.extra.get( i ).isBDetect();
				out.writeDouble( x );
				out.writeDouble( y );
				out.writeInt( width );
				out.writeInt( height );
				out.writeBoolean( bDetect );
			}
			
			out.writeInt( this.finish.size() );
			for ( int i = 0; i < this.finish.size(); i++ )
			{
				double x	= this.finish.get( i ).getX();
				double y	= this.finish.get( i ).getY();
				int width	= this.finish.get( i ).getWidth();
				int height	= this.finish.get( i ).getHeight();
				int finish	= this.finish.get( i ).getPlayer();
				out.writeDouble( x );
				out.writeDouble( y );
				out.writeInt( width );
				out.writeInt( height );
				out.writeInt( finish );
			}
			
			out.writeUTF( this.message );
			
			out.close();
		} catch ( IOException e )
		{
			System.out.println("Error: "+e);
		}
	}
	
	protected boolean hasNextLevel( String fileName )
	{
		try
		{
			FileInputStream file		= new FileInputStream( fileName );
			ObjectInputStream in 		= new ObjectInputStream( file );
			in.close();
			return true;
		} catch ( IOException e )
		{
			return false;
		}
	}
	
	/**
	 * liest eine Datei in das übergebene String Filesystem
	 * @param fileName = von wo wird gelesen
	 */
	protected void readLevel( String fileName )
	{
		try
		{
			FileInputStream file		= new FileInputStream( fileName );
			ObjectInputStream in 		= new ObjectInputStream( file );
			try
			{
				int y = in.readInt();
				int x = in.readInt();
				this.aPlayground	= new int[y][x];
				for ( int i = 0; i < y; i++ )
				{
					for ( int j = 0; j < x; j++ )
					{
						this.aPlayground[i][j]	= in.readInt();
					}
				}
				
				int size 		= in.readInt();
				this.teacher	= new ArrayList<ApoCheatingTeacher>();
				for ( int i = 0; i < size; i++ )
				{
					ArrayList<ApoCheatingTeacherView> view = new ArrayList<ApoCheatingTeacherView>();
					int viewSize = in.readInt();
					for ( int j = 0; j < viewSize; j++ )
					{
						int width		= in.readInt();
						int height		= in.readInt();
						double startDir	= in.readDouble();
						double endDir	= in.readDouble();
						double speed	= in.readDouble();
						int maxTicks	= in.readInt();
						//System.out.println("width = "+width+" height = "+height+" startDir = "+startDir+" endDir = "+endDir+" speed = "+speed+" ticks = "+maxTicks);
						view.add( new ApoCheatingTeacherView( width, height, startDir, endDir, speed, maxTicks ) );
					}
					ArrayList<ApoCheatingTeacherWay> way = new ArrayList<ApoCheatingTeacherWay>();
					int waySize = in.readInt();
					for ( int j = 0; j < waySize; j++ )
					{
						int startX		= in.readInt();
						int startY		= in.readInt();
						int finishX		= in.readInt();
						int finishY		= in.readInt();
						double speed	= in.readDouble();
						int maxTicks	= in.readInt();
						//System.out.println("x = "+startX+" y = "+startY+" fX = "+finishX+" fY = "+finishY+" speed = "+speed+" ticks = "+maxTicks);
						way.add( new ApoCheatingTeacherWay( startX, startY, finishX, finishY, speed, maxTicks ) );
					}
					double xTeacher	= in.readDouble();
					double yTeacher	= in.readDouble();
					int width		= in.readInt();
					int height		= in.readInt();
					ApoCheatingTeacher teacher	= new ApoCheatingTeacher( this.iUndetect, this.iTeacher, (int)xTeacher, (int)yTeacher, width, height );
					this.teacher.add( teacher );
					for ( int j = 0; j < view.size(); j++ )
					{
						view.get( j ).setTeacher( teacher );
					}
					this.teacher.get( i ).setView( view );
					this.teacher.get( i ).setWay( way );
				}
				
				size = in.readInt();
				this.player	= new ArrayList<ApoCheatingPlayer>();
				for ( int i = 0; i < size; i++ )
				{
					double xPlayer	= in.readDouble();
					double yPlayer	= in.readDouble();
					int width		= in.readInt();
					int height		= in.readInt();
					int player		= in.readInt();
					int coins		= in.readInt();
					this.player.add( new ApoCheatingPlayer( this.iUndetect, this.iPlayer, (int)xPlayer, (int)yPlayer, width, height, player, coins ) );
				}
				
				size = in.readInt();
				this.goal	= new ArrayList<ApoCheatingGoal>();
				for ( int i = 0; i < size; i++ )
				{
					double xPlayer	= in.readDouble();
					double yPlayer	= in.readDouble();
					int width		= in.readInt();
					int height		= in.readInt();
					boolean bDetect	= in.readBoolean();
					double plus		= in.readDouble();
					//System.out.println(plus);
					this.goal.add( new ApoCheatingGoal( this.iPaper, this.iUndetect, this.iGoal, (int)xPlayer, (int)yPlayer, width, height, bDetect, plus ) );
				}
				
				size = in.readInt();
				this.extra	= new ArrayList<ApoCheatingExtra>();
				for ( int i = 0; i < size; i++ )
				{
					double xPlayer	= in.readDouble();
					double yPlayer	= in.readDouble();
					int width		= in.readInt();
					int height		= in.readInt();
					boolean bDetect	= in.readBoolean();
					this.extra.add( new ApoCheatingExtra( this.iPaper, this.iUndetect, this.iExtra, (int)xPlayer, (int)yPlayer, width, height, bDetect ) );
				}
				
				size = in.readInt();
				this.finish	= new ArrayList<ApoCheatingFinish>();
				for ( int i = 0; i < size; i++ )
				{
					double xPlayer	= in.readDouble();
					double yPlayer	= in.readDouble();
					int width		= in.readInt();
					int height		= in.readInt();
					int player		= in.readInt();
					this.finish.add( new ApoCheatingFinish( (int)xPlayer, (int)yPlayer, width, height, player ) );
				}
				
				this.message		= in.readUTF();
				
			} catch ( EOFException e )
			{
				System.out.println("Error: "+e);
			}
			in.close();
		} catch ( IOException e )
		{
			System.out.println("Error: "+e);
		}
	}

}
