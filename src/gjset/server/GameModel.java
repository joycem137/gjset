package gjset.server;

import gjset.GameConstants;
import gjset.data.Card;
import gjset.data.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

/**
 * This class stores all of the information used to represent the game.
 */
public class GameModel extends Observable
{
	// Store the cards and their current states. 
	private Deck deck; 
	private CardTable cardTable;
	
	// Stores the current state of the game.
	private int gameState;

	// Keep a list of what cards are currently selected.
	private List<Card> selectedCards; 
	
	// Right now there's only a single player.
	private List<Player> players;
	
	public GameModel()
	{
		// There is no active game at this time.
		gameState = GameConstants.GAME_STATE_NOT_STARTED;

		// Create the deck.
		deck = new Deck();
		selectedCards = new Vector<Card>();
		
		//Create the card table.
		cardTable = new CardTable();
		
		// Create a default player
		players = new Vector<Player>();
	}

	/**
	 * 
	 * Start a new game.
	 *
	 */
	public void startNewGame()
	{
		// Shuffle the cards
		deck.shuffle();
		
		// reset the scores
		Iterator<Player> iterator = players.iterator();
		while(iterator.hasNext())
		{
			iterator.next().resetScore();
		}
		
		//Clear the card table.
		cardTable.removeCards();
		
		//Deal 12 cards to the table.
		cardTable.addCards(deck.drawCards(12));
		
		// Set the game active flag to true.
		gameState = GameConstants.GAME_STATE_IDLE;
		
		// Notify observers that the model has changed.
		setChanged();
		notifyObservers();
	}

	/**
	 * 
	 *
	 * @param playerId
	 */
	public void callSet(int playerId)
	{
		
	}

	/**
	 * Return the current state of the game.
	 *
	 * @return
	 */
	public int getGameState()
	{
		return gameState;
	}

	/**
	 * Return the deck.
	 *
	 * @return
	 */
	public Deck getDeck()
	{
		return deck;
	}

	/**
	 * Return the card table we're using.
	 *
	 * @return
	 */
	public CardTable getCardTable()
	{
		return cardTable;
	}
}
