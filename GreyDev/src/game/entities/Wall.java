/**
 * WALL
 * 
 * Walls handle fairly differently based on directionality, so it is important to put in the right slope
 * Positive vs negative. Walls point up from left to right will have a positive slope, walls pointing down
 * will have a negative slope.
 * 
 * @author Jeremy Barnes  - Jan 20/13
 * 
 */
package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import game.engine.Camera;
import game.entities.components.Sprite;

public class Wall {
	
	int x;
	int y;
	int tileWidth;
	int tileHeight;

	int xDepth;
	int wallWidth;
	int transparentSpace;
	double isoAngle;
	
	protected Sprite graphicsComponent; //sprite

	public int xPos;
	public int yPos;

	/* **** Position is determined by the tile so offsets are tracked to draw the sprite in correct relation to the base tile *****/
	protected int spriteXOff; //horizontal distance from tile to (0,0) on sprite
	protected int spriteYOff; //vertical distance from tile to (0,0) on sprite

	/**
	 * 
	 * Constructor, sets up the polygon off the grid, moves it on to the grid,
	 * sets up the sprite offsets and graphics component.
	 * @param x - the starting x position of the bounding box 
	 * @param y - the starting y position of the bounding box
	 * @param wall - the sprite that this wall will be represented by
	 * @param isoAngle - the slope of the wall
	 * @param tileWidth - used for aligning it to the floor grid.
	 */
	public Wall(int x, int y, Sprite wall, double isoAngle, int tileWidth, int tileHeight) {
		this.x = x;
		this.y = y;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.graphicsComponent = wall;
		
		//math stuff used to set up xP and yP
		this.isoAngle = isoAngle;
		xDepth = graphicsComponent.getWidth() / 6; //depth of the wall in pixels (thin side, not the face)
		wallWidth = graphicsComponent.getWidth() - xDepth; //length of the face of the wall
		transparentSpace = (int) (wallWidth * isoAngle); // how much of the image is transparent space
		
		//used to determine where the wall should be in relation to the grid.
		int xP = (x+y) * wallWidth + (tileWidth/2) - graphicsComponent.getWidth();
		int yP = (x-y) * transparentSpace - (2*graphicsComponent.getWidth() - transparentSpace) +2;
		
		xPos = xP;
		yPos = yP;
	}

	public void render(Graphics g){		
		graphicsComponent.draw(g, xPos - Camera.xOffset, yPos - Camera.yOffset);
		graphicsComponent.tick(); //maybe walls are animated
		//TODO will need to render it transparent when player is behind it.
	}
	
}
