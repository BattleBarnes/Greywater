package game.overlay;

import game.Core;
import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	Sprite back = new Sprite("Back", "Back");

	Sprite[] options = { newGame, howPlay, credits, exit };

	int option = 0;

	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 60);
	FontMetrics fm;

	int cursorLength = 0;

	Core parent;

	public StartMenu(Core parent) {

		this.parent = parent;
	}

	public void render(Graphics g) {
		if (Globals.state == State.gameWon) {
			title.render(g, 0, 0);
			g.setColor(Color.ORANGE);
			g.setFont(menuFont);
			String s0 = "You Escaped! You win!";
			int strw = fm.stringWidth(s0);
			g.drawString(s0, (int) (Camera.width / 3 - strw / 2), Camera.height / 3 + 50);
			return;

		}
		if (fm == null) {
			fm = g.getFontMetrics(menuFont);
		}
		if (titleScrn) {
			title.render(g, 0, 0);
			for (int i = 0; i < 4; i++) {
				options[i].render(g, 0, 0);
				if (option == i) {
					cursor.render(g, 190, 4 * Camera.height / 11 + 110 * i);
				}
			}
		}
		if (howPlayScrn) {
			g.setColor(Color.ORANGE);
			menuFont =  new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 60);
			fm = g.getFontMetrics(menuFont);
			g.setFont(menuFont);
			String s0 = "Ms Sweepy has gotten lost in the sewers again!";
			String s1 = "The objective of the game is to find your robot - Ms Sweepy.";
			String s2 = "Be careful of the Watchmen, the automated security drones --";
			String s3 = "they attack indiscriminately. Find Sweepy and escape the sewers.";
			String s4 = "CONTROLS";
			String s5 = "WASD or Arrow Keys to move";
			String s6 = "Spacebar or click to attack/loot enemies";
			String s7 = "ESC to go to menu.";
			String[] s = { s0, s1, s2, s3, "", s4, s5, s6, s7 };
			for (int i = 0; i < s.length; i++) {
				int strw = fm.stringWidth(s[i]);
				g.drawString(s[i], (int) (Camera.width / 2 - strw / 2), 100 + 70 * i);

			}
			back.render(g, (int)(Camera.width / 2 - 105*Camera.scale),(int)( Camera.height - 200*Camera.scale));

			cursor.render(g, Camera.width / 2 - 160, Camera.height - 200);

		}
		if (creditsScrn) {
			menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 50);
			g.setColor(Color.ORANGE);
			g.setFont(menuFont);
			fm = g.getFontMetrics(menuFont);
			String s0 = "Jill Graves - Graphics Wizard";
			String s1 = "Dominique Barnes - Pixelmancer";
			String s2 = "Grace Hammons - Imagemeister";
			String s3 = "Jeremy Barnes - KeyboardMasher";
			String s4 = "SPECIAL THANKS";
			String s45 = "Alexandr Zhelanov - http://opengameart.org/";
			String s5 = "Iwan Gabovitch - http://opengameart.org/";
			String s6 = "Alvinwhatup2 - http://freesound.org/";
			String s7 = "FreqMan - http://freesound.org/people/FreqMan/";
			String s8 = "Brandon75689 - http://opengameart.org/";
			String s9 = "VERY SPECIAL THANKS";
			String s10 = "Coffee";
			String[] s = { s0, s1, s2, s3, "", s4, s45, s5, s6, s7, s8, "", s9, s10 };
			Graphics2D g2 = (Graphics2D) g;
			for (int i = 0; i < s.length; i++) {
				int strw = fm.stringWidth(s[i]);
				g2.drawString(s[i], (int) (Camera.width / 2 - strw / 2), 50+ 60 * i);

			}
			back.render(g, (int)(Camera.width / 2 - back.getWidth()*Camera.scale),(int)( Camera.height - back.getHeight()*Camera.scale));
			cursor.render(g, Camera.width / 2 - 160, Camera.height - 200);

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
