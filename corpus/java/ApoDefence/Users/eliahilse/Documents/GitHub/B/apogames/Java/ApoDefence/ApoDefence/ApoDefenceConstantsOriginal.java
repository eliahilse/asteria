package apoDefence;

/**
 * enthält alle Konstanten für die Gegner und Tower
 * @author Dirk Aporius
 *
 */
public class ApoDefenceConstantsOriginal
{

	public static final int			TOWER_ARCH_RANGE					= 195;
	public static final int			TOWER_ARCH_SPEED					= 70;
	public static final int			TOWER_ARCH_HEALTH					= 100;
	public static final int			TOWER_ARCH_AMOR						= 1;
	public static final ApoDefencePoint		TOWER_ARCH_POINTATTACK		= new ApoDefencePoint( 5.4f, 6.5f );
	
	public static final float		TOWER_ARCH_RANGE_UPGRADE			= 0.05f;
	public static final float		TOWER_ARCH_SPEED_UPGRADE			= 0.037f;
	public static final float		TOWER_ARCH_HEALTH_UPGRADE			= 0.2f;
	public static final float		TOWER_ARCH_AMOR_UPGRADE				= 1f;
	public static final float		TOWER_ARCH_POINTATTACK_UPGRADE		= 0.31f;
	
	public static final int			TOWER_FIRE_RANGE					= 190;
	public static final int			TOWER_FIRE_SPEED					= 62;
	public static final int			TOWER_FIRE_HEALTH					= 150;
	public static final int			TOWER_FIRE_AMOR						= 3;
	public static final ApoDefencePoint		TOWER_FIRE_POINTATTACK		= new ApoDefencePoint( 9.5f, 11.5f );
	
	public static final float		TOWER_FIRE_RANGE_UPGRADE			= 0.035f;
	public static final float		TOWER_FIRE_SPEED_UPGRADE			= 0.042f;
	public static final float		TOWER_FIRE_HEALTH_UPGRADE			= 0.2f;
	public static final float		TOWER_FIRE_AMOR_UPGRADE				= 2f;
	public static final float		TOWER_FIRE_POINTATTACK_UPGRADE		= 0.28f;
	
	public static final int			TOWER_ICE_RANGE						= 260;
	public static final int			TOWER_ICE_SPEED						= 63;
	public static final int			TOWER_ICE_HEALTH					= 150;
	public static final int			TOWER_ICE_AMOR						= 2;
	public static final ApoDefencePoint		TOWER_ICE_POINTATTACK		= new ApoDefencePoint( 8.5f, 12.5f );
	
	public static final float		TOWER_ICE_RANGE_UPGRADE				= 0.056f;
	public static final float		TOWER_ICE_SPEED_UPGRADE				= 0.04f;
	public static final float		TOWER_ICE_HEALTH_UPGRADE			= 0.2f;
	public static final float		TOWER_ICE_AMOR_UPGRADE				= 1.8f;
	public static final float		TOWER_ICE_POINTATTACK_UPGRADE		= 0.29f;
	
	public static final int			TOWER_LIGHT_RANGE					= 205;
	public static final int			TOWER_LIGHT_SPEED					= 62;
	public static final int			TOWER_LIGHT_HEALTH					= 300;
	public static final int			TOWER_LIGHT_AMOR					= 4;
	public static final ApoDefencePoint		TOWER_LIGHT_POINTATTACK		= new ApoDefencePoint( 17.5f, 19.8f );
		
	public static final float		TOWER_LIGHT_RANGE_UPGRADE			= 0.045f;
	public static final float		TOWER_LIGHT_SPEED_UPGRADE			= 0.04f;
	public static final float		TOWER_LIGHT_HEALTH_UPGRADE			= 0.25f;
	public static final float		TOWER_LIGHT_AMOR_UPGRADE			= 1.5f;
	public static final float		TOWER_LIGHT_POINTATTACK_UPGRADE		= 0.28f;
	
	public static final int			TOWER_ULTRA_RANGE					= 210;
	public static final int			TOWER_ULTRA_SPEED					= 50;
	public static final int			TOWER_ULTRA_HEALTH					= 500;
	public static final int			TOWER_ULTRA_AMOR					= 5;
	public static final ApoDefencePoint		TOWER_ULTRA_POINTATTACK		= new ApoDefencePoint( 40.4f, 45.3f );
	
	public static final float		TOWER_ULTRA_RANGE_UPGRADE			= 0.041f;
	public static final float		TOWER_ULTRA_SPEED_UPGRADE			= 0.051f;
	public static final float		TOWER_ULTRA_HEALTH_UPGRADE			= 0.25f;
	public static final float		TOWER_ULTRA_AMOR_UPGRADE			= 2.5f;
	public static final float		TOWER_ULTRA_POINTATTACK_UPGRADE		= 0.32f;
		
	public static final int			TOWER_ARCH_PRICE					= 10;
	public static final int			TOWER_FIRE_PRICE					= 90;
	public static final int			TOWER_ICE_PRICE						= 150;
	public static final int			TOWER_LIGHT_PRICE					= 300;
	public static final int			TOWER_ULTRA_PRICE					= 1000;
	
	public static final int			TOWER_ARCH_PRICE_UPGRADE 			= 70;
	public static final int			TOWER_FIRE_PRICE_UPGRADE 			= 130;
	public static final int			TOWER_ICE_PRICE_UPGRADE 			= 190;
	public static final int			TOWER_LIGHT_PRICE_UPGRADE 			= 350;
	public static final int			TOWER_ULTRA_PRICE_UPGRADE 			= 1000;
	
	public static final float		TOWER_ARCH_PRICE_UPGRADE_PLUS 		= 0.69f;
	public static final float		TOWER_FIRE_PRICE_UPGRADE_PLUS 		= 0.72f;
	public static final float		TOWER_ICE_PRICE_UPGRADE_PLUS 		= 0.65f;
	public static final float		TOWER_LIGHT_PRICE_UPGRADE_PLUS 		= 0.7f;
	public static final float		TOWER_ULTRA_PRICE_UPGRADE_PLUS 		= 0.85f;
	
	public static final int			TOWER_ARCH_LEVEL					= 1;
	public static final int			TOWER_FIRE_LEVEL					= 3;
	public static final int			TOWER_ICE_LEVEL						= 5;
	public static final int			TOWER_LIGHT_LEVEL					= 7;
	public static final int			TOWER_ULTRA_LEVEL					= 10;
			
	public static final float		TOWER_CASTLE_PRICE_UPGRADE			= 150;
	public static final int			TOWER_CASTLE_HEALTH_UPGRADE			= 50;
	public static final int			TOWER_CASTLE_MAX_LEVEL				= 20;
	
	public static final int			TOWER_ARCH_MAXLEVEL					= 10;
	public static final int			TOWER_FIRE_MAXLEVEL					= 8;
	public static final int			TOWER_ICE_MAXLEVEL					= 10;
	public static final int			TOWER_LIGHT_MAXLEVEL				= 10;
	public static final int			TOWER_ULTRA_MAXLEVEL				= 5;
	
	public static final int			TOWER_RESEARCH_TECHLEVEL			= 1;
	public static final int			TOWER_RESEARCH_ARMORLEVEL			= 0;
	public static final int			TOWER_RESEARCH_SPEEDLEVEL			= 1;
	public static final int			TOWER_RESEARCH_ATTACKLEVEL			= 0;
	
	public static final int			TOWER_RESEARCH_MAX_TECHLEVEL		= 10;
	public static final int			TOWER_RESEARCH_MAX_ARMORLEVEL		= 150;
	public static final int			TOWER_RESEARCH_MAX_SPEEDLEVEL		= 15;
	public static final int			TOWER_RESEARCH_MAX_ATTACKLEVEL		= 7;
	
	public static final int			TOWER_RESEARCH_PRICE_TECHLEVEL		= 100;
	public static final int			TOWER_RESEARCH_PRICE_ARMORLEVEL		= 150;
	public static final int			TOWER_RESEARCH_PRICE_SPEEDLEVEL		= 300;
	public static final int			TOWER_RESEARCH_PRICE_ATTACKLEVEL	= 350;
	
	public static final int			MAX_ENEMIES							= 10;
	public static final float		DIFFICULTY							= 2.0f;
	public static final int			WAVE_TIME							= 15;
	public static final int			MAX_WAVE							= 40;
	
	
	
	public static final int			ENEMY_BIRD						= 1;
	public static final String		ENEMY_BIRD_NAME					= "bird";
	public static final float		ENEMY_BIRD_SPEED				= 80f;
	public static final float		ENEMY_BIRD_HEALTH				= 15.0f;
	public static final float		ENEMY_BIRD_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_BIRD_DAMAGE		= new ApoDefencePoint( 4.5f, 5.5f );
	public static final float		ENEMY_BIRD_SPEED_UPGRADE		= 0.011f;
	public static final float		ENEMY_BIRD_HEALTH_UPGRADE		= 10f;
	public static final float		ENEMY_BIRD_ARMOR_UPGRADE		= 0.3f;
	public static final float		ENEMY_BIRD_DAMAGE_UPGRADE		= 0.055f;
	public static final float		ENEMY_BIRD_MONEY				= 5.4f;
	
	public static final int			ENEMY_GHOST						= 2;
	public static final String		ENEMY_GHOST_NAME				= "ghost";
	public static final float		ENEMY_GHOST_SPEED				= 55f;
	public static final float		ENEMY_GHOST_HEALTH				= 30.0f;
	public static final float		ENEMY_GHOST_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_GHOST_DAMAGE		= new ApoDefencePoint( 4.5f, 6.0f );
	public static final float		ENEMY_GHOST_SPEED_UPGRADE		= 0.01f;
	public static final float		ENEMY_GHOST_HEALTH_UPGRADE		= 14.5f;
	public static final float		ENEMY_GHOST_ARMOR_UPGRADE		= 0.2f;
	public static final float		ENEMY_GHOST_DAMAGE_UPGRADE		= 0.037f;
	public static final float		ENEMY_GHOST_MONEY				= 5.6f;
	
	public static final int			ENEMY_DOG						= 3;
	public static final String		ENEMY_DOG_NAME					= "dog";
	public static final float		ENEMY_DOG_SPEED					= 100f;
	public static final float		ENEMY_DOG_HEALTH				= 10.0f;
	public static final float		ENEMY_DOG_ARMOR					= 1f;
	public static final ApoDefencePoint		ENEMY_DOG_DAMAGE		= new ApoDefencePoint( 4.8f, 5.3f );
	public static final float		ENEMY_DOG_SPEED_UPGRADE			= 0.012f;
	public static final float		ENEMY_DOG_HEALTH_UPGRADE		= 15f;
	public static final float		ENEMY_DOG_ARMOR_UPGRADE			= 0.3f;
	public static final float		ENEMY_DOG_DAMAGE_UPGRADE		= 0.047f;
	public static final float		ENEMY_DOG_MONEY					= 6.5f;
	
	public static final int			ENEMY_LION						= 4;
	public static final String		ENEMY_LION_NAME					= "lion";
	public static final float		ENEMY_LION_SPEED				= 85f;
	public static final float		ENEMY_LION_HEALTH				= 14.0f;
	public static final float		ENEMY_LION_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_LION_DAMAGE		= new ApoDefencePoint( 5.0f, 5.7f );
	public static final float		ENEMY_LION_SPEED_UPGRADE		= 0.012f;
	public static final float		ENEMY_LION_HEALTH_UPGRADE		= 11.5f;
	public static final float		ENEMY_LION_ARMOR_UPGRADE		= 0.26f;
	public static final float		ENEMY_LION_DAMAGE_UPGRADE		= 0.061f;
	public static final float		ENEMY_LION_MONEY				= 6.2f;
	
	public static final int			ENEMY_HORSE						= 5;
	public static final String		ENEMY_HORSE_NAME				= "horse";
	public static final float		ENEMY_HORSE_SPEED				= 90f;
	public static final float		ENEMY_HORSE_HEALTH				= 25.0f;
	public static final float		ENEMY_HORSE_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_HORSE_DAMAGE		= new ApoDefencePoint( 4.6f, 5.1f );
	public static final float		ENEMY_HORSE_SPEED_UPGRADE		= 0.012f;
	public static final float		ENEMY_HORSE_HEALTH_UPGRADE		= 11.5f;
	public static final float		ENEMY_HORSE_ARMOR_UPGRADE		= 0.18f;
	public static final float		ENEMY_HORSE_DAMAGE_UPGRADE		= 0.055f;
	public static final float		ENEMY_HORSE_MONEY				= 7.1f;
	
	public static final int			ENEMY_DEVIL						= 6;
	public static final String		ENEMY_DEVIL_NAME				= "devil";
	public static final float		ENEMY_DEVIL_SPEED				= 75f;
	public static final float		ENEMY_DEVIL_HEALTH				= 15.0f;
	public static final float		ENEMY_DEVIL_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_DEVIL_DAMAGE		= new ApoDefencePoint( 5.5f, 5.9f );
	public static final float		ENEMY_DEVIL_SPEED_UPGRADE		= 0.013f;
	public static final float		ENEMY_DEVIL_HEALTH_UPGRADE		= 14f;
	public static final float		ENEMY_DEVIL_ARMOR_UPGRADE		= 0.24f;
	public static final float		ENEMY_DEVIL_DAMAGE_UPGRADE		= 0.044f;
	public static final float		ENEMY_DEVIL_MONEY				= 7.5f;
	
	public static final int			ENEMY_AQUATIC					= 7;
	public static final String		ENEMY_AQUATIC_NAME				= "aquatic";
	public static final float		ENEMY_AQUATIC_SPEED				= 70f;
	public static final float		ENEMY_AQUATIC_HEALTH			= 20.0f;
	public static final float		ENEMY_AQUATIC_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_AQUATIC_DAMAGE	= new ApoDefencePoint( 4.0f, 5.2f );
	public static final float		ENEMY_AQUATIC_SPEED_UPGRADE		= 0.0115f;
	public static final float		ENEMY_AQUATIC_HEALTH_UPGRADE	= 10f;
	public static final float		ENEMY_AQUATIC_ARMOR_UPGRADE		= 0.21f;
	public static final float		ENEMY_AQUATIC_DAMAGE_UPGRADE	= 0.045f;
	public static final float		ENEMY_AQUATIC_MONEY				= 7.3f;
	
	public static final int			ENEMY_DRACULA					= 8;
	public static final String		ENEMY_DRACULA_NAME				= "dracula";
	public static final float		ENEMY_DRACULA_SPEED				= 77f;
	public static final float		ENEMY_DRACULA_HEALTH			= 25.0f;
	public static final float		ENEMY_DRACULA_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_DRACULA_DAMAGE	= new ApoDefencePoint( 4.9f, 5.4f );
	public static final float		ENEMY_DRACULA_SPEED_UPGRADE		= 0.010f;
	public static final float		ENEMY_DRACULA_HEALTH_UPGRADE	= 12f;
	public static final float		ENEMY_DRACULA_ARMOR_UPGRADE		= 0.182f;
	public static final float		ENEMY_DRACULA_DAMAGE_UPGRADE	= 0.036f;
	public static final float		ENEMY_DRACULA_MONEY				= 7.5f;
	
	public static final int			ENEMY_FIRE						= 9;
	public static final String		ENEMY_FIRE_NAME					= "fire";
	public static final float		ENEMY_FIRE_SPEED				= 68f;
	public static final float		ENEMY_FIRE_HEALTH				= 25.0f;
	public static final float		ENEMY_FIRE_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_FIRE_DAMAGE		= new ApoDefencePoint( 4.8f, 6.1f );
	public static final float		ENEMY_FIRE_SPEED_UPGRADE		= 0.012f;
	public static final float		ENEMY_FIRE_HEALTH_UPGRADE		= 14.5f;
	public static final float		ENEMY_FIRE_ARMOR_UPGRADE		= 0.194f;
	public static final float		ENEMY_FIRE_DAMAGE_UPGRADE		= 0.037f;
	public static final float		ENEMY_FIRE_MONEY				= 7.9f;
	
	public static final int			ENEMY_DRAGON					= 10;
	public static final String		ENEMY_DRAGON_NAME				= "dragon";
	public static final float		ENEMY_DRAGON_SPEED				= 70f;
	public static final float		ENEMY_DRAGON_HEALTH				= 20.0f;
	public static final float		ENEMY_DRAGON_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_DRAGON_DAMAGE		= new ApoDefencePoint( 5.6f, 7.8f );
	public static final float		ENEMY_DRAGON_SPEED_UPGRADE		= 0.012f;
	public static final float		ENEMY_DRAGON_HEALTH_UPGRADE		= 15f;
	public static final float		ENEMY_DRAGON_ARMOR_UPGRADE		= 0.15f;
	public static final float		ENEMY_DRAGON_DAMAGE_UPGRADE		= 0.038f;
	public static final float		ENEMY_DRAGON_MONEY				= 8.0f;
	
	public static final int			ENEMY_GOBLIN					= 11;
	public static final String		ENEMY_GOBLIN_NAME				= "goblin";
	public static final float		ENEMY_GOBLIN_SPEED				= 60f;
	public static final float		ENEMY_GOBLIN_HEALTH				= 20.0f;
	public static final float		ENEMY_GOBLIN_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_GOBLIN_DAMAGE		= new ApoDefencePoint( 7.6f, 9.8f );
	public static final float		ENEMY_GOBLIN_SPEED_UPGRADE		= 0.010f;
	public static final float		ENEMY_GOBLIN_HEALTH_UPGRADE		= 18f;
	public static final float		ENEMY_GOBLIN_ARMOR_UPGRADE		= 0.18f;
	public static final float		ENEMY_GOBLIN_DAMAGE_UPGRADE		= 0.047f;
	public static final float		ENEMY_GOBLIN_MONEY				= 7.8f;
	
	public static final int			ENEMY_KNIGHT					= 12;
	public static final String		ENEMY_KNIGHT_NAME				= "knight";
	public static final float		ENEMY_KNIGHT_SPEED				= 72f;
	public static final float		ENEMY_KNIGHT_HEALTH				= 20.0f;
	public static final float		ENEMY_KNIGHT_ARMOR				= 7f;
	public static final ApoDefencePoint		ENEMY_KNIGHT_DAMAGE		= new ApoDefencePoint( 4.3f, 5.2f );
	public static final float		ENEMY_KNIGHT_SPEED_UPGRADE		= 0.011f;
	public static final float		ENEMY_KNIGHT_HEALTH_UPGRADE		= 14.2f;
	public static final float		ENEMY_KNIGHT_ARMOR_UPGRADE		= 0.16f;
	public static final float		ENEMY_KNIGHT_DAMAGE_UPGRADE		= 0.039f;
	public static final float		ENEMY_KNIGHT_MONEY				= 7.95f;
	
	public static final int			ENEMY_MONK						= 13;
	public static final String		ENEMY_MONK_NAME					= "monk";
	public static final float		ENEMY_MONK_SPEED				= 67f;
	public static final float		ENEMY_MONK_HEALTH				= 15.0f;
	public static final float		ENEMY_MONK_ARMOR				= 2f;
	public static final ApoDefencePoint		ENEMY_MONK_DAMAGE		= new ApoDefencePoint( 4.0f, 4.8f );
	public static final float		ENEMY_MONK_SPEED_UPGRADE		= 0.012f;
	public static final float		ENEMY_MONK_HEALTH_UPGRADE		= 13.2f;
	public static final float		ENEMY_MONK_ARMOR_UPGRADE		= 0.17f;
	public static final float		ENEMY_MONK_DAMAGE_UPGRADE		= 0.040f;
	public static final float		ENEMY_MONK_MONEY				= 8.05f;
	
	public static final int			ENEMY_MUMMY						= 14;
	public static final String		ENEMY_MUMMY_NAME				= "mummy";
	public static final float		ENEMY_MUMMY_SPEED				= 58f;
	public static final float		ENEMY_MUMMY_HEALTH				= 13.0f;
	public static final float		ENEMY_MUMMY_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_MUMMY_DAMAGE		= new ApoDefencePoint( 4.5f, 5.6f );
	public static final float		ENEMY_MUMMY_SPEED_UPGRADE		= 0.011f;
	public static final float		ENEMY_MUMMY_HEALTH_UPGRADE		= 15.2f;
	public static final float		ENEMY_MUMMY_ARMOR_UPGRADE		= 0.17f;
	public static final float		ENEMY_MUMMY_DAMAGE_UPGRADE		= 0.049f;
	public static final float		ENEMY_MUMMY_MONEY				= 6.3f;
	
	public static final int			ENEMY_SKELETON					= 15;
	public static final String		ENEMY_SKELETON_NAME				= "skeleton";
	public static final float		ENEMY_SKELETON_SPEED			= 62f;
	public static final float		ENEMY_SKELETON_HEALTH			= 15.0f;
	public static final float		ENEMY_SKELETON_ARMOR			= 2f;
	public static final ApoDefencePoint		ENEMY_SKELETON_DAMAGE	= new ApoDefencePoint( 4.1f, 4.3f );
	public static final float		ENEMY_SKELETON_SPEED_UPGRADE	= 0.012f;
	public static final float		ENEMY_SKELETON_HEALTH_UPGRADE	= 14.7f;
	public static final float		ENEMY_SKELETON_ARMOR_UPGRADE	= 0.25f;
	public static final float		ENEMY_SKELETON_DAMAGE_UPGRADE	= 0.036f;
	public static final float		ENEMY_SKELETON_MONEY			= 6.1f;
	
	public static final int			ENEMY_SKULL						= 16;
	public static final String		ENEMY_SKULL_NAME				= "skull";
	public static final float		ENEMY_SKULL_SPEED				= 85f;
	public static final float		ENEMY_SKULL_HEALTH				= 15.0f;
	public static final float		ENEMY_SKULL_ARMOR				= 3f;
	public static final ApoDefencePoint		ENEMY_SKULL_DAMAGE		= new ApoDefencePoint( 5.1f, 5.3f );
	public static final float		ENEMY_SKULL_SPEED_UPGRADE		= 0.013f;
	public static final float		ENEMY_SKULL_HEALTH_UPGRADE		= 13.9f;
	public static final float		ENEMY_SKULL_ARMOR_UPGRADE		= 0.15f;
	public static final float		ENEMY_SKULL_DAMAGE_UPGRADE		= 0.037f;
	public static final float		ENEMY_SKULL_MONEY				= 7.5f;
	
	public static final int			ENEMY_SNAKE						= 17;
	public static final String		ENEMY_SNAKE_NAME				= "snake";
	public static final float		ENEMY_SNAKE_SPEED				= 73f;
	public static final float		ENEMY_SNAKE_HEALTH				= 20.5f;
	public static final float		ENEMY_SNAKE_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_SNAKE_DAMAGE		= new ApoDefencePoint( 7.1f, 8.3f );
	public static final float		ENEMY_SNAKE_SPEED_UPGRADE		= 0.012f;
	public static final float		ENEMY_SNAKE_HEALTH_UPGRADE		= 14.6f;
	public static final float		ENEMY_SNAKE_ARMOR_UPGRADE		= 0.15f;
	public static final float		ENEMY_SNAKE_DAMAGE_UPGRADE		= 0.039f;
	public static final float		ENEMY_SNAKE_MONEY				= 8.9f;
	
	public static final int			ENEMY_UNDEAD					= 18;
	public static final String		ENEMY_UNDEAD_NAME				= "undead";
	public static final float		ENEMY_UNDEAD_SPEED				= 64f;
	public static final float		ENEMY_UNDEAD_HEALTH				= 22.5f;
	public static final float		ENEMY_UNDEAD_ARMOR				= 1f;
	public static final ApoDefencePoint		ENEMY_UNDEAD_DAMAGE		= new ApoDefencePoint( 3.5f, 4.5f );
	public static final float		ENEMY_UNDEAD_SPEED_UPGRADE		= 0.010f;
	public static final float		ENEMY_UNDEAD_HEALTH_UPGRADE		= 15.1f;
	public static final float		ENEMY_UNDEAD_ARMOR_UPGRADE		= 0.17f;
	public static final float		ENEMY_UNDEAD_DAMAGE_UPGRADE		= 0.039f;
	public static final float		ENEMY_UNDEAD_MONEY				= 6.9f;
	
	public static final int			ENEMY_UNDEFINED					= 19;
	public static final String		ENEMY_UNDEFINED_NAME			= "undefined";
	public static final float		ENEMY_UNDEFINED_SPEED			= 79f;
	public static final float		ENEMY_UNDEFINED_HEALTH			= 19.7f;
	public static final float		ENEMY_UNDEFINED_ARMOR			= 3f;
	public static final ApoDefencePoint		ENEMY_UNDEFINED_DAMAGE	= new ApoDefencePoint( 7.5f, 8.3f );
	public static final float		ENEMY_UNDEFINED_SPEED_UPGRADE	= 0.012f;
	public static final float		ENEMY_UNDEFINED_HEALTH_UPGRADE	= 14.1f;
	public static final float		ENEMY_UNDEFINED_ARMOR_UPGRADE	= 0.19f;
	public static final float		ENEMY_UNDEFINED_DAMAGE_UPGRADE	= 0.038f;
	public static final float		ENEMY_UNDEFINED_MONEY			= 9.5f;
	
	public static final int			ENEMY_WEREWOLF					= 20;
	public static final String		ENEMY_WEREWOLF_NAME				= "werewolf";
	public static final float		ENEMY_WEREWOLF_SPEED			= 81f;
	public static final float		ENEMY_WEREWOLF_HEALTH			= 17.5f;
	public static final float		ENEMY_WEREWOLF_ARMOR			= 1.5f;
	public static final ApoDefencePoint		ENEMY_WEREWOLF_DAMAGE	= new ApoDefencePoint( 4.6f, 5.3f );
	public static final float		ENEMY_WEREWOLF_SPEED_UPGRADE	= 0.011f;
	public static final float		ENEMY_WEREWOLF_HEALTH_UPGRADE	= 13.9f;
	public static final float		ENEMY_WEREWOLF_ARMOR_UPGRADE	= 0.21f;
	public static final float		ENEMY_WEREWOLF_DAMAGE_UPGRADE	= 0.040f;
	public static final float		ENEMY_WEREWOLF_MONEY			= 6.8f;

}
