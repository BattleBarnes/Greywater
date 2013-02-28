/** FSEM and Windowed mode handle rendering in such a radically different manner that a separate class is needed.
 * Primarily- JFrames without FSEM need a JPanel to do the actual rendering.
 * This class facilitates windowed mode, and controls behind the scenes rendering when in windowed mode.
 * This doesn't render the Engine Class obsolete, but it does override its rendering methods. like ScreenUpdate and fullScreen render
 * 
 * @author Jeremy Barnes
 */
package game.engine;


import game.Globals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;


public class WindowPanel extends JPanel{

	private Image bufferImage = null;
	private Engine parent;


/**
 * Constructor! Sets up the panel and input listeners.
 * @param parent- the container that holds the JPanel (JFrame) and interfaces with the core class
 */
	public WindowPanel(Engine parent){
		this.parent = parent;
		setDoubleBuffered(false);
		setVisible(true);
		setBackground(Color.white);
		setPreferredSize( new Dimension(900, 800));
		
		addKeyListener(parent.inHandle);
		addMouseListener(parent.inHandle);
		addMouseMotionListener(parent.inHandle);
		addMouseWheelListener(parent.inHandle);

		setFocusable(true);
		requestFocus();    // the JPanel now has focus, so receives key events
	}

	/**
	 * Creates the buffered image, renders game/menu
	 * @param dgb - graphics device
	 */
	private void panelRender(Graphics dgb){
		if (bufferImage == null){
			bufferImage = createImage(this.getWidth(), this.getHeight());
		}
			dgb = bufferImage.getGraphics();

		// draw a white background
		dgb.setColor(Color.white);
		dgb.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if(Globals.state.gameRunning)
			parent.gameRender(dgb);
		if(Globals.state.drawMenu)
			parent.menuRender(dgb);
	}

	/**
	 * Paints the graphics image to the screen, 
	 * disposes the backbuffered version of the image so that the next frame is... next
	 * 
	 * @param dgb - graphics device
	 */
	private void paintScreen(Graphics dgb){
		try {
			dgb = this.getGraphics();
			((Graphics2D) dgb).scale(Camera.xScale, Camera.yScale);


			if ((dgb != null) && (bufferImage != null))
				dgb.drawImage(bufferImage, 0, 0, null);

			// Sync the display on some systems.
			Toolkit.getDefaultToolkit().sync();
			dgb.dispose();
		}
		catch (Exception e)
		{ System.out.println("Graphics context error: " + e);  }
	} // end of paintScreen()

	/**
	 * Used to force the JPanel to paint, calls panelRender to draw to buffer
	 * and paintScreen to paint the buffered image to the screen.
	 * 
	 * PanelRender calls the abstract Engine methods to allow the Core class to function as intended.
	 * @param g
	 */
	public void windowRender(Graphics g){
		panelRender(g);
		paintScreen(g);
	}

}  // end of class
