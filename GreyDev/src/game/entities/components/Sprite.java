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

import game.Core;
import game.engine.Camera;
import game.engine.ImageLoader;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite {
	
	//what it uses to represent itself visually
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
	public int seriesPosition;
	private int seriesLength;
		
	
	/**Constructor for sprites.
	 * 
	 * @param name - The name of the character/entity (Such as Tavish)
	 * @param imgName - default image to start with.
	 */
	public Sprite(String name, String imgName){
		animPeriod_Millis = (int) (Core.animPeriodNano/1000000);
		this.name = name;
		currImgName = imgName;
		forceImage(imgName);
	}
		
	/**
	 * Draws the sprite
	 * 
	 * @param g - graphics object for rendering
	 * @param x - x co-ordinate to render at
	 * @param y - y co-ordinate to render at
	 */
	public void render(Graphics g, int x, int y){
		tick();
		BufferedImage rep = getCurrentImage();
		g.drawImage(rep, x, y, rep.getWidth(), rep.getHeight(), null);

	}
	
	
	public void renderScaled(Graphics g, int x, int y){
		tick();
		BufferedImage rep = getCurrentImage();
		//g.drawImage(rep, x, y, rep.getWidth(), rep.getHeight(), null);
	//	((Graphics2D)g).scale(Camera.scale, Camera.scale);
		if(Camera.scale == 1.0){
			render(g,x,y);
			return;
		}
		g.drawImage(rep, x, y,(int)Math.round(rep.getWidth()*Camera.scale), (int)Math.round(rep.getHeight()*Camera.scale), null);
	//	((Graphics2D)g).scale(1/Camera.scale, 1/Camera.scale);
	}
	
	public void drawTransparent(Graphics g, int x, int y, float opacity){
		BufferedImage tranny = getCurrentImage();
		Graphics2D g2d = ((Graphics2D) g);
		Composite c = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.drawImage(tranny, x,y, null);
		g2d.setComposite(c);
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
		if(seriesPosition >= seriesLength-1 && !isLooping)
			stopAnim(); //if it isn't looping, stop
		if(seriesPosition >= seriesLength&& isLooping)
			seriesPosition = 0; //if it is looping, go back to beginning
	}
	
	/**
	 * @return the current image for the sprite as a buffered image
	 */
	public BufferedImage getCurrentImage(){
		if(isTicking)
			return ImageLoader.getGroupedImage(currImgName, seriesPosition);
		return sprite;
	}
	
	public String getCurrentImageName(){
		return currImgName;
	}
	
	
	/**Loops an animation for a set period of time
	 * 
	 * @param duration_seconds - length of time to loop in seconds
	 * @param ident - Images are loaded as name+ident (Tavish + _Walk_North)
	 */
	public void loopImg(double duration_seconds, String ident){
		if(currImgName.equalsIgnoreCase(name+ident)){
			return;
		}
		
		isLooping = true;
		isTicking = true;
		seriesPosition = 0;
		totalAnimTime_Millis = 0;
		sequenceDuration_Millis = duration_seconds * 1000;
		seriesLength = ImageLoader.getSeriesCount(name + ident);
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
		seriesLength = ImageLoader.getSeriesCount(name + ident);
		cycleLength_Millis = sequenceDuration_Millis / seriesLength;
		currImgName = name+ident;
	}

	
	
	/**
	 * Force-sets an image, circumventing the name + ident system.
	 * 
	 * @param name - the image you want to set.
	 */
	public void forceImage(String name){
		sprite = ImageLoader.getSingleImage(name);
		if(sprite == null)
			System.out.println(name + " null!");
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
