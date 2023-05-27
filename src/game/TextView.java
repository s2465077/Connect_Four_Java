package game;

import interfaces.*;
import players.*;
import util.GameSettings;
import util.InputUtil;

/**
 * This class is used to interact with the user.
 */
public class TextView implements IView
{
	public void displayWelcomeMessage()
	{
		System.out.println("Welcome to Connect Four!");
	}
	
	public void displayChosenMove(int move)
	{
		System.out.println("Selected move: " + move);
	}
	
	public void displayMoveRejectedMessage(int move)
	{
		System.out.println("The move (" + move + ") was rejected, please try again.");
	}
	
	public void displayActivePlayer(byte playerID)
	{
		System.out.println("\nPlayer " + playerID + " is next!");
	}
	
	public void displayGameStatus(byte gameStatus)
	{
		System.out.print("\nGame status: ");
		
		switch(gameStatus)
		{
			case IModel.GAME_STATUS_ONGOING: System.out.println("The game is in progress."); break;
			case IModel.GAME_STATUS_WIN_1: System.out.println("Player 1 has won!"); break;
			case IModel.GAME_STATUS_WIN_2: System.out.println("Player 2 has won!"); break;
			case IModel.GAME_STATUS_TIE: System.out.println("The game has ended in a tie!"); break;
			default : System.out.println("Error: Invalid/unknown game status"); break;
		}
	}
	
	public void displayBoard(IModel model)
	{
		System.out.println("\n-------- BOARD --------");

		int nrRows = model.getGameSettings().nrRows;
		int nrCols = model.getGameSettings().nrCols;

		System.out.println("The board has " + nrRows + " rows and " + nrCols + " columns.");


		char[][] currentBoard = new char[nrRows][nrCols];

		for (int r = 0; r < nrRows; r++){
			for (int c = 0; c < nrCols; c++) {
				if (model.getPieceIn(r, c) == 0) {
					System.out.print("_" + ' ');
				} else {
					if(model.getPieceIn(r, c) == 1) {
						currentBoard[r][c] = 'X';
					} else if (model.getPieceIn(r, c) == 2) {
						currentBoard[r][c] = 'O';
					}
					System.out.print(currentBoard[r][c]);
					System.out.print(' ');
				}
			}
			System.out.println(' ');
		}
		// Here is an example of how the output should look:
		//_ _ O O _ _ X
		//_ _ X O _ _ X
		//_ O X X _ _ O
		//_ X X O _ X O
		//X O O X O O O
		//X O X X X O X
	}
	
	public char requestMenuSelection()
	{
		// Display menu options.
		System.out.println("\n-------- MENU --------");
		System.out.println("(1) Start new game");
		System.out.println("(2) Resume saved game");
		System.out.println("(3) Change game settings");
		System.out.println("(4) Change players");
		
		// Request and return user input.
		System.out.print("Select an option and confirm with enter or use any other key to quit: ");
		return InputUtil.readCharFromUser();
	}
	
	public String requestSaveFileName()
	{
		System.out.println("\n-------- LOAD GAME --------");
		System.out.print("File name (e.g. Save.txt): ");
		return InputUtil.readStringFromUser();
	}
	
	public GameSettings requestGameSettings()
	{
		System.out.println("\n-------- GAME SETTINGS --------");

		System.out.println("Please enter a board height, a board width, and the required streak length, respectively.");
		int nrRows = 0;
		do{
			System.out.println("First, please select a board height (min:3 & max:10).");
			System.out.println("Confirm with enter or use any other key to quit.");
			nrRows = InputUtil.readIntFromUser();
		}while(nrRows < IModel.MIN_ROWS || nrRows > IModel.MAX_ROWS);

		int nrCols = 0;
		do {
			System.out.println("Second, please select a board width (min:3 & max:10).");
			System.out.println("Confirm with enter or use any other key to quit.");
			nrCols = InputUtil.readIntFromUser();
		} while(nrCols < IModel.MIN_COLS || nrCols > IModel.MAX_COLS);

		System.out.println("Third, please select the required streak length. Confirm with enter or use any other key to quit.");
		int streak = InputUtil.readIntFromUser();

		// Wrap the selected settings in a GameSettings instance and return.
		return new GameSettings(nrRows, nrCols, streak);
	}
	
	public IPlayer requestPlayerSelection(byte playerId)
	{
		System.out.println("\n-------- CHOOSE PLAYER " + playerId + " --------");
		System.out.println("(1) HumanPlayer");
		System.out.println("(2) RoundRobinPlayer");
		System.out.println("(3) WinDetectingPlayer");
		
		// Request user input.
		System.out.print("Select an option and confirm with enter (invalid input will select a HumanPlayer): ");
		char selectedPlayer = InputUtil.readCharFromUser();
		
		// Instantiate the selected player class.
		switch(selectedPlayer)
		{
			case '2': return new RoundRobinPlayer();
			case '3': return new WinDetectingPlayer();
			default: return new HumanPlayer();
		}
	}
}
