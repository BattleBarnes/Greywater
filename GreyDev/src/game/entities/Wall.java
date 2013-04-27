/**
 * WALL
 * 
 * Walls handle fairly differently based on directionality, so it is important to put in the right slope
 * Positive vs negative. Walls point up from left to right will have a positive slope, walls pointing down
 * will have a negative slope.
 * 
 * This, like the tile, is a special case of the Entity family, in that it never update/ticks because it doesn't move.
 * 
 * PLEASE SEE THE ENTITY CLASS FOR INFORMATION ON THE ISOMETRIC DANCE.
 * 
 * @author Jeremy Barnes  - Jan 20/13
 * 
 */
package game.entities;

import game.Globals;
import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class Wall extends Entity{
	
	int tileWidth;
	int tileHeight;

	int xDepth;
	int wallWidth;
	int transparentSpace;
	double isoAngle;
	
	Player player;
	
	boolean west;


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
	public Wall(int x, int y, Sprite wall, double isoAngle, int tileWidth, int tileHeight, Player p, boolean west) {
		this.west = west;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.graphicsComponent = wall;
		player = p;
		
		//math stuff used to set up xP and yP
		this.isoAngle = isoAngle;
		xDepth = graphicsComponent.getWidth() /6; //depth of the wall in pixels (thin side, not the face)
		wallWidth = graphicsComponent.getWidth() - xDepth; //length of the face of the wall
		transparentSpace = (int) (wallWidth * isoAngle); // how much of the image is transparent space
		

		physicsComponent = new Tangible(x,y, tileWidth, tileHeight, 0);

		spriteXOff =  -(4*tileWidth) - xDepth/2 + 5;
		spriteYOff = -(4*tileHeight) - xDepth + 10;

	}

	/**
	 * Render the wall, draws it transparent if player is behind the wall.
	 * @param g - graphics object
	 * @param pDepth - Depth of the player from Entity.getDepth();
	 */
	public void render(Graphics g){		

		graphicsComponent.tick(); //maybe walls are animated
		
		//if player is in front of wall, render normally
		Point p = Globals.getIsoCoords(getX() + spriteXOff, getY() + spriteYOff);
		
		if(getX() == 0 || getY() == 0){
			graphicsComponent.render(g, p.x - Camera.xOffset, p.y - Camera.yOffset);
			return;
		}
		
		//if(!west){
			if(this.getY() < player.getY()){
				graphicsComponent.render(g, p.x - Camera.xOffset, p.y - Camera.yOffset);
				return;
		//	}
			
		}else{
			if(getX() < player.getX()){
				graphicsComponent.render(g, p.x - Camera.xOffset, p.y - Camera.yOffset);
				return;
			}

		}
			
		
		Point2D plgrid = new Point2D.Double(player.getX(), player.getY());
		Point2D pwgrid = new Point2D.Double(getX(), getY());
		int distance = (int)Globals.distance(plgrid,pwgrid);
		distance = distance/Globals.tileHeight;
		
		if(distance > 20)
			graphicsComponent.render(g, p.x - Camera.xOffset, p.y - Camera.yOffset);
		else{
			graphicsComponent.drawTransparent(g, p.x - Camera.xOffset, p.y - Camera.yOffset, 0.3f);
		}


	}
	
	
}
