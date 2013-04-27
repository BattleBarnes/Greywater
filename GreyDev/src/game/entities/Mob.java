package game.entities;

import game.Globals;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.ai.PathFinder;
import game.overlay.InventoryMenu;
import game.overlay.OverlayManager;
import game.world.World;

import java.awt.Point;
import java.awt.geom.Line2D;

public abstract class Mob extends Entity {

	public int direction; // current direction (N,S,E,W....) see Globals.java

	protected String currDirection = "South";
	protected String name;
	public Entity target;
	public Point destination;
	boolean walks = true;
	boolean playerFriend;

	PathFinder p;

	private int HP = 100;
	protected double walkRate = .59; // used to determine how fast a mob's walk
										// animation cycle is
	protected Line2D sight;
	protected boolean validSight;
	public int sightRange;

	/* **** POSITIONING VARIABLES ***** */
	protected int xMoveBy; // how much the entity is moving in the x direction
							// (DELTA)
	protected int yMoveBy; // how much the entity is moving in the y direction

	public int xLast; // last valid (non colliding) x position
	public int yLast; // last valid (non colliding) y position
	boolean attacking = false;
	protected InventoryMenu inv;
	public InventoryMenu currentLoot;

	/**
	 * Generic method for moving entities: Saves last valid position, moves the
	 * entity on its current trajectory, ticks the graphics component, moves the
	 * hitBox.
	 */
	public void tick() {
		xLast = getX(); // last location was valid or it would have been undone already
		yLast = getY();
		if(HP>0)
			getInput(); // get input from AI or controls or whatever
		super.tick();
		if(HP>0)
			walk();
		if(HP<0 &&!graphicsComponent.isAnimating()){
			graphicsComponent = new Sprite(this.name, name+"Dead");
		}
	}

	public void walk() {
		if (xMoveBy != 0 || yMoveBy != 0) { // sets the physicsComponent moving
			physicsComponent.moveTo(getX() + xMoveBy, getY() + yMoveBy);
			currentLoot = null;
			if (destination == null || destination.x != xMoveBy + getX() || destination.y != yMoveBy + getY()){
				destination = new Point(xMoveBy + getX(), yMoveBy + getY());
			}
		}

		direction = Globals.getIntDir(physicsComponent.xDest - getX(), physicsComponent.yDest - getY());

		if (physicsComponent.isMoving() && !attacking) { // display animation walk loop.
			graphicsComponent.loopImg(walkRate, "Walk" + Globals.getStringDir(direction));
			currDirection = Globals.getStringDir(direction);
		} else if (!attacking) {
			graphicsComponent.loopImg(.5, "Stand" + currDirection);
		}

		xMoveBy = 0;// Clear these out for the next input cycle.
		yMoveBy = 0;

	}

	/**
	 * Stops movement (for collisions)
	 * 
	 * @param x
	 *            - if true, stop movement in the x direction
	 * @param y
	 *            - if true, stop movement in the y direction
	 */
	public void undoMove(boolean x, boolean y) {
		if (x && y) {
			physicsComponent.updateHitSpace(xLast, yLast);
			physicsComponent.stopMovement();
			xMoveBy = 0;
			yMoveBy = 0;
			destination = null;

		} else if (x) {
			physicsComponent.updateHitSpace(xLast, yLast);
			physicsComponent.stopXMovement();
			yMoveBy *= 5;
			xMoveBy *= 0;
			destination = null;

		} else if (y) {
			physicsComponent.updateHitSpace(xLast, yLast);
			physicsComponent.stopYMovement();
			xMoveBy *= 5;
			yMoveBy *= 0;

			destination = null;

		}

		physicsComponent.move(xMoveBy, yMoveBy);
		physicsComponent.tick();
	}

	/**
	 * Gets next action for this mob, can be AI logic or player input,
	 * subclasses!
	 */
	protected abstract void getInput();

	protected abstract void attack(Mob enemy);


	public Line2D getSight() {
		return sight;
	}

	public void validateSight(boolean valid) {
		validSight = valid;
	}

	public void addPathFinder(World l) {
		p = new PathFinder(l);
	}

	public void damage(int damage) {
		HP -= damage;
		if(HP <=0){
			graphicsComponent.animate(0.9, "Die");
		}
	}

	public int getHP() {
		return HP;
	}
	
	public boolean isAlive(){
		if (HP > 0) 
			return true;
		return false;
	}
	
	public void getInteracted(Mob interactor){
		if(!isAlive()){
			interactor.currentLoot = inv;
		}
	}

}
