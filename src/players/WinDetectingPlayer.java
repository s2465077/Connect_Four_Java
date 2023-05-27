package players;

import interfaces.IModel;
import interfaces.IPlayer;
import util.GameSettings;

/**
 * If the player can win on their current turn, a winning move must be selected.
 * Otherwise, choose any valid move that does not let the opponent win next turn.
 * If no such move exists, concede.
 */
public class WinDetectingPlayer implements IPlayer
{
	// A reference to the model, which gets information about
	// the state of the game.
	private IModel model;


	private GameSettings settings;
	int nrRows;
	int nrCols;
	byte playerId;

	
	// The constructor is called when the player is selected from the game menu.
	public WinDetectingPlayer()
	{
		settings = new GameSettings();
		nrRows = settings.nrRows;
		nrCols = settings.nrCols;
	}
	
	// This method is called when a new game is started or loaded.
	// Perform any setup that may be required before
	// the player is asked to make a move. The second argument
	// specify if you are playing as player 1 or player 2.
	public void prepareForGameStart(IModel model, byte playerId)
	{
		this.model = model;
		this.playerId = playerId;
	}
	
	// This method is called to ask the player to take their turn.
	// The move they choose was returned from this method.
	public int chooseMove()
	{
		int randomNumber = (int) (Math.random()*(nrCols))+1;
		int move;

		int winningMove1 = 0;
		int winningMove2 = 0;
		int middleMove = 0;

		move = randomNumber;

		//Testing if any combination at the middle
		move = middleTest();
		if(middleMove >0)
			move = middleMove;


		//Finding winning move
		winningMove1 = checkWinnerMove(1, 2);
		winningMove2 = checkWinnerMove(2, 1);
		if(winningMove1>0)
			move = winningMove1;
		else if (winningMove2 > 0) {
			move = winningMove1;
		}

		if(!model.isMoveValid(move)){
			move = 0;
			while(move <= nrCols) {
				if (!model.isMoveValid(move))
					move += 1;
				break;
			}
		}

		return move;

	}


	public int middleTest() {

		int steak = settings.minStreakLength;
		int length = steak - 1;
		int winningSteak = steak - 2;
		int randomNumber = (int) (Math.random() * (nrCols)) + 1;
		int move;

		move = randomNumber;
		for (int i = 0; i < nrCols; i++) {
			for (int j = 0; j < nrRows; j++) {
				if (model.getPieceIn(j, i) != 0) {

					//Testing vertical
					if (j + winningSteak < nrRows) {
						if(j+2 < nrRows) {
							if (model.getPieceIn(j, i) == model.getPieceIn(j + 1, i) && model.getPieceIn(j, i) == model.getPieceIn(j + 2, i)) {
								//increment by 1 for testing, if j = 0
								if (j == 0) {
									j += 1;
								}
								if (model.getPieceIn(j - 1, i) == 0) {
									move = i + 1;
								}
							}
						}
					}

					//Testing horizontal
					if (i + winningSteak < nrCols) {
						if(i+3 < nrCols) {
							if (model.getPieceIn(j, i) == model.getPieceIn(j, i + 2) && model.getPieceIn(j, i) == model.getPieceIn(j, i + 3)
									&& model.getPieceIn(j, i + 1) == 0) {
								move = i + winningSteak;
							} else if (model.getPieceIn(j, i) == model.getPieceIn(j, i + 1) && model.getPieceIn(j, i) == model.getPieceIn(j, i + 3)
									&& model.getPieceIn(j, i + 2) == 0) {
								move = i + winningSteak + 1;
							} else if(model.getPieceIn(j, i) == model.getPieceIn(j, i + 1) && model.getPieceIn(j, i) == model.getPieceIn(j, i + 2)){
								//increment by 1 for testing if i = 0
								if (i == 0) {
									i += 1;
								}
								if(model.getPieceIn(j, i +3) == 0 || model.getPieceIn(j, i -1) == 0){
									move = i;
								if(move > nrCols)
									move = i - winningSteak;
								break;
							}
							}
						}
						if(i+2< nrCols){
							if (model.getPieceIn(j, i) == model.getPieceIn(j, i + 1) && model.getPieceIn(j, i) == model.getPieceIn(j, i + 2)
									&& model.getPieceIn(j, i -1) == 0) {
									move = i;
									if(move > nrCols)
										move = i - winningSteak;
									break;
									}
						}
					}
				}
			}
		}
			return move;
	}


	public int checkWinnerMove(int activePlayer, int inactivePlayer) {
		int steak = settings.minStreakLength;
		int length = steak - 1;
		int winningSteak = steak - 2;
		int randomNumber = (int) (Math.random() * (nrCols)) + 1;
		int bottomRow;
		int lastCol;
		int nextCol;
		int move;

		// Testing purpose for setting move
		move = randomNumber;

		// Check horizontal steak
		for (int i = 0; i < nrRows; i++) {
			for (int j = 0; j < nrCols - winningSteak; j++) {
				for (int k = j; k < j + length; k++) {
					if (k >= 0 && k < nrCols && model.getPieceIn(i, k) != activePlayer) {
						break;
					}
					if (k == j + winningSteak) {
						move = k + winningSteak;
						if (move > nrCols) {
							move = k - winningSteak;
						}
						break;
					}
				}
			}
		}

		// Check vertical steak
		for (int i = 0; i < nrRows - winningSteak; i++) {
			for (int j = 0; j < nrCols; j++) {
				for (int k = i; k < i + length; k++) {
					if (k >= 0 && k < nrRows && model.getPieceIn(k, j) != activePlayer) {
						break;
					}
					if (k == i + winningSteak && model.getPieceIn(k - length, j) != inactivePlayer) {
						move = j + 1;
						if (!model.isMoveValid(move)) {
							move = randomNumber;
						} else {
							move = j + 1;
						}
						break;
					}
				}
			}
		}

		// Check diagonal down right steak
		for (int i = 0; i < nrRows - winningSteak; i++) {
			for (int j = 0; j < nrCols - winningSteak; j++) {
				for (int k = 0; k < length; k++) {
					if ((i + k >= 0 && i + k < nrRows && j + k >= 0 && j + k < nrCols) &&
							model.getPieceIn(i + k, j + k) != activePlayer) {
						break;
					}
					if (k == winningSteak) {
						bottomRow = i + length;
						lastCol = j + length;
						if (bottomRow >= nrRows) {
							bottomRow = nrRows - 1;
						}
						if (lastCol >= nrCols) {
							lastCol = nrCols - 1;
						}
						if (j - 1 >= 0 && model.getPieceIn(i, j - 1) != 0 && model.getPieceIn(i - 1, j - 1) == 0) {
							move = j;
							break;
						} else if ((model.getPieceIn(i + winningSteak, j + winningSteak) == 0 ||
								(bottomRow >= 0 && bottomRow < nrRows && lastCol >= 0 && lastCol < nrCols) && model.getPieceIn(bottomRow, lastCol) == 0)) {
							move = lastCol + 1;
						}
					}
				}
			}
		}

		// Check diagonal down steak
		for (int i = 0; i < nrRows - winningSteak; i++) {
			for (int j = winningSteak; j < nrCols; j++) {
				nextCol = j + 1;
				int k;

				for (k = 0; k < length; k++) {
					bottomRow = i + length;
					lastCol = j - length;

					if (bottomRow >= nrRows) {
						bottomRow = nrRows - 1;
					}
					if (lastCol < 0) {
						lastCol = 0;
					} else if (lastCol >= nrCols) {
						lastCol = nrCols - 1;
					}
					if (nextCol >= nrCols) {
						nextCol = nrCols - 1;
					}

					if ((i + k >= 0 && i + k < nrRows && j - k >= 0 && j - k < nrCols) &&
							model.getPieceIn(i + k, j - k) != activePlayer) {
						break;
					}

					if (k == winningSteak) {
						if (nextCol >= 0 && model.getPieceIn(i, nextCol) != 0 && (i + 1 >= 0 && i + 1 < nrRows) && model.getPieceIn(i + 1, nextCol) == 0) {
							move = j + 2;
							break;
						} else if ((model.getPieceIn(i + winningSteak, j - winningSteak) == 0 ||
							(bottomRow >= 0 && bottomRow < nrRows && lastCol >= 0 && lastCol < nrCols) && model.getPieceIn(bottomRow, lastCol) == 0)) {
							move = lastCol + 1;
							break;
						}
					}
				}
			}
		}

		return move;

	}
}



