package game.entities.components.ai;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NodePriQueue {

	private ArrayList<PathNode> nodes;

	public NodePriQueue(PathNode node) {
		nodes = new ArrayList<PathNode>();
		nodes.add(node);
	}

	public void add(PathNode node) {

		double newScore = node.getScore();
		PathNode entry;
		for (int i = 0; i < nodes.size(); i++) {
			entry = nodes.get(i);
			if (newScore <= entry.getScore()) {
				nodes.add(i, node);
				return;
			}
		}
		nodes.add(node);
	}

	public PathNode pop() {
		return nodes.remove(0);
	}

	public PathNode findNode(Point2D p)
	// a linear search looking for the tile at point p;
	{
		PathNode entry;
		for (int i = 0; i < nodes.size(); i++) {
			entry = (PathNode) nodes.get(i);
			if ((entry.getPoint()).equals(p))
				return entry;
		}
		return null;
	} // end of findNode()

	public boolean delete(Point2D point2d){
		Point2D entry;
		for (int i = 0; i < nodes.size(); i++) {
			entry = nodes.get(i).getPoint();
			if (entry.equals(point2d)) {
				nodes.remove(i);
				return true;
			}
		}
		return false;
	} // end of delete()

	public int size() {
		return nodes.size();
	}

}
