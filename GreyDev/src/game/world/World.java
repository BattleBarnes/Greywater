/**
 * 
 * 
 * @author Jeremy Barnes
 */
package game.world;

import game.Globals;
import game.engine.Camera;
import game.entities.Mob;
import game.entities.Player;
import game.entities.Wall;
import game.entities.components.Entity;
import game.entities.components.Sprite;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class World {

	int tileWidth;
	int tileHeight;

	int xLength;
	int yHeight;

	Tile[][] tileMap;
	Wall[][] walls;

	Sprite tile;
	Sprite wall;
	Sprite otherwall;
	Player player;

	public ArrayList<Mob> mobList;

	private Comparator<Entity> spriteSorter = new Comparator<Entity>() {

		@Override
		public int compare(Entity e1, Entity e2) {
			if (e1.getDepth() < e2.getDepth())
				return -1;
			if (e1.getDepth() > e2.getDepth())
				return 1;
			return 0;
		}

	};

	public World(Sprite t, Sprite w, Sprite ow, List<Mob> mobs, Player p) {

		tileWidth = t.getWidth() / 2;
		tileHeight = t.getHeight();

		Globals.tileHeight = tileHeight;
		Globals.tileWidth = t.getWidth() / 2; // same as tileHeight
												// (square/flatspace)

		wall = w;
		tile = t;
		otherwall = ow;

		mobList = (ArrayList<Mob>) mobs;
		player = p;

		loadEnviro(9);

	}

	public void render(Graphics g) {
		// points used for render culling
		Point origin = Globals.findTile(player.getX() - Camera.width, player.getY() - Camera.height);
		Point bottomRight = Globals.findTile(player.getX() + Camera.width, player.getY() + 2 * Camera.height);
		if(origin.x < 0)
			origin.setLocation(0, origin.y);
		if(origin.y < 0)
			origin.setLocation(origin.x, 0);
		
		if(bottomRight.x > xLength)
			bottomRight.setLocation(xLength, bottomRight.y);
		if(bottomRight.y > yHeight)
			bottomRight.setLocation(bottomRight.x, yHeight);

		// render loop
		for (int x = origin.x; x < bottomRight.x; x++) {
			for (int y = bottomRight.y; y >= origin.y; y--) {
				try {
					if (tileMap[x][y] != null) 
						tileMap[x][y].render(g);
				} catch (Exception e) {
				}
				;
			}
		}

		ArrayList<Entity> sortList = new ArrayList<Entity>();

		for (Entity e : mobList) {
			sortList.add(e);
		}

		for (int x = origin.x; x < bottomRight.x; x++) {
			for (int y = bottomRight.y; y >= origin.y; y--) {
				try {
					if (walls[x][y] != null)
						sortList.add(walls[x][y]);
				} catch (Exception e) {};
			}
		}

		Collections.sort(sortList, spriteSorter);

		for (Entity e : sortList) {
			e.render(g);
		}

	}

	public void tick() {

		// update mobs and if they are using AI, check to see if the player is
		// visible to them
		for (Mob e : mobList) {
			e.tick();

			Line2D l = e.getSight();
			if (l != null && Globals.distance(l.getP1(), l.getP2()) < e.sightRange && !checkWorldCollision(l)) {
				e.validateSight(true);
			} else
				e.validateSight(false);

			// if(e.target!= null)
			// if(Globals.distance(new Point(e.target.getX(),e.target.getY()),
			// new Point(e.getX(), e.getY())) > 100)

		}

		// collision detection
	
				for (Mob m : mobList) {
					if(checkWorldCollision(m.getPhysicsShape())){
						Rectangle2D r = m.getPhysicsShape();
						// physics shape, renamed for convenience

						// these rectangles use old x and y coords, respectively, to see which direction the collision is in.
						Rectangle2D xRect = new Rectangle2D.Double(((double) m.xLast), r.getY(), r.getWidth(), r.getHeight());
						Rectangle2D yRect = new Rectangle2D.Double(r.getX(), ((double) m.yLast), r.getWidth(), r.getHeight());

						//if (xRect.intersects(walls[x][y].getPhysicsShape()))
						if(checkWorldCollision(xRect))
							m.undoMove(true, false);
					//	else if (yRect.intersects(walls[x][y].getPhysicsShape()))
						else if(checkWorldCollision(yRect))
							m.undoMove(false, true);
						else
							m.undoMove(true, true);
					}
				}
		//	}
	//	}

		// move the camera to follow the player
		Point p = Globals.getIsoCoords(player.getX(), player.getY());
		Camera.moveTo(p.x, p.y);

	}

	public boolean checkWorldCollision(Shape s) {
		Point area = Globals.findTile(s.getBounds().x, s.getBounds().y);
		int areaX = area.x;
		int areaY = area.y;
		if(areaX < 0 )
			areaX = 0;
		if(areaY < 0 )
			areaY = 0;
		
		for (int x = areaX; x < areaX + 10; x++) {
			for (int y = areaY; y < areaY + 10; y++) {
				if (walls[x][y] == null)
					continue;
				if (s != null && s.intersects(walls[x][y].getPhysicsShape()))
					return true;
			}
		}
		return false;
	}
	
	
	public boolean checkEntityCollision(Shape s){
		for(Entity e:mobList){
			if(s.intersects(e.getPhysicsShape()))
				return true;
		}
		return false;
	}

	public boolean checkValidTile(int x, int y) {
		if (x < 0 || y < 0)
			return false;
		Rectangle r = new Rectangle(x, y, 45, 45);
		if (checkWorldCollision(r))
			return false;

		for (int i = 0; i < tileWidth; i++) {
			for (int j = 0; j < tileHeight; j++) {
				if (tileMap[i][j] != null && tileMap[i][j].getPhysicsShape().contains(x, y))
					return true;
			}
		}

		return false;

	}

	private void loadEnviro(int lvlno) {
		try {

			File readFile = new File("Level" + lvlno + ".txt");
			Scanner filer = new Scanner(readFile);
			String line;

			xLength = Integer.parseInt(filer.nextLine());
			yHeight = Integer.parseInt(filer.nextLine());
			tileMap = new Tile[xLength][yHeight];
			walls = new Wall[xLength][yHeight];

			for (int y = 0; y < yHeight; y++) {

				line = filer.nextLine();
				if (line.contains("//")) {
					y--;
					continue;
				}

				for (int x = 0; x < xLength; x++) {
					if (line.charAt(x) == '0') {
						int xCo = x * tileWidth;
						int yCo = y * tileHeight;
						tileMap[x][y] = new Tile(tile, xCo, yCo);
					}
				}
			}
			filer.close();
			filer = null;
			line = "";

			readFile = new File("Walls" + (lvlno) + ".txt");
			filer = new Scanner(readFile);

			for (int y = 0; y < yHeight; y++) {
				line = filer.nextLine();
				if (line.contains("//")) {
					y--;
					continue;
				}

				for (int x = 0; x < xLength; x++) {

					int xCo = x * tileWidth;
					int yCo = y * tileHeight;

					if (line.charAt(x) == 'W') {
						xCo = x * tileWidth;
						yCo = y * tileHeight;
						walls[x][y] = new Wall(xCo, yCo, wall, tileWidth * 2.0 / tileHeight, tileWidth, tileHeight, player, true);
					} else if (line.charAt(x) == 'S') {
						walls[x][y] = new Wall(xCo, yCo, otherwall, tileWidth * (2.0) / tileHeight, tileWidth, tileHeight, player, false);
					}
				}
			}

			filer.close();
			filer = null;

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}
