package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceEntity;
import apoDefence.ApoDefencePoint;

public class ApoDefenceTower extends ApoDefenceEntity
{
	private	float 				range, speed, health, armor, techLevel, level,
								maxLevel, maxHealth, price;
								
	private float				upgradePrice, upgradePricePlus, upgradeRange,
								upgradeSpeed, upgradeHealth, upgradeAmor, upgradeDamage;
	
	private float				damageX, damageY;
	
	private boolean				bShoot;
	
	private boolean				bHealthVisible;
	
	private float				shootTime;
	
	private ArrayList<ApoDefenceShoot>	shoot;
	
	private ApoDefencePoint		pointAttack;
	
	private String 				name;
	
	private boolean				bSelect;
	
	private BufferedImage		iMiniTower;
	
	private Rectangle2D.Double	rec;
	private Rectangle2D.Double	rangeRec;
	
	private boolean 			bBuildUp;
	private boolean 			bRepair;
	
	private int					sellPrice;
	
	private final Color			colorBlue	= Color.BLUE;
	
	private int					tower;

	public ApoDefenceTower( int tower, BufferedImage iTower, int x, int y, String name )
	{
		super( iTower, x, y, iTower.getWidth(), iTower.getHeight() );
		
		this.tower		= tower;
		this.name		= name;
		
		this.rec		= new Rectangle2D.Double( x, y + 5, iTower.getWidth(), iTower.getHeight() - 5 );
		
		this.bSelect		= false;
		this.bBuildUp		= true;
		this.bRepair		= false;
		this.bShoot			= true;
		this.bHealthVisible	= false;
		
		this.shootTime	= -1;
		
		float miniX		= 0;
		float miniY		= 0;
		if ( iTower.getWidth() > iTower.getHeight() )
		{
			miniX	= 89;
			miniY	= (float)iTower.getHeight() / (float)iTower.getWidth();
			miniY	= miniY	* 89;
		} else
		{
			miniY	= 89;
			miniX	= (float)iTower.getWidth() / (float)iTower.getHeight();
			miniX	= miniX	* 89;
		}
		this.iMiniTower	= new BufferedImage( 89, 89, BufferedImage.TYPE_INT_RGB );
		Graphics g 		= this.iMiniTower.getGraphics();
		Image miniMap = iTower.getScaledInstance( (int)miniX, (int)miniY, Image.SCALE_SMOOTH );
		g.drawImage( miniMap, (int)( 0 + ( 89 - miniX )/2 ), (int)( 0 + ( 89 - miniY )/2 ), null );
		g.dispose();
		
		this.initTower();
	}
	
	protected void initTower()
	{
		this.setValues( 1, 1, 1, 1, 1, 1, new ApoDefencePoint( 1, 1 ) );
		this.setUpgradeValues( -1, -1, -1, -1, -1, -1, -1 );
		
		this.shoot		= new ArrayList<ApoDefenceShoot>();
	}
	
	protected void setValues( int range, int speed, int health, int armor, int level, int price, ApoDefencePoint pointAttack )
	{
		this.range			= range;
		this.rangeRec		= new Rectangle2D.Double( this.getX() + this.getWidth()/2 - this.range/2, this.getY() + this.getHeight()/2 - this.range/2, this.range, this.range );
		this.speed			= speed;
		this.maxHealth		= health;
		this.health			= 0;
		this.armor			= armor;
		this.techLevel		= level;
		this.level			= 1;
		this.price			= price;
		this.sellPrice		= price/2;
		this.pointAttack	= pointAttack;
		this.damageX		= this.pointAttack.x;
		this.damageY		= this.pointAttack.y;
	}
	
	protected void setUpgradeValues( float upgradePrice, float upgradePricePlus, float upgradeRange, float upgradeSpeed, float upgradeHealth, float upgradeAmor, float upgradeDamage )
	{
		this.upgradePrice		= upgradePrice;
		this.upgradePricePlus	= upgradePricePlus;
		this.upgradeRange		= upgradeRange;
		this.upgradeSpeed		= upgradeSpeed;
		this.upgradeHealth		= upgradeHealth;
		this.upgradeAmor		= (float)upgradeAmor;
		this.upgradeDamage		= upgradeDamage;
	}
	
	public int getTower()
	{
		return this.tower;
	}
	
	protected int getArmor()
	{
		return (int)this.armor;
	}

	protected void setArmor(int amor)
	{
		this.armor = amor;
	}

	protected int getHealth()
	{
		return (int)this.health;
	}

	protected void setHealth(int health)
	{
		this.health = health;
		if ( this.health < 0 )
			this.health	= 0;
	}

	protected int getRange()
	{
		return (int)this.range;
	}

	protected void setRange(int range)
	{
		this.range = range;
	}

	protected int getSpeed()
	{
		return (int)this.speed;
	}

	protected void setSpeed(int speed)
	{
		this.speed = speed;
	}

	protected ApoDefencePoint getAttack()
	{
		return this.pointAttack;
	}

	protected void setAttack(ApoDefencePoint pointAttack)
	{
		this.pointAttack = pointAttack;
	}

	protected int getMaxHealth() {
		return (int)maxHealth;
	}

	protected void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	protected Rectangle2D.Double getRec()
	{
		return rec;
	}
	
	protected boolean intersects( Rectangle2D.Double rec )
	{
		return this.rec.intersects( rec );
	}

	protected int getLevel()
	{
		return (int)this.level;
	}

	protected void setLevel(int level)
	{
		this.level = level;
	}
	
	protected int getTechLevel()
	{
		return (int)this.techLevel;
	}

	protected void setTechLevel(int techLevel)
	{
		this.techLevel = techLevel;
	}

	protected int getMaxLevel()
	{
		return (int)this.maxLevel;
	}

	protected void setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
	}

	protected boolean isBSelect()
	{
		return this.bSelect;
	}

	protected void setBSelect(boolean bSelect)
	{
		this.bSelect = bSelect;
	}

	public void setRec(Rectangle2D.Double rec)
	{
		this.rec = rec;
	}

	public BufferedImage getIMiniTower()
	{
		return this.iMiniTower;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getPrice()
	{
		return (int)this.price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}
	
	public boolean isBBuildUp()
	{
		return this.bBuildUp;
	}

	public void setBBuildUp(boolean bBuildUp)
	{
		this.bBuildUp = bBuildUp;
	}
	
	public float getUpgradeAmor()
	{
		return this.upgradeAmor;
	}

	public void setUpgradeAmor(float upgradeAmor)
	{
		this.upgradeAmor = upgradeAmor;
	}

	public float getUpgradeDamage()
	{
		return this.upgradeDamage;
	}

	public void setUpgradeDamage(float upgradeDamage)
	{
		this.upgradeDamage = upgradeDamage;
	}

	public float getUpgradeHealth()
	{
		return this.upgradeHealth;
	}

	public void setUpgradeHealth(float upgradeHealth)
	{
		this.upgradeHealth = upgradeHealth;
	}

	public float getUpgradePrice()
	{
		return this.upgradePrice;
	}

	public void setUpgradePrice(float upgradePrice)
	{
		this.upgradePrice = upgradePrice;
	}

	public float getUpgradePricePlus()
	{
		return this.upgradePricePlus;
	}

	public void setUpgradePricePlus( float upgradePricePlus )
	{
		this.upgradePricePlus = upgradePricePlus;
	}

	public float getUpgradeRange()
	{
		return this.upgradeRange;
	}

	public void setUpgradeRange(float upgradeRange)
	{
		this.upgradeRange = upgradeRange;
	}

	public float getUpgradeSpeed()
	{
		return this.upgradeSpeed;
	}

	public void setUpgradeSpeed(float upgradeSpeed)
	{
		this.upgradeSpeed = upgradeSpeed;
	}
	
	public int getNewUpgradeHealth()
	{
		return (int)( this.maxHealth + this.maxHealth * this.upgradeHealth );
	}
	
	public int getNewUpgradeRange()
	{
		return (int)( this.range + this.range * this.upgradeRange );
	}
	
	public ApoDefencePoint getNewUpgradePoint()
	{
		return new ApoDefencePoint( (int)( this.damageX + this.damageX * this.upgradeDamage ), (int)(this.damageY + this.damageY * this.upgradeDamage) );
	}
	
	public int getNewUpgradeSpeed()
	{
		return (int)( this.speed + this.speed * this.upgradeSpeed );
	}
	
	public int getNewUpgradeArmor()
	{
		return (int)( (float)this.armor + (float)this.upgradeAmor );
	}
	
	public int getSellPrice()
	{
		return this.sellPrice;
	}

	public void setSellPrice(int sellPrice)
	{
		this.sellPrice = sellPrice;
	}

	public boolean isBRepair()
	{
		return this.bRepair;
	}

	public void setBRepair(boolean bRepair)
	{
		this.bRepair = bRepair;
	}
	
	public void upgrade()
	{
		this.armor			= this.getNewUpgradeArmor();
		this.maxHealth		= this.getNewUpgradeHealth();
		this.health			= 0;
		this.level			= this.level + 1;
		this.bBuildUp		= true;
		this.sellPrice		+= (int)(this.upgradePrice / 2);
		this.upgradePrice	= this.upgradePrice + this.upgradePrice * this.upgradePricePlus;
		this.range			= this.getNewUpgradeRange();
		this.rangeRec		= new Rectangle2D.Double( this.getX() + this.getWidth()/2 - this.range/2, this.getY() + this.getHeight()/2 - this.range/2, this.range, this.range );
		this.speed			= this.getNewUpgradeSpeed();
		this.pointAttack	= this.getNewUpgradePoint();
		this.damageX		= this.damageX + this.damageX * this.upgradeDamage;
		this.damageY		= this.damageY + this.damageY * this.upgradeDamage;
	}
	
	public void think( ApoDefenceGame game )
	{
		if ( this.isBBuildUp() )
		{
			this.buildUpHealth( game.getResearch().getSpeedLevel()/2 );
			this.setBRepair( false );
		} else if ( this.isBRepair() )
		{
			this.repairHealth( game );
		}
		if ( !this.isBShoot() )
			this.shootTime	-= ApoDefenceConstants.SHOOT_TIME;
		if ( this.shootTime < 0 )
			this.setBShoot( true );

		for ( int i = this.shoot.size() - 1; i >= 0; i-- )
		{
			this.shoot.get( i ).think( game );
			if ( ( !this.shoot.get( i ).isBVisible() ) || ( !this.getRangeRec().intersects( this.shoot.get( i ).getRec() ) ) )
			{
				this.shoot.remove( i );
			}	
		}
	}
	
	private void buildUpHealth( float speed )
	{
		this.health		+= speed;
		if ( this.health >= this.maxHealth )
		{
			this.health		= this.maxHealth;
			this.bBuildUp	= false;
		}
	}
	
	private void repairHealth( ApoDefenceGame game )
	{
		if ( this.getHealth() >= this.getMaxHealth() )
		{
			this.setHealth( this.getMaxHealth() );
			this.setBRepair( false );
		} else
		{
			if ( game.getMoney() - ApoDefenceConstants.REPAIR_COST >= 0 )
			{
				game.setMoney( game.getMoney() - ApoDefenceConstants.REPAIR_COST );
				this.setHealth( this.getHealth() + ApoDefenceConstants.REPAIR_HEALTH );
			} else
			{
				this.setBRepair( false );
			}
		}
	}
	
	public ArrayList<ApoDefenceShoot> getShoot()
	{
		return this.shoot;
	}

	public void setShoot(ArrayList<ApoDefenceShoot> shoot)
	{
		this.shoot = shoot;
	}
	
	public boolean isBShoot()
	{
		return this.bShoot;
	}

	public void setBShoot(boolean bShoot)
	{
		this.bShoot = bShoot;
	}
	
	public boolean isBHealthVisible()
	{
		return this.bHealthVisible;
	}

	public void setBHealthVisible(boolean bHealthVisible)
	{
		this.bHealthVisible = bHealthVisible;
	}

	public float getShootTime()
	{
		return this.shootTime;
	}

	public void setShootTime(float shootTime)
	{
		this.shootTime = shootTime;
	}
	
	public Rectangle2D.Double getRangeRec()
	{
		return this.rangeRec;
	}

	public void setRangeRec(Rectangle2D.Double rangeRec)
	{
		this.rangeRec = rangeRec;
	}
	
	public boolean isIn( double x, double y, double xm, double ym )
	{
		double dx	= (double)(x-xm);
		double dy	= (double)(y-ym);
		return ((dx/this.getRange())*(dx/this.getRange())+(dy/this.getRange())*(dy/this.getRange())) <= 0.25;
	}
	
	public void render( Graphics g )
	{
		this.render( (Graphics2D)g );
	}
	
	public void render( Graphics2D g, int x, int y )
	{
		double value	= ((double)this.health / (double)this.maxHealth);
		if ( ( this.isBSelect() ) || ( ( value < 0.4 ) && ( !this.bBuildUp ) ) || ( this.isBHealthVisible() ) )
		{
			if ( this.isBSelect() )
			{
				g.setColor( this.colorBlue );
				g.drawOval( (int)( this.getX() + x + this.getWidth()/2 - this.getRange()/2 ), (int)( this.getY() + y + (this.getHeight() - 7) / 2 - this.range/2 ), (int)this.range, (int)this.range );
				g.setColor( Color.GREEN );
				g.drawOval( (int)this.getX() + x, (int)( this.getY() + y ), this.getWidth(), this.getHeight() );
			} else
				g.setColor( Color.GREEN );
			value			= value * 50;
			g.fillRect( (int)( this.getX() + this.getWidth()/2 + x - 25 ), (int)( this.getY() + y - 13 ),  (int)value, 10 );
			g.setColor( Color.RED );
			g.fillRect( (int)( value + this.getX() + this.getWidth()/2 + x - 25 ), (int)( this.getY() + y - 13 ), 50 - (int)value, 10 );
			g.setColor( Color.BLACK );
			g.drawRect( (int)( this.getX() + this.getWidth()/2 + x - 25 ), (int)( this.getY() + y - 13 ),  50, 10 );
		}
		
		for ( int i = 0; i < this.shoot.size(); i++ )
		{
			this.shoot.get( i ).render( g, x, y );
		}
		g.drawImage( this.getIBackground(), (int)this.getX() + x, (int)this.getY() + y, (int)this.getX() + x + this.getWidth(), (int)this.getY() + y + this.getHeight(), 0, 0, this.getWidth(), this.getHeight(), null );
	}

}
