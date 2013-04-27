package game.engine;

public enum State {
	
	mainMenu(false, true, false, false),
	inGame(true, false, false, false),
	pauseMenu(false, true, true, false),
	gameMenu(true,true,false,false),
	gameEnding(false, false, false, true),
	gameWon(false,true,true,false);

	public boolean gameRunning, drawMenu, isPaused, finishedOff;
	

	State(boolean gameRunning, boolean drawMenu, boolean isPaused, boolean finishedOff){
		this.gameRunning = gameRunning;
		this.drawMenu = drawMenu;
		this.isPaused = isPaused;
		this.finishedOff = finishedOff;
	}
	

	

}
