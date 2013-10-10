package game.entities;

import game.Globals;
import game.engine.audio.AudioLoader;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.entities.components.ai.PathFinder;
import game.overlay.AIInventory;
import game.world.World;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Watchman extends Mob {

	int lastPos;

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
		playerFriend = false;
		inv = new AIInventory();
	}

	@Override
	protected void getInput() {
		sight = new Line2D.Double(target.getX(), target.getY(), getX(), getY());

		if (Globals.distance(new Point2D.Double(getX(), getY()), new Point2D.Double(target.getX(), target.getY())) < 90 && validSight && ((Mob) target).isAlive()) {
			attack((Mob) target);
			return;
		}

		if (Globals.distance(new Point2D.Double(getX(), getY()), new Point2D.Double(target.getX(), target.getY())) < 500 && HP > 0) {
			AudioLoader.playSingle("click", false);
		}
		// else
		// AudioLoader.stopSingle("click");

		attacking = false;

		pathFind();
	}

	@Override
	protected void attack(Mob enemy) {
		double targX = enemy.getPhysicsShape().getCenterX();
		double targY = enemy.getPhysicsShape().getCenterY();
		double x = getPhysicsShape().getCenterX();
		double y = getPhysicsShape().getCenterY();

		this.physicsComponent.stopMovement();

		if (graphicsComponent.isAnimating()) {
			lastPos = graphicsComponent.seriesPosition;
			if (lastPos == 2) {
				if (attacking) {
					attacking = false;
					int chance = Globals.D(20);
					if (chance > 8) {
						int dmg = 2 * Globals.D(6);
						enemy.damage(dmg);

					}
				}
			} else if (lastPos != 3) {
				attacking = true;
				graphicsComponent.loopImg(0.8, "Attack" + currDirection);
			}
		}

		this.direction = Globals.getIntDir((targX - x), (targY - y));
		this.currDirection = Globals.getStringDir(direction);
	}

	private void pathFind() {

		if (((physicsComponent.destination == null && validSight) || (Globals.distance(sight.getP1(), sight.getP2()) < 300)) && System.nanoTime() % 47 == 0) {
			// System.out.println("Pathfind");
			pathFinder.setNewPath(new Point2D.Double(getX(), getY()), new Point2D.Double(target.getX(), target.getY()));
			Point2D newPoint = pathFinder.getNextLoc();
			if (newPoint != null)
				physicsComponent.destination = newPoint;
		}
		if (physicsComponent.destination != null && getX() == physicsComponent.destination.getX() && getY() == physicsComponent.destination.getY()) {
			Point2D newPoint = pathFinder.getNextLoc();
			if (newPoint != null)
				physicsComponent.destination = newPoint;

		}
		if (physicsComponent.destination == null && validSight) {
			// System.out.println("Pathfind");
			pathFinder.setNewPath(new Point2D.Double(getX(), getY()), new Point2D.Double(target.getX(), target.getY()));
			return;
		}
		if (physicsComponent.destination == null && !validSight) {
			physicsComponent.destination.setLocation(getX(), getY());
		}

	}

	public void damage(int dmg) {
		super.damage(dmg);
		AudioLoader.playSingle("hit", false);
	}

}
