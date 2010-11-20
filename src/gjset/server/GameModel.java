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
 * 
 * Note that the model does not do any checking internally for whether or not
 * a particular action is valid.  It assumes that anyone that is manipulating this data
 * knows what they're doing.
 * 
 * Generally speaking, the controller is graced with the need to verify the validity of incoming actions.
 */
public class GameModel extends Observable
{
	// Store the cards and their current states. 
	private Deck deck; 
	private CardTable cardTable;
	
	// Stores the current state of the game.
	private int gameState;
	private int setCallerId;
	
	// Right now there's only a single player.
	private List<Player> players;
	
	public GameModel()
	{
		// There is no active game at this time.
		gameState = GameConstants.GAME_STATE_NOT_STARTED;

		// Create the deck.
		deck = new Deck();
		
		//Create the card table.
		cardTable = new CardTable();
		
		// Create a default player
		players = new Vector<Player>();
		
		// Set the default set caller id
		setCallerId = 0;
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

	/**
	 * Draw more cards from the deck and place them on the card table.
	 *
	 */
	public void drawCards()
	{
		// Draw 3 new cards to add to the table.
		cardTable.addCards(deck.drawCards(3));

		// Check to see if the game might be over.
		if (deck.getRemainingCards() == 0)
		{
			checkForEndofGame();
		}
	}

	/*
	 * This method checks to see if the game is over.  
	 * The game is considered over when the deck is empty and there are no sets on the board.
	 */
	private void checkForEndofGame()
	{
		// If there are still cards in the deck, the game is not yet over.
		if (deck.getRemainingCards() > 0) return;

		// Now check all the cards to see if there are any sets there.
		List<Card> cards = cardTable.getCards();
		
		for (int i = 0; i < cards.size(); i++)
		{
			Card card1 = cards.get(i);
			for (int j = i + 1; j < cards.size(); j++)
			{
				Card card2 = cards.get(j);
				//System.out.println("Checking " + card1 + " and " + card2);
				Card testCard = finishSet(card1, card2);
				//System.out.println("Found " + testCard);

				// If the remaining cards contains the test card, the game is still on.
				if (cards.contains(testCard)) return;
				//System.out.println("But the card was not on the table.");
			}
		}

		// We made it this far, there are no sets remaining.
		gameState = GameConstants.GAME_STATE_GAME_OVER;
	}

	/*
	 * This function takes two cards and returns the only possible card that completes the set.
	 * 
	 * This method is used to determine if there are any sets on the table.
	 * 
	 */
	private Card finishSet(Card card1, Card card2)
	{
		Card card3 = new Card();
		// Generate each property on the card.
		for (int property = 1; property <= 4; property++)
		{
			// Check if the first two cards match. If they do, then the third card will also match.
			if (card1.getProperty(property) == card2.getProperty(property))
			{
				card3.setProperty(property, card1.getProperty(property));
			}
			else
			{
				// The cards differ. Find out which value they are not using.
				for (int value = 1; value < 4; value++)
				{
					if (card1.getProperty(property) != value && card2.getProperty(property) != value)
					{
						card3.setProperty(property, value);
					}
				}
			}
		}
		
		return card3;
	}

	/**
	 * Return the id of the player that called set.
	 *
	 * @return
	 */
	public int getSetCallerId()
	{
		return setCallerId;
	}

	/**
	 * Cause the model to engage call set mode for the indicated player.
	 *
	 * @param playerId
	 */
	public void callSet(int playerId)
	{
		gameState = GameConstants.GAME_STATE_SET_CALLED;
		setCallerId = playerId;
	}

	/**
	 * Toggle selection on the indicated card. 
	 *
	 * @param card
	 */
	public void toggleCardSelection(Card card)
	{
		cardTable.toggleSelection(card);
	}
}
