/**
 * StartMenu.java
 * @author Barnes 
 * 
 * This class is the main menu, so when the game first begins this is the menu that the players see.
 * It can be the pause menu as well, but I don't really like that much. We'll see.
 * 
 * It has lots of temporary stuff like "HOW TO PLAY" which will later go away and be replaced by a tutuorial level of some 
 * description, but for alpha build it's here to stay (5/9/13)
 * 
 * 
 */
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

public class StartMenu {

	/*
	 * These booleans are temporary (possibly) - since the menu has multiple screens (credits, how to play, menu), it needs to keep track of where it is
	 * If we move these screens to other classes later, these booleans will be unnecessary.
	 */
	boolean titleScrn = true;
	boolean howPlayScrn = false;
	boolean creditsScrn = false;

	/* Menu elements, buttons and the "cursor" */
	Sprite endscreen = new Sprite("Title", "Title");
	Sprite title = new Sprite("NewTitle", "NewTitle");

	Sprite newGame = new Sprite("NewGame", "NewGame");
	Sprite hnew = new Sprite("HNewGame", "HNewGame");
	Sprite exit = new Sprite("Exit", "Exit");
	Sprite hexit = new Sprite("HExit", "HExit");


	// List of main menu option buttons, to allow easy for-looping
	Sprite[] options = { newGame,  exit };

	// Currently selected option
	int option = 0;

	// The font the menu uses for printing text (when the buttons aren't prerendered
	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 60);
	FontMetrics fm; // for sizing up the font.

	// The game core, for resetting and starting new games and other exciting nonsense.
	Core parent;

	/**
	 * Constructor! Main menus don't have a lot of variation, so all this needs is to set up the parent Core.
	 * 
	 * @param parent
	 */
	public StartMenu(Core parent) {
		this.parent = parent;
	}

	/**
	 * Draws the menu to the screen
	 * 
	 * @param g - Graphics object
	 */
	public void render(Graphics g) {
		
		// if the game is over, show the win screen
		if (Globals.state == State.gameWon) {
			title.render(g, 0, 0);
			g.setColor(Color.ORANGE);
			g.setFont(menuFont);
			String s0 = "You Escaped! You win!";
			int strw = fm.stringWidth(s0);
			g.drawString(s0, (int) (Camera.actWidth / 3 - strw / 2), Camera.actHeight / 3 + 50);
			return;

		}
		if (fm == null) {
			fm = g.getFontMetrics(menuFont);
		}

		// if we're on the titlescreen, render the title and the main menu
		if (titleScrn) {
			title.render(g, 0, 0); // Greywater

			for (int i = 0; i < 2; i++) { // loop through menu options
				options[i].render(g, 0, 0); // and draw them

				if (option == 0) // if this element is the currently selected one, render the bullet at that location.
					hnew.render(g, 0, 0);
				if(option == 1)
					hexit.render(g, 0, 0);
			}
		}
		// if we're on the how to play screen, render that information instead
		else if (howPlayScrn) {
			g.setColor(Color.ORANGE);
			menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, (int) (60));
			fm = g.getFontMetrics(menuFont);
			g.setFont(menuFont);

			// set up the screentext
			String s0 = "Ms Sweepy has gotten lost in the sewers again!";
			String s1 = "The objective of the game is to find your robot - Ms Sweepy.";
			String s2 = "Evade the Watchmen who patrol the sewers.";
			String s3 = "Find Sweepy and escape the sewers.";
			
			String[] s = { s0, s1, s2, s3, "" };

			// draw the strings
			for (int i = 0; i < s.length; i++) {
				int strw = fm.stringWidth(s[i]);
				g.drawString(s[i], (int) (Camera.actWidth / 2 - strw / 2), 100 + 70 * i);
			}
			
		}// otherwise, render the "credits" screen!
//		else if (creditsScrn) {
//			menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, (int) (50  ));
//			System.out.println(50      );
//			g.setColor(Color.ORANGE);
//			g.setFont(menuFont);
//			fm = g.getFontMetrics(menuFont);
//
//			// set up the screentext
//			String s0 = "Jill Graves - Graphics Wizard";
//			String s1 = "Dominique Barnes - Pixelmancer";
//			String s2 = "Grace Hammons - Imagemeister";
//			String s3 = "Jeremy Barnes - KeyboardMasher";
//			String s4 = "SPECIAL THANKS";
//			String s45 = "Alexandr Zhelanov - http://opengameart.org/";
//			String s5 = "Iwan Gabovitch - http://opengameart.org/";
//			String s6 = "Alvinwhatup2 - http://freesound.org/";
//			String s7 = "FreqMan - http://freesound.org/people/FreqMan/";
//			String s8 = "Brandon75689 - http://opengameart.org/";
//			String s9 = "VERY SPECIAL THANKS";
//			String s10 = "Coffee";
//			String[] s = { s0, s1, s2, s3, "", s4, s45, s5, s6, s7, s8, "", s9, s10 };
//
//			// draw the strings to screen
//			Graphics2D g2 = (Graphics2D) g;
//			for (int i = 0; i < s.length; i++) {
//				int strw = fm.stringWidth(s[i]);
//				g2.drawString(s[i], (int) (Camera.actWidth / 2 - strw / 2), (int) (50 + 60   * i)); // TODO fix for resolution differences
//
//			}
//			// render the back button and cursor TODO come back when new art assets for differnet resolutions happen
//			back.render(g, (int) (Camera.actWidth / 2 - back.getWidth()  ), (int) (Camera.actHeight - back.getHeight()  ));
//			cursor.render(g, Camera.actWidth / 2 - 160, (int) (Camera.actHeight - back.getHeight()  ));
//
//		}
	}

	/**
	 * Update method, ticks the menu to move the cursor and respond to user input.
	 */
	public void tick() {
		if (this.titleScrn) {
			if (InputHandler.up.keyTapped && option > 0) {
				option--; // move cursor up if not at top
			} else if (InputHandler.down.keyTapped && option < 2) {
				option++; // move cursor down if not at bottom
			} else if (InputHandler.use.keyTapped || InputHandler.leftClick.heldDown) {
				choose(); // if they hit enter, choose that selection
			}else if(InputHandler.getMouse().getY() > 700){
				option = 1;
			}
			else
				option = 0;
//			} else if (InputHandler.getMouse().getY() > (int) (5 * Camera.actHeight / 15) && InputHandler.getMouse().getY() < (int) (5 * Camera.actHeight / 15 + 99  )) {
//				option = 0;
//
//				if (InputHandler.leftClick.keyTapped) {
//					choose();
//				}
//			} else if (InputHandler.getMouse().getY() > (int) (5 * Camera.actHeight / 15 + 99  ) && InputHandler.getMouse().getY() < (int) (5 * Camera.actHeight / 15 + 99   * 2)) {
//				option = 1;
//
//				if (InputHandler.leftClick.keyTapped) {
//					choose();
//				}
//			} else if (InputHandler.getMouse().getY() > (int) (5 * Camera.actHeight / 15 + 99   * 2) && InputHandler.getMouse().getY() < (int) (5 * Camera.actHeight / 15 + 99   * 3)) {
//				option = 2;
//
//				if (InputHandler.leftClick.keyTapped) {
//					choose();
//				}
//			} else if (InputHandler.getMouse().getY() > (int) (5 * Camera.actHeight / 15 + 99   * 3) && InputHandler.getMouse().getY() < (int) (5 * Camera.actHeight / 15 + 99   * 4)) {
//				option = 3;
//
//				if (InputHandler.leftClick.keyTapped) {
//					choose();
//				}
//			}
		}
		else if(InputHandler.leftClick.keyTapped || InputHandler.spaceBar.keyTapped){
			choose();
		}

	}

	/**
	 * Determines what the player has chosen based on their currently selected option integer.
	 */
	public void choose() {
		if (!titleScrn) { // if they aren't in the title now, they can only be going back to the title
			creditsScrn = false;
			titleScrn = true;
			howPlayScrn = false;
			return;
		}

		// Otherwise, they are on the title and they are choosing something from the menu.
		switch (option) {
			case (0):
				parent.initNewGame();
				break;
			case (1):
				parent.exitGame();
				break;
		}
	}
}
