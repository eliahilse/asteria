package apoMario.game;

import apoMario.game.ApoMarioButtons;
import apoMario.game.ApoMarioPanel;
import apoMario.game.panels.ApoMarioGame;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.apogames.entity.ApoButtonText;

import apoMario.ApoMarioConstants;
import apoMario.ApoMarioImage;
import apoMario.game.panels.ApoMarioMenu;
import apoMario.game.panels.ApoMarioHighscore;
import apoMario.game.panels.ApoMarioCredits;
import apoMario.game.panels.ApoMarioSimulation;
import apoMario.game.panels.ApoMarioAnalysis;
import apoMario.game.panels.ApoMarioEditor;
import apoMario.game.panels.ApoMarioOptions;

/**
 * Klasse, die alle Buttons des Spiels erstellt
 * @author Dirk Aporius
 *
 */
public class ApoMarioButtons {

	private ApoMarioPanel game;
	
	public ApoMarioButtons(ApoMarioPanel game) {
		this.game = game;
	}
	
	public void makeButtons() {
		ApoMarioImage images = this.game.getImages();
		int width = ApoMarioConstants.GAME_WIDTH;
		int height = ApoMarioConstants.GAME_HEIGHT;
		
		int size = 30 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int sizeX = 140 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int x = width/2 - sizeX / 2;
		int startY = height/2 - (int)(size * 3.5);
		int distance = 8 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		
		BufferedImage iNormal = images.getButtonImage(sizeX, size, "Start", ApoMarioConstants.FONT_BUTTON, 10);
		BufferedImage iOver = images.getImageMouseOver(iNormal);
		BufferedImage iPressed = images.getImageMouseOver(iNormal, 100);
		
		this.game.getButtons()[0] = new ApoButtonText(iNormal, iOver, iPressed, x, startY, sizeX, size, ApoMarioMenu.FUNCTION_START, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		
		iNormal = images.getButtonImage(sizeX, size, "Options", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[1] = new ApoButtonText(iNormal, iOver, iPressed, x, startY + size + distance, sizeX, size, ApoMarioMenu.FUNCTION_OPTIONS, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		
		iNormal = images.getButtonImage(sizeX, size, "Highscore", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[36] = new ApoButtonText(iNormal, iOver, iPressed, x, startY + (size + distance) * 2, sizeX, size, "highscore", ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Simulate", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[2] = new ApoButtonText(iNormal, iOver, iPressed, x, startY + (size + distance) * 3, sizeX, size, ApoMarioMenu.FUNCTION_SIMULATE, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Editor", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[3] = new ApoButtonText(iNormal, iOver, iPressed, x, startY + (size + distance) * 4, sizeX, size, ApoMarioMenu.fUNCTION_EDITOR, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Credits", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[4] = new ApoButtonText(iNormal, iOver, iPressed, x, startY + (size + distance) * 5, sizeX, size, ApoMarioMenu.FUNCTION_CREDITS, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Quit", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[5] = new ApoButtonText(iNormal, iOver, iPressed, x, startY + (size + distance) * 6, sizeX, size, ApoMarioMenu.FUNCTION_QUIT, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		
		iNormal = images.getButtonImage(sizeX, size, "Load Replay", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[20] = new ApoButtonText(iNormal, iOver, iPressed, width - sizeX - 10, height - size - 10, sizeX, size, ApoMarioMenu.FUNCTION_REPLAYLOAD, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		
		iNormal = images.getButtonImage(sizeX, size, "Load Editor", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[21] = new ApoButtonText(iNormal, iOver, iPressed, width - sizeX - 10, height - (size + 10) * 2, sizeX, size, ApoMarioMenu.FUNCTION_EDITORLOAD, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		int playerWidth = ApoMarioConstants.GAME_WIDTH * 1 / 4 + ApoMarioConstants.TILE_SIZE * 2;
		int playerHeight = (int)(ApoMarioConstants.TILE_SIZE * 3.4);
		int playerY = (int)(ApoMarioConstants.GAME_HEIGHT*1/4 - 3 * ApoMarioConstants.TILE_SIZE);
		int playerXOne = ApoMarioConstants.GAME_WIDTH/8 - ApoMarioConstants.TILE_SIZE;
		int playerXTwo = ApoMarioConstants.GAME_WIDTH*5/8 - ApoMarioConstants.TILE_SIZE;
		int arrowWidth = 20 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int arrowHeight = 20 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		
		iNormal = images.getButtonImage(arrowWidth, arrowHeight, "<", ApoMarioConstants.FONT_ARROW, 5);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[13] = new ApoButtonText(iNormal, iOver, iPressed, playerXOne + 5, playerY + playerHeight - arrowHeight - 5, arrowWidth, arrowHeight, ApoMarioMenu.FUNCTION_PLAYER_ONE_LEFT, ApoMarioConstants.FONT_ARROW, Color.BLACK, Color.BLACK);
		
		iNormal = images.getButtonImage(arrowWidth, arrowHeight, ">", ApoMarioConstants.FONT_ARROW, 5);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[14] = new ApoButtonText(iNormal, iOver, iPressed, playerXOne + playerWidth - arrowWidth - 5, playerY + playerHeight - arrowHeight - 5, arrowWidth, arrowHeight, ApoMarioMenu.FUNCTION_PLAYER_ONE_RIGHT, ApoMarioConstants.FONT_ARROW, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(arrowWidth, arrowHeight, "<", ApoMarioConstants.FONT_ARROW, 5);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[15] = new ApoButtonText(iNormal, iOver, iPressed, playerXTwo + 5, playerY + playerHeight - arrowHeight - 5, arrowWidth, arrowHeight, ApoMarioMenu.FUNCTION_PLAYER_TWO_LEFT, ApoMarioConstants.FONT_ARROW, Color.BLACK, Color.BLACK);
		
		iNormal = images.getButtonImage(arrowWidth, arrowHeight, ">", ApoMarioConstants.FONT_ARROW, 5);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[16] = new ApoButtonText(iNormal, iOver, iPressed, playerXTwo + playerWidth - arrowWidth - 5, playerY + playerHeight - arrowHeight - 5, arrowWidth, arrowHeight, ApoMarioMenu.FUNCTION_PLAYER_TWO_RIGHT, ApoMarioConstants.FONT_ARROW, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(arrowWidth * 2, arrowHeight, "Load AI", ApoMarioConstants.FONT_ARROW, 5);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[22] = new ApoButtonText(iNormal, iOver, iPressed, playerXOne + playerWidth/2 - arrowWidth, playerY + playerHeight - arrowHeight - 5, arrowWidth * 2, arrowHeight, ApoMarioMenu.FUNCTION_LOAD_PLAYER_ONE, ApoMarioConstants.FONT_ARROW, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(arrowWidth * 2, arrowHeight, "Load AI", ApoMarioConstants.FONT_ARROW, 5);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[23] = new ApoButtonText(iNormal, iOver, iPressed, playerXTwo + playerWidth/2 - arrowWidth, playerY + playerHeight - arrowHeight - 5, arrowWidth * 2, arrowHeight, ApoMarioMenu.FUNCTION_LOAD_PLAYER_TWO, ApoMarioConstants.FONT_ARROW, Color.BLACK, Color.BLACK);

		int backWidth = 100 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		int backHeight = 30 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		
		iNormal = images.getButtonImage(backWidth, backHeight, "Back", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[6] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth/2, height - backHeight - 10, backWidth, backHeight, ApoMarioOptions.FUNCTION_OPTIONS_BACK, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		this.game.getButtons()[7] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth/2, height - backHeight - 10, backWidth, backHeight, ApoMarioCredits.FUNCTION_CREDITS_BACK, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		this.game.getButtons()[8] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth/2, height - backHeight - 10, backWidth, backHeight, ApoMarioAnalysis.FUNCTION_ANALYSIS_BACK, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		this.game.getButtons()[9] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth/2, height - backHeight - 10, backWidth, backHeight, ApoMarioSimulation.FUNCTION_SIMULATION_BACK, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
		this.game.getButtons()[37] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth/2, height - backHeight - 10, backWidth, backHeight, ApoMarioHighscore.FUNCTION_HIGHSCORE_BACK, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(backWidth, backHeight, "Restart", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[10] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth - backWidth/2 - 10, height - backHeight - 10, backWidth, backHeight, ApoMarioAnalysis.FUNCTION_ANALYSIS_RESTART, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(backWidth, backHeight, "New Level", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[11] = new ApoButtonText(iNormal, iOver, iPressed, width/2 + backWidth/2 + 10, height - backHeight - 10, backWidth, backHeight, ApoMarioAnalysis.FUNCTION_ANALYSIS_NEWLEVEL, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(backWidth, backHeight, "Save Replay", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[12] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth/2, height - (backHeight + 10) * 2, backWidth, backHeight, ApoMarioAnalysis.FUNCTION_ANALYSIS_REPLAYSAVE, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(backWidth, backHeight, "Simulate", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[17] = new ApoButtonText(iNormal, iOver, iPressed, width/2 - backWidth/2, height/2 + 30 * ApoMarioConstants.SIZE, backWidth, backHeight, ApoMarioSimulation.FUNCTION_SIMULATION_SIMULATE, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Test", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[18] = new ApoButtonText(iNormal, iOver, iPressed, 10, 10, sizeX, size, ApoMarioEditor.FUNCTION_TEST, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Menu", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[19] = new ApoButtonText(iNormal, iOver, iPressed, width - sizeX - 10, 10, sizeX, size, ApoMarioEditor.FUNCTION_MENU, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Save", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[24] = new ApoButtonText(iNormal, iOver, iPressed, width - sizeX - 10, size + 20, sizeX, size, ApoMarioEditor.FUNCTION_SAVE, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Load", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[25] = new ApoButtonText(iNormal, iOver, iPressed, width - sizeX - 10, (size + 10) * 2 + 10, sizeX, size, ApoMarioEditor.FUNCTION_LOAD, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(sizeX, size, "Random", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[26] = new ApoButtonText(iNormal, iOver, iPressed, 10, size + 20, sizeX, size, ApoMarioEditor.FUNCTION_RANDOM, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		int speedWidth = 30 * ApoMarioConstants.SIZE * ApoMarioConstants.APP_SIZE;
		iNormal = images.getButtonImage(speedWidth, size, "<", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[27] = new ApoButtonText(iNormal, iOver, iPressed, 10, height - size - 10, speedWidth, size, ApoMarioGame.FUNCTION_SPEED_LEFT, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);

		iNormal = images.getButtonImage(speedWidth, size, ">", ApoMarioConstants.FONT_BUTTON, 10);
		iOver = images.getImageMouseOver(iNormal);
		iPressed = images.getImageMouseOver(iNormal, 100);
		this.game.getButtons()[28] = new ApoButtonText(iNormal, iOver, iPressed, 20 + speedWidth, height - size - 10, speedWidth, size, ApoMarioGame.FUNCTION_SPEED_RIGHT, ApoMarioConstants.FONT_BUTTON, Color.BLACK, Color.BLACK);
	}
}