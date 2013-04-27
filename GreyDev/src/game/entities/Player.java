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
import game.engine.audio.AudioLoader;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.overlay.InventoryMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.GraphicAttribute;

import com.sun.org.apache.xml.internal.utils.StopParseException;

public class Player extends Mob {

	public Rectangle attRect;
	Point lastClick;

	int lastPos = 0;
	public boolean notPlayed = true;

	/**
	 * Constructor!
	 * 
	 * @param x
	 *            - Starting x location
	 * @param y
	 *            - Starting y location
	 */
	public Player(int x, int y, InventoryMenu m) {
		name = "Tavish";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name + "StandSouth");
		this.physicsComponent = new Tangible(x, y, 35, 35, 2);

		spriteXOff = -graphicsComponent.getWidth() / 2 - 100;
		spriteYOff = -graphicsComponent.getHeight() + 65;
		inv = m;
		this.walkRate = .5;
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
				if (notPlayed) {
					notPlayed = false;
					AudioLoader.playGrouped("footstep");
				}
			} else if (lastPos != 0 && lastPos != 3)
				notPlayed = true;
		}

		if (InputHandler.up.heldDown) {
			xMoveBy = -2;
			yMoveBy = -2;
		}
		if (InputHandler.down.heldDown) {
			xMoveBy = 2;
			yMoveBy = 2;
		}
		if (InputHandler.left.heldDown) {
			xMoveBy = -2;
			yMoveBy = 2;
		}
		if (InputHandler.right.heldDown) {
			xMoveBy = 2;
			yMoveBy = -2;
		}
		if (InputHandler.right.heldDown && InputHandler.up.heldDown) {
			xMoveBy = 0;
			yMoveBy = -2;
		}
		if (InputHandler.right.heldDown && InputHandler.down.heldDown) {
			xMoveBy = 2;
			yMoveBy = 0;
		}
		if (InputHandler.left.heldDown && InputHandler.up.heldDown) {
			xMoveBy = -2;
			yMoveBy = 0;
		}
		if (InputHandler.left.heldDown && InputHandler.down.heldDown) {
			xMoveBy = 0;
			yMoveBy = 2;
		}

		if (InputHandler.leftClick.heldDown && !attacking) {
			Point r = (Point) InputHandler.mouseLoc;
			Point p = Globals.isoToGrid(r.x, r.y);
			lastClick = p;
			attRect = new Rectangle(p.x - 70, p.y - 70, 140, 140);
		} else if (InputHandler.spaceBar.heldDown && !attacking) {
			Rectangle r = this.getPhysicsShape();
			attRect = new Rectangle(r.x - 90, r.y - 90, r.width + 180, r.height + 180);
		} else if (target != null)
			attack((Mob) target);
		else {
			attRect = null;
			attacking = false;
		}

		inv.update();
	}

//	 public void render(Graphics g){
//	 g.setColor(Color.CYAN);
//	 if(lastClick !=null)
//	 g.drawRect(lastClick.x, lastClick.y, 20, 20);
//	 super.render(g);
//	 // Rectangle r = this.getPhysicsShape();
//	 //g.drawRect(r.x, r.y, r.width, r.height);
//	 }

	public void interact(Entity e) {
		if (e instanceof Mob) {
			if (((Mob) e).isAlive() && !playerFriend) {
				attack((Mob) e);
			} else if (!((Mob) e).isAlive()) {
				((Mob)e).getInteracted(this);
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

		if (Globals.distance(new Point(enemy.getX(), enemy.getY()), new Point(getX(), getY())) > 80) {
			return;
		}

		target = enemy;

		physicsComponent.stopMovement();

		double targX = enemy.getPhysicsShape().getCenterX();
		double targY = enemy.getPhysicsShape().getCenterY();
		double x = getPhysicsShape().getCenterX();
		double y = getPhysicsShape().getCenterY();
		this.direction = Globals.getIntDir(targY - y, targX - x); // TODO FIX THIS SO X AND Y AREN'T REVERSE (POST HEARTLAND)
		this.currDirection = Globals.getStringDir(direction);

		this.physicsComponent.stopMovement();
		xMoveBy = 0;
		yMoveBy = 0;
		if (lastPos == 3) {
			attacking = false;
			target = null;
			return;
		}

		if (!attacking) {
			graphicsComponent.loopImg(0.3, "Attack" + currDirection);
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
				//System.out.println("Nat" + chance);
				if (chance + hitMod > 2) {
					damage +=  damMod + Globals.D(6) + Globals.D(6) + Globals.D(6)+ Globals.D(6);
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
