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
	private boolean[] activeKeys = new boolean[65536];
	private boolean[] activeMouse = new boolean[20];
	private double[] amountScrolled = new double[1];
	private Point[] mouseLocation = new Point[1];
	
	public InputHandler(JFrame parent){
		this.parent = parent;
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseLocation[0] = e.getLocationOnScreen();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		activeMouse[e.getButton()] = true;
		mouseLocation [0] = e.getLocationOnScreen();
		InputBundle.mouseLocation = e.getLocationOnScreen();
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
		// TODO Auto-generated method stub
		
	}
	
	public void getCommands(){
		InputBundle.up = activeKeys[KeyEvent.VK_W] || activeKeys[KeyEvent.VK_UP] || activeKeys[KeyEvent.VK_NUMPAD8];
		InputBundle.down = activeKeys[KeyEvent.VK_S] || activeKeys[KeyEvent.VK_DOWN] || activeKeys[KeyEvent.VK_NUMPAD2];
		InputBundle.left = activeKeys[KeyEvent.VK_A] || activeKeys[KeyEvent.VK_LEFT] || activeKeys[KeyEvent.VK_NUMPAD4];
		InputBundle.right = activeKeys[KeyEvent.VK_D] || activeKeys[KeyEvent.VK_RIGHT] || activeKeys[KeyEvent.VK_NUMPAD6];
		InputBundle.enter = activeKeys[KeyEvent.VK_SPACE] || activeKeys[KeyEvent.VK_ENTER];
		InputBundle.mouseClicked = activeMouse[MouseEvent.BUTTON1];
		InputBundle.rightClicked = activeMouse[MouseEvent.BUTTON2];
		InputBundle.wheelClicked = activeMouse[MouseEvent.BUTTON3];
		InputBundle.mouseLocation= mouseLocation[0];
		InputBundle.wheelScrolled = amountScrolled[0];

	}

	public void resetCommands(){

		for (int i=0; i < activeKeys.length; i++) {
			//activeKeys[i] = false;
		}
		for (int i=0; i < activeMouse.length; i++) {
			activeMouse[i] = false;
		}
		for (int i=0; i < amountScrolled.length; i++) {
			amountScrolled[i] = 0;
		}
		//	mouseLocation[0] = null;
	}

}
