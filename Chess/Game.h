#ifndef GAME_H
#define GAME_H

#include <iostream>
#include "Piece.h"
#include "Board.h"
#include "Exceptions.h"

namespace Chess
{

  class Game {
    
  public:
    // This default constructor initializes a board with the standard
    // piece positions, and sets the state to white's turn
    Game();
    
    //copy constructor
    Game(const Game& original);
    
    //destructor
    ~Game();
    
    
    int turnMisMatch(const Position& start);
    void invalidMoveException(int returncode);
    int checkValidMove(const Position& start, const Position& end) const;
    // Returns true if it is white's turn
    bool turn_white() const { return is_white_turn; }
    
    // Displays the game by printing it to stdout
    void display() const { board.display(); }

    // Checks if the game is valid
    bool is_valid_game() const { return board.has_valid_kings(); }

    // Attempts to make a move. If successful, the move is made and
    // the turn is switched white <-> black. Otherwise, an exception is thrown
    void make_move(const Position& start, const Position& end);
    
    //check if attempting this move will expose a check
    bool exposesCheck(const Position& start, const Position& end);
    // Returns true if the designated player is in check
    bool in_check(const bool& white) const;
    
    // Returns true if the designated player is in mate
    bool in_mate(const bool& white) const;
    
    // Returns true if the designated player is in mate
    bool in_stalemate(const bool& white) const;
    
    // Return the total material point value of the designated player
    int point_value(const bool& white) const;
    
    
  private:
    // The board
    Board board;
    
    // Is it white's turn?
    bool is_white_turn;

    // Writes the board out to a stream
    friend std::ostream& operator<< (std::ostream& os, const Game& game);
    
    // Reads the board in from a stream
    friend std::istream& operator>> (std::istream& is, Game& game);
  };
}
#endif // GAME_H
