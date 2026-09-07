package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceImage;

public class ApoDefenceGameTower
{
	private ApoDefenceGame 						game;
	
	private ArrayList<ApoDefenceTower>			tower;
	private ApoDefenceResearch					tower_research;
	private ApoDefenceCastle					tower_castle;
	
	private BufferedImage						iTowerArch, iTowerIce, iTowerFire,
												iTowerLight, iTowerUltra, iTowerUltraShoot;
	
	private int									build;
	private int									buildRange;
	
	private boolean								bBuild;
	private boolean								bHealthVisible;
	
	private Point								choosenTower;
	
	public ApoDefenceGameTower(ApoDefenceGame game )
	{
		super();
		
		this.game				= game;
		
		ApoDefenceImage	image	= new ApoDefenceImage();
		
		this.iTowerArch			= image.getPic( "/images/tower/tower_arch.png", false );
		this.iTowerFire			= image.getPic( "/images/tower/tower_fire.png", false );
		this.iTowerIce			= image.getPic( "/images/tower/tower_ice.png", false );
		this.iTowerLight		= image.getPic( "/images/tower/tower_light.png", false );
		this.iTowerUltra		= image.getPic( "/images/tower/tower_ultra.png", false );
		this.iTowerUltraShoot	= image.getPic( "/images/tower/ultraball.png", false );
		
		this.restart();
	}

	
	protected void restart()
	{
		this.build				= ApoDefenceConstants.EMPTY;
		this.buildRange			= ApoDefenceConstants.EMPTY;
		this.bBuild				= false;
		this.bHealthVisible		= false;
		
		this.tower				= new ArrayList<ApoDefenceTower>();
		
		this.choosenTower		= new Point( -1, -1 );
	}
	
	public void setHealthVisible( boolean bHealthVisible )
	{
		this.bHealthVisible		= bHealthVisible;
		for ( int i = 0; i < this.tower.size(); i++ )
		{
			this.tower.get( i ).setBHealthVisible( bHealthVisible );
		}
	}
	
	public int getBuild()
	{
		return this.build;
	}

	public void setBuild(int build)
	{
		this.build 	= build;
		if ( this.build == ApoDefenceConstants.TOWER_ARCH )
		{
			this.buildRange	= ApoDefenceConstants.TOWER_ARCH_RANGE;
		} else if ( this.build == ApoDefenceConstants.TOWER_FIRE )
		{
			this.buildRange	= ApoDefenceConstants.TOWER_FIRE_RANGE;
		} else if ( this.build == ApoDefenceConstants.TOWER_ICE )
		{
			this.buildRange	= ApoDefenceConstants.TOWER_ICE_RANGE;
		} else if ( this.build == ApoDefenceConstants.TOWER_LIGHT )
		{
			this.buildRange	= ApoDefenceConstants.TOWER_LIGHT_RANGE;
		} else if ( this.build == ApoDefenceConstants.TOWER_ULTRA )
		{
			this.buildRange	= ApoDefenceConstants.TOWER_ULTRA_RANGE;
		}
		this.bBuild	= false;
	}
	
	public void setSell()
	{
		if ( this.choosenTower.x == 0 )
			this.tower.remove( this.choosenTower.y );
	}
	
	public ApoDefenceTower getChoosenTower()
	{
		if ( this.choosenTower.x == 0 )
			return this.tower.get( this.choosenTower.y );
		
		return null;
	}
	
	protected boolean mouseReleased(int x, int y)
	{
		if ( this.build == ApoDefenceConstants.EMPTY )
		{
			Rectangle2D.Double rec = new Rectangle2D.Double( x, y, 1, 1 );
			if ( 	( !this.towerBuildTowerCheck( this.tower, rec ) ) )
				return true;
		} else
		{
			if ( this.towerBuildCheck( x, y ) )
			{
				if ( this.build == ApoDefenceConstants.TOWER_ARCH )
				{
					ApoDefenceTower tower	= new ApoDefenceTowerArch( this.iTowerArch, x - 16, y - 25 ); 
					this.addTower( tower );
				} else if ( this.build == ApoDefenceConstants.TOWER_FIRE )
				{
					ApoDefenceTower tower	= new ApoDefenceTowerFire( this.iTowerFire, x - 16, y - 25 ); 
					this.addTower( tower );
				} else if ( this.build == ApoDefenceConstants.TOWER_ICE )
				{
					ApoDefenceTower tower	= new ApoDefenceTowerIce( this.iTowerIce, x - 16, y - 25 ); 
					this.addTower( tower );
				} else if ( this.build == ApoDefenceConstants.TOWER_LIGHT )
				{
					ApoDefenceTower tower	= new ApoDefenceTowerLight( this.iTowerLight, x - 16, y - 25 ); 
					this.addTower( tower );
				} else if ( this.build == ApoDefenceConstants.TOWER_ULTRA )
				{
					ApoDefenceTower tower; 
					if ( this.iTowerUltraShoot == null )
						tower	= new ApoDefenceTowerUltra( this.iTowerUltra, x - 16, y - 25 );
					else
						tower	= new ApoDefenceTowerUltra( this.iTowerUltra, this.iTowerUltraShoot, x - 16, y - 25 );
					this.addTower( tower );
				}
				this.build				= ApoDefenceConstants.EMPTY;
				this.buildRange			= ApoDefenceConstants.EMPTY;
				this.choosenTower		= new Point( -1, -1 );
				return true;
			}
		}
		return false;
	}
	
	private void addTower( ApoDefenceTower tower )
	{
		double y	= tower.getY();
		tower.setArmor( tower.getArmor() + this.tower_research.getArmorLevel() );
		int value	= 0;
		if ( this.tower.size() == 0 )
			this.tower.add( tower );
		else
		{
			boolean	bBreak	= false;
			for ( int i = 0; i < this.tower.size(); i++ )
			{
				value	= i;
				if ( this.tower.get( i ).getY() > y - 13 )
				{
					this.tower.add( value, tower );
					bBreak	= true;
					break;
				}
			}
			if ( !bBreak )
				this.tower.add( tower );
		}
		tower.setBHealthVisible( this.bHealthVisible );
		this.game.buildTower( tower );
	}
	
	protected void addAmor()
	{
		for ( int i = 0; i < this.tower.size(); i++ )
		{
			this.tower.get( i ).setArmor( this.tower.get( i ).getArmor() + 1 );
		}
	}
	
	protected boolean mouseMoved( int x, int y )
	{
		this.bBuild		= this.towerBuildCheck( x, y );
		return this.bBuild;
	}
	
	protected boolean towerBuildCheck( int x, int y )
	{
		int[][] aPlayground		= this.game.getAPlayground();
		Rectangle2D.Double rec = new Rectangle2D.Double( x - 16, y - 16, 32, 24 );
		
		if ( ( this.getBuild() != ApoDefenceConstants.EMPTY ) &&
			 ( this.towerBuildCornerCheck( aPlayground, x, y, -16, -12 ) ) &&
			 ( this.towerBuildCornerCheck( aPlayground, x, y, -16, +12 ) ) &&
			 ( this.towerBuildCornerCheck( aPlayground, x, y, +16, -12 ) ) &&
			 ( this.towerBuildCornerCheck( aPlayground, x, y, +16, +12 ) ) &&
			 ( this.towerBuildTowerCheck( this.tower, rec ) ) )
		{
			return true;
		}
		return false;
	}
	
	private boolean towerBuildCornerCheck( int[][] aPlayground, int x, int y, int xChange, int yChange )
	{
		int checkX				= x + xChange;
		int checkY				= y + yChange;
		if ( ( checkX >= 0 ) && ( checkX < aPlayground[0].length * 32 ) &&
			 ( checkY >= 0 ) && ( checkY < aPlayground.length * 32 ) )
		{
			if ( aPlayground[checkY/32][checkX/32] != ApoDefenceConstants.SAND )
				return true;
		}
		return false;
	}

	private boolean towerBuildTowerCheck( ArrayList<ApoDefenceTower> tower, Rectangle2D.Double rec )
	{
		for ( int i = 0; i < tower.size(); i++ )
		{
			if ( tower.get( i ).intersects( rec ) )
			{
				this.choosenTower	= new Point( 0, i );
				return false;
			}
		}

		return true;
	}
	
	public void think( ApoDefenceGame game )
	{
		for ( int i = 0; i < this.tower.size(); i++ )
		{
			this.tower.get( i ).think( game );
		}
	}
	
	public boolean isBBuild()
	{
		return this.bBuild;
	}
	
	public ApoDefenceResearch getTowerResearch()
	{
		return this.tower_research;
	}

	public void setTowerResearch(ApoDefenceResearch tower_research)
	{
		this.tower_research = tower_research;
	}

	public ApoDefenceCastle getTowerCastle()
	{
		return this.tower_castle;
	}

	public void setTowerCastle(ApoDefenceCastle tower_castle)
	{
		this.tower_castle = tower_castle;
	}
	
	public ArrayList<ApoDefenceTower> getAllTower()
	{
		return this.tower;
	}
	
	protected void render( Graphics2D g, Rectangle2D.Double rec, int x, int y, int mouseX, int mouseY )
	{
		if ( ( this.build != ApoDefenceConstants.EMPTY ) && ( mouseY > 33 ) && ( mouseY < 370 ) )
		{
			if ( this.bBuild )
				g.setColor( Color.WHITE );
			else
				g.setColor( Color.RED );
			g.drawRect( mouseX - 16, mouseY - 16, 32, 32 );
			g.setColor( Color.BLUE );
			g.drawOval( mouseX - this.buildRange/2, mouseY - this.buildRange/2, this.buildRange, this.buildRange );
		}
		for ( int i = 0; i < this.tower.size(); i++ )
		{
			this.tower.get( i ).render( g, x, y );
		}
	}
	
}
