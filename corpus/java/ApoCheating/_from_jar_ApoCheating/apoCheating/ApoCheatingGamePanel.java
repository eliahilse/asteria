package apoCheating;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ApoCheatingGamePanel extends JPanel implements Runnable
{
	private static final long 				serialVersionUID 	= 1L;
	private final long						WAIT_TIME			= 25;
	
	private ApoCheatingLoadSave				load;
	
	private ArrayList<ApoCheatingTeacher>	teacher;
	private ArrayList<ApoCheatingPlayer>	player;
	private ArrayList<ApoCheatingGoal>		goal;
	private ArrayList<ApoCheatingExtra>		extra;
	private ArrayList<ApoCheatingFinish>	finish;
	private ArrayList<ApoCheatingEntity> 	entity;
	private boolean							bThread, bFinish;
	private int								playerWin;
	
	private int[][]							aPlayground;

	private BufferedImage[][]				aImage;
	private BufferedImage					iF, iArrow, iPokal, iBackground, iMessage, iWin;
	
	private ApoCheatingHudPanel				apoCheatingHudPanel;
	
	/** A FileChooser */
	private final JFileChooser 				fc = new JFileChooser();
	/** A Class file filter */
	private final ApoCheatingFileFilter		acff = new ApoCheatingFileFilter( "cheat" );
	
	private String 							message;
	private String							file, mes;
	
	private final Font						fontBig = new Font( "Dialog", Font.BOLD, 23 );
	private final Font						fontNormal = new Font( "Dialog", Font.BOLD, 19 );
	private final Font						fontGiant = new Font( "Dialog", Font.BOLD, 30 );
	
	private ApoCheatingTimer				apoCheatingTimer;
	
	public ApoCheatingGamePanel()
	{
		super( true );
	}
	
	public void init()
	{
		this.fc.setCurrentDirectory(new File( System.getProperty("user.dir") + File.separator+"levels" ));
		this.fc.setFileFilter( this.acff );
		
		ApoCheatingImage	image	= new ApoCheatingImage();
		
		this.iPokal					= image.setPicsMain( "/images/win.png", false );
		this.iF						= image.setPicsMain( "/images/f.png", false );
		this.iArrow					= image.setPicsMain( "/images/arrow.png", false );
		
		this.load					= new ApoCheatingLoadSave();

		this.message				= "";
		
		BufferedImage iTileSheet		= image.setPicsMain( "/images/tilesheet.png", false );
		
		int x				= iTileSheet.getWidth()/32;
		int y				= iTileSheet.getHeight()/32;
		int startX			= 0;
		int startY			= 0;
		
		this.aImage			= new BufferedImage[y][x];
		for ( int i = 0; i < y; i++ )
		{
			for ( int j = 0; j < x; j++ )
			{
				this.aImage[i][j]	= iTileSheet.getSubimage( startX * 32, startY * 32, 32, 32 );
				startX		+= 1;
			}
			startX	= 0;
			startY	+= 1;
		}
		
		this.load( System.getProperty("user.dir") + File.separator+"levels"+File.separator+"Original1.cheat" );
		
		this.playerWin		= 0;
		
		this.apoCheatingTimer	= new ApoCheatingTimer( this );
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(this.apoCheatingTimer, 0,this.WAIT_TIME);
		
		//this.bThread		= false;
		//this.bFinish		= false;
		
		//this.start();
	}
	
	private BufferedImage getIMessage() {
		BufferedImage iMessage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( this.getWidth(), this.getHeight(), Transparency.TRANSLUCENT );
		
		Graphics2D g = (Graphics2D)iMessage.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		this.drawMessage( g );
		
		return iMessage;
	}
	
	private BufferedImage getIWin() {
		BufferedImage iWin = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage( this.getWidth(), this.getHeight(), Transparency.TRANSLUCENT );
		
		Graphics2D g = (Graphics2D)iWin.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		this.drawWin( g );
		
		return iWin;
	}
	
	private void setEntity()
	{
		this.entity	= new ArrayList<ApoCheatingEntity>();
		for ( int i = 0; i < this.extra.size(); i++ )
			this.entity.add( (ApoCheatingEntity)this.extra.get( i ) );
		for ( int i = 0; i < this.goal.size(); i++ )
			this.entity.add( (ApoCheatingEntity)this.goal.get( i ) );
	}
	
	protected boolean isBThread()
	{
		return this.bThread;
	}
	
	protected void load( String file )
	{
		if ( this.bThread )
			return;
		int p = 0;
		if ( file == null )
			p = this.fc.showOpenDialog(this);
		if(p == 0)
		{
			String s;
			if ( file == null )
				s = this.fc.getSelectedFile().getPath();
			else
				s = file;
			int t = s.indexOf(46);
			if ( t != -1 )
			{
				s	= s.substring( 0, t );
			}
			
			int u	= s.lastIndexOf( 92 );
			String v	= "";
			if ( u != -1 )
				v	= s.substring( u+1 );
			
			this.file	= s;
			//System.out.println( "file = "+this.file );
			s	+= this.acff.getLevelName();
			this.load.readLevel( s );
			this.load.setLevelName( v );
			
			this.aPlayground	= this.load.getAPlayground();
			this.teacher		= this.load.getTeacher();
			this.player			= this.load.getPlayer();
			this.goal			= this.load.getGoal();
			this.extra			= this.load.getExtra();
			this.finish			= this.load.getFinish();
			this.message		= this.load.getMessage();
			
			if ( this.apoCheatingHudPanel != null )
				this.apoCheatingHudPanel.setPlayer( this.player );
			
			this.setHands();
			this.setEntity();
			
			this.makeBackground();
			
			this.iMessage = this.getIMessage();
			
			this.repaint();
		}
	}
	
	protected String getLevelName()
	{
		if ( this.load != null )
			return this.load.getLevelName() + " " + this.load.getLevelNumber();
		return "";
	}
	
	protected boolean next()
	{
		int t	= this.getValueFromFile();
		t++;
		String mes	= this.mes + t;
		if ( this.load.hasNextLevel( mes+".cheat" ) )
		{
			this.load( mes+".cheat" );
			return true;
		} else if ( this.load.hasNextLevel( this.mes+"1"+".cheat" ) )
		{
			this.load( this.mes+"1"+".cheat" );
			return true;
		}
		return false;
	}
	
	protected boolean previous()
	{
		int t	= this.getValueFromFile();
		t--;
		String mes	= this.mes + t;
		if ( this.load.hasNextLevel( mes+".cheat" ) )
		{
			this.load( mes+".cheat" );
			return true;
		}
		return false;
	}
	
	private int getValueFromFile()
	{
		if ( this.file != null )
		{
			String value = "";
			for ( int i = this.file.length()-1; i >= 0; i-- )
			{
				char c	= this.file.charAt( i );
				if ( c == 92 )
					break;
				if ( ( c >= 48 ) && ( c <= 57 ) )
				{
					value	= c + value;
					
				} else
					break;
			}
			if ( value.length() != 0 )
			{
				mes	= this.file.substring( 0, this.file.length()-value.length() );
				int t = 0;
				try
				{
					t = Integer.parseInt (value);
					return t;
				} catch (Exception e)
				{
					//	es war keine Zahl
				}
			}
		}
		return 0;
	}
	
	protected void save()
	{
		if ( this.bThread )
			return;
		int p = this.fc.showSaveDialog(this);
		if(p == 0)
		{
			String s = this.fc.getSelectedFile().getPath();
			int t = s.indexOf(46);
			if ( t != -1 )
			{
				s	= s.substring( 0, t );
			}
			s	+= this.acff.getLevelName();
			this.load.setAll( this.aPlayground, this.teacher, this.player, this.goal, this.extra, this.finish, "Test" );
			this.load.writeLevel( s );
		}
	}
	
	protected ApoCheatingHudPanel getApoCheatingHudPanel()
	{
		return this.apoCheatingHudPanel;
	}

	protected void setApoCheatingHudPanel( ApoCheatingHudPanel apoCheatingHudPanel )
	{
		this.apoCheatingHudPanel = apoCheatingHudPanel;
		this.apoCheatingHudPanel.setPlayer( this.player );
		
	}

	private void setHands()
	{
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			int x	= (int)((this.goal.get( i ).getX() + this.goal.get( i ).getWidth()/2) / 32);
			int y	= (int)((this.goal.get( i ).getY() + this.goal.get( i ).getHeight()/2) / 32);
			if ( ( x - 1 >= 0 ) && 
				 ( ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_LEFT_LEFT ) ||
				   ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_LEFT_RIGHT ) ||
				   ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_RIGHT_LEFT ) ||
				   ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_RIGHT_RIGHT ) ) &&
				 ( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_LEFT_LEFT ) ||
				   ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_LEFT_RIGHT ) ) )
			{
				this.goal.get( i ).setWayHand( ApoCheatingConstants.EMPTY, ApoCheatingConstants.WRITE_LEFT );
			} else 
			if ( ( x + 1 < 15  ) && 
				 ( ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_LEFT_LEFT ) ||
				   ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_LEFT_RIGHT ) ||
				   ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_RIGHT_LEFT ) ||
				   ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_RIGHT_RIGHT ) ) &&
				 ( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_RIGHT_LEFT ) ||
			       ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_RIGHT_RIGHT ) ) )
			{
				this.goal.get( i ).setWayHand( ApoCheatingConstants.EMPTY, ApoCheatingConstants.WRITE_RIGHT );
			} else
			if ( ( y - 1 >= 0 ) && 
				( ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_UP_LEFT ) ||
				  ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_UP_RIGHT ) ||
				  ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_DOWN_LEFT ) ||
				  ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_DOWN_RIGHT ) ) &&
				( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_UP_LEFT ) ||
				  ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_UP_RIGHT ) ) )
			{
				this.goal.get( i ).setWayHand( ApoCheatingConstants.WRITE_UP, ApoCheatingConstants.EMPTY );
			} else
			if ( ( y + 1 < 15 ) && 
				( ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_UP_LEFT ) ||
				  ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_UP_RIGHT ) ||
				  ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_DOWN_LEFT ) ||
				  ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_DOWN_RIGHT ) ) &&
				( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_DOWN_LEFT ) ||
				  ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_DOWN_RIGHT ) ) )
			{
				this.goal.get( i ).setWayHand( ApoCheatingConstants.WRITE_DOWN, ApoCheatingConstants.EMPTY );
			}
		}
		
		for ( int i = 0; i < this.extra.size(); i++ )
		{
			int x	= (int)((this.extra.get( i ).getX() + this.extra.get( i ).getWidth()/2) / 32);
			int y	= (int)((this.extra.get( i ).getY() + this.extra.get( i ).getHeight()/2) / 32);
			if ( ( x - 1 >= 0 ) && 
				 ( ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_LEFT_LEFT ) ||
				   ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_LEFT_RIGHT ) ||
				   ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_RIGHT_LEFT ) ||
				   ( this.aPlayground[y][x-1] == ApoCheatingConstants.DESK_RIGHT_RIGHT ) ) &&
				 ( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_LEFT_LEFT ) ||
				   ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_LEFT_RIGHT ) ) )
			{
				this.extra.get( i ).setWayHand( ApoCheatingConstants.EMPTY, ApoCheatingConstants.WRITE_LEFT );
			} else 
			if ( ( x + 1 < 15  ) && 
				 ( ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_LEFT_LEFT ) ||
				   ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_LEFT_RIGHT ) ||
				   ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_RIGHT_LEFT ) ||
				   ( this.aPlayground[y][x+1] == ApoCheatingConstants.DESK_RIGHT_RIGHT ) ) &&
				 ( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_RIGHT_LEFT ) ||
			       ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_RIGHT_RIGHT ) ) )
			{
				this.extra.get( i ).setWayHand( ApoCheatingConstants.EMPTY, ApoCheatingConstants.WRITE_RIGHT );
			} else
			if ( ( y - 1 >= 0 ) && 
				 ( ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_UP_LEFT ) ||
				   ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_UP_RIGHT ) ||
				   ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_DOWN_LEFT ) ||
				   ( this.aPlayground[y-1][x] == ApoCheatingConstants.DESK_DOWN_RIGHT ) ) &&
				 ( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_UP_LEFT ) ||
				   ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_UP_RIGHT ) ) )
			{
				this.extra.get( i ).setWayHand( ApoCheatingConstants.WRITE_UP, ApoCheatingConstants.EMPTY );
			} else
			if ( ( y + 1 < 15 ) && 
				 ( ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_UP_LEFT ) ||
				   ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_UP_RIGHT ) ||
				   ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_DOWN_LEFT ) ||
				   ( this.aPlayground[y+1][x] == ApoCheatingConstants.DESK_DOWN_RIGHT ) ) &&
				 ( ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_DOWN_LEFT ) ||
				   ( this.aPlayground[y][x] == ApoCheatingConstants.CHAIR_DOWN_RIGHT ) ) )
			{
				this.extra.get( i ).setWayHand( ApoCheatingConstants.WRITE_DOWN, ApoCheatingConstants.EMPTY );
			}
		}
	}
	
	protected void start()
	{
		if ( this.bThread )
			return;
		this.bThread		= true;
		this.bFinish		= false;
		this.playerWin		= 0;
		Thread t	= new Thread( this );
		t.start();
		//this.apoCheatingTimer.setBRunning( true );
	}
	
	protected void stop()
	{
		this.bThread		= false;
		//this.apoCheatingTimer.setBRunning( false );
		this.playerWin		= 0;
		this.setOld();
		this.repaint();
	}

	private void setOld()
	{
		this.bFinish	= false;
		for ( int i = 0; i < this.player.size(); i++ )
		{
			this.player.get( i ).init();
		}
		
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			this.goal.get( i ).init();
		}
		
		for ( int i = 0; i < this.extra.size(); i++ )
		{
			this.extra.get( i ).init();
		}
		
		for ( int i = 0; i < this.finish.size(); i++ )
		{
			this.finish.get( i ).init();
		}
		
		for ( int i = 0; i < this.teacher.size(); i++ )
		{
			this.teacher.get(i).init();
		}
	}
	
	protected void setOriginal()
	{
		this.goal			= this.load.getGoal();
		this.extra			= this.load.getExtra();
		this.setHands();
		this.repaint();
	}
	
	protected void setNewRandom( boolean bDetect )
	{
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			this.extra.add( new ApoCheatingExtra( this.goal.get( i ).getIPaper(), (int)this.goal.get( i ).getX(), (int)this.goal.get( i ).getY(), this.goal.get( i ).getWidth(), this.goal.get( i ).getHeight(), bDetect ) );
		}
		this.goal	= new ArrayList<ApoCheatingGoal>();
		int goals	= (int)(Math.random() * 2) + 1;
		for ( int i = 0; i < goals; i++ )
		{
			if ( this.extra.size() == 0 )
				break;
			int value		= (int)(Math.random() * this.extra.size());
			if ( value == this.extra.size() )
				value--;
			double plus		= Math.random() * 0.4 + 0.1;
			this.goal.add( new ApoCheatingGoal( this.extra.get( value ).getIPaper(), (int)this.extra.get( value ).getX(), (int)this.extra.get( value ).getY(), this.extra.get( value ).getWidth(), this.extra.get( value ).getHeight(), bDetect, plus ) );
			this.extra.remove( value );
		}
		this.setHands();
		this.repaint();
	}
	
	protected void setDetect( boolean bDetect )
	{
		for ( int i = 0; i < this.extra.size(); i++ )
		{
			this.extra.get( i ).setBOldDetect( bDetect );
		}
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			this.goal.get( i ).setBOldDetect( bDetect );
		}
		this.repaint();
	}
	
	protected void think()
	{
		this.finish();
		boolean bCoin		= false;
		for ( int i = 0; i < this.player.size(); i++ )
		{
			ApoCheatingEnemy enemy	= null;
			if ( this.player.size() != 1 )
			{
				if ( i == 0 )
					enemy	= new ApoCheatingEnemy( this.player.get( i+1 ) );
				else
					enemy	= new ApoCheatingEnemy( this.player.get( i-1 ) );
			}
			this.player.get( i ).think( this.aPlayground, this.player.get( i ), this.entity, enemy, this.teacher );
			if ( this.player.get( i ).isBCoin() )
			{
				this.player.get( i ).setCurrentCoinTicks( this.player.get( i ).getCurrentCoinTicks() + 1 );
				if ( this.player.get( i ).getCurrentCoinTicks() < this.player.get( i ).getThrowCoinTicks() )
				{
					double speed	= this.player.get( i ).getSpeed();
					if ( 	( this.player.get( i ).getCoinLeftDirection() != ApoCheatingConstants.EMPTY ) &&
							( this.player.get( i ).getCoinUpDirection() != ApoCheatingConstants.EMPTY ) )
					{
						speed		= speed/2;
					}
					if ( this.isThrowCoin( this.player.get( i ), speed ) )
					{
						this.player.get( i ).setCoinX( this.player.get( i ).getCoinLeftDirection() * speed + this.player.get( i ).getCoinX() );
						this.player.get( i ).setCoinY( this.player.get( i ).getCoinUpDirection() * speed + this.player.get( i ).getCoinY() );
					}
				} else if ( this.player.get( i ).getCurrentCoinTicks() == this.player.get( i ).getThrowCoinTicks() )
				{
					if ( !bCoin )
					{
						if ( this.setCoinDown( this.player.get( i ).getCoinX(), this.player.get( i ).getCoinY() ) )
						{
							bCoin = true;
						}
					}
				}
				if ( this.player.get( i ).getCurrentCoinTicks() >= this.player.get( i ).getMaxCoinTicks() )
					this.player.get( i ).setBCoin( false );
			}
			if ( this.player.get( i ).isBAction() )
			{
				this.action();
			}
		}
		for ( int i = 0; i < this.teacher.size(); i++ )
		{
			for ( int j = 0; j < this.player.size(); j++ )
			{
				this.teacher.get(i).isVisible( this.player.get( j ) );
			}
			this.teacher.get(i).think();
		}
		this.addFrame();
		this.move();
		if ( this.apoCheatingHudPanel != null )
			this.apoCheatingHudPanel.repaint();
		this.repaint();
	}
	
	private boolean isThrowCoin( ApoCheatingPlayer player, double speed )
	{
		int x	= (int)( player.getCoinX() + player.getCoinLeftDirection() * speed ) / 32;
		int y	= (int)( player.getCoinY() + player.getCoinUpDirection() * speed ) / 32;
		if ( this.aPlayground[y][x] < ApoCheatingConstants.WALL_ABOVE )
			return true;
		return false;
	}
	
	private boolean setCoinDown( double x, double y )
	{
		boolean bCoin			= false;
		ArrayList<Point> point	= new ArrayList<Point>();
		for ( int i = 0; i < this.teacher.size(); i++ )
		{
			if ( this.teacher.get( i ).isCoinHearing( x, y ) )
			{
				bCoin	= true;
				point.add( new Point( i, i ) );
			}
		}
		if ( !bCoin )
			return false;
		boolean[][]	aWay	= new boolean[this.aPlayground.length][this.aPlayground[0].length];
		for ( int i = 0; i < this.aPlayground.length; i++ )
		{
			for ( int j = 0; j < this.aPlayground[0].length; j++ )
			{
				if ( this.aPlayground[i][j] >= ApoCheatingConstants.DESK_UP_LEFT )
					aWay[i][j]	= false;
				else
					aWay[i][j]	= true;
			}
		}	
		for ( int i = 0; i < this.extra.size(); i++ )
		{
			int xValue	= (int)((this.extra.get( i ).getX() + this.extra.get( i ).getWidth()/2)/32);
			int yValue	= (int)((this.extra.get( i ).getY() + this.extra.get( i ).getHeight()/2)/32);
			aWay[yValue][xValue]	= false;
		}
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			int xValue	= (int)((this.goal.get( i ).getX() + this.goal.get( i ).getWidth()/2)/32);
			int yValue	= (int)((this.goal.get( i ).getY() + this.goal.get( i ).getHeight()/2)/32);
			aWay[yValue][xValue]	= false;
		}
		ArrayList<Point> shortestWay	= null;
		int teacherValue		= -1;
		ApoCheatingSearch search	= new ApoCheatingSearch();
		x		= (int)x/32;
		y		= (int)y/32;
		for ( int i = 0; i < point.size(); i++ )
		{
			int value	= point.get( i ).x;
			Point startPoint	= new Point( (int)(this.teacher.get( value ).getX() + this.teacher.get( value ).getWidth()/2)/32, (int)(this.teacher.get( value ).getY() + this.teacher.get( value ).getHeight()/2)/32 );
			ArrayList<Point> p	= search.findShortestWay( aWay, startPoint, new Point( (int)x, (int)y ) );
			if ( ( shortestWay == null ) || ( ( p.size() != 0 ) && ( p.size() < shortestWay.size() ) ) )
			{
				shortestWay		= p;
				teacherValue	= i;
			}
			//System.out.println( "x = "+x+" y = "+y+" x = "+(int)(this.teacher.get( point.get( i ).x ).getX()/32));
			//System.out.println( p.size() );
		}
		if ( shortestWay.size() != 0 )
		{
			this.teacher.get( point.get( teacherValue ).x ).coinHearAndMakeViewAndWay( shortestWay );
		}
		return true;
	}
	
	private void action()
	{
		for ( int j = 0; j < this.player.size(); j++ )
		{
			for ( int i = 0; i < this.goal.size(); i++ )
			{
				this.goal.get( i ).action( this.player.get( j ) );
			}
			for ( int i = 0; i < this.extra.size(); i++ )
			{
				this.extra.get( i ).action( this.player.get( j ) );
			}
		}
	}
	
	private void addFrame()
	{
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			this.goal.get( i ).addFrame();
		}
		for ( int i = 0; i < this.extra.size(); i++ )
		{
			this.extra.get( i ).addFrame();
		}
	}
	
	private void move()
	{
		for ( int i = 0; i < this.player.size(); i++ )
		{
			boolean bCollision	= false;
			int x	= (int)(this.player.get( i ).getX() + 2 + this.player.get( i ).getLeftDirection()*this.player.get( i ).getSpeed());
			int y	= (int)(this.player.get( i ).getY() + 2 + this.player.get( i ).getUpDirection()*this.player.get( i ).getSpeed());
			bCollision	= this.movePlayer( this.player.get( i ), x, y );
			if ( !bCollision )
				this.player.get( i ).setNewXAndY( true, true );
			else if ( ( this.player.get( i ).getLeftDirection() != ApoCheatingConstants.EMPTY ) &&
					  ( this.player.get( i ).getUpDirection() != ApoCheatingConstants.EMPTY ) )
			{
				bCollision	= this.movePlayer( this.player.get( i ), x, (int)(this.player.get( i ).getY() + 2) );
				if ( !bCollision )
					this.player.get( i ).setNewXAndY( true, false );
				else
				{
					bCollision	= this.movePlayer( this.player.get( i ), (int)(this.player.get( i ).getX() + 2), y );
					if ( !bCollision )
						this.player.get( i ).setNewXAndY( false, true );
				}
			}
		}
	}
	
	private boolean movePlayer( ApoCheatingPlayer player, int x, int y )
	{
		boolean bCollision	= false;
		
		int newX	= x / 32;
		int newY	= y / 32;
		if ( this.aPlayground[newY][newX] >= ApoCheatingConstants.DESK_UP_LEFT )
			bCollision	= true;
		newX	= (x+player.getWidth()-6) / 32;
		newY	= y / 32;
		if ( this.aPlayground[newY][newX] >= ApoCheatingConstants.DESK_UP_LEFT )
			bCollision	= true;
		newX	= (x+player.getWidth()-6) / 32;
		newY	= (y+player.getHeight()-6) / 32;
		if ( this.aPlayground[newY][newX] >= ApoCheatingConstants.DESK_UP_LEFT )
			bCollision	= true;
		newX	= (x) / 32;
		newY	= (y+player.getHeight()-6) / 32;
		if ( this.aPlayground[newY][newX] >= ApoCheatingConstants.DESK_UP_LEFT )
			bCollision	= true;
		for ( int j = 0; j < this.extra.size(); j++ )
		{
			if ( bCollision )
				break;
			if ( this.extra.get( j ).isHitting( player, x, y ) )
			{
				bCollision	= true;
				break;
			}
		}
		for ( int j = 0; j < this.goal.size(); j++ )
		{
			if ( bCollision )
				break;
			if ( this.goal.get( j ).isHitting( player, x, y ) )
			{
				bCollision	= true;
				break;
			}
		}
		return bCollision;
	}
	
	private void finish()
	{
		for ( int j = 0; j < this.player.size(); j++ )
		{
			if ( ( this.player.size() > 1 ) && ( this.player.get( j ).getDetected() >= 100 ) )
			{
				this.bFinish	= true;
				this.bThread	= false;
				this.iWin		= this.getIWin();
				//this.apoCheatingTimer.setBRunning( false );
				if ( j == 0 )
					this.playerWin	= this.player.get( j+1 ).getPlayer();
				else
					this.playerWin	= this.player.get( j-1 ).getPlayer();
				if ( this.apoCheatingHudPanel != null )
				{
					this.apoCheatingHudPanel.setReloadButton( true );
				}
				return;
			}
			else if ( ( this.player.size() == 1 ) && ( this.player.get(j ).getDetected() >= 100 ) )
			{
				this.bFinish	= true;
				this.playerWin	= -1;
				this.bThread	= false;
				this.iWin		= this.getIWin();
				//this.apoCheatingTimer.setBRunning( false );
				if ( this.apoCheatingHudPanel != null )
					this.apoCheatingHudPanel.setReloadButton( false );
				return;
			}
			for ( int i = 0; i < this.finish.size(); i++ )
			{
				if ( this.finish.get( i ).isFinish( this.player.get( j ) ) )
				{
					this.bFinish	= true;
					this.playerWin	= this.player.get( j ).getPlayer();
					this.bThread	= false;
					this.iWin		= this.getIWin();
					//this.apoCheatingTimer.setBRunning( false );
					if ( this.apoCheatingHudPanel != null )
						this.apoCheatingHudPanel.setReloadButton( true );
					return;
				}
				else if ( this.finish.get( i ).isOnFinish( this.player.get( j ) ) )
				{
					this.player.get( j ).setBFinish( true );
					break;
				}
				else
				{
					this.player.get( j ).setBFinish( false );
				}
			}
		}
	}
	
	public void run()
	{
		while ( this.bThread )
		{
			if ( !this.bFinish )
				this.think();
			//else
			//	this.end();
			try
			{
				Thread.sleep( WAIT_TIME );
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * malt das eigentliche Spielfeld mit Spielern usw.
	 */
	public void paintComponent(Graphics g) 
	{
		//long time	= System.nanoTime();
		
		super.paintComponent(g);
		
		Graphics2D gfx	= (Graphics2D)g;
		
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//VolatileImage draw	= gfx.getDeviceConfiguration().createCompatibleVolatileImage(this.getWidth(), this.getHeight());
		
		gfx.drawImage( this.iBackground, 0, 0, this );
		
		//draw.getGraphics().drawImage( draw, 0, 0, this );
		
		for ( int i = 0; i < this.player.size(); i++ )
		{
			this.player.get( i ).render( gfx );
		}
		
		for ( int i = 0; i < this.goal.size(); i++ )
		{
			this.goal.get( i ).render( gfx );
		}
		
		for ( int i = 0; i < this.extra.size(); i++ )
		{
			this.extra.get( i ).render( gfx );
		}
		
		for ( int i = 0; i < this.finish.size(); i++ )
		{
			this.finish.get( i ).render( gfx );
		}
		
		for ( int i = 0; i < this.teacher.size(); i++ )
		{
			this.teacher.get(i).render( gfx );
		}
		
		if ( this.bFinish )
			gfx.drawImage( this.iWin, 0, 0, null );
		else if ( !this.bThread )
			gfx.drawImage( this.iMessage, 0, 0, null );
		
		//System.out.println("time = "+( System.nanoTime() - time ));
	}
	
	private void drawWin( Graphics2D g )
	{
		this.drawBackground( g );

		g.drawImage( this.iPokal, this.getWidth()/2 - this.iPokal.getWidth()/2, this.getHeight()/2 - this.iPokal.getHeight()/2, this );
		g.setFont( this.fontGiant );
		g.setColor( Color.BLACK );
		String s	= this.load.getLevelName() + " " + this.load.getLevelNumber();
		int w		= g.getFontMetrics().stringWidth( s );
		g.drawString( s, this.getWidth()/2 - w/2, 115 );
		g.setFont( this.fontNormal );
		g.setColor( Color.BLUE );
		if ( this.playerWin == -1 )
		{
			s	= "You";
			w		= g.getFontMetrics().stringWidth( s );
			g.drawString( s, 365 - w, 75 );
			this.drawGrade( g );
		} else if ( this.playerWin != 0 )
		{
			if ( this.player.size() != 1 )
				s	= this.player.get( this.playerWin-1 ).getName()+"("+this.player.get( this.playerWin-1 ).getPlayer()+")";
			else
				s	= this.player.get( this.playerWin-1 ).getName()+"";
			w		= g.getFontMetrics().stringWidth( s );
			g.drawString( s, 365 - w, 75 );
			this.drawGrade( g );
		}
	}
	
	private void drawBackground( Graphics2D g )
	{
		Composite clone = g.getComposite();
		
		g.setColor( Color.BLACK );
		g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.5f ) );
		g.fillRoundRect( 20, 20, this.getWidth() - 40, this.getHeight() - 40, 15, 15 );
		g.setColor( Color.WHITE );
		g.drawRoundRect( 20, 20, this.getWidth() - 40, this.getHeight() - 40, 15, 15 );
		
		g.setComposite( clone );
	}
	
	private void drawGrade( Graphics2D g )
	{
		double grade	= 5.0D;
		String s		= "LOOSER";
		if ( this.playerWin != -1 )
		{
			if ( this.player.get( this.playerWin - 1 ).getDetected() < 5.0D ) {
				grade		= 1.0D;
				s			= "perfect";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 12.0D ) {
				grade		= 1.3D;
				s			= "very good";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 17.0D ) {
				grade		= 1.7D;
				s			= "nice";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 22.0D ) {
				grade		= 2.0D;
				s			= "good";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 30.0D ) {
				grade		= 2.3D;
				s			= "sound";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 45.0D ) {
				grade		= 2.7D;
				s			= "ok";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 60.0D ) {
				grade		= 3.0D;
				s			= "contending";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 72.0D ) {
				grade		= 3.3D;
				s			= "satisfiable";
			} else if ( this.player.get( this.playerWin - 1 ).getDetected() < 85.0D ) {
				grade		= 3.7D;
				s			= "adequate";
			} else {
				grade		= 4.0D;
				s			= "enough";
			}
		}
		
		if ( grade > 4.0D )
		{
			g.setColor( Color.RED );
			g.drawImage( this.iF, 340, 260, this );
		}
		else
		{
			g.setColor( new Color( 0, 160 - (int)( grade * 30 ), 5 ) );
			g.drawImage( this.iArrow, 340, 260, this );
		}
		g.setFont( this.fontNormal );
		g.drawString( s, 110, 400 );
		g.setFont( this.fontGiant );
		g.drawString( ""+grade, 225, 405 );
	}
	
	private void drawMessage( Graphics2D g )
	{
		if ( this.message == null )
			return;
		
		this.drawBackground( g );
		
		g.setColor( Color.WHITE );
		g.setFont( this.fontBig );
		String levelName	= this.load.getLevelName();
		int w				= g.getFontMetrics().stringWidth( levelName );
		g.drawString( levelName, this.getWidth()/2 - w/2, 50 );
		
		g.setFont( this.fontNormal );
		
		String message			= this.message;
		String currentMessage	= "";
		int i		= message.indexOf( " " );
		int y		= 0;
		int newW	= g.getFontMetrics().stringWidth( currentMessage );
		while ( i != -1 )
		{
			String newMessage = message.substring( 0, i+1 );
			message			= message.substring( i+1 );
			newW	= g.getFontMetrics().stringWidth( currentMessage + newMessage );
			
			if ( newW > 420 )
			{
				g.drawString( currentMessage, 30, 120 + y );
				currentMessage	= "";
				y	+= 25;
			}
			currentMessage	+= newMessage;
			
			i	= message.indexOf( " " );
		}
		g.drawString( currentMessage+message, 30, 120 + y );
	}
	
	/**
	 * 	 This method draws a volatile image and returns it or possibly a
	 	newly created volatile image object. Subsequent calls to this method
	 	should always use the returned volatile image.
	 	If the contents of the image is lost, it is recreated using orig.
     	img may be null, in which case a new volatile image is created.
	 */
    public VolatileImage drawVolatileImage( Graphics2D g, VolatileImage img,
    										int x, int y, Image orig) {
        final int MAX_TRIES = 100;
        for (int k=0; k<MAX_TRIES; k++) {
            if (img != null) {
                // Draw the volatile image
                //g.drawImage(img, x, y, null);
            	g.drawImage( img, 0 + x, 0 + y, this );
            	
                // Check if it is still valid
                if (!img.contentsLost()) {
                    return img;
                }
            } else {
                // Create the volatile image
                img = g.getDeviceConfiguration().createCompatibleVolatileImage(
                    orig.getWidth(null), orig.getHeight(null));
            }
    
            // Determine how to fix the volatile image
            switch (img.validate(g.getDeviceConfiguration()))
            {
            	case VolatileImage.IMAGE_OK:
            		// This should not happen
            		break;
            	case VolatileImage.IMAGE_INCOMPATIBLE:
            		// Create a new volatile image object;
            		// this could happen if the component was moved to another device
            		img.flush();
            		img = g.getDeviceConfiguration().createCompatibleVolatileImage(
            				orig.getWidth(null), orig.getHeight(null));
            	case VolatileImage.IMAGE_RESTORED:
            		// Copy the original image to accelerated image memory
            		Graphics2D gc = (Graphics2D)img.createGraphics();
            		gc.drawImage(orig, 0, 0, null);
            		gc.dispose();
            		break;
            }
        }
    
        // The image failed to be drawn after MAX_TRIES;
        // draw with the non-accelerated image
        g.drawImage(orig, x, y, null);
        return img;
    }

    private boolean makeBackground()
    {
   		this.iBackground	= new BufferedImage( this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB );
   		Graphics	gfx	= this.iBackground.getGraphics();
   		int x	= 0;
   		int y	= 0;
   		for ( int i = 0; i < this.aPlayground.length; i++ )
   		{
   			for ( int j = 0; j < this.aPlayground[0].length; j++ )
   			{
   				if ( ( this.aPlayground[i][j] == 0 ) && ( i > 0 ) && ( i < this.aPlayground.length - 1 ) &&
   						( j > 0 ) && ( j < this.aPlayground[0].length - 1) )
   					gfx.drawImage( this.aImage[0][2], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 2 * 32, 0 * 32, 3 * 32, 1 * 32, this );
   				if ( ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_UP_LEFT ) || ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_DOWN_LEFT ) )
   					gfx.drawImage( this.aImage[0][0], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 0 * 32, 0 * 32, 1 * 32, 1 * 32, this );
   				else if ( ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_UP_RIGHT ) || ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_DOWN_RIGHT ) )
   					gfx.drawImage( this.aImage[0][1], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 1 * 32, 0 * 32, 2 * 32, 1 * 32, this );
   				else if ( ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_RIGHT_LEFT ) || ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_LEFT_LEFT ) )
   					gfx.drawImage( this.aImage[0][6], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 6 * 32, 0 * 32, 7 * 32, 1 * 32, this );
   				else if ( ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_RIGHT_RIGHT ) || ( this.aPlayground[i][j] == ApoCheatingConstants.DESK_LEFT_RIGHT ) )
   					gfx.drawImage( this.aImage[1][6], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 6 * 32, 1 * 32, 7 * 32, 2 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_UP_LEFT )
   					gfx.drawImage( this.aImage[1][0], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 0 * 32, 1 * 32, 1 * 32, 2 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_UP_RIGHT )
   					gfx.drawImage( this.aImage[1][1], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 1 * 32, 1 * 32, 2 * 32, 2 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_LEFT_LEFT )
   					gfx.drawImage( this.aImage[2][6], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 6 * 32, 2 * 32, 7 * 32, 3 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_LEFT_RIGHT )
   					gfx.drawImage( this.aImage[3][6], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 6 * 32, 3 * 32, 7 * 32, 4 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_DOWN_LEFT )
   					gfx.drawImage( this.aImage[1][2], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 2 * 32, 1 * 32, 3 * 32, 2 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_DOWN_RIGHT )
   					gfx.drawImage( this.aImage[1][3], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 3 * 32, 1 * 32, 4 * 32, 2 * 32, this );
   				else if ( ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_RIGHT_LEFT ) || ( this.aPlayground[i][j] == ApoCheatingConstants.CHAIR_RIGHT_RIGHT ) )
   					gfx.drawImage( this.aImage[0][3], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 3 * 32, 0 * 32, 4 * 32, 1 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_ABOVE )
   					gfx.drawImage( this.aImage[2][2], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 2 * 32, 2 * 32, 3 * 32, 3 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_DOWN )
   					gfx.drawImage( this.aImage[3][2], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 2 * 32, 3 * 32, 3 * 32, 4 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_LEFT )
   					gfx.drawImage( this.aImage[3][3], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 3 * 32, 3 * 32, 4 * 32, 4 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_RIGHT )
   					gfx.drawImage( this.aImage[2][3], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 3 * 32, 2 * 32, 4 * 32, 3 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_LEFT_ABOVE )
   					gfx.drawImage( this.aImage[2][0], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 0 * 32, 2 * 32, 1 * 32, 3 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_RIGHT_ABOVE )
   					gfx.drawImage( this.aImage[2][1], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 1 * 32, 2 * 32, 2 * 32, 3 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_LEFT_DOWN )
   					gfx.drawImage( this.aImage[3][0], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 0 * 32, 3 * 32, 1 * 32, 4 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WALL_RIGHT_DOWN )
   					gfx.drawImage( this.aImage[3][1], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 1 * 32, 3 * 32, 2 * 32, 4 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WINDOW_LEFT_UP )
   					gfx.drawImage( this.aImage[2][4], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 2 * 32, 5 * 32, 3 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WINDOW_LEFT_DOWN )
   					gfx.drawImage( this.aImage[3][4], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 3 * 32, 5 * 32, 4 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.DOOR_RIGHT_UP )
   					gfx.drawImage( this.aImage[0][4], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 0 * 32, 5 * 32, 1 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.DOOR_RIGHT_DOWN )
   					gfx.drawImage( this.aImage[1][4], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 1 * 32, 5 * 32, 2 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WINDOW_RIGHT_UP )
   					gfx.drawImage( this.aImage[2][5], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 2 * 32, 5 * 32, 3 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.WINDOW_RIGHT_DOWN )
   					gfx.drawImage( this.aImage[3][5], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 3 * 32, 5 * 32, 4 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.DOOR_LEFT_UP )
   					gfx.drawImage( this.aImage[0][5], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 0 * 32, 5 * 32, 1 * 32, this );
   				else if ( this.aPlayground[i][j] == ApoCheatingConstants.DOOR_LEFT_DOWN )
   					gfx.drawImage( this.aImage[1][5], 0 + x, 0 + y, this );//this.iTileSheet, 0 + x, 0 + y, 32 + x, 32 + y, 4 * 32, 1 * 32, 5 * 32, 2 * 32, this );
   				x	+= 32;
   			}
   			x	= 0;
   			y	+= 32;
   		}
   		return true;
   	}
    
}
