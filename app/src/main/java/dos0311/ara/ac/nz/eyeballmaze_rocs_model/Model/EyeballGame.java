package dos0311.ara.ac.nz.eyeballmaze_rocs_model.Model;

public class EyeballGame implements IEyeBallGamePanel {
	protected boolean isSoundOn;
	protected GameGridIron gr;
	protected Eyeball eb;
	@Override
	public void startGame(int newGameLevel) {
		gr = new GameGridIron(newGameLevel);
		eb = new Eyeball(gr);
	};
	@Override
	public void reStartGame(int newGameLevel) {
		if (gr !=null ) {
			eb = new Eyeball(gr);
		} else {
			gr = new GameGridIron(newGameLevel);
			eb = new Eyeball(gr);
		}
	};
	@Override
	public void showGameSolution(int newGameLevel) {
		 int[][] solutionSteps = GameGridIron.ALL_SOLUTION_LISTS[newGameLevel];
         this.startGame(newGameLevel);
         for(int[] eachStep: solutionSteps) {
             eb.moveToNextPieceSucceed(eachStep[0], eachStep[1]);
         }
	};
	@Override
	public void alertGameMessage() {
		System.out.println("Will Display Controller Message here");
	};
	@Override
	public void selectSoundOn(boolean isSoundOn) {
		//Sound.isSoundOn = isSoundOn;	
	}
}
