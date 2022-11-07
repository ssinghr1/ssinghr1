#include "Pawn.h"
#include "Exceptions.h"
#include <iostream>

namespace Chess
{
  bool Pawn::legal_move_shape(const Position &start, const Position &end) const
  {

    //get displacements of start-end, both vertically & horizontally
    int hori = end.first - start.first;
    int vert = end.second - start.second;

    
    //white pawn
    if (is_white())
    {
      //first move of white pawn (when starting position is at row 2)
      if (start.second == '2')
      {
        //can either move vertically 1 or 2 squares
        if (hori == 0 && (vert == 1 || vert == 2))
        {
          return true;
        }
      }
      //not first move (not at starting position)
      else
      {
        //only legal move is moving up one square vertically
	if (hori == 0 && vert == 1)
        {
          return true;
        }
      }
    }

    //black pawn
    else
    {
      //first move of black pawn (when starting position is at row 7)
      if (start.second == '7')
      {
        //can either move vertically -1 or -2 squares (move down vertically)
        if (hori == 0 && (vert == -1 || vert == -2))
        {
          return true;
        }
      }
      //not first move (not at starting position)
      else
      {
        //only legal move is moving down one square vertically
        if (hori == 0 && vert == -1)
        {
          return true;
        }
      }
    }

    return false;
  }

  bool Pawn::legal_capture_shape(const Position &start, const Position &end) const
  {
    
    //get displacements of start-end, both vertically & horizontally
    int hori = end.first - start.first;
    int vert = end.second - start.second;

    //white pawn
    if (is_white())
    {
      //only legal capture moves are diagonal (one block up and one block left or right)
      if (vert == 1 && abs(hori) == 1)
      {
        return true;
      }
    }

    //black pawn
    else
    {
      //only legal capture moves are diagonal (one block down and one block left or right)
      if (vert == -1 && abs(hori) == 1)
      {
        return true;
      }
    }

    return false;
  }
}
