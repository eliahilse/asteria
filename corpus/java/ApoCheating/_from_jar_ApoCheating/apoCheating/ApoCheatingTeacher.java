package apoCheating;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ApoCheatingTeacher extends ApoCheatingEntity
{
	private Color								cheatColor;
	private ArrayList<ApoCheatingTeacherWay>	way, coinWay;
	private ArrayList<ApoCheatingTeacherView>	view, coinView;
	private int									currentWay, currentView, currentCoinWay;
	private double								plus, oldPlus;
	private boolean								bCoin;
	
	public ApoCheatingTeacher( int x, int y, int width, int height )
	{
		super( Color.RED, x, y, width, height, ApoCheatingConstants.TEACHER );
		
		this.oldPlus	= 5.0D;
		this.init( x, y, width, height, this.oldPlus );
	}
	
	public ApoCheatingTeacher( BufferedImage iDetect, BufferedImage iEntity, int x, int y, int width, int height )
	{
		super( iDetect, iEntity, x, y, width, height, ApoCheatingConstants.TEACHER );
		
		this.oldPlus	= 5.0D;
		this.init( x, y, width, height, this.oldPlus );
	}
	
	public ApoCheatingTeacher( int x, int y, int width, int height, double plus )
	{
		super( Color.RED, x, y, width, height, ApoCheatingConstants.TEACHER );
		
		this.oldPlus	= plus;
		this.init( x, y, width, height, this.oldPlus );
	}
	
	public ApoCheatingTeacher( BufferedImage iDetect, BufferedImage iEntity, int x, int y, int width, int height, double plus )
	{
		super( iDetect, iEntity, x, y, width, height, ApoCheatingConstants.TEACHER );
		
		this.oldPlus	= plus;
		this.init( x, y, width, height, this.oldPlus );
	}
	
	protected void init()
	{
		super.init();
		this.init( (int)this.getX(), (int)this.getY(), this.getWidth(), this.getHeight(), this.oldPlus );
	}
	
	private void init( int x, int y, int width, int height, double plus )
	{
		this.cheatColor	= Color.GREEN;
		
		this.bCoin				= false;
		this.currentWay			= 0;
		this.currentView		= 0;
		this.currentCoinWay		= 0;
				
		if ( this.view != null )
		{
			for ( int i = 0; i < this.view.size(); i++ )
			{
				this.view.get( i ).init( this );
			}
		} else
			this.view			= new ArrayList<ApoCheatingTeacherView>();

		if ( this.way != null )
		{
			for ( int i = 0; i < this.way.size(); i++ )
			{
				this.way.get( i ).init();
			}
		} else
			this.way			= new ArrayList<ApoCheatingTeacherWay>();

		this.plus				= plus;
	}
	
	protected boolean isVisible( ApoCheatingPlayer player )
	{
		if ( ( (new Rectangle( (int)this.getX() - 15, (int)this.getY() - 15, this.getWidth() + 30, this.getHeight() + 30 )).intersects( new Rectangle( (int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight() ) ) ) && ( !player.isBFinish() ) )
		{
			player.setDetected( 100.0D );
			this.cheatColor	= Color.RED;
			player.setCheatColor( this.cheatColor );
			return true;
		}
		Polygon poly = null;
		if ( ( !this.bCoin ) && ( this.view.size() > 0 ) )
			poly	= this.view.get( this.currentView ).getPolygon();//new Polygon( this.aX, this.aY, this.aX.length );
		else if ( this.bCoin )
			poly	= this.coinView.get( 0 ).getPolygon();
		if ( poly == null )
			return false;
		boolean bCut	= poly.intersects( player.getX(), player.getY(), player.getWidth(), player.getHeight() );
		if ( ( bCut ) && ( player.getDetected() < 100 ) && ( !player.isBFinish() ) )
		{
			int red	= this.cheatColor.getRed() + (int)this.plus;
			int redPlayer	= player.getCheatColor().getRed() + (int)this.plus;
			if ( red > 255 )
				red = 255;
			if ( redPlayer > 255 )
				redPlayer = 255;
			int blue	= this.cheatColor.getBlue();
			int green	= this.cheatColor.getGreen() - (int)this.plus;
			int greenPlayer	= player.getCheatColor().getGreen() - (int)this.plus;
			if ( green < 0 )
				green = 0;
			if ( greenPlayer < 0 )
				greenPlayer = 0;
			player.setDetected( this.plus*100.0D / 255.0D + player.getDetected() );
			this.cheatColor	= new Color( red, green, blue );
			player.setCheatColor( new Color( redPlayer, greenPlayer, blue ) );
		}
		return bCut;
	}
	
	protected void think()
	{
		if ( this.bCoin )
		{
			this.coinView.get( 0 ).next( this );
			if ( !this.coinWay.get( this.currentCoinWay ).next( this ) )
			{
				this.currentCoinWay 	+= 1;
				if ( this.currentCoinWay >= this.coinWay.size() )
				{
					this.bCoin	= false;
					return;
				}
				this.coinWay.get( this.currentCoinWay ).next( this );
			}
		} else
		{
			if ( this.way.size() > 0 )
			{
				if ( !this.way.get( this.currentWay ).next( this ) )
				{
					this.currentWay++;
					if ( this.currentWay >= this.way.size() )
						this.currentWay	= 0;
				}
			}
			if ( this.view.size() > 0 )
			{
				if ( !this.view.get( this.currentView ).next( this ) )
				{
					//System.out.println("Check");
					this.currentView += 1;
					if ( this.currentView >= this.view.size() )
						this.currentView	= 0;
					this.view.get( this.currentView ).makeView( this );
				}
			}
		}
	}
	
	protected Color getCheatColor()
	{
		return this.cheatColor;
	}
	
	protected boolean isCoinHearing( double x, double y )
	{
		if ( this.bCoin )
			return false;
		Rectangle2D.Double r			= new Rectangle2D.Double( this.getX(), this.getY(), this.getWidth(), this.getHeight() );
		Rectangle2D.Double s			= new Rectangle2D.Double( x - 150, y - 150, 300, 300 );
		return s.intersects( r );
	}
	
	protected ArrayList<ApoCheatingTeacherView> getView()
	{
		return this.view;
	}

	protected void setView(ArrayList<ApoCheatingTeacherView> view)
	{
		this.view = view;
	}

	protected ArrayList<ApoCheatingTeacherWay> getWay()
	{
		return this.way;
	}

	protected void setWay(ArrayList<ApoCheatingTeacherWay> way)
	{
		//System.out.println( "waysize == "+way.size());
		this.way = way;
	}
	
	protected void coinHearAndMakeViewAndWay( ArrayList<Point> points )
	{
		this.coinView			= new ArrayList<ApoCheatingTeacherView>();
		this.coinWay			= new ArrayList<ApoCheatingTeacherWay>();
		this.currentCoinWay		= 0;
		int currentX			= (int)(this.getX()+this.getWidth()/2+16);
		int currentY			= (int)(this.getY()+this.getHeight()/2+16);
		double realX			= this.getX();
		double realY			= this.getY();
		
		double finishX			= points.get( points.size()-1 ).getX() * 32 + 16;
		finishX					= finishX*32 + 16;
		double finishY			= points.get( points.size()-1 ).getX() * 32 + 16;
		finishY					= finishY*32 + 16;
		
		int startDir			= 0;
		int endDir				= 0;
		int middleDir			= 0;
		
		double way				= Math.sqrt( Math.pow( Math.abs( currentX - points.get( 0 ).x * 32 ), 2) * Math.pow( Math.abs( currentY - points.get( 0 ).y * 32 ), 2) ); 
		
		currentX			= (int)points.get( 0 ).x * 32;
		currentY			= (int)points.get( 0 ).y * 32;
		
		middleDir			= (int)Math.toDegrees( Math.acos( (currentX - (this.getX()+this.getWidth()/2+16) ) / way ) );
		startDir		= middleDir - 60;
		if ( startDir < 0 )
			startDir	= 360 + startDir;
		endDir			= middleDir + 60;
		if ( endDir >= 360 )
			endDir	= endDir - 360;

		this.coinView.add( new ApoCheatingTeacherView( 50, 270, startDir, endDir, 2.0, 100000 ) );
		this.coinView.get( 0 ).makeView( this );
		
		for ( int i = 1; i < points.size(); i++ )
		{
			int ticks	= -1;
			if ( i == 1 )
				ticks	= 180;
			this.coinWay.add( 0, (new ApoCheatingTeacherWay( points.get( i ).x * 32, points.get( i ).y * 32, currentX, currentY, 1.6, ticks )) );
			this.coinWay.add( this.coinWay.size(), (new ApoCheatingTeacherWay( currentX, currentY, points.get( i ).x * 32, points.get( i ).y * 32, 1.6, -1 )) );
			
			currentX	= points.get( i ).x * 32;
			currentY	= points.get( i ).y * 32;
		}
		
		realX			= this.getX();
		realY			= this.getY();
		if ( this.coinWay.size() == 0 )
		{
			return;
		}
		finishX			= this.coinWay.get( 0 ).getStartX();
		finishY			= this.coinWay.get( 0 ).getStartY();
		
		this.coinWay.add( 0, (new ApoCheatingTeacherWay( (int)realX, (int)realY, (int)finishX, (int)finishY, 1.6, -1 )) );
		this.coinWay.add( this.coinWay.size(), (new ApoCheatingTeacherWay( (int)finishX, (int)finishY, (int)realX, (int)realY, 1.6, -1 )) );
		
		this.bCoin		= true;
		
		/*System.out.println("x = "+this.getX()+" y = "+this.getY());
		
		for ( int i = 0; i < this.coinWay.size(); i++ )
		{
			System.out.print( "startX = "+this.coinWay.get( i ).getStartX() );
			System.out.print( " startY = "+this.coinWay.get( i ).getStartY() );
			System.out.print( " finishX = "+this.coinWay.get( i ).getFinishX() );
			System.out.println( " finishY = "+this.coinWay.get( i ).getFinishY() );
		}*/
	}
	
	protected void render( Graphics2D g )
	{
		Composite clone = g.getComposite();
		
		g.setColor( this.cheatColor );
		g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.4f ) );
		if ( ( !this.bCoin ) && ( this.view.size() > 0 ) )
			g.fillPolygon( this.view.get( this.currentView ).getPolygon() );
		else if ( this.bCoin )
			g.fillPolygon( this.coinView.get( 0 ).getPolygon() );
		
		g.setComposite( clone );
		
		super.render( g );
	}

}
