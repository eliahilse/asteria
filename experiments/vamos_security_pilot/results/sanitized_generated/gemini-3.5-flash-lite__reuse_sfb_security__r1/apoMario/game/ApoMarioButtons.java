package apoMario.game;

import apoMario.game.ApoMarioButtons;
import apoMario.game.ApoMarioPanel;

import java.awt.Color;
import java.awt.Font;

import org.apogames.entity.ApoButtonText;

import apoMario.ApoMarioConstants;
import apoMario.game.panels.ApoMarioCredits;
import apoMario.game.panels.ApoMarioHighscoreView;
import apoMario.game.panels.ApoMarioMenu;
import apoMario.game.panels.ApoMarioOptions;

public class ApoMarioButtons {

	private ApoMarioPanel game;

	public ApoMarioButtons(ApoMarioPanel game) {
		this.game = game;
	}

	public void makeButtons() {
		ApoButtonText[] buttons = new ApoButtonText[40];
		int width = 140 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int height = 24 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int x = ApoMarioConstants.GAME_WIDTH / 2 - width / 2;
		int y = ApoMarioConstants.GAME_HEIGHT / 4 + 10 * ApoMarioConstants.APP_SIZE;
		
		buttons[0] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Start Game", 10), x, y, width, height, ApoMarioMenu.FUNCTION_START);
		buttons[1] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Options", 10), x, y + height + 5, width, height, ApoMarioMenu.FUNCTION_OPTIONS);
		buttons[2] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Highscore", 10), x, y + (height + 5) * 2, width, height, "menuHighscore");
		buttons[3] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Credits", 10), x, y + (height + 5) * 3, width, height, ApoMarioMenu.FUNCTION_CREDITS);
		buttons[4] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Quit", 10), x, y + (height + 5) * 4, width, height, ApoMarioMenu.FUNCTION_QUIT);

		// Back button for Highscore view
		buttons[5] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - 40, width, height, ApoMarioHighscoreView.FUNCTION_BACK);

		// Options back
		buttons[6] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - 40, width, height, ApoMarioOptions.FUNCTION_OPTIONS_BACK);

		// Credits back
		buttons[7] = new ApoButtonText(this.game.getImages().getButtonImage(width, height, "Back", 10), ApoMarioConstants.GAME_WIDTH / 2 - width / 2, ApoMarioConstants.GAME_HEIGHT - 40, width, height, ApoMarioCredits.FUNCTION_CREDITS_BACK);

		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] != null) {
				buttons[i].setFontSize(12 * ApoMarioConstants.APP_SIZE);
			}
		}

		this.game.setButtons(buttons);
	}
}