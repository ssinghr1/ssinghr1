//NAMES:
//Yisehak Ebrahim; yebrahi1
//SShamtej Singh Rana; ssinghr1
//Heng-Chung Kung; hkung2

#include "Queen.h"
#include "Exceptions.h"
#include <cstdlib>

namespace Chess
{
  bool Queen::legal_move_shape(const Position& start, const Position& end) const {

    int absHori = abs(start.first - end.first);
    int absVert = abs(start.second - end.second);
    
        
    //Now, check first if vert & hori diffrence between start-end is 1
    //Cases... 
    if (absHori == 1 && absVert == 0) {
      return true; //Horizontally displaced by 1
    } else if (absHori == 0 && absVert == 1) {
      return true; //Vertically displaced by 1
    } else { //All other cases where displacement is NOT one

      if (absHori == absVert) {
	return true;
      }

      //Only one can be 0 at this point, so it's valid either way
      //Case where it's either a staight vert. or staight hori. displacement
      if (absHori == 0 || absVert == 0) { 
	return true;
      }
      
    }
    
    return false;
  }
}
