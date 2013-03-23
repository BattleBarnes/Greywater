package game.menu;

import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.Player;
import game.entities.components.Item;
import game.entities.components.Sprite;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class InventoryMenu {
	public final int COLUMNS = 2;
	public final int ROWS = 7;
	
	ArrayList<Item> inv = new ArrayList<Item>((ROWS + 1) * COLUMNS);

	private boolean objectSelected = false;
	private Player owner;
	private Item selectedItem;
	private Sprite slot;

	/**
	 * Constructor, sets up the inventory slot image, and fills the inventory
	 * list with nulls (null = empty in this system)
	 */
	public InventoryMenu() {
		slot = new Sprite("invslot", "invslot");
		for (int i = 0; i < COLUMNS*(ROWS+1); i++) {
			inv.add(null);
		}
	}

	/**
	 * Adds a new item to the inventory grid. If grid is full, drops the item.
	 * 
	 * @param i - the item to add.
	 */
	public void addItem(Item i) {
		selectedItem = i;
		objectSelected = true;
		int loc = inv.lastIndexOf(null);
		System.out.println(loc);
		placeItem(loc);
		if(objectSelected){
			dropItem();
			objectSelected = false;
			selectedItem = null;
		}
	}

	/**
	 * Render method, draws the grid and the items in it.
	 * Grid is one sprite tiled repeatedly.
	 * 
	 * Also draws the "selected item" which is the item being dragged currently.
	 * 
	 * @param g - Graphics object
	 */
	public void render(Graphics g) {

		for (int i = 0; i < COLUMNS*(ROWS+1) ; i++) {
			int col = i % COLUMNS; //because inventory is a 1D arraylist, the 2D grid effect
			int row = ROWS - i / COLUMNS; //is achieved via math.
			
			slot.draw(g, (Camera.width - slot.getWidth() * COLUMNS) + slot.getWidth()* col, row * slot.getHeight());
			if (inv.get(i) != null) {
				inv.get(i).render(g);
			}
		}
		
		if (selectedItem != null) {
			selectedItem.render(g); //dragged item
		}
	}

	/**
	 * The drop method relies on knowing the player location, so
	 * this method serves as a sort of secondary constructor
	 * that sets the owner of this inventory (the player).
	 * It must be called in the Core init method (@see Core.java)
	 * @param owner
	 */
	public void setOwner(Player owner){
		this.owner = owner;
	}
	
	/**
	 * The tick method. If an object is selected (mouse dragged) it will move to 
	 * follow the mouse. If the mouse button is pressed, it will try to place the currently selected item
	 * in the inventory (or drop it, depending on location). If no item is selected, it will try to select one.
	 */
	public void update() {
		if (objectSelected) {
			selectedItem.move((int) InputHandler.mouseLoc.getX(), (int) InputHandler.mouseLoc.getY());
			
			if (InputHandler.leftClick.keyTapped) 
				placeItem(calcSlot(InputHandler.mouseLoc));
			
		} else if (InputHandler.leftClick.keyTapped && Globals.state == State.gameMenu) {
			grabItem(InputHandler.mouseLoc);
		}
	}

	/**
	 * Determines which slot has been clicked. Used for placing/removing items from the inventory grid.
	 * @param mouse - the location of the mouse click on screen.
	 * @return - the slot (0-inv.size-1) or -1 if the slot is invalid/not a slot
	 */
	private int calcSlot(Point2D mouse){
		int colPos = (int) (COLUMNS - (Camera.width - mouse.getX()) / slot.getWidth()); //turn arraylist into grid
		int rowPos = (int) (ROWS - ((mouse.getY())- slot.getHeight()) / slot.getHeight());
		
		//if the col/row are not valid (the -10th row, for example)
		if (colPos > COLUMNS || colPos < 0 || rowPos < 0 || rowPos > ROWS + 1) {
			dropItem();//drop it
			return -1; //return invalid slot
		} else {
			return COLUMNS*rowPos + colPos; //otherwise, return the selected slot
		}
	}

	/**
	 * Puts the selected item on the ground near the player.
	 */
	private void dropItem(){
		int x = owner.xPos;
		int y = owner.yPos;
		
		selectedItem.drop(x, y); //set it to the "floor" version
		
		owner.world.setFloorItem(selectedItem); //re-add to the floor item list.
		
		//if the item was in inventory previously, remove it and set it to null.
		if(inv.indexOf(selectedItem) > 0 && inv.indexOf(selectedItem) < COLUMNS*(ROWS+1))
			inv.set(inv.indexOf(selectedItem), null);
		
		selectedItem = null;//no item is currently selected.
		objectSelected = false;
	}
	
	/**
	 * Picks up an item from within the grid.
	 * 
	 * @param mouse - mouse click location
	 * @return the item selected
	 */
	private Item grabItem(Point2D mouse) {
		
		for (Item i : inv) {//check each slot in the inventory
			if (i != null) { //if there is an item in this slot, check it
				if (i.getPhysicsShape().contains(mouse)) { //if collides
					selectedItem = i; //it is selected
					objectSelected = true;
					i = null;
					return selectedItem;
				}
			}
		}
		return null; //if nothing is found, return nothing
	}

	/**
	 * Places the currently selected item into the grid
	 * @param loc - slot where the item should be placed
	 */
	private void placeItem(int loc) {
		//if the slot loc is invalid, abort!
		if(loc < 0 || loc >= (ROWS+1) * COLUMNS)
			return;
		
		int col = loc % COLUMNS; //math to turn the arraylist into a grid
		int row = ROWS - loc / COLUMNS;
		for(Item e:inv){//check each slot
			//if the slot is full
			if(e!= null && e.xPos == (Camera.width - slot.getWidth() * COLUMNS) + slot.getWidth() * col && e.yPos == row * slot.getHeight())
				return; //abort!
		}
		//otherwise, place it in the slot.
		selectedItem.pickUp((Camera.width - slot.getWidth() * COLUMNS) + slot.getWidth() * col, row * slot.getHeight());
		inv.set(loc, selectedItem);
		selectedItem = null; //item is no longer selected
		objectSelected = false;
	}
}
