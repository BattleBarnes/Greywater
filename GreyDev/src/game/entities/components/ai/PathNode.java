package game.entities.components.ai;

import game.Globals;
import game.world.World;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class PathNode {
	private Point coord;
	private double costFromStart;
	private double costToGoal;

	private PathNode parent;

	// can be used to build a path from the starting tile to here

	public PathNode(Point p) {
		coord = p;
		parent = null;
		costFromStart = 0.0;
	}

	public void setCostFromStart(double v) {
		costFromStart = v;
	}

	public double getCostFromStart() {
		return costFromStart;
	}

	public void costToGoal(Point goal)
	// calculate _floor_ of the straight line dist. to the goal
	{
		double dist = coord.distance(goal.x, goal.y);
		costToGoal = Math.floor(dist);
		// System.out.println(coord + " to " + goal + ": " + costToGoal);
	}

	public double getScore() {
		return costFromStart + costToGoal;
	}

	public Point getPoint() {
		return coord;
	}

	public void setParent(PathNode p) {
		parent = p;
	}

	public PathNode getParent() {
		return parent;
	}

	public PathNode makeNeighbour(int direction, World wd)
	/*
	 * Return the neighbouring tile node in the quad direction, except when that
	 * location is invalid according to WorldDisplay.
	 */
	{
		PathNode newNode = null;
		int x = coord.x;
		int y = coord.y;
		
		switch(direction){
		case Globals.NORTH: newNode = makeNode(x, y - Globals.tileHeight, wd, direction); break;
		case Globals.NORTHEAST: newNode = makeNode(x + Globals.tileWidth, y - Globals.tileHeight, wd, direction); break;
		case Globals.EAST: newNode = makeNode(x + Globals.tileWidth, y, wd, direction); break;
		case Globals.SOUTHEAST: newNode = makeNode(x + Globals.tileWidth, y + Globals.tileHeight, wd, direction); break;
		case Globals.SOUTH: newNode = makeNode(x, y+Globals.tileHeight, wd, direction); break;
		case Globals.SOUTHWEST: newNode = makeNode(x - Globals.tileWidth, y + Globals.tileHeight, wd, direction); break;
		case Globals.WEST: newNode = makeNode(x - Globals.tileWidth, y, wd, direction); break;
		case Globals.NORTHWEST: newNode = makeNode(x - Globals.tileWidth,  y - Globals.tileHeight , wd, direction); break;
		default:System.out.println("makeNeighbour() error");
			newNode = null;
			break;
		}
	
		return newNode;
	}

	
	/*
	 * Create a neigbouring tile node. costFromStart is one more than the
	 * current node's value, since the new node is one node further along the
	 * path.
	 */
	private PathNode makeNode(int x, int y, World wd, int direction){
		if (!wd.checkValidTile(x, y))
			return null;
			if(wd.checkWorldCollision(new Rectangle(coord.x +10, coord.y +10 ,2*(x - coord.x) ,(y - coord.y) *2))){
				return null;
			}
		PathNode newNode = new PathNode(new Point(x, y));
		newNode.setCostFromStart(getCostFromStart() + Math.pow(Globals.distance(newNode.getPoint(), this.getPoint()),2));
		newNode.setParent(this);
		return newNode;
	} // end of makeNode()

	public ArrayList<Point> buildPath(){
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(coord);
		PathNode temp = parent;
		while (temp != null) {
			path.add(0, temp.getPoint()); // add at start to reverse the path
			temp = temp.getParent();
		}
		return path;
	}

}