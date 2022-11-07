#ifndef BOARD_H
#define BOARD_H

#include <iostream>
#include <map>
#include "Piece.h"
#include "Pawn.h"
#include "Rook.h"
#include "Knight.h"
#include "Bishop.h"
#include "Queen.h"
#include "King.h"
#include "Mystery.h"
#include <iterator>

namespace Chess
{
  class Board
  {
    
    // Throughout, we will be accessing board positions using Position defined in Piece.h.
    // The assumption is that the first value is the column with values in
    // {'A','B','C','D','E','F','G','H'} (all caps)
    // and the second is the row, with values in {'1','2','3','4','5','6','7','8'}
    
  public:
    // Default constructor
    Board();
    //copy constructor
    Board(const Board &origBoard);
    //assignment operator
    Board& operator=(const Board &origBoard);
    
    // const Board operator*(){return *this;}
    
    ~Board();
    // Returns a const pointer to the piece at a prescribed location if it exists,
    // or nullptr if there is nothing there.
    const Piece *operator()(const Position &position) const;
    
    // Attempts to add a new piece with the specified designator, at the given position.
    // Throw exception for the following cases:
    // -- the designator is invalid, throw exception with error message "invalid designator"
    // -- the specified position is not on the board, throw exception with error message "invalid position"
    // -- if the specified position is occupied, throw exception with error message "position is occupied"
    void add_piece(const Position &position, const char &piece_designator);
    
    // Displays the board by printing it to stdout
    void display() const;
    
    // Returns true if the board has the right number of kings on it
    bool has_valid_kings() const;
    
    //removes a piece from a certain location
    void remove_piece(const Position &position);
    
    //iterator class for Board
    class iterator
    {
      
      //Current position in iterator class
    private:
      //uses index (var) to keep track of position
      int var;
      Position pos;

      //function to set index (var)
      void set_index(int start_idx)
      {
	var = start_idx;
	pos.first = 'A' + var % 8;
	pos.second = '1' + var / 8;
      }
            
    public:
      //default constructor 
      iterator()
      {
	var = 0;
	pos.first = 'A';
	pos.second = '1';
      }
      //non-default constructor -> calls set_index
      iterator(int idx) : var(idx)
      {
	set_index(idx);
      }

      //++ operator
      iterator &operator++()
      {
	set_index(var + 1);
	return *this;
      }

      //++operator
      iterator operator++(int)
      {
	iterator it = *this;
	set_index(var + 1);
	return it;
      }

      //equal operator
      bool operator==(iterator o) const
      {
	//compare index
	return o.var == var;
      }

      //!= operator
      bool operator!=(iterator o) const
      {
	//compare index
	return o.var != var;
      }

      //deference operator
      Position &operator*()
      {
	//returns position
	return pos;
      }
      
      Position *operator->()
      {
	return &pos;
      }
    };

    //begin
    iterator begin() const
    {
      return iterator();
    }

    //end -> return position after the last position on board
    iterator end() const
    {
      return iterator(64);
    }
    
  private:
    // The sparse map storing the pieces, keyed off locations
    std::map<Position, Piece *> occ;
    
    // Write the board state to an output stream
    friend std::ostream &operator<<(std::ostream &os, const Board &board);
  };
}
#endif // BOARD_H
