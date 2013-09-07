
package game.overlay;

import java.awt.Graphics;

import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

/**
 * Button.java
 * @author Jeremy Barnes 4/20/13
 * 
 * Used for menu buttons, its basically just an entity, though it can be invisible.
 * Use by finding if mousepoint is inside button hitspace.
 *
 */
public class Button extends Entity {
	boolean visible;
	
	/**
	 * Constructor for invisible buttons
	 * @param x - location of hitbox
	 * @param y 
	 * @param width - dimensions of hitbox
	 * @param height
	 */
	public Button(int x, int y, int width, int height){
		visible = false;
		physicsComponent = new Tangible(x,y,width,height,0);
	}
	
	/**
	 * Constructor for button with it's own graphics
	 * @param s - image
	 * @param x - location of hitbox
	 * @param y
	 * @param width - dimensions of hitbox
	 * @param height
	 */
	public Button(Sprite s, int x, int y, int width, int height) {
		graphicsComponent = s;
		visible = true;
		physicsComponent = new Tangible(x,y,width,height,0);
	}
	
	/**
	 * Draw the hitbox iff it has a graphics component, otherwise do nothing.
	 */
	public void render(Graphics g){
		if(visible)
			graphicsComponent.render(g, (int)getX(), (int)getY());
	}

}



