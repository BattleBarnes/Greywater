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
import game.engine.Camera;
import game.engine.ImageLoader;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.Point;

public class Player extends Entity {
	
	String currDirection = "South";

	/**
	 * Constructor!
	 * 
	 * @param x - Starting x location
	 * @param y - Starting y location
	 * @param anim_Per_Mil - Period of animation in millis
	 * @param ims - image loader (for the sprite)
	 */
	public Player(int x, int y, int anim_Per_Mil){
		this.graphicsComponent = new Sprite("Watchman", anim_Per_Mil, "WatchmanStandSouth");
		this.physicsComponent  = new Tangible(x, y, 35, 35, 2);

		spriteXOff = -graphicsComponent.getWidth()/2 - 65;
		spriteYOff = -graphicsComponent.getHeight() + 65;
	}

	/**
	 * Gets player input from the InputHandler class (static calls).
	 * Uses direction method from Globals.java to determine which way player is facing (NSEW...)
	 * and sets the graphics component accordingly.
	 */
	protected void getInput(){
		if(InputHandler.up.heldDown){
			xMoveBy = -2;
			yMoveBy = -2;

		}
		if(InputHandler.down.heldDown){
			xMoveBy = 2;
			yMoveBy = 2;
		}
		if(InputHandler.left.heldDown){
			xMoveBy = -2;
			yMoveBy =  2;

		}
		if(InputHandler.right.heldDown){
			xMoveBy = 2;
			yMoveBy = -2;
		}
		if(InputHandler.right.heldDown && InputHandler.up.heldDown){
			xMoveBy = 0;
			yMoveBy = -2;
		}
		if(InputHandler.right.heldDown && InputHandler.down.heldDown){
			xMoveBy = 2;
			yMoveBy = 0;
		}
		if(InputHandler.left.heldDown && InputHandler.up.heldDown){
			xMoveBy = -2;
			yMoveBy = 0;
		}
		if(InputHandler.left.heldDown && InputHandler.down.heldDown){
			xMoveBy = 0;
			yMoveBy = 2;
		}

		if(InputHandler.leftClick.heldDown){ //sets player destination to the tile they clicked.
			Point p = Globals.isoToGrid(InputHandler.leftClick.xPos + Camera.xOffset,InputHandler.leftClick.yPos + Camera.yOffset);
			Point r = Globals.findTile(p.x, p.y);
			xMoveBy = Globals.tileWidth*r.x - xPos;
			yMoveBy = Globals.tileHeight*r.y - yPos;
		}
		
		if(xMoveBy != 0 || yMoveBy != 0){ //sets the physicsComponent moving
			physicsComponent.moveTo(xPos + xMoveBy, yPos + yMoveBy);
		}

		direction = Globals.getIntDir(physicsComponent.xDest - xPos, physicsComponent.yDest - yPos);
		

		if(physicsComponent.isMoving()){ //display animation walk loop.
			graphicsComponent.loopImg(.9, "Walk" +Globals.getStringDir(direction));
			currDirection = Globals.getStringDir(direction);
		}
//		else
//			graphicsComponent.forceImage("WatchmanStand"+currDirection);


		if(InputHandler.menu.keyTapped){ //brings up the menu.
			if(!Globals.state.drawMenu)
				Globals.state = State.gameMenu;
			else
				Globals.state = State.inGame;
		}

		xMoveBy = 0;//Clear these out for the next input cycle.
		yMoveBy = 0;
		
	}

}
