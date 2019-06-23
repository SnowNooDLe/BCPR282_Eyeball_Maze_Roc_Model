package dos0311.ara.ac.nz.eyeballmaze_rocs_model.Model;
import java.security.InvalidParameterException;
import java.util.ArrayList;
public class GameGridIron {
	//single level
	private Piece[][] levelMap; 
	//create all Game Maps
	private static final Piece[][][] ALL_MAP_LISTS = {
//			Roc's Original
       {	//Level 0
           {new Piece(),new Piece(), new Piece(Shape.FLOWER,Colour.RED,PieceCharacter.END_POINT),new Piece()},
           {new Piece(Shape.CROSS,Colour.BLUE),new Piece(Shape.FLOWER,Colour.YELLOW), new Piece(Shape.DIAMOND,Colour.YELLOW), new Piece(Shape.CROSS,Colour.GREEN)},
           {new Piece(Shape.FLOWER,Colour.GREEN),new Piece(Shape.STAR,Colour.RED),new Piece(Shape.STAR,Colour.GREEN),new Piece(Shape.DIAMOND,Colour.YELLOW)},
           {new Piece(Shape.FLOWER,Colour.RED),new Piece(Shape.FLOWER,Colour.BLUE),new Piece(Shape.STAR,Colour.RED),new Piece(Shape.FLOWER,Colour.GREEN)},
           {new Piece(Shape.STAR,Colour.BLUE),new Piece(Shape.DIAMOND,Colour.RED),new Piece(Shape.FLOWER,Colour.BLUE),new Piece(Shape.DIAMOND,Colour.BLUE)},
           {new Piece(),new Piece(Shape.DIAMOND,Colour.BLUE,PieceCharacter.START_POINT), new Piece(),new Piece()}
       },
        {	//Level 1
	        {new Piece(), new Piece(),new Piece(Shape.FLOWER,Colour.RED,PieceCharacter.END_POINT),new Piece()},
	        {new Piece(Shape.CROSS,Colour.BLUE),new Piece(Shape.FLOWER,Colour.BLUE),new Piece(Shape.DIAMOND,Colour.BLUE),new Piece(Shape.CROSS,Colour.GREEN)},
	        {new Piece(Shape.FLOWER,Colour.GREEN),new Piece(Shape.STAR,Colour.RED),new Piece(Shape.STAR,Colour.GREEN),new Piece(Shape.FLOWER,Colour.YELLOW)},
	        {new Piece(Shape.FLOWER,Colour.RED),new Piece(Shape.DIAMOND,Colour.GREEN),new Piece(Shape.STAR,Colour.RED), new Piece(Shape.STAR,Colour.YELLOW)},
	        {new Piece(Shape.CROSS,Colour.GREEN),new Piece(Shape.DIAMOND,Colour.RED),new Piece(Shape.FLOWER,Colour.BLUE),new Piece(Shape.DIAMOND,Colour.GREEN)},
	        {new Piece(),new Piece(Shape.DIAMOND,Colour.BLUE,PieceCharacter.START_POINT),new Piece(),new Piece()}
        }       
    };
	
	public static final int[][][] ALL_SOLUTION_LISTS = {
			//Level 0
			{{5,1},{3,1},{3,3},{1,3},{1,0},{4,0},{4,2},{0,2}},
			//Level 1
			{{5,1},{4,1},{2,1},{2,2},{3,2},{3,0},{2,0},{2,3},{3,3},{3,2},{0,2}},
	};
	//construct the whole GameGridIron and set each Piece locations for the nominated level map
	public GameGridIron(int gameLevel) {
		this.getGameLevelMap(gameLevel);
		for(int x = 0; x < this.levelMap.length; x++) {
            for(int y = 0; y < this.levelMap[x].length; y++) {
                Piece eachPiece = this.levelMap[x][y];
                eachPiece.setPosition(x,y);
            }
        }
	}
	public void getGameLevelMap (int gameLevel) {
		this.levelMap = ALL_MAP_LISTS[gameLevel];		
	}
	
	public Piece[][] getMyLevelMap(){
		return this.levelMap;
	}
	
	
	public Piece findPieceInMap(int x, int y) {
		if (x< levelMap.length && y < levelMap[0].length) {
			return levelMap[x][y];
		} else {
			throw new InvalidParameterException("Invalid X Y points: " + "x = " + x + " y = " + y); 
		}		
	}
	
	public Piece findStartPointPiece() {
		ArrayList<Piece> startPointList = new ArrayList<>(); 
		for(int x = 0; x < this.levelMap.length; x++) {
            for(int y = 0; y < this.levelMap[x].length; y++) {
                if(this.levelMap[x][y].isStartPoint()) {
                	startPointList.add(levelMap[x][y]);
                }
            }
        }
		return startPointList.get(0);
	}
	
	
	public Piece findEndPointPiece () {
		ArrayList<Piece> endPointList = new ArrayList<>(); 
		for(int x = 0; x < this.levelMap.length; x++) {
            for(int y = 0; y < this.levelMap[x].length; y++) {
                if(this.levelMap[x][y].isEndPoint()) {
                	endPointList.add(levelMap[x][y]);
                }
            }
        }
		return endPointList.get(0);
	}
	
	public static int countingMapLevel() {
		return ALL_MAP_LISTS.length;
	}
	
	public static int countingSolutions() {
		return ALL_SOLUTION_LISTS.length;
	}
}
