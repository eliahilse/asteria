package apoDefence.game;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefencePoint;

public class ApoDefenceCastle extends ApoDefenceTower
{

	public ApoDefenceCastle( BufferedImage iTower, int x, int y )
	{
		super( -1, iTower, x, y, "Castle" );
		
		this.setRec( new Rectangle2D.Double( x, y, iTower.getWidth(), iTower.getHeight() ) );

		this.setValues( 0, 0, 100, 1, 1, 0, new ApoDefencePoint( 0, 0 ) );
		this.setHealth( this.getMaxHealth() );
		this.setMaxLevel( ApoDefenceConstants.TOWER_CASTLE_MAX_LEVEL );
		this.setUpgradePrice( ApoDefenceConstants.TOWER_CASTLE_PRICE_UPGRADE );
	}
	
	public float getUpgradePrice()
	{
		return ApoDefenceConstants.TOWER_CASTLE_PRICE_UPGRADE;
	}
	
	public void upgrade()
	{
		if ( this.getLevel() < this.getMaxLevel() )
		{
			this.setMaxHealth( this.getHealth() + ApoDefenceConstants.TOWER_CASTLE_HEALTH_UPGRADE );
			this.setLevel( this.getLevel() + 1 );
			this.setBBuildUp( true );
		}
	}

}
