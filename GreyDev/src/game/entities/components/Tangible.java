/**
 * TANGIBLE CLASS 
 * 
 * This class is used to hold data about an Entities' hitbox, including position, speed, and accessor methods.
 * In reality, this class is the Entity. The Entity interacts with the world entirely through this class because it is the
 * 2D representation. 
 * 
 *  ***********  IMPORTANT ************************
 * The graphics component (sprite) is the isometric image that is rendered to the screen, but isometric math is unnecessarily
 * intense, and wasteful to do when cheaper, more effective methods are available. To that end, the world is ACTUALLY a top down
 * rectangular world, and is merely represented isometrically.
 * 
 * This class is the 2D physics component (hitbox) and the iso aspect is the graphics component.
 * This class controls the speed, movement, location, and collision of the object as if it were top-down, and then
 * that data is taken by the entity class and math'd into isometric silliness.
 * 
 * I adopted this paradigm from Roger Engelbert
 * http://www.rengelbert.com/tutorial.php?id=76&show_all=true
 * 
 * 
 * @author Jeremy Barnes Dec 28/12 
 */
package game.entities.components;

import java.awt.Rectangle;

public class Tangible {

	/* ****** POSITIONAL VARIABLES *******/
	protected int xPos; //current x
	protected int yPos; //current y
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
		
		if(x == 0){
			xDest = xPos;
		}
		if( y == 0){
			yDest = yPos;
		}
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
	
	/**
	 * @return the Rectangle used for collision detection.
	 */
	public Rectangle getPhysicsShape(){
		return hitBox;
	}
	
	/**
	 * @return Whether or not the physicscomponent still has distance to traverse.
	 */
	public boolean isMoving(){
		if(xDest != xPos)
			return true;
		else if(yDest != yPos)
			return true;
		else
			return false;
	}
}
	