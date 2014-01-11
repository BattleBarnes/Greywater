package game.entities;

import game.Globals;
import game.engine.State;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Sweepy extends Mob {

	public Sweepy(double x, double y, Player p) {
		name = "Sweepy";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name+"South");
		this.physicsComponent = new Tangible(x, y, 30, 30, 2);
		System.out.println("Sweep Loc " + x + " " +  y);

		spriteXOff = 0;
		spriteYOff = 0;
		super.walkRate = .7;
		target = p;
		sightRange = 1000;
		walks = false;
		playerFriend = true;
		this.HP = 999999999;
	}
	
	@Override
	public void walk() {
		Rectangle2D r = this.physicsComponent.getHitBox();
		//2200, 250,
		if(r.getX() > 2000 && r.getX() < 2600 && r.getY() < 200){
			Globals.state = State.gameWon;
		}

	

			
			direction = Globals.getIntDir(physicsComponent.destination.getX() - getX(), physicsComponent.destination.getY() - getY());

			if (physicsComponent.isMoving() && !attacking) { // display animation walk loop.
				graphicsComponent.loopImg(walkRate, Globals.getStringDir(direction));
				currDirection = Globals.getStringDir(direction);
			}



	}


	@Override
	protected void getInput() {
		sight = new Line2D.Double(this.getLocation(), target.getLocation());
		validateSight();
	//	physicsComponent.destination.setLocation(target.getX(), target.getY());
		if(!pathFinder.hasPath() && target.getLocation().distance(this.getLocation()) > 100 && validSight){
			pathFinder.setNewPath(this.getLocation(), target.getLocation());
			System.out.println(name + " began pathfinding...");
		}
		else if (!physicsComponent.isMoving()) {
			Point2D newPoint = pathFinder.getNextLoc();
			if (newPoint != null) {
				physicsComponent.destination = newPoint;
				physicsComponent.moveTo(newPoint.getX(), newPoint.getY());
			}

		}
	}

	@Override
	protected void attack(Mob enemy) {
		return;
	}

	
	@Override
	public boolean interact() {
		return false;
	}


}
