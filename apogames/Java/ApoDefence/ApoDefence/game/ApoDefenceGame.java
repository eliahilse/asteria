package apoDefence.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceDragObject;
import apoDefence.ApoDefenceImage;
import apoDefence.ApoDefenceLoadSave;
import apoDefence.ApoDefenceMain;
import apoDefence.ApoDefenceTimer;

/**
 * wichtigste Klasse, die alles im Spiel zusammenhält
 * @author Dirk Aporius
 *
 */
public class ApoDefenceGame extends JComponent implements KeyListener, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;

	private int							x	= 20;
	private int							y	= 15;
	private double						currentX	= 0;
	private double						currentY	= 0;
	private int							money;
	private float						points;
	private int							tower;
	private int							maxTower;
	private int							maxWaves, waves;
	
	private boolean						bPulling;
	private int							pullingX;	
	private int							pullingY;
	
	private boolean						bUpload;
	private boolean						bHealthVisibleTower;
	private boolean						bHealthVisibleEnemy;
	
	private BufferedImage				iBackground, iUnderground, iDefend, iUpgrade,
										iMiniMap, iMenu, iWin, iGameOver, iStage,
										iNextWave, iQuest, iHighscore;
	
	private ApoDefenceResearch			research;
	private ApoDefenceCastle			castle;
	
	private int[][]						aPlayground;
	
	private ApoDefenceHighscore			highscore;
	
	private boolean						bKeyPressed;
	private boolean						bMenu;
	private boolean						bWin;
	private boolean						bLoose;
	private boolean						bNextWave;
	private boolean 					bQuest;
	private boolean 					bRestart;
	private long						nextWaveTime;
	private long						pauseTime;
	private int							keyCode;
	private ApoDefenceTimer				defenceTimer;
	private Timer						timer;
	
	private boolean						bMouseCorner;
	private int							mouseX;
	private int							mouseY;
	
	private int							miniMapX	= 0;
	private int							miniMapY	= 0;
	
	private Point						pointDefend, pointUpgrade;
	
	private ArrayList<ApoDefenceDragObject>	enemyWay;
	
	private ApoDefenceLoadSave			loadSave;
	
	private String 						imageString;
	
	/** A FileChooser */
	private final JFileChooser 			fc = new JFileChooser();
	/** A Class file filter */
	private final ApoDefenceFileFilter	adff = new ApoDefenceFileFilter( "defence" );
	
	private ApoDefenceImage				image;
	
	private ApoDefenceGameHud			apoDefenceGameHud;
	
	private Font						fontBig;
	private Font						fontVeryBig;
	
	private ApoDefenceButton[]			buttons;
	
	private ApoDefenceGameTower			gameTower;
	private ApoDefenceGameEnemy			gameEnemy;
	
	private ApoDefenceMain				main;
	
	private JTextField					nicknameField;
	
	public ApoDefenceGame(ApoDefenceMain main )
	{
		super();
		
		this.main					= main;
		
		this.setLayout( null );
		
		this.fc.setCurrentDirectory( new File( System.getProperty("user.dir") + File.separator+ "levels" ) );
		this.fc.setFileFilter( this.adff );
		
		this.loadSave				= new ApoDefenceLoadSave();
		this.imageString			= "";
		
		this.image					= new ApoDefenceImage();
		
		this.iDefend				= this.image.getPic( "/images/tower/castle_defence.png", false );
		this.iUpgrade				= this.image.getPic( "/images/tower/castle_upgrade.png", false );
		this.iMenu					= this.image.getPic( "/images/hud_gamemenu.png", false );
		this.iWin					= this.image.getPic( "/images/victory.png", false );
		this.iGameOver				= this.image.getPic( "/images/game_over.png", false );
		this.iStage					= this.image.getPic( "/images/stage.png", false );
		this.iNextWave				= this.image.getPic( "/images/nextWave.png", false );
		this.iQuest					= this.image.getPic( "/images/quest_new.png", false );
		this.iHighscore				= this.image.getPic( "/images/hud_highscore.png", false );
		
		this.buttons	= new ApoDefenceButton[4];
		this.buttons[0]	= new ApoDefenceButton( image.getPic( "/images/button/button_restart.png" , false), 	    240, 165, 162, 24, "Restart" );
		this.buttons[0].setBVisible( false );
		this.buttons[1]	= new ApoDefenceButton( image.getPic( "/images/button/button_exit_to_menu.png" , false), 	240, 245, 162, 24, "ExitToMenu" );
		this.buttons[1].setBVisible( false );
		this.buttons[2]	= new ApoDefenceButton( image.getPic( "/images/button/button_return_to_game.png" , false), 	240, 325, 162, 24, "ReturnToGame" );
		this.buttons[2].setBVisible( false );
		this.buttons[3]	= new ApoDefenceButton( image.getPic( "/images/button/button_upload.png" , false),			475, 305, 114, 25, "Upload" );
		this.buttons[3].setBVisible( false );
		
		this.nicknameField	= new JTextField("You");
		this.nicknameField.setSize( 60, 20 );
		this.nicknameField.setLocation( 160, 308 );
		this.nicknameField.setVisible( false );
		
		this.add( this.nicknameField );
		
		this.money					= 0;
		this.points					= 0;
		this.tower					= 0;
		this.maxTower				= 0;
		this.maxWaves				= ApoDefenceConstants.MAX_WAVE;
		this.waves					= 1;
		this.nextWaveTime			= -1;
		this.pauseTime				= -1;
		
		this.apoDefenceGameHud		= new ApoDefenceGameHud( this );
		this.gameTower				= new ApoDefenceGameTower( this );
		this.gameEnemy				= new ApoDefenceGameEnemy( this );
		
		this.setLoad( System.getProperty("user.dir") + File.separator + "levels" + File.separator + "Original_First.defence" );
		
		this.bKeyPressed			= false;
		this.keyCode				= -1;
		
		this.bMouseCorner			= false;
		this.mouseX					= -1;
		this.mouseY					= -1;
		
		this.bWin					= false;
		this.bLoose					= false;
		this.bNextWave				= true;
		this.bQuest					= false;
		this.bHealthVisibleTower	= false;
		this.bHealthVisibleEnemy	= false;
		this.bPulling				= false;
		
		this.pullingX				= -1;
		this.pullingY				= -1;
		
		this.fontBig				= new Font( "Dialog", Font.BOLD, 17 );
		this.fontVeryBig			= new Font( "Dialog", Font.BOLD, 33 );
		this.bMenu					= false;
		
		this.addMouseListener( this );
		this.addMouseMotionListener( this );
	}
	
	/**
	 * setzt alles wichtige neu auf die übergebenen Werte
	 * @param x: x-Größe des Spielfeldes
	 * @param y: y-Größe des Spielfeldes
	 * @param bArray: true alles neu erstellen, sonst nur Image
	 */
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
		}
		
		this.iBackground			= new BufferedImage( this.x * 32, this.y * 32, BufferedImage.TYPE_INT_RGB );
		
		this.makeBackground();
	}
	
	/**
	 * erstellt das Hintergrundbild aus dem Spielfeld
	 */
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
				{
					g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 1 * 32, 0 * 32, 2 * 32, 1 * 32, null );
					int random		= (int)( Math.random() * 25 );
					if ( random == 7 )
						g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 1 * 32, 1 * 32, 2 * 32, null );
					else if ( random == 15 )
						g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 2 * 32, 1 * 32, 3 * 32, 2 * 32, null );
					else if ( random == 19 )
						g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 0 * 32, 3 * 32, 1 * 32, 4 * 32, null );
					else if ( random == 23 )
						g.drawImage( this.iUnderground, j*32, i*32, (j+1)*32, (i+1)*32, 2 * 32, 3 * 32, 3 * 32, 4 * 32, null );
				}
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
	
	/**
	 * öffnet den Laden Dialog und lädt das Level
	 * @param level: falls NULL öffne den OpenDialog sonst öffne den übergebenen String
	 */
	public void setLoad( String level )
	{
		int p	= 0;
		if ( level == null )
			p = this.fc.showOpenDialog(this);
		if(p == 0)
		{
			String s = "";
			if ( level == null )
			{
				s	= this.fc.getSelectedFile().getPath();
			} else
			{
				s	= level;
			}
			this.loadSave.readLevel( s );

			ApoDefenceConstants.MONEY		= this.loadSave.getMoney();
			ApoDefenceConstants.MAX_TOWERS	= this.loadSave.getTowerCount();
			ApoDefenceConstants.MAX_WAVE	= this.loadSave.getWaves();
			
			this.restart();
		}
	}
	
	/**
	 * wird alle 25 Millisekunden aufgerufen und lässt die Gegner gehen, 
	 * die Türme feuern, scrollen usw. 
	 */
	public void think()
	{
		//long time	= System.nanoTime();
		if ( ( this.bMenu ) || ( this.bQuest ) || ( this.bRestart ) )
		{
			return;
		}
		else if ( this.bNextWave )
		{
			this.gameTower.think( this );
			this.castle.think( this );
			this.research.think( this );
			long time		= TimeUnit.NANOSECONDS.toSeconds( System.nanoTime() - this.nextWaveTime );
			if ( time > ApoDefenceConstants.WAVE_TIME )
			{
				this.bNextWave	= false;
				this.nextWaveTime	= -1;
				this.gameEnemy.makeNewWave( this.waves );
			}
		} else if ( this.castle.getHealth() < 1 )
		{
			this.bUpload	= true;
			this.bLoose		= true;
			this.highscore	= new ApoDefenceHighscore( this.loadSave.getLevel() );
			if ( ( this.highscore.canConnect() ) && ( this.main.isOriginal() ) )
			{
				this.nicknameField.setVisible( true );
				this.nicknameField.requestFocus( true );
				this.buttons[3].setBVisible( true );
				this.highscore.makeEntity();
				this.highscore.addEntity( this.nicknameField.getText(), this.loadSave.getLevel(), this.waves, (int)this.points );
			} else
			{
				this.highscore.setBConnect( false );
			}
		} else if ( this.gameEnemy.getAllEnemy().size() < 1 )
		{
			if ( this.waves + 1 <= this.maxWaves )
			{
				this.points			+= 100 * this.waves;
				this.setMoney( (int)((float)this.getMoney() + (float)this.waves * 5.0f * ApoDefenceConstants.DIFFICULTY + 30.0f ) );
				this.waves			+= 1;
				this.bNextWave		= true;
				this.nextWaveTime	= System.nanoTime();
			} else
			{
				this.bUpload	= true;
				this.points		+= 100 * this.waves;
				this.bWin		= true;
				this.highscore	= new ApoDefenceHighscore( this.loadSave.getLevel() );
				if ( ( this.highscore.canConnect() ) && ( this.main.isOriginal() ) )
				{
					this.nicknameField.setVisible( true );
					this.nicknameField.requestFocus( true );
					this.buttons[3].setBVisible( true );
					this.highscore.makeEntity();
					this.highscore.addEntity( this.nicknameField.getText(), this.loadSave.getLevel(), this.waves, (int)this.points );
				} else
				{
					//System.out.println( "Check" );
					this.highscore.setBConnect( false );
				}
			}
		}
		else
		{
			this.gameTower.think( this );
			this.gameEnemy.think();
			this.castle.think( this );
			this.research.think( this );
		}
		if ( this.bKeyPressed )
		{
			if ( this.keyCode == KeyEvent.VK_LEFT )
			{
				if ( this.currentX >= 4 )
					this.currentX	= this.currentX - 4;
			} else if ( this.keyCode == KeyEvent.VK_RIGHT )
			{
				if ( this.currentX < this.getXValue() * 32 - 640 - 3 )
					this.currentX	= this.currentX + 4;
			} else if ( this.keyCode == KeyEvent.VK_UP )
			{
				if ( this.currentY >= 4 )
					this.currentY	= this.currentY - 4;
			} else if ( this.keyCode == KeyEvent.VK_DOWN )
			{
				if ( this.currentY < this.getYValue() * 32 - 342 - 3 )
					this.currentY	= this.currentY + 4;
			}
		}
		if ( this.bMouseCorner )
		{
			if ( this.mouseX < 20 )
			{
				if ( this.currentX >= 4 )
					this.currentX	= this.currentX - 4;
			} else if ( this.mouseX > 620 )
			{
				if ( this.currentX < this.x * 32 - 640 - 3 )
					this.currentX	= this.currentX + 4;
			} else if ( this.mouseY < 54 )
			{
				if ( this.currentY >= 4 )
					this.currentY	= this.currentY - 4;
			} else if ( this.mouseY > 354 )
			{
				if ( this.currentY < this.y * 32 - 342 - 3 )
					this.currentY	= this.currentY + 4;
			}
		}
		//System.out.println( "time = "+( System.nanoTime() - time ) );
	}
	
	public void keyTyped(KeyEvent arg0) {}

	public void keyPressed(KeyEvent e)
	{
		int keyCode	= e.getKeyCode();
		if ( this.bNextWave )
		{
			if ( keyCode == KeyEvent.VK_SPACE )
			{
				this.nextWaveTime	= 0;
			}
		}
		if ( ( !this.isBMenu() ) && ( !this.bQuest ) )
		{
			if ( keyCode == KeyEvent.VK_M )
			{
				this.setBMenu( true );
			} else if ( keyCode == KeyEvent.VK_Q )
			{
				this.setBQuest( true );
			} else if ( keyCode == KeyEvent.VK_R )
			{
				this.research.setBSelect( true );
				this.castle.setBSelect( false );
				this.apoDefenceGameHud.setBResearch( true );
			} else if ( keyCode == KeyEvent.VK_C )
			{
				this.research.setBSelect( false );
				this.castle.setBSelect( true );
				this.apoDefenceGameHud.setBCastle( true );
			} else if ( this.apoDefenceGameHud.isDeselect() )
			{
				if ( keyCode == KeyEvent.VK_1 )
					this.apoDefenceGameHud.setBuildArch();
				else if ( keyCode == KeyEvent.VK_2 )
					this.apoDefenceGameHud.setBuildFire();
				else if ( keyCode == KeyEvent.VK_3 )
					this.apoDefenceGameHud.setBuildIce();
				else if ( keyCode == KeyEvent.VK_4 )
					this.apoDefenceGameHud.setBuildLight();
				else if ( keyCode == KeyEvent.VK_5 )
					this.apoDefenceGameHud.setBuildUltra();
			} else if ( this.apoDefenceGameHud.isBResearch() )
			{
				if ( keyCode == KeyEvent.VK_T )
					this.apoDefenceGameHud.setTechLevelUp();
				else if ( keyCode == KeyEvent.VK_S )
					this.apoDefenceGameHud.setSpeedLevelUp();
				else if ( keyCode == KeyEvent.VK_A )
					this.apoDefenceGameHud.setArmorLevelUp();
				else if ( keyCode == KeyEvent.VK_P )
					this.apoDefenceGameHud.setArmorPiercingLevelUp();
			} else if ( this.apoDefenceGameHud.isTower() )
			{
				if ( keyCode == KeyEvent.VK_U )
					this.apoDefenceGameHud.setUpgrade();
				else if ( keyCode == KeyEvent.VK_R )
					this.apoDefenceGameHud.setRepair();
				else if ( keyCode == KeyEvent.VK_S )
					this.apoDefenceGameHud.setSell();
			} else if ( this.apoDefenceGameHud.isBCastle() )
			{
				if ( keyCode == KeyEvent.VK_U )
					this.apoDefenceGameHud.setUpgrade();
			}
		} else if ( this.isBMenu() )
		{
			if ( keyCode == KeyEvent.VK_S )
			{
				this.restart();
			} else if ( keyCode == KeyEvent.VK_E )
			{
				this.main.setOptions();
			} else if ( keyCode == KeyEvent.VK_R )
			{
				this.setBMenu( false );
			}
		} else if ( this.bQuest )
		{
			if ( keyCode == KeyEvent.VK_R )
			{
				this.setBQuest( false );
			}
		}
		if ( !this.bKeyPressed )
		{
			if ( 	( keyCode == KeyEvent.VK_LEFT ) ||
					( keyCode == KeyEvent.VK_RIGHT ) ||
					( keyCode == KeyEvent.VK_UP ) ||
					( keyCode == KeyEvent.VK_DOWN ) )
			{
				this.bKeyPressed	= true;
				this.keyCode		= keyCode;
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int keyCode		= e.getKeyCode();
		if ( keyCode == KeyEvent.VK_H )
		{
			this.bHealthVisibleTower	= !this.bHealthVisibleTower;
			this.gameTower.setHealthVisible( this.bHealthVisibleTower );
		} else if ( keyCode == KeyEvent.VK_G )
		{
			this.bHealthVisibleEnemy	= !this.bHealthVisibleEnemy;
			this.gameEnemy.setHealthVisible( this.bHealthVisibleEnemy );
		}
		if ( this.bKeyPressed )
		{
			if ( 	( keyCode == KeyEvent.VK_LEFT ) ||
					( keyCode == KeyEvent.VK_RIGHT ) ||
					( keyCode == KeyEvent.VK_UP ) ||
					( keyCode == KeyEvent.VK_DOWN ) )
			{
				this.bKeyPressed	= false;
			}
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		if ( this.bPulling )
		{
			int x	= e.getX();
			int y	= e.getY();
			if ( ( x != this.pullingX ) || ( y != this.pullingY ) )
			{
				this.currentX	+= this.pullingX - x;
				this.currentY	+= this.pullingY - y;
				this.pullingX	= x;
				this.pullingY	= y;
				
				if ( this.currentX < 0 )
					this.currentX	= 0;
				if ( this.currentX >= this.x * 32 - 640 )
					this.currentX	= this.x * 32 - 640;
				if ( this.currentY < 0 )
					this.currentY	= 0;
				if ( this.currentY >= this.y * 32 - 342 )
					this.currentY	= this.y * 32 - 342;
			}
		}
	}

	public void mouseMoved(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();

		this.mouseX			= x;
		this.mouseY			= y;
		
		if ( ( this.bMenu ) || ( this.bQuest ) || ( this.bWin ) || ( this.bLoose ) )
		{
			for ( int i = 0; i < this.buttons.length; i++ )
			{
				if ( this.buttons[i].getMove( x, y ) )
				{
					return;
				}
			}
			if ( ( this.bWin ) || ( this.bLoose ) )
			{
				this.bMouseCorner	= false;
				this.apoDefenceGameHud.mouseMoved( x, y );
			}
		} else if ( this.gameTower.mouseMoved( x + (int)this.currentX, y + (int)this.currentY - 33 ) )
		{
			//System.out.println( "TowerOk" );
		} else if ( ( y <= 45 ) || ( y >= 360 ) )
		{
			this.bMouseCorner	= false;
			this.apoDefenceGameHud.mouseMoved( x, y );
		} else
		{
			this.apoDefenceGameHud.setOver( false );
		}
		if ( ( x < 20 ) && 
			 ( y > 33 ) &&
			 ( y < 375 ) )
		{
			this.bMouseCorner	= true;
		} else if (	( x > 620 ) && 
					( y > 33 ) &&
					( y < 375 ) )
		{
			this.bMouseCorner	= true;
		} else if ( ( y > 0 ) &&
					( y < 20 ) )
		{
			this.bMouseCorner	= true;
		} else if ( ( y > 460 ) &&
					( y < 480 ) )
		{
			this.bMouseCorner	= true;
		} else
		{
			this.bMouseCorner	= false;
		}
	}

	public void mouseClicked(MouseEvent arg0) {}

	public void mousePressed(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();

		if ( ( this.bMenu ) || ( this.bQuest ) || ( this.bWin ) || ( this.bLoose ) )
		{
			for ( int i = 0; i < this.buttons.length; i++ )
			{
				if ( this.buttons[i].getPressed( x, y ) )
				{
					return;
				}
			}
			if ( ( this.bWin ) || ( this.bLoose ) )
			{
				this.bMouseCorner	= false;
				this.apoDefenceGameHud.mouseMoved( x, y );
			}
		} else if ( ( y <= 33 ) || ( y >= 375 ) )
		{
			this.apoDefenceGameHud.mousePressed( x, y );
		} else
		{
			this.bPulling	= true;
			this.pullingX	= x;
			this.pullingY	= y;
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		int x	= e.getX();
		int y	= e.getY();

		this.bPulling	= false;
		this.pullingX	= -1;
		this.pullingY	= -2;
		Rectangle2D.Double rec = new Rectangle2D.Double( x + this.currentX, y + this.currentY - 33, 1, 1 );
		
		if ( e.getButton() == MouseEvent.BUTTON3 )
		{
			this.apoDefenceGameHud.deselectAll();
			this.gameTower.setBuild( ApoDefenceConstants.EMPTY );
		} else if ( ( this.bMenu ) || ( this.bQuest ) )
		{
			for ( int i = 0; i < this.buttons.length; i++ )
			{
				if ( this.buttons[i].getReleased( x, y ) )
				{
					String function	= this.buttons[i].getFunction();
					if ( "Restart".equals( function ) )
					{
						this.nicknameField.setVisible( false );
						this.nicknameField.requestFocus( false );
						this.requestFocus();
						this.restart();
					} else if ( "ExitToMenu".equals( function ) )
					{
						this.main.setOptions();
					} else if ( "ReturnToGame".equals( function ) )
					{
						if ( this.bMenu )
							this.setBMenu( false );
						else if ( this.bQuest )
							this.setBQuest( false );
						if ( ( this.isBWin() ) && ( this.bUpload ) && ( this.highscore.isBConnect() ) )
						{
							this.nicknameField.setVisible( true );
							this.nicknameField.requestFocus( true );
							this.buttons[3].setBVisible( true );
						}
					}
				}
			}
		} else if ( ( this.bWin ) || ( this.bLoose ) )
		{
			if ( this.buttons[3].getReleased( x, y ) )
			{
				String function	= this.buttons[3].getFunction();
				if ( "Upload".equals( function ) )
				{
					this.highscore.send( this.nicknameField.getText(), this.loadSave.getLevel(), this.waves, (int)this.points );
					this.buttons[3].setBVisible( false );
					this.nicknameField.setVisible( false );
					this.nicknameField.requestFocus( false );
					this.main.requestFocus();
					this.bUpload	= false;
				}
			} else
				this.setBMenu( true );
		} else if ( ( y <= 25 ) || ( y >= 360 ) )
		{
			this.apoDefenceGameHud.mouseReleased( x, y );
		} else if ( ( y > 33 ) &&
				    ( y < 360 ) &&
				    ( this.gameTower.mouseReleased( x + (int)this.currentX, y + (int)this.currentY - 33 ) ) )
		{
			ApoDefenceTower tower	= this.gameTower.getChoosenTower();
			if ( tower != null )
			{
				tower.setBSelect( true );
				this.apoDefenceGameHud.setTower( tower );
			}
		} else if ( ( y > 33 ) &&
	    			( y < 360 ) &&
	    			( this.gameEnemy.mouseReleased( x + (int)this.currentX, y + (int)this.currentY - 33 ) ) )
		{
			ApoDefenceEnemy enemy	= this.gameEnemy.getChoosenEnemy();
			if ( enemy != null )
			{
				enemy.setBSelect( true );
				this.apoDefenceGameHud.setEnemy( enemy );
			}
		} else if ( this.research.intersects( rec ) )
		{
			this.research.setBSelect( true );
			this.castle.setBSelect( false );
			this.apoDefenceGameHud.setBResearch( true );
		} else if ( this.castle.intersects( rec ) )
		{
			this.research.setBSelect( false );
			this.castle.setBSelect( true );
			this.apoDefenceGameHud.setBCastle( true );
		} else
		{
			this.apoDefenceGameHud.deselectAll();
		}
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}
	
	/**
	 * gibt zurück, ob das Menu aufgerufen ist oder nicht
	 * @return gibt zurück, ob das Menu aufgerufen ist oder nicht
	 */
	public boolean isBMenu()
	{
		return this.bMenu;
	}

	/**
	 * 
	 * @param bMenu
	 */
	public void setBMenu(boolean bMenu)
	{
		this.bMenu = bMenu;
		if ( this.nicknameField.isVisible() )
		{
			this.nicknameField.setVisible( false );
			this.nicknameField.requestFocus( false );
			this.buttons[3].setBVisible( false );
			this.main.requestFocus();
		}
		for ( int i = 0; i < 3; i++ )
			this.buttons[i].setBVisible( bMenu );
		if ( this.bMenu )
		{
			this.buttons[2].setX( 240 );
			this.buttons[2].setY( 325 );
			this.pauseTime	= System.nanoTime();
		} else
		{
			this.pauseTime	= System.nanoTime() - this.pauseTime;
			if ( this.bNextWave )
				this.nextWaveTime	+= this.pauseTime;
			if ( this.gameEnemy.getAllEnemy().size() > 0 )
			{
				for ( int i = 0; i < this.gameEnemy.getAllEnemy().size(); i++ )
				{
					this.gameEnemy.getAllEnemy().get( i ).setStartTime( this.gameEnemy.getAllEnemy().get( i ).getStartTime() + this.pauseTime );  
				}
			}
		}
	}
	
	public void setBQuest( boolean bQuest )
	{
		this.bQuest	= bQuest;
		this.buttons[2].setBVisible( this.bQuest );
		if ( this.nicknameField.isVisible() )
		{
			this.nicknameField.setVisible( false );
			this.nicknameField.requestFocus( false );
			this.buttons[3].setBVisible( false );
			this.main.requestFocus();
		}
		if ( this.bQuest )
		{
			this.buttons[2].setX( 300 );
			this.buttons[2].setY( 290 );
			this.pauseTime	= System.nanoTime();
		} else
		{
			this.pauseTime	= System.nanoTime() - this.pauseTime;
			if ( this.bNextWave )
				this.nextWaveTime	+= this.pauseTime;
		}
	}
	
	public ApoDefenceResearch getResearch()
	{
		return this.research;
	}
	
	public ApoDefenceCastle getCastle()
	{
		return this.castle;
	}
	
	public int[][] getAPlayground()
	{
		return this.aPlayground;
	}
	
	public ApoDefenceGameTower getGameTower()
	{
		return this.gameTower;
	}

	public void setBuildMode( int build )
	{
		this.gameTower.setBuild( build );
	}
	
	public void buildTower( ApoDefenceTower tower )
	{
		this.money	= this.money - tower.getPrice();
		this.tower	= this.tower + 1;
	}
	
	public void buildUpgrade( ApoDefenceTower tower )
	{
		this.money	= this.money - (int)tower.getUpgradePrice();
	}
	
	public boolean canPay( int price, boolean bUpgrade )
	{
		if ( (!bUpgrade) && ( this.tower + 1 <= this.maxTower ) &&
			 ( this.money - price >= 0 ) )
		{
			return true;
		} else if ( ( bUpgrade ) && ( this.money - price >= 0 ) )
			return true;
		return false;
	}
	
	public boolean canUpgrade( ApoDefenceTower tower )
	{
		if ( tower.isBBuildUp() )
			return false;
		if ( tower.getLevel() + 1 <= tower.getMaxLevel() )
			return true;
		return false;
	}
	
	public boolean isTechLevel( int level )
	{
		if ( this.research.getTechLevel() >= level )
			return true;
		return false;
	}
	
	public void setSell( ApoDefenceTower tower )
	{
		this.money	+=	tower.getSellPrice();
		this.tower	= this.tower - 1;
		this.gameTower.setSell();
	}
	
	public int getMoney()
	{
		return this.money;
	}
	
	public void setMoney( int money )
	{
		this.money	= money;
	}
	
	public int getTower()
	{
		return this.tower;
	}
	
	public void setTower(int tower)
	{
		this.tower = tower;
	}
	
	public ArrayList<ApoDefenceDragObject> getEnemyWay()
	{
		return this.enemyWay;
	}
	
	public ApoDefenceGameEnemy getGameEnemy()
	{
		return this.gameEnemy;
	}
	
	public boolean isBWin()
	{
		return this.bWin;
	}

	public void setBWin(boolean bWin)
	{
		this.bWin = bWin;
	}
	
	public ApoDefenceMain getMain()
	{
		return this.main;
	}
	
	public int getMaxWaves()
	{
		return this.maxWaves;
	}

	public void setMaxWaves(int maxWaves)
	{
		this.maxWaves = maxWaves;
	}
	
	public int getMaxTower()
	{
		return this.maxTower;
	}

	public void setMaxTower(int maxTower)
	{
		this.maxTower = maxTower;
	}
	
	public ApoDefenceLoadSave getLoadSave()
	{
		return this.loadSave;
	}
	
	public float getPoints()
	{
		return this.points;
	}

	public void setPoints(float points)
	{
		this.points = points;
	}

	public void restart()
	{
		this.bRestart		= true;
		this.currentX		= 0;
		this.currentY		= 0;
		this.bUpload		= false;
		this.bLoose			= false;
		this.bWin			= false;
		this.x				= this.loadSave.getX();
		this.y				= this.loadSave.getY();
		this.aPlayground	= this.loadSave.getAPlayground();
		this.pointDefend	= this.loadSave.getPointDefend();
		this.castle			= new ApoDefenceCastle( this.iDefend, this.pointDefend.x, this.pointDefend.y );
		this.gameTower.setTowerCastle( this.castle );
		this.pointUpgrade	= this.loadSave.getPointUpgrade();
		this.research		= new ApoDefenceResearch( this.iUpgrade, this.pointUpgrade.x, this.pointUpgrade.y );
		this.gameTower.setTowerResearch( this.research );
		this.gameTower.restart();
		this.enemyWay		= this.loadSave.getEnemyWay();
		this.gameEnemy.setEnemyWay( this.enemyWay );
		this.gameEnemy.removeAllEnemies();
		if ( !this.imageString.equals( this.loadSave.getImageString() ) )
		{
			this.imageString	= this.loadSave.getImageString();
			this.iUnderground	= this.image.getPic( this.imageString, false );
		}
		if ( ApoDefenceConstants.MONEY == -1 )
		{
			ApoDefenceConstants.MONEY	= this.loadSave.getMoney();
		}
		this.money			= ApoDefenceConstants.MONEY;
		if ( ApoDefenceConstants.MAX_TOWERS == -1 )
		{
			ApoDefenceConstants.MAX_TOWERS	= this.loadSave.getTowerCount();
		}
		this.maxTower		= ApoDefenceConstants.MAX_TOWERS;
		if ( ApoDefenceConstants.MAX_WAVE == -1 )
		{
			ApoDefenceConstants.MAX_WAVE	= this.loadSave.getWaves();
		}
		this.money			= ApoDefenceConstants.MONEY;
		this.points			= 0;
		this.tower			= 0;
		
		this.gameEnemy.setCount( 0 );
		this.gameEnemy.setEnemyOrder( this.loadSave.getEnemies() );
		
		this.iBackground	= null;
		this.makeNewXAndY( this.x, this.y, false );
	
		this.bWin			= false;
		this.bMenu			= false;
		
		this.iMiniMap	= new BufferedImage( 105, 105, BufferedImage.TYPE_INT_RGB );
		Graphics g 		= this.iMiniMap.getGraphics();
		this.miniMapX	= 0;
		this.miniMapY	= 0;
		if ( this.x > this.y )
		{
			this.miniMapY	= (int)(((double)this.y / (double)this.x) * 105);
			this.miniMapX	= 105;
		} else
		{
			this.miniMapX	= (int)(((double)this.x / (double)this.y) * 105);
			this.miniMapY	= 105;
		}
		Image miniMap = this.iBackground.getScaledInstance( this.miniMapX, this.miniMapY, Image.SCALE_SMOOTH );
		g.drawImage( miniMap, 0 + ( 105 - this.miniMapX )/2, 0 + ( 105 - this.miniMapY )/2, null );
		g.dispose();
		
		this.waves					= 1;
		this.bNextWave				= true;
		this.bLoose					= false;
		this.maxWaves				= ApoDefenceConstants.MAX_WAVE;
		this.bWin					= false;
		this.bHealthVisibleTower	= false;
		this.bHealthVisibleEnemy	= false;
		this.nextWaveTime			= System.nanoTime();
		this.nicknameField.setVisible( false );
		this.nicknameField.requestFocus( false );
		this.main.requestFocus();
		this.bRestart				= false;
	}
	
	public void start()
	{
		this.restart();
		this.apoDefenceGameHud.setStart();
		if ( this.timer == null )
		{
			this.defenceTimer			= new ApoDefenceTimer( this );
			this.timer					= new Timer();
			this.timer.scheduleAtFixedRate( this.defenceTimer, 25, 25 );
		} else if ( !this.defenceTimer.isBRunning() )
		{
			this.defenceTimer.setBRunning( true );
		}
	}
	
	public void stop()
	{
		if ( this.timer != null )
		{
			this.defenceTimer.setBRunning( false );
			this.timer.cancel();
			this.timer			= null;
			this.defenceTimer	= null;
		}
	}
	
	private String getNextEnemy()
	{
		return this.gameEnemy.getNextEnemy();
	}
	
	public double getCurrentX()
	{
		return this.currentX;
	}

	public void setCurrentX(double currentX)
	{
		this.currentX = currentX;
	}

	public double getCurrentY()
	{
		return this.currentY;
	}

	public void setCurrentY(double currentY)
	{
		this.currentY = currentY;
	}

	public float getMiniMapPercentX()
	{
		float width		= 640 / (float)this.iBackground.getWidth();
		//System.out.println( "width = "+width );
		return width;
	}
	
	public int getMiniMapX()
	{
		return this.miniMapX;
	}

	public int getMiniMapY()
	{
		return this.miniMapY;
	}

	public float getMiniMapPercentY()
	{
		float height	= 342 / (float)this.iBackground.getHeight();
		//System.out.println( "height = "+height );
		return height;
	}
	
	public int getXValue()
	{
		return this.x;
	}

	public int getYValue()
	{
		return this.y;
	}

	public BufferedImage getIBackground()
	{
		return this.iBackground;
	}

	public boolean isBLoose()
	{
		return this.bLoose;
	}

	public boolean isBHealthVisibleTower()
	{
		return this.bHealthVisibleTower;
	}

	public boolean isBHealthVisibleEnemy()
	{
		return this.bHealthVisibleEnemy;
	}
	
	/**
	 * malt das eigentliche Spielfeld mit Spielern usw.
	 */
	public void paintComponent( Graphics gfx ) 
	{
		super.paintComponent( gfx );
		
		Graphics2D g	= (Graphics2D)gfx;
		//long time	= System.nanoTime();
		
		Rectangle2D.Double rec	= new Rectangle2D.Double( this.currentX, this.currentY, 640, 342 );
		
		g.drawImage( this.iBackground, 0, 33, 640, 375, (int)this.currentX, (int)this.currentY, (int)this.currentX + 640, (int)this.currentY + 342, null );
		if ( ( this.research.isBSelect() ) && ( this.research.intersects( rec ) ) )
		{
			this.research.render( g, -(int)this.currentX, -(int)this.currentY + 33 );
		} else if ( ( this.castle.isBSelect() ) && ( this.castle.intersects( rec ) ) )
		{
			this.castle.render( g, -(int)this.currentX, -(int)this.currentY + 33 );
		}
		
		this.gameTower.render( g, rec, -(int)this.currentX, -(int)this.currentY + 33, this.mouseX, this.mouseY );
		this.gameEnemy.render( g, rec, -(int)this.currentX, -(int)this.currentY + 33, this.mouseX, this.mouseY );
		
		this.drawHud( g );
		
		/*g.setColor( Color.WHITE );
		String x	= "Score: "+((int)this.points)+" Points";
		g.drawString( x, 10, 70 );*/
		
		if ( this.bMenu )
			this.drawMenu( g );
		else if ( this.bQuest )
		{
			g.drawImage( this.iQuest, this.getWidth()/2 - this.iQuest.getWidth()/2, this.getHeight()/2 - this.iQuest.getHeight()/2 - 40, null );
			g.drawImage( this.iMiniMap, this.getWidth()/2 - this.iQuest.getWidth()/2 + 18, this.getHeight()/2 - this.iQuest.getHeight()/2 - 40 + 135, null );
			this.buttons[2].render( g );
		} else if ( this.bWin )
		{
			this.drawWin( g, this.iWin );
		}
		else if ( this.bLoose )
		{
			this.drawWin( g, this.iGameOver );
		}
		else if ( this.bNextWave )
		{
			this.drawNextWave( g );
		}
		
		//System.out.println( "time = "+( System.nanoTime() - time ) );
	}
	
	private void drawHud( Graphics2D g )
	{
		this.apoDefenceGameHud.render( g );
		
		g.setColor( Color.WHITE );
		g.setFont( this.fontBig );
		String s	= this.money + " g";
		int w		= g.getFontMetrics().stringWidth( s );
		g.drawString( s, 505 - w, 22 );
		
		s	= this.tower + " / " + this.maxTower;
		w		= g.getFontMetrics().stringWidth( s );
		g.drawString( s, 625 - w, 22 );
		
		this.drawMiniMap( g );
	}
	
	public int getMiniRectMapX()
	{
		return ((105 - this.miniMapX)/2 + (int)(this.currentX * this.miniMapX / this.iBackground.getWidth()));
	}
	
	public int getMiniRectMapY()
	{
		return ((105 - this.miniMapY)/2 + (int)(this.currentY * this.miniMapY / this.iBackground.getHeight()));
	}
	
	private void drawMiniMap( Graphics2D g )
	{
		g.drawImage( this.iMiniMap, 12, 368, null );
		g.setColor( Color.BLACK );
		double width	= this.getMiniMapPercentX();
		double height	= this.getMiniMapPercentY();
		width			= this.miniMapX * width;
		height			= this.miniMapY * height;
		g.drawRect( 12 + this.getMiniRectMapX(), 368 + this.getMiniRectMapY(), (int)(width), (int)(height) );
		
		g.setColor( Color.RED );
		for ( int i = this.gameEnemy.getAllEnemy().size() - 1; i >= 0; i-- )
		{
			int xMiniMap	= 12;
			int yMiniMap	= 368;
			xMiniMap		+= this.miniMapX * this.gameEnemy.getAllEnemy().get( i ).getX() / this.iBackground.getWidth() + ( 105 - this.miniMapX )/2;
			yMiniMap		+= this.miniMapY * this.gameEnemy.getAllEnemy().get( i ).getY() / this.iBackground.getHeight() + ( 105 - this.miniMapY )/2;
			if ( ( xMiniMap > 12 + ( 105 - this.miniMapX )/2 ) && ( xMiniMap < 117 - ( 105 - this.miniMapX )/2 ) && ( yMiniMap > 368 + ( 105 - this.miniMapY )/2 ) && ( yMiniMap < 368 + 105 - ( 105 - this.miniMapX )/2 ) )
				g.fillRect( xMiniMap , yMiniMap, 2, 2 );
		}

		for ( int i = this.gameTower.getAllTower().size() - 1; i >= 0; i-- )
		{
			ApoDefenceTower	tower	= this.gameTower.getAllTower().get( i );
			int xMiniMap	= 12;
			int yMiniMap	= 368;
			xMiniMap		+= this.miniMapX * tower.getX() / this.iBackground.getWidth() + ( 105 - this.miniMapX )/2;
			yMiniMap		+= this.miniMapY * tower.getY() / this.iBackground.getHeight() + ( 105 - this.miniMapY )/2;
			if ( ( xMiniMap > 12 ) && ( xMiniMap < 117 ) && ( yMiniMap > 368 ) && ( yMiniMap < 368 + 105 ) ) 
			{
				if ( tower.getTower() == ApoDefenceConstants.TOWER_ARCH )
					g.setColor( Color.BLACK );
				else if ( tower.getTower() == ApoDefenceConstants.TOWER_FIRE )
					g.setColor( new Color( 255, 132, 0 ) );
				else if ( tower.getTower() == ApoDefenceConstants.TOWER_ICE )
					g.setColor( new Color( 100, 255, 255 ) );
				else if ( tower.getTower() == ApoDefenceConstants.TOWER_LIGHT )
					g.setColor( Color.YELLOW );
				else if ( tower.getTower() == ApoDefenceConstants.TOWER_ULTRA )
					g.setColor( new Color( 157, 34, 176 ) );
				g.fillRect( xMiniMap + 1, yMiniMap + 2, 3, 3 );
			}
		}
		
	}
	
	private void drawMenu( Graphics2D g )
	{
		g.drawImage( this.iMenu, 219, 91, null );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
	}
	
	private void drawWin( Graphics2D g, BufferedImage iWin )
	{
		if ( !this.highscore.isBConnect() )
		{
			g.drawImage( iWin, this.getWidth()/2 - iWin.getWidth()/2, this.getHeight()/2 - iWin.getHeight()/2 - 55, null );
			g.setFont( this.fontVeryBig );
			g.setColor( Color.WHITE );
			String s	= "Score: "+((int)this.points)+" Points";
			int w		= g.getFontMetrics().stringWidth( s );
			g.drawString( s, this.getWidth()/2 - w/2, this.getHeight()/2 );
		} else
		{
			g.drawImage( this.iHighscore, 25, 72, null );
			g.setFont( this.fontBig );
			g.setColor( Color.WHITE );
			if ( this.bUpload )
				g.drawString( "Nickname: ", 60, 325 );
			this.highscore.render( g );
			this.buttons[3].render( g );
		}
	}
	
	private void drawNextWave( Graphics2D g )
	{
		g.setColor( Color.BLACK );
		g.drawImage( this.iNextWave, 400, 40, null );
		g.drawString( (int)( ApoDefenceConstants.WAVE_TIME + 0.2f - TimeUnit.NANOSECONDS.toSeconds( System.nanoTime() - this.nextWaveTime ) ) + " s", 530, 71 );

		int w		= 0;
		String s	= "";
		if ( this.waves > 1 )
		{			
			g.setColor( Color.YELLOW );
			s	= "+ "+(int)((this.waves-1) * 5 * ApoDefenceConstants.DIFFICULTY + 30)+" gold for completing this round";
			w		= g.getFontMetrics().stringWidth( s );
			g.drawString( s, this.getWidth()/2 - w/2, this.getHeight()/2 - this.iStage.getHeight() );
		}
		
		g.setFont( this.fontVeryBig );
		s	= this.waves + " / " + this.maxWaves;
		w	= this.getWidth()/2 - this.iStage.getWidth()/2 - g.getFontMetrics().stringWidth( s )/2 - 5;
		g.drawImage( this.iStage, w, this.getHeight()/2 - this.iStage.getHeight()/2, null );
		w	= this.getWidth()/2 + this.iStage.getWidth()/2 - g.getFontMetrics().stringWidth( s )/2 - 5;
		int h	= this.getHeight()/2 + this.iStage.getHeight()/2;
		g.setColor( Color.WHITE );
		g.drawString( s, w + 10, h - 5 );
		
		g.setFont( this.fontBig );
		s	= "Next enemy: "+this.getNextEnemy();
		w	= this.getWidth()/2 - g.getFontMetrics().stringWidth( s )/2 - 5;
		h	= this.getHeight()/2 + this.iStage.getHeight()/2 + 30;
		g.drawString( s, w, h );
	}

}
