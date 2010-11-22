package gjset.client;

import gjset.data.Card;
import gjset.data.Player;
import gjset.tools.MessageHandler;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

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

/**
 *
 */
public class ConcreteClientGUIController implements ClientGUIController, MessageHandler
{
	DocumentFactory documentFactory;
	private ClientGUIModel	model;
	private ClientCommunicator client;
	
	/**
	 * Construct a controller for the indicated player connected to the indicated model.
	 *
	 * @param client The client for communicating with the server.
	 * @param localPlayer the Player object that represents the local player.
	 */
	public ConcreteClientGUIController(ClientCommunicator client, Player localPlayer)
	{
		documentFactory = DocumentFactory.getInstance();
		model = new ClientGUIModel();
		model.setLocalPlayer(localPlayer);
		
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
		root.addAttribute("type", "newgame");
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
	 * 
	 * Parse an incoming message from the server.
	 *
	 * @param root
	 */
	public void handleMessage(Element root)
	{
		if (root.element("gameupdate") != null)
		{
			Element updateElement = root.element("gameupdate");
			model.update(updateElement);
		}
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

}
