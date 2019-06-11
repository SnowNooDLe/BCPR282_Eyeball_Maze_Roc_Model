package dos0311.ara.ac.nz.eyeballmaze_rocs_model.Model;

public abstract interface IEyeBallGamePanel {
	public abstract void startGame(int newGameLevel);	//Start button
	public abstract void reStartGame(int newGameLevel);	// Restart button
	public abstract void showGameSolution(int newGameLevel); //Show Solution button
	public abstract void alertGameMessage();	//Game Alert Message
	public abstract void selectSoundOn(boolean isSoundOn);	//Sound Selection
}
