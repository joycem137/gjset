package gjset.client;

import gjset.GameConstants;
import gjset.data.CardTableData;
import gjset.data.PlayerData;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import org.dom4j.Element;

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
 * Stores all of the information for the GUI's model.  Note that this class does not just store the model for the
 * game state, but specifically the model for the GUI.  All information needed to render to the screen is stored here.
 */
public class ClientGUIModel extends Observable
{	
	private boolean setCorrect;
	private int setCallerId;
	private int gameState;
	private int deckSize;
	private List<PlayerData> players;
	private PlayerData localPlayer;
	
	private CardTableData cardTableData;
	
	public ClientGUIModel()
	{
		cardTableData = null;
		
		deckSize = 0;
		
		setCallerId = -1;
		
		gameState = GameConstants.GAME_STATE_NOT_STARTED;
	}
	
	/**
	 * Returns the number of cards in the deck.
	 *
	 * @return
	 */
	public int getCardsInDeck()
	{
		return deckSize;
	}
	
	/**
	 * Returns the indicated game state.
	 *
	 * @return
	 */
	public int getGameState()
	{
		return gameState;
	}

	/**
	 * Returns the card table associated with this model.
	 *
	 * @return
	 */
	public CardTableData getCardTable()
	{
		return cardTableData;
	}

	/**
	 * Return the player
	 *
	 * @return
	 */
	public PlayerData getLocalPlayer()
	{
		return localPlayer;
	}

	/**
	 * Set the player that represents *this* player.
	 *
	 * @param player
	 */
	public void setLocalPlayer(PlayerData player)
	{
		this.localPlayer = player;
	}

	/**
	 * Return the ID of the set caller.  Will be 0 or -1 if no one has called set.
	 *
	 * @return
	 */
	public int getSetCaller()
	{
		return setCallerId;
	}

	/**
	 * Returns a boolean indicating whether the last set called by a player was correct.
	 * 
	 * @return
	 */
	public boolean getSetCorrect()
	{
		return setCorrect;
	}
	
	/**
	 * Return the array of players.
	 *
	 * @return
	 */
	public List<PlayerData> getPlayers()
	{
		return players;
	}

	/**
	 * 
	 * Returns true if the player is allowed to select cards on this client.
	 *
	 * @return
	 */
	public boolean canSelectCards()
	{
		return (gameState == GameConstants.GAME_STATE_IDLE || 
				( gameState == GameConstants.GAME_STATE_SET_CALLED && setCallerId == localPlayer.getId() )); 
	}

	/**
	 * Returns true if this client is allowed to call set.
	 *
	 * @return
	 */
	public boolean canCallSet()
	{
		return gameState == GameConstants.GAME_STATE_IDLE;
	}

	/**
	 * Returns true if the GUI is able to request a drawing of cards.
	 *
	 * @return
	 */
	public boolean canDrawCards()
	{
		// In general, we can draw if the deck permits us to, and there's room.
		boolean canDraw = deckSize > 0 && cardTableData.getCols() < 7;
		
		// In single player mode, we can draw in either the set state or the idle state.
		boolean singlePlayerDraw = (players.size() == 1 
			&& (gameState == GameConstants.GAME_STATE_IDLE
			 || gameState == GameConstants.GAME_STATE_SET_CALLED));
		
		// In multi player mode, we can draw only in the idle state.
		boolean multiPlayerDraw = gameState == GameConstants.GAME_STATE_IDLE;
		
		return canDraw && (multiPlayerDraw || singlePlayerDraw);
	}

	/**
	 * 
	 * Receive server data and convert it back into reasonable data.
	 *
	 * @param data - An XML Node containing the model information.
	 */
	@SuppressWarnings("rawtypes")
	public void update(Element modelElement)
	{
		// Get the players.
		players = new Vector<PlayerData>();
		Element playersElement = modelElement.element("players");
		Iterator iterator = playersElement.elements("player").iterator();
		while(iterator.hasNext())
		{
			Element playerElement = (Element)iterator.next();
			PlayerData newPlayer = new PlayerData(playerElement);
			
			players.add(newPlayer);
			
			// Don't forget to update the local player.
			if(newPlayer.getId() == localPlayer.getId())
			{
				localPlayer = newPlayer;
			}
		}
		
		// Get the deck size.
		String deckText = modelElement.element("deck").getText();
		deckSize = Integer.parseInt(deckText);
		
		// Get the game state
		String gameStateText = modelElement.element("gamestate").attributeValue("state", "1");
		gameState = Integer.parseInt(gameStateText);
		
		// Get the id of the player that called set, if appropriate.
		if(gameState == GameConstants.GAME_STATE_SET_CALLED ||
			gameState == GameConstants.GAME_STATE_SET_FINISHED)
		{
			String setCallerIdString = modelElement.element("gamestate").element("setcaller").getText();
			setCallerId = Integer.parseInt(setCallerIdString);
			
			if (gameState == GameConstants.GAME_STATE_SET_FINISHED)
			{
				String setCorrectString = modelElement.element("gamestate").element("setcorrect").getText();
				setCorrect = new Boolean(setCorrectString).booleanValue();
			}
		}
		else
		{
			setCallerId = -1;
		}
		
		// Get the card table.
		Element cardTableElement = modelElement.element("cardtable");
		cardTableData = new CardTableData(cardTableElement);
		
		setChanged();
		notifyObservers();
	}

	/**
	 * Destroy the model
	 *
	 */
	public void destroy()
	{
		players = null;
		localPlayer = null;
		cardTableData.destroy();
	}
}
