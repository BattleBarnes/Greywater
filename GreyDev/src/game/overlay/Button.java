package game.overlay;

import java.awt.Graphics;

import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

public class Button extends Entity {
	
	public Button(Sprite button, int x, int y){
		physicsComponent = new Tangible(x, y, ((int)(Camera.scale * button.getWidth())),((int)( Camera.scale* button.getHeight())), 0);
		graphicsComponent = button;
	}
	
	public void render(Graphics g){
		graphicsComponent.render(g, getX(), getY());
	}
	
	
	public void renderScaled(Graphics g){
			graphicsComponent.renderScaled(g, getX(), getY());
	}
	

}



