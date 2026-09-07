package apoCheating;

import java.awt.Polygon;

public class ApoCheatingTeacherView
{
	private int					currentTicks, maxTicks;
	private double				startDir, currentDir, endDir;
	private int 				width, height;
	private double				speed;
	private int[] 				startX, startY, currentX, currentY;
	
	public ApoCheatingTeacherView( int width, int height, double startDir, double endDir, double speed, int maxTicks )
	{
		this.width			= width;
		this.height			= height;
		this.startDir		= startDir;
		this.currentDir		= this.startDir;
		this.endDir			= endDir;
		this.speed			= speed;
		this.maxTicks		= maxTicks;
		this.currentTicks	= 0;
	}
	
	public ApoCheatingTeacherView( int width, int height, double startDir, double endDir, double speed, int maxTicks, ApoCheatingTeacher teacher )
	{
		this.width			= width;
		this.height			= height;
		this.startDir		= startDir;
		this.currentDir		= this.startDir;
		this.endDir			= endDir;
		this.speed			= speed;
		this.maxTicks		= maxTicks;
		this.currentTicks	= 0;
		this.init( teacher );
	}

	protected void setTeacher(ApoCheatingTeacher teacher)
	{
		this.init( teacher );
	}
	
	protected void init( ApoCheatingTeacher teacher )
	{
		this.currentDir		= this.startDir;
		this.currentTicks	= 0;
		//System.out.println("max = "+this.maxTicks);
		this.makeView( teacher, true );
	}
	
	protected Polygon getPolygon()
	{
		return new Polygon( this.currentX, this.currentY, this.currentX.length );
	}
	
	public boolean next( ApoCheatingTeacher teacher )
	{
		if ( 	( this.maxTicks == this.currentTicks ) )
		{
			System.arraycopy( this.currentX, 0, this.startX, 0, this.startX.length );
			System.arraycopy( this.currentY, 0, this.startY, 0, this.startY.length );
			this.currentDir		= startDir;
			this.currentTicks	= 0;
			return false;
		} else
		{
			if ( 	( (int)teacher.getX() != this.currentX[0] ) ||
					( (int)teacher.getY() != this.currentY[0] ) )
			{
				this.currentX[0] = this.currentX[3] = (int)teacher.getX() + teacher.getHeight() / 2;
				this.currentY[0] = this.currentY[3] = (int)teacher.getY() + teacher.getHeight() / 2;
			}
			this.currentDir		+= this.speed;
			if ( this.currentDir < 0 )
				this.currentDir	= 360.0D;
			else if ( this.currentDir > 360.0D )
				this.currentDir	-= 360.0D;
			if ( this.startDir < this.endDir )
			{
				
				if ( this.currentDir < this.startDir )
					this.speed	= -this.speed;
				else if ( this.currentDir > this.endDir )
					this.speed	= -this.speed;
			} else
			{
				double oldDir	= this.currentDir - this.speed;
				if ( ( this.currentDir > this.endDir ) && ( oldDir <= this.endDir ) )
					this.speed	= -this.speed;
				else if ( ( this.currentDir < this.startDir ) && ( oldDir >= this.startDir ) )
					this.speed	= -this.speed;
			}
			this.makeView( teacher );
			this.currentTicks	+= 1;
		}
		return true;
	}
	
	protected void makeView( ApoCheatingTeacher teacher )
	{
		this.makeView( teacher, false );
	}
	
	private void makeView( ApoCheatingTeacher teacher, boolean bNew )
	{
		boolean bFirst	= false;
		if ( (bNew) || ( this.startX == null ) || ( this.startY == null ) || ( this.currentX == null ) )
		{
			bFirst			= true;
			this.startX		= this.currentX		= new int[4];
			this.startY		= this.currentY		= new int[4];
			this.startX[0]	= this.startX[3]	= this.currentX[0]	= this.currentX[3]	= (int)teacher.getX() + teacher.getWidth() / 2;
			this.startY[0]	= this.startY[3]	= this.currentY[0]	= this.currentY[3]	= (int)teacher.getY() + teacher.getHeight() / 2;
		}
		int x				= 0;
		int y				= 0;
		int dir				= (int)this.currentDir;
		dir 				+= this.width/2;
		if ( dir > 360 )
			dir 			-= 360;
		x				= (int)(this.currentX[0] + this.height * Math.cos( Math.toRadians( dir )) );
		y				= (int)(this.currentY[0] + this.height * Math.sin( Math.toRadians( dir )) );
		this.currentX[1]	= (int)(x);
		this.currentY[1]	= (int)(y);
		
		dir				= (int)this.currentDir;
		dir 			-= this.width/2;
		if ( dir < 0 )
			dir 		= 360 + dir;
		x				= (int)(this.currentX[0] + this.height * Math.cos( Math.toRadians( dir )) );
		y				= (int)(this.currentY[0] + this.height * Math.sin( Math.toRadians( dir )) );
		this.currentX[2]	= (int)(x);
		this.currentY[2]	= (int)(y);
		if ( bFirst )
		{
			this.startX[1]	= this.currentX[1];
			this.startY[1]	= this.currentY[1];
			this.startX[2]	= this.currentX[2];
			this.startY[2]	= this.currentY[2];
		}
	}

	protected double getEndDir()
	{
		return this.endDir;
	}

	protected int getHeight()
	{
		return this.height;
	}

	protected int getMaxTicks()
	{
		return this.maxTicks;
	}

	protected double getSpeed()
	{
		return this.speed;
	}

	protected double getStartDir()
	{
		return this.startDir;
	}

	protected int getWidth()
	{
		return this.width;
	}

}
