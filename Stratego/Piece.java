package Stratego;

/**
Abstract class to represent a piece.
There is no way to create a piece object.
A piece is an abstract thing. We can not 
have a piece without a specific type (moveable
or immoveable) which is why this is abstract.
*/
public abstract class Piece {

/**The character representation of the piece.*/
protected char character;

/** Which player owns the piece.
true for red, false for blue 
*/
protected boolean red; 


/**The main constructor for a piece.
   @param setChar the char to set the piece to.
   @param setColor the player who owns the new piece. 
*/
public Piece(char setChar, boolean setColor) {
   super();
   this.character = setChar;
   this.red = setColor;
}

/**The constructor for a piece that only takes in a name.
   The default owner is red.
   @param setChar the char to set the piece to.
*/
public Piece(char setChar) {
   super();
   this.character = setChar;
   this.red = true;
}

/**
   Returns the char that represents the piece.
   @return the character of the piece.
*/
public char getCharacter() {
   return this.character;
}

/**
   Method to assign a new character to a piece.
   @param setChar the new value to assign to a piece.
*/
public void setCharacter(char setChar) {
   this.character = setChar;
}

/**
   Method to see who owns the piece.
   @return if red owns the piece.
*/
public boolean isRed() {
   return this.red;
}

/**Method to update who owns the piece.
   @param setColor the color to assign to the piece.
   true for red, false for blue.
*/
public void setColor(boolean setColor) {
   this.red = setColor;
}


@Override
public String toString() {
   return "Piece [piece=" + this.character + ", color=" + this.red + "]";
}

@Override
public int hashCode() {
   final int PRIME = 31;
   int result = 1;
   result = PRIME * result + character;
   result = PRIME * result + (red ? 1231 : 1237);
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
   Piece other = (Piece) obj;
   if (character != other.character) {
      return false;
   }
   if (red != other.red) {
      return false;
   }
   return true;
}


}
