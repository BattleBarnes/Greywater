package game.overlay;

import game.Globals;
import game.engine.Camera;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.components.Sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class HUDge {
	
	Sprite hud = new Sprite("hud", "hud");
	String drawText = "";
	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT,35);
	Button inv;

	public HUDge(){
		inv = new Button((int)(hud.getWidth()/2*Camera.scale - 45), (int)(Camera.height - hud.getHeight()*Camera.scale), 90,90);
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
		
		FontMetrics fm = g.getFontMetrics(menuFont); // or another font
		int strw = fm.stringWidth(drawText);
		
		g2.drawString(drawText, (int)(hud.getWidth()/2  - strw/2),(int) (Camera.height/Camera.scale - hud.getHeight()/2 + 80));
		g2.scale(1/Camera.scale, 1/Camera.scale);

	}
	
	public void update(){
		if(InputHandler.leftClick.keyTapped){
			if(inv.getPhysicsShape().contains(InputHandler.mouseLoc)){
				if(Globals.state != State.gameMenu)
					Globals.state = State.gameMenu;
				else
					Globals.state = State.inGame;
				System.out.println("Clcik!");
			}
		}
	}

}
