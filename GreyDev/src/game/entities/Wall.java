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
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Wall extends Entity {

	double tileWidth;
	double tileHeight;

	double xDepth;
	double wallWidth;
	double transparentSpace;
	double isoAngle;

	Player player;

	boolean west;

	/**
	 * 
	 * Constructor, sets up the polygon off the grid, moves it on to the grid,
	 * sets up the sprite offsets and graphics component.
	 * 
	 * @param x - the starting x position of the bounding box
	 * @param y - the starting y position of the bounding box
	 * @param wall - the sprite that this wall will be represented by
	 * @param isoAngle - the slope of the wall
	 * @param tileWidth - used for aligning it to the floor grid.
	 */
	public Wall(double x, double y, Sprite wall, double isoAngle, double tileWidth, double tileHeight, Player p, boolean west) {
		this.west = west;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.graphicsComponent = wall;
		player = p;

		// math stuff used to set up xP and yP
		this.isoAngle = isoAngle;
		xDepth = graphicsComponent.getWidth() / 6; // depth of the wall in pixels (thin side, not the face)
		wallWidth = graphicsComponent.getWidth() - xDepth; // length of the face of the wall
		transparentSpace = (int) (wallWidth * isoAngle); // how much of the image is transparent space

		physicsComponent = new Tangible(x, y, (int)tileWidth, (int)tileHeight, 0);

		spriteXOff = (int) (-(4 * tileWidth) - xDepth / 2 + 5);
		spriteYOff = (int) (-(4 * tileHeight) - xDepth + 10);

	}

	/**
	 * Render the wall, draws it transparent if player is behind the wall.
	 * 
	 * @param g - graphics object
	 * @param pDepth - Depth of the player from Entity.getDepth();
	 */
	public void render(Graphics g) {
		
	if(Globals.DEVMODE != 1){
			 g.setColor(Color.green);
			 Rectangle2D r = this.getPhysicsShape();
			 g.drawRect((int)r.getX() - 500, (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
			 if(Globals.DEVMODE == 0)
				 return;

	}

		graphicsComponent.tick(); // maybe walls are animated

		// if player is in front of wall, render normally
		Point2D p = Globals.getIsoCoords(getX() + spriteXOff, getY() + spriteYOff);

		if (getX() == 0 || getY() == 0) {
			graphicsComponent.render(g, (int)Math.round(p.getX())  - Camera.xOffset, (int)Math.round(p.getY()) - Camera.yOffset);
			return;
		}
		if (this.getY() < player.getY()) {
			graphicsComponent.render(g, (int)Math.round(p.getX())  - Camera.xOffset, (int)Math.round(p.getY()) - Camera.yOffset);
			return;

		} else {
			if (getX() < player.getX()) {
				graphicsComponent.render(g, (int)Math.round(p.getX())  - Camera.xOffset, (int)Math.round(p.getY()) - Camera.yOffset);
				return;
			}

		}

		Point2D plgrid = new Point2D.Double(player.getX(), player.getY());
		Point2D pwgrid = new Point2D.Double(getX(), getY());
		int distance = (int) Globals.distance(plgrid, pwgrid);
		distance = (int) Math.round( distance / Globals.tileHeight);

		if (distance > 10)
			graphicsComponent.render(g,(int)Math.round(p.getX() ) - Camera.xOffset, (int)Math.round(p.getY()) - Camera.yOffset);
		else {
			graphicsComponent.drawTransparent(g, Math.round((int)p.getX())  - Camera.xOffset, (int)Math.round(p.getY()) - Camera.yOffset, 0.3f);
		}

	}
	
	public void printName(){
		System.out.println(graphicsComponent.getCurrentImageName());
	}

}
