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
import java.awt.geom.Point2D;
/**
 * HUDge.java
 * @author Jeremy Barnes 4/25/13
 *
 * Heads up display manager, manages health and mana level display, displays the HUD body and displaytext for things like item stats etc.
 * If you get the reference to DA HUUUUUDGE, well done. How do you type with boxing gloves on, anyways?
 *
 */
public class HUDge {
	
	Sprite hud = new Sprite("hud", "hud");
	Sprite health;
	Sprite mana;
	int currHp = 100;
	int currMana = 100;;
	String drawText = "";
	Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT,35);
	Button inv;


	public HUDge(){
		inv = new Button((int)(Camera.actWidth/2 - 45), (Camera.actHeight - hud.getHeight()), 90,90);
		health = new Sprite("hp0", "hp0");
		mana = new Sprite("mp100", "mp100");
	}

	/**
	 * Set text that is designated for the center textbox
	 * @param text - text to draw
	 */
	public void drawText(String text){
		drawText=  text;
	}
	
	
	/**
	 * Draw everything, HUD body, indicators, text, etc
	 * @param g
	 */
	public void render(Graphics g){
		g.setFont(menuFont);
		g.setColor(Color.BLACK);
		inv.render(g);

		hud.render(g, 0, (int) (Camera.actHeight - hud.getHeight())); //draw hudbody
		//g.drawRect((int)(Camera.actWidth/2 - 45), (Camera.actHeight - hud.getHeight()), 90,90);
		Point2D p = InputHandler.getScaledMouse();
		if(p!=null)
		g.drawOval((int) p.getX(), (int) p.getY(), 100, 100);
		health.render(g, 0, (int) (Camera.actHeight - hud.getHeight()) ); //draw the health indicator needle
		mana.render(g, 0, (int) (Camera.actHeight - hud.getHeight()) ); //and mana needle
		FontMetrics fm = g.getFontMetrics(menuFont); 
		int strw = fm.stringWidth(drawText); 
		g.drawString(drawText, (int)(hud.getWidth()/2  - strw/2),(int) (Camera.actHeight - hud.getHeight()/2 + 80)); //draw text to center text box

	}
	
	/**
	 * Updates the status every cycle, processes hp, mp, and user input.
	 * @param hp - healthpoints to display. Can only be between 100 and 0 inclusive
	 * @param mp - manapoints to display. Can only be between 100 and 0 inclusive
	 */
	public void tick(int hpVal, int mpVal){

		if(hpVal > 100)
			hpVal= 100;
		
		if(hpVal < 0)
			hpVal = 0;
		
		if(mpVal < 100)
			mpVal =100;
		
		if(mpVal < 0)
			mpVal = 0;
		

		health.forceImage("hp"+(hpVal));
		mana.forceImage("mp"+mpVal);
		
		if(InputHandler.leftClick.keyTapped){ 
			System.out.println("Tap");
			if(inv.getPhysicsShape().contains(InputHandler.leftClick.location)){ //if clicked the inventory button
				System.out.println("Contain");
				if(Globals.state != State.gameMenu) //toggle the inventory screen
					Globals.state = State.gameMenu;
				else
					Globals.state = State.inGame;
			}
		}
	}

}//end class
