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
	Sprite health;
	Sprite mana;
	int currHp = 100;
	int currMana = 100;;
	String drawText = "";
	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT,35);
	Button inv;
	Sprite[] hpSprites = {new Sprite("hp0", "hp0"),new Sprite("hp10", "hp10"), new Sprite("hp20", "hp20"), new Sprite("hp30", "hp30"), new Sprite("hp40", "hp40"),new Sprite("hp50", "hp50"), new Sprite("hp60", "hp60"), new Sprite("hp70", "hp70"), new Sprite("hp80", "hp80"), new Sprite("hp90", "hp90"), new Sprite("hp100", "hp100")};

	Sprite[] mpSprites = {new Sprite("mp0", "mp0"),new Sprite("mp10","mp10"), new Sprite("mp20", "mp20"), new Sprite("mp30", "mp30"), new Sprite("mp40", "mp40"),new Sprite("mp50", "mp50"), new Sprite("mp60", "mp60"), new Sprite("mp70", "mp70"), new Sprite("mp80", "mp80"), new Sprite("mp90", "mp90"), new Sprite("mp100", "mp100")};


	public HUDge(){
		inv = new Button((int)(hud.getWidth()/2*Camera.scale - 45), (int)(Camera.height - hud.getHeight()*Camera.scale), 90,90);
		health = new Sprite("hp100", "hp100");
	//	mana = new Sprite("mp100", "mp100");
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
		health.render(g2, 0, (int) (Camera.height/Camera.scale - hud.getHeight()) );
	//	mana.render(g2, 0, (int) (Camera.height/Camera.scale - hud.getHeight()) );
		FontMetrics fm = g.getFontMetrics(menuFont); // or another font
		int strw = fm.stringWidth(drawText);
		
		g2.drawString(drawText, (int)(hud.getWidth()/2  - strw/2),(int) (Camera.height/Camera.scale - hud.getHeight()/2 + 80));
		g2.scale(1/Camera.scale, 1/Camera.scale);

	}
	
	public void update(int hp, int mp){
		int hpVal = (hp - hp%10)/10;
		if(hpVal >= 11)
			hpVal = 10;
		
		if(hpVal < 0){
			hpVal = 0;
		}
		health = hpSprites[hpVal];
	//	mana = mpSprites[(mp - mp%10)/10];
		if(InputHandler.leftClick.keyTapped){
			if(inv.getPhysicsShape().contains(InputHandler.mouseLoc)){
				if(Globals.state != State.gameMenu)
					Globals.state = State.gameMenu;
				else
					Globals.state = State.inGame;
			}
		}
	}

}
