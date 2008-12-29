package gjset.engine;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton
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

import gjset.gui.CardComponent;
import gjset.gui.GjSetGUI;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class GameController
{
	private Deck					deck;			//Stores the deck of cards.
	private GjSetGUI				gui;			//The gui interface.
	private boolean					gameActive;	//Indicates whether there is an active game or not.

	private Vector<CardComponent>	selectedCards;	//Stores the current set of selected cards.

	public GameController()
	{
		//There is no active game at this time.
		gameActive = false;

		//Create the deck.
		this.deck = new Deck();
		selectedCards = new Vector<CardComponent>();
	}

	//Link the GUI object to the game controller.
	public void linkGUI( GjSetGUI gui )
	{
		this.gui = gui;
	}

	//This function starts a new game of set.
	public void newGame()
	{
		//Shuffle the cards
		deck.shuffle();

		//Remove any cards that were previously on the table.
		gui.getCardTable().removeCards();

		//Draw 12 new cards to add to the table.
		gui.getCardTable().addCards(deck.drawCards(12));

		//Show a message indicating that the game has begun.
		gui.getMessageBar().displayMessage("Welcome to jSet!");

		//Set the game active flag to true.
		gameActive = true;

		//Show the player panel
		gui.showPlayerPanel();
	}

	//Handles the act of selecting the card passed in as an object.
	public void onCardClicked( CardComponent card )
	{
		if (!gameActive)
			return; //Do nothing if the game isn't running.

		//Check to see if the indicated card has already been highlighted.
		if (card.isHighlighted())
		{
			//Remove the highlight
			card.setHighlight(false);

			//Remove the card from the vector of selected cards.
			selectedCards.remove(card);
		}
		else
		{
			//Add the card to the list of selected cards.
			selectedCards.add(card);

			//Display the highlight on the card.
			card.setHighlight(true);

			//Check to see if sufficient cards have been selected.
			if (selectedCards.size() == 3)
			{
				//Determine if the set of cards is a set.
				if (checkForSet(convertCardComponentVectorToCardVector(selectedCards)))
				{
					//Display a message indicating that this is a set.
					gui.getMessageBar().displayMessage("That's a set!");

					//Check to see if we can draw more cards.
					if (deck.remainingCards() > 0 && gui.getCardTable().getNumCards() <= 12)
					{
						//Draw the new cards and place them on the table.
						gui.getCardTable().replaceCards(selectedCards, deck.drawCards(3));
						if (deck.remainingCards() == 0)
						{
							gui.getMessageBar().displayMessage("That's all the cards we've got!");
						}
					}
					else
					{
						//There are no cards left to draw.  Just remove the selected ones.
						gui.getCardTable().removeCards(selectedCards);
					}
					selectedCards.removeAllElements();

					checkForEndofGame();
				}
				else
				{
					//Display a message on the gui.
					gui.getMessageBar().displayMessage("That's not a set!");

					//That wasn't a set...  Remove the highlight on these cards.					
					Iterator<CardComponent> iterator = selectedCards.iterator();
					while (iterator.hasNext())
					{
						iterator.next().setHighlight(false);
					}

					//Deselect the cards.
					selectedCards.removeAllElements();
				}
			}
		}
	}

	private void checkForEndofGame()
	{
		//System.out.println("Checking for end of game.");

		//If there are still cards in the deck, the game is not yet over.
		if (deck.remainingCards() > 0)
			return;

		//Now check all the cards to see if there are any sets there.
		List<Card> cards = convertCardComponentVectorToCardVector(gui.getCardTable().getCards());
		for (int i = 0; i < cards.size(); i++)
		{
			Card card1 = cards.get(i);
			for (int j = i + 1; j < cards.size(); j++)
			{
				Card card2 = cards.get(j);
				//System.out.println("Checking " + card1 + " and " + card2);
				Card testCard = finishSet(card1, card2);
				//System.out.println("Found " + testCard);

				//If the remaining cards contains the test card, the game is still on.
				if (cards.contains(testCard))
					return;
				//System.out.println("But the card was not on the table.");
			}
		}

		//We made it this far, there are no sets remaining.
		gameActive = false;
		gui.hidePlayerPanel();
		gui.getMessageBar().displayMessage("No sets remain.  YOU WIN!");
	}

	/**
	 * This function takes two cards and provides the system with the only possible card that would make the two cards part of a set.
	 * 
	 * @param card1
	 * @param card2
	 * @return
	 */
	private Card finishSet( Card card1, Card card2 )
	{
		Card card3 = new Card();
		//Generate each property on the card.
		for (int property = 1; property <= 4; property++)
		{
			//Check if the first two cards match.  If they do, then the third card will also match.
			if (card1.getProperty(property) == card2.getProperty(property))
			{
				card3.setProperty(property, card1.getProperty(property));
			}
			else
			{
				//The cards differ.  Find out which value they are not using.
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

	private Vector<Card> convertCardComponentVectorToCardVector( List<CardComponent> cardComponents )
	{
		Vector<Card> cards = new Vector<Card>();
		Iterator<CardComponent> iterator = cardComponents.iterator();
		while (iterator.hasNext())
		{
			cards.add(iterator.next().getCard());
		}
		return cards;
	}

	//This function checks a vector of cards to determine if they are a set.
	private boolean checkForSet( Vector<Card> cards )
	{
		//		System.out.println("Comparing the following cards:");
		//		for(int i = 0; i < cards.size(); i++)
		//		{
		//			System.out.println(cards.get(i).toString());
		//		}

		//Check each property
		for (int property = 1; property <= 4; property++)
		{
			//System.out.println("Checking property " + property);

			//Check the first two cards against each other.
			if (cards.get(0).getProperty(property) == cards.get(1).getProperty(property))
			{
				//System.out.println("The first two cards match in this property!");
				//The first two cards match.  The next two should match as well.
				if (cards.get(1).getProperty(property) != cards.get(2).getProperty(property))
				{
					//System.out.println("But the second two do not.  This is not a set.");
					return false;
				}
			}
			else
			{
				//System.out.println("The first two cards do not match in this property!");
				//The first two cards do not match.  The next two should not match either.
				if (cards.get(1).getProperty(property) == cards.get(2).getProperty(property)
						|| cards.get(0).getProperty(property) == cards.get(2).getProperty(property))
				{
					//System.out.println("But there are two that do.  This is not a set.");
					return false;
				}
			}
		}

		//System.out.println("Well, we ran the gauntlet.  This is definitely a set.");
		//We managed to make it through the gauntlet!  This REALLY IS A SET!!!
		return true;
	}

	public void callSet()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Handle the situation where the user believes there are no more sets available.
	 */
	public void noMoreSets()
	{
		//If the game is not active, do nothing.
		if (!gameActive)
			return;

		if (deck.remainingCards() < 3)
		{
			gui.getMessageBar().displayMessage("There are no more cards to draw.");
		}
		else if (gui.getCardTable().getNumCards() < 18)
		{
			//Unselect all selected cards.
			Iterator<CardComponent> iterator = selectedCards.iterator();
			while (iterator.hasNext())
			{
				iterator.next().setHighlight(false);
			}
			selectedCards.removeAllElements();

			//Draw 3 new cards to add to the table.
			gui.getCardTable().addCards(deck.drawCards(3));

			//Check to see if the game might be over.
			if (deck.remainingCards() == 0)
			{
				checkForEndofGame();
			}
		}
		else
		{
			gui.getMessageBar().displayMessage("You don't need to draw more cards.");
		}
	}
}
