package apoMario.game;

import apoMario.game.ApoMarioButtons;
import apoMario.game.ApoMarioPanel;
import java.awt.*;

import org.apogames.entity.ApoButtonText;
import apoMario.ApoMarioConstants;
import apoMario.game.panels.ApoMarioAnalysis;
import apoMario.game.panels.ApoMarioCredits;
import apoMario.game.panels.ApoMarioEditor;
import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioMenu;
import apoMario.game.panels.ApoMarioOptions;
import apoMario.game.panels.ApoMarioSimulation;

public class ApoMarioButtons {

	private ApoMarioPanel game;

	public ApoMarioButtons(ApoMarioPanel game) {
		this.game = game;
	}

	public void makeButtons() {
		int w = 150 * ApoMarioConstants.APP_SIZE;
		int h = 30 * ApoMarioConstants.APP_SIZE;
		int x = ApoMarioConstants.GAME_WIDTH / 2 - w / 2;
		int startY = 50 * ApoMarioConstants.APP_SIZE;
		int diffY = 40 * ApoMarioConstants.APP_SIZE;

		ApoButtonText[] buttons = new ApoButtonText[38];
		
		// 0: Start
		buttons[0] = new ApoButtonText(x, startY, w, h, ApoMarioMenu.FUNCTION_START, "Start Game");
		buttons[0].setFont(ApoMarioConstants.FONT_BUTTON_START);
		buttons[0].setColorReleased(Color.BLACK);
		buttons[0].setColorPressed(Color.RED);
		buttons[0].setIBackground(this.game.getImages().getButtonImage(w, h, "Start Game", 10));

		// 1: Options
		buttons[1] = new ApoButtonText(x, startY + diffY, w, h, ApoMarioMenu.FUNCTION_OPTIONS, "Options");
		buttons[1].setFont(ApoMarioConstants.FONT_BUTTON);
		buttons[1].setColorReleased(Color.BLACK);
		buttons[1].setColorPressed(Color.RED);
		buttons[1].setIBackground(this.game.getImages().getButtonImage(w, h, "Options", 10));

		// 2: Credits
		buttons[2] = new ApoButtonText(x, startY + diffY * 2, w, h, ApoMarioMenu.FUNCTION_CREDITS, "Credits");
		buttons[2].setFont(ApoMarioConstants.FONT_BUTTON);
		buttons[2].setColorReleased(Color.BLACK);
		buttons[2].setColorPressed(Color.RED);
		buttons[2].setIBackground(this.game.getImages().getButtonImage(w, h, "Credits", 10));

		// 3: Simulation
		buttons[3] = new ApoButtonText(x, startY + diffY * 3, w, h, ApoMarioMenu.FUNCTION_SIMULATE, "Simulation");
		buttons[3].setFont(ApoMarioConstants.FONT_BUTTON);
		buttons[3].setColorReleased(Color.BLACK);
		buttons[3].setColorPressed(Color.RED);
		buttons[3].setIBackground(this.game.getImages().getButtonImage(w, h, "Simulation", 10));

		// 4: Quit
		buttons[4] = new ApoButtonText(x, startY + diffY * 4, w, h, ApoMarioMenu.FUNCTION_QUIT, "Quit");
		buttons[4].setFont(ApoMarioConstants.FONT_BUTTON);
		buttons[4].setColorReleased(Color.BLACK);
		buttons[4].setColorPressed(Color.RED);
		buttons[4].setIBackground(this.game.getImages().getButtonImage(w, h, "Quit", 10));

		// 5: Load Player 2
		buttons[5] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH * 3 / 4 - w / 2, startY + diffY * 3, w, h, ApoMarioMenu.FUNCTION_LOAD_PLAYER_TWO, "Load AI 2");
		buttons[5].setFont(ApoMarioConstants.FONT_BUTTON);
		buttons[5].setColorReleased(Color.BLACK);
		buttons[5].setColorPressed(Color.RED);
		buttons[5].setIBackground(this.game.getImages().getButtonImage(w, h, "Load AI 2", 10));

		// 6: Highscore
		buttons[6] = new ApoButtonText(x, startY + diffY * 3.5f, w, h, ApoMarioMenu.FUNCTION_HIGHSCORE, "Highscore");
		buttons[6].setFont(ApoMarioConstants.FONT_BUTTON);
		buttons[6].setColorReleased(Color.BLACK);
		buttons[6].setColorPressed(Color.RED);
		buttons[6].setIBackground(this.game.getImages().getButtonImage(w, h, "Highscore", 10));

		// 7-37: filler default buttons to satisfy any index checks
		for (int i = 7; i < buttons.length; i++) {
			buttons[i] = new ApoButtonText(0, 0, w, h, "dummy", "dummy");
			buttons[i].setBVisible(false);
		}

		// Highscore panel navigation buttons
		buttons[11] = new ApoButtonText(50, ApoMarioConstants.GAME_HEIGHT - 50, 60, 30, ApoMarioHighscore.LEFT, "<-");
		buttons[11].setIBackground(this.game.getImages().getButtonImage(60, 30, "<-", 5));
		buttons[12] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH - 110, ApoMarioConstants.GAME_HEIGHT - 50, 60, 30, ApoMarioHighscore.RIGHT, "->");
		buttons[12].setIBackground(this.game.getImages().getButtonImage(60, 30, "->", 5));
		buttons[13] = new ApoButtonText(ApoMarioConstants.GAME_WIDTH / 2 - 40, ApoMarioConstants.GAME_HEIGHT - 50, 80, 30, ApoMarioHighscore.FUNCTION_BACK, "Back");
		buttons[13].setIBackground(this.game.getImages().getButtonImage(80, 30, "Back", 5));

		this.game.setButtons(buttons);
	}
}