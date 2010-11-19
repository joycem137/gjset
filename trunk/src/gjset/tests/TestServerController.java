package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gjset.GameConstants;
import gjset.client.ClientGUIModel;
import gjset.client.ConcreteClientCommunicator;
import gjset.client.ConcreteClientGUIController;
import gjset.data.CardTableData;
import gjset.server.GameModel;
import gjset.server.GameServer;
import gjset.server.ServerGameController;

import org.dom4j.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Exercise the server's controller to verify that its behavior is correct.
 */
public class TestServerController
{
	private MockMessageHandler clientHandler;
	
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
	
	/**
	 * Verify that starting a new game works correctly on the server.
	 */
	@Test
	public void testStartNewGame()
	{
		serverController.startNewGame();
		
		try
		{
			Thread.sleep(200);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		GameModel serverModel = serverController.getModel();
		CardTableData serverCardTable = serverModel.getCardTable();
		
		// Verify things on the server.
		assertEquals(69, serverModel.getDeck().getRemainingCards());
		assertEquals(12, serverCardTable.getNumCards());
		assertEquals(3, serverCardTable.getRows());
		assertEquals(4, serverCardTable.getCols());
	
		ClientGUIModel clientModel = clientController.getModel();
		CardTableData clientCardTable = clientModel.getCardTable();
		
		// Verify things on the client.
		assertEquals(69, clientModel.getCardsInDeck());
		assertEquals(12, clientCardTable.getNumCards());
		assertEquals(3, clientCardTable.getRows());
		assertEquals(4, clientCardTable.getCols());
	}
	
	/**
	 * Verify that we can draw cards from a new game.
	 */
	@Test
	public void testDrawingCards()
	{
		serverController.startNewGame();
		
		try
		{
			Thread.sleep(200);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		// Send the command from the client.
		clientController.drawMoreCards();
		
		try
		{
			Thread.sleep(400);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		// Verify the changes on the server.
		GameModel serverModel = serverController.getModel();
		CardTableData serverCardTable = serverModel.getCardTable();
		assertEquals(66, serverModel.getDeck().getRemainingCards());
		assertEquals(15, serverCardTable.getNumCards());
		assertEquals(3, serverCardTable.getRows());
		assertEquals(5, serverCardTable.getCols());
		
		// Verify the changes on the client.
		ClientGUIModel clientModel = clientController.getModel();
		CardTableData clientCardTable = clientModel.getCardTable();
		assertEquals(66, clientModel.getCardsInDeck());
		assertEquals(15, clientCardTable.getNumCards());
		assertEquals(3, clientCardTable.getRows());
		assertEquals(5, clientCardTable.getCols());
	}

}
