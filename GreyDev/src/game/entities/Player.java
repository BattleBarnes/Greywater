/**
 * PLAYER CLASS
 * 
 * Represents the only object in the game that takes player input.
 * This is a subclass of Dynamic Entity with a preset Sprite name (Tavish) and with a
 * non-abstract getInput method, which takes player input from mouse and keyboard.
 * 
 */
package game.entities;

import game.Globals;
import game.engine.ImageLoader;
import game.engine.InputBundle;
import game.entities.components.DynEntity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

public class Player extends DynEntity {
	
	/**
	 * Constructor!
	 * 
	 * @param x - Starting x location
	 * @param y - Starting y location
	 * @param anim_Per_Mil - Period of animation in millis
	 * @param ims - image loader (for the sprite)
	 */
	public Player(int x, int y, int anim_Per_Mil, ImageLoader ims){
		this.graphicsComponent = new Sprite(ims, "Tavish", anim_Per_Mil, "TavishStandSouth");
		this.physicsComponent  = new Tangible(x, y, graphicsComponent.getWidth() / 3, graphicsComponent.getHeight()/ 10, 2);

		spriteXOff = -graphicsComponent.getWidth()/3;
		spriteYOff = -9*graphicsComponent.getHeight()/10;
	}

	/**
	 * Gets player input from the InputBundle class (static calls).
	 * Uses direction method from Globals.java to determine which way player is facing (NSEW...)
	 * and sets the graphics component accordingly.
	 */
	protected void getInput(){
		if(InputBundle.up)
			yMoveBy = -10;
		if(InputBundle.down)
			yMoveBy = 10;
		if(InputBundle.left)
			xMoveBy = -10;
		if(InputBundle.right)
			xMoveBy = 10;
		if(InputBundle.mouseClicked){
			//TODO fix mouse input
//			xMoveBy = InputBundle.mouseLocation.x - Camera.xOffset - xPos;
//			yMoveBy = InputBundle.mouseLocation.y + Camera.xOffset - yPos;
		}
			
		direction = Globals.getIntDir(xMoveBy, yMoveBy);
		
		if(!InputBundle.left && !InputBundle.right)
			physicsComponent.stopXMovement();
		if(!InputBundle.up && !InputBundle.down)
			physicsComponent.stopYMovement();
		//these methods make you animate in the direction of movement, the art assets don't exist yet.
//		if(yMoveBy != 0 || xMoveBy != 0 )
//		 	graphicsComponent.loopImg(.9, "Walk" +Globals.getStringDir(direction));
//		else
//			graphicsComponent.loopImg(.4, "Stand"+Globals.getStringDir(direction));
		//TODO uncomment when images are ready
	}
	
	
}
