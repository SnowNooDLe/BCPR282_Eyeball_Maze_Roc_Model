package dos0311.ara.ac.nz.eyeballmaze_rocs_model.Model;

public class Piece {
	//for blank piece
    public PieceCharacter myCharacter;
	public Shape myShape;
	public Colour myColour;
	public int x;
	public int y;
	protected Direction myDirection;
	//create blank piece 
	public Piece() {
		this.myColour = Colour.WHITE;
		this.myShape = Shape.BLANK;
	}
	//create normal grid piece
	public Piece(Shape newShape, Colour newColour) {
		this.myShape = newShape;
		this.myColour = newColour;	
		this.myCharacter = PieceCharacter.GRID;
	}
	//for start_point and end_point piece as they have characters
	public Piece(Shape newShape, Colour newColour, PieceCharacter newCharacter) {
		this.myShape = newShape;
		this.myColour = newColour;
		this.myCharacter = newCharacter;
	}
	public void setPosition(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	//remember Direction for undoMove
	public void setDirection(Direction newDirection) {
		this.myDirection = newDirection;
	}
	
	public Direction getPieceDirection() {
		return this.myDirection;
	}
	//the goal
	public boolean isEndPoint() {
		return myCharacter == PieceCharacter.END_POINT;
	}
	//the start
	public boolean isStartPoint() {
		return myCharacter == PieceCharacter.START_POINT;
	}
	//normal Grid in the game map
	public boolean isGrid() {
		return myCharacter == PieceCharacter.GRID;
	}
	//blank area in the game map
	public boolean isBlank() {
		return (myShape == Shape.BLANK && myColour == Colour.WHITE);
	}
	
}
