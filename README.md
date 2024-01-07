#1 Program Description
The basic command of this game:
 a-move left
 d-move right
 s-move down
  *each move moves by one unit grid*

 w-rotate
  *there may several status of the same shape.*

 space-drop
   *move and consolidate a piece to the bottom*

 z-store piece
   *at first, there is no stored piece, when you first store, the temporary
   active piece would be the store piece. You may only use this hold for once
   until a new line is cleared.*

 o-save
   *save the temporary game status*

 q-quit
   *exit the game*


#2 How Did You Test Your Program?

First, test all the command to see if they are functioning correctly.
Second, check method written by myself one by one in the tetris class.

Use shape "I" to check the rotation output, rotation at boundaries, drop,
and hold methods.
Use shape "J" to check the rotation output.
Use shape "O" to check the rotation output.

Press space twice to see if the piece can be piled up.
Fill one line to see if it can be cleared. 
Fill two lines at the same time and see if they can be cleared.

Output the file and use the output file to test the second constructor. 
Create a new file which has one line already filled.
Create a new file which is game over when the first active piece is created.

The "I" is 4x4 tiles with enpty rows from both sides and rotates first
counterclockwise and then clockwise.
The "J" is 3x3 tiles and always rotates clockwise.
The "O" is 2x2 tiles and always rotates clockwise.
*Any other piece should be good if these pieces works well with all these
command.*

*The lines can be leared either by one or by several ones.*
We first check if the line can be successfully cleared with one row.
Then check if several rows can be cleared at the same time.

If the saved game file can be load in successfully, it proves that there 
are no problems with the outputFile method, and the second constructor can
read normal files. We then need to check if the grid is in specific 
conditions and process these conditions. The methods that changes the grid
is the clearedLines. The hint also give the case of game over file, so
there shouldn't be other edge cases.
