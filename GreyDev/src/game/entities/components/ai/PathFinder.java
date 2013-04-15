package game.entities.components.ai;

import game.Globals;
import game.world.World;

import java.awt.Point;
import java.util.ArrayList;

public class PathFinder {

	ArrayList<Point> path;
	private World world;
	int pathIndex = 0;

	public PathFinder(World w) {
		path = new ArrayList<Point>();
		world = w;
	}

	public void setNewPath(Point start, Point goal) {
		path = aStar(start, goal);
		pathIndex = 0;
	}

	public Point getNextLoc() {
		if (path == null || pathIndex == path.size())
			return null;
		else {
			pathIndex++;
			return path.get(pathIndex - 1);
		}
	}

	private ArrayList<Point> aStar(Point startLoc, Point goalLoc) {
		double newCost;
		PathNode bestNode, newNode;

		PathNode startNode = new PathNode(startLoc); // set start node
		startNode.costToGoal(goalLoc);

		// create the open queue and closed list
		NodePriQueue open = new NodePriQueue(startNode);
		NodeList closed = new NodeList();

		while (open.size() != 0) { // while some node still left to investigate
			bestNode = open.pop();

			if (Globals.distance(goalLoc, bestNode.getPoint()) < Globals.tileHeight || Globals.distance(goalLoc, bestNode.getPoint()) < Globals.tileHeight - 20) { // goal!

				return bestNode.buildPath(); // return a path to that goal
			} else {
				for (int i = 0; i < 8; i++) { // try every direction
					if ((newNode = bestNode.makeNeighbour(i, world)) != null) {
						newCost = newNode.getCostFromStart();
						PathNode oldVer = open.findNode(newNode.getPoint());
						// if this tile already has a cheaper open or closed
						// node then ignore the new node

						if ((oldVer != null)
								&& (oldVer.getCostFromStart() <= newCost))
							continue;
						else if (((oldVer = closed.findNode(newNode.getPoint())) != null)
								&& (oldVer.getCostFromStart() <= newCost))
							continue;
						else { // store the new/improved node, removing the old
								// one
							newNode.costToGoal(goalLoc);
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
		return null; // no path found
	}
}