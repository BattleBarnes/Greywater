package game.entities.components.ai;

import game.world.World;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PathFinder {
	private final int MAXRETRIES = 400;
	int retries = 0;
	ArrayList<Point2D> path;
	private World world;
	int pathIndex = 0;

	public PathFinder(World w) {
		path = new ArrayList<Point2D>();
		world = w;

	}

	public boolean hasPath(){
		if (pathIndex == path.size())
			return false;

		return true;
	}
	
	public void setNewPath(Point2D start, Point2D goal) {
		path = aStar(start, goal);
		pathIndex = 0;
		retries = 0;

	}

	public Point2D getNextLoc() {
		if (path == null || pathIndex == path.size())
			return null;
		else {
			pathIndex++;
			return path.get(pathIndex - 1);
		}
	}

	private ArrayList<Point2D> aStar(Point2D start, Point2D goal) {
		double newCost;
		PathNode bestNode, newNode;

		PathNode startNode = new PathNode(start); // set start node
		startNode.costToGoal(goal);

		// create the open queue and closed list
		NodePriQueue open = new NodePriQueue(startNode);
		NodeList closed = new NodeList();

		while (open.size() != 0) { // while some node still left to investigate
			Thread.yield();

			retries++;
			bestNode = open.pop();

			if (retries >= MAXRETRIES) {
				return bestNode.buildPath(world);
			}

			 // goal!
			if (Math.abs(bestNode.getPoint().getX() - goal.getX()) < 50.0 && Math.abs(bestNode.getPoint().getY() - goal.getY()) < 50.0)
				return bestNode.buildPath(world); // return a path to that goal

			else {
				for (int i = 0; i < 8; i++) { // try every direction
					Thread.yield();
					if ((newNode = bestNode.makeNeighbour(i, world)) != null) {
						newCost = newNode.getCostFromStart();
						PathNode oldVer = open.findNode(newNode.getPoint());
						// if this tile already has a cheaper open or closed
						// node then ignore the new node

						if ((oldVer != null) && (oldVer.getCostFromStart() <= newCost))
							continue;
						else if (((oldVer = closed.findNode(newNode.getPoint())) != null) && (oldVer.getCostFromStart() <= newCost))
							continue;
						else if ((oldVer != null) && oldVer.getCostFromStart() <= 3000)
							continue;
						else { // store the new/improved node, removing the old one
							newNode.costToGoal(goal);
							// delete the old details (if they exist)
							closed.delete(newNode.getPoint()); // may do nothing
							open.delete(newNode.getPoint()); // may do nothing
							open.add(newNode);
						}
					}
				} // end of for block
			} // end of if-else
			closed.add(bestNode);
		}
		System.out.println("*****");
		System.out.println("Absolutely no path found. We tried boss..");
		System.out.println("*****");
		System.out.println();
		return null; // no path found
	}

}