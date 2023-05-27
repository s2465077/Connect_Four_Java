package game;

import interfaces.IModel;
import interfaces.IPlayer;
import util.GameSettings;
import java.io.*;

/**
 * This class represents the state of the game.
 */
public class Model implements IModel
{
	// A reference to the game settings from which you can retrieve the number
	// of rows and columns the board has and how long the win streak is.
	private GameSettings settings;
	private byte[][] gameBoard;
	private boolean player1_turn;
	private boolean concede;
	private int Rows;
	private int Cols;
	private int minStreakLength;



	// The default constructor.
	public Model() {

	}


	// Called when a new game is started on an empty board.
	public void initNewGame(GameSettings settings) {
		this.settings = settings;
		this.Rows = settings.nrRows;
		this.Cols = settings.nrCols;
		this.minStreakLength = settings.minStreakLength;
		this.gameBoard = new byte[Rows][Cols];
		byte[][] Board = this.gameBoard;
		this.player1_turn = true;


	}

	// Called when a game state should be loaded from the given file.
	public void initSavedGame(String fileName) {
		try {

			File resourceFile = new File("./saves/" + fileName);


			BufferedReader loadGameBufferedReader = new BufferedReader(new FileReader(resourceFile));

			this.Rows = Integer.parseInt(loadGameBufferedReader.readLine());
			this.Cols = Integer.parseInt(loadGameBufferedReader.readLine());
			this.minStreakLength = Integer.parseInt(loadGameBufferedReader.readLine());
			this.settings = new GameSettings(this.Rows, this.Cols, this.minStreakLength);
			this.gameBoard = new byte[Rows][Cols];

			int player1Turn = loadGameBufferedReader.read();
			if(player1Turn == 1) {
				this.player1_turn = true;
			}else {
				this.player1_turn = false;
			}
			System.out.println("player1 turn" + this.player1_turn);
			String line = loadGameBufferedReader.readLine();

			for (int r = 0; r < Rows; r++) {
				String tokenNumber = loadGameBufferedReader.readLine();
				//Break the string into array of single character
				char[] token = tokenNumber.toCharArray();
				System.out.println(token);
				for (int c = 0; c < Cols; c++) {
					char value = token[c];
					this.gameBoard[r][c] = (byte) Character.getNumericValue(value);

				}
			}
			loadGameBufferedReader.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	// Returns whether or not the passed in move is valid at this time.
	public boolean isMoveValid(int move) {
		int nrCols = this.Cols;
		// Assuming all moves are valid.
		if (move > 0 && move <= nrCols && gameBoard[0][move-1] == 0 || move == -1)
			return true;
		return false;

	}

	// Actions the given move if it is valid. Otherwise, does nothing.
	public void makeMove(int move) {

		int nrRows = this.Rows;
		int nrCols = this.Cols;

		byte symbol = 0;
		if (getActivePlayer() == 1) {
			symbol = 1;
			//Rotate to another player if the move is valid
			if (isMoveValid(move))
				player1_turn = false;
		} else if (getActivePlayer() == 2) {
			symbol = 2;
			if (isMoveValid(move))
				player1_turn = true;
		}

		int row = nrRows - 1;
		int selectedCol = move - 1;
		if (move == -1) {
			this.concede = true;
		} else if (move > 0 && move <= nrCols) {
			while (row > -1) {
				if (gameBoard[row][selectedCol] == 0) {
					gameBoard[row][selectedCol] = symbol;
					break;
				}
				row--;
			}
		}

	}

	private boolean checkBoardIsFull() {

		int empty = 0;
		int nrRows = this.Rows;
		int nrCols = this.Cols;

		for (int r = 0; r < nrRows; r++) {
			for (int c = 0; c < nrCols; c++) {
				if (gameBoard[r][c] == 0) {
					empty += 1;
				}
			}
		}
		if (empty > 0)
			return false;
		return true;

	}


	// Returns one of the following codes to indicate the game's current status.
	// IModel.java in the "interfaces" package defines constants you can use for this.
	// 0 = Game in progress
	// 1 = Player 1 has won
	// 2 = Player 2 has won
	// 3 = Tie (board is full and there is no winner)
	public byte getGameStatus() {
		boolean playerConcede = this.concede;
		boolean player1 = this.player1_turn;
		int steak = this.minStreakLength;
		boolean player1Win = PlayerWin(1, steak);
		boolean player2Win = PlayerWin(2, steak);

		if (playerConcede && player1 || player1Win) {
			return 1;
		} else if (playerConcede && !player1 || player2Win) {
			return 2;
		} else if (checkBoardIsFull()) {
			return 3;
		} else {
			return 0;

		}


	}


	// Returns the number of the player whose turn it is.
	public byte getActivePlayer() {

		if (player1_turn)
			return 1;
		return 2;

	}


	// Returns the owner of the piece in the given row and column on the board.
	// Return 1 or 2 for players 1 and 2 respectively or 0 for empty cells.
	public byte getPieceIn(int row, int column) {

		byte piece;

		if (gameBoard[row][column] == 1) {
			piece = 1;
		} else if (gameBoard[row][column] == 2) {
			piece = 2;
		} else {
			piece = 0;
		}
		return piece;
	}

	// Returns a reference to the game settings, from which you can retrieve the
	// number of rows and columns the board has and how long the win streak is.
	public GameSettings getGameSettings() {
		return settings;
	}

	// Detect the winning player
	public boolean PlayerWin(int activePlayer, int steak) {

		int nrRows = settings.nrRows;
		int nrCols = settings.nrCols;
		int length = steak - 1;

		//Check horizontal steak
		for (int i = 0; i < nrRows; i++)
			for (int j = 0; j < nrCols - length; j++)
				for (int k = j; k < j + steak && gameBoard[i][k] == activePlayer; k++)
					if (k == j + length)
						return true;

		//Check vertical steak
		for (int i = 0; i < nrRows - length; i++)
			for (int j = 0; j < nrCols; j++)
				for (int k = i; k < i + steak && gameBoard[k][j] == activePlayer; k++)
					if (k == i + length)
						return true;

		//Check diagonal down right steak
		for (int i = 0; i < nrRows - length; i++)
			for (int j = 0; j < nrCols - length; j++)
				for (int k = 0; k < steak && gameBoard[i + k][j + k] == activePlayer; k++)
					if (k == length)
						return true;

		//Check diagonal down steak
		for (int i = 0; i < nrRows - length; i++)
			for (int j = length; j < nrCols; j++)
				for (int k = 0; k < steak && gameBoard[i + k][j - k] == activePlayer; k++)
					if (k == length)
						return true;

		return false;
	}
}

