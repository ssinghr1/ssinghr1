#include "Bishop.h"
#include "Exceptions.h"

namespace Chess
{
  bool Bishop::legal_move_shape(const Position& start, const Position& end) const {

    //Get displacements of start-end, both vertically & horizontally
    int absHori = abs(start.first - end.first);
    int absVert = abs(start.second - end.second);

    //Case where pieces are the same position
    if (absHori == 0 && absVert == 0) {

      throw Exception("Cannot capture own piece"); //Cannot capture it's own piece exception
    }

    //Case where pieces are diagonal, no matter the spacing between them
    if (absHori == absVert) {
      return true;
    }
   
    return false;
  }
}
