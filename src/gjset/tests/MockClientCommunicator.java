package gjset.tests;

import org.dom4j.Element;

import gjset.client.ClientCommunicator;
import gjset.tools.MessageHandler;

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
 *
 */
public class MockClientCommunicator implements ClientCommunicator
{
	private MessageHandler handler;
	private Element message;

	/**
	 * Grab the message handler.
	 *
	 * @param gui
	 * @see gjset.client.ClientCommunicator#addMessageHandler(gjset.tools.MessageHandler)
	 */
	public void addMessageHandler(MessageHandler handler)
	{
		this.handler = handler;
	}

	/**
	 * Pretend to send the indicated message.
	 *
	 * @param messageElement
	 * @see gjset.client.ClientCommunicator#sendMessage(org.dom4j.Element)
	 */
	public void sendMessage(Element message)
	{
		this.message = message;
	}

	/**
	 * returns the message handler assigned to this client.
	 *
	 * @return
	 */
	public MessageHandler getHandler()
	{
		return handler;
	}

	/**
	 * Returns the last mesasge sent using the sendMessage method.
	 *
	 * @return
	 */
	public Element getLastMessage()
	{
		return message;
	}

	/**
	 * Pretend to send the indicated message to the message handler.
	 *
	 * @param updateMessage
	 */
	public void injectMessage(Element updateMessage)
	{
		handler.handleMessage(updateMessage);
	}

	/**
	 * Remove this object.
	 *
	 * @see gjset.client.ClientCommunicator#destroy()
	 */
	public void destroy()
	{
		// Nothing to do.
	}

	/**
	 * Removes the indicated message handler.  Nothing to do here.
	 *
	 * @param handler
	 * @see gjset.client.ClientCommunicator#removeMessageHandler(gjset.tools.MessageHandler)
	 */
	public void removeMessageHandler(MessageHandler handler)
	{
		// Nothing to do.
	}

	/**
	 * connect to the serer.
	 *
	 * @see gjset.client.ClientCommunicator#connectToServer()
	 */
	public void connectToServer()
	{
		// Nothing to do.
	}

}
