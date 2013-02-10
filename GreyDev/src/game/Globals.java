package game;

import java.awt.Point;

import game.engine.State;

public class Globals {
	
	
	//Directional data! Sprites can only face these directions.
	public static final int NORTH = 0;
	public static final int NORTHEAST = 1;
	public static final int EAST = 2;
	public static final int SOUTHEAST = 3;
	public static final int SOUTH = 4;
	public static final int SOUTHWEST = 5;
	public static final int WEST = 6;
	public static final int NORTHWEST = 7;
	public static State state = State.mainMenu;
	
	public static int tileWidth;
	public static int tileHeight;
	
	
	public static Point findTile(int x, int y){
		int xt = x / tileWidth;
		int yt = y/tileHeight;
		return new Point(xt,yt);
	}
	

	public static Point isoToGrid(int x, int y) {
		int X = x/2 + y;
		int Y = y - x/2;
		return new Point(X, Y);
	}
	
	/**
	 * Convert the square co-ordinates into isometric co-ordinates
	 * @param x - x location in square space
	 * @param y - y location in square-space
	 * @return a Point with the objects X and Y co-ordinates adjusted for Isometric Display
	 */
	public static Point getIsoCoords(int x, int y) {
		int X = x - y;
		int Y = (x + y)/2;
		return new Point(X, Y);
	}
	
	
	/**
	 * Returns the text-representation of the direction the sprite is facing.
	 * Relies on the Global final ints named in this class
	 * 
	 * @param dir = the integer representing direction 
	 * @return String with North or NorthEast or South etc...
	 */
	public static String getStringDir(int dir){
		String dirString = "";
		
		switch(dir){
		case NORTH: dirString = "North"; break;
		case NORTHEAST: dirString = "NorthEast"; break;
		case EAST: dirString = "East"; break;
		case SOUTHEAST: dirString = "SouthEast"; break;
		case SOUTH: dirString = "South"; break;
		case SOUTHWEST: dirString = "SouthWest"; break;
		case WEST: dirString = "West"; break;
		case NORTHWEST: dirString = "NorthWest"; break;
		}
		
		//Images are named, so the sprite walking north would be SpriteWalkNorth.png
		//this allows the sprite to get that information from the numerical direction.
		return dirString;
	}
	
	/**
	 * Returns the integer direction for a sprite. 
	 * This abstracts the ugly if testing to see which way a sprite is facing
	 * and replaces it with a method call instead.
	 * 
	 * @param xMoveBy the amount to be moved in the x direction
	 * @param yMoveBy the amount to be moved in the y direction
	 * @return integer representing direction. It corresponds to the public static final ints
	 * 			in this class, but when used in conjunction with the getStringDir method, 
	 * 			you need not concern yourself with them.
	 */
	public static int getIntDir(int xMoveBy, int yMoveBy){
		int dir = 0;
		if(xMoveBy != 0 && yMoveBy != 0){ //if cardinal movement
			if(xMoveBy > 0 && yMoveBy > 0)//right and down
				return dir = SOUTH;
			else if (xMoveBy > 0  && yMoveBy < 0) //right and up
				return dir = EAST;
			else if (xMoveBy < 0 && yMoveBy > 0)//left and down
				return dir = WEST;
			else if (xMoveBy < 0 && yMoveBy < 0)//left and up
				return dir = NORTH;
		}
		else if(xMoveBy != 0){//if ordinal movement
			if(xMoveBy > 0)//right
				return dir = SOUTHEAST;
			else if(xMoveBy < 0)//left
				return dir = NORTHWEST;
		}
		else if(yMoveBy != 0){//if ordinal movement
			if(yMoveBy > 0)//down
				return dir = SOUTHWEST;
			else if(yMoveBy < 0)//up
				return dir = NORTHEAST;
		}
		
		return dir;
	}

}
