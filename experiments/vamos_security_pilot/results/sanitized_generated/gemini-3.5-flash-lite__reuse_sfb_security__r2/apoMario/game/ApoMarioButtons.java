package apoMario.game;

import apoMario.game.ApoMarioButtons;
import apoMario.game.ApoMarioPanel;
import apoMario.game.panels.ApoMarioAnalysis;
import apoMario.game.panels.ApoMarioCredits;
import apoMario.game.panels.ApoMarioEditor;
import apoMario.game.panels.ApoMarioOptions;
import apoMario.game.panels.ApoMarioSimulation;
import org.apogames.entity.ApoButton;

import java.awt.Color;
import java.awt.Font;

import org.apogames.entity.ApoButtonText;
import apoMario.ApoMarioConstants;
import apoMario.game.panels.ApoMarioHighscorePanel;
import apoMario.game.panels.ApoMarioMenu;

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
		org.apogames.entity.ApoButton[] buttons = new org.apogames.entity.ApoButton[40];
		int x = 10 * ApoMarioConstants.APP_SIZE;
		int y = 10 * ApoMarioConstants.APP_SIZE;
		int width = 100 * ApoMarioConstants.APP_SIZE;
		int height = 25 * ApoMarioConstants.APP_SIZE;
		
		int startX = ApoMarioConstants.GAME_WIDTH / 2 - width / 2;
		int startY = ApoMarioConstants.GAME_HEIGHT * 1 / 4;
		int diffY = 35 * ApoMarioConstants.APP_SIZE;
		
		buttons[0] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Start", 10), startX, startY, width, height, ApoMarioMenu.FUNCTION_START);
		buttons[1] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Options", 10), startX, startY + diffY, width, height, ApoMarioMenu.FUNCTION_OPTIONS);
		buttons[2] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Simulation", 10), startX, startY + diffY * 2, width, height, ApoMarioMenu.FUNCTION_SIMULATE);
		buttons[3] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Highscore", 10), startX, startY + diffY * 3, width, height, "highscoreMenu");
		buttons[4] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Credits", 10), startX, startY + diffY * 4, width, height, ApoMarioMenu.FUNCTION_CREDITS);
		buttons[5] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Quit", 10), startX, startY + diffY * 5, width, height, ApoMarioMenu.FUNCTION_QUIT);
		
		buttons[6] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - height - 15, width, height, apoMario.game.panels.ApoMarioCredits.FUNCTION_CREDITS_BACK);
		
		buttons[7] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - height - 15, width, height, apoMario.game.panels.ApoMarioOptions.FUNCTION_OPTIONS_BACK);
		
		buttons[8] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - height - 15, width, height, apoMario.game.panels.ApoMarioAnalysis.FUNCTION_ANALYSIS_BACK);
		buttons[9] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Restart", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2 - width - 10, ApoMarioConstants.GAME_HEIGHT - height - 15, width, height, apoMario.game.panels.ApoMarioAnalysis.FUNCTION_ANALYSIS_RESTART);
		buttons[10] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "New Level", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2 + width + 10, ApoMarioConstants.GAME_HEIGHT - height - 15, width, height, apoMario.game.panels.ApoMarioAnalysis.FUNCTION_ANALYSIS_NEWLEVEL);
		
		int wPlayer = 30 * ApoMarioConstants.APP_SIZE;
		int hPlayer = 20 * ApoMarioConstants.APP_SIZE;
		buttons[11] = new ApoButtonText(this.game.getImages().getButtonImage(wPlayer, hPlayer, "<", 5), ApoMarioConstants.GAME_WIDTH/8 - wPlayer, (int)(ApoMarioConstants.GAME_HEIGHT*1/4 - 2.5 * hPlayer), wPlayer, hPlayer, ApoMarioMenu.FUNCTION_PLAYER_ONE_LEFT);
		buttons[12] = new ApoButtonText(this.game.getImages().getButtonImage(wPlayer, hPlayer, ">", 5), ApoMarioConstants.GAME_WIDTH/8 + ApoMarioConstants.GAME_WIDTH * 1 / 4, (int)(ApoMarioConstants.GAME_HEIGHT*1/4 - 2.5 * hPlayer), wPlayer, hPlayer, ApoMarioMenu.FUNCTION_PLAYER_ONE_RIGHT);
		buttons[13] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 1.5), hPlayer, "Load", 5), ApoMarioConstants.GAME_WIDTH/8 + ApoMarioConstants.GAME_WIDTH * 1 / 8 - (int)(width * 0.75), (int)(ApoMarioConstants.GAME_HEIGHT*1/4 + 0.5 * hPlayer), (int)(width * 1.5), hPlayer, ApoMarioMenu.FUNCTION_LOAD_PLAYER_ONE);

		buttons[14] = new ApoButtonText(this.game.getImages().getButtonImage(wPlayer, hPlayer, "<", 5), ApoMarioConstants.GAME_WIDTH*5/8 - wPlayer, (int)(ApoMarioConstants.GAME_HEIGHT*1/4 - 2.5 * hPlayer), wPlayer, hPlayer, ApoMarioMenu.FUNCTION_PLAYER_TWO_LEFT);
		buttons[15] = new ApoButtonText(this.game.getImages().getButtonImage(wPlayer, hPlayer, ">", 5), ApoMarioConstants.GAME_WIDTH*5/8 + ApoMarioConstants.GAME_WIDTH * 1 / 4, (int)(ApoMarioConstants.GAME_HEIGHT*1/4 - 2.5 * hPlayer), wPlayer, hPlayer, ApoMarioMenu.FUNCTION_PLAYER_TWO_RIGHT);
		buttons[16] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 1.5), hPlayer, "Load", 5), ApoMarioConstants.GAME_WIDTH*5/8 + ApoMarioConstants.GAME_WIDTH * 1 / 8 - (int)(width * 0.75), (int)(ApoMarioConstants.GAME_HEIGHT*1/4 + 0.5 * hPlayer), (int)(width * 1.5), hPlayer, ApoMarioMenu.FUNCTION_LOAD_PLAYER_TWO);

		buttons[17] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - height - 15, width, height, apoMario.game.panels.ApoMarioSimulation.FUNCTION_SIMULATION_BACK);
		buttons[18] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Simulate", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - height * 2 - 25, width, height, apoMario.game.panels.ApoMarioSimulation.FUNCTION_SIMULATION_SIMULATE);
		
		buttons[19] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Save", 10), 10, ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYSAVE);
		buttons[20] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Load", 10), 20 + (int)(width * 0.8), ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYLOAD);
		buttons[21] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Play", 10), 30 + (int)(width * 1.6), ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYPLAY);
		
		buttons[22] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Editor", 10), ApoMarioConstants.GAME_WIDTH - (int)(width * 0.8) - 10, 10, (int)(width * 0.8), height, ApoMarioMenu.fUNCTION_EDITOR);
		buttons[23] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Load", 10), ApoMarioConstants.GAME_WIDTH - (int)(width * 1.6) - 20, 10, (int)(width * 0.8), height, ApoMarioMenu.FUNCTION_EDITORLOAD);

		buttons[24] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Back", 10), 10, ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioEditor.FUNCTION_MENU);
		buttons[25] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Random", 10), ApoMarioConstants.GAME_WIDTH - (int)(width * 0.8) - 10, ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioEditor.FUNCTION_RANDOM);
		buttons[26] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Test", 10), ApoMarioConstants.GAME_WIDTH - (int)(width * 1.6) - 20, ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioEditor.FUNCTION_TEST);
		buttons[27] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Load", 10), ApoMarioConstants.GAME_WIDTH - (int)(width * 2.4) - 30, ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioEditor.FUNCTION_LOAD);
		buttons[28] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.8), height, "Save", 10), ApoMarioConstants.GAME_WIDTH - (int)(width * 3.2) - 40, ApoMarioConstants.GAME_HEIGHT - height - 10, (int)(width * 0.8), height, apoMario.game.panels.ApoMarioEditor.FUNCTION_SAVE);

		buttons[29] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.6), (int)(height * 0.8), "<", 5), ApoMarioConstants.GAME_WIDTH - 80, 50, (int)(width * 0.6), (int)(height * 0.8), ApoMarioHighscorePanel.LEFT);
		buttons[30] = new ApoButtonText(this.game.getImages().getButtonImage((int)(width * 0.6), (int)(height * 0.8), ">", 5), ApoMarioConstants.GAME_WIDTH - 40, 50, (int)(width * 0.6), (int)(height * 0.8), ApoMarioHighscorePanel.RIGHT);
		buttons[31] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - height - 15, width, height, ApoMarioHighscorePanel.FUNCTION_BACK);

		this.game.setButtons(buttons);
	}
}