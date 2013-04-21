package game.overlay;

import game.Core;
import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class OverlayManager {

	HUDge headsUp;
	StartMenu start;
	InventoryMenu inv;
	Sprite light = new Sprite("light", "light");
	String displayText = "";
	Sprite hud = new Sprite("hud", "hud");

	public OverlayManager(Core parent, InventoryMenu inv) {
		headsUp = new HUDge();
		start = new StartMenu(parent);
		this.inv = inv;
		inv.setParent(this);

	}

	public void tick() {
		InputHandler.tick();
		if (InputHandler.menu.keyTapped) { // brings up the menu.
			if (!Globals.state.drawMenu)
				Globals.state = State.gameMenu;
			else
				Globals.state = State.inGame;
		}
		if (Globals.state == State.mainMenu)
			start.update();
		inv.update();
		headsUp.update();
		headsUp.drawText(displayText);
		displayText="";
	}

	public void render(Graphics g) {

		if (Globals.state.gameRunning) {
			Graphics2D g2 = (Graphics2D) g;
			g2.scale(Camera.width * 1.0 / light.getWidth(), Camera.height * 1.0/ light.getHeight());
			light.render(g2, 0, 0);
			g2.scale(light.getWidth() * 1.0 / Camera.width, light.getHeight()* 1.0 / Camera.height);

			headsUp.render(g);
		}
		if (Globals.state == State.mainMenu)
			start.render(g);
		else if (Globals.state == State.gameMenu) {
			inv.render(g);
		}
		g.setColor(Color.PINK);
		
		//g.drawString(displayText, 100, 100);

	}

}
