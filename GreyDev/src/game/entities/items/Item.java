package game.entities.items;

import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.Graphics;

public class Item extends Entity {
	
	public String name = "GodlyPlateoftheGlitch";
	public int itemID = 00;
	
	
	public Item(String name, int x, int y){
		this.name = name;
		graphicsComponent = new Sprite(name+"Inventory", name+"Inventory");
		physicsComponent = new Tangible(x, y, graphicsComponent.getWidth(), graphicsComponent.getHeight(), 0);
	}

	
	public void pickUp(int x, int y){
		physicsComponent.updateHitSpace(x, y);
	}
	
	
	public void render(Graphics g){
		graphicsComponent.render(g, getX(), getY());
	}
	
	public void move(int x, int y){
		physicsComponent.updateHitSpace(x, y);
	}


	public void renderScaled(Graphics g) {
		graphicsComponent.renderScaled(g, getX(), getY());
		
	}
	
	public String showText(){
		return name;
	}

}
