package game.entities;

import game.Globals;
import game.engine.State;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.Point;
import java.awt.Rectangle;

public class Sweepy extends Mob {

	public Sweepy(int x, int y, Player p) {
		name = "Sweepy";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name);
		this.physicsComponent = new Tangible(x, y, 30, 30, 3);

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
		Rectangle r = this.physicsComponent.getPhysicsShape();
		//2200, 250,
		if(r.x > 2000 && r.x < 2600 && r.y < 200){
			Globals.state = State.gameWon;
		}

		if (xMoveBy != 0 || yMoveBy != 0 && validSight) { // sets the physicsComponent moving
			physicsComponent.moveTo(getX() + xMoveBy, getY() + yMoveBy);
			if (destination == null || destination.x != xMoveBy + getX() || destination.y != yMoveBy + getY())
				destination = new Point(xMoveBy + getX(), yMoveBy + getY());
			
			direction = Globals.getIntDir(physicsComponent.xDest - getX(), physicsComponent.yDest - getY());

			if (physicsComponent.isMoving() && !attacking) { // display animation walk loop.
				graphicsComponent.loopImg(walkRate, Globals.getStringDir(direction));
				currDirection = Globals.getStringDir(direction);
			}
		}

		xMoveBy = 0;// Clear these out for the next input cycle.
		yMoveBy = 0;

	}


	@Override
	protected void getInput() {
	//	if (System.nanoTime() % 11 == 0) {
			xMoveBy = target.getX() - this.getX();
			yMoveBy = target.getY() - this.getY();
	//	}
		if(Math.abs(xMoveBy) < 35 && Math.abs(yMoveBy) < 35){
			
			this.physicsComponent.stopMovement();
			xMoveBy = 0;
			yMoveBy = 0;
		}
			
	}

	@Override
	protected void attack(Mob enemy) {
		return;
	}


}
