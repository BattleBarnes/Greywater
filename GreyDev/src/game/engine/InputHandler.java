/**
 * InputHandler.java keeps track of player input by assigning common actions (forward, back, click, etc) to triggers.
 * The triggers class is an inner class you can find below. Because the triggers themselves are not linked to keys, it is easy to change what
 * button activates what trigger. This will likely be done with a dictionary later. Currently it's done with a big fuckoff set of switch statements.
 * 
 * @author Jeremy Barnes
 */
package game.engine;


import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.event.MouseInputAdapter;

public class InputHandler extends MouseInputAdapter implements KeyListener {

	/**
	 * @author J Barnes
	 * 
	 * Represents a player's input on a given key.
	 * Maintains how many cycles (ticks) it has been pressed, whether or not its held down (pressed for >3 cycles) or tapped
	 * (pressed and let off immediately).
	 * In the case of mouseclicks, the click location is saved in the trigger data as well.
	 *
	 */
	public class Trigger{
		public int ticks; //how many times the button has been pressed
		public int waitTicks; //how long the button has been considered toggled on
		public boolean heldDown; //for mashing a button
		public boolean keyTapped = false; //for one press buttons. (Discrete, not continuous)
		public double xPos;
		public double yPos;
		public Point2D location;
		
		public Trigger(){
			controls.add(this);
		}
		
		public void toggle(boolean pressed){
			if(pressed != heldDown){ 
				heldDown = pressed; //if not pressed, not held down, and vice versa
				keyTapped = pressed;
			}
			if(pressed)
				ticks++;
		}
		
		/**
		 * Mouse triggers
		 * @param pressed - true = clicked, false = released
		 * @param loc - point that was clicked.
		 */
		public void toggle(boolean pressed, Point2D loc){
			xPos = loc.getX();
			yPos = loc.getY();
			location = loc;
						
			if(pressed != heldDown){
				heldDown = pressed; //if button is not pressed, then it is not held down, and vice versa
				keyTapped = pressed;
			}
			if(pressed)
				ticks++; 
		
		}
		
		/**
		 * Update the trigger to check if its been pressed or released or tapped.
		 */
		public void tick(){
			//allows a button to be tapped for 1 tick
			if(waitTicks < ticks + 3 && keyTapped ){
				waitTicks++;
				keyTapped = true;
			}
			else
				keyTapped = false; //then we're holding it down, or not touching it at all.
		}
	}
	


	//list of controls to tick
	private static ArrayList<Trigger> controls = new ArrayList<Trigger>();
	
	
	private static double[] amountScrolled = new double[1];
	
	
	
	/* Controls that are supported by default. They don't actually have to map to their name-d buttons. Spacebar could be activated by "M" 
	 * Note that controls are static because each machine can only have one player, and thus only have one set of inputs. This eliminates unnecessary passing
	 * of an "InputHandler" object because there is only one, everyone can call it. 
	 * Cleaner, better. If you disagree, you are wrong.
	 * 
	 */
	public static Trigger up;  
	public static Trigger down; 
	public static Trigger left; 
	public static Trigger right;
	public static Trigger menu;
	public static Trigger use;
	public static Trigger leftClick;
	private static Point2D mouseLoc = new Point.Double(0,0);
	public static Trigger spaceBar;
	public static Trigger exit;
	public static Trigger rightClick;
	
	//Data that doesn't need a trigger
	public static boolean mouseClicked;
	public static double wheelScrolled;
	public static boolean rightClicked;
	public static boolean wheelClicked;
	
	/**
	 * Constructor called in engine to set up the static class. This is technically bad, but its the exact same as an 
	 * "init" method for a static class, and who really cares? They act the exact same for all intents and purposes.
	 */
	public InputHandler(){
		up = new Trigger();
		down = new Trigger();
		left = new Trigger();
		right = new Trigger();
		menu = new Trigger();
		use = new Trigger();
		leftClick = new Trigger();
		spaceBar = new Trigger();
		exit = new Trigger();
		rightClick = new Trigger();
	}
	
	/**
	 * Sometimes, scaling happens, and so physics objects for menu items don't line up perfectly with the images on screen.
	 * Its a fact of life. The easiest way to deal with it is to simply scale the mouse input by the inverse image scale, so that
	 * clicking on the misplaced image actually clicks on the physics hitbox.
	 * 
	 * @return mouse co-ordinates that match up with *PROGRAM SCALED* images.
	 */
	public static Point2D getScaledMouse(){
		return new Point2D.Double((mouseLoc.getX()/Camera.sscale),( mouseLoc.getY()/Camera.sscale));
	}
	
	
	/**
	 * Sometimes, scaling happens, and so physics objects for menu items don't line up perfectly with the images on screen.
	 * Its a fact of life. The easiest way to deal with it is to simply scale the mouse input by the inverse image scale, so that
	 * clicking on the misplaced image actually clicks on the physics hitbox.
	 * 
	 * @return mouse co-ordinates that match up with *PROGRAM SCALED* images.
	 */
	public static Point2D getScaledMouse(Point2D mouse){
		return new Point2D.Double((mouse.getX()/Camera.sscale),( mouse.getY()/Camera.sscale));
	}
	
	/**
	 * @return the mouse's current co-ordinates on the screen in pixels
	 */
	public static Point2D getMouse(){
		return mouseLoc;
	}
	
	
/* ***********HANDLERS*********************
 *  Most of these methods don't do anything, and are only here because they come with the superclass/interface. I don't like it.
 *  None of them are commented, because if you can't tell what "mouseDragged" means, you shouldn't be programming.
 * 
 */
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseLoc = e.getPoint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseLoc = e.getPoint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		toggle(e, null, true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		toggle(e,null,false);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		amountScrolled[0] = e.getPreciseWheelRotation();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		toggle(null, e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggle(null, e,false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public static void tick(){
		
		for(Trigger t:controls){
			t.tick(); //tick all the triggers
		}
	}
	
	/**
	 * This is the real, interesting part of the class. The meat.
	 * 
	 * When an input method (mouseClicked()) is called above, it sends that data here, the toggle method.
	 * This is important, because it means that mouseClicking has no inherent meaning, nor does any key.
	 * 
	 * The switch statements below group lots of common commands together as the same trigger. Like
	 * WASD and arrow keys are merged. This setup allows easy re-configuration of controls, and, if expanded to a dictionary like system, easy
	 * remapping of controls by a player.
	 * @param me - mouse event (null if called by keyboard)
	 * @param ke - key event (null if called by mouse)
	 * @param pressed - pressed = true, released = false
	 */
	private static void toggle(MouseEvent me, KeyEvent ke, boolean pressed){
	if(ke != null) //if key input exists, parse it
		switch(ke.getKeyCode()){
		case(KeyEvent.VK_NUMPAD8):;
		case(KeyEvent.VK_UP):;
		case(KeyEvent.VK_W): up.toggle(pressed); break;
		
		case(KeyEvent.VK_S):;
		case(KeyEvent.VK_DOWN):;
		case(KeyEvent.VK_NUMPAD2): down.toggle(pressed); break;
		
		case(KeyEvent.VK_A):;
		case(KeyEvent.VK_LEFT):;
		case(KeyEvent.VK_NUMPAD4): left.toggle(pressed); break;
		
		case(KeyEvent.VK_D):;
		case(KeyEvent.VK_RIGHT):;
		case(KeyEvent.VK_NUMPAD6): right.toggle(pressed); break;
		
		case(KeyEvent.VK_ENTER): use.toggle(pressed); break;
		
		case(KeyEvent.VK_I): menu.toggle(pressed); break;
		
		case(KeyEvent.VK_ESCAPE): exit.toggle(pressed);break;
		
		case(KeyEvent.VK_SPACE): spaceBar.toggle(pressed);
				
		}
	
	if(me != null) //if mouseinput exists, parse it.
		switch(me.getButton()){
		case(MouseEvent.BUTTON1): leftClick.toggle(pressed, getScaledMouse(me.getLocationOnScreen())); break;	
		case(MouseEvent.BUTTON3):;
		case(MouseEvent.BUTTON2): rightClick.toggle(pressed, getScaledMouse(me.getLocationOnScreen()));break;
		}
		
		
			
		
	}
	
	

	

}
