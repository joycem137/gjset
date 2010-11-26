package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import gjset.GameConstants;
import gjset.client.ConcreteClientCommunicator;
import gjset.server.GameServer;
import gjset.tools.MessageUtils;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
public class TestRealClientServerComm
{
	private MockMessageHandler clientHandler;
	private MockServerMessageHandler serverHandler;
	
	private ConcreteClientCommunicator client;
	private GameServer server;
	
	/**
	 * Sets up the socket server that will be used to communicate with the client communicator.
	 */
	@Before
	public void setUp()
	{
		// Create the server!
		server = new GameServer(GameConstants.GAME_PORT);
		
		serverHandler = new MockServerMessageHandler();
		server.addMessageHandler(serverHandler);
		
		// Now connect everybody.
		server.listenForClients();
		
		// Create our basic client.
		client = new ConcreteClientCommunicator("127.0.0.1", GameConstants.GAME_PORT);
		
		// We're going to use the just the client.  To see how this works.
		clientHandler = new MockMessageHandler();
		client.addMessageHandler(clientHandler);
		
		try
		{
			client.connectToServer();
		} catch (IOException e1)
		{
			org.junit.Assert.fail("Failed to connect ot server.");
		}
		
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Destroy the server.
	 */
	@After
	public void tearDown()
	{
		if(server != null && client != null)
		{
			server.destroy();
			client.destroy();
			
			server = null;
			client = null;
			serverHandler = null;
			clientHandler = null;
			
			try
			{
				Thread.sleep(200);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Verify that the client can send messages to the server.
	 */
	@Test
	public void testMessageClientToServer()
	{
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		Element message = documentFactory.createElement("testingsend");
		client.sendMessage(message);
		
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			fail("Sleep interrupted.");
		}
		
		Element messageReceived = serverHandler.getLastMessage();
		
		assertNotNull(messageReceived);
		
		Element mockMessage = MessageUtils.wrapMessage(message.createCopy());

		NodeComparator comparator = new NodeComparator();
		assertEquals(0, comparator.compare(mockMessage, messageReceived));
	}
	
	/**
	 * 
	 * Verify that the server can send messages to the client.
	 *
	 */
	@Test
	public void testMessageServerToClient()
	{
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		Element message = documentFactory.createElement("testingrecv");
		server.sendMessage(message);
		
		try
		{
			Thread.sleep(400);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			fail("Interuppted sleep");
		}
		
		// Verify that the client received the message.
		Element lastMessage = clientHandler.getLastMessage();
		
		assertNotNull(lastMessage);
		
		Element fullMessage = MessageUtils.wrapMessage(message);
		
		NodeComparator comparator = new NodeComparator();
		assertEquals(0, comparator.compare(fullMessage, lastMessage));
	}
}
