package apoMario.game;

import apoMario.game.ApoMarioButtons;
import apoMario.game.ApoMarioPanel;
import org.apogames.entity.ApoButton;

import apoMario.ApoMarioConstants;
import apoMario.game.panels.ApoMarioAnalysis;
import apoMario.game.panels.ApoMarioCredits;
import apoMario.game.panels.ApoMarioEditor;
import apoMario.game.panels.ApoMarioGame;
import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioMenu;
import apoMario.game.panels.ApoMarioOptions;
import apoMario.game.panels.ApoMarioSimulation;
import org.apogames.entity.ApoButtonText;

/**
 * Hilfsklasse, um die ganzen Buttons (fr alle Panels) zu erstellen
 * @author Dirk Aporius
 *
 */
public class ApoMarioButtons {

	private ApoMarioPanel game;
	
	public ApoMarioButtons(ApoMarioPanel game) {
		this.game = game;
	}
	
	public void makeButtons() {
		org.apogames.entity.ApoButton[] buttons = new org.apogames.entity.ApoButton[50];
		
		int w = 140 * ApoMarioConstants.APP_SIZE;
		int h = 30 * ApoMarioConstants.APP_SIZE;
		int x = ApoMarioConstants.GAME_WIDTH/2 - w/2;
		int y = ApoMarioConstants.GAME_HEIGHT * 1 / 4 - h;
		
		buttons[0] = new ApoButtonText(x, y, w, h, ApoMarioMenu.FUNCTION_START, ApoMarioMenu.FUNCTION_START, ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[1] = new ApoButtonText(x, y + h + 10, w, h, ApoMarioMenu.FUNCTION_OPTIONS, ApoMarioMenu.FUNCTION_OPTIONS, ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[2] = new ApoButtonText(x, y + (h + 10) * 2, w, h, ApoMarioMenu.FUNCTION_SIMULATE, ApoMarioMenu.FUNCTION_SIMULATE, ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[3] = new ApoButtonText(x, y + (h + 10) * 3, w, h, ApoMarioMenu.fUNCTION_EDITOR, ApoMarioMenu.fUNCTION_EDITOR, ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[4] = new ApoButtonText(x, y + (h + 10) * 4, w, h, ApoMarioMenu.FUNCTION_CREDITS, ApoMarioMenu.FUNCTION_CREDITS, ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[19] = new ApoButtonText(x, y + (h + 10) * 5, w, h, ApoMarioMenu.FUNCTION_QUIT, ApoMarioMenu.FUNCTION_QUIT, ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[20] = new ApoButtonText(x, y + (h + 10) * 6, w, h, ApoMarioMenu.FUNCTION_REPLAYLOAD, "Load Replay", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[37] = new ApoButtonText(x, y + (h + 10) * 7, w, h, ApoMarioMenu.FUNCTION_EDITORLOAD, "Load Editor", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[42] = new ApoButtonText(x, y + (h + 10) * 8, w, h, ApoMarioMenu.FUNCTION_HIGHSCORE, "Highscore", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		
		int wSmall = 30 * ApoMarioConstants.APP_SIZE;
		int hSmall = 25 * ApoMarioConstants.APP_SIZE;
		int xLeft = ApoMarioConstants.GAME_WIDTH/8 - wSmall/2;
		int xRight = ApoMarioConstants.GAME_WIDTH*7/8 - wSmall/2;
		int yPlayer = (int)(y + (h + 10) * 1.5f);
		
		buttons[13] = new ApoButtonText(xLeft - wSmall - 5, yPlayer, wSmall, hSmall, ApoMarioMenu.FUNCTION_PLAYER_ONE_LEFT, "<", ApoMarioConstants.FONT_ARROW, ApoMarioConstants.SIZE);
		buttons[14] = new ApoButtonText(xLeft + wSmall + 5, yPlayer, wSmall, hSmall, ApoMarioMenu.FUNCTION_PLAYER_ONE_RIGHT, ">", ApoMarioConstants.FONT_ARROW, ApoMarioConstants.SIZE);
		buttons[22] = new ApoButtonText(xLeft - wSmall/2, yPlayer + hSmall + 5, wSmall * 2, hSmall, ApoMarioMenu.FUNCTION_LOAD_PLAYER_ONE, "Load AI", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		
		buttons[15] = new ApoButtonText(xRight - wSmall - 5, yPlayer, wSmall, hSmall, ApoMarioMenu.FUNCTION_PLAYER_TWO_LEFT, "<", ApoMarioConstants.FONT_ARROW, ApoMarioConstants.SIZE);
		buttons[16] = new ApoButtonText(xRight + wSmall + 5, yPlayer, wSmall, hSmall, ApoMarioMenu.FUNCTION_PLAYER_TWO_RIGHT, ">", ApoMarioConstants.FONT_ARROW, ApoMarioConstants.SIZE);
		buttons[23] = new ApoButtonText(xRight - wSmall/2, yPlayer + hSmall + 5, wSmall * 2, hSmall, ApoMarioMenu.FUNCTION_LOAD_PLAYER_TWO, "Load AI", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);

		int wOptions = 100 * ApoMarioConstants.APP_SIZE;
		int hOptions = 30 * ApoMarioConstants.APP_SIZE;
		buttons[5] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioOptions.FUNCTION_OPTIONS_BACK, "Back", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[6] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions - 10, ApoMarioConstants.GAME_HEIGHT - hOptions * 2 - 30, wOptions, hOptions, ApoMarioOptions.FUNCTION_OPTIONS_LEFT_DIFFICULTY, "<", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[7] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 + 10, ApoMarioConstants.GAME_HEIGHT - hOptions * 2 - 30, wOptions, hOptions, ApoMarioOptions.FUNCTION_OPTIONS_RIGHT_DIFFICULTY, ">", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);

		buttons[8] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioCredits.FUNCTION_CREDITS_BACK, "Back", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);

		buttons[9] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2 - wOptions - 10, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioAnalysis.FUNCTION_ANALYSIS_BACK, "Menu", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[10] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioAnalysis.FUNCTION_ANALYSIS_RESTART, "Restart", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[11] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 + wOptions/2 + 10, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioAnalysis.FUNCTION_ANALYSIS_NEWLEVEL, "New Level", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[21] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2 - wOptions - 10, ApoMarioConstants.GAME_HEIGHT - hOptions * 2 - 30, wOptions, hOptions, ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYSAVE, "Save Replay", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);

		buttons[12] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioEditor.FUNCTION_MENU, "Menu", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[17] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions - 10, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioEditor.FUNCTION_LOAD, "Load", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[18] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 + 10, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioEditor.FUNCTION_SAVE, "Save", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);

		buttons[24] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioSimulation.FUNCTION_SIMULATION_BACK, "Back", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[25] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2, 50, wOptions, hOptions, ApoMarioSimulation.FUNCTION_SIMULATION_SIMULATE, "Simulate", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);

		buttons[43] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH/2 - wOptions/2, ApoMarioConstants.GAME_HEIGHT - hOptions - 20, wOptions, hOptions, ApoMarioHighscore.FUNCTION_BACK, "Back", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[44] = new ApoButtonText(40, ApoMarioConstants.GAME_HEIGHT/2 - 15, 40, 30, ApoMarioHighscore.LEFT, "<", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);
		buttons[45] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH - 80, ApoMarioConstants.GAME_HEIGHT/2 - 15, 40, 30, ApoMarioHighscore.RIGHT, ">", ApoMarioConstants.FONT_BUTTON, ApoMarioConstants.SIZE);

		this.game.setButtons(buttons);
	}
}