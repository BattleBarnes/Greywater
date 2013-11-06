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
import java.awt.Toolkit;
import java.io.File;

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
		
		System.out.println("Available Mem");
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getAvailableAcceleratedMemory());
		System.out.println("FSEM?");
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported());
		System.out.println();
		System.out.println(System.getProperties().toString());


		
		
	}

	public static void main(String[] args) {
		System.setProperty("apple.awt.graphics.UseQuartz","false");
		System.setProperty("sun.java2d.opengl", "true");
		System.setProperty("sun.java2d.translaccel", "true");
	//	System.setProperty("sun.java2d.ddforcevram", "true");

		

		// set up the fps and period of animation.
		// I stole these from the cs3023 examples, because I'm not sure what the
		// anim period should be, we can tinker with it.
		int framesPerSecond = 60;

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

		
		if(System.currentTimeMillis() - startTime < 10000){
			Font menuFont = new Font("Baskerville Old Face", Font.TRUETYPE_FONT, 18);
			panel.render(g, 600, 1080/2 -panel.getHeight()+ 100);
			g.setColor(Color.WHITE);
			g.setFont(menuFont);
			
			String s0 = "Ms Sweepy has gotten lost in the sewers again!";
			String s1 = "  The objective of the game is to find your";
			String s2 = "               robot - Ms Sweepy.";
			String s3 = "Evade the Watchmen who patrol the sewers.";
			String s4 = "    Find Sweepy and escape the sewers.";
			
			String[] s = { s0, s1, s2, s3,s4 };

			// draw the strings
			for (int i = 0; i < s.length; i++) {
				//int strw = fm.stringWidth(s[i]);
				g.drawString(s[i], 660 , 1080/2 -panel.getHeight()+ 140 + 30*i);
			}
			g.drawString("   Ms Sweepy:", 760, 1080/2 -panel.getHeight()+ 140 + 30 * 5 + 190 );
			sweep.render(g, 770, 1080/2 -panel.getHeight()+ 140 + 30 * 5 + 200 );
			
			//g.drawString("", 605 , 1080/2 -panel.getHeight()+ 105);
		}

	}

	/**
	 * Updates game state
	 */
	protected void gameTick() {
		//if(!bgp.playing() && Globals.state == State.mainMenu){
		//	bgp.newSong(new File("Audio/OldtownGreywater.wav"));
			//System.out.println("newsong");
		//	bgp.start();
	//	}
//			 bgp = new BGMusicPlayer(new File("Audio/cave.wav"));
//			 bgp.start();
		//	bgp.newSong(new File("Audio/OldtownGreywater.wav"));
		

	
		
		OverlayManager.tick();
		
		
		
		
		if(InputHandler.exit.keyTapped){
			if(Globals.state != State.pauseMenu || Globals.state != State.mainMenu)
				Globals.state = State.pauseMenu;
			else if(Globals.state == State.pauseMenu)
				Globals.state = State.inGame;
		}
		
		InputHandler.tick();
		
		if(System.currentTimeMillis() - startTime < 10000){
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
		//AudioLoader.init("audio.txt");
		SoundLoader.init("audio.txt");
		
		cam = super.cam;
		Crafting.init();


		OverlayManager.initMenu(this);
		
		bgp = new BGMusicPlayer(new File("Audio/OldtownGreywater.wav"));
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
