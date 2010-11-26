package gjset.client;

import java.io.IOException;

import gjset.tools.MessageHandler;

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
 * This class encapsulates the basic behavior of a communicator on the client side.
 */
public interface ClientCommunicator
{

	/**
	 * Provides a link to the game UI so that messages coming back from the server can be executed on the UI.
	 *
	 * @param gui The {@link MainGamePanel} object to forward incoming messages to.
	 */
	public abstract void addMessageHandler(MessageHandler gui);

	/**
	 * Remove the indicated message handler from the message handlers.
	 *
	 * @param gameInitiator
	 */
	public abstract void removeMessageHandler(MessageHandler handler);

	/**
	 * Send the indicated message over.
	 *
	 * @param root
	 */
	public abstract void sendMessage(Element messageElement);

	/**
	 * Destroy the indicated client.
	 *
	 */
	public abstract void destroy();

	/**
	 * Instruct the client to connect to the server.
	 * @throws IOException 
	 *
	 */
	public abstract void connectToServer() throws IOException;

}