package apoCheating;

import java.awt.Point;

public class ApoCheatingSearchClass
{
	private Point	previous, my;
	private int		cost, exCost;
	private boolean bClosed;
	
	public ApoCheatingSearchClass( Point my )
	{
		super();
		
		this.my			= my;
		
		this.cost		= 0;
		this.exCost		= -1;
		this.previous	= new Point( -1, -1 );
		this.bClosed	= false;
	}
	
	protected int getCost()
	{
		return this.cost;
	}

	protected void setCost(int cost)
	{
		//System.out.println("x = "+my.x+" y = "+my.y+" cost = "+cost);
		this.cost = cost;
	}

	protected int getExCost()
	{
		return exCost;
	}

	protected void setExCost(int exCost)
	{
		this.exCost = exCost;
	}
	
	protected void exCostCalculation( Point finishPoint )
	{
		this.exCost	= Math.abs( this.my.x- finishPoint.x ) + Math.abs( this.my.y - finishPoint.y );
	}

	protected Point getPrevious() 
	{
		return this.previous;
	}

	protected void setPrevious(Point previous)
	{
		this.previous = previous;
	}
	
	protected Point getPoint() 
	{
		return this.my;
	}

	protected boolean isBClosed()
	{
		return this.bClosed;
	}

	protected void setBClosed(boolean bClosed)
	{
		this.bClosed = bClosed;
	}
	
	protected int getCompleteCost()
	{
		return ( this.cost + this.exCost );
	}

}
