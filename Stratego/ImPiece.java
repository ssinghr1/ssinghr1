package Stratego;

/** Class that represents a piece that can not move.
*/
public class ImPiece extends Piece {
   
    /** is this immovable piece a flag or bomb? */
    boolean flag;
    
    /**Constructor of a Imovable piece.
       @param piece the name of the piece
       @param color which player the piece belongs to
    */
    public ImPiece(char piece, boolean color) {
       super(piece, color);
    }
 
    /**Constructor of a Imoveable piece.
    @param piece the name of the piece
    @param color which player the piece belongs to
    @param flag is flag or not
    */
    public ImPiece(char piece, boolean color, boolean flag) {
       super(piece, color);
       this.flag = flag;
    }
    
    /** getter for flag.
    @return is flag or not?
    */
    public boolean isFlag() {
       return flag;
    }
 
    @Override
    public String toString() {
       return "ImPiece [flag=" + flag + ", toString()=" + super.toString() + "]";
    }
 
 }
 