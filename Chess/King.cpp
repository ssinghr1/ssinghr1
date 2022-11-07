#include "King.h"
#include "Exceptions.h"

namespace Chess
{
  bool King::legal_move_shape(const Position& start, const Position& end) const {


    //how many squares are we moving?
    int absHori = abs(start.first - end.first);
    int absVert = abs(start.second - end.second);

    //if the object is not moving at all, big exception
    if (absHori == 0 && absVert == 0) {
      //TODO: exception handling here
      throw Exception("cannot capture own piece");
    }
    
    //king only moves one place, so these are the only cases that work
    if (absHori == 1 && absVert == 0) return true; //movement left or right
    if (absVert == 1 && absHori == 0) return true; //movement up or down
    if (absVert == 1 && absHori == 1) return true; //movement diagonally

    //invalid move when moving more than 1 place
    return false;
    
  }
}
