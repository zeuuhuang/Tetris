/** This file is used for the piece of the game Tetris. The piece can be
 * created, rotated and moved.
 * My name is Zeyu Huang, and my email is zehuang@ucsd.edu
 */

import java.util.*;

/** This class contains methods enable a piece to be constructed and 
 * operated. 
 */
public class Piece {

  /*
     public static void main(String[] args){
  //case 1
  Piece test1 = new Piece();
  System.out.println("tiles 1:");
  for(int i = 0; i < test1.tiles.length; i++){      
  for(int j = 0; j< test1.tiles[i].length; j++){
  System.out.print(test1.tiles[i][j]+" ");
  }
  System.out.println();
  }
  System.out.println(test1.shape+"("+test1.rowOffset+","+test1.colOffset+")");
  System.out.println();

  test1.rotate();
  System.out.println("tiles 1 rotate:");
  for(int i = 0; i < test1.tiles.length; i++){
  for(int j = 0; j< test1.tiles[i].length; j++){
  System.out.print(test1.tiles[i][j]+" ");
  }
  System.out.println();
  }
  System.out.println();


  //case 2
  Piece test2 = new Piece(test1);
  for(int i = 0; i < test2.tiles.length; i++){
  for(int j = 0; j< test2.tiles[i].length; j++){
  System.out.print(test2.tiles[i][j]+" ");
  }
  System.out.println();
  }
  System.out.println("if they are the same object:");
  System.out.println(test1 == test2);
  System.out.println();

  test2.rotate();
  System.out.println("tiles 2 rotate:");
  for(int i = 0; i < test2.tiles.length; i++){
  for(int j = 0; j< test2.tiles[i].length; j++){
  System.out.print(test2.tiles[i][j]+" ");
  }
  System.out.println();
  }
  System.out.println();


  test1.movePiece(Direction.LEFT);
  System.out.println("move left:");
  System.out.println("("+test1.rowOffset+","+test1.colOffset+")");
  System.out.println();

  test1.movePiece(Direction.RIGHT);
  System.out.println("move right:");
  System.out.println("("+test1.rowOffset+","+test1.colOffset+")");
  System.out.println();

  test1.movePiece(Direction.DOWN);
  System.out.println("move down:");
  System.out.println("("+test1.rowOffset+","+test1.colOffset+")");
  System.out.println();


  //case 3
  Piece test3 = new Piece('O');
  for(int i = 0; i < test3.tiles.length; i++){
  for(int j = 0; j< test3.tiles[i].length; j++){
  System.out.print(test3.tiles[i][j]+" ");
  }
  System.out.println();
  }
System.out.println("if they are the same object:");
System.out.println(test3.tiles == initialTiles[0]);
System.out.println();


//case 4
Piece test4 = new Piece('I');
for(int i = 0; i < test4.tiles.length; i++){
  for(int j = 0; j< test4.tiles[i].length; j++){
    System.out.print(test4.tiles[i][j]+" ");
  }
  System.out.println();
}
System.out.println("if they are the same object:");
System.out.println(test4.tiles == initialTiles[1]);
System.out.println();
     }
*/

// all possible char representation of a piece
public static final char[] possibleShapes = 
{'O', 'I', 'S', 'Z', 'J', 'L', 'T'}; 

// initial state of all possible shapes, notice that this array's 
// content shares index with the possibleShapes array 
public static final int[][][] initialTiles = {
  {{1,1},
    {1,1}}, // O

  {{0,0,0,0},
    {1,1,1,1},
    {0,0,0,0},
    {0,0,0,0}}, // I

  {{0,0,0},
    {0,1,1},
    {1,1,0}}, // S

  {{0,0,0},
    {1,1,0},
    {0,1,1}}, // Z

  {{0,0,0},
    {1,1,1},
    {0,0,1}}, // J

  {{0,0,0},
    {1,1,1},
    {1,0,0}}, // L

  {{0,0,0},
    {1,1,1},
    {0,1,0}} // T
};  

// random object used to generate a random shape
public static Random random = new Random(); 

// char representation of shape of the piece, I, J, L, O, S, Z, T
public char shape;

// the position of the upper-left corner of the tiles array 
// relative to the Tetris grid
public int rowOffset;
public int colOffset;

// used to determine 2-state-rotations for shapes S, Z, I
// set to true to indicate the next call to rotate() should
// rotate clockwise
public boolean rotateClockwiseNext = false;

// an array marking where the visible tiles are
// a 1 indicates there is a visible tile in that position
// a 0 indicates there is no visible tile in that position
public int[][] tiles;


/**This method creates a random piece.
*/
public Piece(){
  //randomly choose a shape from possibleShapes and initialize the shape
  int index = random.nextInt(possibleShapes.length);
  shape = possibleShapes[index];

  //create a new 2D array amd deep-copy the corresponding initial tiles
  int[][] thisTiles = new int[initialTiles[index].length][];
  for(int i = 0; i < initialTiles[index].length; i++){
    thisTiles[i] = new int[initialTiles[index][i].length];
    for(int j = 0; j < thisTiles[i].length; j++){
      thisTiles[i][j] = initialTiles[index][i][j];
    }
  }
  tiles = thisTiles;

  //initialize position
  //for piece O, (0,4); for others, (-1,3)
  if(shape == 'O'){
    rowOffset = 0;
    colOffset = 4;
  }
  else{
    rowOffset = -1;
    colOffset = 3;
  }
}

/**This method creates a piece in particular shape
 * @param shape the shape of the piece
*/
public Piece(char shape) {
  boolean exist = false;
  int index = 0;

  //find if the input shape exists.
  //If it does, record its index.
  for(int i = 0; i < possibleShapes.length; i++){
    if(shape == possibleShapes[i]){
      index = i;
      exist = true;
    }
  }

  if(exist){
    //initialize the shape
    shape = shape;

    //create a new 2D array amd deep-copy the corresponding initial tiles
    int[][] thisTiles = new int[initialTiles[index].length][];
    for(int i = 0; i < initialTiles[index].length; i++){
      thisTiles[i] = new int[initialTiles[index][i].length];
      for(int j = 0; j < thisTiles[i].length; j++){
        thisTiles[i][j] = initialTiles[index][i][j];
      }
    }
    tiles = thisTiles;

    //initialize position of piece
    //for piece O, (0,4); for others, (-1,3)
    if(shape == 'O'){
      rowOffset = 0;
      colOffset = 4;
    }
    else{
      rowOffset = -1;
      colOffset = 3;
    }
  }
}


/**This method deep copies a piece.
 * @param other the piece this constructor is copying
*/
public Piece(Piece other){
  //copy the shape
  shape = other.shape;

  //copy the tiles
  int[][] thisTiles = new int[other.tiles.length][other.tiles[0].length];
  for(int i = 0; i < other.tiles.length; i++)
    for(int j = 0; j < thisTiles[i].length; j++)
      thisTiles[i][j] = other.tiles[i][j];
  tiles = thisTiles;

  //copy the position
  rowOffset = other.rowOffset;
  colOffset = other.colOffset;
  rotateClockwiseNext = other.rotateClockwiseNext;
}


/**This method is designed for the rotation of pieces.
*/
public void rotate(){
  //the shape O, T, L, J always rotate clockwise.
  if(shape == 'O' || shape == 'T' || shape == 'L' || shape == 'J'){
    rotateClockwise();
  }
  //the shape I, S, Z first rotate clockwise, and then rotate counterclockwise
  if(shape == 'I' || shape == 'S' || shape == 'Z'){
    if(rotateClockwiseNext){
      rotateClockwise();
      rotateClockwiseNext = false;
    }
    else{          
      rotateCounterClockwise();
      rotateClockwiseNext = true;
    }
  }
}

/**This method makes a piece rotates clockwise.
*/
public void rotateClockwise() {
  //make the first column become the first row, 
  //make the second column become the second row, 
  //and so on
  int[][] result = new int[tiles.length][];
  for(int i = 0; i < result.length; i++){
    result[i] = new int[tiles[i].length];
    for(int j = 0; j < result[i].length; j++){
      result[i][j] = tiles[tiles.length-1-j][i];
    }
  }
  tiles = result;
}

/**This method makes a piece rotates counterclockwise
*/
public void rotateCounterClockwise () {
  //make the first row become the first column,
  //the second row become the second column,
  //and so on
  int[][] result = new int[tiles.length][];
  for(int i = 0; i < result.length; i++){
    result[i] = new int[tiles[i].length];
  }
  for(int i = 0; i < result.length; i++){
    for(int j = 0; j < result[i].length; j++){
      result[result.length-1-j][i] = tiles[i][j];
    }
  }
  tiles = result;
}  

/**This method moves pieces.
 * @param direction the direction the piece is moving to
*/
public void movePiece(Direction direction) {
  switch(direction){
    //move left
    case LEFT:
      colOffset--;
      break;

      //move right
    case RIGHT:
      colOffset++;
      break;

      //move down
    case DOWN:
      rowOffset++;
      break;
  }
}


}
