package Stratego;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
   Class to perform all fileIO for a stratego board.
*/
public class FileIO {

   /**
      Fills the board based on the contents of a file.
      It creates a new Cell[][] and grabs a 
      piece type for each cell and gives
      it the correct value.
      @param fileName file name
      @param board the board to fill
      @return the current turn number
 * @throws FileNotFoundException 
   */
   public static int fillBoard(String fileName, Board board) 
                              throws FileNotFoundException {
      Scanner inFS = new Scanner(new FileInputStream(fileName));
      Cell[][] b = new Cell[board.size()][board.size()];
      for (int i = 0; i < b.length; i++) {
         for (int j = 0; j < b[0].length; j++) {
            char c = inFS.next().charAt(0);
            if (c == '-') {
               b[i][j] = new Cell(i, j, false);
            }
            else if (c == 'X' || c == 'x') { // lake cell
               b[i][j] = new Cell(i, j, true);
            }
            else if (Character.isUpperCase(c)) { // red piece
               if (c == 'B' || c == 'F') {
                  b[i][j] = new Cell(new ImPiece(c, true, c == 'F'), i, j);
               } else {
                  b[i][j] = new Cell(new MPiece(c, true), i, j);
               }
            }
            else if (Character.isLowerCase(c)) { // blue piece
               if (c == 'b' || c == 'f') {
                  b[i][j] = new Cell(new ImPiece(c, false, c == 'f'), i, j);
               }
               else {
                  b[i][j] = new Cell(new MPiece(c, false), i, j);
               }
            }
         
         }
      }
      char turn = Character.toLowerCase(inFS.next().charAt(0)); 
      inFS.close();
      board.setGrid(b);
      board.setTurn(turn == 'r' ? true : false);
      return turn;
   }

   /**Function that saves the current board to a file.
      @param fileName file name
      @param board the board to save to the file
      @param turn the current turn number
      @throws IOException on error writing to file
   */
   public static void saveBoard(String fileName, Board board, 
                                char turn) throws IOException {
      PrintWriter bf = new PrintWriter(fileName);
      Cell[][] b = board.getGrid();
      for (int i = 0; i < board.size(); i++) {
         for (int j = 0; j < board.size(); j++) {
            if (b[i][j].isLake()) {
               bf.write("X ");
            } else if (b[i][j].isEmpty()) {
               bf.write("- ");
            } else {
               bf.write(b[i][j].getPiece().getCharacter() + " ");
            }
         }
         bf.write("\n");
      }
      bf.write(turn);
      bf.flush();
      bf.close();
   }
}
