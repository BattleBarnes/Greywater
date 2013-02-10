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
	public Player(int x, int y, int anim_Per_Mil, ImageLoader ims){
		this.graphicsComponent = new Sprite(ims, "Watchman", anim_Per_Mil, "WatchmanStandSouth");
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

		if(InputHandler.leftClick.heldDown){
			Point p = Globals.isoToGrid(InputHandler.leftClick.xPos + Camera.xOffset,InputHandler.leftClick.yPos + Camera.yOffset);
			Point r = Globals.findTile(p.x, p.y);
		//	xMoveBy = -(xPos - Globals.tileWidth*r.x);
		//	yMoveBy =-( yPos - Globals.tileHeight*r.y);
		physicsComponent.moveTo(Globals.tileWidth*r.x, Globals.tileHeight*r.y ); //move to new location


		}
		System.out.println(yMoveBy);

		direction = Globals.getIntDir(xMoveBy, yMoveBy);

	//	if(!InputHandler.left.heldDown && !InputHandler.right.heldDown && !InputHandler.leftClick.heldDown )
	//		physicsComponent.stopXMovement();
//		if(!InputHandler.up.heldDown && !InputHandler.down.heldDown && !InputHandler.leftClick.heldDown)
	//		physicsComponent.stopYMovement();


		
//		if(yMoveBy != 0 || xMoveBy != 0 ){
//			graphicsComponent.loopImg(.9, "Walk" +Globals.getStringDir(direction));
//			currDirection = Globals.getStringDir(direction);
//		}
//		else
//			graphicsComponent.forceImage("WatchmanStand"+currDirection);


		if(InputHandler.menu.keyTapped){
			if(!Globals.state.drawMenu)
				Globals.state = State.gameMenu;
			else
				Globals.state = State.inGame;

		}

	}

}
