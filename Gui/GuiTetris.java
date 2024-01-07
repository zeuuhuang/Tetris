/**
 * Zeyu Huang
 * CSE 8B PSA6
 * Fall 2018
 *
 * This file uses gui to make the Tetris game looks nicer.
 */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import javafx.scene.media.*;

/**
 * GuiTetris extends Application
 * consist of five parts: grid, next piece, stored piece, game status, 
 * and lines cleared
 */
public class GuiTetris extends Application {

  //instance value
  private static final int PADDING = 10;
  private static final int TILE_GAP = 2;
  private static final int UNIT = 25;
  private static final int PIECE = 4;
  private static final int TEXTLENGTH = 8; 

  //instance variables
  private Tetris tetris;
  private GridPane pane;
  private MyKeyHandler myKeyHandler;
  private Text gameStatus;
  private Text clearedLines = new Text(""+0);
  private Rectangle[][] GRID;
  private Rectangle[][] STOREDPIECE;
  private Rectangle[][] NEXTPIECE;
  private int length;
  private int width;
  private boolean gridFirstTime = true;
  private boolean nextFirstTime = true;
  private boolean holdFirstTime = true;
  private boolean firstTime = true;
  private boolean FirstTime = true;


  /**
   * set the color of corresponding rectangle according to grid in Tetris
   * the background color is white
   * shape O is gold
   * shape T is firebrick
   * shape L is indianred
   * shape J is marron
   * shape I is red
   * shape S is lightsalmon
   * shape Z is light coral
   */
  private void setGrid(){
    //the first time initialize GRID area
    if(gridFirstTime){
      GRID = new Rectangle[tetris.grid.length][tetris.grid[0].length];
      for(int i = 0; i<tetris.grid.length; i++)
        for(int j = 0; j<tetris.grid[0].length; j++)
          //set the default color white
          GRID[i][j] = new Rectangle(UNIT,UNIT,Color.WHITE);
      gridFirstTime = false;
    }
    //deep copy the grid in Tetris
    for(int i = 0; i<tetris.grid.length; i++)
      for(int j = 0; j<tetris.grid[0].length; j++){
        //set the default color white
        GRID[i][j].setFill(Color.WHITE);
        //ghostShape();
        //set different color according to the shapes
        if(tetris.grid[i][j] == 'O')
          GRID[i][j].setFill(Color.GOLD);
        else if(tetris.grid[i][j] == 'T')
          GRID[i][j].setFill(Color.FIREBRICK);
        else if(tetris.grid[i][j] == 'L')
          GRID[i][j].setFill(Color.INDIANRED);
        else if(tetris.grid[i][j] == 'J')
          GRID[i][j].setFill(Color.MAROON);
        else if(tetris.grid[i][j] == 'I')
          GRID[i][j].setFill(Color.RED);
        else if(tetris.grid[i][j] == 'S')
          GRID[i][j].setFill(Color.LIGHTSALMON);
        else if(tetris.grid[i][j] == 'Z')
          GRID[i][j].setFill(Color.LIGHTCORAL);
      }
  }

  /**
   * assign the color of active piece in the grid pane at corresponding 
   * position
   */
  private void setActivePiece(){
    ColorPiece colorPiece = new ColorPiece(tetris.activePiece);
    for(int i = 0; i < tetris.activePiece.tiles.length; i++)
      for(int j = 0; j < tetris.activePiece.tiles[0].length; j++)
        //insert the active piece in the grid
        if(tetris.activePiece.tiles[i][j] == 1)
          GRID[i+tetris.activePiece.rowOffset]
            [j+tetris.activePiece.colOffset].setFill(colorPiece.c);
  }

  /**
   * display the next piece on the upper right corner 
   */
  private void setNextPiece(){
    ColorPiece colorPiece = new ColorPiece(tetris.nextPiece);
    //the first time initialize the NEXTPIECE area
    if(nextFirstTime){
      NEXTPIECE = new Rectangle[PIECE][PIECE];
      for(int i = 0; i < PIECE; i++)
        for(int j = 0; j < PIECE; j++)
          NEXTPIECE[i][j] = new Rectangle(UNIT,UNIT,Color.BLACK);
      nextFirstTime = false;
    }
    for(int i = 0; i < PIECE; i++)
      for(int j = 0; j < PIECE; j++)
        //use different color to display the next piece
        if(i < tetris.nextPiece.tiles.length && 
            j < tetris.nextPiece.tiles[0].length &&
            tetris.nextPiece.tiles[i][j] == 1)
          NEXTPIECE[i][j].setFill(colorPiece.c);
        else NEXTPIECE[i][j].setFill(Color.BLACK);
  }

  /**
   * display the stored piece on the upper left corner 
   */
  private void setStoredPiece(){

    //the first time initialize the STOREDPIECE area
    if(holdFirstTime){
      STOREDPIECE = new Rectangle[PIECE][PIECE];
      for(int i = 0; i < PIECE; i++)
        for(int j = 0; j < PIECE; j++)
          STOREDPIECE[i][j] = new Rectangle(UNIT,UNIT,Color.BLACK);
      holdFirstTime = false;
    }

    //if there is no stored piece, print this part with the same color
    if(tetris.storedPiece == null)
      for(int i = 0; i < PIECE; i++)
        for(int j = 0; j < PIECE; j++)
          STOREDPIECE[i][j].setFill(Color.BLACK);
    else{
      ColorPiece colorPiece = new ColorPiece(tetris.storedPiece);
      for(int i = 0; i < PIECE; i++)
        for(int j = 0; j < PIECE; j++)
          //use different color to displace the stored piece
          if(i < tetris.storedPiece.tiles.length && 
              j < tetris.storedPiece.tiles[0].length &&
              tetris.storedPiece.tiles[i][j] == 1)
            STOREDPIECE[i][j].setFill(colorPiece.c);
          else STOREDPIECE[i][j].setFill(Color.BLACK);
    }
  }

  /**
   * set the game status
   * when the game is on, it appears "Tetris"
   * when the game is over, it appears "Game Over!"
   */
  private void setGameStatus(){
    if(firstTime){
      gameStatus = new Text("Tetris");
      gameStatus.setFont(new Font(16));
      firstTime = false;
    }
    //if the game is over, change the status to Game over.
    if(tetris.isGameover == true)
      gameStatus.setText("Game Over!");
  }

  /**
   * set the number of cleared lines according to the lines cleared in 
   * Tetris game
   */
  private void setLinesCleared(){
    clearedLines.setText(""+tetris.linesCleared);
    clearedLines.setFont(new Font(16));
  }


  /*private void ghostShape(){
    //make a backup of instance variables in Tetris
    //deep copy the grid
    char[][] backup = new char[tetris.grid.length][tetris.grid[0].length];
    for(int i = 0; i<tetris.grid.length; i++)
      for(int j = 0; j<tetris.grid[0].length; j++)
        backup[i][j] = tetris.grid[i][j];
    Piece activeCopy = new Piece(tetris.activePiece);
    Piece nextCopy = new Piece(tetris.nextPiece);
    Piece holdCopy = new Piece(tetris.storedPiece);
    int copyLinesCleared = tetris.linesCleared;
    boolean copyGameStatus = tetris.isGameover;

    tetris.drop();
    for(int i = 0; i<tetris.grid.length; i++)
      for(int j = 0; j<tetris.grid[0].length; j++){
        GRID[i][j].setFill(Color.WHITE);
        if(tetris.grid[i][j] != ' ')
          GRID[i][j].setFill(Color.MOCCASIN);
      }
    tetris.grid = backup;
    tetris.activePiece = activeCopy;
    tetris.nextPiece = nextCopy;
    tetris.storedPiece = holdCopy;
    tetris.linesCleared = copyLinesCleared;
    tetris.isGameover = copyGameStatus;
  }*/


  /**
   * this inner class creates an object which contains a Piece and a Color
   */
  private class ColorPiece{
    //instance variables
    private Color c;
    private Piece piece;

    /**
     * constructor which assign color according to the shape of pieces
     * @param p input piece
     */
    private ColorPiece(Piece p){
      piece = new Piece(p);
      if(p.shape == 'O')
        c = Color.GOLD;
      else if(p.shape == 'T')
        c = Color.FIREBRICK;
      else if(p.shape == 'L')
        c = Color.INDIANRED;
      else if(p.shape == 'J')
        c = Color.MAROON;
      else if(p.shape == 'I')
        c = Color.RED;
      else if(p.shape == 'S')
        c = Color.LIGHTSALMON;
      else
        c = Color.LIGHTCORAL;
    }
  }

  /**
   * this method starts displaying the game of tetris
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    this.tetris = new Tetris();

    // Comment out if needed
    // startMusic();

    pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    pane.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
    pane.setStyle("-fx-background-color: GREY");
    pane.setHgap(TILE_GAP); 
    pane.setVgap(TILE_GAP);


    //display the game status
    setGameStatus();
    pane.setHalignment(gameStatus, HPos.CENTER);
    pane.add(gameStatus,0,0,TEXTLENGTH,1);

    //display the lines cleared
    setLinesCleared();
    pane.setHalignment(clearedLines, HPos.CENTER);
    pane.add(clearedLines,TEXTLENGTH,0,TEXTLENGTH,1);

    //display stored piece
    setStoredPiece();
    for(int i = 0; i < STOREDPIECE.length; i++)
      for(int j = 0; j < STOREDPIECE[0].length; j++)
        pane.add(STOREDPIECE[i][j],j,i+1);

    //display next piece
    setNextPiece();
    for(int i = 0; i < NEXTPIECE.length; i++)
      for(int j = 0; j < NEXTPIECE[0].length; j++)
        pane.add(NEXTPIECE[i][j],tetris.grid[0].length-PIECE+j,i+1);


    //display the grid and active piece
    setGrid();
    setActivePiece();
    for(int i = 0; i<GRID.length; i++)
      for(int j = 0; j<GRID[0].length; j++)
        pane.add(GRID[i][j],j,PIECE+1+i);

    Scene scene = new Scene(pane);
    primaryStage.setTitle("Tetris");
    primaryStage.setScene(scene);
    primaryStage.show();

    myKeyHandler = new MyKeyHandler();
    scene.setOnKeyPressed(myKeyHandler);
    MoveDownWorker worker = new MoveDownWorker();
    worker.start();

  }

  /**
   * this inner class handle the key pressed by the player
   */
  private class MyKeyHandler implements EventHandler<KeyEvent> {
    /**
     * make corresponding operation according to the key pressed by players
     * @param e the key pressed by player
     */
    @Override
    public void handle(KeyEvent e) {
      //if the game is over, any key is invalid
      if(tetris.isGameover)
        return;
      switch(e.getCode()){
        //drop method is called and refresh the part of next piece
        case SPACE: 
          tetris.drop();
          setNextPiece();
          setLinesCleared();
          if(tetris.isGameover)
              setGameStatus();
          break;
          //hold method is called and refresh the part of stored piece
        case Z: 
          if(tetris.storedPiece == null){
            setNextPiece();
            tetris.hold(); 
            setStoredPiece();
          }
          else{
            tetris.hold(); 
            setStoredPiece();
          }
          break;
          //move down, if consolidates, refresh the part of next piece
        case DOWN: 
          if(!tetris.move(Direction.DOWN)){
            setNextPiece();
            setLinesCleared();
            if(tetris.isGameover)
              setGameStatus();
          }
          break;
          //move left
        case LEFT: 
          tetris.move(Direction.LEFT); 
          break;
          //move right
        case RIGHT: 
          tetris.move(Direction.RIGHT); 
          break; 
          //rotate
        case UP: 
          tetris.rotate(); 
          break; 
        case O:
          try{
            tetris.outputToFile();
          }
          catch(IOException i){
            System.err.println("SOS please someone help me");
          }
          break;
      }
      //update the grid and active piece
      setGrid();
      setActivePiece();
    }
  }


  /* ---------------- DO NOT EDIT BELOW THIS LINE ---------------- */

  /**
   * private class GuiTetris.MoveDownWorker
   * a thread that simulates a downwards keypress every some interval
   * @author Junshen (Kevin) Chen
   */
  private class MoveDownWorker extends Thread{

    private static final int DROP_INTERVAL = 500; // millisecond
    private int move_down_timer = DROP_INTERVAL; 

    /**
     * method run
     * called when the thread begins, decrements the timer every iteration
     * of a loop, reset the timer and sends a keydown when timer hits 0
     */
    @Override
    public void run(){

      // run forever until returned
      while (true) {
        // stop the thread if the game is over
        if (tetris.isGameover) return; 

        // wait 1ms per iteration
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          break;
        }

        move_down_timer -= 1;
        if (move_down_timer <= 0 ) {

          // simulate one keydown by calling the 
          // handler.handle()
          myKeyHandler.handle(
              new KeyEvent(null, "", "", KeyCode.DOWN, 
                false, false, false, false)
              );

          move_down_timer = DROP_INTERVAL;
        }
      }
    }
  } // end of private class MoveDownWorker

  /**
   * Cheng Shen Nov. 11th 2018
   * This method plays the background music
   */
  private void startMusic(){
    try{
      System.out.println("Playing Music~~~");
      File mp3 = new File("./Tetris.mp3");
      Media bgm = new Media(mp3.toURI().toString());
      MediaPlayer bgmPlayer = new MediaPlayer(bgm);
      bgmPlayer.setCycleCount(bgmPlayer.INDEFINITE);
      bgmPlayer.play();
    }catch (Exception e){
      System.out.println("Exception playing music");
    }
  }

}
