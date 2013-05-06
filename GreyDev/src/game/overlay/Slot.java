package game.overlay;

import game.entities.components.Entity;
import game.entities.components.Sprite;
import game.entities.components.Tangible;
import game.entities.items.Item;

import java.awt.Graphics;

public class Slot extends Entity {
	private Item held;

	public Slot(Sprite slot, int x, int y) {
		physicsComponent = new Tangible(x, y, slot.getWidth(), slot.getHeight(), 0);
		graphicsComponent = slot;
	}

	public boolean add(Item i) {
		if (held == null) {
			held = i;
			i.setLocation(getX(), getY());
		} else
			return false;
		return true;
	}

	public Item grabItem() {
		Item h = held;
		held = null;
		return h;
	}

	public void render(Graphics g) {
		graphicsComponent.render(g, getX(), getY());
		if (held != null)
			held.render(g);
	}

	public boolean isEmpty() {
		if (held != null)
			return false;
		return true;
	}

	public Item getItem() {
		if (held != null)
			return held;
		return null;
	}

}
