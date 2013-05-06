package game.overlay;

import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Sprite;
import game.entities.items.Crafting;
import game.entities.items.Item;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class InventoryMenu {
	public final int COLUMNS;// = 6;
	public final int ROWS;// = 5;

	protected ArrayList<Slot> inv;
	private ArrayList<Slot> equip = new ArrayList<Slot>();
	private ArrayList<Slot> craftarea = new ArrayList<Slot>();
	private Slot craftOutput;

	protected boolean objectSelected = false;
	protected Item selectedItem;
	protected Sprite slot;
	protected Sprite craft;
	protected Sprite weap;
	private Sprite area = new Sprite("inv", "inv");
	public int buff;

	/**
	 * Constructor, sets up the inventory slot image, and fills the inventory
	 * list with nulls (null = empty in this system)
	 * This one's for the player.
	 */
	public InventoryMenu() {
		COLUMNS = 6;
		ROWS = 4;
		slot = new Sprite("invslot", "invslot");
		weap = new Sprite("WeaponSlot", "WeaponSlot");
		craft = new Sprite("Crafting", "Crafting");

		setNewInv(Camera.actWidth);

		for (int i = 0; i < 4; i++) {
			int col = i % 2; // because inventory is a 1D arraylist, the 2D grid effect is achieved via math.
			int row = 2 - i / 2;
			craftarea.add(new Slot(craft, Camera.actWidth - (-1 * craft.getWidth() * col + area.getWidth() / 2 + craft.getWidth()), (60 + row * craft.getHeight())));

		}

		equip.add(new Slot(weap, Camera.actWidth - (-1 * weap.getWidth() / 2 + area.getWidth() / 2 + craft.getWidth()), 20));

		craftOutput = new Slot(slot, craftarea.get(3).getX() + 100, craftarea.get(3).getY());

		this.addItem(new Item("Wrench", 0, 0, 5, "Shop Wrench"));
	}

	public InventoryMenu(int r, int c) {
		slot = new Sprite("invslot", "invslot");

		ROWS = r;
		COLUMNS = c;

	//	int w = Camera.width;
		//Camera.width /= 4;
		setNewInv(Camera.actWidth/4);
	//	Camera.width = w;
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
	 * Render method, draws the grid and the items in it. Grid is one sprite
	 * tiled repeatedly.
	 * 
	 * Also draws the "selected item" which is the item being dragged currently.
	 * 
	 * @param g
	 *            - Graphics object
	 */
	public void render(Graphics g) {
		area.render(g, Camera.actWidth - area.getWidth(), 0);
		for (int i = 0; i < COLUMNS * (ROWS + 1); i++) {
			int col = i % COLUMNS; // because inventory is a 1D arraylist, the 2D grid effect is achieved via math.
			int row = ROWS - i / COLUMNS;
			
			inv.get(i).render(g);

			// Rectangle r = inv.get(i).getPhysicsShape();
			// g.setColor(Color.PINK);
			// g.drawRect(Camera.actWidth - (slot.getWidth()*(COLUMNS + 1) - (slot.getWidth() * col)), (area.getHeight() / 2 + row * slot.getHeight() - 45), 45, 45);

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
				placeItem(calcSlot(InputHandler.getMouse())); // place it if mouse
				System.out.println("Tap place"); // click
				return;
			}
			selectedItem.move((int) InputHandler.getMouse().getX(), (int) InputHandler.getMouse().getY());

		} else if (InputHandler.leftClick.keyTapped && Globals.state == State.gameMenu) {
			grabItem(InputHandler.getMouse()); // pick up item from inventory
			System.out.println(selectedItem);
			if (!objectSelected && craftOutput.getPhysicsShape().contains(InputHandler.getMouse())) {
				if (!craftOutput.isEmpty()) {
					objectSelected = true;
					selectedItem = craftOutput.grabItem();
					for (Slot s : craftarea) {
						s.grabItem();
					}
				}
			}
		} else if (InputHandler.rightClick.keyTapped && Globals.state == State.gameMenu) {
			Slot s = calcSlot(InputHandler.getMouse());
			if (s != null && !s.isEmpty()) {
				if (s.getItem().itemID == 1) {
					buff = s.getItem().use();
					s.grabItem();
				}
			}
		} else {
			if (Globals.state == State.gameMenu) {
				Slot s = calcSlot(InputHandler.getMouse());
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
		return null;
	}

	/**
	 * Destroys the selected item.
	 */
	private void dropItem() {

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

		for (Slot i : craftarea) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				selectedItem = i.grabItem(); // it is selected
				if (selectedItem != null)
					objectSelected = true;
			}
		}

		for (Slot i : equip) {// check each slot in the inventory
			if (i.getPhysicsShape().contains(mouse)) { // if collides
				selectedItem = i.grabItem(); // it is selected
				if (selectedItem != null)
					objectSelected = true;
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
		craft();
		// if the slot loc is invalid, abort!
		if (dest == null)
			return;

		if (dest.add(selectedItem)) {
			selectedItem = null; // item is no longer selected
			objectSelected = false;
		}
		craft();
	}

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
		if (craftOutput.isEmpty()) {

			Item n = Crafting.craft(newItem);

			if (n != null) {
				if (craftOutput.isEmpty())
					craftOutput.add(n);
				else {
					craftOutput.grabItem();// TODO is this line necessary?
					craftOutput.add(n);
				}
			} else {
				craftOutput.grabItem();// TODO is this line necessary?
			}
		}
	}

	public Item getWeap() {
		return equip.get(0).getItem();
	}

	private void setNewInv(int startX) {
		inv = new ArrayList<Slot>((ROWS + 1) * COLUMNS);
		for (int i = 0; i < COLUMNS * (ROWS + 1); i++) {
			int col = i % COLUMNS; // because inventory is a 1D arraylist, the 2D grid effect is achieved via math.
			int row = ROWS - i / COLUMNS;

			inv.add(new Slot(slot, startX - (slot.getWidth()*(COLUMNS + 1) - (slot.getWidth() * col) - 35), (area.getHeight() / 2 + row * slot.getHeight() - 45)));
		}

	}
}
