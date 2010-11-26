package gjset.server;

import gjset.GameConstants;
import gjset.data.Card;
import gjset.data.Player;
import gjset.tools.CountdownTimer;

import java.util.List;
import java.util.Observable;
import java.util.Vector;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  Set¨ is a registered trademark of Set Enterprises. 
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
	private boolean isLastSetCorrect;
	
	// Right now there's only a single player.
	private Player[] players;
	
	private CountdownTimer setTimer;
	private CountdownTimer displayTimer;
	
	private static final int SET_TIME = 3500;
	private static final int DISPLAY_TIME = 1000;
	
	
	public GameModel()
	{
		// There is no active game at this time.
		gameState = GameConstants.GAME_STATE_NOT_STARTED;

		// Create the deck.
		deck = new Deck();
		
		//Create the card table.
		cardTable = new CardTable();
		
		// Create the player array.
		players = new Player[GameConstants.MAX_PLAYERS];
		
		// Set the default set caller id
		setCallerId = 0;
		
		// Create a set timer to abort an incoming set.
		setTimer = new CountdownTimer(SET_TIME, new Runnable()
		{
			public void run()
			{
				handleSetTimeout();
			}
		});
		
		// Create a display timer to resume gameplay after the results of a set are displayed.
		displayTimer = new CountdownTimer(DISPLAY_TIME, new Runnable()
		{
			public void run()
			{
				resumeGame();
			}
		});
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
	 * Return the the player that called set.
	 *
	 * @return
	 */
	public Player getSetCaller()
	{
		return players[setCallerId - 1];
	}
	
	/**
	 * Returns whether the last set called was correct.
	 * 
	 * @return boolean
	 */
	public boolean getLastSetCorrect()
	{
		return isLastSetCorrect;
	}

	/**
	 * Return the full list of players.
	 *
	 * @return
	 */
	public List<Player> getPlayers()
	{
		Vector<Player> playerList = new Vector<Player>();
		
		for(int i = 0; i < players.length; i++)
		{
			if(players[i] != null)
			{
				playerList.add(players[i]);
			}
		}
		
		return playerList;
	}

	/**
	 * Return an existing player associated with the indicated username.
	 * 
	 * If none exists, null is returned.
	 *
	 * @param username
	 * @return
	 */
	public Player getExistingPlayer(String username)
	{
		for(int i = 0; i < players.length; i++)
		{
			Player player = players[i];
			if(player != null && player.getName().equals(username))
			{
				return player;
			}
		}
		
		return null;
	}

	/**
	 * Create a new player and then return it.
	 * @param username 
	 *
	 * @return
	 */
	public Player addNewPlayer(String username)
	{
		// Find the first empty slot.
		int id = 1;
		Player player = players[id - 1];
		while(player != null)
		{
			id++;
			player = players[id - 1];
		}
		
		// Then add a new player to that slot.
		player = new Player(id, username);
		players[id - 1] = player;
		return player;
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
		for(int i = 0; i < players.length; i++)
		{
			Player player = players[i];
			if(player != null)
			{
				player.resetScore();
			}
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
	 * Draw more cards from the deck and place them on the card table.
	 *
	 */
	public void drawCards()
	{
		resetCardDrawDesires();
		
		// Draw 3 new cards to add to the table.
		cardTable.addCards(deck.drawCards(3));
	
		// Check to see if the game might be over.
		if (deck.getRemainingCards() == 0)
		{
			checkForEndofGame();
		}
		
		// Notify observers that the model has changed.
		setChanged();
		notifyObservers();
	}

	/**
	 * Find the player with the indicated id and set their draw cards desire.
	 *
	 * @param playerId
	 * @param value
	 */
	public void setDesireToDrawCards(int playerId, boolean value)
	{
		for(int i = 0; i < players.length; i++)
		{
			if(players[i] != null && players[i].getId() == playerId)
			{
				players[i].setWantsToDraw(value);
				return;
			}
		}
	}

	/**
	 * Hunt through all players to see who wants to draw cards.
	 * Return true if all players have this desire.
	 *
	 * @return
	 */
	public boolean allPlayersWantToDraw()
	{
		for(int i = 0; i < players.length; i++)
		{
			if(players[i] != null && !players[i].getWantsToDraw())
			{
				return false;
			}
		}
		
		// If we made it this far, everyone wants to draw.
		return true;
	}

	/**
	 * Cause the model to engage call set mode for the indicated player.
	 *
	 * @param playerId
	 */
	public void callSet(int playerId)
	{
		resetCardDrawDesires();
		
		gameState = GameConstants.GAME_STATE_SET_CALLED;
		setCallerId = playerId;
		
		// If this isn't a single player game, start a timer.
		if(getPlayers().size() > 1)
		{
			setTimer.start();
		}
		
		// Notify observers that the model has changed.
		setChanged();
		notifyObservers();
	}

	/**
	 * Toggle selection on the indicated card. 
	 *
	 * @param card
	 */
	public void toggleCardSelection(Card card)
	{
		cardTable.toggleSelection(card);
		
		// Notify observers that the model has changed.
		setChanged();
		notifyObservers();
	}

	/**
	 * Resolves the selected cards to determine if they're a set.
	 *
	 * @param isTimeout		false if the set timer expired, otherwise true
	 * 
	 * @return Return true if this actually was a set.
	 */
	public boolean resolveSet(boolean isWithinTime)
	{
		// Start by clearing the set timer.
		setTimer.cancel();
		
		// Get the player object associated with the player that selected these cards.
		Player setPlayer = getSetCaller();
		
		List<Card> selectedCards = cardTable.getSelectedCards();
		isLastSetCorrect = checkForSet(selectedCards);
		
		if(isLastSetCorrect)
		{
			// SCORE!
			setPlayer.addPoints(GameConstants.SET_POINTS);
		}
		else
		{
			// Take away points
			setPlayer.addPenalty(GameConstants.SET_PENALTY);
		}
		
		// Switch to the "Set Finished" gameState for a few seconds.
		gameState = GameConstants.GAME_STATE_SET_FINISHED;
		
		// Start the displayTimer which will switch us back to the Idle gameState.
		displayTimer.start();
			
		// Notify observers that the model has changed.
		setChanged();
		// If time ran out, this is an unsolicited update and we need to process it accordingly.
		notifyObservers(new Boolean(isWithinTime));
		
		return isLastSetCorrect;
	}	


	/**
	 * Remove the player with the indicated id.
	 *
	 * @param playerId
	 */
	public void removePlayer(int playerId)
	{
		players[playerId - 1] = null;
	}

	/**
	 * Destroy the model.
	 *
	 */
	public void destroy()
	{
		players = null;
		setTimer.cancel();
		setTimer = null;
	}

	/**
	 * Tell all players to stop wanting to draw cards.
	 *
	 */
	private void resetCardDrawDesires()
	{
		for(int i = 0; i < players.length; i++)
		{
			if(players[i] != null)
			{
				players[i].setWantsToDraw(false);
			}
		}
	}

	/**
	 * Deal with the fact that no set was called within the alotted time.
	 *
	 */
	private void handleSetTimeout()
	{
		// Verify that the model is still in the same state it was.
		if(gameState == GameConstants.GAME_STATE_SET_CALLED)
		{
			// Treat the selected cards as an incorrect Set due to time running out.
			resolveSet(false);
		}
	}

	/**
	 * Resume the game from the "Set Finished" game state, removing, drawing, and de-selecting
	 * cards as necessary.
	 */
	private void resumeGame()
	{
		// Figure out which cards are selected.
		List<Card> selectedCards = cardTable.getSelectedCards();
		
		// If the set call was successful, remove the Set and replace cards as necessary.
		if (isLastSetCorrect)
		{	
			// Check to see if we can draw more cards.
			if (deck.getRemainingCards() > 0 && cardTable.getNumCards() <= 12)
			{
				// Draw the new cards and place them on the table.
				cardTable.replaceCards(selectedCards, deck.drawCards(3));
			}
			else
			{
				// We aren't allowed to draw new cards, so just remove the selected ones.
				cardTable.removeCards(selectedCards);
			}
		}
		
		// De-select any cards that are still selected.
		cardTable.unSelectCards();
		
		// For now, we immediately return to the idle state.
		gameState = GameConstants.GAME_STATE_IDLE;
		setCallerId = 0;
		
		// Check to see if this is the end of the game.
		checkForEndofGame();
		
		// Notify observers that the model has changed.
		setChanged();
		// This is an unsolicited update, too.
		notifyObservers(new Boolean(false));
	}

	
	/**
	 *  This function checks a vector of cards to determine if they are a set.
	 */
	private boolean checkForSet(List<Card> cards)
	{
		// Make sure there are three cards.  If not, it's obviously not a set.
		if (cards.size() != 3)
		{
			return false;
		}
		
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

	/**
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
}
