package jset.engine;

import java.util.Iterator;
import java.util.Vector;

import jset.gui.Card;
import jset.gui.JSetGUI;

public class GameController
{
	private Deck		deck; //Stores the deck of cards.
	private JSetGUI		gui; //The gui interface.
	
	private Vector<Card> selectedCards; //Stores the current set of selected cards.

	public GameController()
	{
		this.deck = new Deck();
		selectedCards = new Vector<Card>();
	}
	
	//Link the GUI object to the game controller.
	public void linkGUI(JSetGUI gui)
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
	}

	//Handles the act of selecting the card passed in as an objecct.
	public void selectCard(Card card)
	{
		//Check to see if the indicated card has already been highlighted.
		if(card.isHighlighted())
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
			if(selectedCards.size() == 3)
			{
				//Determine if the set of cards is a set.
				if( checkForSet(selectedCards))
				{			
					//Display a message indicating that this is a set.
					gui.getMessageBar().displayMessage("That's a set!");
					
					//TODO The following process should be animated.
					//Draw 3 new cards to replace the existing ones.
					gui.getCardTable().replaceCards(selectedCards, deck.drawCards(3));
					selectedCards.removeAllElements();
				}
				else
				{
					//Display a message on the gui.
					gui.getMessageBar().displayMessage("That's not a set!");
					
					//That wasn't a set...  Remove the highlight on these cards.					
					Iterator<Card> iterator = selectedCards.iterator();
					while(iterator.hasNext())
					{
						iterator.next().setHighlight(false);						
					}
					
					//Deselect the cards.
					selectedCards.removeAllElements();
				}
			}
		}
		
		//Once we've finished selecting the card, redraw the table.
		gui.getCardTable().drawTable();
	}

	//This function checks a vector of cards to determine if they are a set.
	private boolean checkForSet(Vector<Card> cards)
	{
		//Check each property
		for(int property = 1; property <= 4; property++)
		{
			//Check the first two cards against each other.
			if(cards.get(0).getProperty(property) == cards.get(1).getProperty(property))
			{
				//The first two cards match.  The next two should match as well.
				if(cards.get(1).getProperty(property) != cards.get(2).getProperty(property))
				{
					return false;
				}
			}
			else
			{
				//The first two cards do not match.  The next two should not match either.
				if(cards.get(1).getProperty(property) == cards.get(2).getProperty(property))
				{
					return false;
				}
			}
		}
		
		//We managed to make it through the gauntlet!  This REALLY IS A SET!!!
		return true;
	}
}
