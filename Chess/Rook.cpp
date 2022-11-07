#include "Rook.h"
#include "Exceptions.h"

namespace Chess
{
  bool Rook::legal_move_shape(const Position& start, const Position& end) const {

    //how many places to move?
    int absHori = abs(start.first - end.first);
    int absVert = abs(start.second - end.second);

    //throw exception if move causes no movement
    if (absHori == 0 && absVert == 0) {
      //TODO: Exception handling 
      throw Exception("cannot capture own piece");
    }

    //for ROOK, movment is either horizontal or vertical
    if (absHori > 0 && absVert > 0) return false; //if both are changing, this is a diagonal move
    //if one changes and other is constant, then movement is fine
    if (absHori > 0 && absVert == 0) return true;
    if (absVert > 0 && absHori == 0) return true;
    
    return false;
  }
}
