package Stratego;

/** Class that represents a piece that can move.
*/
public class MPiece extends Piece {
   
    /**The main constructor for a moveablepiece.
       @param piece the piece to set to.
       @param color the player who owns the new piece. 
    */
    
    public MPiece(char piece, boolean color) {
       super(piece, color);
    }
    
   /** This method should assign the rank of the current 
     * moveable piece. There is no other way to get 
     * the current Moveable pieces rank as the pieces 
     * rank is private.
     *
     * @return the rank of the piece
     */
     
    public int getRank() {
       switch (Character.toUpperCase(this.getCharacter())) {
       //del all lowecase
        
          case 'M':
             return 10;
          case 'G':
          
             return 9;
             
          case 'C':
          
             return 8;
          case 'J':
          
             return 7;
             
          case 'P':
          
             return 6;
             
          case 'L':
          
             return 5;
             
          case 'S':
          
             return 4;
             
          case 'I':
          
             return 3;
             
          case 'O':
          
             return 2;
             
          case 'Y':
          
             return 1;
             
          default:
             return -1;
             
       }
    }
    
    
    /** This method should move the current piece from its 
     * current location into the new position on the “board” 
     * given by the object “move” and following the “rules” of 
     * the game. The board should be updated with the new move
     * and the method returns true, otherwise the board stays 
     * intact and false is returned. 
     *
     * @param board the board
     * @param move the move to be made
     * @return true if the board changes, false if it doesnt.
     */
    public  boolean  makeMove(Board board, Move move) {
       Cell[][] grid = board.getGrid();
       int sc = move.getSc();
       int sr = move.getSr();
       int dc = move.getDc();
       int dr = move.getDr();
       boolean isLegal = isLegalMove(move);
       boolean red = board.getGrid()[sr][sc].getPiece().isRed();
       if (isLegal && Stratego.twoSquareRule(board, move) 
           && !board.isLake(dr, dc)) {
          board.setLMove(move, red);
          Piece att = board.getGrid()[sr][sc].getPiece();
          Piece def = board.getGrid()[dr][dc].getPiece();
          int winner = Stratego.getWinner(att, def);
          
          if (winner == 0) {
             grid[dr][dc].setPiece(grid[sr][sc].getPiece());
             grid[sr][sc].setPiece(null);
          }
          if (winner == 1) {
             grid[sr][sc].setPiece(null);
          }
          if (winner == 2) {
             grid[sr][sc].setPiece(null);
             grid[dr][dc].setPiece(null);
          }
       }
       return isLegal; // change
    }
    
    /** This method decides if the given “move” is legal based
     * on the kind of piece. If legal, it returns true, otherwise
     * it returns false. For example, a scout may freely move 
     * along its current row or column, but not diagonally 
     * (similar to how a rook moves in the game of chess). Other 
     * movable pieces, though, may only move to their adjacent 
     * cells along the row/column.
     *
     * @param move the move that is to be checked
     * @return true if the move is legal, false if not
     */
 
    public boolean isLegalMove(Move move) {
       boolean moveLegal = true;
             
       int sr = move.getSr();
       int sc = move.getSc();
       int dr = move.getDr();
       int dc = move.getDc();
       
       if (Math.abs(sr - dr) > 1 || Math.abs(sc - dc) > 1) {
       
          if (getRank() == 2) { 
             moveLegal = true;
             
          }
          else {
             moveLegal =  false;
          }
       }
       
       return moveLegal; // change
    }
 }