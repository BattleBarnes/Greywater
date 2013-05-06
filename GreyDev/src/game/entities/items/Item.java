package game.entities.items;

import game.engine.Camera;
import game.entities.Mob;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;

import java.awt.Graphics;

public class Item extends Entity {
	
	public String name = "Fist Fulla Gears";
	public int itemID = 00;
	
	
	public Item(String imgName, int x, int y, int code, String displayName){
		this.name = displayName;
		itemID = code;
		graphicsComponent = new Sprite(imgName, imgName);
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
	
	public String showText(){
		return name;
	}
	
	public int getHitMod(){
		return 5;
	}
	
	public int getDamageMod(){
		if(name.contains("Electric"))
			return 20;
		return 10;
	}
	
	public int use(){
		if(name.contains("Health"))
			return 25;
		else
			return 0;
	}
	

}
