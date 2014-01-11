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
import game.engine.InputHandler;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.overlay.InventoryMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Player extends Mob {

	public Rectangle2D attRect;

	int lastPos = 0;
	public boolean notPlayed = true;
	public Sprite selection;

	/**
	 * Constructor!
	 * 
	 * @param x - Starting x location
	 * @param y - Starting y location
	 */
	public Player(int x, int y, InventoryMenu m) {
		name = "Tavish";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name + "StandSouth");
		x = (int) (x * Globals.tileWidth);
		y = (int) (y * Globals.tileHeight);

		this.physicsComponent = new Tangible(x, y, 35, 35, 1.);
		selection = new Sprite("highlight", "highlight");
		spriteXOff = (int) (-graphicsComponent.getWidth() / 2 - 65 - 35);
		spriteYOff = (int) (-graphicsComponent.getHeight() + 30 + 35);
		inv = m;
		this.walkRate = 1;
		playerFriend = false;
	}

	/**
	 * Gets player input from the InputHandler class (static calls). Uses direction method from Globals.java to determine which way player is facing
	 * (NSEW...) and sets the graphics component accordingly.
	 */
	protected void getInput() {
		if (-inv.buff != 0)
			damage(-inv.buff);
		inv.buff = 0;

		//if left clicking pathfind and establish attack-collision box.
		if (InputHandler.leftClick.heldDown) {
			Point2D clickLoc = InputHandler.leftClick.location;
			Point2D gridLoc = Globals.isoToGrid(clickLoc.getX(), clickLoc.getY());
			attRect = new Rectangle2D.Double(clickLoc.getX() - 15, clickLoc.getY() - 15, 30, 30);

			if (interact()) //if the attack-collision box hits a mob, don't pathfind.
				return;

			pathFinder.setNewPath(getLocation(), gridLoc); //otherwise, walk to the click
			System.out.println(name + " began pathfinding...");
			InputHandler.leftClick.heldDown = false;
			Point2D newPoint = pathFinder.getNextLoc();
			newPoint = pathFinder.getNextLoc(); //first point is where the player is standing now, ignore it
			if (newPoint != null) {
				physicsComponent.destination = newPoint;
				physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
			}

		} else { //if no click, go to next location on path.
			if (!physicsComponent.isMoving()) {
				Point2D newPoint = pathFinder.getNextLoc();
				if (newPoint != null) {
				//	physicsComponent.destination = newPoint;
					physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
				}
			}
		}

		attRect = null; //remove attack rectangle so that attacks don't persist forever
		inv.update();
	}

	public void render(Graphics g) {
		if (Globals.DEVMODE >= 1) {
			super.render(g);
		} else {
			g.setColor(Color.RED);
			Rectangle2D r = this.getPhysicsShape();
			g.drawRect((int) r.getX() - 500, (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());

		}
	}

	@Override
	public boolean interact() {
		if (attRect == null) //if no interaction box, no need to look.
			return false;
		Mob e = (Mob) world.getEntityCollision(attRect, this); //find the entity we clicked
		if (e instanceof Mob) { //if we clicked a Mob
			if (((Mob) e).isAlive() && !playerFriend) {//if its alive and an enemy
				attack((Mob) e); //kill!
				target = e;
			} else if (!((Mob) e).isAlive() && e.getLocation().distance(getLocation()) < 175) { //if it's dead, loot it.
				((Mob) e).getInteracted(this);
				target = null;
			} else if (playerFriend) {//sweepy interaction goes here.
				// talk or (sweepy).getloot
			}else{
				target = null;
				return false;
			}

		} else { //interact with walls/tomes/floors (non mob entities)
			// interact
			target = null;
		}
		if (e != null) //report whether or not we interacted with anything
			return true;
		else
			return false;
	}

	@Override
	public void attack(Mob enemy) {

		if (enemy == null || attacking)
			return;

		//if too far away to attack
		if (Globals.distance(new Point2D.Double(enemy.getX(), enemy.getY()), new Point2D.Double(getX(), getY())) > 80) {

			if (enemy != null) {
				System.out.print("Tavish was too far away from " + enemy.name + " (" + this.getLocation().distance(enemy.getLocation()) + ") ... ");
				pathFinder.setNewPath(this.getLocation(), enemy.getLocation());
				Point2D newPoint = pathFinder.getNextLoc();
				newPoint = pathFinder.getNextLoc();
				if (newPoint != null) {
					physicsComponent.destination = newPoint;
					physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
				}
				System.out.println();
				System.out.println(newPoint + " " + this.getLocation());
			}
			InputHandler.leftClick.heldDown = false;
			return;
		}

		System.out.println(name + " attacked " + ((Mob) enemy).name);

		physicsComponent.stopMovement();

		double targX = enemy.getPhysicsShape().getCenterX();
		double targY = enemy.getPhysicsShape().getCenterY();
		double x = getPhysicsShape().getCenterX();
		double y = getPhysicsShape().getCenterY();
		this.direction = Globals.getIntDir(targX - x, targY - y);
		this.currDirection = Globals.getStringDir(direction);
		attacking = true;

		int damage = 0;
		int hitMod = 0;
		int damMod = 0;
		if (inv.getWeap() != null) {
			hitMod += inv.getWeap().getHitMod();
			damMod += inv.getWeap().getDamageMod();
		}
		int chance = Globals.D(20);
		System.out.println(this.name + " rolled " + chance + " to hit " + enemy.name);
		if (chance + hitMod > 2) {
			damage += damMod + Globals.D(6) + Globals.D(6);
			enemy.damage(damage);
			System.out.println(name + " hit " + enemy.name + " for " + damage + " damage...");
		}

	}

	public InventoryMenu getInv() {
		return inv;
	}

	@Override
	public Rectangle2D getAttbox() {
		Point2D p = Globals.getIsoCoords(getX() + spriteXOff, getY() + spriteYOff);
		return new Rectangle2D.Double((int) Math.round(p.getX()) + 70, (int) Math.round(p.getY()) + 70, graphicsComponent.getWidth() -140, graphicsComponent.getHeight() - 70);
	}
}
