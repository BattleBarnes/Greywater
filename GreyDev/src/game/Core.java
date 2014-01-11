package game;

import game.engine.Camera;
import game.engine.Engine;
import game.engine.ImageLoader;
import game.engine.InputHandler;
import game.engine.State;
import game.engine.audio.BGMusicPlayer;
import game.engine.audio.SoundLoader;
import game.entities.Player;
import game.entities.components.Sprite;
import game.entities.items.Crafting;
import game.overlay.OverlayManager;
import game.world.World;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class Core extends Engine {

	Camera cam;
	Player p;
	World l;
	boolean newGame = true;
	Sprite panel = new Sprite("inv", "inv");
	long startTime = 0;
	BGMusicPlayer bgp;
	Sprite sweep = new Sprite("SweepySouth", "SweepySouth");

	public Core(long anim_period) {
		super(anim_period);
		
		System.out.println("********* SYSTEM DATA **********");
		System.out.println("Available Mem (MB)");
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getAvailableAcceleratedMemory()/(1000*1000));
		System.out.println("FSEM?");
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported());
		System.out.println("Display Mode (depth, width, height)");
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getBitDepth());
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth());
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
		System.out.println("Graphics Screen Device ID string");
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getIDstring());
		System.out.println("OS Arch");
		System.out.println(System.getProperty("os.arch"));
		System.out.println("OS Name");
		System.out.println(System.getProperty("os.name"));
		System.out.println("OS Version");
		System.out.println(System.getProperty("os.version"));
		System.out.println("Java Version");
		System.out.println(System.getProperty("java.version"));
		System.out.println("Java Vendor");
		System.out.println(System.getProperty("java.vendor"));
		System.out.println("Java Vendor URL");
		System.out.println(System.getProperty("java.vendor.url"));
		
	}

	public static void main(String[] args) {
	//	System.setProperty("apple.awt.graphics.UseQuartz","false");
	//	System.setProperty("sun.java2d.opengl", "true");
		System.setProperty("sun.java2d.translaccel", "true");
		System.setProperty("sun.java2d.ddforcevram", "true");

		

		// set up the fps and period of animation.
		// I stole these from the cs3023 examples, because I'm not sure what the
		// anim period should be, we can tinker with it.
		int framesPerSecond = 120;

		long animPeriod = (long) (1000.0 / framesPerSecond); // period of animation (1/freq) in milliseconds
		System.out.println("fps: " + framesPerSecond + " period: " + animPeriod);


		new Core(animPeriod * 1000000L); // milliseconds -> nanoseconds
	}

	/**
	 * Renders the game world
	 */
	protected void gameRender(Graphics g) {
		if (Globals.state.gameRunning) 
			l.render(g); // all game objects are in the level
		
		OverlayManager.render(g);

		
		if(System.currentTimeMillis() - startTime < 7000){
			Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 18);
			panel.render(g, 600, 1080/2 -panel.getHeight()+ 100);
			g.setColor(Color.WHITE);
			g.setFont(menuFont);
			
			String s0 = "      Miss Sweepy has gotten lost in the sewers again!";
			String s1 = "       The objective of the alpha is to find your";
			String s2 = "                     robot - Miss Sweepy.";
			String s3 = "      Evade the Watchmen who patrol the sewers.";
			String s4 = "Find Sweepy and bring her back to the sewer entrance.";
			
			String[] s = { s0, s1, s2, s3,s4 };

			// draw the strings
			for (int i = 0; i < s.length; i++) {
				//int strw = fm.stringWidth(s[i]);
				g.drawString(s[i], 625 , 1080/2 -panel.getHeight()+ 140 + 30*i);
			}
			g.drawString("   Miss Sweepy:", 750, 1080/2 -panel.getHeight()+ 140 + 30 * 5 + 190 );
			sweep.render(g, 770, 1080/2 -panel.getHeight()+ 140 + 30 * 5 + 200 );
			
			//g.drawString("", 605 , 1080/2 -panel.getHeight()+ 105);
		}

	}

	/**
	 * Updates game state
	 */
	protected void gameTick() {
		if(!bgp.playing()){
			System.out.println("New song");
			bgp.stop();
			bgp = null;
			System.out.println("Stopped");
			bgp = new BGMusicPlayer("Audio/greywater sewerv1.wav");
			bgp.start();
	}

		

	
		
		OverlayManager.tick();
		
		
		
		
		if(InputHandler.exit.keyTapped){
			if(Globals.state != State.pauseMenu || Globals.state != State.mainMenu)
				Globals.state = State.pauseMenu;
			else if(Globals.state == State.pauseMenu)
				Globals.state = State.inGame;
		}
		
		InputHandler.tick();
		
		if(System.currentTimeMillis() - startTime < 7000){
			return;
		}
		
		if (Globals.state.gameRunning)
			l.tick();

	}

	/**
	 * Load stuff pre-game.
	 */
	protected void init() {
		
		ImageLoader.init("images.ini");
		SoundLoader.init("audio.txt");
		
		cam = super.cam;
		Crafting.init();


		OverlayManager.initMenu(this);
		
		bgp = new BGMusicPlayer("Audio/oldtowngreywater.wav");
		bgp.start();
		
	}

	/**
	 * Start a brand new game
	 */
	public void initNewGame() {
		//if(bgp!= null)
		//	bgp.stop();

		Globals.state = State.inGame;
		// bgp = new BGMusicPlayer(new File("Audio/cave.wav"));
	
	//	bgp.newSong(new File("Audio/Greywater SewerV1.wav"));
		//bgp.start();
		System.out.println("New Game Began...");
		//for size purposes only
		Sprite s = new Sprite("Tile1", "Tile1");
		l = new World(s);
		startTime = System.currentTimeMillis();

	}

	/**
	 * Load an old game from file
	 */
	public void loadGame() {
		// TODO add loading and saving function
	}

	/**
	 * Make the game go away.
	 */
	public void exitGame() {
		Globals.state = State.gameEnding;
	}
}
