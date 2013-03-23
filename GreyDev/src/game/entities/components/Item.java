package game.entities.components;

import game.engine.Camera;

import java.awt.Graphics;

public class Item extends Entity {
	
	public String name = "GodlyPlateoftheWhale";
	private boolean onFloor;
	private Sprite floorSprite;
	private Sprite invSprite;
	
	
	public Item(String name, int x, int y){
		this.name = name;
		floorSprite = new Sprite(name+"Floor", name+"Floor");
		invSprite = new Sprite(name+"Inventory", name+"Inventory");
		graphicsComponent = floorSprite;
		physicsComponent = new Tangible(x, y, floorSprite.getWidth(), floorSprite.getHeight(), 0);
		onFloor = true;
		this.xPos = x;
		this.yPos = y;

	}

	@Override
	protected void getInput() {	
		return;//do nothing
	}
	
	public void pickUp(int x, int y){
		graphicsComponent = invSprite; 		
		onFloor = false;
		xPos = x;
		yPos = y;
		physicsComponent.updateHitSpace(x, y);
	}
	
	public void drop(int x, int y){
		graphicsComponent = floorSprite;
		onFloor = true;
		this.xPos = x;
		this.yPos = y;
		physicsComponent.updateHitSpace(x, y);
	}
	
	public void render(Graphics g){
		if(onFloor)
			super.render(g);
		else
			graphicsComponent.draw(g, xPos, yPos);
	}
	
	public void move(int x, int y){
		this.xPos = x;
		this.yPos = y;
		physicsComponent.updateHitSpace(x, y);
	}

}
