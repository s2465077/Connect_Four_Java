# Connect Four Java Program
---
A Connect 4 game developed in Java using vanilla Java. \
Was developed as an Intellij project.

## Running the program

To play the game, follow these steps:

1. Open *ConnectFour.java* in your preferred IDE.
2. Press the IDE's run button to execute the program.
3. A welcome message and a menu will be displayed.

    Welcome to Connect Four!

    -------- MENU -------- \
    (1) Start new game \
    (2) Resume saved game \
    (3) Change game settings \
    (4) Change players\
    Select an option and confirm with enter or use any other key to quit:

4. The board will be displayed.

	-------- BOARD --------\
   	The board has 6 rows and 7 columns.\
    _ _ _ _ _ _ _\
    _ _ _ _ _ _ _  \
    _ _ _ _ _ _ _  \
    _ _ _ _ _ _ _  \
    _ _ _ _ _ _ _  \
    _ _ _ _ _ _ _ .

5. Player 1 and Player 2 can take turns to select the move.

    -------- BOARD -------- \
    The board has 6 rows and 7 columns. \
    _ _ _ _ O _ _ \
    _ _ _ _ O _ O  
    _ O _ _ X _ O  
    X O _ _ O _ X  
    O X _ _ O _ X  
    X X _ X O _ X 

6. Winning message will be shown.
    e.g. Game status: Player 1 has won!

 ## Variable game settings

The board setting can be changed.
* no. of rows
* no. of columns
* the required streak of length for winning

## Load saved game status from files
Users can load a game state from a text file and resume play. \
After selecting ‘(2) Resume saved game‘ at the menu, the below message will be shown and the saved file name must be entered.  

-------- LOAD GAME -------- \
File name (e.g. Save.txt):

All saved game files should be stored in the "saves" folder within the project hierarchy. \
The file format should start with four lines (similar to below format).\
The board state is shown by subsequent lines, with '0' = empty cells & '1' or '2' = corresponding players.

Example:\
    3 (no. of rows) \
    6 (no. of columns) \
    5 (streak length) \
    2 (no. of player) \
    120100 (board state) \
    112200 \
    221210

## Select the computer players

Users can choose to play with computer players by selecting ‘(4) Change players’ at the menu. \
Below are the players provided.

1. RoundRobinPlayer
* Basic AI player
* Selects the leftmost column that is not full as its first move.
* Subsequent moves increase the column index by 1 until reaching the last column, then returning to column 0.
* Skips over full columns to find the next valid move.
* Compatible with variable game settings.
2. WinDetectingPlayer
* AI player that makes winning moves if available on the current turn.
* Selects any valid move that does not allow the opponent to win on the next turn.
* Concedes if no such move is available.
* Compatible with variable game settings.


