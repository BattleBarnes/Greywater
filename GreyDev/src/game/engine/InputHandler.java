package game.engine;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

public class InputHandler extends MouseInputAdapter implements KeyListener {

	JFrame parent;
	private static  boolean[] activeKeys = new boolean[65536];
	private static boolean[] activeMouse = new boolean[20];
	private static double[] amountScrolled = new double[1];
	private static Point mouseLocation = new Point();
	
	
	
	public static boolean up;
	public static boolean down;
	public static boolean left;
	public static boolean right;
	public static boolean enter;
	public static boolean inv;
	
	public static boolean mouseClicked;
	public static double wheelScrolled;
	public static boolean rightClicked;
	public static boolean wheelClicked;
	
	
	

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseLocation = e.getLocationOnScreen();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseLocation = e.getLocationOnScreen();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseLocation = e.getLocationOnScreen();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseLocation = e.getLocationOnScreen();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		activeMouse[e.getButton()] = true;
		mouseLocation= e.getLocationOnScreen();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		activeMouse[e.getButton()] = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		amountScrolled[0] = e.getPreciseWheelRotation();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		activeKeys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		activeKeys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public static void getCommands(){
		up = activeKeys[KeyEvent.VK_W] || activeKeys[KeyEvent.VK_UP] || activeKeys[KeyEvent.VK_NUMPAD8];
		down = activeKeys[KeyEvent.VK_S] || activeKeys[KeyEvent.VK_DOWN] || activeKeys[KeyEvent.VK_NUMPAD2];
		left = activeKeys[KeyEvent.VK_A] || activeKeys[KeyEvent.VK_LEFT] || activeKeys[KeyEvent.VK_NUMPAD4];
		right = activeKeys[KeyEvent.VK_D] || activeKeys[KeyEvent.VK_RIGHT] || activeKeys[KeyEvent.VK_NUMPAD6];
		enter = activeKeys[KeyEvent.VK_SPACE] || activeKeys[KeyEvent.VK_ENTER];
		mouseClicked = activeMouse[MouseEvent.BUTTON1];
		rightClicked = activeMouse[MouseEvent.BUTTON2];
		wheelClicked = activeMouse[MouseEvent.BUTTON3];
		wheelScrolled = amountScrolled[0];
		inv = activeKeys[KeyEvent.VK_M];

	}

	public static void resetCommands(){

		for (int i=0; i < activeKeys.length; i++) {
			//activeKeys[i] = false;
		}
		for (int i=0; i < activeMouse.length; i++) {
			activeMouse[i] = false;
		}
		for (int i=0; i < amountScrolled.length; i++) {
			amountScrolled[i] = 0;
		}
			mouseLocation = null;
	}

}
