package apoDefence.game;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefencePoint;

public class ApoDefenceResearch extends ApoDefenceTower
{
	private int		techLevel, armorLevel, speedLevel, attackLevel,
					maxTechLevel, maxArmorLevel, maxSpeedLevel, maxAttackLevel,
					priceTechLevel, priceArmorLevel, priceSpeedLevel, priceAttackLevel,
					waitTime, maxWaitTime;
	
	
	public ApoDefenceResearch( BufferedImage iTower, int x, int y )
	{
		super( -1, iTower, x, y, "Research Building" );
		
		this.setRec( new Rectangle2D.Double( x, y, iTower.getWidth(), iTower.getHeight() ) );
		
		this.initResearch();
		this.setValues( 0, 0, 100, 1, 1, 0, new ApoDefencePoint( 0, 0 ) );
		this.setHealth( this.getMaxHealth() );
	}
	
	public void initResearch()
	{
		this.techLevel		= ApoDefenceConstants.TOWER_RESEARCH_TECHLEVEL;
		this.armorLevel		= ApoDefenceConstants.TOWER_RESEARCH_ARMORLEVEL;
		this.speedLevel		= ApoDefenceConstants.TOWER_RESEARCH_SPEEDLEVEL;
		this.attackLevel	= ApoDefenceConstants.TOWER_RESEARCH_ATTACKLEVEL;
		
		this.maxTechLevel	= ApoDefenceConstants.TOWER_RESEARCH_MAX_TECHLEVEL;
		this.maxArmorLevel	= ApoDefenceConstants.TOWER_RESEARCH_MAX_ARMORLEVEL;
		this.maxSpeedLevel	= ApoDefenceConstants.TOWER_RESEARCH_MAX_SPEEDLEVEL;
		this.maxAttackLevel	= ApoDefenceConstants.TOWER_RESEARCH_MAX_ATTACKLEVEL;
		
		this.priceTechLevel		= ApoDefenceConstants.TOWER_RESEARCH_PRICE_TECHLEVEL;
		this.priceArmorLevel	= ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL;
		this.priceSpeedLevel	= ApoDefenceConstants.TOWER_RESEARCH_PRICE_SPEEDLEVEL;
		this.priceAttackLevel	= ApoDefenceConstants.TOWER_RESEARCH_PRICE_ATTACKLEVEL;
		
		this.waitTime			= 0;
		this.maxWaitTime		= 0;
	}
	
	public void think( ApoDefenceGame game )
	{
		if ( this.waitTime > 0 )
			this.waitTime	-= 2;
	}
	
	public boolean canUpgrade()
	{
		if ( this.waitTime <= 0 )
			return true;
		else
			return false;
	}
	
	public int getArmorLevel()
	{
		return this.armorLevel;
	}

	public void setArmorLevel(int amorLevel)
	{
		this.armorLevel = amorLevel;
	}
	
	public void upgradeArmorLevel()
	{
		this.armorLevel			= this.armorLevel + 1;
		this.priceArmorLevel	= this.priceArmorLevel + ApoDefenceConstants.TOWER_RESEARCH_PRICE_ARMORLEVEL_UPGRADE;
		this.waitTime			= ApoDefenceConstants.TOWER_RESEARCH_WAIT + this.armorLevel * ApoDefenceConstants.TOWER_RESEARCH_WAIT_ARMORLEVEL_UPGRADE;
		this.maxWaitTime		= this.waitTime;
	}

	public int getAttackLevel()
	{
		return this.attackLevel;
	}

	public void setAttackLevel(int attackLevel)
	{
		this.attackLevel = attackLevel;
		this.waitTime	= ApoDefenceConstants.TOWER_RESEARCH_WAIT + this.attackLevel * ApoDefenceConstants.TOWER_RESEARCH_WAIT_ATTACKLEVEL_UPGRADE;
		this.maxWaitTime		= this.waitTime;
	}

	public float getSpeedLevel()
	{
		return (float)this.speedLevel;
	}

	public void setSpeedLevel(int speedLevel)
	{
		this.speedLevel = speedLevel;
		this.waitTime	= ApoDefenceConstants.TOWER_RESEARCH_WAIT + this.speedLevel * ApoDefenceConstants.TOWER_RESEARCH_WAIT_SPEEDLEVEL_UPGRADE;
		this.maxWaitTime		= this.waitTime;
	}

	public int getTechLevel()
	{
		return this.techLevel;
	}

	public void setTechLevel(int techLevel)
	{
		this.techLevel = techLevel;
		this.waitTime	= ApoDefenceConstants.TOWER_RESEARCH_WAIT + this.techLevel * ApoDefenceConstants.TOWER_RESEARCH_WAIT_TECHLEVEL_UPGRADE;
		this.maxWaitTime		= this.waitTime;
	}

	public int getMaxArmorLevel()
	{
		return this.maxArmorLevel;
	}

	public void setMaxArmorLevel(int maxAmorLevel)
	{
		this.maxArmorLevel = maxAmorLevel;
	}

	public int getMaxAttackLevel()
	{
		return this.maxAttackLevel;
	}

	public void setMaxAttackLevel(int maxAttackLevel)
	{
		this.maxAttackLevel = maxAttackLevel;
	}

	public int getMaxSpeedLevel()
	{
		return this.maxSpeedLevel;
	}

	public void setMaxSpeedLevel(int maxSpeedLevel)
	{
		this.maxSpeedLevel = maxSpeedLevel;
	}

	public int getMaxTechLevel()
	{
		return this.maxTechLevel;
	}

	public void setMaxTechLevel(int maxTechLevel)
	{
		this.maxTechLevel = maxTechLevel;
	}
	
	public boolean canTechLevelUp()
	{
		if ( ( this.getTechLevel() + 1 <= this.getMaxTechLevel() ) && ( this.canUpgrade() ) )
			return true;
		else
			return false;
	}
	
	public boolean canSpeedLevelUp()
	{
		if ( ( this.getSpeedLevel() + 1 <= this.getMaxSpeedLevel() ) && ( this.canUpgrade() ) )
			return true;
		else
			return false;
	}
	
	public boolean canArmorLevelUp()
	{
		if ( ( this.getArmorLevel() + 1 <= this.getMaxArmorLevel() ) && ( this.canUpgrade() ) )
			return true;
		else
			return false;
	}
	
	public boolean canArmorPiercingLevelUp()
	{
		if ( ( this.getAttackLevel() + 1 <= this.getMaxAttackLevel() ) && ( this.canUpgrade() ) )
			return true;
		else
			return false;
	}

	public int getPriceArmorLevel()
	{
		return this.priceArmorLevel;
	}

	public void setPriceArmorLevel(int priceArmorLevel)
	{
		this.priceArmorLevel = priceArmorLevel;
	}

	public int getPriceAttackLevel()
	{
		return this.priceAttackLevel;
	}

	public void setPriceAttackLevel(int priceAttackLevel)
	{
		this.priceAttackLevel = priceAttackLevel;
	}

	public int getPriceSpeedLevel()
	{
		return this.priceSpeedLevel;
	}

	public void setPriceSpeedLevel(int priceSpeedLevel)
	{
		this.priceSpeedLevel = priceSpeedLevel;
	}

	public int getPriceTechLevel()
	{
		return this.priceTechLevel;
	}

	public void setPriceTechLevel(int priceTechLevel)
	{
		this.priceTechLevel = priceTechLevel;
	}

	public int getMaxWaitTime()
	{
		return this.maxWaitTime;
	}

	public int getWaitTime()
	{
		return this.waitTime;
	}
	
	

}
