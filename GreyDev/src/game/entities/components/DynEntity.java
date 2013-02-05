/** DYNAMIC ENTITY CLASS
 * 
 * Used for objects which need collision detection and are mobile (Players, Enemies)
 * 
 * Hold the graphics and collision aspects of the object. ALL objects in game must inherit either this class
 * or STATIC ENTITY @see StatEntity.java 
 * 
 * This class inherits StatEntity, because a DynEntity is just a StatEntity that can move.
 * 
 * This is an important differentiation because Static Entities do not need to be render sorted, since they never change position, whereas
 * Dynamic Entities need to be sorted amongst themselves and placed in proper order between Static Entities.
 * 
 * 
 * @author Jeremy Barnes Dec 10/12
 * Updated from generic ENTITY to DYNAMIC ENTITY and STATIC ENTITY Feb 03/13
 */

package game.entities.components;

import game.engine.Camera;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public abstract class DynEntity {
	
	/* **** All entities have a physics component for collisions and position, and a graphics component to render on screen ****/
	protected Tangible physicsComponent; //hitbox
	protected Sprite graphicsComponent; //sprite

	
	/* **** POSITIONING VARIABLES ******/
	public int xPos;//current x
	public int yPos;//current y
	protected int xMoveBy; //how much the entity is moving in the x direction
	protected int yMoveBy; //how much the entity is moving in the y direction
	public int xLast; //last valid (non colliding) x position
	public int yLast; //last valid (non colliding) y position
	
	public int direction; //current direction (N,S,E,W....) see Globals.java
	
	/* *** Position is determined by the hitbox, so offsets are tracked to draw in relation to the hitbox. Subclasses must set. ***/
	protected int spriteXOff; //horizontal distance from (0,0) on hitbox to (0,0) on sprite
	protected int spriteYOff; //vertical distance from (0,0) on hitbox to (0,0) on sprite
	

	/**
	 * Generic method for moving entities:
	 * Saves last valid position, moves the entity on its
	 * current trajectory, ticks the graphics component, moves the hitBox.
	 */
	public void update(){
		getInput();
		xLast = xPos; //last location was valid or it would have been undone already
		yLast = yPos;
		physicsComponent.move(xMoveBy, yMoveBy); //move to new location
		xPos = physicsComponent.xPos;
		yPos = physicsComponent.yPos;
		xMoveBy = 0;//no more movement needed
		yMoveBy = 0;
		physicsComponent.tick(); //update components
		graphicsComponent.tick();
	}
	
	/**
	 * Draws the current sprite for this entity.
	 * @param g - Graphics object
	 */
	public void render(Graphics g){
		graphicsComponent.draw(g, xPos + spriteXOff - Camera.xOffset, yPos + spriteYOff - Camera.yOffset);
	}
	
	/**
	 * Stops movement (for collisions)
	 * 
	 * @param x - if true, stop movement in the x direction
	 * @param y - if true, stop movement in the y direction
	 */
	public void undoMove(boolean x, boolean y){

		if(x){
			xPos = xLast;
			physicsComponent.xPos = xLast;
			physicsComponent.stopXMovement();
			xMoveBy = 0;
		}
		if(y){
			yPos = yLast;
			physicsComponent.yPos = yLast;
			physicsComponent.stopYMovement();
			yMoveBy = 0;
		}
		
		getInput();
		physicsComponent.move(xMoveBy, yMoveBy);
		physicsComponent.tick();
	}
	
	/**
	 * @return the Tangible used for collisions and positioning
	 */
	public Tangible getPhysics(){
		return physicsComponent;
	}

	/**
	 * @return the Shape used for collision detection
	 */
	public Rectangle getPhysicsShape(){
		return physicsComponent.getPhysicsShape();
	}


	/**
	 * Can be AI algorithms or player keypresses. I don't care, but all must implement.
	 */
	protected abstract void getInput();
		
}
	
	
