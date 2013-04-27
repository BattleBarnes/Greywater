package game.entities.items;

public class Recipes {
	

	public static final int WRENCH = 5;
	public static final int VOLTAICCELL = 7;
	public static final int ELECTRICTRUNCHEON = 12;

	public static Item craft(int newItem){
		Item crafted = null;
		switch(newItem){
			case (ELECTRICTRUNCHEON): crafted = new Item("ElectricWrench", 0, 0, 12, "Electric Arc Truncheon"); break;
			default: crafted = null;
		}
		return crafted;
	}
}
