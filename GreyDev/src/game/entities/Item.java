package game.entities;

import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.Graphics;

public class Item extends Entity {
	
	public String name = "GodlyPlateoftheWhale";
	
	
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

}
