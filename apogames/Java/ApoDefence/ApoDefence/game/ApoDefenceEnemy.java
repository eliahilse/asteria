package apoDefence.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceEntity;
import apoDefence.ApoDefencePoint;

public class ApoDefenceEnemy extends ApoDefenceEntity
{
	private final int			UP		= 0;
	private final int			RIGHT	= 1;
	private final int			DOWN	= 2;
	private final int			LEFT	= 3;
	private final int			RANGE	= 200;
	
	private BufferedImage		iMiniEnemy;
	
	private	float				speedX, speedY;
	
	private float				maxHealth, health, healthUpgrade, 
								speed, speedUpgrade, armor, armorUpgrade;
	
	private ApoDefencePoint		damage;
	
	private boolean				bShoot;
	
	private boolean				bHealthVisible;
	
	private float				shootTime;
	
	private int					level;
	
	private float 				money;
	
	private ArrayList<Point>	enemyWay;
	
	private ArrayList<ApoDefenceShoot>	shoot;
	
	private int					currentWay, lookWay, countLookWay;
	
	private float				countWay;
	
	private boolean 			bVisible, bSelect;
	
	private boolean				bIce, bFire, bLight, bUndead;
	
	private float				extraTime, extraFire, extraAfterTime;
	
	private Rectangle2D.Double	rec;
	private Rectangle2D.Double	rangeRec;
	
	private String 				name;
	
	private int					enemy;
	
	private long				startTime;
	
	public ApoDefenceEnemy( int enemy, BufferedImage iEnemy, ArrayList<Point> enemyWay )
	{
		super( iEnemy, -1, -1, iEnemy.getWidth()/3, iEnemy.getHeight()/4 );
		
		this.init();
		
		this.enemy			= enemy;
		
		this.enemyWay		= enemyWay;
		
		float miniX		= 0;
		float miniY		= 0;
		if ( this.getWidth() > this.getHeight() )
		{
			miniX	= 89;
			miniY	= (float)this.getHeight() / (float)this.getWidth();
			miniY	= miniY	* 89;
		} else
		{
			miniY	= 89;
			miniX	= (float)this.getWidth() / (float)this.getHeight();
			miniX	= miniX	* 89;
		}
		this.iMiniEnemy	= new BufferedImage( 89, 89, BufferedImage.TYPE_INT_RGB );
		Graphics g 		= this.iMiniEnemy.getGraphics();
		BufferedImage subImage	= iEnemy.getSubimage( 1 * this.getWidth(), 2 * this.getHeight(), this.getWidth(), this.getHeight() );
		Image miniMap = subImage.getScaledInstance( (int)miniX, (int)miniY, Image.SCALE_SMOOTH );
		g.drawImage( miniMap, (int)( 0 + ( 89 - miniX )/2 ), (int)( 0 + ( 89 - miniY )/2 ), null );
		g.dispose();
	}

	public void init()
	{
		this.speedX		= 0.0f;
		this.speedY		= 0.0f;
		
		this.currentWay		= 1;
		this.countWay		= -1;
		this.countLookWay	= (int)( Math.random() * 20 );
		this.lookWay		= this.UP;
		
		this.bVisible		= true;
		this.bIce			= false;
		this.bFire			= false;
		this.bLight			= false;
		this.bUndead		= false;
		this.bShoot			= true;
		this.bHealthVisible	= false;
		
		this.shootTime	= -1;
		this.extraTime		= -1;
		this.extraAfterTime	= -1;
		this.extraFire		= 0;
		
		this.startTime		= System.nanoTime();
		
		this.rec			= new Rectangle2D.Double( 1, 1, 1, 1 );
		this.rangeRec		= new Rectangle2D.Double( this.getX() - 100, this.getY() - 100, 200, 200 );
		
		this.shoot			= new ArrayList<ApoDefenceShoot>();
	}
	
	public void setValues( String name, int level, float health, float healthUpgrade, float speed, float speedUpgrade, float armor, float armorUpgrade, ApoDefencePoint damage, float damageUpgrade, float money )
	{
		this.name			= name;
		this.maxHealth		= health;
		this.healthUpgrade	= healthUpgrade;
		this.speed			= speed;
		this.speedUpgrade	= speedUpgrade;
		this.level			= level;
		this.armor			= armor;
		this.armorUpgrade	= armorUpgrade;
		this.damage			= damage;
		this.money			= money;
		float damageX		= this.damage.x;
		float damageY		= this.damage.y;
		for ( int i = 1; i < this.level; i++ )
		{
			this.maxHealth	= this.maxHealth + this.healthUpgrade;
			this.speed		= this.speed + this.speed * this.speedUpgrade;
			this.armor		= this.armor + this.armorUpgrade;
			damageX			= damageX + damageX * damageUpgrade;
			damageY			= damageY + damageY * damageUpgrade;
		}
		this.maxHealth		= this.maxHealth + this.level * 2.5f;
		
		float difficulty	= 1.0f;
		
		if ( ApoDefenceConstants.DIFFICULTY == ApoDefenceConstants.DIFFICULTY_EASY )
		{
			difficulty		= ApoDefenceConstants.DIFFICULTY_EASY_CONST;
		} else if ( ApoDefenceConstants.DIFFICULTY == ApoDefenceConstants.DIFFICULTY_HARD )
		{
			difficulty		= ApoDefenceConstants.DIFFICULTY_HARD_CONST;
		}
		
		damageX			= damageX * difficulty;
		damageY			= damageY * difficulty;
		this.armor		= this.armor * difficulty;
		this.speed		= this.speed * difficulty;
		this.maxHealth	= this.maxHealth * difficulty;
		this.money		= this.money * ( 3.0f - difficulty );
		
		this.damage			= new ApoDefencePoint( damageX, damageY );
		this.health			= this.maxHealth;
		
		this.setEnemyWay( this.enemyWay );
	}
	
	public ApoDefencePoint getShootPoint()
	{
		float x	= (float)this.getX();
		float y	= (float)this.getY();
		for ( int i = 0; i < 10; i++ )
		{
			x	+= this.getSpeedX();
			y	+= this.getSpeedY();
		}
		return new ApoDefencePoint( x, y );
	}
	
	public ArrayList<Point> getEnemyWay()
	{
		return this.enemyWay;
	}

	public void setEnemyWay(ArrayList<Point> enemyWay)
	{
		if ( ( enemyWay == null ) || ( enemyWay.size() < 2 ) )
			return;
		this.enemyWay 		= enemyWay;
		this.bVisible		= true;
		
		int	x	= this.enemyWay.get( 0 ).x;
		int y	= this.enemyWay.get( 0 ).y;
		this.setX( x );
		this.setY( y );
		
		this.rec		= new Rectangle2D.Double( this.getX(), this.getY(), this.getWidth(), this.getHeight() );
		
		this.currentWay		= 0;
		this.getNewWay( this.enemyWay.get( this.currentWay ).x, this.enemyWay.get( this.currentWay ).y );
		
	}
	
	protected void getNewWay( int x, int y )
	{
		if ( this.currentWay + 1 >= this.enemyWay.size() )
		{
			this.bVisible	= false;
			return;
		}
		this.currentWay		+= 1;
		int xDiff			= x - this.enemyWay.get( this.currentWay ).x;
		int yDiff			= y - this.enemyWay.get( this.currentWay ).y;
		int sum				= Math.abs( xDiff ) + Math.abs( yDiff );
		float maxSpeed		= (float)( this.speed / 100.0f );
		
		if ( Math.abs( xDiff ) > Math.abs( yDiff ) )
		{
			if ( xDiff < 0 )
			{
				this.lookWay	= this.RIGHT;
				this.speedX		= maxSpeed * ( Math.abs( (float)xDiff / (float)sum ) );
			}
			else
			{
				this.lookWay	= this.LEFT;
				this.speedX		= -maxSpeed * ( Math.abs( (float)xDiff / (float)sum ) );;
			}
			this.countWay	= (int)( (float)( ( Math.abs( xDiff ) / maxSpeed ) + 1 ) );
			yDiff	= -yDiff;
			this.speedY		= maxSpeed * ( Math.abs( (float)yDiff / (float)sum ) );;
			if ( yDiff < 0 )
				this.speedY	= -Math.abs( this.speedY );
			else
				this.speedY	= Math.abs( this.speedY );
		} else
		{
			if ( yDiff < 0 )
			{
				this.lookWay	= this.DOWN;
				this.speedY		= maxSpeed * ( Math.abs( (float)yDiff / (float)sum ) );;
				//System.out.println( "speedY = "+speedY+" = ("+maxSpeed+" sum = "+sum );
			}
			else
			{
				this.lookWay	= this.UP;
				this.speedY		= -maxSpeed * ( Math.abs( (float)yDiff / (float)sum ) );;
			}
			xDiff	= -xDiff;
			this.countWay	= (int)( Math.abs( yDiff ) / maxSpeed ) + 1;
			this.speedX		= maxSpeed * ( Math.abs( (float)xDiff / (float)sum ) );
			if ( xDiff < 0 )
				this.speedX	= -Math.abs( this.speedX );
			else
				this.speedX	= Math.abs( this.speedX );
		}
		//System.out.println( "x = "+x+" y = "+y+" newX = "+this.enemyWay.get( this.currentWay ).x+" newY = "+this.enemyWay.get( this.currentWay ).y );
		//System.out.println( "xDiff = "+xDiff+" yDiff = "+yDiff+"maxSpeed = "+maxSpeed+" speedX = "+this.speedX+" speedY = "+this.speedY );
	}
	
	public boolean isNextStep()
	{
		if ( this.speedX > 0 )
		{
			 if ( this.getX() < this.enemyWay.get( this.currentWay ).x )
			 {
				 return false;
			 } else
			 {
				 this.setX( this.enemyWay.get( this.currentWay ).x );
			 }
		} else if ( this.speedX < 0 )
		{
			if ( this.getX() > this.enemyWay.get( this.currentWay ).x )
			 {
				 return false;
			 } else
			 {
				 this.setX( this.enemyWay.get( this.currentWay ).x );
			 }
		}
		if ( this.speedY > 0 )
		{
			 if ( this.getY() < this.enemyWay.get( this.currentWay ).y )
			 {
				 return false;
			 } else
			 {
				 this.setY( this.enemyWay.get( this.currentWay ).y );
			 }
		} else if ( this.speedY < 0 )
		{
			if ( this.getY() > this.enemyWay.get( this.currentWay ).y )
			 {
				 return false;
			 } else
			 {
				 this.setY( this.enemyWay.get( this.currentWay ).y );
			 }
		}
		return true;
	}
	
	public void think( ApoDefenceGame game )
	{
		if ( this.enemyWay == null )
			return;
		
		this.rangeRec		= new Rectangle2D.Double( this.getX() - this.RANGE/2, this.getY() - this.RANGE/2, this.RANGE, this.RANGE );
		
		if ( this.extraTime != -1 )
		{
			this.extraTime	-=	5;
			if ( this.extraTime <= 0 )
			{
				this.extraAfterTime	= 200;
				this.extraTime		= -1;
				this.extraFire		= 0;
				this.bFire			= false;
				this.bIce			= false;
				this.bLight			= false;
			}
		}
		if ( this.extraAfterTime > 0 )
			this.extraAfterTime -= 5;
		
		this.countLookWay	= this.countLookWay + 1;
		if ( this.countLookWay >= 20 )
			this.countLookWay	= 0;
		
		if ( this.getHealth() < 1 )
		{
			this.setHealth( 0 );
			this.setBVisible( false );
			game.setMoney( (int)( game.getMoney() + this.getMoney() ) );
		}
		
		if ( this.shoot.size() != 0 )
		{
			for ( int i = this.shoot.size() - 1; i >= 0; i-- )
			{
				this.shoot.get( i ).think( game );
				if ( ( !this.shoot.get( i ).isBVisible() ) || ( !this.getRangeRec().intersects( this.shoot.get( i ).getRec() ) ) )
				{
					this.shoot.remove( i );
				}	
			}
		}
	}

	public float getCountWay()
	{
		return this.countWay;
	}

	public void setCountWay(float countWay)
	{
		this.countWay = countWay;
	}
	
	public int getCurrentWay()
	{
		return this.currentWay;
	}

	public void setCurrentWay(int currentWay)
	{
		this.currentWay = currentWay;
	}
	
	public float getArmor()
	{
		return this.armor;
	}

	public ApoDefencePoint getDamage()
	{
		return this.damage;
	}

	public float getHealth()
	{
		return this.health;
	}
	
	public void setHealth(float health)
	{
		this.health = health;
	}

	public int getLevel()
	{
		return this.level;
	}

	public float getSpeed()
	{
		return this.speed;
	}

	public boolean isBVisible()
	{
		return this.bVisible;
	}

	public void setBVisible(boolean bVisible)
	{
		this.bVisible = bVisible;
	}
	
	protected Rectangle2D.Double getRec()
	{
		return this.rec;
	}
	
	public void setRec(Rectangle2D.Double rec)
	{
		this.rec = rec;
	}

	protected boolean intersects( Rectangle2D.Double rec )
	{
		return this.rec.intersects( rec );
	}
	
	public boolean isBSelect()
	{
		return this.bSelect;
	}

	public void setBSelect(boolean bSelect)
	{
		this.bSelect = bSelect;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public BufferedImage getIMiniEnemy()
	{
		return this.iMiniEnemy;
	}
	
	public float getMaxHealth()
	{
		return this.maxHealth;
	}
	
	public float getMoney()
	{
		return this.money;
	}

	public void setMoney(float money)
	{
		this.money = money;
	}
	
	public boolean isBFire()
	{
		return this.bFire;
	}

	public void setBFire(boolean bFire)
	{
		this.bFire = bFire;
	}

	public boolean isBIce()
	{
		return this.bIce;
	}

	public void setBIce(boolean bIce)
	{
		this.bIce = bIce;
	}

	public boolean isBLight()
	{
		return this.bLight;
	}

	public void setBLight(boolean bLight)
	{
		this.bLight = bLight;
	}

	public boolean isBUndead()
	{
		return this.bUndead;
	}

	public void setBUndead(boolean bUndead)
	{
		this.bUndead = bUndead;
	}
	
	public boolean isExtra()
	{
		if ( ( ( this.bIce ) || ( this.bFire ) || ( this.bLight ) ) && ( this.extraAfterTime < 1 ) )
			return true;
		return false;
	}
	
	public float getExtraFire()
	{
		return this.extraFire;
	}

	public void setExtraFire(float extraFire)
	{
		this.extraFire = extraFire;
	}
	
	public float getExtraTime()
	{
		return this.extraTime;
	}

	public void setExtraTime(float extraTime)
	{
		this.extraTime = extraTime;
	}
	
	public boolean isBHealthVisible()
	{
		return this.bHealthVisible;
	}

	public void setBHealthVisible(boolean bHealthVisible)
	{
		this.bHealthVisible = bHealthVisible;
	}
	
	public float getSpeedX()
	{
		return this.speedX;
	}

	public void setSpeedX(float speedX)
	{
		this.speedX = speedX;
	}

	public float getSpeedY()
	{
		return this.speedY;
	}

	public void setSpeedY(float speedY)
	{
		this.speedY = speedY;
	}
	
	public ArrayList<ApoDefenceShoot> getShoot()
	{
		return this.shoot;
	}

	public void setShoot(ArrayList<ApoDefenceShoot> shoot)
	{
		this.shoot = shoot;
	}
	
	public Rectangle2D.Double getRangeRec()
	{
		return this.rangeRec;
	}
	
	public boolean isBShoot()
	{
		return this.bShoot;
	}

	public void setBShoot(boolean bShoot)
	{
		this.bShoot = bShoot;
	}
	
	public float getShootTime()
	{
		return this.shootTime;
	}

	public void setShootTime(float shootTime)
	{
		this.shootTime = shootTime;
	}
	
	public int getEnemy()
	{
		return this.enemy;
	}
	
	public boolean isIn( double x, double y, double xm, double ym )
	{
		double dx	= (double)(x-xm);
		double dy	= (double)(y-ym);
		return ((dx/this.RANGE)*(dx/this.RANGE)+(dy/this.RANGE)*(dy/this.RANGE)) <= 0.25;
	}
	
	public long getStartTime()
	{
		return this.startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * malt das Objekt
	 * @param g
	 */
	public void render( Graphics g )
	{
		this.render( (Graphics2D)g );
	}
	
	/**
	 * malt das Objekt
	 * @param g
	 */
	public void render( Graphics2D g, int x, int y )
	{
		if ( this.isBVisible() )
		{
			if ( ( this.isBSelect() ) || ( this.isBHealthVisible() ) )
			{
				g.setColor( Color.GREEN );
				if ( this.isBSelect() )
					g.drawOval( (int)( this.getX() + x - this.getWidth()/2 ), (int)( this.getY() + y - this.getHeight()/2 ), this.getWidth(), this.getHeight() );
				
				double value	= ((double)this.health / (double)this.maxHealth);
				value			= value * 50;
				g.fillRect( (int)( this.getX() + x - 25 ), (int)( this.getY() - this.getHeight()/2 + y - 13 ),  (int)value, 10 );
				g.setColor( Color.RED );
				g.fillRect( (int)( value - 1 + this.getX() + x - 25 ), (int)( this.getY() - this.getHeight()/2 + y - 13 ), 50 - (int)value, 10 );
				g.setColor( Color.BLACK );
				g.drawRect( (int)( this.getX() + x - 25 ), (int)( this.getY() - this.getHeight()/2 + y - 13 ),  49, 10 );
			}
			/*g.setColor( Color.BLACK );
			g.drawRect( (int)( this.rec.getX() + x ), (int)( this.rec.getY() + y ), this.getWidth(), this.getHeight() );*/
			int countLookWay	= this.countLookWay / 5;
			if ( countLookWay >= 3  )
				countLookWay	= 1;
			g.drawImage( this.getIBackground(), (int)( this.getX() + x - this.getWidth()/2 ), (int)( this.getY() + y - this.getHeight()/2 ), (int)( this.getX() + x + this.getWidth()/2 ), (int)( this.getY() + y + this.getHeight()/2 ), countLookWay * this.getWidth(), this.lookWay * this.getHeight(), ( countLookWay + 1 ) * this.getWidth(), ( this.lookWay + 1 ) * this.getHeight(), null );
			
			for ( int i = 0; i < this.shoot.size(); i++ )
			{
				this.shoot.get( i ).render( g, x, y );
			}
			
			//g.setColor( Color.BLACK );
			//g.fillRect( (int)this.getX() + x - 1, (int)this.getY() + y - 1, 3, 3 );
		}
	}

}
