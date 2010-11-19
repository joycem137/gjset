package gjset.tests;


import static org.junit.Assert.assertNotNull;
import gjset.GameConstants;
import gjset.client.ConcreteClientCommunicator;
import gjset.client.ConcreteClientGUIController;
import gjset.server.GameServer;
import gjset.server.ServerGameController;

import org.dom4j.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class TestServerController
{
	private MockMessageHandler clientHandler;
	private MockServerMessageHandler serverHandler;
	
	private ConcreteClientCommunicator client;
	private ConcreteClientGUIController clientController;
	
	private GameServer server;
	private ServerGameController serverController;
	
	/**
	 * Sets up the socket server that will be used to communicate with the client communicator.
	 */
	@Before
	public void setUp()
	{
		// Create the server!
		server = new GameServer();
		
		// Add an extra message handler in there.
		serverHandler = new MockServerMessageHandler();
		server.addMessageHandler(serverHandler);
		
		serverController = new ServerGameController(server);
		
		// Start listening.
		server.listenForClients();
		
		// Create our basic client.
		client = new ConcreteClientCommunicator("127.0.0.1", GameConstants.GAME_PORT);
		
		// We're going to use the controller *AND* the client.  To see how this works.
		clientHandler = new MockMessageHandler();
		client.addMessageHandler(clientHandler);
		
		clientController = new ConcreteClientGUIController(client);
		
		// Now connect everybody.
		client.connectToServer();
		
		try
		{
			Thread.sleep(200);
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
			clientController.destroy();
			serverController.destroy();
			
			server = null;
			client = null;
			serverHandler = null;
			clientHandler = null;
			serverController = null;
			clientController = null;
			
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
	 * Verify that the client controller receives a game update upon connection.
	 */
	@Test
	public void testGameUpdateUponConnection()
	{
		// We should already be connected, so let's see if that update went through.
		Element lastMessage = clientHandler.getLastMessage();
		
		Element gameupdateElement = lastMessage.element("gameupdate");
		
		assertNotNull(gameupdateElement);
	}

}
