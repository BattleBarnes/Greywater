package game.entities;

import game.Globals;
import game.engine.audio.AudioLoader;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.entities.components.ai.PathFinder;
import game.world.World;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;

public class Watchman extends Mob{
	
	int lastPos;
	
	public Watchman(int x, int y, Player p){
		name = "Watchman";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name + "StandSouth");
		this.physicsComponent = new Tangible(x, y, 30, 30, 1);

		spriteXOff = -graphicsComponent.getWidth() / 2 - 65;
		spriteYOff = -graphicsComponent.getHeight() + 65;
		super.walkRate = .7;
		target = p;
		sightRange = 1000;
	}
	
	public void addPathFinder(World l){
		p = new PathFinder(l);
		destination = null;
	}

	@Override
	protected void getInput() {
		
		

		
		if(Globals.distance(new Point(getX(), getY()), new Point(target.getX(),target.getY()) ) < 90 && validSight){
			attack((Mob)target);
			return;
		}
		attacking = false;

		
	//	if(destination != null){ 
	//		if(System.nanoTime()%47 == 0 || (this.getX() == destination.x && this.getY() == destination.y )){
	//			destination = null;
		//		pathFind();
		//	}
		//}
			pathFind();
	}
	

	@Override
	protected void attack(Mob enemy) {
		double targX = enemy.getPhysicsShape().getCenterX();
		double targY = enemy.getPhysicsShape().getCenterY();
		double x = getPhysicsShape().getCenterX();
		double y = getPhysicsShape().getCenterY();
		
		this.physicsComponent.stopMovement();
		xMoveBy = 0;
		yMoveBy= 0;
		if(graphicsComponent.isAnimating()){
			lastPos =graphicsComponent.seriesPosition;
			if(lastPos == 2){
				if(attacking){
					attacking = false;
					int chance = Globals.D(20);
					if(chance > 8){
						int dmg = 2*Globals.D(6);
						enemy.damage(dmg);

					}
				}
			}else if (lastPos != 3){
				attacking = true;
				graphicsComponent.loopImg(0.8, "Attack");
			}
		}

		
		
		this.direction = Globals.getIntDir((targY - y),(targX - x));
		this.currDirection = Globals.getStringDir(direction);
	}

	@Override
	protected void takeDamage(int damage) {
		// TODO Auto-generated method stub
		
	}
	
	private void pathFind(){
		
		sight = new Line2D.Double(target.getX(),target.getY(), getX(), getY());
		
		if(((destination == null /*&& validSight*/ )||( Globals.distance(sight.getP1(), sight.getP2()) < 900 ))&& System.nanoTime()%47 == 0){
			System.out.println("New path");
			p.setNewPath(new Point(getX(), getY()), new Point(target.getX(), target.getY()));
			destination = p.getNextLoc();
			System.out.println(destination);
			
		}
		if(destination != null && getX() == destination.x && getY() == destination.y )
			destination = p.getNextLoc();
		
		
		if(destination == null){
			p.setNewPath(new Point(getX(), getY()), new Point(target.getX(), target.getY()));
			System.out.println("ERROR");
			return;
		}


		xMoveBy =  destination.x - getX();
		yMoveBy =  destination.y - getY();
		
	}
	

}
