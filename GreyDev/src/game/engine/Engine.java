/**
 * Central Framework (Engine.java)
 * This class is the skeleton on which the rest of the program hangs. It is called by the Core
 * class because Core allows the programmer to focus on game design, not subsystem building.
 * This Engine provides a camera, FSEM mode, and the main game loop. These
 * are all behind the scenes. Encapsulation is the name of the game, and the only input needed is how fast it should run.
 * 
 * This class is the engine, programmers need only focus on steering and the gas pedal.
 * 
 * @author Jeremy Barnes 
 */

package game.engine;

import game.Globals;
import game.utils.Logger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class Engine extends JFrame implements Runnable {
	private static final long serialVersionUID = 1863596360846514344L;

	private static final int NUM_BUFFERS = 2; // used for page flipping

	/*
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads.
	 */
	private static final int NO_DELAYS_PER_YIELD = 50;//15;

	private static int MAX_FRAME_SKIPS = 60; // no. of frames that can be skipped in any one animation loop
	// i.e the games state is updated but not rendered

	private Thread animator; // the thread that performs the animation

	public static long animPeriodNano; // period between drawing in nanoseconds

	// used for full-screen exclusive mode
	private GraphicsDevice vidcard;
	private BufferStrategy bufferStrategy;

	// player input
	protected InputHandler inHandle = new InputHandler();

	protected Camera cam; // Graphics devices
	private Graphics g;
	private Logger log;

	/* *************************************************************************** Initialization in all its many forms ********************************************** */

	/**
	 * Constructor for the class, sets up the JFrame (and JPanel if windowed)
	 * 
	 * @param anim_period_ns - period of animation in nanoseconds
	 * @param w - Whether or not the game is to be played in a window (vs FSEM)
	 */
	public Engine(long animPeriod_ns) {
		try {
			JOptionPane.showMessageDialog(null, "Welcome to the Greywater Public Alpha. \nIf you would like to assist the Greywater Development Team \nwith system data and performance information, \nplease select a location to save a log file for this playthrough. \nNo personal data is collected. \n\nIf you don't want to submit a log, hit cancel on the next window. \n\nLogs can be sent to teamsweepy@gmail.com.");
			JFileChooser jfc = new JFileChooser();
			jfc.showSaveDialog(null);
			log = new Logger(jfc.getSelectedFile());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No Printer Selected.");
		}
		System.setOut(log);
		System.setErr(log);
		animPeriodNano = animPeriod_ns;

		Globals.state = State.mainMenu;

		initFullScreen();
		init();
		startGame();

	} // end of constructor

	/**
	 * Creates a full screen exclusive mode window, sets up the graphics device.
	 * If FSEM isn't supported, crashes
	 * 
	 * Also sets up input handling.
	 */
	private void initFullScreen() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vidcard = ge.getDefaultScreenDevice();

		if (!vidcard.isFullScreenSupported()) { // if FSEM isn't supported, windowed mode default.
			System.out.println("Full-screen exclusive mode not supported");
			JOptionPane.showMessageDialog(null, "Your device doesn't support fullscreen!");
			System.exit(1);
		}

		setUndecorated(true); // no menu bar, borders, etc. or Swing components
		setIgnoreRepaint(true); // turn off all paint events since active rendering
		setResizable(false);

		addKeyListener(inHandle);
		addMouseListener(inHandle);
		addMouseMotionListener(inHandle);
		addMouseWheelListener(inHandle);

		vidcard.setFullScreenWindow(this); // switch on full-screen exclusive mode
		setBufferStrategy();

		cam = new Camera(0, 0, getBounds().width, getBounds().height); // set up camera object for scaling and player following

		setBufferStrategy();
	} // end of initFullScreen()

	/**
	 * Creates a buffer strategy
	 */
	private void setBufferStrategy() {
		createBufferStrategy(NUM_BUFFERS);
		bufferStrategy = getBufferStrategy();
	} // end of setBufferStrategy()

	/**
	 * Starts the animation loop
	 */
	protected void startGame()
	// initialize and start the thread
	{
		if (animator == null) {
			animator = new Thread(this);
			animator.start();
		}
	} // end of startGame()

	/*
	 *  ************************************************************************Life Cycle Methods ***********************************************************
	 * These methods are for pausing, unpausing, and ending the game
	 */

	public void resumeGame() {
		Globals.state = State.inGame;
	}

	public void pauseGame() {
		Globals.state = State.pauseMenu;
	}

	/**
	 * Tasks to do before terminating. Called at end of run() and via the
	 * shutdown hook in readyForTermination(). The call at the end of run() is
	 * not really necessary, but included for safety. The flag stops the code
	 * being called twice.
	 */
	private void finishOff() {
		if (Globals.state.finishedOff) {
			restoreScreen();
			System.exit(0);
			Globals.state = State.gameEnding;
		}
	} // end of finishedOff()

	/**
	 * Switches off full screen mode. This also resets the display mode if it's
	 * been changed.
	 */
	private void restoreScreen() {
		Window w = vidcard.getFullScreenWindow();
		if (w != null)
			w.dispose();
		vidcard.setFullScreenWindow(null);
	} // end of restoreScreen()

	/*
	 *  ***************************************************** Game Logic and Rendering Behind the Scenes ***********************************************************
	 * These don't do anything interesting, they just call the abstract methods with real game logic, they also create and pass on the graphics context
	 */

	/**
	 * CENTRAL GAME LOOP
	 * 
	 * Limits game FPS, controls logic and rendering.
	 * Calculates length of a render/update cycle to allow the thread
	 * to sleep so it doesn't cycle out of control.
	 */
	public void run() {
		
		
	
		long startTime = System.nanoTime();
		long endTime;
		long spareTime, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		double secondselapsed = 0;

		Globals.state = State.mainMenu; // begin in menu
		int tickCount = 0;
		int frameCount = 0;

		while (!Globals.state.finishedOff) {
			// used to calculate cycle time to keep it bounded
			startTime = System.nanoTime();

			tickCount++;
			gameUpdate();
			frameCount++;
			screenUpdate(g);

			endTime = System.nanoTime();
			spareTime = endTime - startTime;
			sleepTime = (animPeriodNano - spareTime) - overSleepTime;

			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime / 1000000L); // nano -> ms
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				overSleepTime = (System.nanoTime() - endTime) - sleepTime;
			} else { // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			// If frame animation is too slow, update the game logic
			// without rendering
			int skips = 0;
			while ((excess > animPeriodNano) && (skips < MAX_FRAME_SKIPS)) {
				excess -= animPeriodNano;
				tickCount++;
				gameUpdate(); // update state but don't render
				skips++;
			}

			endTime = System.nanoTime();

			secondselapsed += (endTime - startTime) / 1000000000.0;

			if (secondselapsed >= 10) {
				System.out.println();
				System.out.println();
				System.out.println("*******************************");
				System.out.println("Ticks per sec:" + tickCount / secondselapsed);
				System.out.println("Time elapsed: " + secondselapsed);
				System.out.println("Ticks total " + tickCount);
				tickCount = 0;

				System.out.println("FPS:" + frameCount / secondselapsed);
				System.out.println("Frames total " + tickCount);
				System.out.println("*******************************");
				System.out.println();
				System.out.println();

				secondselapsed = 0;
				frameCount = 0;
			}

		}
		finishOff();
		System.exit(0);
	} // end of run()

	/**
	 * Renders to the backbuffer,
	 * renders a white background to remove images of previous frames.
	 */
	public void fullScreenRender(Graphics g) {
		// clear the background
		g.setColor(Color.black);
		g.fillRect(0, 0, cam.width, cam.height);

		gameRender(g);

	} // end of gameRender()

	/**
	 * Updates the screen by giving data to the graphics context
	 * Disposes of graphics to encourage garbage collection
	 * and remove "trails"
	 * 
	 * @param g - graphics object used for real rendering
	 */
	private void screenUpdate(Graphics g) {
		try {
			
			g = bufferStrategy.getDrawGraphics();

		//	g.setColor(Color.BLACK);

	//		g.fillRect(0, 0, 3 * Camera.height, 3 * Camera.width);
			Graphics2D g2 = (Graphics2D) g;
		// SCALE UP TODO	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		// SCLAE DOWN TODO	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		//	g2.scale(.85375,.85375);
		//	g2.translate(-400, 0);
			
			
			AffineTransform at = new AffineTransform();
			at.scale(Camera.sscale, Camera.sscale);
			//if (Camera.scale != 1.0) {
				AffineTransformOp t = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				g2.transform(t.getTransform());
				//g2.translate((Camera.width - Camera.actWidth) / 2, (Camera.height - Camera.actHeight) / 2);
		//	}
			fullScreenRender(g2);
			g2.dispose();
			g.dispose();
			if (!bufferStrategy.contentsLost())
				bufferStrategy.show();
			else
				System.out.println("Contents Lost");
		} catch (Exception e) {
			e.printStackTrace();
			Globals.state = State.gameEnding;
		}
	}// end screenUpdate

	/**
	 * Updates the game logic
	 */
	private void gameUpdate() {
		gameTick();
	} // end of gameUpdate()

	// ************************************************* Abstract Methods to Enable Engine-like functionality****************************************************/

	protected abstract void gameRender(Graphics g);

	protected abstract void gameTick();

	protected abstract void init();

} // end of GamePanel class