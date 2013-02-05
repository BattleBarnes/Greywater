/**
 *TILE CLASS
 * 
 * Used for objects which need collision detection but do not move (Walls, doodads, items on the ground)
 * Object which do not move do not translate their physics space, but they MAY animate (glowing symbols on a wall for instance).
 * Items such as these do not actually have a physics space, they make a tile "unpassable".
 * 
 * Hold the graphics and collision aspects of the object. 
 * 
 * 
 * 
 * 
 * 
 * @author Jeremy Barnes Feb 3/13
 */
package game.world;

import game.engine.Camera;
import game.entities.components.Sprite;

import java.awt.Graphics;
import java.awt.Polygon;

public class Tile {

	/* **** All entities have a physics component for collisions and position, and a graphics component to render on screen ****/
	protected Polygon physicsComponent; //hitbox
	protected Sprite graphicsComponent; //sprite

	/* **** Gives the location of the hitbox ******/
	public int xPos;//current x
	public int yPos;//current y

	
	
	public Tile(Sprite graphicsComponent, int xPos, int yPos) {
		this.graphicsComponent = graphicsComponent;
		this.xPos = xPos;
		this.yPos = yPos;
		int[] xes = {0, graphicsComponent.getWidth()/2, graphicsComponent.getWidth(), graphicsComponent.getWidth()/2, 0};
		int[] yes = {graphicsComponent.getHeight()/2, graphicsComponent.getHeight(), graphicsComponent.getHeight()/2, 0, graphicsComponent.getHeight()/2};
		physicsComponent = new Polygon(xes, yes, 5);
		physicsComponent.translate(xPos,yPos);
	}


	/**
	 *  Draws the current sprite for this entity.
	 * @param g - Graphics device
	 */
	public void render(Graphics g){
		graphicsComponent.tick(); //maybe walls are animated
		graphicsComponent.draw(g, xPos  - Camera.xOffset, yPos - Camera.yOffset);
	}


	/**
	 * @return the Shape used for collisions and positioning
	 */
	public Polygon getPhysicsShape(){
		return physicsComponent;
	}

	


}