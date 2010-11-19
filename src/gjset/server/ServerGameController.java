package gjset.server;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
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

import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

/**
 * This class handles all operations for the server.  Whenever something changes, all players are updated.
 */
public class ServerGameController implements ServerMessageHandler
{	
	private GameModel model;
	private DocumentFactory documentFactory;
	private GameServer server;
	
	/**
	 * Create a ServerGameController
	 * 
	 * This constructor instantiates and initializes all of the game model data.
	 * @param server 
	 *
	 */
	public ServerGameController(GameServer server)
	{
		this.server = server;
		server.addMessageHandler(this);
		
		model = new GameModel();
		documentFactory = DocumentFactory.getInstance();
	}

	/**
	 * Request more cards be drawn from the deck.
	 *
	 * @return An XML tag containing the results of the command
	 */
	private Element drawCards()
	{
		Element responseElement = documentFactory.createElement("commandresponse");
		boolean success = false;
		String failureReason = "";
		
		int gameState = model.getGameState();
		
		// If the game is not currently idle, do nothing.
		if (gameState == GameConstants.GAME_STATE_IDLE)
		{
			Deck deck = model.getDeck();
			CardTable cardTable = model.getCardTable();
			
			if (deck.getRemainingCards() < 3)
			{
				// If we're out of cards, do nothing.
				failureReason = "No cards remaining";
			}
			else if (cardTable.getNumCards() < CardTable.CARD_LIMIT)
			{
				// We *CAN* draw the cards! YAY!
				model.drawCards();
				
				success = true;
			}
			else
			{
				// There are plenty of cards on the table.  No need to draw more.
				failureReason = "Sufficient cards already on table";
			}
		}
		else
		{
			// The game is not in the correct state to draw cards.
			failureReason = "Game is not idle.";
		}
		
		// Create our response
		String resultText = "failed";
		if(success)
		{
			resultText = "success";
		}
		
		responseElement.addAttribute("result", resultText);
		
		Element reasonElement = documentFactory.createElement("reason");
		reasonElement.setText(failureReason);
		responseElement.add(reasonElement);
		
		return responseElement;
	}
	
	/**
	 * Handle a message incoming from the server.
	 */
	public void handleMessage(PlayerClientHandler client, Element message)
	{
		// Start by getting the player id.
		int playerId = client.getPlayerId();
		
		// Now see if this was a command.
		Element responseElement = null;
		Element commandElement = message.element("command");
		if(commandElement != null)
		{
			String commandType = commandElement.attributeValue("type", "");
			if(commandType.equals("drawcards"))
			{
				responseElement = drawCards();
			}
		}
		
		// See if we need to send a response to the player.
		if(responseElement != null)
		{
			// Add the original command so that the client can cross reference it, if necessary.
			responseElement.add(message.createCopy());
			
			// Send the result back to the player that sent it in.
			client.sendMessage(responseElement);
			
			// Check to see if we need to update all players with new data.
			String result = responseElement.attributeValue("result", "false");
			if(result.equals("success"))
			{
				updateAllPlayers();
			}
		}
	}

	/**
	 * send game updates to all of the players.
	 *
	 */
	private void updateAllPlayers()
	{
		// Start by building the update XML.
		Element gameUpdate = buildGameUpdate();
		
		List<PlayerClientHandler> clients = server.getClients();
		Iterator<PlayerClientHandler> iterator = clients.iterator();

		while(iterator.hasNext())
		{
			PlayerClientHandler client = iterator.next();
			client.sendMessage(gameUpdate.createCopy());
		}
		
	}

	/**
	 * TODO: Describe method
	 *
	 * @return
	 */
	private Element buildGameUpdate()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Start a new game and update the players with the relevant information.
	 *
	 */
	public void startNewGame()
	{
		model.startNewGame();
		
		updateAllPlayers();
	}
}
