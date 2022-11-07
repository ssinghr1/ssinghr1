package Stratego;

/** 
   Class to represent a single cell in the board.
   Contains the information necessary to know 
   the physical piece type and the location of the piece.
*/
public class Cell {

    /**The piece in the cell.*/
    private Piece piece;
    
    /**The row the cell is in.*/
    private int row;
    
    /**The column the cell is in.*/
    private int column;
    
    /**The column the cell is in.*/
    private boolean lake;
    
 
    /**
       Constuctor that only assigns the piece.
       @param newPiece the piece in the cell
    */
    public Cell(Piece newPiece) {
       this.piece = newPiece;
       lake = false;
    }
 
    /**
       Constructor to assign the location and peice in a cell.
       @param newPiece the piece in the cell
       @param newRow the row the cell represents
       @param newColumn the column the cell represents
    */
    public Cell(Piece newPiece, int newRow, int newColumn) {
       this.piece = newPiece;
       this.row = newRow;
       this.column = newColumn;
       this.lake = false; 
    }
 
    /**
       Constructor to assign the location of the cell.
       @param newRow the row the cell represents
       @param newColumn the column the cell represents
       @param lake lake or not
 
    */
    public Cell(int newRow, int newColumn, boolean lake) {
       this.row = newRow;
       this.column = newColumn;
       this.piece = null;
       this.lake = lake;
    }
 
    /**
       Default constructor.
       Sets the piece to null and the row/column to -1
    */
    public Cell() {
       this.piece = null;
       this.row = -1;
       this.column = -1;
    }
 
    /**
       Method to get the piece in the cell.
       @return the piece in the current cell
    */
    public Piece getPiece() {
       return this.piece;
    }
 
    /**
       Method to assign a piece to the current cell.
       @param newPiece the new peice for the current cell.
    */
    public void setPiece(Piece newPiece) {
       this.piece = newPiece;
    }
 
    /**
       Method to get the row the cell is in.
       @return the row of the cell.
    */
    public int getRow() {
       return this.row;
    }
 
    /**
       Method to assign the row of the cell.
       @param newRow the row to set the cell to.
    */
    public void setRow(int newRow) {
       this.row = newRow;
    }
 
    /**
       Method to get the column the cell is in.
       @return the column of the cell.
    */
    public int getColumn() {
       return this.column;
    }
    
    /**
       Method to assign the column of the cell.
       @param newColumn the column to set the cell to.
    */
    public void setColumn(int newColumn) {
       this.column = newColumn;
    }
 
    /** check if the current cell is a lake cell.
    @return true if lake, false otherwise
     */
    public boolean isLake() {
       return lake;
    }
 
    /**
       Method to check if the current cell is empty.
       @return true if empty, false otherwise
    */
    public boolean isEmpty() {
       return (!isLake() && getPiece() == null);
    }
    
    /**
     Method to set lake field.
     @param l lake
     */
    public void setLake(boolean l) {
       this.lake = l;
    }
 
    @Override
    public String toString() {
       return "Cell [piece=" + piece + ", row=" + row + ", column=" 
              + column + ", lake=" + lake + ", isEmpty()=" + isEmpty() + "]";
    }
 
    @Override
    public int hashCode() {
       final int PRIME = 31;
       int result = 1;
       result = PRIME * result + column;
       result = PRIME * result + (lake ? 1231 : 1237);
       result = PRIME * result + ((piece == null) ? 0 : piece.hashCode());
       result = PRIME * result + row;
       return result;
    }
 
    @Override
    public boolean equals(Object obj) {
       if (this == obj) {
          return true;
       }
       if (obj == null) {
          return false;
       }
       if (getClass() != obj.getClass()) {
          return false;
       }
       Cell other = (Cell) obj;
       if (column != other.column) {
          return false;
       }
       if (lake != other.lake) {
          return false;
       }
       if (piece == null) {
          if (other.piece != null) {
             return false;
          }
       } else if (!piece.equals(other.piece)) {
          return false;
       }
       if (row != other.row) {
          return false;
       }
       return true;
    }
     
    
    
 
 }
 