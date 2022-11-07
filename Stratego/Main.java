package Stratego;

import java.io.IOException;
import java.util.Scanner;


/** The class that contains the main method.
   This is what is run to run your program.
*/
public class Main {

   /** A method to print the user menu to standard input.
   */
   private static void printMenu() {
      System.out.println("Q or q - quit the game");
      System.out.println("D or d - display the board");
      System.out.println("L or l - load a game");
      System.out.println("S or s - save the current game");
      System.out.println("M or m - make a move: sr sc dr dc\n \t sr and sc:"
            + " the row and column indices of source\n"
            + "\t dr and dc: row and column indices "
            + "of destination");
   }
   
   /**
      A method to print the board.
      @param board the board
      @param turn whose turn it is 
   */
   private static void printBoard(Board board, boolean turn) {
      if (turn) {
         board.display(true, false); // only show red pieces
         System.out.println("It's red's turn!");
      }
      else {
         board.display(false, true); //only show blue pieces
         System.out.println("It's blue's turn!");
      }
   }
   
   /**
      Tries the entered move by a player.
      Prints relevant messages on screen.
      @param board the board
      @param move the intended move
      @param turn whose turn it is
      @return true if the move is successfully made, false otherwise.
   */
   private static boolean tryMove(Board board,
                                  Move move, boolean turn) {
      if (board.isOutOfBound(move)) {
         System.out.println("Out of bounds! Try again!");
         return false;
      }
     
      int sr = move.getSr();
      int sc = move.getSc();
      int dr = move.getDr();
      int dc = move.getDc();
      
      Cell cell = board.getGrid()[sr][sc];
      
      if (cell.isLake()) {
         System.out.println("(" + sr + ", " + sc + 
                            ") is a lake!");
         return false;
      }
      
      if (cell.isEmpty()) {
         System.out.println("No piece at (" + sr + ", " + sc + ")");
         return false;  
      }
      
      Piece p = cell.getPiece();
      
      if (!(p instanceof MPiece)) {
         System.out.println("Piece at (" + sr + ", " + sc + 
                            ") is not a movable piece!");
         return false;
      }
      
      if (turn && !p.isRed()) {
         System.out.println("Not blue's turn!");
         return false;
      }
      
      if (!turn && p.isRed()) {
         System.out.println("Not red's turn!");
         return false;
      }
      
      if (!((MPiece) p).isLegalMove(move)) {
         System.out.println("Not a legal move for " + 
                             p.getCharacter());
         return false;
      }
      
      if (Stratego.crossesLake(board, move)) {
         System.out.println("Cannot cross or land in a lake!");
         return false; // crossing or landing in a lake is not allowed!
      }
      
      if (Stratego.crossesAnotherPiece(board, move)) {
         System.out.println("Cannot jump over another piece!");
         return false; // jumping over a piece is not allowed!
      }
      
      if (!board.getGrid()[dr][dc].isEmpty() &&
          p.isRed() == board.getGrid()[dr][dc].getPiece().isRed()) {
         System.out.println("cannot move to a cell with the same color piece");
         return false; // cannot move to a cell with the same color piece
      }
      
      if (!Stratego.twoSquareRule(board, move)) {
         //System.out.println(!Stratego.twoSquareRule(board, move));
         System.out.println("A piece cannot make three consecutive "
              + "moves between same cells!");
         return false;
      }
      if (!((MPiece) cell.getPiece()).makeMove(board, move)) {
         System.out.println("Move failed!");
         return false;
      }
      
      System.out.println("Move successful!");
      return true;
   }
   
   /**
      Main method of Stratego.
      Runs the game and gives all the options.
      @param args ignored.
      @throws IOException if an input file does not exist or is corrupted
      @throws InterruptedException if something goes wrong.
   */
   public static void main(String[] args) 
         throws IOException, InterruptedException {
      Board board = Board.getInstance();
      FileIO.fillBoard("in.txt", board);
      Scanner scnr = new Scanner(System.in);
      String choice = "";
      printMenu();
      while (!"q".equals(choice)) {
         System.out.print("?> ");
         choice = scnr.nextLine();
         choice = choice.toLowerCase();
         if ("m".equals(choice)) {
            printBoard(board, board.getTurn());
            int sr = scnr.nextInt();
            int sc = scnr.nextInt();
            int dr = scnr.nextInt();
            int dc = scnr.nextInt();
            scnr.nextLine();
            Move move = new Move(sr, sc, dr, dc);
            // try the intended move: do all necessary checks and 
            // print relevant messages as appropriate
            if (!tryMove(board, move, board.getTurn())) {
               continue;
            }
            int go = Stratego.isGameOver(board);
            if (go == 1) {
               System.out.println("Congrats! Red Wins!");
               break;
            }
            else if (go == 2) {
               System.out.println("Congrats! Blue Wins!");
               break;
            }
            board.setTurn(!board.getTurn());
         }
         else if ("s".equals(choice)) {
            System.out.println("What file do you want to save to?");
            String oFileName = scnr.nextLine(); 
            // save board and whose turn it is
            FileIO.saveBoard(oFileName, board, 
                             board.getTurn() ? 'r' : 'b'); 
            System.out.println("Successfully saved to " + oFileName);
         }
         else if ("l".equals(choice)) {
            System.out.println("What file do you want to load? "
               + "(must be in the same folder)");
            String iFileName = scnr.nextLine(); 
            FileIO.fillBoard(iFileName, board);
            System.out.println(iFileName + " Successfully loaded!");
            int go = Stratego.isGameOver(board);
            if (go == 1) {
               System.out.println("Congrats! Red Wins!");
               break;
            }
            else if (go == 2) {
               System.out.println("Congrats! Blue Wins!");
               break;
            }
         }
         else if ("d".equals(choice)) {
            board.display(true, true);
         }
         else {
            System.out.println("Not a valid input! Try again!");
         }
      }
      scnr.close();
   }


}
