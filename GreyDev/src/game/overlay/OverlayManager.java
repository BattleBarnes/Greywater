package game.overlay;

import game.Core;
import game.Globals;
import game.engine.Camera;
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
	String displayText;
	
	public OverlayManager(Core parent, InventoryMenu inv){
		headsUp = new HUDge();
		start = new StartMenu(parent);
		this.inv = inv;
		inv.setParent(this);
		
	}
	
	public void tick(){
		start.update();
		inv.update();
	}
		
	
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(Camera.width*1.0/light.getWidth(), Camera.height*1.0/light.getHeight());
		light.render(g2, 0, 0);
		g2.scale(light.getWidth()*1.0/Camera.width, light.getHeight()*1.0/Camera.height);
		
		g2.scale(Camera.scale, Camera.scale);
//		hud.render(g2, 0, (int) (Camera.height/Camera.scale - hud.getHeight()) );

		g2.scale(1/Camera.scale, 1/Camera.scale);
		
		if(Globals.state == State.mainMenu)
			start.render(g);
		else if (Globals.state == State.gameMenu)
			inv.render(g);
		g.setColor(Color.PINK);
		
		//g.drawString(displayText, 0, 0);
		
	}
	

}
