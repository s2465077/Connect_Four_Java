package players;

import interfaces.IModel;
import interfaces.IPlayer;
import util.InputUtil;

/**
 * The class allows the user to enter the column index into which he/she wants to insert their next piece
 * (or -1 if they really wish to concede).
 * It prints out a message asking the user to enter a move.
 */
public class HumanPlayer implements IPlayer
{
	// The constructor is called when the player is selected from the game menu.
	public HumanPlayer()
	{
	}
	
	// This method is called when a new game is started or loaded.
	public void prepareForGameStart(IModel model, byte playerId)
	{
	}
	
	// This method is called to ask the player to take their turn.
	// The move they choose should be returned from this method.
	public int chooseMove()
	{

		System.out.println("Enter a column (1-7) for your next move! ");
		System.out.println("(Please enter -1 if you wish to concede.)");
		int input = InputUtil.readIntFromUser();
		if(input == -1){
			return IModel.CONCEDE_MOVE;
		} else {
			return input;
		}

	}
}
