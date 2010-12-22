package gjset.client;

import gjset.GameConstants;
import gjset.data.Card;
import gjset.data.PlayerData;
import gjset.tools.MessageHandler;

import java.util.Iterator;
import java.util.Vector;

import org.dom4j.DocumentFactory;
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
 * This controls the UI on the client side.  It takes messages from the communicator
 * and uses them to update the ClientGUIModel.  It also receives actions from the GUI
 * and sends the appropriate commands to the server.
 */
public class ConcreteClientGUIController implements ClientGUIController, MessageHandler
{
	private DocumentFactory documentFactory;
	private ClientGUIModel	model;
	private ClientCommunicator client;
	private Vector<Runnable> gameStartTriggers;
	
	/**
	 * Construct a controller for the indicated player connected to the indicated model.
	 *
	 * @param client The client for communicating with the server.
	 * @param localPlayer the Player object that represents the local player.
	 */
	public ConcreteClientGUIController(ClientCommunicator client, PlayerData localPlayer)
	{
		documentFactory = DocumentFactory.getInstance();
		model = new ClientGUIModel();
		model.setLocalPlayer(localPlayer);
		
		gameStartTriggers = new Vector<Runnable>();
		
		this.client = client;
		client.addMessageHandler(this);
	}

	/**
	 * Returns the model that this controller is using.
	 *
	 * @return
	 * @see gjset.client.ClientGUIController#getModel()
	 */
	public ClientGUIModel getModel()
	{
		return model;
	}

	/**
	 * Request that the server start a new game.
	 *
	 * @see gjset.client.ClientGUIController#startNewGame()
	 */
	public void startNewGame()
	{
		Element root = documentFactory.createElement("command");
		root.addAttribute("type", "startgame");
		client.sendMessage(root);
	}

	/**
	 * Initiates a request to call set from this client.
	 *
	 * @see gjset.client.ClientGUIController#callSet()
	 */
	public void callSet()
	{
		Element root = documentFactory.createElement("command");
		root.addAttribute("type", "callset");
		client.sendMessage(root);
	}

	/**
	 * Initiates a request to draw more cards from this client.
	 *
	 * @see gjset.client.ClientGUIController#drawMoreCards()
	 */
	public void drawMoreCards()
	{
		Element root = documentFactory.createElement("command");
		root.addAttribute("type", "drawcards");
		client.sendMessage(root);
	}

	/**
	 * Initiates a request to select a given card. Note that this may cause the game to declare a set.
	 *
	 * @param cardData
	 * @see gjset.client.ClientGUIController#selectCard(gjset.data.Card)
	 */
	public void selectCard(Card cardData)
	{
		if(model.canSelectCards())
		{
			Element root = documentFactory.createElement("command");
			root.addAttribute("type", "selectcard");
			root.add(cardData.getXMLRepresentation());
			
			client.sendMessage(root);
		}
	}
	
	/**
	 * Send a message to the server indicating that this player is disconnecting.
	 *
	 * @see gjset.client.ClientGUIController#disconnectPlayer()
	 */
	public void disconnectPlayer()
	{
		Element root = documentFactory.createElement("command");
		root.addAttribute("type", "dropout");
		
		client.sendMessage(root);
	}

	/**
	 * 
	 * Parse an incoming message from the server.
	 *
	 * @param root
	 */
	public void handleMessage(Element root)
	{
		Element gameUpdateElement = root.element("gameupdate"); 
		
		if (gameUpdateElement != null)
		{
			// Start by triggering any game start events, if appropriate to do so.
			String gameStateString = gameUpdateElement.element("gamestate").attributeValue("state", "-1");
			int gameState = Integer.parseInt(gameStateString);
			
			int currentGameState = model.getGameState();
			
			if(gameState != currentGameState 
					&& ( currentGameState == GameConstants.GAME_STATE_NOT_STARTED 
					|| currentGameState == GameConstants.GAME_STATE_GAME_OVER) )
			{
				triggerGameStart();
			}
			
			// Update the model.
			model.update(gameUpdateElement);
		}
	}

	/**
	 * Handle a connection error from the client.
	 *
	 * @param e
	 * @see gjset.tools.MessageHandler#handleConnectionError(java.lang.Exception)
	 */
	public void handleConnectionError(Exception e)
	{
		// Nothing to do at this time.
	}

	/**
	 * Add an event to fire when the game starts.
	 *
	 * @param runnable
	 * @see gjset.client.ClientGUIController#addGameStartTrigger(java.lang.Runnable)
	 */
	public void addGameStartTrigger(Runnable trigger)
	{
		gameStartTriggers.add(trigger);
	}

	/**
	 * Destroy this controller.
	 *
	 */
	public void destroy()
	{
		model.destroy();
		client.destroy();
		
		model = null;
		client = null;
		documentFactory = null;
	}

	/**
	 * Trigger the game start triggers and then quit.
	 *
	 */
	private void triggerGameStart()
	{
		Iterator<Runnable> iterator = gameStartTriggers.iterator();
		while(iterator.hasNext())
		{
			iterator.next().run();
		}
		
		// Consume all of the triggers, now that they have been fired.
		gameStartTriggers.removeAllElements();
	}

}
