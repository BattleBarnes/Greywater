package game.overlay;

import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.engine.audio.SoundLoader;
import game.entities.components.Sprite;
import game.entities.items.Crafting;
import game.entities.items.Item;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * InventoryMenu.java
 * 
 * @author Barnes 4/19/13
 * 
 *  Used to represent and manage all the images and logic for player inventory and items.
 *  See also - Slot.java, Button.java, and Crafting.java
 * 
 */
public class InventoryMenu {
	public final int COLUMNS;// = 6; number of columns of slots
	public final int ROWS;// = 5; number of rows of slots

	/* ********* Logical Slots used to actually hold things and provide functionality *** */
	protected ArrayList<Slot> inv; // arraylist of slots is the real inventory
	private ArrayList<Slot> equip = new ArrayList<Slot>(); // equipped items array. weapon, armor, trinket, offhand
	private ArrayList<Slot> craftarea = new ArrayList<Slot>(); // 4 slots for crafting type purposes
	private Slot craftOutput; // single slot for crafted output

	protected boolean objectSelected = false; // whether or not the user has picked an object to move between slots (mouse slot)
	protected Item selectedItem; // what the picked up object is (mouse slot)

	/* ***** Sprites - Used to represent the various aspects of the inventory menu **** */
	protected Sprite slot = new Sprite("invslot", "invslot");;
	protected Sprite craft = new Sprite("Crafting", "Crafting");
	protected Sprite weap = new Sprite("WeaponSlot", "WeaponSlot");
	private Sprite area = new Sprite("inv", "inv"); // back-board used to hold the inventory

	public int buff;// total buff from items (as in buff/debuff)

	/**
	 * Constructor, sets up the inventory slot image, and fills the inventory
	 * list with nulls (null = empty in this system)
	 * This one's for the player.
	 */
	public InventoryMenu() {
		COLUMNS = 6;
		ROWS = 5;

		setNewInv(Camera.actWidth); // create the inventory grid

		// set up crafting area
		for (int i = 0; i < 4; i++) {
			int col = i % 2; // because inventory sections are 1D arraylists, the 2D grid effect is achieved via math.
			int row = 2 - i / 2;

			craftarea.add(new Slot(craft, Camera.actWidth - (area.getWidth() / 2 + craft.getWidth() - craft.getWidth() * col), (60 + row * craft.getHeight())));
		}

		// set up equipment slots
		equip.add(new Slot(weap, Camera.actWidth - (area.getWidth() / 2 + craft.getWidth() - weap.getWidth() / 2), 20));

		// add craft output slot
		craftOutput = new Slot(slot, craftarea.get(3).getX() + 100, craftarea.get(3).getY());

		// Tavish always has his motherfucking wrench
		this.addItem(new Item("Wrench", 0, 0, 5, "Wrench"));
	}

	/**
	 * Used for setting up NPC inventory grids.
	 * 
	 * @param r - rows the inventory grid should have
	 * @param c - columns the grid should have
	 */
	public InventoryMenu(int r, int c) {
		slot = new Sprite("invslot", "invslot");

		ROWS = r;
		COLUMNS = c;

		setNewInv(Camera.actWidth / 4);
	}

	/**
	 * Adds a new item to the inventory grid. If grid is full, drops the item.
	 * 
	 * @param i - the item to add.
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

	/**
	 * Get the item in the weapons slot of the equipment array
	 * 
	 * @return whatever Item is in the equip slot, null if empty
	 */
	public Item getWeap() {
		return equip.get(0).getItem();
	}

	/**
	 * Render method, draws the grid and the items in it. Grid is one sprite
	 * tiled repeatedly.
	 * 
	 * Also draws the "selected item" which is the item being dragged currently.
	 * 
	 * @param g
	 * 
	 */
	public void render(Graphics g) {
		area.render(g, Camera.actWidth - area.getWidth(), 0);
		for (int i = 0; i < COLUMNS * (ROWS); i++) {
			inv.get(i).render(g);
		}
		for (Slot s : craftarea)
			s.render(g);

		for (Slot s : equip)
			s.render(g);

		craftOutput.render(g);
		if (selectedItem != null) {
			selectedItem.render(g); // dragged item
		}
	}

	/**
	 * The tick method. If an object is selected (mouse dragged) it will move to
	 * follow the mouse. If the mouse button is pressed, it will try to place
	 * the currently selected item in the inventory (or drop it, depending on
	 * location). If no item is selected, it will try to select one.
	 */
	public void update() {
		if (objectSelected) {// make selection follow the mouse
			if (InputHandler.leftClick.keyTapped) {
				placeItem(calcSlot(InputHandler.leftClick.location)); // place it if mouse
				InputHandler.leftClick.heldDown = false;
				return;
			}
			selectedItem.move((int) InputHandler.getScaledMouse().getX(), (int) InputHandler.getScaledMouse().getY());

		} else if (InputHandler.leftClick.keyTapped && Globals.state == State.gameMenu) {
			grabItem(InputHandler.leftClick.location); // pick up item from inventory
		

			if (!objectSelected && craftOutput.getPhysicsShape().contains(InputHandler.leftClick.location)) {
				if (!craftOutput.isEmpty()) {
					SoundLoader.playSingle("Craft");
					objectSelected = true;
					selectedItem = craftOutput.grabItem();
					
					for (Slot s : craftarea) {
						s.grabItem();
					}
					InputHandler.leftClick.heldDown = false;
				}
				InputHandler.leftClick.heldDown = false;
			}
			
		} else if (InputHandler.rightClick.keyTapped && Globals.state == State.gameMenu) {
			Slot s = calcSlot(InputHandler.rightClick.location);
			if (s != null && !s.isEmpty()) {
				if (s.getItem().itemID == 1) {
					buff = s.getItem().use();
					s.grabItem();
					SoundLoader.playSingle("Drink");
				}
			}
		} else {
			if (Globals.state == State.gameMenu) {
				Slot s = calcSlot(InputHandler.getScaledMouse());
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
	 * @param mouse - the location of the mouse click on screen.
	 * @return - the slot (0-inv.size-1) or -1 if the slot is invalid/not a slot
	 */
	private Slot calcSlot(Point2D mouse) {
		for (Slot i : inv) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				return i;
			}
		}
		for (Slot i : equip) {
			if (i.getPhysicsShape().contains(mouse)) {
				return i;
			}
		}
		for (Slot i : craftarea) {
			if (i.getPhysicsShape().contains(mouse)) {
				return i;
			}
		}
		if (mouse.getX() < Camera.actWidth - area.getWidth())
			dropItem();
		return null;
	}

	/**
	 * Checks the recipe list to see if the items in the crafting slots
	 * make anything new. If they do, it puts the result in the craftoutput slot, so
	 * that you can see before you craft. Somewhere else needs to clear the crafting slots when the output
	 * is picked up.
	 */
	private void craft() {
		int s1 = 0;
		int s2 = 0;
		int s3 = 0;
		int s4 = 0;

		if (!craftarea.get(0).isEmpty()) {
			s1 = craftarea.get(0).getItem().itemID;
		}
		if (!craftarea.get(1).isEmpty()) {
			s2 = craftarea.get(1).getItem().itemID;
		}
		if (!craftarea.get(2).isEmpty()) {
			s3 = craftarea.get(2).getItem().itemID;
		}
		if (!craftarea.get(3).isEmpty()) {
			s4 = craftarea.get(3).getItem().itemID;
		}
		int[] newItem = { s1, s2, s3, s4 };

		if (craftOutput.isEmpty()) {//TODO FIX THIS SHIT (only updates if craft out is empty, if remove items from recipe, nothing bad happens)

			Item n = Crafting.craft(newItem); // see if the recipe in the craft area is a thing

			if (n != null) { // if it is
				if (craftOutput.isEmpty()) // and the output slot is empty
					craftOutput.add(n); // put the output in the output slot

			}
		}
	}

	/**
	 * Destroys the selected item.
	 */
	private void dropItem() {
		
		// if the item was in inventory previously, remove it and set it to null.
		if (inv.indexOf(selectedItem) > 0 && inv.indexOf(selectedItem) < COLUMNS * (ROWS))
			inv.set(inv.indexOf(selectedItem), null);

		selectedItem = null;// no item is currently selected.
		objectSelected = false;
	}

	private Slot findEmptySlot() {
		for (Slot i : inv) {
			if (i.isEmpty())
				return i;
		}
		return null;
	}

	/**
	 * Picks up an item from within the grid.
	 * 
	 * @param mouse - mouse click location
	 * @return the item selected
	 */
	private void grabItem(Point2D mouse) {

		for (Slot i : inv) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				selectedItem = i.grabItem(); // it is selected
				if (selectedItem != null)
					objectSelected = true;
				InputHandler.leftClick.heldDown = false;
			}
		}

		for (Slot i : craftarea) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				selectedItem = i.grabItem(); // it is selected
				if (selectedItem != null)
					objectSelected = true;
				InputHandler.leftClick.heldDown = false;
			}
		}

		for (Slot i : equip) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				selectedItem = i.grabItem(); // it is selected
				if (selectedItem != null)
					objectSelected = true;
				InputHandler.leftClick.heldDown = false;
			}
		}

		if (craftOutput != null)
			if (craftOutput.getPhysicsShape().contains(mouse)) {
				selectedItem = craftOutput.grabItem(); // it is selected
				if (selectedItem != null) {
					for (Slot s : craftarea) {
						s.grabItem();
					}
					objectSelected = true;
					InputHandler.leftClick.heldDown = false;
				}
			}

	}

	/**
	 * Places the currently selected item into the grid and removes it from the mouse slot.
	 * Does nothing if the slot isn't real.
	 * 
	 * @param loc - slot where the item should be placed
	 */
	private void placeItem(Slot dest) {
		// if the slot loc is invalid, abort!
		if (dest == null)
			return;
		if (dest.add(selectedItem)) {
			if(selectedItem!= null && selectedItem.itemID == 1)
				SoundLoader.playSingle("Glass");
			selectedItem = null; // item is no longer selected
			objectSelected = false;
		}
		craft();
	}

	/**
	 * Avoiding duplicate code for the constructors, sets up a new grid of slots that is ROWS x COLUMNS.
	 * The right side is anchored to StartX
	 * 
	 * @param startX - the right side of the grid
	 */
	private void setNewInv(int startX) {
		inv = new ArrayList<Slot>((ROWS) * COLUMNS);
		for (int i = 0; i < COLUMNS * (ROWS); i++) {
			int col = i % COLUMNS; // because inventory is a 1D arraylist, the 2D grid effect is achieved via math.
			int row = ROWS - 1 - i / COLUMNS;
			
			//I don't know, it fits. Don't try to understand the math.
			inv.add(new Slot(slot, startX - (slot.getWidth() * (COLUMNS + 1) - (slot.getWidth() * col) - 30), (area.getHeight() / 2. + row * slot.getHeight() - 37)));
		}

	}
}
