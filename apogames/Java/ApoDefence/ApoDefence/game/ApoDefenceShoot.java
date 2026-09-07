package apoDefence.game;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceEntity;
import apoDefence.ApoDefencePoint;

public class ApoDefenceShoot extends ApoDefenceEntity
{	
	private float				speedX;
	private float				speedY;
	
	private ApoDefencePoint		damage;
	
	private boolean				bVisible;
	
	private boolean				bIce, bFire, bLight, bUndead;
	
	private Rectangle2D.Double	rec;
	
	private ApoDefenceTower 	tower;
	private ApoDefenceEnemy		enemy;
	
	public ApoDefenceShoot( ApoDefenceTower tower, double x, double y, double width, double height )
	{
		super( null, x, y, (int)width, (int)height );
		
		this.bVisible		= true;
		this.bIce			= false;
		this.bFire			= false;
		this.bLight			= false;
		this.bUndead			= false;
		this.tower			= tower;
	}
	
	public ApoDefenceShoot( ApoDefenceEnemy enemy, double x, double y, double width, double height )
	{
		super( null, x, y, (int)width, (int)height );
		
		this.bVisible		= true;
		this.bIce			= false;
		this.bFire			= false;
		this.bLight			= false;
		this.bUndead			= false;
		this.enemy			= enemy;
	}
	
	public void setValues( float speedX, float speedY, ApoDefencePoint damage )
	{
		this.speedX		= speedX;
		this.speedY		= speedY;
		
		this.damage		= damage;
	}
	
	public Rectangle2D.Double getRec()
	{
		return this.rec;
	}

	public void setRec(Rectangle2D.Double rec)
	{
		this.rec = rec;
	}
	
	public float getSpeedX()
	{
		return this.speedX;
	}

	public float getSpeedY()
	{
		return this.speedY;
	}

	public boolean isBVisible()
	{
		return this.bVisible;
	}
	
	public void setBVisible(boolean bVisible)
	{
		this.bVisible = bVisible;
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
		if ( ( this.bIce ) || ( this.bFire ) || ( this.bLight ) )
			return true;
		return false;
	}
	
	public ApoDefencePoint getDamage()
	{
		return this.damage;
	}

	public void setDamage(ApoDefencePoint damage)
	{
		this.damage = damage;
	}
	
	public ApoDefenceTower getTower()
	{
		return this.tower;
	}

	public void setTower(ApoDefenceTower tower)
	{
		this.tower = tower;
	}
	
	public ApoDefenceEnemy getEnemy()
	{
		return this.enemy;
	}

	public void setEnemy(ApoDefenceEnemy enemy) {
		this.enemy = enemy;
	}
	
	public void think( ApoDefenceGame game )
	{
		if ( ( !this.bVisible ) || ( this.tower == null ) )
			return;
		this.setRec( new Rectangle2D.Double( this.getX(), this.getY(), this.getWidth(), this.getHeight() ) );
		
		//System.out.println( "speedX = "+this.getSpeedX() );
		//System.out.println( "speedY = "+this.getSpeedY() );
		
		this.setX( this.getX() + this.getSpeedX() );
		this.setY( this.getY() + this.getSpeedY() );
		
		ArrayList<ApoDefenceEnemy>	enemy	= game.getGameEnemy().getAllEnemy();
		for ( int i = 0; i < enemy.size(); i++ )
		{
			if ( enemy.get( i ).getRec().intersects( this.getRec() ) )
			{
				int damage	= (int)( Math.random() * ( this.damage.y - this.damage.x ) + this.damage.x - enemy.get( i ).getArmor() * ( 1 - game.getResearch().getAttackLevel() * 0.1 ) );
				//System.out.println( "damage = "+damage+" und armor = "+(enemy.get( i ).getArmor() * ( 1 - game.getResearch().getAttackLevel() * 0.15 )) );
				
				this.setBVisible( false );
				if ( ( !enemy.get( i ).isExtra() ) && ( enemy.get( i ).getExtraTime() < 0 ) )
				{
					int enemyValue	= enemy.get( i ).getEnemy();
					if ( this.isBFire() )
					{
						enemy.get( i ).setExtraTime( 100 + this.tower.getLevel() * 10 );
						enemy.get( i ).setBFire( true );
						float fireDamage	= this.getDamage().x * 0.05f;
						if ( fireDamage > 0.35f )
							fireDamage	= 0.35f;
						if ( 	( enemyValue == ApoDefenceConstants.ENEMY_AQUATIC ) ||
								( enemyValue == ApoDefenceConstants.ENEMY_DRACULA ) )
						{
							fireDamage	= (int)( (float)fireDamage * 1.15f );
							damage		= (int)( (float)damage * 1.23f );
						}
						enemy.get( i ).setExtraFire( fireDamage );
					} else if ( this.bIce )
					{
						if ( enemyValue == ApoDefenceConstants.ENEMY_AQUATIC )
						{
							damage		= (int)( 0.25 * (float)damage );
							enemy.get( i ).setBIce( false );
							enemy.get( i ).setExtraTime( -1 );
						} else
						{
							if ( ( enemyValue == ApoDefenceConstants.ENEMY_DEVIL ) || 
								 ( enemyValue == ApoDefenceConstants.ENEMY_FIRE ) ||
								 ( enemyValue == ApoDefenceConstants.ENEMY_DRAGON ) )
							{
								damage		= (int)( (float)damage * 1.23f );
							}
							enemy.get( i ).setBIce( true );
							enemy.get( i ).setExtraTime( 150 + this.tower.getLevel() * 15 );
						}
					} else if ( this.bLight )
					{
						if ( ( enemyValue == ApoDefenceConstants.ENEMY_GHOST ) ||
							 ( enemyValue == ApoDefenceConstants.ENEMY_DRACULA ) ||
							 ( enemyValue == ApoDefenceConstants.ENEMY_FIRE ) ||
							 ( enemyValue == ApoDefenceConstants.ENEMY_DRAGON ) ||
							 ( enemyValue == ApoDefenceConstants.ENEMY_DEVIL ) )
						{
							damage		= (int)( 0.25 * (float)damage );
							enemy.get( i ).setBLight( false );
							enemy.get( i ).setExtraTime( -1 );
						} else
						{
							enemy.get( i ).setBLight( true );
							enemy.get( i ).setExtraTime( 125 + this.tower.getLevel() * 8 );
						}
					}
				}
				if ( this.tower != null )
				{
					if ( enemy.get( i ).isBUndead() )
					{
						if ( this.tower.getTower() == ApoDefenceConstants.TOWER_ULTRA )
							damage		= (int)( ApoDefenceConstants.ENEMY_IS_UNDEAD_ULTRA * damage );
						else if ( this.tower.getTower() == ApoDefenceConstants.TOWER_FIRE )
							damage		= (int)( ApoDefenceConstants.ENEMY_IS_UNDEAD_FIRE * damage );
					} else
					{
						if ( this.tower.getTower() == ApoDefenceConstants.TOWER_ICE )
							damage		= (int)( ApoDefenceConstants.ENEMY_IS_NOT_UNDEAD_ICE * damage );
						else if ( this.tower.getTower() == ApoDefenceConstants.TOWER_LIGHT )
							damage		= (int)( ApoDefenceConstants.ENEMY_IS_NOT_UNDEAD_LIGHT * damage );
					}
				}
				if ( damage > 0  )
					enemy.get( i ).setHealth( enemy.get( i ).getHealth() - damage );
				
				if ( enemy.get( i ).getHealth() <= 0 )
				{
					float points	= enemy.get( i ).getMoney() - TimeUnit.NANOSECONDS.toSeconds( System.nanoTime() - enemy.get( i ).getStartTime() );
					if ( points > 0 )
						game.setPoints( game.getPoints() + points );
				}
				break;
			}
		}
	}
	
	public void render( Graphics2D g, int x, int y )
	{
		
	}

}
