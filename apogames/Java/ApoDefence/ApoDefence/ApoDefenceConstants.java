package apoDefence;

/**
 * enthält viele statische Variablen, die für das Spiel gebraucht werden
 * @author Dirk Aporius
 *
 */
public class ApoDefenceConstants
{

	public static final int			EMPTY				= 0;
	public static final int			LEFT_UP				= 1;
	public static final int			LEFT_UP_RIGHT		= 2;
	public static final int			LEFT				= 3;
	public static final int			LEFT_DOWN			= 4;
	public static final int			LEFT_RIGHT			= 5;
	public static final int			LEFT_DOWN_RIGHT		= 6;
	public static final int			LEFT_DOWN_RIGHT_UP	= 7;
	public static final int			RIGHT				= 8;
	public static final int			UP					= 9;
	public static final int			UP_DOWN				= 10;
	public static final int			UP_RIGHT			= 11;
	public static final int			UP_RIGHT_DOWN		= 12;
	public static final int			DOWN				= 13;
	public static final int			DOWN_RIGHT			= 14;
	public static final int			DOWN_LEFT_UP		= 15;
	public static final int			SAND				= 16;
	
	public static final int			WAY					= 0;
	public static final int			CASTLE_UPGRADE		= 1;
	public static final int			CASTLE_DEFEND		= 2;
	public static final int			WAYPOINT			= 3;
	
	public static final int			TOWER_ARCH			= 1;
	public static final int			TOWER_FIRE			= 2;
	public static final int			TOWER_ICE			= 3;
	public static final int			TOWER_LIGHT			= 4;
	public static final int			TOWER_ULTRA			= 5;
	
	public static final float		SHOOT_TIME			= 2.9f;
	public static final float		SHOOT_SPEED			= 0.08f;
	
	public static final int			SHOOT_ARCH			= 1;
	public static final int			SHOOT_FIRE			= 2;
	public static final int			SHOOT_ICE			= 3;
	public static final int			SHOOT_LIGHT			= 4;
	public static final int			SHOOT_ULTRA			= 5;
	
	public static final float		ENEMY_ICE			= 0.6f;
	public static final float		ENEMY_ICE_LION		= 0.8f;
	public static final float		ENEMY_ICE_HORSE		= 0.7f;
	
	public static final float		ENEMY_ICE_DAMAGE_DEVIL	= 0.1f;
	public static final float		ENEMY_ICE_DAMAGE_FIRE	= 0.15f;
	public static final float		ENEMY_ICE_DAMAGE_DRAGON	= 0.05f;
	
	public static final float		ENEMY_FIRE_DAMAGE_AQUATIC	= 0.05f;
	
	public static final float		ENEMY_IS_UNDEAD_ULTRA		= 0.5f;
	public static final float		ENEMY_IS_UNDEAD_FIRE		= 1.15f;
	
	public static final float		ENEMY_IS_NOT_UNDEAD_ICE		= 1.1f;
	public static final float		ENEMY_IS_NOT_UNDEAD_LIGHT	= 0.9f;
	
	public static final int			OPTIONS_GENERAL				= 0;
	public static final int			OPTIONS_RESEARCH			= 1;
	public static final int			OPTIONS_TOWERS				= 2;
	public static final int			OPTIONS_ENEMIES				= 3;
	
	public static final float		DIFFICULTY_HARD				= 1.9f;
	public static final float		DIFFICULTY_MEDIUM			= 2;
	public static final float		DIFFICULTY_EASY				= 2.5f;
	
	public static final float		DIFFICULTY_EASY_CONST		= 0.9f;
	public static final float		DIFFICULTY_HARD_CONST		= 1.05f;

	public static final int			REPAIR_COST					= 1;
	public static final int			REPAIR_HEALTH				= 2;
	
	public static int				TOWER_ARCH_RANGE		= ApoDefenceConstantsOriginal.TOWER_ARCH_RANGE;
	public static int				TOWER_ARCH_SPEED		= ApoDefenceConstantsOriginal.TOWER_ARCH_SPEED;
	public static int				TOWER_ARCH_HEALTH		= ApoDefenceConstantsOriginal.TOWER_ARCH_HEALTH;
	public static int				TOWER_ARCH_AMOR			= ApoDefenceConstantsOriginal.TOWER_ARCH_AMOR;
	public static ApoDefencePoint	TOWER_ARCH_POINTATTACK	= ApoDefenceConstantsOriginal.TOWER_ARCH_POINTATTACK;
	
	public static float				TOWER_ARCH_RANGE_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ARCH_RANGE_UPGRADE;
	public static float				TOWER_ARCH_SPEED_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ARCH_SPEED_UPGRADE;
	public static float				TOWER_ARCH_HEALTH_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ARCH_HEALTH_UPGRADE;
	public static float				TOWER_ARCH_AMOR_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ARCH_AMOR_UPGRADE;
	public static float				TOWER_ARCH_POINTATTACK_UPGRADE	= ApoDefenceConstantsOriginal.TOWER_ARCH_POINTATTACK_UPGRADE;
	
	public static int				TOWER_FIRE_RANGE		= ApoDefenceConstantsOriginal.TOWER_FIRE_RANGE;
	public static int				TOWER_FIRE_SPEED		= ApoDefenceConstantsOriginal.TOWER_FIRE_SPEED;
	public static int				TOWER_FIRE_HEALTH		= ApoDefenceConstantsOriginal.TOWER_FIRE_HEALTH;
	public static int				TOWER_FIRE_AMOR			= ApoDefenceConstantsOriginal.TOWER_FIRE_AMOR;
	public static ApoDefencePoint	TOWER_FIRE_POINTATTACK	= ApoDefenceConstantsOriginal.TOWER_FIRE_POINTATTACK;
	
	public static float				TOWER_FIRE_RANGE_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_FIRE_RANGE_UPGRADE;
	public static float				TOWER_FIRE_SPEED_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_FIRE_SPEED_UPGRADE;
	public static float				TOWER_FIRE_HEALTH_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_FIRE_HEALTH_UPGRADE;
	public static float				TOWER_FIRE_AMOR_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_FIRE_AMOR_UPGRADE;
	public static float				TOWER_FIRE_POINTATTACK_UPGRADE	= ApoDefenceConstantsOriginal.TOWER_FIRE_POINTATTACK_UPGRADE;
	
	public static int				TOWER_ICE_RANGE			= ApoDefenceConstantsOriginal.TOWER_ICE_RANGE;
	public static int				TOWER_ICE_SPEED			= ApoDefenceConstantsOriginal.TOWER_ICE_SPEED;
	public static int				TOWER_ICE_HEALTH		= ApoDefenceConstantsOriginal.TOWER_ICE_HEALTH;
	public static int				TOWER_ICE_AMOR			= ApoDefenceConstantsOriginal.TOWER_ICE_AMOR;
	public static ApoDefencePoint	TOWER_ICE_POINTATTACK	= ApoDefenceConstantsOriginal.TOWER_ICE_POINTATTACK;
	
	public static float				TOWER_ICE_RANGE_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ICE_RANGE_UPGRADE;
	public static float				TOWER_ICE_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ICE_SPEED_UPGRADE;
	public static float				TOWER_ICE_HEALTH_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ICE_HEALTH_UPGRADE;
	public static float				TOWER_ICE_AMOR_UPGRADE			= ApoDefenceConstantsOriginal.TOWER_ICE_AMOR_UPGRADE;
	public static float				TOWER_ICE_POINTATTACK_UPGRADE	= ApoDefenceConstantsOriginal.TOWER_ICE_POINTATTACK_UPGRADE;
	
	public static int				TOWER_LIGHT_RANGE		= ApoDefenceConstantsOriginal.TOWER_LIGHT_RANGE;
	public static int				TOWER_LIGHT_SPEED		= ApoDefenceConstantsOriginal.TOWER_LIGHT_SPEED;
	public static int				TOWER_LIGHT_HEALTH		= ApoDefenceConstantsOriginal.TOWER_LIGHT_HEALTH;
	public static int				TOWER_LIGHT_AMOR		= ApoDefenceConstantsOriginal.TOWER_LIGHT_AMOR;
	public static ApoDefencePoint	TOWER_LIGHT_POINTATTACK	= ApoDefenceConstantsOriginal.TOWER_LIGHT_POINTATTACK;
	
	public static float				TOWER_LIGHT_RANGE_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_LIGHT_RANGE_UPGRADE;
	public static float				TOWER_LIGHT_SPEED_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_LIGHT_SPEED_UPGRADE;
	public static float				TOWER_LIGHT_HEALTH_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_LIGHT_HEALTH_UPGRADE;
	public static float				TOWER_LIGHT_AMOR_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_LIGHT_AMOR_UPGRADE;
	public static float				TOWER_LIGHT_POINTATTACK_UPGRADE	= ApoDefenceConstantsOriginal.TOWER_LIGHT_POINTATTACK_UPGRADE;
	
	public static int				TOWER_ULTRA_RANGE		= ApoDefenceConstantsOriginal.TOWER_ULTRA_RANGE;
	public static int				TOWER_ULTRA_SPEED		= ApoDefenceConstantsOriginal.TOWER_ULTRA_SPEED;
	public static int				TOWER_ULTRA_HEALTH		= ApoDefenceConstantsOriginal.TOWER_ULTRA_HEALTH;
	public static int				TOWER_ULTRA_AMOR		= ApoDefenceConstantsOriginal.TOWER_ULTRA_AMOR;
	public static ApoDefencePoint	TOWER_ULTRA_POINTATTACK	= ApoDefenceConstantsOriginal.TOWER_ULTRA_POINTATTACK;
	
	public static float				TOWER_ULTRA_RANGE_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ULTRA_RANGE_UPGRADE;
	public static float				TOWER_ULTRA_SPEED_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ULTRA_SPEED_UPGRADE;
	public static float				TOWER_ULTRA_HEALTH_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ULTRA_HEALTH_UPGRADE;
	public static float				TOWER_ULTRA_AMOR_UPGRADE		= ApoDefenceConstantsOriginal.TOWER_ULTRA_AMOR_UPGRADE;
	public static float				TOWER_ULTRA_POINTATTACK_UPGRADE	= ApoDefenceConstantsOriginal.TOWER_ULTRA_POINTATTACK_UPGRADE;
	
	public static int				TOWER_ARCH_PRICE		= ApoDefenceConstantsOriginal.TOWER_ARCH_PRICE;
	public static int				TOWER_FIRE_PRICE		= ApoDefenceConstantsOriginal.TOWER_FIRE_PRICE;
	public static int				TOWER_ICE_PRICE			= ApoDefenceConstantsOriginal.TOWER_ICE_PRICE;
	public static int				TOWER_LIGHT_PRICE		= ApoDefenceConstantsOriginal.TOWER_LIGHT_PRICE;
	public static int				TOWER_ULTRA_PRICE		= ApoDefenceConstantsOriginal.TOWER_ULTRA_PRICE;
	
	public static int				TOWER_ARCH_PRICE_UPGRADE 	= ApoDefenceConstantsOriginal.TOWER_ARCH_PRICE_UPGRADE;
	public static int				TOWER_FIRE_PRICE_UPGRADE 	= ApoDefenceConstantsOriginal.TOWER_FIRE_PRICE_UPGRADE;
	public static int				TOWER_ICE_PRICE_UPGRADE 	= ApoDefenceConstantsOriginal.TOWER_ICE_PRICE_UPGRADE;
	public static int				TOWER_LIGHT_PRICE_UPGRADE 	= ApoDefenceConstantsOriginal.TOWER_LIGHT_PRICE_UPGRADE;
	public static int				TOWER_ULTRA_PRICE_UPGRADE 	= ApoDefenceConstantsOriginal.TOWER_ULTRA_PRICE_UPGRADE;
	
	public static float				TOWER_ARCH_PRICE_UPGRADE_PLUS 	= ApoDefenceConstantsOriginal.TOWER_ARCH_PRICE_UPGRADE_PLUS;
	public static float				TOWER_FIRE_PRICE_UPGRADE_PLUS 	= ApoDefenceConstantsOriginal.TOWER_FIRE_PRICE_UPGRADE_PLUS;
	public static float				TOWER_ICE_PRICE_UPGRADE_PLUS 	= ApoDefenceConstantsOriginal.TOWER_ICE_PRICE_UPGRADE_PLUS;
	public static float				TOWER_LIGHT_PRICE_UPGRADE_PLUS 	= ApoDefenceConstantsOriginal.TOWER_LIGHT_PRICE_UPGRADE_PLUS;
	public static float				TOWER_ULTRA_PRICE_UPGRADE_PLUS 	= ApoDefenceConstantsOriginal.TOWER_ULTRA_PRICE_UPGRADE_PLUS;
			
	public static int				TOWER_ARCH_LEVEL		= ApoDefenceConstantsOriginal.TOWER_ARCH_LEVEL;
	public static int				TOWER_FIRE_LEVEL		= ApoDefenceConstantsOriginal.TOWER_FIRE_LEVEL;
	public static int				TOWER_ICE_LEVEL			= ApoDefenceConstantsOriginal.TOWER_ICE_LEVEL;
	public static int				TOWER_LIGHT_LEVEL		= ApoDefenceConstantsOriginal.TOWER_LIGHT_LEVEL;
	public static int				TOWER_ULTRA_LEVEL		= ApoDefenceConstantsOriginal.TOWER_ULTRA_LEVEL;
	
	public static float				TOWER_CASTLE_PRICE_UPGRADE	= ApoDefenceConstantsOriginal.TOWER_CASTLE_PRICE_UPGRADE;
	public static int				TOWER_CASTLE_HEALTH_UPGRADE	= ApoDefenceConstantsOriginal.TOWER_CASTLE_HEALTH_UPGRADE;
	public static int				TOWER_CASTLE_MAX_LEVEL		= ApoDefenceConstantsOriginal.TOWER_CASTLE_MAX_LEVEL;
	
	public static int				TOWER_ARCH_MAXLEVEL					= ApoDefenceConstantsOriginal.TOWER_ARCH_MAXLEVEL;
	public static int				TOWER_FIRE_MAXLEVEL					= ApoDefenceConstantsOriginal.TOWER_FIRE_MAXLEVEL;
	public static int				TOWER_ICE_MAXLEVEL					= ApoDefenceConstantsOriginal.TOWER_ICE_MAXLEVEL;
	public static int				TOWER_LIGHT_MAXLEVEL				= ApoDefenceConstantsOriginal.TOWER_LIGHT_MAXLEVEL;
	public static int				TOWER_ULTRA_MAXLEVEL				= ApoDefenceConstantsOriginal.TOWER_ULTRA_MAXLEVEL;
	
	public static int				TOWER_RESEARCH_TECHLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_TECHLEVEL;
	public static int				TOWER_RESEARCH_ARMORLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_ARMORLEVEL;
	public static int				TOWER_RESEARCH_SPEEDLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_SPEEDLEVEL;
	public static int				TOWER_RESEARCH_ATTACKLEVEL			= ApoDefenceConstantsOriginal.TOWER_RESEARCH_ATTACKLEVEL;
	
	public static int				TOWER_RESEARCH_MAX_TECHLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_TECHLEVEL;
	public static int				TOWER_RESEARCH_MAX_ARMORLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_ARMORLEVEL;
	public static int				TOWER_RESEARCH_MAX_SPEEDLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_SPEEDLEVEL;
	public static int				TOWER_RESEARCH_MAX_ATTACKLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_MAX_ATTACKLEVEL;
	
	public static int				TOWER_RESEARCH_PRICE_TECHLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_TECHLEVEL;
	public static int				TOWER_RESEARCH_PRICE_ARMORLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_ARMORLEVEL;
	public static int				TOWER_RESEARCH_PRICE_SPEEDLEVEL		= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_SPEEDLEVEL;
	public static int				TOWER_RESEARCH_PRICE_ATTACKLEVEL	= ApoDefenceConstantsOriginal.TOWER_RESEARCH_PRICE_ATTACKLEVEL;
	
	public static final int			TOWER_RESEARCH_PRICE_ARMORLEVEL_UPGRADE	= 5;
	
	public static final int			TOWER_RESEARCH_WAIT						= 100;
	public static final int			TOWER_RESEARCH_WAIT_TECHLEVEL_UPGRADE	= 50;
	public static final int			TOWER_RESEARCH_WAIT_SPEEDLEVEL_UPGRADE	= 15;
	public static final int			TOWER_RESEARCH_WAIT_ARMORLEVEL_UPGRADE	= 3;
	public static final int			TOWER_RESEARCH_WAIT_ATTACKLEVEL_UPGRADE	= 75;
	
	public static int				MAX_ENEMIES							= ApoDefenceConstantsOriginal.MAX_ENEMIES;
	public static float				DIFFICULTY							= ApoDefenceConstantsOriginal.DIFFICULTY;
	public static int				WAVE_TIME							= ApoDefenceConstantsOriginal.WAVE_TIME;
	public static int				MAX_WAVE							= ApoDefenceConstantsOriginal.MAX_WAVE;
	public static int				MONEY								= -1;
	public static int				MAX_TOWERS							= -1;
	// enemies
	
	public static int				ENEMY_BIRD							= ApoDefenceConstantsOriginal.ENEMY_BIRD;
	public static String			ENEMY_BIRD_NAME						= ApoDefenceConstantsOriginal.ENEMY_BIRD_NAME;
	public static float				ENEMY_BIRD_SPEED					= ApoDefenceConstantsOriginal.ENEMY_BIRD_SPEED;
	public static float				ENEMY_BIRD_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_BIRD_HEALTH;
	public static float				ENEMY_BIRD_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_BIRD_ARMOR;
	public static ApoDefencePoint	ENEMY_BIRD_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_BIRD_DAMAGE;
	public static float				ENEMY_BIRD_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_BIRD_SPEED_UPGRADE;
	public static float				ENEMY_BIRD_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_BIRD_HEALTH_UPGRADE;
	public static float				ENEMY_BIRD_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_BIRD_ARMOR_UPGRADE;
	public static float				ENEMY_BIRD_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_BIRD_DAMAGE_UPGRADE;
	public static float				ENEMY_BIRD_MONEY					= ApoDefenceConstantsOriginal.ENEMY_BIRD_MONEY;
	
	public static int				ENEMY_GHOST							= ApoDefenceConstantsOriginal.ENEMY_GHOST;
	public static String			ENEMY_GHOST_NAME					= ApoDefenceConstantsOriginal.ENEMY_GHOST_NAME;
	public static float				ENEMY_GHOST_SPEED					= ApoDefenceConstantsOriginal.ENEMY_GHOST_SPEED;
	public static float				ENEMY_GHOST_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_GHOST_HEALTH;
	public static float				ENEMY_GHOST_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_GHOST_ARMOR;
	public static ApoDefencePoint	ENEMY_GHOST_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_GHOST_DAMAGE;
	public static float				ENEMY_GHOST_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GHOST_SPEED_UPGRADE;
	public static float				ENEMY_GHOST_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GHOST_HEALTH_UPGRADE;
	public static float				ENEMY_GHOST_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GHOST_ARMOR_UPGRADE;
	public static float				ENEMY_GHOST_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GHOST_DAMAGE_UPGRADE;
	public static float				ENEMY_GHOST_MONEY					= ApoDefenceConstantsOriginal.ENEMY_GHOST_MONEY;
	
	public static int				ENEMY_DOG							= ApoDefenceConstantsOriginal.ENEMY_DOG;
	public static String			ENEMY_DOG_NAME						= ApoDefenceConstantsOriginal.ENEMY_DOG_NAME;
	public static float				ENEMY_DOG_SPEED						= ApoDefenceConstantsOriginal.ENEMY_DOG_SPEED;
	public static float				ENEMY_DOG_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_DOG_HEALTH;
	public static float				ENEMY_DOG_ARMOR						= ApoDefenceConstantsOriginal.ENEMY_DOG_ARMOR;
	public static ApoDefencePoint	ENEMY_DOG_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_DOG_DAMAGE;
	public static float				ENEMY_DOG_SPEED_UPGRADE				= ApoDefenceConstantsOriginal.ENEMY_DOG_SPEED_UPGRADE;
	public static float				ENEMY_DOG_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DOG_HEALTH_UPGRADE;
	public static float				ENEMY_DOG_ARMOR_UPGRADE				= ApoDefenceConstantsOriginal.ENEMY_DOG_ARMOR_UPGRADE;
	public static float				ENEMY_DOG_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DOG_DAMAGE_UPGRADE;
	public static float				ENEMY_DOG_MONEY						= ApoDefenceConstantsOriginal.ENEMY_DOG_MONEY;
	
	public static int				ENEMY_LION							= ApoDefenceConstantsOriginal.ENEMY_LION;
	public static String			ENEMY_LION_NAME						= ApoDefenceConstantsOriginal.ENEMY_LION_NAME;
	public static float				ENEMY_LION_SPEED					= ApoDefenceConstantsOriginal.ENEMY_LION_SPEED;
	public static float				ENEMY_LION_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_LION_HEALTH;
	public static float				ENEMY_LION_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_LION_ARMOR;
	public static ApoDefencePoint	ENEMY_LION_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_LION_DAMAGE;
	public static float				ENEMY_LION_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_LION_SPEED_UPGRADE;
	public static float				ENEMY_LION_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_LION_HEALTH_UPGRADE;
	public static float				ENEMY_LION_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_LION_ARMOR_UPGRADE;
	public static float				ENEMY_LION_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_LION_DAMAGE_UPGRADE;
	public static float				ENEMY_LION_MONEY					= ApoDefenceConstantsOriginal.ENEMY_LION_MONEY;
	
	public static int				ENEMY_HORSE							= ApoDefenceConstantsOriginal.ENEMY_HORSE;
	public static String			ENEMY_HORSE_NAME					= ApoDefenceConstantsOriginal.ENEMY_HORSE_NAME;
	public static float				ENEMY_HORSE_SPEED					= ApoDefenceConstantsOriginal.ENEMY_HORSE_SPEED;
	public static float				ENEMY_HORSE_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_HORSE_HEALTH;
	public static float				ENEMY_HORSE_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_HORSE_ARMOR;
	public static ApoDefencePoint	ENEMY_HORSE_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_HORSE_DAMAGE;
	public static float				ENEMY_HORSE_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_HORSE_SPEED_UPGRADE;
	public static float				ENEMY_HORSE_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_HORSE_HEALTH_UPGRADE;
	public static float				ENEMY_HORSE_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_HORSE_ARMOR_UPGRADE;
	public static float				ENEMY_HORSE_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_HORSE_DAMAGE_UPGRADE;
	public static float				ENEMY_HORSE_MONEY					= ApoDefenceConstantsOriginal.ENEMY_HORSE_MONEY;
	
	public static int				ENEMY_DEVIL							= ApoDefenceConstantsOriginal.ENEMY_DEVIL;
	public static String			ENEMY_DEVIL_NAME					= ApoDefenceConstantsOriginal.ENEMY_DEVIL_NAME;
	public static float				ENEMY_DEVIL_SPEED					= ApoDefenceConstantsOriginal.ENEMY_DEVIL_SPEED;
	public static float				ENEMY_DEVIL_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_DEVIL_HEALTH;
	public static float				ENEMY_DEVIL_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_DEVIL_ARMOR;
	public static ApoDefencePoint	ENEMY_DEVIL_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_DEVIL_DAMAGE;
	public static float				ENEMY_DEVIL_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_SPEED_UPGRADE;
	public static float				ENEMY_DEVIL_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_HEALTH_UPGRADE;
	public static float				ENEMY_DEVIL_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_ARMOR_UPGRADE;
	public static float				ENEMY_DEVIL_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DEVIL_DAMAGE_UPGRADE;
	public static float				ENEMY_DEVIL_MONEY					= ApoDefenceConstantsOriginal.ENEMY_DEVIL_MONEY;
	
	public static int				ENEMY_AQUATIC						= ApoDefenceConstantsOriginal.ENEMY_AQUATIC;
	public static String			ENEMY_AQUATIC_NAME					= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_NAME;
	public static float				ENEMY_AQUATIC_SPEED					= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_SPEED;
	public static float				ENEMY_AQUATIC_HEALTH				= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_HEALTH;
	public static float				ENEMY_AQUATIC_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_ARMOR;
	public static ApoDefencePoint	ENEMY_AQUATIC_DAMAGE				= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_DAMAGE;
	public static float				ENEMY_AQUATIC_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_SPEED_UPGRADE;
	public static float				ENEMY_AQUATIC_HEALTH_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_HEALTH_UPGRADE;
	public static float				ENEMY_AQUATIC_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_ARMOR_UPGRADE;
	public static float				ENEMY_AQUATIC_DAMAGE_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_DAMAGE_UPGRADE;
	public static float				ENEMY_AQUATIC_MONEY					= ApoDefenceConstantsOriginal.ENEMY_AQUATIC_MONEY;
	
	public static int				ENEMY_DRACULA						= ApoDefenceConstantsOriginal.ENEMY_DRACULA;
	public static String			ENEMY_DRACULA_NAME					= ApoDefenceConstantsOriginal.ENEMY_DRACULA_NAME;
	public static float				ENEMY_DRACULA_SPEED					= ApoDefenceConstantsOriginal.ENEMY_DRACULA_SPEED;
	public static float				ENEMY_DRACULA_HEALTH				= ApoDefenceConstantsOriginal.ENEMY_DRACULA_HEALTH;
	public static float				ENEMY_DRACULA_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_DRACULA_ARMOR;
	public static ApoDefencePoint	ENEMY_DRACULA_DAMAGE				= ApoDefenceConstantsOriginal.ENEMY_DRACULA_DAMAGE;
	public static float				ENEMY_DRACULA_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DRACULA_SPEED_UPGRADE;
	public static float				ENEMY_DRACULA_HEALTH_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_DRACULA_HEALTH_UPGRADE;
	public static float				ENEMY_DRACULA_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DRACULA_ARMOR_UPGRADE;
	public static float				ENEMY_DRACULA_DAMAGE_UPGRADE		= ApoDefenceConstantsOriginal.ENEMY_DRACULA_DAMAGE_UPGRADE;
	public static float				ENEMY_DRACULA_MONEY					= ApoDefenceConstantsOriginal.ENEMY_DRACULA_MONEY;
	
	public static int				ENEMY_FIRE							= ApoDefenceConstantsOriginal.ENEMY_FIRE;
	public static String			ENEMY_FIRE_NAME						= ApoDefenceConstantsOriginal.ENEMY_FIRE_NAME;
	public static float				ENEMY_FIRE_SPEED					= ApoDefenceConstantsOriginal.ENEMY_FIRE_SPEED;
	public static float				ENEMY_FIRE_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_FIRE_HEALTH;
	public static float				ENEMY_FIRE_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_FIRE_ARMOR;
	public static ApoDefencePoint	ENEMY_FIRE_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_FIRE_DAMAGE;
	public static float				ENEMY_FIRE_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_FIRE_SPEED_UPGRADE;
	public static float				ENEMY_FIRE_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_FIRE_HEALTH_UPGRADE;
	public static float				ENEMY_FIRE_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_FIRE_ARMOR_UPGRADE;
	public static float				ENEMY_FIRE_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_FIRE_DAMAGE_UPGRADE;
	public static float				ENEMY_FIRE_MONEY					= ApoDefenceConstantsOriginal.ENEMY_FIRE_MONEY;
	
	public static int				ENEMY_DRAGON						= ApoDefenceConstantsOriginal.ENEMY_DRAGON;
	public static String			ENEMY_DRAGON_NAME					= ApoDefenceConstantsOriginal.ENEMY_DRAGON_NAME;
	public static float				ENEMY_DRAGON_SPEED					= ApoDefenceConstantsOriginal.ENEMY_DRAGON_SPEED;
	public static float				ENEMY_DRAGON_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_DRAGON_HEALTH;
	public static float				ENEMY_DRAGON_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_DRAGON_ARMOR;
	public static ApoDefencePoint	ENEMY_DRAGON_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_DRAGON_DAMAGE;
	public static float				ENEMY_DRAGON_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_SPEED_UPGRADE;
	public static float				ENEMY_DRAGON_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_HEALTH_UPGRADE;
	public static float				ENEMY_DRAGON_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_ARMOR_UPGRADE;
	public static float				ENEMY_DRAGON_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_DRAGON_DAMAGE_UPGRADE;
	public static float				ENEMY_DRAGON_MONEY					= ApoDefenceConstantsOriginal.ENEMY_DRAGON_MONEY;
	
	public static int				ENEMY_GOBLIN						= ApoDefenceConstantsOriginal.ENEMY_GOBLIN;
	public static String			ENEMY_GOBLIN_NAME					= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_NAME;
	public static float				ENEMY_GOBLIN_SPEED					= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_SPEED;
	public static float				ENEMY_GOBLIN_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_HEALTH;
	public static float				ENEMY_GOBLIN_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_ARMOR;
	public static ApoDefencePoint	ENEMY_GOBLIN_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_DAMAGE;
	public static float				ENEMY_GOBLIN_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_SPEED_UPGRADE;
	public static float				ENEMY_GOBLIN_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_HEALTH_UPGRADE;
	public static float				ENEMY_GOBLIN_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_ARMOR_UPGRADE;
	public static float				ENEMY_GOBLIN_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_DAMAGE_UPGRADE;
	public static float				ENEMY_GOBLIN_MONEY					= ApoDefenceConstantsOriginal.ENEMY_GOBLIN_MONEY;
	
	public static int				ENEMY_KNIGHT						= ApoDefenceConstantsOriginal.ENEMY_KNIGHT;
	public static String			ENEMY_KNIGHT_NAME					= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_NAME;
	public static float				ENEMY_KNIGHT_SPEED					= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_SPEED;
	public static float				ENEMY_KNIGHT_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_HEALTH;
	public static float				ENEMY_KNIGHT_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_ARMOR;
	public static ApoDefencePoint	ENEMY_KNIGHT_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_DAMAGE;
	public static float				ENEMY_KNIGHT_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_SPEED_UPGRADE;
	public static float				ENEMY_KNIGHT_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_HEALTH_UPGRADE;
	public static float				ENEMY_KNIGHT_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_ARMOR_UPGRADE;
	public static float				ENEMY_KNIGHT_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_DAMAGE_UPGRADE;
	public static float				ENEMY_KNIGHT_MONEY					= ApoDefenceConstantsOriginal.ENEMY_KNIGHT_MONEY;
	
	public static int				ENEMY_MONK						= ApoDefenceConstantsOriginal.ENEMY_MONK;
	public static String			ENEMY_MONK_NAME					= ApoDefenceConstantsOriginal.ENEMY_MONK_NAME;
	public static float				ENEMY_MONK_SPEED					= ApoDefenceConstantsOriginal.ENEMY_MONK_SPEED;
	public static float				ENEMY_MONK_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_MONK_HEALTH;
	public static float				ENEMY_MONK_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_MONK_ARMOR;
	public static ApoDefencePoint	ENEMY_MONK_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_MONK_DAMAGE;
	public static float				ENEMY_MONK_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MONK_SPEED_UPGRADE;
	public static float				ENEMY_MONK_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MONK_HEALTH_UPGRADE;
	public static float				ENEMY_MONK_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MONK_ARMOR_UPGRADE;
	public static float				ENEMY_MONK_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MONK_DAMAGE_UPGRADE;
	public static float				ENEMY_MONK_MONEY					= ApoDefenceConstantsOriginal.ENEMY_MONK_MONEY;
	
	public static int				ENEMY_MUMMY						= ApoDefenceConstantsOriginal.ENEMY_MUMMY;
	public static String			ENEMY_MUMMY_NAME					= ApoDefenceConstantsOriginal.ENEMY_MUMMY_NAME;
	public static float				ENEMY_MUMMY_SPEED					= ApoDefenceConstantsOriginal.ENEMY_MUMMY_SPEED;
	public static float				ENEMY_MUMMY_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_MUMMY_HEALTH;
	public static float				ENEMY_MUMMY_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_MUMMY_ARMOR;
	public static ApoDefencePoint	ENEMY_MUMMY_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_MUMMY_DAMAGE;
	public static float				ENEMY_MUMMY_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_SPEED_UPGRADE;
	public static float				ENEMY_MUMMY_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_HEALTH_UPGRADE;
	public static float				ENEMY_MUMMY_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_ARMOR_UPGRADE;
	public static float				ENEMY_MUMMY_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_MUMMY_DAMAGE_UPGRADE;
	public static float				ENEMY_MUMMY_MONEY					= ApoDefenceConstantsOriginal.ENEMY_MUMMY_MONEY;
	
	public static int				ENEMY_SKELETON						= ApoDefenceConstantsOriginal.ENEMY_SKELETON;
	public static String			ENEMY_SKELETON_NAME					= ApoDefenceConstantsOriginal.ENEMY_SKELETON_NAME;
	public static float				ENEMY_SKELETON_SPEED					= ApoDefenceConstantsOriginal.ENEMY_SKELETON_SPEED;
	public static float				ENEMY_SKELETON_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_SKELETON_HEALTH;
	public static float				ENEMY_SKELETON_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_SKELETON_ARMOR;
	public static ApoDefencePoint	ENEMY_SKELETON_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_SKELETON_DAMAGE;
	public static float				ENEMY_SKELETON_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_SPEED_UPGRADE;
	public static float				ENEMY_SKELETON_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_HEALTH_UPGRADE;
	public static float				ENEMY_SKELETON_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_ARMOR_UPGRADE;
	public static float				ENEMY_SKELETON_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKELETON_DAMAGE_UPGRADE;
	public static float				ENEMY_SKELETON_MONEY					= ApoDefenceConstantsOriginal.ENEMY_SKELETON_MONEY;
	
	public static int				ENEMY_SKULL						= ApoDefenceConstantsOriginal.ENEMY_SKULL;
	public static String			ENEMY_SKULL_NAME					= ApoDefenceConstantsOriginal.ENEMY_SKULL_NAME;
	public static float				ENEMY_SKULL_SPEED					= ApoDefenceConstantsOriginal.ENEMY_SKULL_SPEED;
	public static float				ENEMY_SKULL_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_SKULL_HEALTH;
	public static float				ENEMY_SKULL_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_SKULL_ARMOR;
	public static ApoDefencePoint	ENEMY_SKULL_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_SKULL_DAMAGE;
	public static float				ENEMY_SKULL_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKULL_SPEED_UPGRADE;
	public static float				ENEMY_SKULL_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKULL_HEALTH_UPGRADE;
	public static float				ENEMY_SKULL_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKULL_ARMOR_UPGRADE;
	public static float				ENEMY_SKULL_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SKULL_DAMAGE_UPGRADE;
	public static float				ENEMY_SKULL_MONEY					= ApoDefenceConstantsOriginal.ENEMY_SKULL_MONEY;
	
	public static int				ENEMY_SNAKE						= ApoDefenceConstantsOriginal.ENEMY_SNAKE;
	public static String			ENEMY_SNAKE_NAME					= ApoDefenceConstantsOriginal.ENEMY_SNAKE_NAME;
	public static float				ENEMY_SNAKE_SPEED					= ApoDefenceConstantsOriginal.ENEMY_SNAKE_SPEED;
	public static float				ENEMY_SNAKE_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_SNAKE_HEALTH;
	public static float				ENEMY_SNAKE_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_SNAKE_ARMOR;
	public static ApoDefencePoint	ENEMY_SNAKE_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_SNAKE_DAMAGE;
	public static float				ENEMY_SNAKE_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_SPEED_UPGRADE;
	public static float				ENEMY_SNAKE_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_HEALTH_UPGRADE;
	public static float				ENEMY_SNAKE_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_ARMOR_UPGRADE;
	public static float				ENEMY_SNAKE_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_SNAKE_DAMAGE_UPGRADE;
	public static float				ENEMY_SNAKE_MONEY					= ApoDefenceConstantsOriginal.ENEMY_SNAKE_MONEY;
	
	public static int				ENEMY_UNDEAD						= ApoDefenceConstantsOriginal.ENEMY_UNDEAD;
	public static String			ENEMY_UNDEAD_NAME					= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_NAME;
	public static float				ENEMY_UNDEAD_SPEED					= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_SPEED;
	public static float				ENEMY_UNDEAD_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_HEALTH;
	public static float				ENEMY_UNDEAD_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_ARMOR;
	public static ApoDefencePoint	ENEMY_UNDEAD_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_DAMAGE;
	public static float				ENEMY_UNDEAD_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_SPEED_UPGRADE;
	public static float				ENEMY_UNDEAD_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_HEALTH_UPGRADE;
	public static float				ENEMY_UNDEAD_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_ARMOR_UPGRADE;
	public static float				ENEMY_UNDEAD_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_DAMAGE_UPGRADE;
	public static float				ENEMY_UNDEAD_MONEY					= ApoDefenceConstantsOriginal.ENEMY_UNDEAD_MONEY;
	
	public static int				ENEMY_UNDEFINED						= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED;
	public static String			ENEMY_UNDEFINED_NAME					= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_NAME;
	public static float				ENEMY_UNDEFINED_SPEED					= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_SPEED;
	public static float				ENEMY_UNDEFINED_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_HEALTH;
	public static float				ENEMY_UNDEFINED_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_ARMOR;
	public static ApoDefencePoint	ENEMY_UNDEFINED_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_DAMAGE;
	public static float				ENEMY_UNDEFINED_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_SPEED_UPGRADE;
	public static float				ENEMY_UNDEFINED_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_HEALTH_UPGRADE;
	public static float				ENEMY_UNDEFINED_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_ARMOR_UPGRADE;
	public static float				ENEMY_UNDEFINED_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_DAMAGE_UPGRADE;
	public static float				ENEMY_UNDEFINED_MONEY					= ApoDefenceConstantsOriginal.ENEMY_UNDEFINED_MONEY;
	
	public static int				ENEMY_WEREWOLF						= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF;
	public static String			ENEMY_WEREWOLF_NAME					= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_NAME;
	public static float				ENEMY_WEREWOLF_SPEED					= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_SPEED;
	public static float				ENEMY_WEREWOLF_HEALTH					= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_HEALTH;
	public static float				ENEMY_WEREWOLF_ARMOR					= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_ARMOR;
	public static ApoDefencePoint	ENEMY_WEREWOLF_DAMAGE					= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_DAMAGE;
	public static float				ENEMY_WEREWOLF_SPEED_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_SPEED_UPGRADE;
	public static float				ENEMY_WEREWOLF_HEALTH_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_HEALTH_UPGRADE;
	public static float				ENEMY_WEREWOLF_ARMOR_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_ARMOR_UPGRADE;
	public static float				ENEMY_WEREWOLF_DAMAGE_UPGRADE			= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_DAMAGE_UPGRADE;
	public static float				ENEMY_WEREWOLF_MONEY					= ApoDefenceConstantsOriginal.ENEMY_WEREWOLF_MONEY;
}
