package game;

import game.engine.Camera;
import game.engine.Engine;
import game.engine.ImageLoader;
import game.engine.InputHandler;
import game.engine.State;
import game.engine.audio.AudioLoader;
import game.entities.Mob;
import game.entities.Player;
import game.entities.Sweepy;
import game.entities.Watchman;
import game.entities.components.Sprite;
import game.menu.InventoryMenu;
import game.menu.StartMenu;
import game.world.World;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Core extends Engine {
	
	StartMenu m;
	InventoryMenu i;
	Camera cam;
	Player p;
	ImageLoader imgLd;
	World l;
	
	Sprite light = new Sprite("light", "light");
	Sprite hud = new Sprite("hud", "hud");
	Sprite hud2 = new Sprite("hps", "hps");
	Sprite hpoint = new Sprite("hu", "hu");
	
	public Core(long anim_period, boolean windowed) {
		super(anim_period, windowed);
		
		System.setProperty("sun.java2d.translaccel", "true");
		System.setProperty("sun.java2d.ddforcevram", "true");
		System.setProperty("sun.java2d.opengl", "true");		
	}

	public static void main(String[] args){
		
		//I hope to allow both windowed and FSEM mode, so this prompts the user to decide at runtime. This makes things harder, but its worth it
		int choice =JOptionPane.showConfirmDialog(null, "Fullscreen Mode?");
		boolean windowed = false;
		
		//0 = yes, 1 = no, 2 = cancel, so if they cancel out, abort the program.
		if(choice == 0){
			windowed = false;
		}
		if(choice == 1)
			windowed = true;
		else if(choice == 2)
			System.exit(0);
		
		
		//set up the fps and period of animation.
		//I stole these from the cs3023 examples, because I'm not sure what the anim period should be, we can tinker with it.
		int frames_per_second = 120;
		if(windowed)
			frames_per_second = 120;
		long anim_period = (long) (1000.0/ frames_per_second); //period of anim in millisecond
		System.out.println("fps: " + frames_per_second + " period: " + anim_period);
		
		new Core(anim_period * 1000000L, windowed); //milliseconds -> nanoseconds
	}
	

	/**
	 * Draws the menu
	 */
	protected void menuRender(Graphics g) {
		if(Globals.state == State.mainMenu)
			m.render(g);
		else
			i.render(g);
		
	}

	/**
	 * Renders the game world
	 */
	protected void gameRender(Graphics g){
		
		l.render(g); //all game objects are in the level
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(Camera.width*1.0/light.getWidth(), Camera.height*1.0/light.getHeight());
		light.render(g2, 0, 0);
		g2.scale(light.getWidth()*1.0/Camera.width, light.getHeight()*1.0/Camera.height);
		
		g2.scale(Camera.xScale, Camera.yScale);
		hud.render(g2, 0, (int) (Camera.height/Camera.xScale - hud.getHeight()) );
		hud2.render(g2, (int) (Camera.width/Camera.xScale/2 - hud2.getWidth()/2) ,  (int) (Camera.height/Camera.xScale-hud.getHeight()/2-hud2.getHeight()/2));
		
		
		for(int i = 0; i < 10; i++){
			hpoint.render(g2, (int) (Camera.width/Camera.yScale/2 - hud2.getWidth()/2 + 2 +  i*hud2.getWidth()/10), (int) (Camera.height/Camera.xScale-hud.getHeight()/2-hud2.getHeight()/2 + 10));
		}

	}



	/**
	 * Allows user input on menu's
	 */
	protected void menuTick() {
		InputHandler.tick();
		m.update();
	}

	/**
	 * Updates game state
	 */
	protected void gameTick(){
		InputHandler.tick();
		l.tick();
	}

	/**
	 * Initialize game and menu elements pre-game.
	 */
	protected void init() {
		ImageLoader.init("images.txt");
		AudioLoader.init("audio.txt");
		
		cam = super.cam;
		
		
		//in game objects. Menu system might need to change to allow
		//inventory screens and such.
		m = new StartMenu(this);
		
		i = new InventoryMenu();
		//p = new Player(17000, 4000, i);
		p = new Player(100, 100, i);
		Watchman w1 = new Watchman(500, 500, p);
		i.setOwner(p);
		
		ArrayList<Mob> mobs = new ArrayList();
		mobs.add(p);
		mobs.add(w1);
		mobs.add(new Sweepy(900,300,p));

		
		
		//item elements, will be replaced with proper tilesets later TODO 
		Sprite w = new Sprite("Wal2l", "Wal2l");
		Sprite ow = new Sprite("Wall", "Wall");
		Sprite s = new Sprite("Tile1", "Tile1");
		
		
		l = new World(s,w,ow, mobs, p);
		w1.addPathFinder(l);
	}

	/**
	 * Start a brand new game
	 */
	public void initNewGame() {
		Globals.state = State.inGame;
	}
	
	/**
	 * Load an old game from file
	 */
	public void loadGame(){
		//TODO add loading and saving function
	}
	
	/**
	 * Make the game go away.
	 */
	public void exitGame(){
		Globals.state = State.gameEnding;
	}
}
