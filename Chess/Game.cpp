#include <cassert>
#include "Game.h"

namespace Chess
{

  Game::Game() : is_white_turn(true)
  {
    // Add the pawns
    for (int i = 0; i < 8; i++)
      {
	board.add_piece(Position('A' + i, '1' + 1), 'P');
	board.add_piece(Position('A' + i, '1' + 6), 'p');
      }
    
    // Add the rooks
    board.add_piece(Position('A' + 0, '1' + 0), 'R');
    board.add_piece(Position('A' + 7, '1' + 0), 'R');
    board.add_piece(Position('A' + 0, '1' + 7), 'r');
    board.add_piece(Position('A' + 7, '1' + 7), 'r');
    
    // Add the knights
    board.add_piece(Position('A' + 1, '1' + 0), 'N');
    board.add_piece(Position('A' + 6, '1' + 0), 'N');
    board.add_piece(Position('A' + 1, '1' + 7), 'n');
    board.add_piece(Position('A' + 6, '1' + 7), 'n');
    
    // Add the bishops
    board.add_piece(Position('A' + 2, '1' + 0), 'B');
    board.add_piece(Position('A' + 5, '1' + 0), 'B');
    board.add_piece(Position('A' + 2, '1' + 7), 'b');
    board.add_piece(Position('A' + 5, '1' + 7), 'b');
    
    // Add the kings and queens
    board.add_piece(Position('A' + 3, '1' + 0), 'Q');
    board.add_piece(Position('A' + 4, '1' + 0), 'K');
    board.add_piece(Position('A' + 3, '1' + 7), 'q');
    board.add_piece(Position('A' + 4, '1' + 7), 'k');
  }
  //copy constructor
  Game::Game(const Game &original)
  {
    board = original.board;
    is_white_turn = original.is_white_turn;
  }
  
  //destructor
	Game::~Game()
	{
	}
  
  int Game::checkValidMove(const Position &start, const Position &end) const
  {
    //if there is a no piece in end, move as long as right shape
    
    bool validStartVertPosition = (start.second >= '1' && start.second <= ('1' + 7));
    bool validStartHoriPosition = (start.first >= 'A' && start.first <= ('A' + 7));
    bool validEndVertPosition = (end.second >= '1' && end.second <= ('1' + 7));
    bool validEndHoriPosition = (end.first >= 'A' && end.first <= ('A' + 7));
    
    if (!validStartVertPosition || !validStartHoriPosition)
      {
	return 1;
      }
    
    if (!validEndVertPosition || !validEndHoriPosition)
      {
	return 2;
      }
    
    //check if there is piece at start position
    const Piece *checkStart = board(start);
    if (checkStart == NULL)
      {
	return 3;
      }
    const Piece *cur = board(start);
    
    if (board(end) == NULL)
      {
	if (!cur->legal_move_shape(start, end))
	  {	    
	    return 5;
	  }
      }
    else if (board(end) != NULL)
      {
	if (!cur->legal_capture_shape(start, end))
	  {
	    return 7;
	  }
	
	else if (board(end)->is_white() == board(start)->is_white())
	  {
	    return 6;
	  }
      }
    
    
    
    //check if spot is occupied along path
    int horizDisp = end.first - start.first;
    int vertDisp = end.second - start.second;
    
    //diagonal
    if (abs(horizDisp) == abs(vertDisp))
      {
	//diagonal case with slope  = 1
	if (horizDisp == vertDisp) {
	  for (int i = 1; i < abs(vertDisp); i++)
	    {
	      //check if the position along the diagonal has a piece
	      int places = horizDisp / abs(horizDisp) * i;
	      if (board(Position(start.first + places, start.second + places)))
		{
		  return 8;
		}
	    }
	}
	
	else if (horizDisp == -vertDisp) {
	  
	  //diagonal case with slope = -1
	  for (int j = 1; j < abs(vertDisp); j++)
	    {
	      //check if the position along the diagonal has a piece
	      int places = horizDisp / abs(horizDisp) * j;
	      if (board(Position(start.first + places, start.second - places)))
		{
		  return 8;
		}
	    }
	}
      }
    
    //horiz case
    if (horizDisp != 0 && vertDisp == 0)
      {
	for (int i = 1; i < abs(horizDisp); i++)
	  {
	    int places = horizDisp / abs(horizDisp) * i;
	    if (board(Position(start.first + places, start.second)))
	      {
		return 8;
	      }
	  }
      }

    //vert case
    if (vertDisp != 0 && horizDisp == 0)
      {
	for (int j = 1; j < abs(vertDisp); j++)
	  {
	    int places = vertDisp / abs(vertDisp) * j;
	    if (board(Position(start.first, start.second + places)))
	      {
		return 8;
	      }
	  }
      }
    
    return 0;
  }
  
  int Game::turnMisMatch(const Position &start)
  {
    //Getting piece at position
    const Piece *checkStart = board(start);
    
    if (checkStart != NULL) {
      if (
	  //If black turn but moving white piece
	  (!(is_white_turn) && checkStart->is_white()) ||
	  //If white turn but moving black piece
	  ((is_white_turn) && !(checkStart->is_white())))
	{
	  return 4;
	}
    }
    return 0;
  }
  
  void Game::invalidMoveException(int returncode)
  {
    switch (returncode)
      {
      case -1:
	throw Exception("Cannot load the game!");
	break; //cannot load the file from input
      case 0:	   //literally nothing this is so good
      case 1:
	throw Exception("start position is not on board");
	break;
      case 2:
	throw Exception("end position is not on board");
	break;
      case 3:
	throw Exception("no piece at start position");
	break;
      case 4:
	throw Exception("piece color and turn do not match");
	break;
      case 5:
	throw Exception("illegal move shape");
	break;
      case 6:
	throw Exception("cannot capture own piece");
	break;
      case 7:
	throw Exception("illegal capture shape");
	break;
      case 8:
	throw Exception("path is not clear");
	break;
      case 9:
	throw Exception("move exposes check");
	break;
      }
  }

  void Game::make_move(const Position &start, const Position &end)
  {
    //change the piece location if valid move. change piece location and also delte piece that was there if capture
    
    //IMPLEMENTATION:
    
    int isValidMove = checkValidMove(start, end);
    
    //Perform based on valid move or not
    if (turnMisMatch(start) == 4)
      {
	invalidMoveException(4);
      }
    
    if (isValidMove != 0)
      {
	//std::cout << isValidMove << std::endl;
	//Call exception handler function
	invalidMoveException(isValidMove);
      }
    else if (exposesCheck(start, end))
      {
	//attempting this move will expose a check, so cant make it, throw an exception.
	invalidMoveException(9);
      }
    else
      { //Move is valid, so proceed
	
	//special cases for pawn
	bool isPawn = board(start)->to_ascii() == 'p' || board(start)->to_ascii() == 'P';
	
	board.remove_piece(end); //get rid of the end piece so there is no conflict
	
	if (isPawn && (end.second == '8' || end.second == '1')) //piece_promotionf for pawn
	  {
	    board.add_piece(Position(end.first, end.second), board(start)->to_ascii() + 1); //+1 because q is 1 after p and Q is 1 after P
	  }
	else //not a pawn, make the move
	  {
	    board.add_piece(Position(end.first, end.second), board(start)->to_ascii());
	  }
	//Delete the original piece in the start position
	//not sure how to delete the old piece.
	board.remove_piece(start); //remove starting piece so there isnt 2 copies
	//switch turn color so that opposite color can take a turn
	is_white_turn = !is_white_turn;
      }
  }


  //function to check whether the move will expose player to check
  bool Game::exposesCheck(const Position &start, const Position &end)
  {
    
    Game replica = *this;
    const char start_piece_char = board(start)->to_ascii();
    replica.board.remove_piece(start);
    replica.board.remove_piece(end);
    replica.board.add_piece(end, start_piece_char);
    const bool white = board(start)->is_white();
    if (replica.in_check(white))
      {
	//delete replica;
	return true;
      }
    //delete replica;
    return false;
  }
  
  //function to check if a player is in chess
  bool Game::in_check(const bool &white) const
  {
    //First loop to get Kings
    Position kingPos = Position('A', '1');
    for (Board::iterator it = board.begin(); it != board.end(); it++)
      {
	//Get current piece
	if (board(*it))
	  {
	    const Piece *currPiece = board(*it);
	    if (white)
	      {
		if (currPiece->to_ascii() == 'K')
		  {
		    kingPos = *it;
		  }
	      }
	    else
	      {
		if (currPiece->to_ascii() == 'k')
		  {
		    kingPos = *it;
		  }
	      }
	  }
      } 
    
    bool return_val = false;
    for (Board::iterator it = board.begin(); it != board.end(); it++)
      {
	
	if (board(*it))
	  {
	    //Get current piece
	    const Piece *currPiece = board(*it);
	    Board *boardCopy = new Board(); //use the copy constructor to make a board copy
	    
	    if (currPiece->is_white() != white)
	      {
		//trying to make a move with opposite piece but currently have is_white
		
		const int return_code = checkValidMove(*it, kingPos);
		//if valid move, then king can be attacked, so you are in check
		if (return_code == 0)
		  {
		    return_val = true;
		  }
	      }
	    delete boardCopy;
	  }
      }
    return return_val;
  }

  
  //function to determine if you are in mate
  bool Game::in_mate(const bool &white) const
  {
    bool in_checkmate = true;
    //if you are not in check, you are not in mate
    if (in_check(white) == false)
      {
	return false;
      }
    
    //loop through every piece's possible moves to see if the move can be made without putting yourself in check
    //if a move is valid and does not put you check, you are not in checkmate
    for (Board::iterator it = board.begin(); it != board.end() && in_checkmate; it++)
      {
	if (board(*it) && board(*it)->is_white() == white)
	  {
	    for (Board::iterator lit = board.begin(); lit != board.end() && in_checkmate; lit++)
	      {
		//make a replica of Game using copy constructor
		Game replica = *this;
		if (*it != *lit)
		  {
		    //check if move is valid
		    if (replica.checkValidMove(*it, *lit) == 0)
		      {
			//make move and see if it puts you in check
			replica.board.remove_piece(*lit);
			replica.board.remove_piece(*it);
			replica.board.add_piece(*lit, board(*it)->to_ascii());
			
			//if not in check after move, you can make the move, and not in checkmate
			if (replica.in_check(white) == false)
			  {
			    in_checkmate = false;
			  }
		      }
		  }
	      }
	  }
      }
    
    return in_checkmate;
  }
  
  //function to determine if the game is in stalemate
  bool Game::in_stalemate(const bool &white) const
  {
    bool in_stale = true;
    for (Board::iterator it = board.begin(); it != board.end() && in_stale; it++)
      {
	//if white go through all the moves that can be made
	if (board(*it) && (board(*it)->is_white() == white))
	  {
	    //iterate through all of the moves that can be made on the board and cehck if valid move
	    for (Board::iterator lit = board.begin(); lit != board.end() && in_stale; lit++)
	      {
		//check if valid move
		if (*it != *lit && checkValidMove(*it, *lit) == 0)
		  {
		    //if valid move, make move on the replica of the Game (made through copy constructor
		    Game replica = *this;
		    
		    replica.board.remove_piece(*lit);
		    replica.board.remove_piece(*it);
		    replica.board.add_piece(*lit, board(*it)->to_ascii());
		    
		    //check if this move puts you in check, if not, in_stale = false and will break out of both for loops
		    if (replica.in_check(white) == false)
		      {
			in_stale = false;
		      }
		  }
	      }
	  }
      }
    
    return in_stale;
  }
  
  // Return the total material point value of the designated player
  int Game::point_value(const bool &white) const
  {
    int w_total_point_val = 0;
    int b_total_point_val = 0;
    
    //loop through all the positions on the board and add up the points for each piece
    for (Board::iterator it = board.begin(); it != board.end(); it++)
      {
	//check if there is a piece on the position 
	if (board(*it) != nullptr)
	  {
	    if (white)
	      {
		switch (board(*it)->to_ascii())
		  {
		  case 'P':
		    w_total_point_val += 1;
		    break;
		  case 'N':
		  case 'B':
		    w_total_point_val += 3;
		    break;
		  case 'R':
		    w_total_point_val += 5;
		    break;
		  case 'Q':
		    w_total_point_val += 9;
		    break;
		  }
	      }
	    else
	      {
		switch (board(*it)->to_ascii())
		  {
		  case 'p':
		    b_total_point_val += 1;
		    break;
		  case 'n':
		  case 'b':
		    b_total_point_val += 3;
		    break;
		  case 'r':
		    b_total_point_val += 5;
		    break;
		  case 'q':
		    b_total_point_val += 9;
		    break;
		  }
	      }
	  }
      }
    
    if (white)
      {
	return w_total_point_val;
      }
    else
      {
	return b_total_point_val;
      }
  }
  
  std::istream &operator>>(std::istream &is, Game &game)
  {
    //loop through the size of a game board as this will not change in the file
    for (Board::iterator it = game.board.begin(); it != game.board.end(); it++)
      {
	game.board.remove_piece(*it);
      }
    for (int r = '8'; r >= '1'; r--)
      {
	for (char c = 'A'; c <= 'H'; c++)
	  {
	    //retrieve the current character in the array
	    char desigChar = 'a';
	    is >> desigChar;
	    //when crossing by a piece, add it to the game's board
	    if (desigChar != '-')
	      {
		//game.board.remove_piece(Chess::Position(c, r));
		game.board.add_piece(Chess::Position(c, r), desigChar);
	      }
	  }
      }
    
    //retrieve the char at the end of the file that designates whose turn it is
    char charBool = 'a';
    is >> charBool;
    //char at bottom shows who's turn is next, so b means that it is not white turn.
    if (charBool == 'w')
      {
	game.is_white_turn = true;
      }
    else
      {
	game.is_white_turn = false;
      }
    return is;
  }
  
  std::ostream &operator<<(std::ostream &os, const Game &game)
  {
    // Write the board out and then either the character 'w' or the character 'b',
    // depending on whose turn it is
    return os << game.board << (game.turn_white() ? 'w' : 'b');
  }
}
