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
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.overlay.InventoryMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Player extends Mob {

	public Rectangle2D attRect;
	Point2D lastClick = new Point2D.Double();

	int lastPos = 0;
	public boolean notPlayed = true;

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

		spriteXOff = (int) (-graphicsComponent.getWidth() / 2 - 65 - 35);
		spriteYOff = (int) (-graphicsComponent.getHeight() + 30 + 35);
		inv = m;
		this.walkRate = 1;

	}

	/**
	 * Gets player input from the InputHandler class (static calls). Uses direction method from Globals.java to determine which way player is facing
	 * (NSEW...) and sets the graphics component accordingly.
	 */
	protected void getInput() {
		if (-inv.buff != 0)
			damage(-inv.buff);
		inv.buff = 0;

		if (InputHandler.leftClick.heldDown) {
			Point2D clickLoc = InputHandler.leftClick.location;
			Point2D gridLoc = Globals.isoToGrid(clickLoc.getX(), clickLoc.getY());
			attRect = new Rectangle2D.Double(clickLoc.getX() - 15, clickLoc.getY() - 15, 30, 30);

			if (interact())
				return;

			pathFinder.setNewPath(new Point2D.Double(getX(), getY()), gridLoc);
			System.out.println(name + " began pathfinding...");
			InputHandler.leftClick.heldDown = false;
			Point2D newPoint = pathFinder.getNextLoc();
			newPoint = pathFinder.getNextLoc();
			if (newPoint != null) {
				physicsComponent.destination = newPoint;
				physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
			}

		} else {
			if (!physicsComponent.isMoving()) {
				Point2D newPoint = pathFinder.getNextLoc();
				if (newPoint != null) {
					physicsComponent.destination = newPoint;
					physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
				}
			}
			//attacking = false;
		}

		attRect = null;
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
		if (attRect == null)
			return false;
		Mob e = (Mob) world.getEntityCollision(attRect, this);

		if (e instanceof Mob) {
			if (((Mob) e).isAlive() && !playerFriend) {
				attack((Mob) e);
			} else if (!((Mob) e).isAlive()) {
				((Mob) e).getInteracted(this);
			} else if (playerFriend) {
				// talk or (sweepy).getloot
			}

		} else {
			// interact
		}
		if (e != null)
			return true;
		else
			return false;
	}

	@Override
	public void attack(Mob enemy) {

		if (enemy == null || attacking)// && target == null)
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
		this.physicsComponent.stopMovement();

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
			damage += damMod + Globals.D(6) + Globals.D(6) + Globals.D(6) + Globals.D(6);
			enemy.damage(damage);
			System.out.println(name + " hit " + enemy.name + " for " + damage + " damage...");
		}

	}

	public InventoryMenu getInv() {
		return inv;
	}

}
