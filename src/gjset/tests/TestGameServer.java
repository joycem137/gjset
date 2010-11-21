package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import gjset.GameConstants;
import gjset.server.GameServer;
import gjset.tools.MessageUtils;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Verify that the game server is working correctly.
 */
public class TestGameServer
{
	private GameServer server;
	private MockClient client;
	private MockServerMessageHandler handler;
	
	/**
	 * Create the server, hook it up, and start listening to it.
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		server = new GameServer();
		
		// Add our mock message handler.
		handler = new MockServerMessageHandler();
		server.addMessageHandler(handler);
		
		// Create the client.
		client = new MockClient("127.0.0.1", GameConstants.GAME_PORT);
		
		// Start the threads up
		server.listenForClients();
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
		server.destroy();
		client.destroy();
		
		server = null;
		client = null;
		handler = null;
		
		try
		{
			Thread.sleep(300);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Try sending a message to the server and see if the handler gets it.
	 *
	 */
	@Test
	public void testSendMessageToServer()
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
		
		Element messageReceived = handler.getLastMessage();
		
		assertNotNull(messageReceived);
		
		Element mockMessage = MessageUtils.wrapMessage(message.createCopy());

		NodeComparator comparator = new NodeComparator();
		assertEquals(0, comparator.compare(mockMessage, messageReceived));
		
	}
	
	/**
	 * 
	 * Verify that the client receives messages from the server.
	 *
	 */
	@Test
	public void testReceiveMessageFromServer()
	{
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		Element message = documentFactory.createElement("testingrecv");
		
		server.sendMessage(message);
		
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			fail("Interuppted sleep");
		}
		
		// Verify that the client received the message.
		Element lastMessage = client.getLastMessage();
		
		assertNotNull(lastMessage);
		
		Element fullMessage = MessageUtils.wrapMessage(message);
		
		NodeComparator comparator = new NodeComparator();
		assertEquals(0, comparator.compare(fullMessage, lastMessage));
	}

}
