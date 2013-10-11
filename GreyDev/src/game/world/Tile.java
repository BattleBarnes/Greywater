/**
 *TILE CLASS
 * 
 * Used for floor tiles. They have a collision space in case tiles need to be "unpassable" or have effects
 * such as harming a player or slowing their movement, now that's possible.
 * 
 * Tiles do not move and do not translate their physics space, but they MAY animate (glowing symbols on for instance)
 * 
 * PLEASE SEE THE ENTITY CLASS FOR INFORMATION ABOUT ISOMETRIC/FLATSPACE INTERCHANGE.
 * 
 * @author Jeremy Barnes Feb 3/13
 */
package game.world;

import game.Globals;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Tile extends Entity {
	public boolean selected = false;
	/**
	 * Constructor!
	 * 
	 * @param graphicsComponent - tile sprite that represents this bit of terrain
	 * @param xPos -y location in 2D flatspace
	 * @param yPos -x location in 2D flatspace
	 */
	public Tile(Sprite graphicsComponent, double xPos, double yPos) {
		this.graphicsComponent = graphicsComponent;
		//width is half because graphics component is isometric - 2:1 W:H ratio, needs fixing for square flatspace
		//speed is 0 because tiles don't move, silly
		this.physicsComponent = new Tangible(xPos, yPos, graphicsComponent.getWidth()/2, graphicsComponent.getHeight(), 0); 
		
	}


	/**
	 * Ticks the graphics component in case it is animated.
	 *  Draws the current sprite for this entity by calling the superclass's render method
	 * @param g - Graphics device
	 */
	public void render(Graphics g){
		Rectangle2D r = this.getPhysicsShape();

		if (Globals.DEVMODE != 1) {
			
			if(selected){
				g.setColor(Color.WHITE);
				g.fillRect((int) r.getX() - 500, (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
			}
			
			g.setColor(Color.PINK);
			g.drawRect((int) r.getX() - 500, (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
			
		}
	
		
		graphicsComponent.tick(); //maybe walls are animated
		super.render(g);
	}

	
	



}