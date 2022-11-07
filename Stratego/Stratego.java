package Stratego;

/** 
   The rule class keeps track of 
   a couple of past two moves and
   does the necessary checks.
*/
public class Stratego {

    /** CHECK
       Method to check whether moving a piece would would go into another piece.
       @param board the board to perform the check on.
       @param move the move to be checked
       @return whether the move is allowed.
    */
    public static boolean crossesAnotherPiece(Board board, Move move) {
       int startRow = move.getSr();
       int startCol = move.getSc();
       int finalRow = move.getDr();
       int finalCol = move.getDc();
       Cell[][] grid = board.getGrid();
       
       for (int countRow = startRow; countRow < finalRow; countRow++) {
          for (int countCol = startCol; countCol < finalCol; countCol++) {
             if (!grid[countRow][countCol].isEmpty()) {
                return true;
             }
          }
       }
       return false; 
    }
 
 
    /** method to check if two square rule is respected.
       @param move the movement to check
       @param board the board
       @return whether the rules are met.
    */
    public static boolean twoSquareRule(Board board, Move move) {
    
       int sc = move.getSc();
       int sr = move.getSr();
       
       boolean red = board.getGrid()[sr][sc].getPiece().isRed();
       Move[] lastTwoMoves = board.getL2Moves(red);
       Move lastMove = lastTwoMoves[1];
       Move secondLastMove = lastTwoMoves[0];
    
       if ((lastMove.isOpposite(secondLastMove))
           && (secondLastMove.equals(move))) {
          return false;
       }
       return true; 
    }
     
    /** CHECK
       Method to check whether the game is over.
       @param board the board to check on.
       @return an int representing the game state
       0 for game is not over
       1 for red wins
       2 for blue wins
    */
    public static int isGameOver(Board board) {
       //check for no movable pieces
       if (board.getCountMovables(true) == 0 || !board.hasFlag(true)) {
          
          return 2;
       }
       else if (board.getCountMovables(false) == 0 || !board.hasFlag(false)) {
          
          return 1;
       }
       
       int redUnTrapCount = 0;
       for (Cell checkTrapPiece: board.getMovables(true)) {
          
          if (!board.isTrapped(checkTrapPiece)) {
             redUnTrapCount++;
             
          }
       }
       if (redUnTrapCount == 0) {
          
          return 2;
       }
       
       int blueUnTrapCount = 0;
       for (Cell checkTrapPiece: board.getMovables(false)) {
         
          if (!board.isTrapped(checkTrapPiece)) {
             
             blueUnTrapCount++;
          }
       }
       if (blueUnTrapCount == 0) {
          return 1;
       }
       
       return 0;  
    }
 
    /**Method to check whether a move would cross a 
     * lake or land in a lake. HINT: there is a isLake()
     * method in the Board class that can be use here!
       @param board the board
       @param move the movement to check
       @return whether the move would cross a lake
    */
    public static boolean crossesLake(Board board, Move move) {
       int startRow = move.getSr();
       int startCol = move.getSc();
       int finalRow = move.getDr();
       int finalCol = move.getDc();
       Cell[][] grid = board.getGrid();
       
       for (int countRow = startRow; countRow < finalRow + 1; countRow++) {
          for (int countCol = startCol; countCol < finalCol + 1; countCol++) {
             if (grid[countRow][countCol].isLake()) {
                return true;
             }
          }
       }
    
       return false; 
    }
    
    /** Method to get the winner of an attack.
       @param att the piece attacking
       @param def the piece defending
       @return an int representing the winner of the attack. See below.
       0 return means attacker wins
       1 return means defender wins
       2 means both lose
       3 invalid, something is wrong!
    */
    public static int getWinner(Piece att, Piece def) {
       int output = 3;
       if (att instanceof MPiece && def instanceof MPiece) {
          MPiece attacker = (MPiece) att;
          int attRank = attacker.getRank();
          
          MPiece defender = (MPiece) def;
          int defRank = defender.getRank();
          if (attRank == 1 && defRank == 10) {
             return 0;
          }
          // for anything other than spy
          if (attRank > defRank) {
             return 0;
          }
          else if (defRank > attRank) {
             return 1;
          }
          else if (attRank == defRank) {
             return 2;
          }
       }
       else if (att instanceof MPiece && def instanceof ImPiece) {
          MPiece attacker = (MPiece) att;
          ImPiece defender = (ImPiece) def;
          output = checkDefImPiece(attacker, defender);
       }
       //add in for going to null
       else if (att instanceof MPiece && !(def instanceof Piece)) {
          output = 0;
       }
       //else if (att instanceof MPiece && def.
       return output; 
    }
    
    /* Method to get the winner of an attack if the attacker is a 
     * moveable piece and the defender is an immovable piece. This is
     * used in the case that the attacker is a miner attacking a bomb,
     * a non-miner attacking a bomb, and a moveable piece attacking a flag
     * 
     * @param attacker the attacking MPiece
     * @param defender the defending ImPiece
     * @return 0 for attacker wins
     *         1 for defender wins
     *         3 for no one wins, error
     */
    private static int checkDefImPiece(MPiece attacker, ImPiece defender) {
       if (defender.isFlag()) {
          return 0;
       }
       else if (!defender.isFlag() && attacker.getRank() == 3) {
          return 0;
       }
       else if (!defender.isFlag()) {
          return 1;
       }
       return 3;
    }
 }
 