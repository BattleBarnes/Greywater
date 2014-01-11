package game.entities;

import game.Globals;
import game.engine.Camera;
import game.engine.audio.SoundLoader;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.overlay.AIInventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Watchman extends Mob {

	public Watchman(double x, double y, Player p) {
		name = "Watchman";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name + "StandSouth");
		this.physicsComponent = new Tangible(x, y, 20, 20, 1);

		spriteXOff = -graphicsComponent.getWidth() / 2 - 65;
		spriteYOff = -graphicsComponent.getHeight() + 65;
		super.walkRate = .7;
		target = p;
		sightRange = 700;
		sight = new Line2D.Double(target.getX(), target.getY(), getX(), getY());
		playerFriend = false;
		inv = new AIInventory();
	}

	@Override
	protected void getInput() {

		Thread finder;

		finder = new Thread(new Runnable() {
			public void run() {

				sight.setLine(target.getX(), target.getY(), getX(), getY());
				validateSight();

				if (interact()) {
					return;
				}
			

				if (!pathFinder.hasPath() && (validSight || target.getLocation().distance(getLocation()) < 300) &&  System.nanoTime() % 47 == 0) {
					pathFinder.setNewPath(getLocation(), target.getLocation());
					
					Point2D newPoint = pathFinder.getNextLoc();
					newPoint = pathFinder.getNextLoc(); // first point is where the watchman is standing now, ignore it
					if (newPoint != null) {
						physicsComponent.destination = newPoint;
						physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
						return;
					}
				} else if (!physicsComponent.isMoving()) {
					Point2D newPoint = pathFinder.getNextLoc();
					
					if (newPoint != null) 
						physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
				}
				if (physicsComponent.destination == null && !validSight) {
					physicsComponent.destination.setLocation(getX(), getY());
				}

			}
		});
		finder.setPriority(Thread.MIN_PRIORITY);
		finder.start();
	}

	@Override
	protected void attack(Mob enemy) {

		if (enemy == null || attacking)
			return;
		pathFinder.clearPath();
		System.out.println(name + " attacked " + (enemy).name);

		physicsComponent.stopMovement();

		double targX = enemy.getPhysicsShape().getCenterX();
		double targY = enemy.getPhysicsShape().getCenterY();
		double x = getPhysicsShape().getCenterX();
		double y = getPhysicsShape().getCenterY();
		this.direction = Globals.getIntDir(targX - x, targY - y);
		this.currDirection = Globals.getStringDir(direction);
		attacking = true;

		int damage = 0;

		int chance = Globals.D(20);
		System.out.println(this.name + " rolled " + chance + " to hit " + enemy.name);
		if (chance > 8) {
			damage += Globals.D(6);
			enemy.damage(damage);
			System.out.println(name + " hit " + enemy.name + " for " + damage + " damage...");
		}
	}

	/**
	 * Take damage
	 */
	public void damage(int dmg) {
		super.damage(dmg);
		Random r = new Random();
		int num = r.nextInt(3) + 1;
		SoundLoader.playSingle("Wrench" + num);
	}

	@Override
	public boolean interact() {
		// Point2D p = Globals.getIsoCoords(getX() + spriteXOff, getY() + spriteYOff);
		if (target.getLocation().distance(getLocation()) < 100 && ((Mob) target).isAlive()) {
			attack((Mob) target);
			return true;
		}

		// Mob e = (Mob) world.getEntityCollision(new Rectangle2D.Double(p.getX() + 60, p.getY() + 60, graphicsComponent.getWidth() - 60,
		// graphicsComponent.getHeight() - 60), this);
		// Mob e = (Mob) world.getEntityCollision(getAttbox(), this);

		// if (e instanceof Player) { // if we clicked the Player
		// if (((Mob) e).isAlive()) // if its alive
		// attack((Mob) e); // kill!
		// return true;
		// } else { // interact with walls/tomes/floors (non mob entities)
		// // interact
		// }
		// // if (e != null) // report whether or not we interacted with anything
		// return true;
		// else
		return false;
	}

	
}
