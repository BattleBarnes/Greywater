/** ENTITY CLASS
 * 
 * Used for objects which need physical representation in game (Players, Enemies, Tiles, Walls)
 * 
 * Hold the graphics and collision aspects of the object. ALL objects in game must inherit this class.
 * Acts as a wrapper for two central components- graphics and physics. Instead of the main game loop having to
 * track two objects per character, it only has to track one, and by calling it's tick/update and render methods
 * it tracks the two disparate aspects (graphics and physics) and makes sure shit gets done. This is much easier.
 * 
 * 
 *  
 * ***********  IMPORTANT ************************
 * The graphics component (sprite) is an isometric image, it is a skewed square, rotated 45 degrees.
 * An iso-tile has width:height of 2:1, giving it the illusion of depth. Unfortunately, that makes programming
 * super hard, and I am lazy. Far more powerful and understandable is to represent the tiles as square in memory, as if
 * the game were a top-down 2D style game. This means that all objects are squares or rectangles, which makes the math easy
 * and render sorting easier. The 2D is represented through the physics component (hitbox) and the iso aspect is the graphics component.
 * The middleman, which holds these two, is the Entity (or subclass thereof, such as this Tile) which makes them play nice, see below.
 * 
 * Only when an item is being rendered is it treated as isometric - the square co-ordinates are run through the math method - 
 * getIsoCoords() and converted from boring 2D to exciting, sexy isometric co-ordinates. Because all images undergo
 * the same transformation, the isometric image is a fair and accurate representation of the 2D "true" world in memory.
 * 
 * This class is that middleman that takes the 2D square world (flatspace) and uses fancible math things
 * to make its x and y data into isometric fancy x and y data, which is then used to control rendering by the graphics component.
 * 
 * Inspiration for this system is properly due to Roger Englebert ("From Tile to Isometric"/"Do the Isometric Dance")
 * http://www.rengelbert.com/tutorial.php?id=76&show_all=true
 * Though the implementation is custom made, not a copy.
 * 
 * 
 * 
 * @author Jeremy Barnes Dec 10/12
 * SQUARE-ISO PARADIGM ADDED FEB 06/13
 */

package game.entities.components;

import game.Globals;
import game.engine.Camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

	/* **** All entities have a physics component for collisions and position, and a graphics component to render on screen *** */
	protected Tangible physicsComponent; // hitbox
	protected Sprite graphicsComponent; // sprite

	/* *** Position is determined by the hitbox, so offsets are tracked to draw in relation to the hitbox. Subclasses must set. ** */
	protected int spriteXOff; // horizontal distance from (0,0) on hitbox to (0,0) on sprite
	protected int spriteYOff; // vertical distance from (0,0) on hitbox to (0,0) on sprite

	/**
	 * Ticks components (graphics and physics)
	 */
	public void tick() {
		physicsComponent.tick(); // update components
		graphicsComponent.tick();
	}

	/**
	 * Draws the current sprite for this entity.
	 * @param g - Graphics object
	 */
	public void render(Graphics g) {
		Rectangle2D r = this.getPhysicsShape();

		if (Globals.DEVMODE != 1) {
			g.setColor(Color.PINK);
			g.drawRect((int) r.getX() - 500, (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
		}
	
		if(Globals.DEVMODE > 0){
			Point2D p = Globals.getIsoCoords(getX() + spriteXOff, getY() + spriteYOff);
			graphicsComponent.render(g, (int) Math.round(p.getX() - Camera.xOffset), (int)Math.round(p.getY() - Camera.yOffset));
		}
	}
	
	/**
	 * @return the physicsComponent for filthy outsiders
	 */
	public Tangible getPhysics() {
		return physicsComponent;
	}

	/**
	 * @return the Shape used for collision detection
	 */
	public Rectangle2D getPhysicsShape() {
		return physicsComponent.getHitBox();
	}

	public double getX() {
		return physicsComponent.getHitBox().getX();
	}

	public double getY() {
		return physicsComponent.getHitBox().getY();
	}
	
	public Point2D getLocation(){
		return new Point2D.Double(getX(), getY());
	}

	/**
	 * @return The approximate depth in Z space of the entity. Used for render sorting.
	 */
	public double getDepth() {
		double x = physicsComponent.getHitBox().getCenterX();
		double y = physicsComponent.getHitBox().getCenterY();
		return (x + y) * .866;
	}
}
