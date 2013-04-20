package game.entities.items;

public class Recipes {
	
	public static final int WRENCH = 5;
	public static final int VOLTAICCELL = 7;
	public static final int ELECTRICTRUNCHEON = 13;
	
	public static Item craft(Item i1, Item i2){
		int newItem = i1.itemID + i2.itemID;
		Item crafted = null;
		switch(newItem){
			case(WRENCH):crafted = new Item("Wrench", 0, 0);
			case(VOLTAICCELL): crafted = new Item("Voltaic Cell", 0 ,0);
			case (ELECTRICTRUNCHEON): crafted = new Item("Electric Truncheon", 0, 0);
		}
		return crafted;
	}

}
