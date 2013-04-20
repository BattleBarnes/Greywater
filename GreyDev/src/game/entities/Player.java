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
import game.engine.InputHandler;
import game.engine.State;
import game.engine.audio.AudioLoader;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.overlay.InventoryMenu;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Mob {
	
	private InventoryMenu inv;
	
	int lastPos = 0;
	public boolean notPlayed = true;

	/**
	 * Constructor!
	 * 
	 * @param x - Starting x location
	 * @param y - Starting y location
	 */
	public Player(int x, int y, InventoryMenu m){
		name = "Watchman";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name+"StandSouth");
		this.physicsComponent  = new Tangible(x, y, 35, 35, 2);

		spriteXOff = -graphicsComponent.getWidth()/2 - 65;
		spriteYOff = -graphicsComponent.getHeight() + 65;
		inv = m;
	}
	

	/**
	 * Gets player input from the InputHandler class (static calls).
	 * Uses direction method from Globals.java to determine which way player is facing (NSEW...)
	 * and sets the graphics component accordingly.
	 */
	protected void getInput(){
		
		if(graphicsComponent.isAnimating()){
			lastPos =graphicsComponent.seriesPosition;
			if(lastPos == 0 || lastPos == 3){
				if(notPlayed){
					notPlayed = false;
					AudioLoader.playGrouped("footstep");
				}
			}else if (lastPos != 0 && lastPos != 3)
				notPlayed = true;
		}
		
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
		

		if(InputHandler.menu.keyTapped){ //brings up the menu.
			if(!Globals.state.drawMenu)
				Globals.state = State.gameMenu;
			else
				Globals.state = State.inGame;
		}

		inv.update();

	}
	
	
//	public void render(Graphics g){
//		super.render(g);
//		Rectangle r = this.getPhysicsShape();
//		g.drawRect(r.x, r.y, r.width, r.height);
//	}


	@Override
	protected void attack(Mob enemy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void takeDamage(int damage) {
		// TODO Auto-generated method stub
		
	}


}
