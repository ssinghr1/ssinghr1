package Connect4;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
 
public class Connect4 {

   public static void main(String[] args) throws IOException {
      Random rnd = new Random(0);
      Scanner kb = new Scanner(System.in);
      char[][] board;
      boolean redTurn;
      
      char input;
      boolean userQ = true;
      do {
         printMenu();
         String selection = kb.nextLine();
         if (selection.length() == 1) {
            input = selection.charAt(0);
         }
         else {
            input = 'i';
         }
         switch (input) {
            case 'q':
            case 'Q':
               userQ = false;
               continue;
               //break;
            case 'l':
            case 'L':
               System.out.println("Enter filename: ");
               String fileName = kb.nextLine();
               
               FileInputStream fileInStream = new FileInputStream(fileName);
               Scanner inFS = new Scanner(fileInStream);
               int row = inFS.nextInt();
               int col = inFS.nextInt();
               inFS.nextLine();
               board = new char[row][col];
               redTurn = loadGame(board, inFS); 
               fileInStream.close();
                
               play(board, kb, redTurn);           
               break;
            case 'n':
            case 'N':
               System.out.println("Enter number of columns: ");
               int width = kb.nextInt();
               while (width < 4 || width > 10) {
                  System.out.println("Error, enter number of columns: ");
                  width = kb.nextInt();
               }
               System.out.println("Enter number of rows: ");
               int length = kb.nextInt();
               while (length < 4 || length > 10) {
                  System.out.println("Error, enter number of rows: ");
                  length = kb.nextInt();
               }
               board = new char[width][length];
               initBoard(board);
               
               int boolRed = rnd.nextInt(1);
               redTurn = false;
               if (boolRed == 0) {
                  redTurn = false;
               }
               else {
                  redTurn = true;
               }
               play(board, kb, redTurn);
               kb.nextLine();
               break;
            default:
               System.out.println("Invalid choice!");
               break;
         }
      } while (userQ);
      
      kb.close();
   }
   
   /** NOT DONE
    * This method controls the entire game flow. As long as the game is being 
    * player by the players and "save" (i.e., -1) choice is not selected, the 
    * game continues. The flow stops when either the game ends 
    * (i.e., a player wins) or because -1 is entered to save the game.
    * @param board a 2D array which is the game's board representing 
    * the current state of the game
    * @param kb the scanner to be used to collect user inputs from 
    * standard input
    * @param redTurn a boolean indicating if it is red's player to move or not
    * @return -1 if save is selected, 0 if red wins, 1 if yellow wins,
    *          2 if the game is drawn.
    * @throws IOException
    */
   public static int play(char[][] board, Scanner kb, 
                     boolean redTurn) throws IOException {
      String pieceAdd = "";
      boolean gameOver = false;
      while (!gameOver) {
         if (redTurn) {
         //System.out.println("It is red's turn");
            pieceAdd = "red's";
         }
         else {
         //System.out.println("It is yellow's turn");
            pieceAdd = "yellow's";
         }
      
         System.out.println(pieceAdd + " turn, enter the move (-1 to save): ");
         int choice = kb.nextInt();
         if (choice == -1) {
            System.out.println("Enter Filename: ");
            kb.nextLine();
            String fileName = kb.nextLine();
            saveGame(board, fileName, redTurn);
            return -1;
         }
      
         move(board, choice, redTurn);
         printBoard(board);
         if (redTurn) {
            redTurn = false;
         }
         else if (!redTurn) {
            redTurn = true;
         }
      
         if (isGameOver(board) == 0) {
            System.out.println("Congrats, red wins!");
            gameOver = true;
            return 0;
         }
         else if (isGameOver(board) == 1) {
            System.out.println("Congrats, yellow wins!");
            gameOver = true;
            return 1;
         }
         else if (isGameOver(board) == 2) {
            System.out.println("Game is drawn!");
            gameOver = true;
            return 2;
         }
      }
      
   
      return -2;
   }

   /** DONE
    * This method plays a move on the board by adding a new piece 
    * to one of the columns on the correct row which is lowest 
    * empty row on the chosen column.
    * @param board the game board
    * @param index the index of the column a peice is being added
    * @param redTurn a boolean indicating which player's piece is being played
    * @return true if a piece is added successfully added on the 
    * board, false otherwise
    */
   public static boolean move(char[][] board, int index, boolean redTurn) {
      if (board[0][index] == 'R' || board[0][index] == 'Y') {
         return false;
      }
      if (board[board.length - 1][index] == '-') {
         if (redTurn) {
            board[board.length - 1][index] = 'R';
         }
         else {
            board[board.length - 1][index] = 'Y';
         }
         return true;
      }
      for (int i = 0; i < board.length - 1; i++) {
         if (board[i + 1][index] != '-') {
            if (redTurn) {
               board[i][index] = 'R';
            }
            else {
               board[i][index] = 'Y';
            }
            return true;
         }
         
      }
      printBoard(board);
      return false; 
   }

   /** This method prints the menu.
    * 
    */
   public static void printMenu() {
      System.out.println();
      System.out.println("n/N: New game");
      System.out.println("l/L: Load a game");
      System.out.println("q/Q: Quit");
      System.out.println("-------------");
      System.out.print("Enter your choice: ");
   }

   /** This method initialize the board to all empty cells.
    * 
    * @param board the board
    */
   public static void initBoard(char[][] board) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            board[i][j] = '-';
         }
      }
   }

   /** This method prints the board.
    * 
    * @param board the board
    */
   public static void printBoard(char[][] board) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            System.out.print(board[i][j] + " ");
         }
         System.out.println();
      }
   }

   /** 
    * This method saves the game in an external text file.
    *
    * @param board the current board
    * @param fileName the file name the game is going to be saved to
    * @param redTurn which player's turn it is next time the game will resume
    * @throws IOException
    */
   public static void saveGame(char[][] board, String fileName, 
                      boolean redTurn) throws IOException {
     
      FileOutputStream fileStream = new FileOutputStream(fileName);
      PrintWriter outFS = new PrintWriter(fileStream);
      outFS.print(board.length + " " + board[0].length);
      outFS.println();
      
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            outFS.print(board[i][j]);
         }
         outFS.println();
      }
      
      if (redTurn) {
         outFS.print('R');
      }
      else {
         outFS.print('Y');
      }
      outFS.close();
   }

   /** 
    * This method loads a game from a saved file. The board must be filled
    * according to the content of the file and whose turn it is to make a move
    * is also read and returned as a boolean from this method.
    * @param board the game board
    * @param inFS the open scanner associated with the file the game 
    * is being loaded from
    * @return true if it is red to move, false otherwise
    * @throws IOException
    */
   public static boolean loadGame(char[][] board, 
                 Scanner inFS) throws IOException {
      for (int row = 0; row < board.length; row++) {
         
         String nextRow = inFS.nextLine();
         for (int col = 0; col < board[0].length; col++) {
            board[row][col] = nextRow.charAt(col);
         }
      }
      boolean redTurn = true;
      if (inFS.next().charAt(0) == 'Y') {
         redTurn = false;
      }
      
      printBoard(board);
      return redTurn; 
   }
      
   /** 
    * This method takes the board and cycles through the indices to
    * check if there are any spots that have not been filled, and
    * returns a true if there are open spots, and false if there
    * are no open spots.
    *
    * @param board the game board
    * @return true if there are open spots, false if the board is
    * filled.
    */
   public static boolean hasOpenSpot(char[][] board) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[i].length; j++) {
            if (board[i][j] == '-') {
               return true;
            }
         }  
      }
      return false;
   }
   
   /**
    * This method checks for a wins where the pieces are horizontal
    * 4 in a row. returns 0 if red wins, 1 if yellow
    * wins, and 2 and neither is currently a win. 
    * 
    * @param board the board to look through
    * @return output value for win, 2 if neither win condition
    */
   public static int horiz(char[][] board) {
      // horizontal
      
      for (int row = 0; row < board.length; ++row) {
         for (int col = 0; col < board[0].length - 3; ++col) {
         
            if (board[row][col] == 'R' 
                && board[row][col + 1] == 'R' 
                && board[row][col + 2] == 'R'
                && board[row][col + 3] == 'R') {
               return 0;
            }
            if (board[row][col] == 'Y' 
                && board[row][col + 1] == 'Y' 
                && board[row][col + 2] == 'Y'
                && board[row][col + 3] == 'Y') {
               return 1;
            }
         }
      }
      return 2;
   }
   
   /**
    * This method checks for a wins where the pieces are vertical
    * 4 in a row. returns 0 if red wins, 1 if yellow wins, and 2 
    * if neither is currently a win. 
    * 
    * @param board the board to look through
    * @return output value for win, 2 if neither win condition
    */
   public static int vert(char[][] board) {
      // vertical
      for (int row = 0; row < board.length - 3; ++row) {
         for (int col = 0; col < board[0].length; ++col) {
            
            
            if (board[row][col] == 'R' 
                && board[row + 1][col] == 'R' 
                && board[row + 2][col] == 'R'
                && board[row + 3][col] == 'R') {
               return 0;
            }
            if (board[row][col] == 'Y' 
                && board[row + 1][col] == 'Y' 
                && board[row + 2][col] == 'Y'
                && board[row + 3][col] == 'Y') {
               return 1;
            }
         }
      }
      return 2;
   }
   
   /**
    * This method checks for a wins where the pieces are diagonal
    * 4 in a row going down. returns 0 if red wins, 1 if yellow 
    * wins, and 2 if neither is currently a win. 
    * 
    * @param board the board to look through
    * @return output value for win, 2 for no true win condition
    */
   public static int diagonalsDownRight(char[][] board) {
      //down and right horizontals
      for (int row = 3; row < board.length; ++row) {
         for (int col = 0; col < board[0].length - 3; ++col) {
            if (board[row][col] == 'R' 
                && board[row - 1][col + 1] == 'R' 
                && board[row - 2][col + 2] == 'R'
                && board[row - 3][col + 3] == 'R') {
               return 0;
            }
            if (board[row][col] == 'Y' 
                && board[row - 1][col + 1] == 'Y' 
                && board[row - 2][col + 2] == 'Y'
                && board[row - 3][col + 3] == 'Y') {
               return 1;
            }
         }
      }
      return 2;
   }
   
   /**
    * This method checks for a wins where the pieces are diagonal
    * 4 in a row going up. returns 0 if red wins, 1 if yellow 
    * wins, and 2 if neither is currently a win. 
    * 
    * @param board the board to look through
    * @return output value for win, 2 for no true win condition
    */
   public static int diagonalsUpRight(char[][] board) {  
      //up and to the right
      for (int row = 0; row < board.length - 3; ++row) {
         for (int col = 0; col < board[0].length - 3; ++col) {
            if (board[row][col] == 'R'
                && board[row + 1][col + 1] == 'R' 
                && board[row + 2][col + 2] == 'R'
                && board[row + 3][col + 3] == 'R') {
               return 0;
            }
            if (board[row][col] == 'Y'
                && board[row + 1][col + 1] == 'Y' 
                && board[row + 2][col + 2] == 'Y'
                && board[row + 3][col + 3] == 'Y') {
               return 1;
            }
         }
      }
      return 2;
   }
    
   /** 
    * This method decides whether the game is over or not. The game is 
    * over if the player as indicated by red wins the game based on the 
    * current state of the board. The method can also tell if the game 
    * is drawn or not.
    *
    * @param board the game board
    * @return 0 if red wins the game, 1 if
    *         yellow wins, 2 if draw, 3 otherwise.
    */
   public static int isGameOver(char[][] board) {
      
      int horizCheck = horiz(board);
      if (horizCheck == 0 || horizCheck == 1) {
         return horizCheck;
      }
      int vertCheck = vert(board);
      if (vertCheck == 0 || vertCheck == 1) {
         return vertCheck;
      }
      
      int diagUpCheck = diagonalsUpRight(board);
      if (diagUpCheck == 0 || diagUpCheck == 1) {
         return diagUpCheck;
      }
      int diagDownCheck = diagonalsDownRight(board);
      if (diagDownCheck == 0 || diagDownCheck == 1) {
         return diagDownCheck;
      }
            
      if (!hasOpenSpot(board)) {
         return 2;   
         
      }
      return 3; 
   }

}
