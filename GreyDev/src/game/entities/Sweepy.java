package game.entities;

import java.awt.Point;

import game.Globals;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

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
		sightRange = 100000000;
		walks = false;
	}
	
	@Override
	public void walk() {

		if (xMoveBy != 0 || yMoveBy != 0) { // sets the physicsComponent moving
			physicsComponent.moveTo(getX() + xMoveBy, getY() + yMoveBy);
			if (destination == null || destination.x != xMoveBy + getX() || destination.y != yMoveBy + getY())
				destination = new Point(xMoveBy + getX(), yMoveBy + getY());
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
		if(Math.abs(xMoveBy) < 15 && Math.abs(yMoveBy) < 15){
			
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
