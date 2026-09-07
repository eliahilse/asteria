package apoDefence.options;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import apoDefence.ApoDefenceButton;
import apoDefence.ApoDefenceConstants;
import apoDefence.ApoDefenceConstantsOriginal;
import apoDefence.ApoDefenceImage;

public class ApoDefenceOptionsGeneral
{
	private BufferedImage			iBackground;
	private ApoDefenceButton[]		buttons;
	private ApoDefenceOptions		options;
	
	public ApoDefenceOptionsGeneral( ApoDefenceOptions options )
	{
		super();
	
		this.options				= options;
		
		ApoDefenceImage		image	= new ApoDefenceImage();
		this.iBackground			= image.getPic( "/images/options_general.png", false );
		
		BufferedImage	button_up	= image.getPic( "/images/button/button_options_up.png", false );
		BufferedImage	button_down	= image.getPic( "/images/button/button_options_down.png", false );
		
		this.buttons				= new ApoDefenceButton[13];
		this.buttons[0]				= new ApoDefenceButton( image.getPic( "/images/button/button_options_original.png", false ),   156 + 15,  172 + 244, 100, 45, "Original" );
		this.buttons[1]				= new ApoDefenceButton( button_up,   195 + 15,  50 + 244, 20, 20, "WavesUp" );
		this.buttons[2]				= new ApoDefenceButton( button_down, 125 + 15,  50 + 244, 20, 20, "WavesDown" );
		this.buttons[3]				= new ApoDefenceButton( button_up,   195 + 15,  94 + 244, 20, 20, "MoneyUp" );
		this.buttons[4]				= new ApoDefenceButton( button_down, 125 + 15,  94 + 244, 20, 20, "MoneyDown" );
		this.buttons[5]				= new ApoDefenceButton( button_up,   195 + 15, 116 + 244, 20, 20, "TowerUp" );
		this.buttons[6]				= new ApoDefenceButton( button_down, 125 + 15, 116 + 244, 20, 20, "TowerDown" );
		this.buttons[7]				= new ApoDefenceButton( button_up,   195 + 15,  28 + 244, 20, 20, "DifficultyUp" );
		this.buttons[8]				= new ApoDefenceButton( button_down, 125 + 15,  28 + 244, 20, 20, "DifficultyDown" );
		this.buttons[9]				= new ApoDefenceButton( button_up,   195 + 15, 138 + 244, 20, 20, "EnemiesUp" );
		this.buttons[10]			= new ApoDefenceButton( button_down, 125 + 15, 138 + 244, 20, 20, "EnemiesDown" );
		this.buttons[11]			= new ApoDefenceButton( button_up,   195 + 15,  72 + 244, 20, 20, "WavesTimeUp" );
		this.buttons[12]			= new ApoDefenceButton( button_down, 125 + 15,  72 + 244, 20, 20, "WavesTimeDown" );
	}
	
	public void mouseMoved( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getMove( x, y ) )
			{
				this.options.repaint();
				return;
			}
		}
	}
	
	public void mouseReleased( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getReleased( x, y ) )
			{
				String function	= this.buttons[i].getFunction();
				if ( "Original".equals( function ) )
				{
					this.setOriginal();
				} else if ( "WavesUp".equals( function ) )
				{
					ApoDefenceConstants.MAX_WAVE++;
					if ( ApoDefenceConstants.MAX_WAVE > 300 )
						ApoDefenceConstants.MAX_WAVE	= 300;
				} else if ( "WavesDown".equals( function ) )
				{
					ApoDefenceConstants.MAX_WAVE--;
					if ( ApoDefenceConstants.MAX_WAVE <= 0  )
						ApoDefenceConstants.MAX_WAVE	= 1;
				} else if ( "WavesTimeUp".equals( function ) )
				{
					ApoDefenceConstants.WAVE_TIME++;
					if ( ApoDefenceConstants.WAVE_TIME > 30 )
						ApoDefenceConstants.WAVE_TIME	= 30;
				} else if ( "WavesTimeDown".equals( function ) )
				{
					ApoDefenceConstants.WAVE_TIME--;
					if ( ApoDefenceConstants.WAVE_TIME < 3  )
						ApoDefenceConstants.WAVE_TIME	= 3;
				} else if ( "MoneyUp".equals( function ) )
				{
					ApoDefenceConstants.MONEY	+= 100;
					if ( ApoDefenceConstants.MONEY > 10000 )
						ApoDefenceConstants.MONEY	= 10000;
				} else if ( "MoneyDown".equals( function ) )
				{
					ApoDefenceConstants.MONEY	-= 100;
					if ( ApoDefenceConstants.MONEY < 100  )
						ApoDefenceConstants.MONEY	= 100;
				} else if ( "TowerUp".equals( function ) )
				{
					ApoDefenceConstants.MAX_TOWERS++;
					if ( ApoDefenceConstants.MAX_TOWERS > 100 )
						ApoDefenceConstants.MAX_TOWERS	= 100;
				} else if ( "TowerDown".equals( function ) )
				{
					ApoDefenceConstants.MAX_TOWERS--;
					if ( ApoDefenceConstants.MAX_TOWERS < 5  )
						ApoDefenceConstants.MAX_TOWERS	= 5;
				} else if ( "DifficultyDown".equals( function ) )
				{
					ApoDefenceConstants.DIFFICULTY++;
					if ( ApoDefenceConstants.DIFFICULTY > ApoDefenceConstants.DIFFICULTY_EASY )
						ApoDefenceConstants.DIFFICULTY	= ApoDefenceConstants.DIFFICULTY_EASY;
				} else if ( "DifficultyUp".equals( function ) )
				{
					ApoDefenceConstants.DIFFICULTY--;
					if ( ApoDefenceConstants.DIFFICULTY < ApoDefenceConstants.DIFFICULTY_HARD  )
						ApoDefenceConstants.DIFFICULTY	= ApoDefenceConstants.DIFFICULTY_HARD;
				} else if ( "EnemiesUp".equals( function ) )
				{
					ApoDefenceConstants.MAX_ENEMIES++;
					if ( ApoDefenceConstants.MAX_ENEMIES > 30 )
						ApoDefenceConstants.MAX_ENEMIES	= 30;
				} else if ( "EnemiesDown".equals( function ) )
				{
					ApoDefenceConstants.MAX_ENEMIES--;
					if ( ApoDefenceConstants.MAX_ENEMIES < 5  )
						ApoDefenceConstants.MAX_ENEMIES	= 5;
				}
				this.options.repaint();
			}
		}
	}
	
	public void mousePressed( int x, int y )
	{
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			if ( this.buttons[i].getPressed( x, y ) )
			{
				this.options.repaint();
				return;
			}
		}
	}
	
	private void setOriginal()
	{
		ApoDefenceConstants.MAX_WAVE	= this.options.getGame().getLoadSave().getWaves();
		ApoDefenceConstants.WAVE_TIME	= ApoDefenceConstantsOriginal.WAVE_TIME;
		ApoDefenceConstants.MAX_ENEMIES	= ApoDefenceConstantsOriginal.MAX_ENEMIES;
		ApoDefenceConstants.DIFFICULTY	= ApoDefenceConstantsOriginal.DIFFICULTY;
		ApoDefenceConstants.MONEY		= this.options.getGame().getLoadSave().getMoney();
		ApoDefenceConstants.MAX_TOWERS	= this.options.getGame().getLoadSave().getTowerCount();
	}
	
	public boolean isBOriginal()
	{
		if ( 	( ApoDefenceConstants.MAX_WAVE == this.options.getGame().getLoadSave().getWaves() ) &&
				( ApoDefenceConstants.WAVE_TIME == ApoDefenceConstantsOriginal.WAVE_TIME ) &&
				( ApoDefenceConstants.MAX_ENEMIES == ApoDefenceConstantsOriginal.MAX_ENEMIES ) &&
				( ApoDefenceConstants.DIFFICULTY == ApoDefenceConstantsOriginal.DIFFICULTY ) &&
				( ApoDefenceConstants.MONEY == this.options.getGame().getLoadSave().getMoney() ) &&
				( ApoDefenceConstants.MAX_TOWERS == this.options.getGame().getLoadSave().getTowerCount() ) )
			return true;
		else
			return false;
	}
	
	public void render( Graphics2D g )
	{
		g.drawImage( this.iBackground, 15, 244, null );
		
		for ( int i = 0; i < this.buttons.length; i++ )
		{
			this.buttons[i].render( g );
		}
		
		g.setColor( Color.WHITE );
		g.drawString( ""+ApoDefenceConstants.MAX_WAVE, 160 + 15, 65 + 244 );
		g.drawString( ""+ApoDefenceConstants.WAVE_TIME, 160 + 15, 87 + 244 );
		g.drawString( ""+ApoDefenceConstants.MONEY, 160 + 15, 109 + 244 );
		g.drawString( ""+ApoDefenceConstants.MAX_TOWERS, 160 + 15, 131 + 244 );
		g.drawString( ""+ApoDefenceConstants.MAX_ENEMIES, 160 + 15, 153 + 244 );
		
		String s		= "easy";
		if ( ApoDefenceConstants.DIFFICULTY == ApoDefenceConstants.DIFFICULTY_MEDIUM )
			s			= "middle";
		else if ( ApoDefenceConstants.DIFFICULTY == ApoDefenceConstants.DIFFICULTY_HARD )
			s			= "hard";
		g.drawString( s, 155 + 15, 42 + 244 );
	}

}
