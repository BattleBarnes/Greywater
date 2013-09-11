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
import game.engine.audio.AudioLoader;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.overlay.InventoryMenu;
import game.world.Tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class Player extends Mob {

	public Rectangle2D attRect;
	Point2D lastClick = new Point2D.Double();

	int lastPos = 0;
	public boolean notPlayed = true;
	long lasttime = 0;
	long nowtime = 0;
	long tdiff = 0;

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
		this.physicsComponent = new Tangible(x, y,  35, 35, 1.);

		spriteXOff = (int) (-graphicsComponent.getWidth() / 2 - 65 - 35) ;
		spriteYOff = (int) (-graphicsComponent.getHeight() + 30 + 35);
		inv = m;
		this.walkRate = 1;
	}

	/**
	 * Gets player input from the InputHandler class (static calls).
	 * Uses direction method from Globals.java to determine which way player is facing (NSEW...)
	 * and sets the graphics component accordingly.
	 */
	protected void getInput() {
		damage(-inv.buff);
		inv.buff = 0;

		if (graphicsComponent.isAnimating() && !attacking && physicsComponent.isMoving()) {
			lastPos = graphicsComponent.seriesPosition;
			if (lastPos == 0 || lastPos == 3) {
				lasttime = System.currentTimeMillis();
				if (notPlayed) {
					notPlayed = false;
					AudioLoader.playGrouped("footstep");
				}
			} else if (lastPos != 0 && lastPos != 3)
				notPlayed = true;
		}

		if (InputHandler.leftClick.heldDown && !attacking) {
			Point2D r =  InputHandler.leftClick.location;
			Point2D p = Globals.isoToGrid(r.getX(), r.getY());
			Point p2 = Globals.findTile(p.getX(), p.getY());
			
			attRect = new Rectangle2D.Double(p.getX() - 90, p.getY() - 90, 180, 180);
			this.physicsComponent.moveTo(p.getX(), p.getY());
			
		} else if (InputHandler.spaceBar.heldDown && !attacking) {// if spacebar attack
			Rectangle2D r = this.getPhysicsShape();
			attRect = new Rectangle2D.Double(r.getX() - 90, r.getY() - 90, r.getWidth() + 180, r.getHeight() + 180);
			
		} else if (target != null) // if there is a target assigned, kill it
			attack((Mob) target);
		else { // otherwise, not attacking
			attRect = null;
			attacking = false;
		}

		inv.update();
	}

	public void render(Graphics g) {
		if (Globals.DEVMODE >= 1){
			super.render(g);
		}else {
			g.setColor(Color.RED);
			Rectangle2D r = this.getPhysicsShape();
			g.drawRect((int) r.getX() - 500, (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());

		}
	}

	public void interact(Entity e) {
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
	}

	@Override
	public void attack(Mob enemy) {
		if (enemy == null && target == null)
			return;
		if (target != null)
			enemy = (Mob) target;

		if (Globals.distance(new Point2D.Double(enemy.getX(), enemy.getY()), new Point2D.Double(getX(), getY())) > 80) {
			return;
		}

		target = enemy;

		physicsComponent.stopMovement();

		double targX = enemy.getPhysicsShape().getCenterX();
		double targY = enemy.getPhysicsShape().getCenterY();
		double x = getPhysicsShape().getCenterX();
		double y = getPhysicsShape().getCenterY();
		this.direction = Globals.getIntDir(targX - x, targY - y);
		this.currDirection = Globals.getStringDir(direction);

		this.physicsComponent.stopMovement();

		if (lastPos == 3) {
			attacking = false;
			target = null;
			return;
		}

		if (!attacking) {
			graphicsComponent.loopImg(.3, "Attack" + currDirection);
			attacking = true;
			attRect = null;
			notPlayed = true;
		} else {
			if (lastPos == 2 && graphicsComponent.seriesPosition == 0) { // if ending (last frame)
				attacking = false;
				target = null;
				lastPos = 0;

				int damage = 0;
				int hitMod = 0;
				int damMod = 0;
				if (inv.getWeap() != null) {
					hitMod += inv.getWeap().getHitMod();
					damMod += inv.getWeap().getDamageMod();
				}
				int chance = Globals.D(20);
				// System.out.println("Nat" + chance);
				if (chance + hitMod > 2) {
					damage += damMod + Globals.D(6) + Globals.D(6) + Globals.D(6) + Globals.D(6);
					enemy.damage(damage);
				}

				return;
			}
			lastPos = graphicsComponent.seriesPosition;

			if (lastPos == 1) {
				if (notPlayed) {
					notPlayed = false;
					AudioLoader.playGrouped("swing");
				}
			} else if (lastPos == 0 || lastPos == 2)
				notPlayed = true;

		}
	}

	public InventoryMenu getInv() {
		return inv;
	}

}
