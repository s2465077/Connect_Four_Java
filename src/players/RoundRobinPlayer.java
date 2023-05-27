package players;

import interfaces.IModel;
import interfaces.IPlayer;
import util.GameSettings;


/**
 *This player's first move (after a game is started/loaded) is always to play the leftmost column that is not full.
 *The column index then increases by 1 each time, until we reach the last column and return to column 0.
 *A typical sequence of moves would therefore be: 0, 1, 2, 3, 4, 5, 6, 0, 1, ...
 *The only exception is when a column is full. In this case, skip ahead until a valid column is found.
 */

public class RoundRobinPlayer implements IPlayer
{
	// A reference to the model, which gets information about
	// the state of the game.
	private IModel model;

	private GameSettings settings;
	private int firstCol;
	
	// The constructor is called when the player is selected from the game menu.
	public RoundRobinPlayer()
	{
		settings = new GameSettings();
	}
	
	// This method is called when a new game is started or loaded.
	// Perform any setup that may be required before
	// the player is asked to make a move. The second argument
	// specify if you are playing as player 1 or player 2.
	public void prepareForGameStart(IModel model, byte playerId)
	{
		this.model = model;
		this.settings =  model.getGameSettings();
		int nrRows = settings.nrRows;

		this.firstCol = 1;
		int empty = 0;
		int testCol = 0;

		do{
			for (int row = nrRows-1; row >= 0; row--) {
				if(model.getPieceIn(row, testCol)== 0) {
					empty += 1;
					break;
				}
			} if(empty <= 0) {
				testCol++;
			}
		} while(empty <= 0);

		firstCol = testCol + 1;

	}
	
	// This method is called to ask the player to take their turn.
	// The move they choose was returned from this method.
	public int chooseMove() {

		int nrCols = settings.nrCols;
		System.out.println("AI player choose a column (1-7) for the next move! ");

		int move = this.firstCol;


		while(move <= nrCols) {
			if (!model.isMoveValid(move))
				move += 1;
			break;
		}

		//if no empty space at column 6, return to column 0 to start the rolling.
		if(move == 7 && !model.isMoveValid(move)){
			move = 0;
			while(move <= nrCols) {
				if (!model.isMoveValid(move))
					move += 1;
				break;
			}
		}

		//Add 1 column for next move, return to 0 if already moving through the whole row
		if(firstCol >= nrCols){
			firstCol = 1;
		} else if (firstCol < nrCols) {
			firstCol = move + 1;
		}

		return move;

	}
}
