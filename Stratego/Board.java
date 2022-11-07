package Stratego;

/** 
   Class to represent a stratego board.
   A board is made up of 10x10 grid of 
   cells. It has the basic operations 
   necessary to view and edit the grid.
*/
public final class Board {

    /** singleton instance */
    private static final Board INSTANCE = new Board();
      
    /**The size of the board.*/
    private static final int SIZE = 10;
    
    /**The 2D-array representing the board.*/
    private Cell[][] grid;
    
    /**The last two moves made on the board by red.*/
    private Move[] l2MovesR;
    
    /**The last two moves made on the board by blue.*/ 
    private Move[] l2MovesB; 
 
    /**whose turn it is; true for red, false for blue */ 
    private boolean turn; 
 
    /**Constructor for a board.
       Fills the board with empty cells.
    */
    private Board() {
       this.grid = new Cell[SIZE][SIZE];
       for (int i = 0; i < SIZE; i++) {
          for (int j = 0; j < SIZE; j++) {
             this.grid[i][j] = new Cell(i, j, false);
          }
       }
       // the last two moves made on the board by red
       this.l2MovesB = new Move[2];
       // the last two moves made on the board by blue 
       this.l2MovesR = new Move[2]; 
       this.l2MovesB[0] = new Move(-1, -1, -1, -1); 
       this.l2MovesB[1] = new Move(-1, -1, -1, -1);
       this.l2MovesR[0] = new Move(-1, -1, -1, -1);
       this.l2MovesR[1] = new Move(-1, -1, -1, -1);
    }
    
    /** Method to get the only instance of board in the game.
      @return return the instance
    */
    public static Board getInstance() {
       return INSTANCE;
    }
    
    /**A method to return the size of the board.
       @return the size of the board
    */
    public int size() {
       return SIZE;
    }
    
    /**
       Method to display the board to standard out.
       @param showRed whether the values in red cells are visible
       @param showBlue whether the values in blue cells are visible
    */
    public void display(boolean showRed, boolean showBlue) {
       for (int i = 0; i < SIZE; i++) {
          for (int j = 0; j < SIZE; j++) {
             if (grid[i][j].isLake()) { // lake
                System.out.print("X  ");
             }
             else if (!grid[i][j].isEmpty()) {
                Piece p = this.grid[i][j].getPiece();
                if (showBlue && showRed) {
                   System.out.print(p.getCharacter() + "  ");
                } else if (showRed && p.isRed()) {
                   System.out.print(p.getCharacter() + "  ");
                } else if (showBlue && !p.isRed()) {
                   System.out.print(p.getCharacter() + "  ");
                } else {
                   System.out.print("*  ");
                }
             }
             else { // empty cell  
                System.out.print("-  ");
             }
          }
          System.out.println();
       }
       System.out.println();
    }
 
    /**
       Method to return the grid for the board.
       @return the Cell[][] representing the board.
    */
    public Cell[][] getGrid() {
       return this.grid;
    }
 
    /**
       Method to assign the grid for the board.
       @param newBoard the Cell[][] to replace the current board.
    */
    public void setGrid(Cell[][] newBoard) {
       this.grid = newBoard;
    }
    
    /**
    Method to return the array that has the last two moves
    on the board. This is helpful with twoSquareRule.
    @param red is red piece or not
    @return the last two moves as an array of type Move
    */
    public Move[] getL2Moves(boolean red) {
       if (red) {
          return l2MovesR;
       }
       else {
          return l2MovesB;
       }
    }
 
    /**
    Method to set record last move on the board.
    This is helpful with twoSquareRule.
    @param move the move
    @param red is red piece or not
    */
    public void setLMove(Move move, boolean red) {
       if (red) {
          this.l2MovesR[0] = this.l2MovesR[1];
          this.l2MovesR[1] = move;
       }
       else {
          this.l2MovesB[0] = this.l2MovesB[1];
          this.l2MovesB[1] = move;
       }
    }
 
    
 /**
       Method to check if a move would go out of bounds.
       @param move the movement to check the bounds on.
       @return true if it goes out of bounds. False otherwise.
    */
    public boolean isOutOfBound(Move move) {
       int sr = move.getSr();
       int sc = move.getSc();
       int dr = move.getDr();
       int dc = move.getDc();
       return (sr >= SIZE || sr < 0 || sc >= SIZE || sc < 0 || dr >= SIZE 
          || dr < 0 || dc >= SIZE || dc < 0); 
    }
    
    /**
       Method to check if a move lands in a lake on 
       the current board.
       @param row the row
       @param col the column
       @return true if it goes out of bounds. False otherwise.
    */
    public boolean isLake(int row, int col) {
       return grid[row][col].isLake();
    }
 
    /** CHECK
    Method to get the count of movable pieces on the board
    for a certain player.
    @param red red player or not
    @return the count
    */
    public int getCountMovables(boolean red) {
       int count = 0;
       for (int row = 0; row < grid.length; row++) {
          for (int col = 0; col < grid[0].length; col++) {
             Piece checkPiece = grid[row][col].getPiece();
             if (checkPiece instanceof MPiece && checkPiece.isRed() == red) {
                count++;
             }
          }
       } 
       return count; 
    }
    
    /** CHECK
    Method to get movable pieces on the board
    for a certain player.
    @param red red player or not
    @return an array of cells containing movable pieces
    */
    public Cell[] getMovables(boolean red) {
       int countMove = getCountMovables(red);
       Cell[] moveables = new Cell[countMove];
       
       int incInd = 0;
       for (int row = 0; row < grid.length; row++) {
          for (int col = 0; col < grid[0].length; col++) {
             Piece checkPiece = grid[row][col].getPiece();
             if (checkPiece instanceof MPiece && checkPiece.isRed() == red) {
                moveables[incInd] = grid[row][col];
                incInd++;
             }
          }
       } 
       return moveables; 
    }
    
    /** CHECK
    Method to check if the flag of a certain player
    is still on the board.
    @param red red player or not
    @return true if the flag of the player is still on 
    the board, false otherwise.
    */
    public boolean hasFlag(boolean red) {
       
       for (int row = 0; row < grid.length; row++) {
          for (int col = 0; col < grid[0].length; col++) {
             Piece checkPiece = grid[row][col].getPiece();
             if (checkPiece instanceof ImPiece) {
                ImPiece checkedPiece = (ImPiece) checkPiece;
                if (checkedPiece.isFlag() && checkPiece.isRed() == red) {
                   return true;
                }
             }
          }
       }
       return false; 
    }
    
    /** CHECK
    Method to check the piece contained by cell is 
    trapped or not (trapped means cannot move!).
    @param cell the cell
    @return true if the the piece contained by cell is 
    trapped, false otherwise.
    */
    public boolean isTrapped(Cell cell) {
       int row = cell.getRow();
       int col = cell.getColumn();
        
       if (cell.getPiece() instanceof ImPiece) {
          return false;
       }
       
       Piece cellPiece = cell.getPiece();
       boolean red = cellPiece.isRed();
       
       Move moveLeft = new Move(row, col, row, col - 1);
       Move moveRight = new Move(row, col, row, col + 1);
       Move moveUp = new Move(row, col, row - 1, col);
       Move moveDown = new Move(row, col, row + 1, col);
       
       boolean checkLeft = checkDirection(moveLeft, row, col - 1, red);
       boolean checkRight = checkDirection(moveRight, row, col + 1, red);
       boolean checkUp  = checkDirection(moveUp, row - 1, col, red);
       boolean checkDown = checkDirection(moveDown, row + 1, col, red);
       
       if (!checkLeft || !checkRight || !checkUp || !checkDown) {
          return false;
       } 
       return true; 
    }
    
    /* Checks a given direction's cell to see if it would be a candidate for
     * blocking a piece. Checks to make sure that a destination direction is not
     * out of bounds, a lake, empty, or a different color.
     * 
     * @param moveDir, a move in a certain direction 
     * @param row the row of a destination cell
     * @param col the column of a destination cell
     * @param red a boolean that represnts the color of the source cell
     * @return true for the direction blocking the source piece
     *         false for the direction not blocking the source piece
     */
    private boolean checkDirection(Move moveDir, int row, 
                                   int col, boolean red) {
       if (!isOutOfBound(moveDir) && !isLake(row, col) &&
           (grid[row][col].isEmpty() || 
           (!grid[row][col].isEmpty() && 
           grid[row][col].getPiece().isRed() != red))) {
          return false;
       } 
       return true;
    }
    
    /** return turn. 
     * 
     * @return turn
     */
    public boolean getTurn() {
       return turn;
    }
    
    /** set turn. 
     * 
     * @param b the turn
    */
    public void setTurn(boolean b) {
       this.turn = b;
    }
 
 }
 