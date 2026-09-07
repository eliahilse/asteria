package apoDefence.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceDragObject;
import apoDefence.ApoDefenceFileFilter;
import apoDefence.ApoDefenceImage;
import apoDefence.ApoDefenceLoadSave;

public class ApoDefenceEditorGame extends JComponent implements MouseMotionListener, MouseListener
{
	private static final long 	serialVersionUID = 1L;

	private int							x	= 20;
	private int							y	= 15;
	
	private int							currentWay	= -1;
	
	private BufferedImage				iBackground, iUnderground, iDefend, iUpgrade;
	
	private int[][]						aPlayground;
	
	private int							currentButton;
	
	private Point						pointDefend, pointUpgrade;
	
	private ArrayList<ApoDefenceDragObject>	enemyWay;
	
	private ApoDefenceLoadSave			loadSave;
	
	private String 						imageString;
	
	/** A FileChooser */
	private final JFileChooser 			fc = new JFileChooser();
	/** A Class file filter */
	private final ApoDefenceFileFilter	adff = new ApoDefenceFileFilter( "defence" );
	
	private ApoDefenceImage				image;
	
	private ApoDefenceEditorHud			hud;
	
	private boolean 					bWaypoint, bLeft;

	public ApoDefenceEditorGame()
	{
		super();
		
		this.fc.setCurrentDirectory( new File( System.getProperty("user.dir") + File.separator+ "levels" ) );
		this.fc.setFileFilter( this.adff );
		
		this.image					= new ApoDefenceImage();
		
		this.imageString			= "/images/underground.png";
		
		this.iUnderground			= this.image.getPic( this.imageString, false );
		this.iDefend				= this.image.getPic( "/images/tower/castle_defence.png", false );
		this.iUpgrade				= this.image.getPic( "/images/tower/castle_upgrade.png", false );
		
		this.pointDefend			= new Point( -1, -1 );
		this.pointUpgrade			= new Point( -1, -1 );
		
		this.enemyWay				= new ArrayList<ApoDefenceDragObject>();
		
		this.makeNewXAndY( 20, 15, true );
		
		this.currentButton			= ApoDefenceConstants.WAY;
		
		this.loadSave				= new ApoDefenceLoadSave();
		
		this.bLeft					= true;
		
		this.addMouseListener( this );
		this.addMouseMotionListener( this );
	}
	
	public ApoDefenceEditorHud getHud()
	{
		return this.hud;
	}

	public void setHud(ApoDefenceEditorHud hud)
	{
		this.hud = hud;
		if ( this.hud.getWaypoint() != null )
			this.hud.getWaypoint().setWaypoints( this.enemyWay );
	}
	
	protected void makeNewXAndY( int x, int y, boolean bArray )
	{
		this.x		= x;
		this.y		= y;
		
		if ( bArray )
		{
			this.aPlayground			= new int[this.y][this.x];
			
			this.pointDefend			= new Point( -1, -1 );
			this.pointUpgrade			= new Point( -1, -1 );
			
			this.enemyWay				= new ArrayList<ApoDefenceDragObject>();
			if ( this.hud != null )
			{
				this.hud.getWaypoint().setWaypoints( this.enemyWay );
			}
		}
		
		this.iBackground			= new BufferedImage( this.x * 32, this.y * 32, BufferedImage.TYPE_INT_RGB );
		
		this.makeBackground();
		
		Dimension d = new Dimension( this.x * 32, this.y * 32 );
		this.setSize( d );
        this.setPreferredSize( d );
	}
	
	private void makeBackground()
	{
		if ( this.iBackground == null )
			this.iBackground	= new BufferedImage( this.x * 32, this.y * 32, BufferedImage.TYPE_INT_RGB );
		Graphics g			= this.iBackground.getGraphics();
		for ( int i = 0; i < this.y; i++ )
		{
			for ( int j = 0; j < this.x; j++ )
			{
				if ( this.aPlayground[i][j] == ApoDefenceConstants.EMPTY )
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 0 * 32, 2 * 32, 1 * 32, null );
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.LEFT_UP )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 2 * 32, 1 * 32, 3 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32 + 23, (j+1)*32, (i)*32 + 32, 0 * 32, 0 * 32 + 18, 1 * 32, 0 * 32 + 9, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.LEFT )
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 2 * 32, 1 * 32, 3 * 32, null );
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.RIGHT )
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 2 * 32, 2 * 32, 3 * 32, 3 * 32, null );
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.LEFT_DOWN )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 2 * 32, 1 * 32, 3 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32 + 0, (j+1)*32, (i)*32 + 9, 0 * 32, 0 * 32 + 9, 1 * 32, 0 * 32 + 18, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.LEFT_RIGHT )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 2 * 32, 1 * 32, 3 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.LEFT_UP_RIGHT )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 2 * 32, 1 * 32, 3 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i)*32 + 9, 0 * 32, 0 * 32 + 9, 1 * 32, 0 * 32 + 18, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.LEFT_DOWN_RIGHT )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 2 * 32, 1 * 32, 3 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32 + 23, (j+1)*32, (i)*32 + 32, 0 * 32, 0 * 32 + 0, 1 * 32, 0 * 32 + 9, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
					
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.LEFT_DOWN_RIGHT_UP )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 2 * 32, 1 * 32, 3 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32 + 23, (j+1)*32, (i)*32 + 32, 0 * 32, 0 * 32 + 0, 1 * 32, 0 * 32 + 9, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i)*32 + 9, 0 * 32, 0 * 32 + 9, 1 * 32, 0 * 32 + 18, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.UP )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 1 * 32, 2 * 32, 2 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.DOWN )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 3 * 32, 2 * 32, 4 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.UP_DOWN )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 1 * 32, 2 * 32, 2 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i)*32 + 9, 0 * 32, 0 * 32 + 9, 1 * 32, 0 * 32 + 18, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.UP_RIGHT )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 1 * 32, 2 * 32, 2 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.UP_RIGHT_DOWN )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 1 * 32, 2 * 32, 2 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32 + 23, (j+1)*32, (i)*32 + 32, 0 * 32, 0 * 32 + 0, 1 * 32, 0 * 32 + 9, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.DOWN_RIGHT )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 3 * 32, 2 * 32, 4 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.DOWN_LEFT_UP )
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 3 * 32, 2 * 32, 4 * 32, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i)*32 + 9, 0 * 32, 0 * 32 + 9, 1 * 32, 0 * 32 + 18, null );
					g.drawImage( this.iUnderground, j*32, i*32, (j)*32 + 9, (i+1)*32, 2 * 32 + 9, 0 * 32, 2 * 32 + 18, 1 * 32, null );
				}
				else if ( this.aPlayground[i][j] == ApoDefenceConstants.SAND )
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 2 * 32, 2 * 32, 3 * 32, null );
			}
		}
		if ( this.pointDefend.x != -1 )
		{
			g.drawImage( this.iDefend, this.pointDefend.x, this.pointDefend.y, null );
		}
		if ( this.pointUpgrade.x != -1 )
		{
			g.drawImage( this.iUpgrade, this.pointUpgrade.x, this.pointUpgrade.y, null );
		}
	}
	
	private void makeArray( int x, int y, boolean bButton )
	{
		if ( bButton )
		{
			this.aPlayground[y][x]	= ApoDefenceConstants.SAND;
			if ( ( x - 1 >= 0 ) )
			{
				if ( this.aPlayground[y][x-1] == ApoDefenceConstants.EMPTY )
					this.aPlayground[y][x-1] = ApoDefenceConstants.LEFT;
				else if ( this.aPlayground[y][x-1] == ApoDefenceConstants.UP )
					this.aPlayground[y][x-1] = ApoDefenceConstants.LEFT_UP;
				else if ( this.aPlayground[y][x-1] == ApoDefenceConstants.DOWN )
					this.aPlayground[y][x-1] = ApoDefenceConstants.LEFT_DOWN;
				else if ( this.aPlayground[y][x-1] == ApoDefenceConstants.RIGHT )
					this.aPlayground[y][x-1] = ApoDefenceConstants.LEFT_RIGHT;
			}
			if ( ( x + 1 < this.x ) )
			{
				if ( this.aPlayground[y][x+1] == ApoDefenceConstants.EMPTY )
					this.aPlayground[y][x+1] = ApoDefenceConstants.RIGHT;
				else if ( this.aPlayground[y][x+1] == ApoDefenceConstants.UP )
					this.aPlayground[y][x+1] = ApoDefenceConstants.UP_RIGHT;
				else if ( this.aPlayground[y][x+1] == ApoDefenceConstants.DOWN )
					this.aPlayground[y][x+1] = ApoDefenceConstants.DOWN_RIGHT;
				else if ( this.aPlayground[y][x+1] == ApoDefenceConstants.LEFT )
					this.aPlayground[y][x+1] = ApoDefenceConstants.LEFT_RIGHT;
			}
			if ( ( y - 1 >= 0 ) )
			{
				if ( this.aPlayground[y-1][x] == ApoDefenceConstants.EMPTY )
					this.aPlayground[y-1][x] = ApoDefenceConstants.UP;
				else if ( this.aPlayground[y-1][x] == ApoDefenceConstants.DOWN )
					this.aPlayground[y-1][x] = ApoDefenceConstants.UP_DOWN;
				else if ( this.aPlayground[y-1][x] == ApoDefenceConstants.LEFT )
					this.aPlayground[y-1][x] = ApoDefenceConstants.LEFT_UP;
				else if ( this.aPlayground[y-1][x] == ApoDefenceConstants.RIGHT )
					this.aPlayground[y-1][x] = ApoDefenceConstants.UP_RIGHT;
			}
			if ( ( y + 1 < this.y ) )
			{
				if ( this.aPlayground[y+1][x] == ApoDefenceConstants.EMPTY )
					this.aPlayground[y+1][x] = ApoDefenceConstants.DOWN;
				else if ( this.aPlayground[y+1][x] == ApoDefenceConstants.UP )
					this.aPlayground[y+1][x] = ApoDefenceConstants.UP_DOWN;
				else if ( this.aPlayground[y+1][x] == ApoDefenceConstants.LEFT )
					this.aPlayground[y+1][x] = ApoDefenceConstants.LEFT_DOWN;
				else if ( this.aPlayground[y+1][x] == ApoDefenceConstants.RIGHT )
					this.aPlayground[y+1][x] = ApoDefenceConstants.DOWN_RIGHT;
			}
			this.makeBackground();
		} else
		{
			this.aPlayground[y][x]	= ApoDefenceConstants.EMPTY;
			
			this.makeBackground();
		}
	}
	
	private void makeCastleDefend( int x, int y )
	{
		this.pointDefend	= new Point( x, y );
		this.makeBackground();
		this.repaint();
	}
	
	private void makeCastleUpgrade( int x, int y )
	{
		this.pointUpgrade	= new Point( x, y );
		this.makeBackground();
		this.repaint();
	}
	
	protected void setButton( int button )
	{
		this.currentButton		= button;
	}
	
	protected void setLoad()
	{
		int p = this.fc.showOpenDialog(this);
		if(p == 0)
		{
			String s = this.fc.getSelectedFile().getPath();
			this.loadSave.readLevel( s );

			this.x				= this.loadSave.getX();
			this.y				= this.loadSave.getY();
			this.aPlayground	= this.loadSave.getAPlayground();
			this.pointDefend	= this.loadSave.getPointDefend();
			this.pointUpgrade	= this.loadSave.getPointUpgrade();
			this.enemyWay		= this.loadSave.getEnemyWay();
			this.hud.getWaypoint().setWaypoints( this.enemyWay );
			this.currentWay		= 0;
			this.hud.getWaypoint().setCurrentWaypoint( this.currentWay );
			
			this.hud.setEnemies( this.loadSave.getEnemies() );
			if ( !this.imageString.equals( this.loadSave.getImageString() ) )
			{
				this.imageString	= this.loadSave.getImageString();
				this.iUnderground	= this.image.getPic( this.imageString, false );
			}
			
			this.iBackground	= null;
			this.makeNewXAndY( this.x, this.y, false );
			
			this.repaint();
		}
	}
	
	protected void setSave()
	{
		int p = this.fc.showSaveDialog(this);
		if(p == 0)
		{
			String s = this.fc.getSelectedFile().getPath();
			int t = s.indexOf(46);
			if ( t != -1 )
			{
				s	= s.substring( 0, t );
			}
			s	+= this.adff.getLevelName();
			this.loadSave.setAll( this.x, this.y, this.aPlayground, this.pointDefend, this.pointUpgrade, this.enemyWay, this.imageString, this.hud.getMoney(), this.hud.getTowerCount(), this.hud.getWaves(), this.hud.getEnemies() );
			this.loadSave.writeLevel( s );
		}
	}
	
	public ApoDefenceLoadSave getLoadSave()
	{
		return this.loadSave;
	}
	
	public void mouseDragged(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		if ( this.bWaypoint )
		{		
			for ( int i = 0; i < this.enemyWay.size(); i++ )
			{
				if ( 	( this.enemyWay.get( i ).isBSelected() ) &&
						( this.enemyWay.get( i ).isBPressed() ) &&
						( this.enemyWay.get( i ).getNewX( x ) > 0 ) && 
						( this.enemyWay.get( i ).getNewY( y ) > 0 ) &&
						( this.enemyWay.get( i ).getNewX( x ) < this.x * 32 ) &&
						( this.enemyWay.get( i ).getNewY( y ) < this.y * 32 ) )
				{
					this.enemyWay.get( i ).setDragX( x );
					this.enemyWay.get( i ).setDragY( y );
					this.hud.getWaypoint().setCurrentWaypoint( this.currentWay );
					this.repaint();
					break;
				}
			}
		} else if ( this.currentButton == ApoDefenceConstants.WAY )
		{
			x		= x/32;
			y		= y/32;
			if ( this.bLeft )
				this.makeArray( x, y, true );
			else if ( !this.bLeft )
				this.makeArray( x, y, false );
			this.repaint();
		}
	}

	public void mouseMoved(MouseEvent e)
	{
		if ( this.bWaypoint )
		{
			int x	= e.getX();
			int y	= e.getY();
		
			for ( int i = 0; i < this.enemyWay.size(); i++ )
			{
				if ( this.enemyWay.get( i ).getMove( x, y ) )
				{
					this.repaint();
					break;
				}
			}
		}
	}

	public void mouseClicked(MouseEvent arg0) {}

	public void mousePressed(MouseEvent e)
	{
		if ( this.bWaypoint )
		{
			int x	= e.getX();
			int y	= e.getY();
		
			for ( int i = 0; i < this.enemyWay.size(); i++ )
			{
				if ( this.enemyWay.get( i ).getPressed( x, y) )
				{
					for ( int j = 0; j < this.enemyWay.size(); j++ )
					{
						if ( ( this.enemyWay.get( j ).isBSelected() ) && ( i != j ) )
						{
							this.enemyWay.get( j ).setBSelected( false );
							break;
						}
					}
					this.currentWay		= i;
					this.hud.getWaypoint().setCurrentWaypoint( this.currentWay );
					this.repaint();
					break;
				}
			}
		} else
		{
			if ( e.getButton() == MouseEvent.BUTTON1 )
				this.bLeft	= true;
			else
				this.bLeft	= false;
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();
		
		if ( !this.bWaypoint )
		{
			if ( this.currentButton == ApoDefenceConstants.WAY )
			{
				x		= x/32;
				y		= y/32;
				if ( e.getButton() == MouseEvent.BUTTON1 )
					this.makeArray( x, y, true );
				else if ( e.getButton() == MouseEvent.BUTTON3 )
					this.makeArray( x, y, false );
				this.repaint();
			} else if ( this.currentButton == ApoDefenceConstants.CASTLE_DEFEND )
			{
				if ( e.getButton() == MouseEvent.BUTTON1 )
					this.makeCastleDefend( x, y );
				else if ( e.getButton() == MouseEvent.BUTTON3 )
				{
					if ( 	( this.pointDefend.x != -1 ) &&
							( x > this.pointDefend.x ) && ( x < this.pointDefend.x + this.iDefend.getWidth() ) &&
							( y > this.pointDefend.y ) && ( y < this.pointDefend.y + this.iDefend.getHeight() ) )
						this.makeCastleDefend( -1, -1 );
				}
			} else if ( this.currentButton == ApoDefenceConstants.CASTLE_UPGRADE )
			{
				if ( e.getButton() == MouseEvent.BUTTON1 )
					this.makeCastleUpgrade( x, y );
				else if ( e.getButton() == MouseEvent.BUTTON3 )
				{
					if ( 	( this.pointUpgrade.x != -1 ) &&
							( x > this.pointUpgrade.x ) && ( x < this.pointUpgrade.x + this.iUpgrade.getWidth() ) &&
							( y > this.pointUpgrade.y ) && ( y < this.pointUpgrade.y + this.iUpgrade.getHeight() ) )
						this.makeCastleUpgrade( -1, -1 );
				}
			}
		} else
		{
			for ( int i = 0; i < this.enemyWay.size(); i++ )
			{
				this.enemyWay.get( i ).getReleased( x, y );
			}
			this.repaint();
		}
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}
	
	public void makeWayPoint( int x, int y, int width, int height )
	{
		if ( ( x > -1 ) && ( y > -1 ) )
			this.enemyWay.add( new ApoDefenceDragObject( x, y, width, height ) );
		if ( this.enemyWay.size() == 1 )
		{
			this.enemyWay.get( 0 ).setBSelected( true );
			this.hud.getWaypoint().setCurrentWaypoint( 0 );
		}
		this.repaint();
	}
	
	public void deleteWayPoint( int i )
	{
		if ( this.currentWay == i )
		{
			this.currentWay	= i - 1;
			if ( ( this.currentWay < 0 ) && ( this.enemyWay.size() > 0 ) )
				this.currentWay	= this.enemyWay.size() - 1;
			this.hud.getWaypoint().setCurrentWaypoint( this.currentWay );
		}
		this.enemyWay.remove( i );
	}
	
	public void setCurrentWay( int currentWay )
	{
		if ( currentWay == -1 )
			return;
		this.currentWay		= currentWay;
		for ( int j = 0; j < this.enemyWay.size(); j++ )
		{
			if ( this.enemyWay.get( j ).isBSelected() )
			{
				this.enemyWay.get( j ).setBSelected( false );
				break;
			}
		}
		this.enemyWay.get( this.currentWay ).setBSelected( true );
		this.repaint();
	}
	
	public boolean isBWaypoint()
	{
		return this.bWaypoint;
	}

	public void setBWaypoint(boolean bWaypoint)
	{
		this.bWaypoint = bWaypoint;
	}
	
	public Point getPointDefend()
	{
		return this.pointDefend;
	}

	public Point getPointUpgrade()
	{
		return this.pointUpgrade;
	}

	public int getXLevel()
	{
		return this.x;
	}
	
	public int getYLevel()
	{
		return this.y;
	}
	
	/**
	 * malt das eigentliche Spielfeld mit Spielern usw.
	 */
	public void paintComponent(Graphics gfx) 
	{
		super.paintComponent(gfx);

		Graphics2D g	= (Graphics2D)gfx;		
		
		g.drawImage( this.iBackground, 0, 0, this );
		
		if ( this.bWaypoint )
		{
			g.setColor( Color.BLACK );
			for ( int i = 0; i < this.enemyWay.size(); i++ )
			{
				g.drawString( ""+(i+1), (int)this.enemyWay.get( i ).getX(), (int)this.enemyWay.get( i ).getY() - 5 );
				this.enemyWay.get( i ).render( g );
			}
		}
	}


}
