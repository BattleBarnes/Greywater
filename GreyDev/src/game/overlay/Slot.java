package game.overlay;

import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.entities.items.Item;

import java.awt.Graphics;

/**
 * Slot.java
 * @author Jeremy Barnes 4/10/13
 * 
 * Inventory slot, maintains an image (what the slot looks like), and a reference to the Item that it holds.
 * Basically a button that also has an item. Could allow stacking if Item became Item[stacksize].
 *
 */
public class Slot extends Entity {
	//The item that is in this slot
	private Item held;

	/**
	 * Constructor!
	 * @param slot - slot image
	 * @param x - slot location on screen
	 * @param y - also denotes physical hitbox location for mouse collisions.
	 */
	public Slot(Sprite slot, double x, double y) {
		physicsComponent = new Tangible(x, y, slot.getWidth(), slot.getHeight(), 0);
		graphicsComponent = slot;
	}

	/**
	 * Adds an item to this slot if the slot isn't full.
	 * @param i - Item to add
	 * @return true if item was successfully added, false if it wasn't.
	 */
	public boolean add(Item i) {
		if (held == null) { //only add if not currently holding something
			held = i;
			i.setLocation(getX(), getY()); //item gets same location as slot
		} else 
			return false;
		
		return true;
	}

	/**
	 * Removes the item currently in this slot
	 * @return the item formerly held by this slot.
	 */
	public Item grabItem() {
		Item h = held;
		held = null;
		return h;
	}

	/**
	 * Draw the slot and any item held by the slot.
	 */
	public void render(Graphics g) {
	
		graphicsComponent.render(g, (int)getX(), (int)getY());
		if (held != null)
			held.render(g);
	}

	/**
	 * @return whether or not this slot is empty
	 */
	public boolean isEmpty() {
		if (held != null)
			return false;
		
		return true;
	}

	/**
	 * Returns the item in this slot without removing it from the slot. Like peek in a stack.
	 * @return the item in this slot, or null if there is not an item.
	 */
	public Item getItem() {
		if (held != null)
			return held;
		
		return null;
	}

}
