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

public class OverlayManager {

	static HUDge headsUp;
	static StartMenu start;
	static InventoryMenu inv;
	static Sprite light = new Sprite("light", "light");
	static String displayText = "";
	private static Player p;
	private static AIInventory currentLoot;
	private static Item transferItem;

	public static void init(Core parent, Player np) {
		headsUp = new HUDge();
		start = new StartMenu(parent);
		inv = np.getInv();
		p = np;
	}

	public static void tick() {
		InputHandler.tick();
		currentLoot = (AIInventory) p.currentLoot;
		if (InputHandler.menu.keyTapped) { // brings up the menu.
			if (!Globals.state.drawMenu)
				Globals.state = State.gameMenu;
			else
				Globals.state = State.inGame;
		}
		if (Globals.state.isPaused || !Globals.state.gameRunning)
			start.update();
		inv.update();
		headsUp.update(p.getHP(), 100);
		if(currentLoot != null){
			currentLoot.update();
			transferItem = currentLoot.selectedItem;
			if(transferItem!=null)
				inv.addItem(transferItem);
			transferItem = null;
			currentLoot.dropItem();
		}
		headsUp.drawText(displayText);
		displayText="";
	}

	public static void render(Graphics g) {

		if (Globals.state.gameRunning) {
			Graphics2D g2 = (Graphics2D) g;
			g2.scale(Camera.width * 1.0 / light.getWidth(), Camera.height * 1.0/ light.getHeight());
			light.render(g2, 0, 0); 
			g2.scale(light.getWidth() * 1.0 / Camera.width, light.getHeight()* 1.0 / Camera.height);
			if(currentLoot != null){
				currentLoot.render(g);
			}

			headsUp.render(g);
		}
		if (Globals.state.isPaused || !Globals.state.gameRunning)
			start.render(g);
		else if (Globals.state == State.gameMenu) {
			inv.render(g);
		}
		
		g.setColor(Color.PINK);
		
		//g.drawString(displayText, 100, 100);

	}
	
	

}
