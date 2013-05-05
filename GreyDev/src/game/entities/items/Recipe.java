package game.entities.items;

public class Recipe {
	
	public int[] recipe;
	private Item item;
	
	public Recipe(Item thisItem, int[] recip){
		this.recipe = recip;
		this.item = thisItem;
	}
	
	public Item getNewItem(){
		return new Item(item.name+"", item.getX() , item.getY(), item.itemID, item.showText()+"");
	}

}
