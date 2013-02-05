/**
 * TANGIBLE CLASS 
 * 
 * This class is used to hold positional data about an Entities' hitbox. It works with Static and Dynamic Entities, because Tangibles
 * don't have to move. This abstract class holds the basic information that all hitboxes must have -
 * Position, destination, speed, movement and stop movement.
 * 
 * 
 * @author Jeremy Barnes Dec 28/12 
 */
package game.entities.components;

import java.awt.Rectangle;
import java.awt.Shape;

public class Tangible {

	/* ****** POSITIONAL VARIABLES *******/
	public int xPos; //current x
	public int yPos; //current y
	public int xDest;//future x
	public int yDest;//future y
	public int speed = 0; // how fast it goes from xPos to xDest. Default - 0;
	
	//physics object
	private Rectangle hitBox;
		
	
	/**
	 * Constructor. Sets up location and square hit space
	 * 
	 * @param x - X co-ordinate of hitBox (upper left corner)
	 * @param y - Y co-ordinate of hitBox (upper left corner)
	 * @param speed - how many pixels per update the sprite moves
	 * @param hitWidth - width of the hitBox
	 * @param hitHeight - height of hitBox
	 */
	public Tangible(int x, int y, int hitWidth, int hitHeight, int speed){
		xPos = x;
		yPos = y;
		xDest = x;
		yDest = y;
		this.speed = speed;
		hitBox = new Rectangle(xPos, yPos, hitWidth, hitHeight);
	}


	
	
	/**
	 * Used to move the object if it has a destination. If not, does nothing.
	 */
	public void tick(){
		if(xPos != xDest)
			xPos += Integer.signum(xDest - xPos)*speed;
		if(yPos != yDest)
			yPos += Integer.signum(yDest - yPos) *speed;
		
		updateHitSpace(xPos, yPos);
	}
	
	/**
	 * Updates the hitspace
	 * 
	 * @param x - new x co-ordinate
	 * @param y - new y co-ordinate
	 */
	protected void updateHitSpace(int x, int y){
			hitBox.setLocation(x, y);
	}


	/**
	 * Moves by x and y amount.
	 * Does not move TO X and Y.
	 * 
	 * @param x - how much to move in the x direction
	 * @param y - how much to move in the y direction
	 */
	public void move(int x, int y){
		xDest += x;
		yDest += y;
	}

	/**
	 * Moves character to X,Y
	 * 
	 * @param x - where to go in the x direction
	 * @param y - where to go in the y direction
	 */
	public void moveTo(int x, int y){
		xDest = x;
		yDest = y;
	}

	/**
	 * Stops all movement immediately.
	 */
	public void stopMovement(){
		xDest = xPos;
		yDest = yPos;
		updateHitSpace(xPos, yPos);
	}
	
	/**
	 * Stops X movement immediately
	 */
	public void stopXMovement(){
		xDest = xPos;
		updateHitSpace(xPos, yPos);
	}
	
	/**
	 * Stops Y movement immediately
	 */
	public void stopYMovement(){
		yDest = yPos;
		updateHitSpace(xPos, yPos);
	}
	
	public Rectangle getPhysicsShape(){
		return hitBox;
	}
}
	