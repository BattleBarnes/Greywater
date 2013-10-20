package game.entities.components.ai;

import game.Globals;
import game.world.World;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PathNode {
	private Point2D coord;
	private double costFromStart;
	private double costToGoal;

	private PathNode parent;

	// can be used to build a path from the starting tile to here

	public PathNode(Point2D start) {
		coord = start;
		parent = null;
		costFromStart = 0.0;
	}

	public void setCostFromStart(double v) {
		costFromStart = v;
	}

	public double getCostFromStart() {
		return costFromStart;
	}

	public void costToGoal(Point2D goal)
	// calculate _floor_ of the straight line dist. to the goal
	{
		// double dist = coord.distance(goal.getX(), goal.getY());
		// costToGoal = Math.floor(dist);
		double dx = Math.abs(this.coord.getX() - goal.getX());
		double dy = Math.abs(this.coord.getY() - goal.getY());
		costToGoal = Math.max(dx, dy);

	}

	public double getScore() {
		return costFromStart + costToGoal;
	}

	public Point2D getPoint() {
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
	 * Return the neighbouring tile node in the quad direction, except when that location is invalid according to WorldDisplay.
	 */
	{
		PathNode newNode = null;
		double x = coord.getX();
		double y = coord.getY();

		switch (direction) {
			case Globals.NORTH:
				newNode = makeNode(x, y - Globals.tileHeight, wd, direction);
				break;
			case Globals.NORTHEAST:
				newNode = makeNode(x + Globals.tileWidth, y - Globals.tileHeight, wd, direction);
				break;
			case Globals.EAST:
				newNode = makeNode(x + Globals.tileWidth, y, wd, direction);
				break;
			case Globals.SOUTHEAST:
				newNode = makeNode(x + Globals.tileWidth, y + Globals.tileHeight, wd, direction);
				break;
			case Globals.SOUTH:
				newNode = makeNode(x, y + Globals.tileHeight, wd, direction);
				break;
			case Globals.SOUTHWEST:
				newNode = makeNode(x - Globals.tileWidth, y + Globals.tileHeight, wd, direction);
				break;
			case Globals.WEST:
				newNode = makeNode(x - Globals.tileWidth, y, wd, direction);
				break;
			case Globals.NORTHWEST:
				newNode = makeNode(x - Globals.tileWidth, y - Globals.tileHeight, wd, direction);
				break;
			default:
				System.out.println("makeNeighbour() error");
				newNode = null;
				break;
		}

		return newNode;
	}

	/*
	 * Create a neigbouring tile node. costFromStart is one more than the current node's value, since the new node is one node further along the path.
	 */
	private PathNode makeNode(double x, double y, World wd, int direction) {
		// if (!wd.checkValidTile((int)x, (int)y))
		// return null;
		// if(wd.checkWorldCollision(new Rectangle2D.Double(coord.getX(), coord.getY() ,(x - coord.getX()) ,(y - coord.getY()) )))
		// return null;

		if (wd.checkWorldCollision(new Line2D.Double(this.getPoint(), new Point2D.Double(x, y))))
			return null;
		PathNode newNode = new PathNode(new Point2D.Double(x, y));
		newNode.setCostFromStart(getCostFromStart() + Math.pow(Globals.distance(newNode.getPoint(), this.getPoint()), 2));
		newNode.setParent(this);
		return newNode;
	} // end of makeNode()

	public ArrayList<Point2D> buildPath(World wd) {
		ArrayList<Point2D> path = new ArrayList<Point2D>();
		path.add(coord);

		PathNode temp = parent;
		while (temp != null) {
			path.add(0, temp.getPoint()); // add at start to reverse the path
			temp = temp.getParent();
		}
		return path;
	}

}