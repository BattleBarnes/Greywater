package game.overlay;

import java.awt.Graphics;

import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

public class Button extends Entity {
	boolean visible;
	
	
	public Button(int x, int y, int width, int height){
		visible = false;
	//	physicsComponent = new Tangible((int)(Camera.scale*x), (int)(Camera.scale*y), (int)(width*Camera.scale), (int)(Camera.scale*height), 0);
		physicsComponent = new Tangible(x,y,width,height,0);
	}
	
	public void render(Graphics g){
		if(visible)
			graphicsComponent.render(g, getX(), getY());
	}

}



