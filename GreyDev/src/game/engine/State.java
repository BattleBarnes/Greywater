package game.engine;

public enum State {
	
	mainMenu(false, true, false, false), //the first menu - while things are init-ing
	inGame(true, false, false, false), //standard gameplay
	pauseMenu(false, true, true, false), //when you hit escape to bring up the menu while playing
	gameMenu(true,true,false,false), //inventory menus and shit
	gameEnding(false, false, false, true), //game over, man
	gameWon(false,true,true,false); //show win screen, don't close game.

	public boolean gameRunning, drawMenu, isPaused, finishedOff;
	

	State(boolean gameRunning, boolean drawMenu, boolean isPaused, boolean finishedOff){
		this.gameRunning = gameRunning;
		this.drawMenu = drawMenu;
		this.isPaused = isPaused;
		this.finishedOff = finishedOff;
	}
	

	

}
