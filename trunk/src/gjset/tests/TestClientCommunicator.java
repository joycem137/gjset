package gjset.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import gjset.GameConstants;
import gjset.client.ConcreteClientCommunicator;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class performs tests on the communicator.
 */
public class TestClientCommunicator
{
	private MockMessageHandler handler;
	private ConcreteClientCommunicator client;
	private MockServer server;
	
	/**
	 * Sets up the socket server that will be used to communicate with the client communicator.
	 */
	@Before
	public void setUp()
	{
		handler = new MockMessageHandler();
		client = new ConcreteClientCommunicator("127.0.0.1", GameConstants.GAME_PORT);
		client.addMessageHandler(handler);
		
		server = new MockServer(GameConstants.GAME_PORT);
		
		// Now connect everybody.
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
		if(server != null && client != null)
		{
			server.destroy();
			client.destroy();
			
			server = null;
			client = null;
			handler = null;
			
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
	 * Test that the communicator can send messages.
	 */
	@Test
	public void testSendMessage()
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
		
		Element messageReceived = server.getLastMessage();
		
		assertNotNull(messageReceived);
		
		Element mockMessage = wrapMessage(message.createCopy());

		NodeComparator comparator = new NodeComparator();
		assertEquals(0, comparator.compare(mockMessage, messageReceived));
	}
	
	/**
	 * 
	 * Verify that the communicator can receive messages.
	 *
	 */
	@Test
	public void testReceiveMessage()
	{
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		Element message = documentFactory.createElement("testingrecv");
		Element fullMessage = wrapMessage(message);
		
		server.sendMessage(fullMessage);
		
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			fail("Interuppted sleep");
		}
		
		// Verify that the client received the message.
		Element lastMessage = handler.getLastMessage();
		
		assertNotNull(lastMessage);
		
		NodeComparator comparator = new NodeComparator();
		
		assertEquals(0, comparator.compare(fullMessage, lastMessage));
	}
	
	/**
	 * Test that the real client communicator gets a player id when connecting to the real server.
	 */
	/*@Test
	public void testInitialization()
	{
		// Start by destroying everything we've worked so hard to accomplish!
		server.destroy();
		client.destroy();
		server = null;
		client = null;
		
		try
		{
			Thread.sleep(200);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		// Now build a new life!
		client = new ConcreteClientCommunicator("127.0.0.1", GameConstants.GAME_PORT);
		ConcreteClientGUIController controller = new ConcreteClientGUIController(client);
		
		GameServer server = new GameServer();
		
		// Now connect everybody.
		server.listenForClients();
		client.connectToServer();
		
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			fail("Interuppted sleep");
		}
		
		// Then make sure the client has the right id.
		assertTrue(controller.getPlayerId() > 0);
		
		server.destroy();
		client.destroy();
		controller.destroy();
		
		client = null;
		
		try
		{
			Thread.sleep(200);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}*/

	/**
	 * Wraps a message with enclosing tags and a comm version.
	 *
	 * @param messageElement
	 * @return
	 */
	private Element wrapMessage(Element messageElement)
	{	
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		
		Element rootElement = documentFactory.createElement("combocards");
		
		Element versionElement = documentFactory.createElement("version");
		versionElement.setText("1");
		rootElement.add(versionElement);
		
		rootElement.add(messageElement);
		
		return rootElement;
	}
}
