package game.overlay;

import game.engine.Camera;
import game.entities.components.Sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class HUDge {
	
	Sprite hud = new Sprite("hud", "hud");
	String drawText = "";
	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 40);

	public HUDge(){
		
	}
	
	public void drawText(String text){
		drawText=  text;
	}
	
	
	public void render(Graphics g){
		g.setFont(menuFont);
		g.setColor(Color.BLACK);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(Camera.scale, Camera.scale);
		hud.render(g2, 0, (int) (Camera.height/Camera.scale - hud.getHeight()) );
		g2.drawString(drawText, (int)(hud.getWidth()/2  - drawText.length()*10),(int) (Camera.height/Camera.scale - hud.getHeight()/2 + 80));
		g2.scale(1/Camera.scale, 1/Camera.scale);

	}

}
