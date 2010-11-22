package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
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
		
		client.connectToServer();
		
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
