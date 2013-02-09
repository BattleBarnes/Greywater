/**
 * PLAYER CLASS
 * 
 * Represents the only object in the game that takes player input.
 * This is a subclass of Entity with a preset Sprite name (Tavish) and with a
 * non-abstract getInput method, which takes player input from mouse and keyboard.
 * 
 * Read the Entity.java documentation for better information.
 * 
 */
package game.entities;

import game.Globals;
import game.engine.ImageLoader;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

public class Player extends Entity {

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
		this.physicsComponent  = new Tangible(x, y, 35, 35, 4);

		spriteXOff = -graphicsComponent.getWidth()/2 - 65;
		spriteYOff = -graphicsComponent.getHeight() + 65;
	}

	/**
	 * Gets player input from the InputHandler class (static calls).
	 * Uses direction method from Globals.java to determine which way player is facing (NSEW...)
	 * and sets the graphics component accordingly.
	 */
	protected void getInput(){
		if(InputHandler.up)
			yMoveBy = -10;
		if(InputHandler.down)
			yMoveBy = 10;
		if(InputHandler.left)
			xMoveBy = -10;
		if(InputHandler.right)
			xMoveBy = 10;
		if(InputHandler.mouseClicked){
			//TODO fix mouse input
			//			xMoveBy = InputHandler.mouseLocation.x - Camera.xOffset - xPos;
			//			yMoveBy = InputHandler.mouseLocation.y + Camera.xOffset - yPos;
		}

		direction = Globals.getIntDir(xMoveBy, yMoveBy);

		if(!InputHandler.left && !InputHandler.right)
			physicsComponent.stopXMovement();
		if(!InputHandler.up && !InputHandler.down)
			physicsComponent.stopYMovement();
		//these methods make you animate in the direction of movement, the art assets don't exist yet.
		//		if(yMoveBy != 0 || xMoveBy != 0 )
		//		 	graphicsComponent.loopImg(.9, "Walk" +Globals.getStringDir(direction));
		//		else
		//			graphicsComponent.loopImg(.4, "Stand"+Globals.getStringDir(direction));
		//TODO uncomment when images are ready
		
		if(InputHandler.inv){
			if(!Globals.state.drawMenu)
				Globals.state = State.gameMenu;
			else
				Globals.state = State.inGame;
			
			InputHandler.inv = false;
		}
	}

}
