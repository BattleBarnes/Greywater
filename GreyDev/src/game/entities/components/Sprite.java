/**
 * This class is intended to be the image class for all visually represented items.
 * It holds no position data, all objects need a class of their own to hold that data and
 * do collision detection.
 * 
 * *********Important note on the name system in this class.********************
 * Some string variables are used, primarily "name" and "ident"
 * 
 * This system was built to work a very specific way, using a hashmap to store images.
 * The system used is as follows: each sprite for a character is given a formulaic name.
 * For the Character Tavish, his animation walking north is Tavish_Walk_North. This allows the 
 * direction to be passed each time he changes bearing (Walk_South for instance) without passing
 * his name each time.
 * 
 * See the Image Loader class for a full understanding if you are confused.
 *  
 *  
 * @author Jeremy Barnes
 * January 10, 2013
 */
package game.entities.components;

import game.engine.Camera;
import game.engine.ImageLoader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite {
	
	//what it uses to represent itself visually
	private ImageLoader imgLd;
	private BufferedImage sprite;
	
	private boolean isLooping;
	private boolean isTicking;
	
	private String name;
	private String currImgName;
	
	//time keepers
	private double cycleLength_Millis;
	private long totalAnimTime_Millis;
	private double sequenceDuration_Millis;
	private int animPeriod_Millis;
	
	//series length and position trackers (not time, image-count)
	private int seriesPosition;
	private int seriesLength;
		
	
	/**Constructor for animated sprites.
	 * 
	 * @param ims - ImageLoader (IO class to get actual image data)
	 * @param name - The name of the character/entity (Such as Tavish)
	 * @param animPer - Period of animation in milliseconds
	 * @param imgName - default image to start with.
	 */
	public Sprite(ImageLoader ims, String name, int animPer, String imgName){
		imgLd = ims;
		animPeriod_Millis = animPer;
		this.name = name;
		currImgName = imgName;
		forceImage(imgName);
	}
	
	/**
	 * Constructor for static images (never animating sprites)
	 * Like floor tiles!
	 * 
	 * @param ims - image loader
	 * @param name - the name of the character/entity
	 * @param imgName - the name of the image
	 */
	public Sprite(ImageLoader ims, String name, String imgName){
		imgLd = ims;
		this.name = name;
		currImgName = imgName;
		forceImage(imgName);
	}
	
	
	/**
	 * Draws the sprite at the proper scale (auto scaling!)
	 * 
	 * @param g - graphics object for rendering
	 * @param x - x co-ordinate to render at
	 * @param y - y co-ordinate to render at
	 */
	public void draw(Graphics g, int x, int y){
		tick();
		BufferedImage rep = getCurrentImage();
		g.drawImage(rep, x, y, rep.getWidth(), rep.getHeight(), null);

	}
	
	/**
	 * Updates the image if it is animated, assumed to be called
	 * once every anim-period.
	 */
	public void tick(){
		if(isTicking){
			totalAnimTime_Millis = (long) ((totalAnimTime_Millis + animPeriod_Millis) % sequenceDuration_Millis);
			seriesPosition = (int) (totalAnimTime_Millis / cycleLength_Millis);
		}
		if(seriesPosition >= seriesLength && !isLooping)
			stopAnim(); //if it isn't looping, stop
		if(seriesPosition >= seriesLength&& isLooping)
			seriesPosition = 0; //if it is looping, go back to beginning
	}
	
	/**
	 * @return the current image for the sprite as a buffered image
	 */
	public BufferedImage getCurrentImage(){
		if(isTicking)
			return imgLd.getGroupedImage(currImgName, seriesPosition);
		return sprite;
	}
	
	
	/**Loops an animation for a set period of time
	 * 
	 * @param duration_seconds - length of time to loop in seconds
	 * @param ident - Images are loaded as name+ident (Tavish + _Walk_North)
	 */
	public void loopImg(double duration_seconds, String ident){
		if(currImgName.contains(ident)){
			return;
		}
		
		isLooping = true;
		isTicking = true;
		seriesPosition = 0;
		totalAnimTime_Millis = 0;
		sequenceDuration_Millis = duration_seconds * 1000;
		seriesLength = imgLd.getSeriesCount(name + ident);
		cycleLength_Millis = sequenceDuration_Millis / seriesLength;
		currImgName = name+ident;
	}
	
	/**Plays an animation once.
	 * 
	 * @param duration_seconds - length of time to play the animation in seconds
	 * @param ident - Images are loaded as name+ident (Tavish + _Walk_North)
	 */
	public void animate(double duration_seconds, String ident){
		isLooping = false;
		isTicking = true;
		seriesPosition = 0;
		totalAnimTime_Millis = 0;
		sequenceDuration_Millis = duration_seconds * 1000;
		seriesLength = imgLd.getSeriesCount(name + ident);
		cycleLength_Millis = sequenceDuration_Millis / seriesLength;
		currImgName = name+ident;
	}

	
	
	/**
	 * Force-sets an image, circumventing the name + ident system.
	 * 
	 * @param name - the image you want to set.
	 */
	public void forceImage(String name){
		sprite = imgLd.getSingleImage(name);
		if(sprite == null)
			System.out.println(name);
		currImgName = name;
		isTicking = false;
		isLooping = false;
	}
	
	public int getWidth(){
		return getCurrentImage().getWidth();		
	}
	
	public int getHeight(){
		return getCurrentImage().getHeight();
	}
	
	public boolean isAnimating(){
		return isTicking;
	}
	
	private void stopAnim(){
		isTicking = false;
		isLooping = false;
	}
	


	
}
