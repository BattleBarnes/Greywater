package game;

import game.engine.Camera;
import game.engine.Engine;
import game.engine.ImageLoader;
import game.engine.InputHandler;
import game.engine.State;
import game.engine.audio.AudioLoader;
import game.engine.audio.BGMusicPlayer;
import game.entities.Player;
import game.entities.components.Sprite;
import game.entities.items.Crafting;
import game.overlay.InventoryMenu;
import game.overlay.OverlayManager;
import game.world.World;

import java.awt.Graphics;
import java.io.File;

public class Core extends Engine {

	Camera cam;
	Player p;
	World l;

	BGMusicPlayer bgp;

	public Core(long anim_period) {
		super(anim_period);

		System.setProperty("sun.java2d.translaccel", "true");
		System.setProperty("sun.java2d.ddforcevram", "true");
		System.setProperty("sun.java2d.opengl", "true");
	}

	public static void main(String[] args) {

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

	}

	/**
	 * Updates game state
	 */
	protected void gameTick() {
	//	if(!bgp.playing()){
		//	 bgp = new BGMusicPlayer(new File("Audio/cave.wav"));
	//		 bgp.start();
	//	}

		OverlayManager.tick();
		
		if(InputHandler.exit.keyTapped){
			if(Globals.state != State.pauseMenu || Globals.state != State.mainMenu)
				Globals.state = State.pauseMenu;
			else if(Globals.state == State.pauseMenu)
				Globals.state = State.inGame;
		}
		
		InputHandler.tick();
		
		
		
		if (Globals.state.gameRunning)
			l.tick();

	}

	/**
	 * Load stuff pre-game.
	 */
	protected void init() {

		ImageLoader.init("images.ini");
		AudioLoader.init("audio.txt");
		cam = super.cam;
		Crafting.init();

		OverlayManager.initMenu(this);

	//	bgp = new BGMusicPlayer(new File("Audio/Escadre.wav"));
	//	bgp.start();
		
	}

	/**
	 * Start a brand new game
	 */
	public void initNewGame() {
		System.gc();
		if(bgp!= null)
			bgp.stop();
		bgp = null;
		Globals.state = State.inGame;
		// bgp = new BGMusicPlayer(new File("Audio/cave.wav"));
		// bgp.start();
		 

		InventoryMenu i = new InventoryMenu();
		p = new Player(1800, 250, i);
		OverlayManager.init(p);


		//for size purposes only
		Sprite s = new Sprite("Tile1", "Tile1");

		if(l !=null)
			l = new World(s, p);
		else
			l = new World(s,p);
		System.gc();

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
