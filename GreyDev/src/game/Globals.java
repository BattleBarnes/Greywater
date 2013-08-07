/**
 * Functions needed all over the damn place.
 */
package game;

import game.engine.Camera;
import game.engine.State;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

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
	

	//width and height of a tile in memory (not image size)
	public static int tileWidth;
	public static int tileHeight;
	public static Random rand = new Random(); //used for dicerolls
	
	/**
	 * Finds the array indexes of a tile given two co-ordinates
	 * already in flatspace. Does not convert screen (iso) co-ordinates to 
	 * grid-co-ordinates. You have to do that with isoToGrid()
	 * @param x - x location
	 * @param y - y location
	 * @return Point whose x and y value are the array indices of the tile in question.
	 */
	public static Point findTile(int x, int y){
		int xt = x / tileWidth;
		int yt = y/tileHeight;
		return new Point(xt,yt);
	}
	
	/**
	 * Converts screen co-ordinates (isometic view) into flatspace, 
	 * or grid co-ordinates. Remember, the grid is rectangular and top down in memory.
	 * It is only rotated for display.
	 * 
	 * @param x - x on screen
	 * @param y - y on screen
	 * @return
	 */
	public static Point isoToGrid(int x, int y) {
		x += Camera.xOffset;
		y += Camera.yOffset;
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
	
	/**
	 * Gives the degree-integer value of the entity's direction using angles
	 * instead of move-by's. 
	 * 
	 * @param xDiff - TargetX - CurrentX
	 * @param yDiff - TargetY - CurrentY
	 * @return integer representing direction. It corresponds to the public static final ints
	 * 			in this class, but when used in conjunction with the getStringDir method, 
	 * 			you need not concern yourself with them.
	 */
	public static int getIntDir(double xDiff, double yDiff){
		double angle =(-1)* Math.toDegrees(Math.atan2(yDiff, xDiff)) - 45;
		if(angle < 0)
			angle += 360;
		if(angle >= 337.5 || angle < 22.5 )
			return EAST;
		else if(angle >= 22.5 && angle < 67.5  )
			return NORTHEAST;
		else if(angle >= 67.5 && angle < 112.5 )
			return NORTH;
		else if(angle >= 112.5 && angle < 157.5)
			return NORTHWEST;
		else if(angle >=  157.5 && angle < 202.5)
			return WEST;
		else if(angle >= 202.5 && angle < 247.50)
			return SOUTHWEST;
		else if(angle >= 247.5 && angle < 292.5)
			return SOUTH;
		else if(angle >= 292.5 && angle < 337.5 )
			return SOUTHEAST;
		
		
		return 666;
	}
	
	/**
	 * This is the distance (straight line) between 2 points.
	 * @param p1
	 * @param p2
	 * @return the distance as a double
	 */
	public static double distance(Point2D p1, Point2D p2){
		double dist = 0.;
		dist = Math.sqrt(Math.pow((p1.getX()*1.0 - p2.getX()),2) + Math.pow((p1.getY()*1.0 - p2.getY()),2));
		return dist;
	}
	
	/**
	 * Roll a dice with the specified number of sides. Returns 1-dice, inclusive.
	 * @param dice - D(20) or D(12). Just like DND.
	 * @return the resultant roll.
	 */
	public static int D(int dice){
		return rand.nextInt(dice) + 1;
	}

}
