#include "Knight.h"
#include "Exceptions.h"

namespace Chess
{
  bool Knight::legal_move_shape(const Position& start, const Position& end) const {

    //get displacements of end-start, both vertical and horizontal
    int absHori = abs(end.first - start.first);
    int absVert = abs(end.second - start.second);

    //case where pieces are the same position
    if (absHori == 0 && absVert == 0) {
      throw Exception("cannot capture own pience");
    }

    //distance-traveled-squared
    int dist_squared = absHori*absHori + absVert*absVert;

    //legal moves: move in L shape
    if (dist_squared == 5) {
      return true;
    }
    
    return false;
  }
}
