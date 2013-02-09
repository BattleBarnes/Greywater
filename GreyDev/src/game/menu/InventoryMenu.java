package game.menu;

import game.Core;
import game.engine.Camera;
import game.entities.components.Sprite;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class InventoryMenu{
	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 40);
	FontMetrics fm;
	Core parent;
	Sprite m;
	
	public InventoryMenu(Core parent, Sprite m){
		this.m  = m;
		this.parent = parent;
	}
	
	

	public void render(Graphics g) {
		if(fm == null){
			fm = g.getFontMetrics(menuFont);
		}
		
		Rectangle vp = Camera.viewPort;
		m.draw(g,  vp.width - m.getWidth(), 0);
	}
	
	

}
