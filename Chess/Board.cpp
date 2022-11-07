#include <iostream>
#include <utility>
#include <map>
#ifndef _WIN32
#include "Terminal.h"
#endif // !_WIN32
#include "Board.h"
#include "CreatePiece.h"
#include "Exceptions.h"

namespace Chess
{
  Board::Board(){}

  //copy constructor 
  Board::Board(const Board& origBoard) {
    *this = origBoard;
  }

  Board& Board::operator=(const Board &origBoard) {

    //loop through all of origBoard
    for (Board::iterator it = origBoard.begin(); it!= origBoard.end(); it++){
      //only do copying process if there is a piece at the point
      //get the position at the current point in the loop

      Position& position = *it;
      if (origBoard(position)){
        //char for the designator that matches that of it piece
        char designator = origBoard(position)->to_ascii();
        //add piece to the board at this same position with the same designator
        remove_piece(position);
        add_piece(position, designator);
      }
    }
    
    return *this;
  }

  //board destructor
  Board::~Board(){
    //allocated memory is held in the pieces, need to delete each individal piece
    for (std::map<Position, Piece *>::iterator occ_it = occ.begin(); occ_it!= occ.end(); occ_it++){
      delete occ_it->second;//occ_it is the piece
    }
  }

  // Returns a const pointer to the piece at a prescribed location if it exists,
  // or nullptr if there is nothing there.                                        
   const Piece* Board::operator()(const Position& position) const {
     Piece* piece = nullptr;
     if (occ.find(position) != occ.end()){
       piece = occ.at(position);
     }
     
    return piece;
  }

  
  void Board::add_piece(const Position& position, const char& piece_designator) {
    //invalid designator exception
    bool desigValid = piece_designator;
    if(!desigValid) throw Exception("invalid designator");
    //invalid position exception
    bool isRowValid = position.first>='A' && position.first<='H';
    bool isColValid = position.first>='1' && position.second<='8';

    if (!isRowValid || !isColValid) throw Exception("invalid position");

    //occupied position
    if (occ[position]){ 
      throw Exception("position is occupied");}

    occ[position] = create_piece(piece_designator);
    
  }

  void Board::remove_piece(const Position& position){
    delete occ[position];
    occ.erase(position);
  }
  
  void Board::display() const {
    
    //use terminal.h to set bg and fg colors when using print functions.
    
    //print all col numbers at top of board
    std::cout<<"  ";//offset from the left of row numbers

    for (char c = 'A'; c<='H'; c++) {//all the col names
	std::cout<<c<<" ";
    }
    std::cout<<std::endl;//go onto next line for board
    
    //loop through and print each spot on the board
    bool color = false;//set a bool to indicate square color
    for (char r = '8'; r >='1'; r--) {
      std::cout<< r << " ";//print row number on left of each row   
      for (char c = 'A'; c<='H'; c++) {
	const Position newPair = Position(c, r);
	Piece* piece = nullptr;
	if (occ.find(newPair) != occ.end()) {
	  piece = occ.at(newPair);
	}
	
	
	//true=red square. False = blue square
	if (color) {
	  Terminal::color_bg(Terminal::BLACK);
	} else {
	  Terminal::color_bg(Terminal::WHITE);
	}
       
        color = c != 'H' ? !color : color;//after setting background piece color, make opposite for next loop
	  
        if (piece != nullptr) {
	  if (piece->is_white()) {
	    Terminal::color_fg(true, Terminal::RED);
	    std::cout<<piece->to_ascii();
	  } else {
	    Terminal::color_fg(true, Terminal::BLUE);// if the piece is white, make it white. if not, bleck
            std::cout<<piece->to_ascii();
          }
        } else {
          Terminal::color_fg(true, Terminal::YELLOW);
          std::cout<<"-";
        }
	std::cout<<" ";
      }
      
      Terminal::set_default(); //Reset colors before newline
      std::cout<<" "<<r<<" "<<std::endl;
    }

    //print cols out again for readability
    std::cout<<"  ";//offset from the left of row numbers
    for (char c = 'A'; c<='H'; c++) {//all the col names                                                                                
      std::cout<<c<<" ";
    }
    std::cout<<std::endl;
    Terminal::set_default();//set colors back to default
    
  }
  
  bool Board::has_valid_kings() const {
    int white_king_count = 0;
    int black_king_count = 0;
    for (std::map<std::pair<char, char>, Piece*>::const_iterator it = occ.begin();
	 it != occ.end();
	 it++) {
      if (it->second) {
	switch (it->second->to_ascii()) {
	case 'K':
	  white_king_count++;
	  break;
	case 'k':
	  black_king_count++;
	  break;
	}
      }
    }
    return (white_king_count == 1) && (black_king_count == 1);
  }  

  std::ostream& operator<<(std::ostream& os, const Board& board) {
    for(char r = '8'; r >= '1'; r--) {
      for(char c = 'A'; c <= 'H'; c++) {
	const Piece* piece = board(Position(c, r));
	if (piece) {
	  os << piece->to_ascii();
	} else {
	  os << '-';
	}
      }
      os << std::endl;
    }
    return os;
  }
}
