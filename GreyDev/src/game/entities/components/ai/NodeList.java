package game.entities.components.ai;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NodeList {
	protected ArrayList nodes; // list of PathNode objects

	public NodeList(PathNode node) {
		nodes = new ArrayList();
		nodes.add(node);
	}

	public NodeList() {
		nodes = new ArrayList();
	}

	public void add(PathNode node) {
		nodes.add(node);
	}

	public PathNode findNode(Point2D point2d)
	// a linear search looking for the tile at point p;
	{
		PathNode entry;
		for (int i = 0; i < nodes.size(); i++) {
			entry = (PathNode) nodes.get(i);
			if ((entry.getPoint()).equals(point2d))
				return entry;
		}
		return null;
	} // end of findNode()

	public boolean delete(Point2D point2d)
	/*
	 * Try to delete the tile at point p from the list. If p is not present then
	 * do nothing.
	 */
	{
		Point2D entry;
		for (int i = 0; i < nodes.size(); i++) {
			entry = ((PathNode) nodes.get(i)).getPoint();
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