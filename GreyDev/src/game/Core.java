package game;

import game.engine.Camera;
import game.engine.Engine;
import game.engine.ImageLoader;
import game.engine.InputHandler;
import game.engine.State;
import game.entities.Player;
import game.entities.components.Entity;
import game.entities.components.Item;
import game.entities.components.Sprite;
import game.menu.InventoryMenu;
import game.menu.StartMenu;
import game.world.World;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;


public class Core extends Engine {
	
	StartMenu m;
	InventoryMenu i;
	Camera cam;
	Player p;
	ImageLoader imgLd;
	World l;
	
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
		cam = super.cam;
		
		
		//in game objects. Menu system might need to change to allow
		//inventory screens and such.
		m = new StartMenu(this);
		i = new InventoryMenu(this);
		
		p = new Player(1000, 0, i);
		ArrayList<Entity> mobs = new ArrayList();
		mobs.add(p);
		
		
		Item item = new Item("GodlyPlateoftheWhale", 1000, 600);
		ArrayList<Entity> floorItems = new ArrayList<>();
		floorItems.add(item);
		
		//item elements, will be replaced with proper tilesets later TODO 
		Sprite w = new Sprite("Wal2l", "Wal2l");
		Sprite ow = new Sprite("Wall", "Wall");
		Sprite s = new Sprite("Tile1", "Tile1");
		
		
		l = new World(s,w,ow, mobs, floorItems, p);
		p.setWorld(l);
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
