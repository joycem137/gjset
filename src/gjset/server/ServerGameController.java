package gjset.server;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  gjSet is Copyright 2008-2010 Artless Entertainment
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of Combo Cards are very grateful for
 *  them creating such an excellent card game.
 *  
 *  Combo Cards is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Combo Cards is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with Combo Cards.  If not, see <http://www.gnu.org/licenses/>.
 */

import gjset.GameConstants;
import gjset.data.Card;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.dom4j.Element;

/**
 * 
 */
public class ServerGameController implements Observer
{	
	private GameModel model;
	
	/**
	 * Create a GameEngine with a link to the {@link MainGamePanel} using the {@link ClientLinkInterface} parameter passed in.
	 * 
	 * This constructor instantiates and initializes all of the game model data.
	 *
	 * @param client The link to the {@PlayerUI}.
	 */
	public ServerGameController()
	{
		model = new GameModel();
	}

	/**
	 * 
	 * Causes the indicated {@link Card} object to be highlighted on screen.  If this is the third highlighted card,
	 * this method will determine if this is a set or not and send an update to the {@link MainGamePanel}.
	 * <P>
	 * If the card was already highlighted, this method will unhighlight it.
	 *
	 * @param card The selected card.
	 */
	public Element selectCard(int playerId, Card card)
	{	
		int gameState = model.getGameState();
		
		if(gameState == GameConstants.GAME_STATE_IDLE)
		{
			// Call set.
			model.callSet(playerId);
			
			// Select the card.
			model.toggleCardSelection(card);
		}
		else if(gameState == GameConstants.GAME_STATE_SET_CALLED
				&& playerId == model.getSetCallerId())
		{
			// Allow the next card to be selected.
			model.toggleCardSelection(card);
		}
		else
		{
			// Abort early if we can't select a card.
			return;
		}

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
					if (deck.getRemainingCards() > 0 && cardTable.getNumCards() <= 12)
					{
						// Draw the new cards and place them on the table.
						cardTable.replaceCards(selectedCards, deck.drawCards(3));
						
						if (deck.getRemainingCards() == 0)
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
		if (deck.getRemainingCards() > 0) return;

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
	 * Used by the client when the player selects the "No more sets" button.
	 * This indicates that the player thinks there are no more sets on the board
	 * and that the engine should react appropriately.
	 */
	public void noMoreSets()
	{
		// If the game is not active, do nothing.
		if (!gameActive) return;

		if (deck.getRemainingCards() < 3)
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
			if (deck.getRemainingCards() == 0)
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
	 * Handle any changes coming in from the model
	 *
	 * @param arg0
	 * @param arg1
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj)
	{
		// Handle a change from the model.
	}
	
	/**
	 * Handle a message incoming from the server.
	 */
	public void handleMessage(Element message)
	{
		// Do things like call set, select cards, etc.
	}
}
