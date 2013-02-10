package game.engine;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

public class InputHandler extends MouseInputAdapter implements KeyListener {

	
	public class Trigger{
		public int ticks;
		public int waitTicks;
		public boolean heldDown;
		public boolean keyTapped;
		public int xPos;
		public int yPos;
		
		public Trigger(){
			controls.add(this);
		}
		
		public void toggle(boolean pressed){
			if(pressed != heldDown){ 
				heldDown = pressed; //if not pressed, not held down, and vice versa
			}
			if(pressed)
				ticks++;
		}
		
		public void toggle(boolean pressed, Point p){
			xPos = p.x;
			yPos = p.y;
			
			if(pressed != heldDown){ 
				heldDown = pressed; //if not pressed, not held down, and vice versa
			}
			if(pressed)
				ticks++;
		
		}
		
		public void tick(){
			if(waitTicks < ticks){
				waitTicks++;
				keyTapped = true;
			}
			else
				keyTapped = false; //for one press buttons. (Discrete, not continuous)
		}
	}
	
	JFrame parent;

	
	private static ArrayList<Trigger> controls = new ArrayList<Trigger>();
	
	private static boolean[] activeMouse = new boolean[20];
	private static double[] amountScrolled = new double[1];
	
	
	
	
	public static Trigger up;  
	public static Trigger down; 
	public static Trigger left; 
	public static Trigger right;
	public static Trigger menu;
	public static Trigger use;
	public static Trigger leftClick;
	
	public static boolean mouseClicked;
	public static double wheelScrolled;
	public static boolean rightClicked;
	public static boolean wheelClicked;
	
	public InputHandler(JFrame p){
		parent = p;
		up = new Trigger();
		down = new Trigger();
		left = new Trigger();
		right = new Trigger();
		menu = new Trigger();
		use = new Trigger();
		leftClick = new Trigger();
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
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
	
	private static void toggle(MouseEvent me, KeyEvent ke, boolean pressed){
	if(ke != null)
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
		
		case(KeyEvent.VK_SPACE):;
		case(KeyEvent.VK_ENTER): use.toggle(pressed); break;
		
		case(KeyEvent.VK_M): menu.toggle(pressed); break;
				
		}
	
	if(me != null)
		switch(me.getButton()){
		case(MouseEvent.BUTTON1): leftClick.toggle(pressed, me.getLocationOnScreen()); break;	
		}
		
		
			
		
	}
	
	

	

}
