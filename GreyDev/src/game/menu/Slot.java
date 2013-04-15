package game.menu;

import game.Globals;
import game.engine.Camera;
import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.entities.items.Item;

import java.awt.Graphics;
import java.awt.Point;

public class Slot extends Entity {
	private Item held;
	
	public Slot(Sprite slot, int x, int y){
		physicsComponent = new Tangible(x, y, ((int)(Camera.scale * slot.getWidth())),((int)( Camera.scale* slot.getHeight())), 0);
		graphicsComponent = slot;
	}
	
	public boolean add(Item i){
		if(held == null){
			held = i;
			held.setLocation(getX(), getY());
		}
		else
			return false;
		return true;
	}
	
	public Item getItem(){
		Item h = held;
		held = null;
		return h;
	}
	
	public void render(Graphics g){
		graphicsComponent.render(g, getX(), getY());
		if(held!=null)
			held.render(g);
	}
	
	
	public void renderScaled(Graphics g){
			graphicsComponent.renderScaled(g, getX(), getY());
			if(held!=null)
				held.renderScaled(g);
	}
	
	public boolean isEmpty(){
		if(held!= null)
			return false;
		return true;
	}
	

}
