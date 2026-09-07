package apoMario.game;

import apoMario.game.ApoMarioButtons;
import apoMario.game.ApoMarioPanel;
import java.awt.*;
import org.apogames.entity.ApoButton;

import org.apogames.entity.ApoButtonText;

import apoMario.ApoMarioConstants;
import apoMario.game.panels.ApoMarioAnalysis;
import apoMario.game.panels.ApoMarioCredits;
import apoMario.game.panels.ApoMarioEditor;
import apoMario.game.panels.ApoMarioGame;
import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioMenu;
import apoMario.game.panels.ApoMarioOptions;
import apoMario.game.panels.ApoMarioSimulation;

public class ApoMarioButtons {

	private final ApoMarioPanel game;
	
	public ApoMarioButtons(ApoMarioPanel game) {
		this.game = game;
	}
	
	public void makeButtons() {
		int startX = 20 * ApoMarioConstants.SIZE;
		int startY = 20 * ApoMarioConstants.SIZE;
		int width = 110 * ApoMarioConstants.SIZE;
		int height = 25 * ApoMarioConstants.SIZE;
		int distance = 30 * ApoMarioConstants.SIZE;
		
		int maxButtons = 50;
		this.game.setButtons(new org.apogames.entity.ApoButton[maxButtons]);
		
		// 0: Start menu
		this.game.getButtons()[0] = new ApoButtonText(startX, startY, width, height, ApoMarioMenu.FUNCTION_START);
		((ApoButtonText)this.game.getButtons()[0]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[0]).setText("Start");
		((ApoButtonText)this.game.getButtons()[0]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[0]).setColorReleased(new Color(254, 254, 254));
		
		// 1: Options menu
		this.game.getButtons()[1] = new ApoButtonText(startX, startY + distance, width, height, ApoMarioMenu.FUNCTION_OPTIONS);
		((ApoButtonText)this.game.getButtons()[1]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[1]).setText("Options");
		((ApoButtonText)this.game.getButtons()[1]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[1]).setColorReleased(new Color(254, 254, 254));

		// 2: Credits menu
		this.game.getButtons()[2] = new ApoButtonText(startX, startY + distance * 2, width, height, ApoMarioMenu.FUNCTION_CREDITS);
		((ApoButtonText)this.game.getButtons()[2]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[2]).setText("Credits");
		((ApoButtonText)this.game.getButtons()[2]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[2]).setColorReleased(new Color(254, 254, 254));

		// 3: Highscore menu button
		this.game.getButtons()[3] = new ApoButtonText(startX, startY + distance * 3, width, height, ApoMarioMenu.FUNCTION_HIGHSCORE);
		((ApoButtonText)this.game.getButtons()[3]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[3]).setText("Highscore");
		((ApoButtonText)this.game.getButtons()[3]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[3]).setColorReleased(new Color(254, 254, 254));

		// 4: Quit menu
		this.game.getButtons()[4] = new ApoButtonText(startX, startY + distance * 4, width, height, ApoMarioMenu.FUNCTION_QUIT);
		((ApoButtonText)this.game.getButtons()[4]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[4]).setText("Quit");
		((ApoButtonText)this.game.getButtons()[4]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[4]).setColorReleased(new Color(254, 254, 254));

		// 5: Load player 2
		this.game.getButtons()[5] = new ApoButtonText(startX + width + 20, startY, width, height, ApoMarioMenu.FUNCTION_LOAD_PLAYER_TWO);
		((ApoButtonText)this.game.getButtons()[5]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[5]).setText("Load P2 AI");
		((ApoButtonText)this.game.getButtons()[5]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[5]).setColorReleased(new Color(254, 254, 254));

		// 6: Simulation
		this.game.getButtons()[6] = new ApoButtonText(startX, startY + distance * 5, width, height, ApoMarioMenu.FUNCTION_SIMULATE);
		((ApoButtonText)this.game.getButtons()[6]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[6]).setText("Simulation");
		((ApoButtonText)this.game.getButtons()[6]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[6]).setColorReleased(new Color(254, 254, 254));

		// 7: Replay Load
		this.game.getButtons()[7] = new ApoButtonText(startX, startY + distance * 6, width, height, ApoMarioMenu.FUNCTION_REPLAYLOAD);
		((ApoButtonText)this.game.getButtons()[7]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[7]).setText("Load Replay");
		((ApoButtonText)this.game.getButtons()[7]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[7]).setColorReleased(new Color(254, 254, 254));

		// 8: Editor
		this.game.getButtons()[8] = new ApoButtonText(startX, startY + distance * 7, width, height, ApoMarioMenu.fUNCTION_EDITOR);
		((ApoButtonText)this.game.getButtons()[8]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[8]).setText("Editor");
		((ApoButtonText)this.game.getButtons()[8]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[8]).setColorReleased(new Color(254, 254, 254));

		// 9: Credits Back
		this.game.getButtons()[9] = new ApoButtonText(startX, startY, width, height, ApoMarioCredits.FUNCTION_CREDITS_BACK);
		((ApoButtonText)this.game.getButtons()[9]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[9]).setText("Back");
		((ApoButtonText)this.game.getButtons()[9]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[9]).setColorReleased(new Color(254, 254, 254));

		// 10: Options Back
		this.game.getButtons()[10] = new ApoButtonText(startX, startY, width, height, ApoMarioOptions.FUNCTION_OPTIONS_BACK);
		((ApoButtonText)this.game.getButtons()[10]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[10]).setText("Back");
		((ApoButtonText)this.game.getButtons()[10]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[10]).setColorReleased(new Color(254, 254, 254));

		// 11: Highscore Back
		this.game.getButtons()[11] = new ApoButtonText(startX, startY, width, height, ApoMarioHighscore.FUNCTION_HIGHSCORE_BACK);
		((ApoButtonText)this.game.getButtons()[11]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[11]).setText("Back");
		((ApoButtonText)this.game.getButtons()[11]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[11]).setColorReleased(new Color(254, 254, 254));

		// 12: Analysis Back
		this.game.getButtons()[12] = new ApoButtonText(startX, startY, width, height, ApoMarioAnalysis.FUNCTION_ANALYSIS_BACK);
		((ApoButtonText)this.game.getButtons()[12]).setFontSize(10 + 4 * ApoMarioConstants.APP_SIZE);
		((ApoButtonText)this.game.getButtons()[12]).setText("Back");
		((ApoButtonText)this.game.getButtons()[12]).setColorPressed(new Color(200, 200, 200));
		((ApoButtonText)this.game.getButtons()[12]).setColorReleased(new Color(254, 254, 254));

		// Fill remaining button slots with dummy buttons to avoid null pointers
		for (int i = 13; i < maxButtons; i++) {
			this.game.getButtons()[i] = new ApoButtonText(0, 0, 1, 1, "dummy" + i);
			this.game.getButtons()[i].setBVisible(false);
		}
	}
}