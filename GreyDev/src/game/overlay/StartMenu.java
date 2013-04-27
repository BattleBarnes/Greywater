package game.overlay;

import game.Core;
import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Sprite;

import java.awt.Graphics;

public class StartMenu {
	// String cursor = "";
	// String[] options = new String[]{"Start New Game", "Load Old Game", "Exit"};

	boolean titleScrn = true;
	boolean howPlayScrn;
	boolean creditsScrn;

	Sprite cursor = new Sprite("Bullet", "Bullet");
	Sprite title = new Sprite("Title", "Title");
	Sprite credits = new Sprite("Credits", "Credits");
	Sprite newGame = new Sprite("NewGame", "NewGame");
	Sprite howPlay = new Sprite("HowToPlay", "HowToPlay");
	Sprite exit = new Sprite("Exit", "Exit");

	Sprite[] options = { newGame, howPlay, credits, exit };

	int option = 0;

	// Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 40);
	// FontMetrics fm;

	int cursorLength = 0;

	Core parent;

	public StartMenu(Core parent) {

		this.parent = parent;
	}

	public void render(Graphics g) {
		title.render(g, 0, 0);
		// if(fm == null){
		// fm = g.getFontMetrics(menuFont);
		// }
		// g.setColor(Color.darkGray);
		// g.setFont(menuFont);
		if (titleScrn)
			for (int i = 0; i < 4; i++) {
				options[i].render(g, 0, 0);
				if (option == i) {
					cursor.render(g, 190, 4 * Camera.height / 11 + 110 * i);
				}
			}
		if (howPlayScrn) {

		}
		if (creditsScrn) {

		}
	}

	public void update() {
		if (InputHandler.up.keyTapped && option > 0) {
			option--;
		}
		if (InputHandler.down.keyTapped && option < 3) {
			option++;
		}
		if (InputHandler.use.keyTapped) {
			choose();
		}

	}

	public void choose() {
		if (!titleScrn) {
			creditsScrn = false;
			titleScrn = true;
			howPlayScrn = false;
			return;
		}

		switch (option) {
			case (0):
				parent.initNewGame();
				break;
			case (1):
				creditsScrn = false;
				titleScrn = false;
				howPlayScrn = true;
				break;
			case (2):
				creditsScrn = true;
				titleScrn = false;
				howPlayScrn = false;
				break;
			case (3):
				parent.exitGame();
				break;
		}
	}
}
