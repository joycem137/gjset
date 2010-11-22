package gjset.client;

import gjset.GameConstants;
import gjset.data.Player;
import gjset.tools.MessageHandler;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

/**
 * This class handles all of the communication with the server that happens before the game starts.
 * Primarily, it performs the following tasks:
 * 1) Connect the client to the server.
 * 2) Verify communication compatibility
 * 3) Request a new player object from the indicated username.
 * 4) Build a client side controller to run the game.
 * 5) Load the Play Game Page to play the game.
 */
public class GameInitiator implements MessageHandler
{
	private ConcreteClientCommunicator client;
	private String username;
	private ClientGUIController controller;
	private DocumentFactory documentFactory;
	private boolean readyToStart;
	
	private GameInitiationHandler gameInitiationHandler;

	/**
	 * Create the game initiator.  This will cause the client to connect to the server.  Once the
	 * connection is complete, the initiator will verify communication parameters, register a player,
	 * and start the game.
	 *
	 * @param client
	 * @param username
	 * @param gameInitiationHandler
	 */
	public GameInitiator(ConcreteClientCommunicator client, String username, GameInitiationHandler gameInitiationHandler)
	{
		this.client = client;
		this.username = username;
		
		this.gameInitiationHandler = gameInitiationHandler;
		
		readyToStart = false;
		controller = null;
		
		documentFactory = DocumentFactory.getInstance();
		
		// Add ourselves as a message handler.
		client.addMessageHandler(this);
		
		// And now connect the client!
		client.connectToServer();
	}

	/**
	 * Indicate that this client is ready to start.
	 *
	 */
	public void indicateReadyToStart()
	{
		if(controller != null)
		{
			// We've already gotten configured!  Let's start playing!
			sendNewGameRequest();
		}
		else
		{
			// We aren't configured yet.  Indicate that we're ready to start whenever we are.
			readyToStart = true;
		}
		
	}
	
	/**
	 * Handle incoming messages from the server.
	 *
	 * @param message
	 * @see gjset.tools.MessageHandler#handleMessage(org.dom4j.Element)
	 */
	public void handleMessage(Element root)
	{
		if(root.element("init") != null)
		{
			handleInitialization(root);
		}
		else if (root.element("newplayer") != null)
		{
			handleNewPlayer(root.element("newplayer"));
		}
		else if(root.element("gameupdate") != null)
		{
			handleGameUpdate(root.element("gameupdate"));
		}
	}

	/**
	 * Handle a game update
	 *
	 */
	private void handleGameUpdate(Element updateElement)
	{
		System.out.println("Handling gameUpdate");
		
		// Get the game state.
		String gameStateString = updateElement.element("gamestate").getText();
		int gameState = Integer.parseInt(gameStateString);
		
		// See if the game has started.
		if(gameState != GameConstants.GAME_STATE_NOT_STARTED)
		{
			handleGameStarted();
		}
	}

	/**
	 * Handle the fact that the game just started.
	 *
	 */
	private void handleGameStarted()
	{
		System.out.println("handle game started");
		
		gameInitiationHandler.onGameInitiated(controller);
		
		// Then destroy myself.
		destroy();
	}

	/**
	 * Handle the new player data coming down.
	 *
	 * @param element
	 */
	private void handleNewPlayer(Element newPlayerElement)
	{
		System.out.println("Handling a new player");
		Element playerElement = newPlayerElement.element("player");
		Player player = new Player(playerElement);
		
		// Then create the controller.
		controller = new ConcreteClientGUIController(client, player);
		
		// If we're good to go, start the game!
		if(readyToStart)
		{
			sendNewGameRequest();
		}
	}

	/**
	 * Handle the verification stage of this process.
	 *
	 * @param root
	 */
	private void handleInitialization(Element root)
	{
		// First do verification
		String protocolVersion = root.element("version").getText();
		
		if(protocolVersion.equals(GameConstants.COMM_VERSION))
		{
			// If everything checks out, request a new player.
			Element newPlayerRequest = documentFactory.createElement("command");
			newPlayerRequest.addAttribute("type", "joinasplayer");
			
			Element usernameElement = documentFactory.createElement("username");
			usernameElement.setText(username);
			newPlayerRequest.add(usernameElement);
			
			client.sendMessage(newPlayerRequest);
		}
		else
		{
			// The protocol version is wrong.  Kill ourselves and shut down the client.
			destroy();
			client.destroy();
		}
	}

	/**
	 * Request that the server start a new game.
	 *
	 */
	private void sendNewGameRequest()
	{
		System.out.println("Handling a new game request from the server");
		
		Element commandElement = documentFactory.createElement("command");
		commandElement.addAttribute("type", "startgame");
		
		client.sendMessage(commandElement);
	}

	/**
	 * Destroy myself.
	 *
	 */
	private void destroy()
	{
		client.removeMessageHandler(this);
		gameInitiationHandler = null;
	}

}
