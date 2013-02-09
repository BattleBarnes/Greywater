package game;

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
		case SOUTHWEST: dirString = "South"; break;
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
		if(xMoveBy != 0 && yMoveBy != 0){ //if diagonal movement
			if(xMoveBy > 0 && yMoveBy > 0)//right and down
				dir = SOUTHEAST;
			else if (xMoveBy > 0  && yMoveBy < 0) //right and up
				dir = NORTHEAST;
			else if (xMoveBy < 0 && yMoveBy > 0)//left and down
				dir = SOUTHWEST;
			else if (xMoveBy < 0 && yMoveBy < 0)//left and up
				dir = NORTHWEST;
		}
		else if(xMoveBy != 0){//if horizontal movement
			if(xMoveBy > 0)//right
				dir = EAST;
			else if(xMoveBy < 0)//left
				dir = WEST;
		}
		else if(yMoveBy != 0){//if vertical movement
			if(yMoveBy > 0)//down
				dir = SOUTH;
			else if(yMoveBy < 0)//up
				dir = NORTH;
		}
		
		return dir;
	}

}
