package game.overlay;

import game.Core;
import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.Player;
import game.entities.components.Sprite;
import game.entities.items.Item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * OverlayManager.java
 * @author Jeremy Barnes 4/25/13
 * 
 * Static class that controls any non-world screen overlays (inventories, HUDs, light overlays, etc, menus).
 * Static because only one will ever need to exist, and many different callers need access to it. Simpler and cleaner
 * than passing a billion parameters.
 * BOTH INIT METHODS HAVE TO RUN BEFORE TICKING EVER HAPPENS SWEET GOD.
 *
 */
public class OverlayManager {

	static HUDge headsUp; //heads up display manager
	static StartMenu start;
	static InventoryMenu inv; //player inventory screen
	static Sprite light = new Sprite("light", "light"); //light radius overlay
	static String displayText = ""; //display text for the central box on the heads up display (item names, etc)
	private static Player p; 
	private static AIInventory currentLoot; //loot currently being viewed by the player.
	private static Item transferItem; //item being moved from AI inventory to player inventory

	/**
	 * Sets up start menu, must be called before first tick method call
	 * @param parent - Core game class, so that the start menu can start new games.
	 */
	public static void initMenu(Core parent) {
		start = new StartMenu(parent);

	}

	/**
	 * Acts as a constructor
	 * @param np player needed to set up the inventory screen
	 */
	public static void init(Player np) {
		headsUp = new HUDge();
		inv = np.getInv();
		p = np;
	}

	/**
	 * Tick method. Updates the menu if in menu mode, updates inventory if the game is running
	 * Make sure both init methods are complete before ever calling this.
	 */
	public static void tick() {
		InputHandler.tick();

		if (InputHandler.menu.keyTapped) { // brings up the menu.
			if (!Globals.state.drawMenu)
				Globals.state = State.gameMenu;
			else
				Globals.state = State.inGame;
		}
		if (Globals.state.isPaused || !Globals.state.gameRunning)
			start.tick();
		
		if (Globals.state.gameRunning) {
			currentLoot = (AIInventory) p.currentLoot;
			inv.update();
			headsUp.tick(p.getHP(), 100);
			if (currentLoot != null) {
				currentLoot.update();
				transferItem = currentLoot.selectedItem;
				if (transferItem != null)
					inv.addItem(transferItem);
				transferItem = null;
				currentLoot.dropItem();
			}
			headsUp.drawText(displayText);
			displayText = "";
		}
	}

	/**
	 * Draw the inventory, menu, loot inventory, draw text, as required by gamestate.
	 * @param g
	 */
	public static void render(Graphics g) {

		if (Globals.state.gameRunning) {
			Graphics2D g2 = (Graphics2D) g;
			
//			g2.scale(Camera.scale,  Camera.scale);
			 light.render(g2, 0, 0);
//			 g2.scale(1/Camera.scale, 1/Camera.scale);
			 
			if (currentLoot != null) {
				currentLoot.render(g);
			}

			headsUp.render(g);
		}
		if (Globals.state.isPaused || !Globals.state.gameRunning)
			start.render(g);
		else if (Globals.state == State.gameMenu) {
			inv.render(g);
		}


	}

}
