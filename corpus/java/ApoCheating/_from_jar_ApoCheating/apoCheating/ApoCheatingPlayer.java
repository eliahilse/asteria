package apoCheating;

import human.Human;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ApoCheatingPlayer extends ApoCheatingEntity
{
	private final double	speed			= 2;
	private final int		empty			= 0;
	private final int		up				= -1;
	private final int		right			= 1;
	private final int		down			= 1;
	private final int		left			= -1;
	private final int		maxCoinTicks	= 300;
	private final int		throwCoinTicks	= (int)(128/this.speed);
	private int				coins, oldCoins;
	private int 			leftDirection, upDirection;
	private int 			oldLeftDirection, oldUpDirection;
	private int				coinLeftDirection, coinUpDirection;
	private int				currentCoinTicks;
	private double			coinX, coinY;
	private boolean			bAction;
	private boolean			bFinish;
	private boolean			bCoin;
	private boolean			bCoinBounce;
	private double			detect	= 0;
	private Color  			percentColor;
	private Color			cheatColor;
	private double			cheat;
	private int				player;
	private ApoCheatingAI	ai	= new Human();

	public ApoCheatingPlayer( int x, int y, int width, int height, int player )
	{
		super( Color.BLUE, x, y, width, height, ApoCheatingConstants.EMPTY );
		
		this.player		= player;
		this.oldCoins	= 0;
		
		this.ai.setPlayer( player );
		
		this.init();
	}
	
	public ApoCheatingPlayer( int x, int y, int width, int height, int player, int coins )
	{
		super( Color.BLUE, x, y, width, height, ApoCheatingConstants.EMPTY );
		
		this.player		= player;
		this.oldCoins	= coins;
		
		this.ai.setPlayer( player );
		
		this.init();
	}
	
	public ApoCheatingPlayer( BufferedImage iDetect, BufferedImage iEntity, int x, int y, int width, int height, int player, int coins )
	{
		super( iDetect, iEntity, x, y, width, height, ApoCheatingConstants.EMPTY );
		
		this.player		= player;
		this.oldCoins	= coins;
		
		this.ai.setPlayer( player );
		
		this.init();
	}

	protected void init()
	{
		super.init();
		this.oldLeftDirection	= 0;
		this.oldUpDirection		= 0;
		this.leftDirection		= 0;
		this.upDirection		= 0;
		this.coinLeftDirection	= 0;
		this.coinUpDirection	= 0;
		this.detect				= 0;
		this.coinX				= 0;
		this.coinY				= 0;
		this.currentCoinTicks	= 0;
		this.percentColor		= Color.GREEN;
		this.cheat				= 0.0D;
		this.cheatColor			= Color.GREEN;
		this.coins				= this.oldCoins;
		this.bFinish			= false;
		this.bCoin				= false;
		this.bCoinBounce		= false;
	}
	
	protected ApoCheatingAI getAI()
	{
		return this.ai;
	}

	protected void setAI(ApoCheatingAI ai)
	{
		this.ai = ai;
	}

	public String getName()
	{
		if ( ( this.ai != null ) && ( this.ai.getName() != null ) )
			return this.ai.getName();
		else
			return "";
	}
	
	public String getAuthorName()
	{
		if ( ( this.ai != null ) && ( this.ai.getAuthorName() != null ) )
			return this.ai.getAuthorName();
		else
			return "";
	}
	
	public void setDirection( int up, int left )
	{
		this.upDirection	= up;
		this.leftDirection	= left;
	}
	
	public void setAction( boolean bAction )
	{
		this.bAction	= bAction;
	}
	
	protected int getPlayer()
	{
		return this.player;
	}

	protected boolean isBAction()
	{
		return this.bAction;
	}

	protected void setBAction(boolean bAction)
	{
		this.bAction = bAction;
	}

	protected double getSpeed()
	{
		return this.speed;
	}

	protected int getLeftDirection()
	{
		return this.leftDirection;
	}

	protected void setLeftDirection(int leftDirection)
	{
		this.leftDirection = leftDirection;
	}

	protected int getUpDirection()
	{
		return this.upDirection;
	}

	protected void setUpDirection(int upDirection)
	{
		this.upDirection = upDirection;
	}

	public double getDetected()
	{
		return this.detect;
	}

	protected void setDetected(double percent)
	{
		if ( percent > 100 )
			percent	= 100;
		this.detect = percent;
	}
	
	protected Color getPercentColor()
	{
		return this.percentColor;
	}

	protected void setPercentColor(Color percentColor)
	{
		this.percentColor = percentColor;
	}
	
	public double getCheated()
	{
		return this.cheat;
	}

	protected void setCheat(double cheat)
	{
		if ( cheat > 100 )
			cheat	= 100;
		this.cheat = cheat;
	}

	protected Color getCheatColor()
	{
		return this.cheatColor;
	}

	protected void setCheatColor( Color cheatColor )
	{
		this.cheatColor = cheatColor;
	}
	
	protected int getCoins()
	{
		return coins;
	}

	protected boolean isBCoin()
	{
		return this.bCoin;
	}

	protected void setBCoin( boolean bCoin )
	{
		this.bCoin = bCoin;
	}

	protected boolean isBCoinBounce()
	{
		if ( this.bCoinBounce )
		{
			this.bCoinBounce	= false;
			return true;
		}
		return bCoinBounce;
	}
	
	protected boolean isBFinish()
	{
		return this.bFinish;
	}

	protected void setBFinish(boolean bFinish)
	{
		this.bFinish = bFinish;
	}
	
	protected void think( int[][] aPlayground, ApoCheatingPlayer player, ArrayList<ApoCheatingEntity> entity, ApoCheatingEnemy enemy, ArrayList<ApoCheatingTeacher> teacher )
	{
		if ( this.ai != null )
			this.ai.think( aPlayground, player, entity, enemy, teacher );
			
	}
	
	protected void setNewXAndY( boolean left, boolean up )
	{
		if ( ( this.upDirection == this.empty ) && ( this.leftDirection == this.empty ) )
			return;
		this.oldUpDirection		= this.upDirection;
		this.oldLeftDirection	= this.leftDirection;
		if ( ( this.leftDirection == this.left ) && ( left ) )
			this.setX( this.getX() + this.leftDirection*this.speed );
		if ( ( this.leftDirection == this.right ) && ( left ) )
			this.setX( this.getX() + this.leftDirection*this.speed );
		if ( ( this.upDirection == this.up ) && ( up ) )
			this.setY( this.getY() + this.upDirection*this.speed );
		if ( ( this.upDirection == this.down ) && ( up ) )
			this.setY( this.getY() + this.upDirection*this.speed );
	}
	
	public void throwCoin()
	{
		if ( ( !this.bCoin ) && ( this.coins > 0 ) )
		{
			this.coins				-= 1;
			this.currentCoinTicks	= 0;
			this.bCoin				= true;
			this.coinLeftDirection	= this.oldLeftDirection;
			this.coinUpDirection	= this.oldUpDirection;
			this.coinX				= this.getX() + this.getWidth()/2;
			this.coinY				= this.getY() + this.getHeight()/2;
		}
	}
	
	protected void render( Graphics2D g )
	{
		super.render( g );
		
		if ( this.bCoin )
		{
			g.setColor( Color.YELLOW );
			g.fillOval( (int)this.coinX - 3, (int)this.coinY - 3, 6, 6 );
			g.setColor( Color.BLACK );
			g.drawOval( (int)this.coinX - 3, (int)this.coinY - 3, 6, 6 );
		}
	}

	protected int getCoinLeftDirection() {
		return coinLeftDirection;
	}

	protected void setCoinLeftDirection(int coinLeftDirection) {
		this.coinLeftDirection = coinLeftDirection;
	}

	protected int getCoinUpDirection() {
		return coinUpDirection;
	}

	protected void setCoinUpDirection(int coinUpDirection) {
		this.coinUpDirection = coinUpDirection;
	}

	protected double getCoinX() {
		return coinX;
	}

	protected void setCoinX(double coinX) {
		this.coinX = coinX;
	}

	protected double getCoinY() {
		return coinY;
	}

	protected void setCoinY(double coinY) {
		this.coinY = coinY;
	}

	protected int getCurrentCoinTicks() {
		return currentCoinTicks;
	}

	protected void setCurrentCoinTicks(int currentCoinTicks) {
		this.currentCoinTicks = currentCoinTicks;
	}

	protected int getMaxCoinTicks() {
		return maxCoinTicks;
	}

	protected int getThrowCoinTicks() {
		return throwCoinTicks;
	}

	protected void setCoins(int coins) {
		this.coins = coins;
	}

	protected void setBCoinBounce(boolean coinBounce) {
		bCoinBounce = coinBounce;
	}

}
