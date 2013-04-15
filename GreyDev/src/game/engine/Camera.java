/** Camera.java
 *This class is primarily a holder for co-ordinate data to simulate a moving camera.
 * Sprites are rendered at the offset of the camera, allowing them to appear to maintain their original
 * positions. The camera "follows" the player.
 * 
 * @author Jeremy Barnes
 */

package game.engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Camera  {

	//current location of the camera in world co-ordinates
	private static int xPos;
	private static int yPos;
	
	//how much the camera has moved by, allows drawing to original 
	//world co-ordinates instead of camera co-ordinates.
	public static int xOffset;
	public static int yOffset;
	
	//Sprites were designed for display at 1920 x 1080
	//rescale when the resolution changes.
	public static double scale;
	
	//screen dimensions
	public static int width;
	public static int height;
	
	//rectangle that has all the camera data in it.
	public static Rectangle viewPort;
	
	
	/**
	 * Constructor
	 * 
	 * @param x - x location
	 * @param y - y location
	 * @param w - width of the camera aperture (window size)
	 * @param h - height of the camera aperture (window size)
	 */
	public Camera(int x, int y, int w, int h){
		xPos = x;
		yPos = y;
		width = w;
		height = h;
		
		//yScale = (height/1080.);
		scale = width/1920.;
	
		
		viewPort = new Rectangle(xPos, yPos, width, height);

	}

	/**
	 * Move the camera to this location
	 * @param x
	 * @param y
	 */
	public static void moveTo(int x, int y) {
		if(x  + xOffset == xPos && y + yOffset == yPos)
			return;

		yOffset = y - height/3;
		xOffset = x - width/2 + 50;
		viewPort.setLocation(xPos + xOffset, yPos + yOffset);
	}
	
	
	public Rectangle getViewArea(){
		return new Rectangle(xPos + xOffset, yPos + yOffset, width+ 100, height +  100);
	}


	

}
