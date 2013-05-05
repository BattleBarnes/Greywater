package game.entities.items;

import java.util.ArrayList;

public class Crafting {
	

	public static ArrayList<Recipe> recipes = new ArrayList(); 
	
	
	public static void init(){
		//recipes.add(new Recipe(thisItem, recip)); TODO this a bunch.
	}
	

	public static Item craft(int[] newItem){
		if(newItem == null)
			return null;
		Item crafted = null;
		for(Recipe recipe:recipes){
			
			ArrayList<Integer> newIt = new ArrayList();
			for(int i = 0; i < 4; i++){
				newIt.add(newItem[i]);
			}
			
			boolean has1 = false;
			boolean has2 = false;
			boolean has3 = false;
			boolean has4 = false;
			
			if(newIt.contains(recipe.recipe[0])){
				int getIndex = newIt.lastIndexOf(recipe.recipe[0]);
				has1 = true;
				newIt.remove(getIndex);
			}
			if(newIt.contains(recipe.recipe[1])){
				int getIndex = newIt.lastIndexOf(recipe.recipe[1]);
				has2 = true;
				newIt.remove(getIndex);
			}
			if(newIt.contains(recipe.recipe[2])){
				int getIndex = newIt.lastIndexOf(recipe.recipe[2]);
				has3 = true;
				newIt.remove(getIndex);
			}
			if(newIt.contains(recipe.recipe[3])){
				int getIndex = newIt.lastIndexOf(recipe.recipe[3]);
				has4 = true;
				newIt.remove(getIndex);
			}
			
			if(has1 && has2 && has3 && has4){
				crafted = recipe.getNewItem();
				return crafted;
			}
		}
		
		return crafted;
	}
}
