package game.entities;

import game.Globals;
import game.engine.Camera;
import game.engine.audio.SoundLoader;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.overlay.AIInventory;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Watchman extends Mob {

	int lastPos;
	public Rectangle2D attBox;

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

		Thread finder;

		finder = new Thread(new Runnable() {
			public void run() {
				

				sight = new Line2D.Double(target.getX(), target.getY(), getX(), getY());
				validateSight();

				if (Globals.distance(new Point2D.Double(getX(), getY()), new Point2D.Double(target.getX(), target.getY())) < 90 && validSight && ((Mob) target).isAlive()) {
					attack((Mob) target);
					System.out.println(name + " attacked " + ((Mob)target).name);
					return;
				}

				attacking = false;

				pathFind();

			}
		});
		finder.setPriority(Thread.MIN_PRIORITY);
		finder.start();
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

		if (((physicsComponent.destination.distance(this.getLocation())<10 && validSight) || ((Globals.distance(sight.getP1(), sight.getP2()) < 300)) && System.nanoTime() % 47 == 0) ){
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
			pathFinder.setNewPath(new Point2D.Double(getX(), getY()), new Point2D.Double(target.getX(), target.getY()));
			return;
		}
		if (physicsComponent.destination == null && !validSight) {
			physicsComponent.destination.setLocation(getX(), getY());
		}

	}

	public void damage(int dmg) {
		super.damage(dmg);
		Random r = new Random();
		int num = r.nextInt(3)+1;
		SoundLoader.playSingle("Wrench"+num);
	}

	public boolean interact() {
		return false;
	}

	public Rectangle2D getAttbox(){
		Point2D p = Globals.getIsoCoords(getX() + spriteXOff, getY() + spriteYOff);
		return new Rectangle2D.Double((int) Math.round(p.getX() - Camera.xOffset),(int)Math.round(p.getY() - Camera.yOffset), graphicsComponent.getWidth(), graphicsComponent.getHeight() );
	}
}
