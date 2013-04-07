package game.entities;

import game.Globals;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.entities.components.ai.PathFinder;
import game.world.World;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;

public class Watchman extends Mob{
	
	public Watchman(int x, int y, Player p){
		name = "Watchman";
		currDirection = "South";
		this.graphicsComponent = new Sprite(name, name + "StandSouth");
		this.physicsComponent = new Tangible(x, y, 35, 35, 1);

		spriteXOff = -graphicsComponent.getWidth() / 2 - 65;
		spriteYOff = -graphicsComponent.getHeight() + 65;
		super.walkRate = .7;
		target = p;
		sightRange = 1000;
	}
	
	public void addPathFinder(World l){
		p = new PathFinder(l);
	}

	@Override
	protected void getInput() {
		

		Random rand = new Random();
		rand.nextInt(100);
		
		if(destination == null || this.getX() == destination.x)
			xMoveBy = rand.nextInt();
		
		//pathFind();
	}
	

	@Override
	protected void attack(Mob enemy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void takeDamage(int damage) {
		// TODO Auto-generated method stub
		
	}
	
	private void pathFind(){
		
		sight = new Line2D.Double(target.getX(),target.getY(), getX(), getY());
		
		if(destination == null && validSight){

			p.setNewPath(new Point(getX(), getY()), new Point(target.getX(), target.getY()));
			destination = p.getNextLoc();
			
		}
		if(destination != null && getX() == destination.x && getY() == destination.y )
			destination = p.getNextLoc();
		
		
		if(destination == null)
			return;


		xMoveBy =  destination.x - getX();
		yMoveBy =  destination.y - getY();
		
	}

}
