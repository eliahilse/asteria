package apoMario.game;

import apoMario.game.ApoMarioButtons;
import apoMario.game.ApoMarioPanel;
import apoMario.game.panels.ApoMarioAnalysis;
import apoMario.game.panels.ApoMarioCredits;
import apoMario.game.panels.ApoMarioOptions;
import apoMario.game.panels.ApoMarioSimulation;
import org.apogames.entity.ApoButton;

import java.awt.Font;
import apoMario.ApoMarioConstants;
import apoMario.game.panels.ApoMarioHighscorePanel;
import apoMario.game.panels.ApoMarioMenu;
import org.apogames.entity.ApoButtonText;

public class ApoMarioButtons {

	private ApoMarioPanel game;

	public ApoMarioButtons(ApoMarioPanel game) {
		this.game = game;
	}

	public void makeButtons() {
		int width = 160 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int height = 30 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int startX = ApoMarioConstants.GAME_WIDTH / 2 - width / 2;
		int startY = ApoMarioConstants.GAME_HEIGHT / 2 - 70 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int distance = 35 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;

		org.apogames.entity.ApoButton[] buttons = new org.apogames.entity.ApoButton[50];

		Font font = ApoMarioConstants.FONT_BUTTON;

		buttons[0] = new ApoButtonText(startX, startY, width, height, "start", ApoMarioMenu.FUNCTION_START);
		buttons[19] = new ApoButtonText(startX, startY + distance, width, height, "options", ApoMarioMenu.FUNCTION_OPTIONS);
		buttons[37] = new ApoButtonText(startX, startY + distance * 2, width, height, "highscore", ApoMarioMenu.FUNCTION_SIMULATE.equals("simulate") ? ApoMarioHighscorePanel.FUNCTION_HIGHSCORE_BACK : ApoMarioHighscorePanel.FUNCTION_HIGHSCORE_BACK);
		buttons[18] = new ApoButtonText(startX, startY + distance * 3, width, height, "simulate", ApoMarioMenu.FUNCTION_SIMULATE);
		buttons[9] = new ApoButtonText(startX, startY + distance * 4, width, height, "credits", ApoMarioMenu.FUNCTION_CREDITS);
		buttons[11] = new ApoButtonText(startX, startY + distance * 5, width, height, "quit", ApoMarioMenu.FUNCTION_QUIT);

		buttons[13] = new ApoButtonText(startX - 50, startY, 40, height, "<", ApoMarioMenu.FUNCTION_PLAYER_ONE_LEFT);
		buttons[14] = new ApoButtonText(startX + width + 10, startY, 40, height, ">", ApoMarioMenu.FUNCTION_PLAYER_ONE_RIGHT);
		buttons[22] = new ApoButtonText(startX - 110, startY, 55, height, "load", ApoMarioMenu.FUNCTION_LOAD_PLAYER_ONE);

		buttons[15] = new ApoButtonText(startX - 50, startY + distance, 40, height, "<", ApoMarioMenu.FUNCTION_PLAYER_TWO_LEFT);
		buttons[16] = new ApoButtonText(startX + width + 10, startY + distance, 40, height, ">", ApoMarioMenu.FUNCTION_PLAYER_TWO_RIGHT);
		buttons[5] = new ApoButtonText(startX - 110, startY + distance, 55, height, "load", ApoMarioMenu.FUNCTION_LOAD_PLAYER_TWO);

		buttons[1] = new ApoButtonText(10, 10, 60, height / 2, "back", apoMario.game.panels.ApoMarioOptions.FUNCTION_OPTIONS_BACK);
		buttons[2] = new ApoButtonText(10, 10, 60, height / 2, "back", apoMario.game.panels.ApoMarioCredits.FUNCTION_CREDITS_BACK);
		buttons[3] = new ApoButtonText(10, 10, 60, height / 2, "back", ApoMarioHighscorePanel.FUNCTION_HIGHSCORE_BACK);
		buttons[4] = new ApoButtonText(10, 10, 60, height / 2, "back", apoMario.game.panels.ApoMarioAnalysis.FUNCTION_ANALYSIS_BACK);
		buttons[20] = new ApoButtonText(10, 10, 60, height / 2, "back", apoMario.game.panels.ApoMarioSimulation.FUNCTION_SIMULATION_BACK);

		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == null) {
				buttons[i] = new ApoButtonText(-100, -100, 10, 10, "", "");
				buttons[i].setBVisible(false);
			} else {
				if (buttons[i] instanceof ApoButtonText) {
					((ApoButtonText) buttons[i]).setFontSize(12);
				}
			}
		}

		this.game.setButtons(buttons);
	}
}