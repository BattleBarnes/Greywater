package game.overlay;

import java.awt.Graphics;

import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

public class Button extends Entity {
	boolean visible;
	
	public Button(Sprite button, int x, int y){
		physicsComponent = new Tangible(x, y, ((int)(Camera.scale * button.getWidth())),((int)( Camera.scale* button.getHeight())), 0);
		graphicsComponent = button;
		visible = true;
	}
	
	public Button(int x, int y, int width, int height){
		visible = false;
		physicsComponent = new Tangible(x, y, width, height, 0);
	}
	
	public void render(Graphics g){
		if(visible)
			graphicsComponent.render(g, getX(), getY());
	}
	
	
	public void renderScaled(Graphics g){
		if(visible)
			graphicsComponent.renderScaled(g, getX(), getY());
	}
	

}



