package Stratego;

/**
   Class to represent a movement.
   Holds on to the source row and column
   as well as the destination row and column
*/
public class Move {

    /**The source row.*/
    private int sr;
   
    /**The source column.*/
    private int sc;
   
    /**The destination row.*/
    private int dr;
   
    /**The destination column.*/
    private int dc;
 
 
    /**
       The constructor of a Move object.
       @param srow the source row
       @param scol the source column
       @param drow the destination row
       @param dcol the destination column
    */
    public Move(int srow, int scol, int drow, int dcol) {
       super();
       this.sr = srow;
       this.sc = scol;
       this.dr = drow;
       this.dc = dcol;
    }
 
    /**
       Method to update the source of movement.
       @param srow the new source row
       @param scol the new source column
    */
    public void setSource(int srow, int scol) {
       this.sr = srow;
       this.sc = scol;
    }
    
    /**
       Method to update the destination of movement.
       @param drow the new destination row
       @param dcol the new destination column
    */
    public void setDest(int drow, int dcol) {
       this.dr = drow;
       this.dc = dcol;
    }
 
    /**
       Method to update a move based on another move.
       @param move the movement to copy.
    */
    public void setMove(Move move) {
       this.sr = move.sr;
       this.sc = move.sc;
       this.dr = move.dr;
       this.dc = move.dc;
    }
 
    /**
       Method to get the sr of the movement.
       @return the source row.
    */
    public int getSr() {
       return this.sr;
    }
 
    /**
       Method to set the sr of the movement.
       @param srow the new source row of the movement
    */
    public void setSr(int srow) {
       this.sr = srow;
    }
 
    /**
       Method to get the sc of the movement.
       @return the source column.
    */
    public int getSc() {
       return this.sc;
    }
 
    /**
       Method to set the sc of the movement.
       @param scol the new source column of the movement
    */
    public void setSc(int scol) {
       this.sc = scol;
    }
 
    /**
       Method to get the dr of the movement.
       @return the destination row.
    */
    public int getDr() {
       return this.dr;
    }
 
    /**
       Method to set the dr of the movement.
       @param drow the new destination row of the movement
    */
    public void setDr(int drow) {
       this.dr = drow;
    }
 
    /**
       Method to get the dc of the movement.
       @return the destination column.
    */
    public int getDc() {
       return this.dc;
    }
 
    /**
       Method to set the dc of the movement.
       @param dcol the new destination column of the movement
    */
    public void setDc(int dcol) {
       this.dc = dcol;
    }
 
    @Override
    public int hashCode() {
       final int PRIME = 31;
       int result = 1;
       result = PRIME * result + this.dc;
       result = PRIME * result + this.dr;
       result = PRIME * result + this.sc;
       result = PRIME * result + this.sr;
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
       Move other = (Move) obj;
       if (this.dc != other.dc) {
          return false;
       }
       if (this.dr != other.dr) {
          return false;
       }
       if (this.sc != other.sc) {
          return false;
       }
       if (this.sr != other.sr) {
          return false;
       }
       return true;
    }
 
    /**Method to check if two moves are opposite.
       This means that the source of the first move
       is the destination of the second and reverse.
       @param obj the move to compare against.
       @return whether the moves are opposite.
    */
    public boolean isOpposite(Object obj) {
       if (this == obj) {
          return true;
       }
       if (obj == null) {
          return false;
       }
       if (getClass() != obj.getClass()) {
          return false;
       }
       Move other = (Move) obj;
       if (this.dc != other.sc) {
          return false;
       }
       if (this.dr != other.sr) {
          return false;
       }
       if (this.sc != other.dc) {
          return false;
       }
       if (this.sr != other.dr) {
          return false;
       }
       return true;
    }
    
    /** Method to check if the move is along a row.
    @return true if horizontal, false otherwise.
     */
    public boolean isHorizontal() {
       return (sc != dc) && (sr == dr);
    }
 
    /** Method to check if the move is along a column.
    @return true if vertical, false otherwise.
     */
    public boolean isVertical() {
       return (sr != dr) && (sc == dc);
    }
    
    /** Method to check if the move is along a row to right.
    @return true if horizontal right, false otherwise.
     */
    public boolean isHorizontalRight() {
       return (sc < dc) && (sr == dr);
    }
 
    /** Method to check if the move is along a column to down.
    @return true if vertical down, false otherwise.
     */
    public boolean isVerticalDown() {
       return (sr < dr) && (sc == dc);
    }
 
    @Override
    public String toString() {
       return "Move [sr=" + sr + ", sc=" + sc + ", dr=" 
              + dr + ", dc=" + dc + "]";
    }
    
    
 }
 