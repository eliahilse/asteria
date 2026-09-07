package apoCheating;

public class ApoCheatingTeacherWay
{
	private int		currentTicks, maxTicks;
	private double	speed;
	private int		finishX, finishY;
	private int		startX, startY;
	private double	currentX, currentY;
	
	public ApoCheatingTeacherWay( int startX, int startY, int finishX, int finishY, double speed, int maxTicks )
	{
		this.finishX		= finishX;
		this.finishY		= finishY;
		this.startX			= startX;
		this.startY			= startY;
		this.speed			= Math.abs( speed );
		this.currentX		= startX;
		this.currentY		= startY;
		this.maxTicks		= maxTicks;
		int x				= (int)Math.abs( ( this.finishX - this.startX ) / this.speed );
		int y				= (int)Math.abs( ( this.finishY - this.startY ) / this.speed );
		if ( this.maxTicks < 0 )
		{
			if ( x > y )
				this.maxTicks	= x-1;
			else
				this.maxTicks	= y-1;
			this.maxTicks	= 0;
		}
		this.currentTicks	= 0;
		//System.out.print( "startX = "+this.currentX+" startY = "+this.currentY+" ");
		//System.out.println( "finishX = "+this.finishX+" finishY = "+this.finishY);
	}
	
	protected void init()
	{
		currentTicks	= 0;
		currentX		= this.startX;
		currentY		= this.startY;
	}
	
	protected boolean next( ApoCheatingTeacher teacher )
	{
		if ( 	( ( this.currentX == this.finishX ) &&
				  ( this.currentY == this.finishY ) ) &&
				( this.currentTicks == this.maxTicks ) )
		{
			//System.out.println("Check mit x = "+this.finishX+" y = "+this.finishY);
			this.currentX		= this.startX;
			this.currentY		= this.startY;
			this.currentTicks	= 0;
			return false;
		}
		else
		{
			if ( ( this.currentX != this.finishX ) ||
				 ( this.currentY != this.finishY ) )
			{
				if ( teacher.getY() != this.currentY )
					teacher.setY( this.currentY );
				if ( teacher.getX() != this.currentX )
					teacher.setX( this.currentX );
				double speed		= 0;

				{
					if ( this.startY < this.finishY )
						speed		= this.speed;
					else if ( this.startY > this.finishY )
						speed		= -this.speed;
					this.currentY	+= speed;
					if ( ( speed < 0 ) && ( this.currentY < this.finishY ) )
						this.currentY	= this.finishY;
					else if ( ( speed > 0 ) && ( this.currentY > this.finishY ) )
						this.currentY	= this.finishY;
					teacher.setY( this.currentY );
				}
				{
					if ( this.startX < this.finishX )
						speed		= this.speed;
					else if ( this.startX > this.finishX )
						speed		= -this.speed;
					this.currentX	+= speed;
					if ( ( speed < 0 ) && ( this.currentX < this.finishX ) )
						this.currentX	= this.finishX;
					else if ( ( speed > 0 ) && ( this.currentX > this.finishX ) )
						this.currentX	= this.finishX;
					teacher.setX( this.currentX );
				}
			}
			if ( this.currentTicks != this.maxTicks )
				this.currentTicks	+= 1;
		}
		return true;
	}

	protected int getFinishX()
	{
		return finishX;
	}

	protected int getFinishY()
	{
		return finishY;
	}

	protected int getMaxTicks()
	{
		return maxTicks;
	}

	protected double getSpeed()
	{
		return speed;
	}

	protected int getStartX()
	{
		return startX;
	}

	protected int getStartY()
	{
		return startY;
	}

}
