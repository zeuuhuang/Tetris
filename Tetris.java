/** This file is the main body of the game Tetris. It allows players to 
 * operate the pieces including rotation, movement, and even store pieces.
 * My name is Zeyu Huang, and my email is zehuang@ucsd.edu*/

import java.util.*;
import java.io.*;

/** This class contains methods for a functional tetris game.
*/
public class Tetris {

  public int linesCleared; // how many lines cleared so far

  public boolean isGameover;  // true if the game is over
  // false if the game is not over

  public Piece activePiece;   // holds a Piece object that can be moved
  // or rotated by the player

  public Piece nextPiece; // holds a Piece object that will become the new 
  // activePiece when the activePiece consolidates

  // The following 2 variables are used for the extra credit 
  public boolean usedHold = false;    // set to true if the player already 
  // used hold once since last piece 
  // consolidated

  public Piece storedPiece = null;    // holds the stored piece 

  public char[][] grid;   // contains all consolidated pieces, each tile  
  // represented by a char of the piece's shape
  // a position stores a space char if it is empty

  /** This constructor creates a new game.
  */
  public Tetris(){
    linesCleared = 0;
    isGameover = false;

    //initialize the 10*20 grid
    grid = new char[20][10];
    for(int row = 0; row < grid.length; row++)
      for(int col = 0; col < grid[row].length; col++)
        grid[row][col] = ' ';

    activePiece = new Piece();
    nextPiece = new Piece();
  }


  /**This constructor loads in a game from file.
   * @param filename the saved game
  */
  public Tetris (String filename) throws IOException {
    isGameover = false;
    grid = new char[20][10];

    //read a file
    Scanner file = new Scanner(new File(filename));

    //initialize varialbles with data in corresponding lines
    linesCleared = file.nextInt();
    activePiece = new Piece(file.next().charAt(0));
    nextPiece = new Piece(file.next().charAt(0));
    file.nextLine();

    int row = 0;
    while(file.hasNextLine()){
      //turn every line into a char array
      String temp = file.nextLine();
      char[] tempArr = new char[temp.length()];
      tempArr = temp.toCharArray();
      // and copy them in grid
      for(int col = 0; col < tempArr.length; col++)
        grid[row][col] = tempArr[col];
      row++;
    }

    //check if the game is over
    if(hasConflict(activePiece))
      isGameover = true;

  }


  /**This method checks if a piece has visible tile outside
   * of the grid and if the piece sits on a non-empty spot 
   * on the grid.
   * @param piece the piece that needs to be determined
   * @return If there is a conflict.
   */
  public boolean hasConflict(Piece piece) {
    int pieceRowLength = piece.tiles.length;
    int pieceColLength = piece.tiles[0].length;
    int pieceRow = piece.rowOffset;
    int pieceCol = piece.colOffset;

    //check if is outside of the grid from right
    if((pieceCol + pieceColLength) > grid[0].length)
      for(int i = 0; i < piece.tiles.length; i++)
        for(int j = 1; j <= pieceCol + pieceColLength - grid[0].length; j++)
          if(piece.tiles[i][pieceColLength - j] == 1)
            return true;
    //check if is outside of the grid from left
    if(pieceCol < 0)
      for(int i = 0; i < pieceRowLength; i++)
        for(int j = 0; j < -pieceCol; j++)
          if(piece.tiles[i][j] == 1)
            return true;
    //check if is outside of the grid from above
    if(pieceRow < 0)
      for(int i = 0; i < pieceColLength; i++)
        for(int j = 0; j < -pieceRow; j++)
          if(piece.tiles[j][i] == 1)
            return true;
    //check if is outside of the grid from bottom
    if((pieceRow + pieceRowLength) > grid.length)
      for(int i = 0; i < pieceColLength; i++)
        for(int j = 1; j <= pieceRow + pieceRowLength - grid.length; j++)
          if(piece.tiles[pieceRowLength-j][i] == 1)
            return true;

    //check if is sits on a non-empty spot
    for(int i = 0; i < pieceRowLength; i++)
      for(int j = 0; j < pieceColLength; j++)
        if(piece.tiles[i][j] == 1)
          if(grid[pieceRow+i][pieceCol+j] != ' ')
            return true;

    return false;
  }


  /**This method makes the active piece solid in the grid.
  */
  public void consolidate() {
    for(int row = 0; row < activePiece.tiles.length; row++)
      for(int col = 0; col < activePiece.tiles[row].length; col++)
        if(activePiece.tiles[row][col] == 1)
          grid[activePiece.rowOffset+row][activePiece.colOffset+col] 
            = activePiece.shape;
  }


  /**This method clears fully occupied rows and
   * record the counts.
   */
  public void clearLines() {
    //search from the first row all the way down
    for(int row = 0; row < grid.length; row++){
      boolean full = true;
      //if there is a space, the row is not full.
      for(int col = 0; col < grid[row].length; col++){
        if(grid[row][col] == ' '){
          full = false;
          break;
        }
      }

      if(full){ 
        //increase the number of cleared lines
        linesCleared++;
        //refresh the usedHold;
        usedHold = false;
        for(int i = row; i > 0; i--){
          //move all the rows above it down by one
          for(int j = 0; j < grid[i].length; j++)
            grid[i][j] = grid [i-1][j];
          //set the first row blank
          for(int j = 0; j < grid[0].length; j++)
            grid[0][j] = ' ';    
        }          
      }

    }
  }


  /**This method determines whether a piece can be moved
   * and if the game is over.
   * @param direction the direction the piece is moving to
   * @return if the piece can be moved successfully
   */
  public boolean move(Direction direction) {
    //create a copy of the active piece
    Piece copy = new Piece(activePiece);
    copy.movePiece(direction);
    if(hasConflict(copy)){
      //if the conflict happens when moves down,
      //make it consolidated.
      switch(direction){
        case DOWN:
          consolidate();
          clearLines();
          activePiece = new Piece(nextPiece);
          nextPiece = new Piece();
          if(hasConflict(activePiece))
            isGameover = true;
          break;
      }
      return false;
    }
    //if there is no conflict, move the active piece
    else {
      activePiece = copy;
      return true;
    }
  }


  /**This method makes the piece all the way down until 
   * it is consolidated.
   */
  public void drop() {
    //keep the piece moving down untile it cannot be moved anymore.
    while(move(Direction.DOWN)){
    }
  }

  /**This method rotate the active piece if the rotation
   * dosen't cause a conflict.
   */
  public void rotate() {
    //first make a copy to test if the roration would cause a conflict.
    Piece copy = new Piece(activePiece);
    copy.rotate();
    //if there is no conflict, rotate the active piece.
    if(!hasConflict(copy)){
      activePiece = copy;
    }
  }

  /**This method saves the game.
  */
  public void outputToFile() throws IOException {
    //creat an output file to save the temporary game
    PrintWriter outputFile = new PrintWriter(new File("output.out"));

    //the first line is the lines cleared
    outputFile.println(linesCleared);
    //the second line is the shape of the active piece
    outputFile.println(activePiece.shape);
    //the third line is the shape of next piece
    outputFile.print(nextPiece.shape);

    //save the grid row by row
    for(int i = 0; i < grid.length; i++){
      outputFile.println();
      for(int j = 0; j < grid[i].length; j++)
        outputFile.print(grid[i][j]);        
    }
    outputFile.close();

    System.out.println("Saved to output.txt");
  }

  /**This method creates loops for players to play tetris,
   * which stops when the game is over.
   */
  public void play () {
    while(!isGameover){
      System.out.println(toString());
      System.out.print("> ");

      //input a command key
      Scanner sc = new Scanner(System.in);
      String nextLine = sc.nextLine();
      if(nextLine != null && nextLine.length() >= 1){
        if(nextLine.toLowerCase().equals("a"))
          move(Direction.LEFT);
        if(nextLine.toLowerCase().equals("d"))
          move(Direction.RIGHT);
        if(nextLine.toLowerCase().equals("s"))
          move(Direction.DOWN);
        if(nextLine.toLowerCase().equals("w"))
          rotate();
        if(nextLine.toLowerCase().equals(" "))
          drop();
        if(nextLine.toLowerCase().equals("z")){
          if(!usedHold)
            hold();
          //before a line is cleared, player cannot use hold again
          usedHold = true;
        }
        if(nextLine.toLowerCase().equals("o")){
          try{
            outputToFile();
          }
          catch(IOException e){
            System.err.println("SOS please someone help me");
          }
        }
        if(nextLine.toLowerCase().equals("q"))
          return;
      }
    }
    System.out.println("Game over! Total lines cleared: " + linesCleared);
  }

  /**
   * returns the string representation of the Tetris game state in the 
   * following format:
   *  Lines cleared: [number]
   *  Next piece: [char]  (Stored piece: [char])
   *  char[20][10]
   * @return string representation of the Tetris game
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();

    str.append("\nLines cleared: " + this.linesCleared + '\n');

    str.append("Next piece: " + this.nextPiece.shape);
    if (this.storedPiece == null) str.append("\n");
    else str.append("  Stored piece: " + this.storedPiece.shape + '\n');

    str.append("| - - - - - - - - - - |\n");

    /*deep copy the grid*/
    char[][] temp_grid = new char[this.grid.length][this.grid[0].length];
    for (int row=0; row<this.grid.length; row++)
      for (int col=0; col<this.grid[0].length; col++)
        temp_grid[row][col] = this.grid[row][col];

    /*put the active piece in the temp grid*/
    for (int row=0; row<this.activePiece.tiles.length; row++)
      for (int col=0; col<this.activePiece.tiles[0].length; col++)
        if (activePiece.tiles[row][col] == 1)
          temp_grid[row+activePiece.rowOffset]
            [col+activePiece.colOffset] = 
            activePiece.shape;

    /*print the temp grid*/
    for (int row=0; row<temp_grid.length; row++){
      str.append('|');
      for (int col=0; col<temp_grid[0].length; col++){
        str.append(' ');
        str.append(temp_grid[row][col]);
      }
      str.append(" |\n");
    }

    str.append("| - - - - - - - - - - |\n");
    return str.toString();        
  }


  /**This method enable player to store temporary piece for future use.
   */
  public void hold() {
    activePiece = new Piece(activePiece.shape);
    if(storedPiece == null){
      storedPiece = new Piece(activePiece.shape);
      activePiece = new Piece(nextPiece);
      nextPiece = new Piece();
    }
    else{
      Piece temp = new Piece(activePiece.shape);
      activePiece = new Piece(storedPiece);
      storedPiece = new Piece(temp);        
    }
  }

  /**
   * first method called during program execution
   * @param args: an array of String when running the program from the 
   * command line, either empty, or contains a valid filename to load
   * the Tetris game from
   */
  public static void main(String[] args) {
    if (args.length != 0 && args.length != 1) {
      System.err.println("Usage: java Tetris / java Tetris <filename>");
      return;
    }
    try {
      Tetris tetris;
      if (args.length == 0) tetris = new Tetris();
      else tetris = new Tetris(args[0]);
      tetris.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
