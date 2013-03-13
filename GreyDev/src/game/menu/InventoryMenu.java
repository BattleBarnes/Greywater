package game.menu;

import game.Core;
import game.engine.Camera;
import game.entities.components.Sprite;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class InventoryMenu{
	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 40);
	FontMetrics fm;
	Core parent;
	ArrayList<Integer> inv = new ArrayList<>(40);
	Sprite slot;
	
	public InventoryMenu(Core parent){
		this.parent = parent;
		slot = new Sprite("invslot", "invslot");
		for(int i = 0; i < 40; i++){
			inv.add(0);
		}
		
	}
	
	

	public void render(Graphics g) {
		if(fm == null){
			fm = g.getFontMetrics(menuFont);
		}
				
		Rectangle vp = Camera.viewPort;
	
		for(int i = 0; i < 40; i++){
			int col = i%5;
			int row = 5 - i/5;
			slot.draw(g,  (vp.width - slot.getWidth() * 5) + slot.getWidth()*col, row*slot.getHeight());
			//	if(inv.get(i) ==1)
					//gpow.draw(g,  (vp.width - slot.getWidth() * 5) + slot.getWidth()*col, row*slot.getHeight());
			}
	}
	
	
	public void addItem(int i){
		inv.set(0, 1);
		System.out.println("Added");
	}
	

}
