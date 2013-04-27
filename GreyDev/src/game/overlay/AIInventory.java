package game.overlay;

import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.items.Item;
import game.entities.items.Recipes;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class AIInventory extends InventoryMenu {

	public AIInventory() {
		super(4, 4);
		Random rand = new Random();
		if(rand.nextInt(20)> 14)
			this.addItem(new Item("VoltaicCell", 0, 0, 7, "Voltaic Cell"));
		if(rand.nextInt(20)> 10)
			this.addItem(new Item("Health", 0, 0, 1, "Health Drank"));

	}
	
	public void render(Graphics g){
		for (int i = 0; i < COLUMNS * (ROWS + 1); i++) {
			inv.get(i).renderScaled(g);
		}
		if (selectedItem != null) {
			selectedItem.render(g); // dragged item
		}
	}
	
	/**
	 * Adds a new item to the inventory grid. If grid is full, drops the item.
	 * 
	 * @param i
	 *            - the item to add.
	 */
	public void addItem(Item i) {
		selectedItem = i;
		objectSelected = true;
		placeItem(findEmptySlot());
		if (objectSelected) {
			dropItem();
			objectSelected = false;
			selectedItem = null;
		}
	}

	public Slot findEmptySlot() {
		for (Slot i : inv) {
			if (i.isEmpty())
				return i;
		}
		return null;
	}


	/**
	 * The tick method. If an object is selected (mouse dragged) it will move to
	 * follow the mouse. If the mouse button is pressed, it will try to place
	 * the currently selected item in the inventory (or drop it, depending on
	 * location). If no item is selected, it will try to select one.
	 */
	public void update() {

		if (InputHandler.leftClick.keyTapped) {
			grabItem(InputHandler.mouseLoc); // pick up item from inventory
			System.out.println(selectedItem);
		} else {
			if (Globals.state == State.gameMenu) {
				Slot s = calcSlot(InputHandler.mouseLoc);
				if (s != null) {
					Item i = s.getItem();
					if (i != null) {
						OverlayManager.displayText = i.name;
					}
				}
				// get text from item from slot from mouse
			}
		}
	}

	/**
	 * Determines which slot has been clicked. Used for placing/removing items
	 * from the inventory grid.
	 * 
	 * @param mouse
	 *            - the location of the mouse click on screen.
	 * @return - the slot (0-inv.size-1) or -1 if the slot is invalid/not a slot
	 */
	private Slot calcSlot(Point2D mouse) {
		for (Slot i : inv) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				return i;
			}
		}
		return null;
	}

	/**
	 * Destroys the selected item.
	 */
	public void dropItem() {

		// if the item was in inventory previously, remove it and set it to
		// null.
		if (inv.indexOf(selectedItem) > 0 && inv.indexOf(selectedItem) < COLUMNS * (ROWS + 1))
			inv.set(inv.indexOf(selectedItem), null);

		selectedItem = null;// no item is currently selected.
		objectSelected = false;
	}

	/**
	 * Picks up an item from within the grid.
	 * 
	 * @param mouse
	 *            - mouse click location
	 * @return the item selected
	 */
	private void grabItem(Point2D mouse) {

		for (Slot i : inv) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				selectedItem = i.grabItem(); // it is selected
				if (selectedItem != null)
					objectSelected = true;
			}
		}
	}

	/**
	 * Places the currently selected item into the grid
	 * 
	 * @param loc
	 *            - slot where the item should be placed
	 */
	private void placeItem(Slot dest) {
		// if the slot loc is invalid, abort!
		if (dest == null)
			return;

		if (dest.add(selectedItem)) {
			selectedItem = null; // item is no longer selected
			objectSelected = false;
		}
	}

	private void setNewInv() {
		inv = new ArrayList<Slot>((ROWS + 1) * COLUMNS);
		for (int i = 0; i < COLUMNS * (ROWS + 1); i++) {
			int col = i % COLUMNS; // because inventory is a 1D arraylist, the
									// 2D grid effect is achieved via math.
			int row = ROWS - i / COLUMNS;

			inv.add(new Slot(slot, (int) (((Camera.width - slot.getWidth() * COLUMNS) + slot.getWidth() * col) * Camera.scale), (int) (row * slot.getHeight() * Camera.scale)));
		}

	}
}