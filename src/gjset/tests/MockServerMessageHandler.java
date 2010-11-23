package gjset.tests;

import org.dom4j.Element;

import gjset.server.PlayerClientHandler;
import gjset.server.ServerMessageHandler;

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
 * Pretend to be your ordinary message handler for the server.
 */
public class MockServerMessageHandler implements ServerMessageHandler
{
	private int lastClientId;
	private Element lastMessage;

	/**
	 * Store the message so that we can find out later that this message got sent.
	 *
	 * @param client
	 * @param message
	 * @see gjset.server.ServerMessageHandler#handleMessage(gjset.server.PlayerClientHandler, org.dom4j.Element)
	 */
	public void handleMessage(PlayerClientHandler client, Element message)
	{
		this.lastClientId = client.getPlayerId();
		this.lastMessage = message;
	}

	/**
	 * Return the last message we received.
	 *
	 * @return
	 */
	public Element getLastMessage()
	{
		return lastMessage;
	}

	/**
	 * Return the last client id we received a message from.
	 *
	 * @return
	 */
	public int getLastClientId()
	{
		return lastClientId;
	}

	/**
	 * Handle a new client.
	 *
	 * @param client
	 * @see gjset.server.ServerMessageHandler#handleNewClient(gjset.server.PlayerClientHandler)
	 */
	public void handleNewClient(PlayerClientHandler client)
	{
		// Do nothing.
	}

}
