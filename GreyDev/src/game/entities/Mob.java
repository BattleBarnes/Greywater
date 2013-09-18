package game.entities;

import game.Globals;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.ai.PathFinder;
import game.overlay.InventoryMenu;
import game.world.World;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public abstract class Mob extends Entity {

	public int direction; // current direction (N,S,E,W....) see Globals.java

	protected String currDirection = "South";
	protected String name;
	public Entity target;
	// public Point destination;
	boolean walks = true;
	public boolean playerFriend;

	PathFinder p;

	protected int HP = 100;
	protected double walkRate = 1; // used to determine how fast a mob's walk
									// animation cycle is
	protected Line2D sight;
	protected boolean validSight;
	public int sightRange;

	/* **** POSITIONING VARIABLES ***** */
	public double xLast; // last valid (non colliding) x position
	public double yLast; // last valid (non colliding) y position
	boolean attacking = false;
	protected InventoryMenu inv;
	public InventoryMenu currentLoot;
	protected World world;

	public void init(World w) {
		world = w;
	}

	/**
	 * Generic method for moving entities: Saves last valid position, moves the
	 * entity on its current trajectory, ticks the graphics component, moves the
	 * hitBox.
	 */
	public void tick() {

		xLast = getX(); // last location was valid or it would have been undone already
		yLast = getY();
		if (HP > 0)
			getInput(); // get input from AI or controls or whatever
		super.tick();
		if (!validateNextPos(getPhysicsShape())) {
			System.out.println("Colliding!");
			double width = getPhysicsShape().getWidth();
			double height = getPhysicsShape().getHeight();

			Rectangle2D r = new Rectangle2D.Double(xLast, getY(), width, height);
			if (!validateNextPos(r)) { // if xLast didn't fix it, y is the problem
				physicsComponent.destination.setLocation(physicsComponent.destination.getX(), yLast);
				physicsComponent.position.y = yLast;
			}
			r = new Rectangle2D.Double(getX(), yLast, width, height);
			if (!validateNextPos(r)){ //if yLast didn't fix it, x is the problem
				physicsComponent.destination.setLocation(xLast, physicsComponent.destination.getX());
				physicsComponent.position.x = xLast;
			}
		}

		if (!physicsComponent.isMoving())
			physicsComponent.stopMovement();

		if (HP > 0)
			walk();

		if (HP <= 0 && !graphicsComponent.isAnimating()) {
			graphicsComponent = new Sprite(this.name, name + "Dead");
		}

	}

	/**
	 * Dismisses any open loot windows, finds the direction of movement,
	 * shows the walk animation
	 */
	public void walk() {

		// direction = Globals.getIntDir(physicsComponent.xDest - getX(), physicsComponent.yDest - getY());
		//direction = Globals.getIntDir(physicsComponent.destination.getX() - getX(), physicsComponent.destination.getY() - getY());
		direction = Globals.getIntDir(physicsComponent.direction.getX(), physicsComponent.direction.getY());
		if (physicsComponent.isMoving() && !attacking) { // display animation walk loop.
			graphicsComponent.loopImg(walkRate, "Walk" + Globals.getStringDir(direction));
			currDirection = Globals.getStringDir(direction);
		} else if (!attacking) {
			graphicsComponent.loopImg(.5, "Stand" + currDirection);
		}

	}

	/**
	 * Stops movement (for collisions)
	 * 
	 * @param x - if true, stop movement in the x direction
	 * @param y - if true, stop movement in the y direction
	 */
	public void undoMove(boolean x, boolean y) {
		if (x && y) {
			physicsComponent.updateHitSpace(xLast, yLast);
			physicsComponent.stopMovement();

		} else if (x) {
			physicsComponent.updateHitSpace(xLast, yLast);
			physicsComponent.stopXMovement();

		} else if (y) {
			physicsComponent.updateHitSpace(xLast, yLast);
			physicsComponent.stopYMovement();
		}

		physicsComponent.tick();
	}

	/**
	 * Gets next action for this mob, can be AI logic or player input,
	 * subclasses!
	 */
	protected abstract void getInput();

	protected abstract void attack(Mob enemy);

	/**
	 * @return a line connecting the entity with the player
	 */
	public Line2D getSight() {
		return sight;
	}

	/**
	 * Determines whether or not the Mob has a valid sightline (exists, is not longer than sightRange, and doesn't collide with walls)
	 * Sets validSight to whether or not the sight is valid.
	 */
	public void validateSight() {
		if(sight != null && Globals.distance(sight.getP1(), sight.getP2()) < this.sightRange && !world.checkWorldCollision(sight))
			validSight = true;
		validSight = false;
	}

	public void addPathFinder(World l) {
		p = new PathFinder(l);
	}

	/**
	 * Change the Mob's HP by the given amount.
	 * 
	 * @param damage - how much to change mob hp by
	 */
	public void damage(int damage) {
		HP -= damage;
		if (HP <= 0) {
			graphicsComponent.animate(0.9, "Die");
		}
	}

	public int getHP() {
		return HP;
	}

	/**
	 * @return True if HP > 0
	 */
	public boolean isAlive() {
		if (HP > 0)
			return true;
		return false;
	}

	/**
	 * Allows another mob to interact with this mob.
	 * 
	 * @param interactor - caller
	 */
	public void getInteracted(Mob interactor) {
		if (!isAlive()) {
			interactor.currentLoot = inv;
		}
	}

	/**
	 * 
	 * @return whether or not the current position is valid (not colliding with the world)
	 *         (True = not colliding, valid; False =  colliding, invalid)
	 */
	public boolean validateNextPos(Rectangle2D hitbox) {
		return !world.checkWorldCollision(hitbox);
	}

}
