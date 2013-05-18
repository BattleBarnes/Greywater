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
		this.graphicsComponent = new Sprite(name, name+"South");
		this.physicsComponent = new Tangible(x, y, 30, 30, 2);

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
	//	Rectangle r = this.physicsComponent.getPhysicsShape();
		//2200, 250,
	//	if(r.x > 2000 && r.x < 2600 && r.y < 200){
	//		Globals.state = State.gameWon;
	//	}

		if (xMoveBy != 0 || yMoveBy != 0 && validSight) { // sets the physicsComponent moving
			physicsComponent.moveTo(getX() + xMoveBy, getY() + yMoveBy);

			
			direction = Globals.getIntDir(target.getX() - getX()*1.0, 1.0*target.getY() - getY());

			if (physicsComponent.isMoving() && !attacking) { // display animation walk loop.
				graphicsComponent.loopImg(walkRate, Globals.getStringDir(direction));
				currDirection = Globals.getStringDir(direction);
			}
		}
		//System.out.println("SweepyData: XMB: " + xMoveBy +" YMB: " +yMoveBy + " StrDir: " +currDirection);
		xMoveBy = 0;// Clear these out for the next input cycle.
		yMoveBy = 0;

	}


	@Override
	protected void getInput() {
		
		xMoveBy = target.getX() - this.getX();
		yMoveBy = target.getY() - this.getY();
	
//		if(Math.abs(xMoveBy) < 2*getPhysicsShape().width && Math.abs(yMoveBy) < 2*getPhysicsShape().width){
//			this.physicsComponent.stopMovement();
//			xMoveBy = 0;
//			yMoveBy = 0;
//		}

		//System.out.println("SweepyData GETINPUT: XMB: " + xMoveBy +" YMB: " +yMoveBy + " StrDir: " +currDirection);
		

			
	}

	@Override
	protected void attack(Mob enemy) {
		return;
	}


}
