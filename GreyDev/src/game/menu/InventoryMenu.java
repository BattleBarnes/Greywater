package game.menu;

import game.Core;
import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Item;
import game.entities.components.Sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class InventoryMenu {
	Core parent;
	boolean objectSelected = false;
	Item selectedItem;

	public final int COLUMNS = 2;
	public final int ROWS = 7;

	ArrayList<Item> inv = new ArrayList<Item>((ROWS + 1) * COLUMNS);
	Sprite slot;

	public InventoryMenu(Core parent) {
		this.parent = parent;
		slot = new Sprite("invslot", "invslot");
		for (int i = 0; i < COLUMNS*(ROWS+1); i++) {
			inv.add(null);
		}
		addItem(new Item("GodlyPlateoftheWhale", 1000, 600));
	}

	public void update() {
		if (objectSelected) {
			selectedItem.move((int) InputHandler.mouseLoc.getX(),
					(int) InputHandler.mouseLoc.getY());
			if (InputHandler.leftClick.keyTapped) {
				placeItem(calcSlot(InputHandler.mouseLoc));
			}
		} else if (InputHandler.leftClick.keyTapped
				&& Globals.state == State.gameMenu) {
			grabItem(InputHandler.mouseLoc);
		}
	}

	public void render(Graphics g) {

		for (int i = 0; i < COLUMNS*(ROWS+1) ; i++) {
			int col = i % COLUMNS;
			int row = ROWS - i / COLUMNS;

			g.setColor(Color.RED);
			
			slot.draw(g, (Camera.width - slot.getWidth() * COLUMNS) + slot.getWidth()* col, row * slot.getHeight());
			if (inv.get(i) != null) {
				inv.get(i).render(g);
			}
		}
		if (selectedItem != null) {
			selectedItem.render(g);
		}
	}

	public void addItem(Item i) {
		selectedItem = i;
		objectSelected = true;
		int loc = inv.lastIndexOf(null);
		placeItem(loc);
			
		
	}

	private Item grabItem(Point2D mouse) {
		for (Item i : inv) {
			if (i != null) {
				if (i.getPhysicsShape().contains(mouse)) {
					Item pick = i;
					selectedItem = pick;
					objectSelected = true;
					i = null;
					return pick;
				}
			}
		}
		return null;
	}

	private void placeItem(int loc) {
		if(loc < 0 || loc >= (ROWS+1) * COLUMNS)
			return;
		System.out.println((ROWS +1)  * COLUMNS);
		int col = loc % COLUMNS;
		int row = ROWS - loc / COLUMNS;
		selectedItem.pickUp((Camera.width - slot.getWidth() * COLUMNS) + slot.getWidth() * col, row * slot.getHeight());
		
		inv.set(loc, selectedItem);
		selectedItem = null;
		objectSelected = false;
	}
	
	private int calcSlot(Point2D mouse){
		int colPos = (int) (COLUMNS - (Camera.width - mouse.getX()) / slot.getWidth());
		int rowPos = (int) (ROWS - ((mouse.getY())- slot.getHeight()) / slot.getHeight());
		
		
		if (colPos > COLUMNS || colPos < 0 || rowPos < 0 || rowPos > ROWS + 1) {
			dropItem();
			return -1;
		} else {
			return COLUMNS*rowPos + colPos;
		}
		
	}

	
	
	private void dropItem(){
		
	}
}
