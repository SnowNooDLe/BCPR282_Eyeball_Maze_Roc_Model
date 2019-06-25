package dos0311.ara.ac.nz.eyeballmaze_rocs_model.Model;


import java.sql.Timestamp;
import java.util.ArrayList;


public class Eyeball {
	private GameGridIron myGameGridIron;
	public Piece myCurrentPiece;
	private Timestamp myStartTime;
	public Direction myCurrentDirection;
//	as there was no getter, had to change this to public
	public ArrayList<Piece> myAllMovedPieces = new ArrayList<>();
	
	public Eyeball(GameGridIron newGameGridIron) {
		this.myGameGridIron = newGameGridIron;
		this.myCurrentPiece = myGameGridIron.findStartPointPiece();
		this.myCurrentDirection = Direction.NORTH;
		this.myCurrentPiece.myDirection = Direction.NORTH;
		this.myAllMovedPieces.add(this.myCurrentPiece);		
		this.myStartTime =  new Timestamp(System.currentTimeMillis());
    }
	
	public Direction getMyCurrentDirection() {
		return this.myCurrentDirection;
	}
	
	public ArrayList<Piece> getMyAllMovedPieces() {
		return this.myAllMovedPieces;
	}
	
	public Timestamp getCurrentTime() {
		Timestamp timestamp =new Timestamp(System.currentTimeMillis());
		return timestamp;
	}
	
	public Direction getNewDirectionFromNextPiece (Piece newNextPiece) {
		Direction nextDirection = this.myCurrentDirection;
		if (newNextPiece.x == myCurrentPiece.x) {
            if (newNextPiece.y> this.myCurrentPiece.y) {
            	nextDirection =  Direction.EAST;
            } else {
            	nextDirection =  Direction.WEST;
            }
		}   
		if (newNextPiece.y == myCurrentPiece.y) {
            if (newNextPiece.x> this.myCurrentPiece.x) {
            	nextDirection =  Direction.SOUTH;
            } else {
            	nextDirection =  Direction.NORTH;
            }
		}	
       return nextDirection;
	}
	
	public boolean canDoNextMove(int x, int y) {
		boolean isCanDoNextMove = true;
		Direction nextDirection  = this.myCurrentDirection;
		Piece nextPiece= this.myGameGridIron.findPieceInMap(x,y);
		/* Debug only
		System.out.println (nextPiece.x);
		System.out.println (nextPiece.y);
		System.out.println (nextPiece.myDirection);
		System.out.println (nextPiece.myShape);
		System.out.println (nextPiece.myColour);
		*/
		nextDirection = getNewDirectionFromNextPiece (nextPiece);
		if ((myCurrentDirection == Direction.NORTH) && (nextDirection == Direction.SOUTH))  {
			isCanDoNextMove = false;			
		}
		else if ((myCurrentDirection == Direction.SOUTH) && (nextDirection == Direction.NORTH)) {
			isCanDoNextMove = false;
		}
		else if ((myCurrentDirection == Direction.WEST) && (nextDirection == Direction.EAST)) {
			isCanDoNextMove = false;
		}
		else if ((myCurrentDirection == Direction.EAST) && (nextDirection == Direction.WEST)) {
			isCanDoNextMove = false;	
		}	
		else if ((myCurrentPiece.myColour != nextPiece.myColour) && (myCurrentPiece.myShape != nextPiece.myShape)) {
			isCanDoNextMove = false;
		}
		//System.out.println(nextDirection);
		return isCanDoNextMove;
	}
	
	public boolean isReachingEndPoint() {
		if (this.myCurrentPiece.isEndPoint()) {
			return true;
		}
		else {
			return false;
		}		
	}
	
	public boolean isReachingStartPoint() {
		if (this.myCurrentPiece.isStartPoint()) {
			return true;
		}
		else {
			return false;
		}		
	}
		
	public boolean moveToNextPieceSucceed (int x, int y){
		Piece nextPiece= this.myGameGridIron.findPieceInMap(x,y);
		if (canDoNextMove(x, y)) {        	
            myCurrentDirection = getNewDirectionFromNextPiece(nextPiece);            
            myCurrentPiece = nextPiece;
            myCurrentPiece.setDirection(myCurrentDirection);
           
            this.myAllMovedPieces.add(myCurrentPiece);
			if (this.isReachingEndPoint()) {
				
				System.out.println("Congradulations! Winning!");
			}
            return true;
        } else {
        	//throw new InvalidParameterException("Cannot move to: " + "x = " + x + " y = " + y);
        	
        	System.out.println("Cannot move to: " + "x = " + x + " y = " + y);
        	return false;
        }		
    }
	
	public boolean moveBackPreviousPieceSucceed (int x, int y) {
		int movedPieceSize = this.myAllMovedPieces.size();
		boolean isMoveBackSucceed = false; 
		if (movedPieceSize >1) {
			this.myAllMovedPieces.remove(movedPieceSize-1);
			this.myCurrentPiece = this.myAllMovedPieces.get(this.myAllMovedPieces.size()-1);
			this.myCurrentDirection = this.myCurrentPiece.myDirection;
			isMoveBackSucceed = true;
		}
		else {
			System.out.println("This is StartPoint");			
		}
		return isMoveBackSucceed;	
	}
	public Piece getMyCurrentPiece() {
		return this.myCurrentPiece;
	}
	public double getTotalMoveSpendingTime() {
		return (getCurrentTime().getTime() - this.myStartTime.getTime());
	}
	public int countTotalMove() {
		return this.myAllMovedPieces.size()-1;
	}
}
