package apoCheating;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class ApoCheatingSearch
{
	private boolean[][]					aWay;
	private ArrayList<Point> 			searchWay;
	private ApoCheatingSearchClass[][]	aSearch;
	private LinkedList 					openList = new LinkedList();
	private LinkedList 					closedList = new LinkedList();
	private Point						startPoint, finishPoint;
	
	public ApoCheatingSearch()
	{}

	@SuppressWarnings("unchecked")
	protected ArrayList<Point> findShortestWay( boolean[][] aWay, Point start, Point finish )
	{
		this.startPoint		= start;
		//System.out.println("startX = "+start.x+" startY = "+start.y);
		this.finishPoint	= finish;
		//System.out.println("finishX = "+finish.x+" finishY = "+finish.y);
		this.aWay			= aWay;
		this.aSearch		= new ApoCheatingSearchClass[this.aWay.length][this.aWay[0].length];
		int x				= aWay[0].length;
		int y				= aWay.length;
		for ( int i = 0; i < y; i++ )
		{
			for ( int j = 0; j < x; j++ )
			{
				/*if ( this.aWay[i][j] )
					System.out.print(" ");
				else
					System.out.print("#");*/
				this.aSearch[i][j]	= new ApoCheatingSearchClass( new Point(j, i) );
				this.aSearch[i][j].exCostCalculation( this.finishPoint );
			}
			//System.out.println();
		}
		this.openList.clear();
		this.closedList.clear();
		
		this.openList.add( this.startPoint );
		
		this.aStar();

		this.searchWay	= new ArrayList<Point>();
		x			= this.finishPoint.x;
		y			= this.finishPoint.y;
		
		if ( this.aSearch[this.finishPoint.y][this.finishPoint.x].getCost() == 0 )
		{
			int minCost		= 1000000;
			for ( int i = 0; i < this.aWay.length; i++ )
			{
				for ( int j = 0; j < this.aWay[0].length; j++ )
				{
					//System.out.println( "Check mit cost = "+this.aSearch[i][j].getCost()+" minCost = "+this.aSearch[i][j].getCompleteCost()+" x = "+j+" y = "+i );
					if ( ( this.aSearch[i][j].getCompleteCost() < minCost ) && ( this.aSearch[i][j].getCompleteCost() > 0 ) && 
						 ( this.aSearch[i][j].getCost() > 0 ) )
					{
						minCost	= this.aSearch[i][j].getCompleteCost();
						x		= j;
						y		= i;
						//System.out.println( "x = "+x+" y = "+y+" minCost = "+minCost );
					}
				}
			}
		}
		
		//System.out.println(" x = "+x+" y = "+y);
		
		Point p		= new Point( x, y );

		Point currentPoint	= this.aSearch[p.y][p.x].getPoint();
		x				= 0;
		while ( ( this.startPoint != currentPoint ) && ( x < this.aWay.length*this.aWay[0].length ) )
		{
			//System.out.println( "x = "+currentPoint.x+" y = "+currentPoint.y);
			this.searchWay.add( currentPoint );
			currentPoint	= this.aSearch[currentPoint.y][currentPoint.x].getPrevious();
			x				+= 1;
		}

		return this.searchWay;
	}
	
	@SuppressWarnings("unchecked")
	private void aStar()
	{
		Point p = new Point(this.startPoint.x, this.startPoint.y);
        //System.out.println("x = "+p.x+" y = "+p.y);
        while ( !this.openList.isEmpty() )
        {
        	if ( ( p.x == this.finishPoint.x ) &&
        		 ( p.y == this.finishPoint.y ) )
        		return;
            p = (Point) this.openList.removeFirst(); // erstes Element aus der Queue
            this.closedList.addLast(p);
            this.aSearch[p.y][p.x].setBClosed( true );
        	this.expansion( p );
        }
	}
	
	private void expansion( Point p )
	{
		this.expansionPoint( p,  0, -1 ); // nach Norden
		this.expansionPoint( p, +1,  0 ); // nach Osten
		this.expansionPoint( p,  0, +1 ); // nach Sueden
		this.expansionPoint( p, -1,  0 ); // nach Westen
	}
	
	public void expansionPoint( Point p, int x, int y )
	{
		Point newPoint	= new Point( p.x + x, p.y + y );
		if ( 	( newPoint.x < this.aWay[0].length ) && 
				( newPoint.y < this.aWay.length ) &&
				( newPoint.x >= 0 ) && 
				( newPoint.y >= 0 ) &&
				( this.aWay[newPoint.y][newPoint.x] )  )
		{
			int cost	=  this.aSearch[p.y][p.x].getCost() + this.aSearch[newPoint.y][newPoint.x].getCost() + 1;
			if ( ( !this.isInOpenList( newPoint ) ) && ( !this.isInClosedList( newPoint ) ) )
			{
				this.aSearch[newPoint.y][newPoint.x].setCost( cost );
				this.aSearch[newPoint.y][newPoint.x].setPrevious( p );
				this.addToOpenList( newPoint );                
			} else if ( this.isInClosedList( newPoint ) )
			{
				if ( this.aSearch[p.y][p.x].getCost() > cost )
				{
					this.aSearch[newPoint.y][newPoint.x].setCost( cost );
					this.aSearch[newPoint.y][newPoint.x].setPrevious( p );
					this.closedList.remove( newPoint );
					this.addToOpenList( newPoint );
                }
            } else if ( this.isInOpenList( newPoint ) )
            {
            	if ( this.aSearch[p.y][p.x].getCost() > cost )
				{
            		this.aSearch[newPoint.y][newPoint.x].setCost( cost );
            		this.aSearch[newPoint.y][newPoint.x].setPrevious( p );
            		this.openList.remove( newPoint );
            		this.addToOpenList( newPoint );
				}
            }
		}
	}
	
	private boolean isInOpenList(Point point)
	{        
		if ( this.openList.indexOf( point ) != -1)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isInClosedList(Point point)
	{
		if ( this.closedList.indexOf( point ) != -1)
		{
			return true;
		} else
		{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addToOpenList(Point point )
	{
		double cost	= this.aSearch[point.y][point.x].getCompleteCost();
		if ( this.openList.isEmpty() )
		{
			//System.out.println("openList x = "+point.x+" y = "+point.y);
			this.openList.add(0, point);
		} else
		{
			for (int i = this.openList.size()-1; i >= 0; i--)
			{
				Point p = (Point)this.openList.get(i);
				ApoCheatingSearchClass current	= this.aSearch[p.y][p.x];
				if ( cost > current.getCompleteCost() )
				{
					//System.out.println("openList x = "+point.x+" y = "+point.y);
					this.openList.add(i+1, point);
					break;
				} else if (i == 0)
				{
					//System.out.println("openList x = "+point.x+" y = "+point.y);
					this.openList.add(0, point);
					break;
				}
			}
		}
	}
	
}
