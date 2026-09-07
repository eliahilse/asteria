package apoDefence.game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceDragObject;
import apoDefence.ApoDefenceImage;

public class ApoDefenceGameEnemy
{
	private	ApoDefenceGame					game;
	
	private BufferedImage					iEnemyBird;
	private BufferedImage					iEnemyDog;
	private BufferedImage					iEnemyGhost;
	private BufferedImage					iEnemyLion;
	private BufferedImage					iEnemyHorse;
	private BufferedImage					iEnemyDevil;
	private BufferedImage					iEnemyAquatic;
	private BufferedImage					iEnemyDracula;
	private BufferedImage					iEnemyFire;
	private BufferedImage					iEnemyDragon;
	private BufferedImage					iEnemyGoblin;
	private BufferedImage					iEnemyKnight;
	private BufferedImage					iEnemyMonk;
	private BufferedImage					iEnemyMummy;
	private BufferedImage					iEnemySkeleton;
	private BufferedImage					iEnemySkull;
	private BufferedImage					iEnemySnake;
	private BufferedImage					iEnemyUndead;
	private BufferedImage					iEnemyUndefined;
	private BufferedImage					iEnemyWerewolf;
	
	private ArrayList<Point>				enemyWay;
	
	private Point							choosenEnemy;
	
	private ArrayList<ApoDefenceEnemy>		enemy;
	
	private ArrayList<Integer>				enemyOrder;
	
	private int								count;

	public ApoDefenceGameEnemy( ApoDefenceGame game )
	{
		super();
		
		this.game		= game;
		
		ApoDefenceImage	image	= new ApoDefenceImage();
		
		this.iEnemyBird			= image.getPic( "/images/monster/monster_bird.png", false );
		this.iEnemyDog			= image.getPic( "/images/monster/monster_dog.png", false );
		this.iEnemyGhost		= image.getPic( "/images/monster/monster_ghost.png", false );
		this.iEnemyLion			= image.getPic( "/images/monster/monster_lion.png", false );
		this.iEnemyHorse		= image.getPic( "/images/monster/monster_horse.png", false );
		this.iEnemyDevil		= image.getPic( "/images/monster/monster_devil.png", false );
		this.iEnemyAquatic		= image.getPic( "/images/monster/monster_aquatic.png", false );
		this.iEnemyDracula		= image.getPic( "/images/monster/monster_dracula.png", false );
		this.iEnemyFire			= image.getPic( "/images/monster/monster_fire.png", false );
		this.iEnemyDragon		= image.getPic( "/images/monster/monster_dragon.png", false );
		this.iEnemyGoblin		= image.getPic( "/images/monster/monster_goblin.png", false );
		this.iEnemyKnight		= image.getPic( "/images/monster/monster_knight.png", false );
		this.iEnemyMonk			= image.getPic( "/images/monster/monster_monk.png", false );
		this.iEnemyMummy		= image.getPic( "/images/monster/monster_mummy.png", false );
		this.iEnemySkeleton		= image.getPic( "/images/monster/monster_skeleton.png", false );
		this.iEnemySkull		= image.getPic( "/images/monster/monster_skull.png", false );
		this.iEnemySnake		= image.getPic( "/images/monster/monster_snake.png", false );
		this.iEnemyUndead		= image.getPic( "/images/monster/monster_undead.png", false );
		this.iEnemyUndefined	= image.getPic( "/images/monster/monster_undefined.png", false );
		this.iEnemyWerewolf		= image.getPic( "/images/monster/monster_werewolf.png", false );
		
		this.enemy				= new ArrayList<ApoDefenceEnemy>();
		
		this.enemyOrder			= new ArrayList<Integer>();
		this.enemyOrder.add( ApoDefenceConstants.ENEMY_BIRD );
		this.count				= 0;
	}

	public String getNextEnemy()
	{
		int value				= 1;
		if ( ( this.enemyOrder != null ) && ( this.count < this.enemyOrder.size() ) )
		{
			value				= this.enemyOrder.get( this.count );
		}
		if ( value == ApoDefenceConstants.ENEMY_BIRD )
			return ApoDefenceConstants.ENEMY_BIRD_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_GHOST )
			return ApoDefenceConstants.ENEMY_GHOST_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_DOG )
			return ApoDefenceConstants.ENEMY_DOG_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_LION )
			return ApoDefenceConstants.ENEMY_LION_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_HORSE )
			return ApoDefenceConstants.ENEMY_HORSE_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_DEVIL )
			return ApoDefenceConstants.ENEMY_DEVIL_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_AQUATIC )
			return ApoDefenceConstants.ENEMY_AQUATIC_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_DRACULA )
			return ApoDefenceConstants.ENEMY_DRACULA_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_FIRE )
			return ApoDefenceConstants.ENEMY_FIRE_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_DRAGON )
			return ApoDefenceConstants.ENEMY_DRAGON_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_GOBLIN )
			return ApoDefenceConstants.ENEMY_GOBLIN_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_KNIGHT )
			return ApoDefenceConstants.ENEMY_KNIGHT_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_MONK )
			return ApoDefenceConstants.ENEMY_MONK_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_MUMMY )
			return ApoDefenceConstants.ENEMY_MUMMY_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_SKELETON )
			return ApoDefenceConstants.ENEMY_SKELETON_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_SKULL )
			return ApoDefenceConstants.ENEMY_SKULL_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_SNAKE )
			return ApoDefenceConstants.ENEMY_SNAKE_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_UNDEAD )
			return ApoDefenceConstants.ENEMY_UNDEAD_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_UNDEFINED )
			return ApoDefenceConstants.ENEMY_UNDEFINED_NAME;
		else if ( value == ApoDefenceConstants.ENEMY_WEREWOLF )
			return ApoDefenceConstants.ENEMY_WEREWOLF_NAME;
		return "";
	}
	
	public ArrayList<Integer> getEnemyOrder()
	{
		return this.enemyOrder;
	}

	public void setEnemyOrder(ArrayList<Integer> enemyOrder)
	{
		this.enemyOrder = enemyOrder;
	}
	
	public ArrayList<Point> getEnemyWay()
	{
		return this.enemyWay;
	}

	public void setEnemyWay(ArrayList<ApoDefenceDragObject> enemyWay)
	{
		this.enemyWay = new ArrayList<Point>();
		for ( int i = 0; i < enemyWay.size(); i++ )
		{
			this.enemyWay.add( new Point( (int)enemyWay.get( i ).getX(), (int)enemyWay.get( i ).getY() ) );
		}
		this.count				= 0;
	}
	
	public void makeNewWave( int level )
	{
		level					= level + 0;
		this.enemy				= new ArrayList<ApoDefenceEnemy>();
		int value				= 1;
		if ( ( this.enemyOrder != null ) && ( this.count < this.enemyOrder.size() ) )
		{
			value				= this.enemyOrder.get( this.count );
		}
		if ( value == ApoDefenceConstants.ENEMY_BIRD )
			this.enemy.add( new ApoDefenceEnemyBird( this.iEnemyBird, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_GHOST )
			this.enemy.add( new ApoDefenceEnemyGhost( this.iEnemyGhost, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_DOG )
			this.enemy.add( new ApoDefenceEnemyDog( this.iEnemyDog, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_LION )
			this.enemy.add( new ApoDefenceEnemyLion( this.iEnemyLion, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_HORSE )
			this.enemy.add( new ApoDefenceEnemyHorse( this.iEnemyHorse, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_DEVIL )
			this.enemy.add( new ApoDefenceEnemyDevil( this.iEnemyDevil, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_AQUATIC )
			this.enemy.add( new ApoDefenceEnemyAquatic( this.iEnemyAquatic, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_DRACULA )
			this.enemy.add( new ApoDefenceEnemyDracula( this.iEnemyDracula, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_FIRE )
			this.enemy.add( new ApoDefenceEnemyFire( this.iEnemyFire, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_DRAGON )
			this.enemy.add( new ApoDefenceEnemyDragon( this.iEnemyDragon, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_GOBLIN )
			this.enemy.add( new ApoDefenceEnemyGoblin( this.iEnemyGoblin, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_KNIGHT )
			this.enemy.add( new ApoDefenceEnemyKnight( this.iEnemyKnight, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_MONK )
			this.enemy.add( new ApoDefenceEnemyMonk( this.iEnemyMonk, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_MUMMY )
			this.enemy.add( new ApoDefenceEnemyMummy( this.iEnemyMummy, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_SKELETON )
			this.enemy.add( new ApoDefenceEnemySkeleton( this.iEnemySkeleton, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_SKULL )
			this.enemy.add( new ApoDefenceEnemySkull( this.iEnemySkull, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_SNAKE )
			this.enemy.add( new ApoDefenceEnemySnake( this.iEnemySnake, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_UNDEAD )
			this.enemy.add( new ApoDefenceEnemyUndead( this.iEnemyUndead, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_UNDEFINED )
			this.enemy.add( new ApoDefenceEnemyUndefined( this.iEnemyUndefined, this.enemyWay, level ) );
		else if ( value == ApoDefenceConstants.ENEMY_WEREWOLF )
			this.enemy.add( new ApoDefenceEnemyWerewolf( this.iEnemyWerewolf, this.enemyWay, level ) );
		this.enemy.get( this.enemy.size() - 1 ).setBHealthVisible( this.game.isBHealthVisibleEnemy() );
		for ( int i = 1; i < ApoDefenceConstants.MAX_ENEMIES; i++ )
		{
			int x			= (int)this.enemyWay.get( 0 ).getX();
			int y			= (int)this.enemyWay.get( 0 ).getY();
			float speedX	= this.enemy.get( 0 ).getSpeedX();
			float speedY	= this.enemy.get( 0 ).getSpeedY();
			int xValue	= 0;
			int yValue	= 0;
			float sum	= Math.abs( speedX ) + Math.abs( speedY );
			//System.out.println( "x = "+x+" y = "+y+" speedX = "+speedX+" speedY = "+speedY );
			xValue		= Math.abs( (int)( ( speedX / sum ) * Math.abs( sum * 40 * i ) ) );
			if ( speedX > 0 )
				xValue	= -xValue;
			yValue		= Math.abs( (int)( ( speedY / sum ) * Math.abs( sum * 40 * i ) ) );
			if ( speedY > 0 )
				yValue	= -yValue;
			//System.out.println( "sum = "+sum+" xValue = "+(x+xValue)+" yValue = "+(y+yValue) );
			if ( value == ApoDefenceConstants.ENEMY_BIRD )
				this.enemy.add( new ApoDefenceEnemyBird( this.iEnemyBird, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_GHOST )
				this.enemy.add( new ApoDefenceEnemyGhost( this.iEnemyGhost, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_DOG )
				this.enemy.add( new ApoDefenceEnemyDog( this.iEnemyDog, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_LION )
				this.enemy.add( new ApoDefenceEnemyLion( this.iEnemyLion, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_HORSE )
				this.enemy.add( new ApoDefenceEnemyHorse( this.iEnemyHorse, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_DEVIL )
				this.enemy.add( new ApoDefenceEnemyDevil( this.iEnemyDevil, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_AQUATIC )
				this.enemy.add( new ApoDefenceEnemyAquatic( this.iEnemyAquatic, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_DRACULA )
				this.enemy.add( new ApoDefenceEnemyDracula( this.iEnemyDracula, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_FIRE )
				this.enemy.add( new ApoDefenceEnemyFire( this.iEnemyFire, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_DRAGON )
				this.enemy.add( new ApoDefenceEnemyDragon( this.iEnemyDragon, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_GOBLIN )
				this.enemy.add( new ApoDefenceEnemyGoblin( this.iEnemyGoblin, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_KNIGHT )
				this.enemy.add( new ApoDefenceEnemyKnight( this.iEnemyKnight, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_MONK )
				this.enemy.add( new ApoDefenceEnemyMonk( this.iEnemyMonk, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_MUMMY )
				this.enemy.add( new ApoDefenceEnemyMummy( this.iEnemyMummy, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_SKELETON )
				this.enemy.add( new ApoDefenceEnemySkeleton( this.iEnemySkeleton, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_SKULL )
				this.enemy.add( new ApoDefenceEnemySkull( this.iEnemySkull, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_SNAKE )
				this.enemy.add( new ApoDefenceEnemySnake( this.iEnemySnake, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_UNDEAD )
				this.enemy.add( new ApoDefenceEnemyUndead( this.iEnemyUndead, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_UNDEFINED )
				this.enemy.add( new ApoDefenceEnemyUndefined( this.iEnemyUndefined, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			else if ( value == ApoDefenceConstants.ENEMY_WEREWOLF )
				this.enemy.add( new ApoDefenceEnemyWerewolf( this.iEnemyWerewolf, this.enemyWay, (int)(Math.random() * 1 + level), (int)x + xValue, (int)y + yValue ) );
			this.enemy.get( this.enemy.size() - 1 ).setBHealthVisible( this.game.isBHealthVisibleEnemy() );
		}
		
		if ( this.count + 1 >= this.enemyOrder.size() )
			this.count	= 0;
		else
			this.count	= this.count + 1;
	}
	
	public void setCount( int count )
	{
		this.count		= count;
	}
	
	public void setHealthVisible( boolean bHealthVisible )
	{
		for ( int i = 0; i < this.enemy.size(); i++ )
		{
			this.enemy.get( i ).setBHealthVisible( bHealthVisible );
		}
	}
	
	public ApoDefenceEnemy getChoosenEnemy()
	{
		if ( this.choosenEnemy.x == ApoDefenceConstants.ENEMY_BIRD )
			return this.enemy.get( this.choosenEnemy.y );
		
		return null;
	}
	
	private boolean enemyCheck( ArrayList<ApoDefenceEnemy> enemy, Rectangle2D.Double rec )
	{
		for ( int i = 0; i < enemy.size(); i++ )
		{
			if ( enemy.get( i ).intersects( rec ) )
			{
				this.choosenEnemy	= new Point( ApoDefenceConstants.ENEMY_BIRD, i );
				return false;
			}
		}

		return true;
	}
	
	protected boolean mouseReleased(int x, int y)
	{
		Rectangle2D.Double rec = new Rectangle2D.Double( x, y, 1, 1 );
		if ( 	( !this.enemyCheck( this.enemy, rec ) ) )
				return true;
		return false;
	}
	
	public void think()
	{
		for ( int i = this.enemy.size() - 1; i >= 0; i-- )
		{
			if ( this.enemy.get( i ).isBVisible() )
				this.enemy.get( i ).think( this.game );
			else
				this.enemy.remove( i );
		}
	}
	
	public ArrayList<ApoDefenceEnemy> getAllEnemy()
	{
		return this.enemy;
	}
	
	public void removeAllEnemies()
	{
		for ( int i = this.enemy.size() - 1; i >= 0; i-- )
		{
			this.enemy.remove( i );
		}
	}
	
	public void render( Graphics2D g, Rectangle2D.Double rec, int x, int y, int mouseX, int mouseY )
	{
		if ( this.enemy.size() != 0 )
		{
			if ( ( this.enemy.get( 0 ).getSpeedY() < 0 ) && ( Math.abs( this.enemy.get( 0 ).getSpeedY() ) > Math.abs( this.enemy.get( 0 ).getSpeedX() ) ) )
			{
				for ( int i = 0; i < this.enemy.size(); i++ )
				{
					this.enemy.get( i ).render( g, x, y );
				}
			} else
			{
				for ( int i = this.enemy.size() - 1; i >= 0; i-- )
				{
					this.enemy.get( i ).render( g, x, y );
				}
			}
		}
	}

}
