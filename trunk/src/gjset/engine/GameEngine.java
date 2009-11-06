package gjset.engine;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton and Andrea Kilpatrick
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of gjSet are very grateful for
 *  them creating such an excellent card game.
 *  
 *  gjSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  gjSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with gjSet.  If not, see <http://www.gnu.org/licenses/>.
 */

import gjset.data.Card;
import gjset.data.CardTable;
import gjset.data.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * This is the "brains" behind the operation. In the Model-View-Controller architecture, this is handling both the Controller and Model
 * aspects.  The GameEngine object gets a {@link ClientLinkInterface} object to communicate with the client to send updates to the UI
 * whenever something changes.  If something happens on the client, the {@link PlayerUI} object sends a message to this object informing it
 * of the event, and the GameEngine reacts appropriately.
 * 
 * The Model portion of the GameEngine is stored in 3 objects:  The {@link Deck}, the {@link CardTable}, and the {@link Player}.
 * Each stores pertinent data about what the current state of the game is.
 */
public class GameEngine implements Observer
{
	private Deck					deck;					// Stores the deck of cards.
	private ClientLinkInterface		client;					// The gui interface.
	private boolean					gameActive;				// Indicates whether there is an active game or not.

	private Vector<Card>			selectedCards;			// Stores the current set of selected cards.
	private CardTable				cardTable;
	private Player					player;					// player data for this game.
	
	/**
	 * Create a GameEngine with a link to the {@link PlayerUI} using the {@link ClientLinkInterface} parameter passed in.
	 * 
	 * This constructor instantiates and initializes all of the game model data.
	 *
	 * @param client The link to the {@PlayerUI}.
	 */
	public GameEngine(ClientLinkInterface client)
	{
		//Store a link to the player interface
		this.client = client;
		
		// There is no active game at this time.
		gameActive = false;

		// Create the deck.
		deck = new Deck();
		selectedCards = new Vector<Card>();
		
		//Create the card table.
		cardTable = new CardTable();
		
		//Add this as an observer to the CardTable.
		cardTable.addObserver(this);
		
		//Create a player.
		player = new Player(1);
	}

	/**
	 * 
	 * Tell the player's UI to do whatever it needs to do to show that a new game has been started.
	 *
	 */
	public void newGame()
	{
		// Shuffle the cards
		deck.shuffle();
		
		// reset the scores
		player.resetScore();

		//Start a new game on the interface.
		client.displayNewGame();
		
		//Clear the card table.
		cardTable.removeCards();
		
		//Deal 12 cards to the table.
		cardTable.addCards(deck.drawCards(12));
		
		// Set the game active flag to true.
		gameActive = true;
	}

	/**
	 * 
	 * Causes the indicated {@link Card} object to be highlighted on screen.  If this is the third highlighted card,
	 * this method will determine if this is a set or not and send an update to the {@link PlayerUI}.
	 * <P>
	 * If the card was already highlighted, this method will unhighlight it.
	 *
	 * @param card The selected card.
	 */
	public void selectCard(Card card)
	{
		if (!gameActive) return; // Do nothing if the game isn't running.

		// Check to see if the indicated card has already been highlighted.
		if (cardTable.isHighlighted(card))
		{
			// Remove the highlight
			cardTable.setHighlight(card, false);

			// Remove the card from the vector of selected cards.
			selectedCards.remove(card);
		}
		else
		{
			// Add the card to the list of selected cards.
			selectedCards.add(card);

			// Display the highlight on the card.
			cardTable.setHighlight(card, true);

			// Check to see if sufficient cards have been selected.
			if (selectedCards.size() == 3)
			{
				// Determine if the set of cards is a set.
				if (checkForSet(selectedCards))
				{
					player.addPoints(3);
					client.confirmSet();
					client.updatePlayer(player);

					// Check to see if we can draw more cards.
					if (deck.remainingCards() > 0 && cardTable.getNumCards() <= 12)
					{
						// Draw the new cards and place them on the table.
						cardTable.replaceCards(selectedCards, deck.drawCards(3));
						
						if (deck.remainingCards() == 0)
						{
							client.indicateOutOfCardsToDraw();
						}
					}
					else
					{
						// There are no cards left to draw. Just remove the selected ones.
						cardTable.removeCards(selectedCards);
					}
					selectedCards.removeAllElements();

					checkForEndofGame();
				}
				else
				{
					player.addPenalty(3);
					client.rejectSet();
					client.updatePlayer(player);

					// That wasn't a set... Remove the highlight on these cards.
					Iterator<Card> iterator = selectedCards.iterator();
					while (iterator.hasNext())
					{
						cardTable.setHighlight(iterator.next(), false);
					}

					// Deselect the cards.
					selectedCards.removeAllElements();
				}
			}
		}
	}

	/*
	 * This private method checks to see if the game is over.  
	 * The game is considered over when the deck is empty and there are no sets on the board.
	 */
	private void checkForEndofGame()
	{
		System.out.println("Checking for end of game.");

		// If there are still cards in the deck, the game is not yet over.
		if (deck.remainingCards() > 0) return;

		// Now check all the cards to see if there are any sets there.
		List<Card> cards = cardTable.getCards();
		/*System.out.println("We have the following " + cards.size() + " cards on the table.");
		Iterator<Card> iterator = cards.iterator();
		while(iterator.hasNext())
		{
			System.out.println(iterator.next().toString());
		}*/
		
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
		gameActive = false;
		client.displayEndOfGame();
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

	// This function checks a vector of cards to determine if they are a set.
	private boolean checkForSet(Vector<Card> cards)
	{
		// Check each property
		for (int property = 1; property <= 4; property++)
		{
			// System.out.println("Checking property " + property);

			// Check the first two cards against each other.
			if (cards.get(0).getProperty(property) == cards.get(1).getProperty(property))
			{
				// System.out.println("The first two cards match in this property!");
				// The first two cards match. The next two should match as well.
				if (cards.get(1).getProperty(property) != cards.get(2).getProperty(property))
				{
					// System.out.println("But the second two do not.  This is not a set.");
					return false;
				}
			}
			else
			{
				// System.out.println("The first two cards do not match in this property!");
				// The first two cards do not match. The next two should not match either.
				if (cards.get(1).getProperty(property) == cards.get(2).getProperty(property)
						|| cards.get(0).getProperty(property) == cards.get(2).getProperty(property))
				{
					// System.out.println("But there are two that do.  This is not a set.");
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * Currently a placeholder, this function will perform the behavior of "calling set," which means locking out
	 * any other players that have not called set.
	 *
	 */
	public void callSet()
	{
		//Nothing to do at this time.
	}

	/**
	 * Used by the client when the player selects the "No more sets" button.
	 * This indicates that the player thinks there are no more sets on the board
	 * and that the engine should react appropriately.
	 */
	public void noMoreSets()
	{
		// If the game is not active, do nothing.
		if (!gameActive) return;

		if (deck.remainingCards() < 3)
		{
			client.indicateOutOfCardsToDraw();
		}
		else if (cardTable.getNumCards() < CardTable.CARD_LIMIT)
		{
			// Unselect all selected cards.
			Iterator<Card> iterator = selectedCards.iterator();
			while (iterator.hasNext())
			{
				cardTable.setHighlight(iterator.next(), false);
			}
			selectedCards.removeAllElements();

			// Draw 3 new cards to add to the table.
			cardTable.addCards(deck.drawCards(3));

			// Check to see if the game might be over.
			if (deck.remainingCards() == 0)
			{
				checkForEndofGame();
			}
		}
		else
		{
			client.indicateNoNeedToDrawMoreCards();
		}
	}

	/**
	 * 
	 * Currently a placeholder method, this method will do whatever is necessary for a player to quit a game and remove the player
	 * from the game.
	 *
	 */
	public void quitGame()
	{
		//Nothing to do at this time.
	}

	/**
	 * Implements the update method of the <code>Observer</code> interface.  
	 * This is currently used by the <code>CardTable</code> to update the <code>GameController</code> with
	 * the latest updates to the <code>CardTable</code>'s data.
	 * 
	 * @param o At this time, this is only going to be the <code>CardTable<code> object.
	 * @param arg Not used. 
	 * @see Observer, Observable
	 */
	public void update(Observable o, Object arg)
	{
		//Update the client's card table.
		client.updateTable(cardTable);
	}
}
