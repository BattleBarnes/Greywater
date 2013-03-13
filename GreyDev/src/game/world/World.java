/**
 * 
 * 
 * @author Jeremy Barnes
 */
package game.world;

import game.Globals;
import game.engine.Camera;
import game.entities.Player;
import game.entities.Wall;
import game.entities.components.Entity;
import game.entities.components.Item;
import game.entities.components.Sprite;

import java.awt.Graphics;
import java.awt.Point;
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
	
	public ArrayList<Entity> mobList;
	public ArrayList<Entity> floorItems;
	


	private Comparator<Entity> spriteSorter = new Comparator<Entity>(){

		@Override
		public int compare(Entity e1, Entity e2) {
			if(e1.getDepth() < e2.getDepth())
				return -1;
			if(e1.getDepth() > e2.getDepth()) 
				return 1;
			return 0;
		}


	};

	public World(Sprite t, Sprite w, Sprite ow, List<Entity> mobs, List<Entity> items, Player p){
		
		tileWidth = t.getWidth()/2;
		tileHeight = t.getHeight();
		
		Globals.tileHeight =tileHeight;
		Globals.tileWidth = t.getWidth()/2;
		
		wall = w;
		tile = t;
		otherwall = ow;
		
		
		mobList = (ArrayList<Entity>) mobs;
		floorItems = (ArrayList<Entity>) items;
		player = p;
		
		loadEnviro(9);

	}

	public void render(Graphics g){

		Point origin = Globals.findTile(player.xPos - 1250, player.yPos - 1200);
		Point bottomRight = Globals.findTile(player.xPos + 1250, player.yPos + 1250);

		for (int x = origin.x; x < bottomRight.x; x++) {
			for (int y = bottomRight.y; y >= origin.y; y--) {

				try {
					if (tileMap[x][y] != null) {
						tileMap[x][y].render(g);
					}
				} catch (Exception e) {};
			}
		}

		for(Entity e:floorItems){
			e.render(g);
		}
		
		ArrayList<Entity> sortList = new ArrayList<Entity>();

		for(Entity e:mobList){
			sortList.add(e);
		}
		
		for(int x = 0; x < xLength; x++){
			for(int y  = 0; y < yHeight ; y++){
				if(walls[x][y] != null)
					sortList.add(walls[x][y]);
			}
		}
		
		Collections.sort(sortList, spriteSorter);
		
		for(Entity e:sortList){
			e.render(g);
		}
		
		
	
	}

	public void tick(){
		
		for(Entity e:mobList){
			e.update();
		}
		
		for(int x = 0; x < walls.length; x++){
			for(int y = 0; y < walls[x].length; y++){	
				if(walls[x][y] == null)
					continue;
				if(player.getPhysicsShape().intersects(walls[x][y].getPhysicsShape()))
					player.undoMove(true, true);
			}
		}
		
		Point p = Globals.getIsoCoords(player.xPos, player.yPos);
		Camera.moveTo(p.x, p.y);
		
		
	}
	
	public Entity floorItemCheck(int x, int y){
		for(Entity e:floorItems){
			if(e.getPhysicsShape().contains(x, y)){
				Entity ee = e;
				floorItems.remove(e);
				return ee;
			}
		}
		return null;
	}



	private void loadEnviro(int lvlno){
		try {

			File readFile = new File("Level" + lvlno + ".txt");
			Scanner filer= new Scanner(readFile);
			String line;

			xLength = Integer.parseInt(filer.nextLine());
			yHeight =Integer.parseInt(filer.nextLine());
			tileMap = new Tile[xLength][yHeight];
			walls = new Wall[xLength][yHeight];

			for(int y = 0; y < yHeight; y++){

				line = filer.nextLine();
				if(line.contains("//")){
					y--;
					continue;
				}

				for(int x  = 0; x < xLength ; x++){
					if(line.charAt(x) == '0'){
						int xCo = x*tileWidth;
						int yCo = y*tileHeight;
						tileMap[x][y] = new Tile(tile, xCo, yCo, false);
					}			
				}
			}
			filer.close();
			filer = null;
			line = "";
			readFile = new File("Walls" + lvlno + ".txt");
			filer= new Scanner(readFile);


			for(int y = 0; y < 6; y++){
				line = filer.nextLine();
				if(line.contains("//")){
					y--;
					continue;
				}

				for(int x  = 0; x < 20 ; x++){

					int xCo = x*tileWidth;
					int yCo = y*tileHeight;
					
					if(line.charAt(x) == 'W'){
						xCo = x*tileWidth;
						yCo = y*tileHeight;
						walls[x][y] = new Wall(xCo, yCo, wall, tileWidth*2.0/tileHeight, tileWidth, tileHeight, player);
					}
					else if(line.charAt(x) == 'S'){
						walls[x][y] = new Wall(xCo, yCo, otherwall, tileWidth*(2.0)/tileHeight, tileWidth, tileHeight, player);
					}
				}
			}

			filer.close();
			filer = null;

		} catch (FileNotFoundException e1) {e1.printStackTrace();}
	}



}
