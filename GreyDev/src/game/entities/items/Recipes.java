package game.entities.items;

public class Recipes {
	
	public static final int WRENCH = 5;
	public static final int VOLTAICCELL = 7;
	public static final int ELECTRICTRUNCHEON = 12;
	
	public static Item craft(int newItem){
		Item crafted = null;
		switch(newItem){
			case(WRENCH):crafted = new Item("Wrench", 0, 0, 5, "Shop Wrench"); break;
			case(VOLTAICCELL): crafted = new Item("VoltaicCell", 0 ,0, 7, "Voltaic Cell"); break;
			case (ELECTRICTRUNCHEON): crafted = new Item("electricwrench", 0, 0, 12, "Electric Arc Club"); break;
			default: crafted = null;
		}
		return crafted;
	}

}
