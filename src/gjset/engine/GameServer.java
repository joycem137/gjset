package gjset.engine;

import gjset.client.GameClient;
import gjset.client.gui.PlayerUI;
import gjset.data.Card;
import gjset.data.CardTable;
import gjset.data.Player;
import gjset.engine.gui.ServerConsole;
import gjset.engine.server.PlayerClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008-2009 Joyce Murton and Andrea Kilpatrick
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of gjSet are very grateful for
 *  them creating such an excellent card game.
 *  
 *  gjSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  gjSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with gjSet.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This class implements the {@link ClientLinkInterface} to provide the game engine with a link to all {@link PlayerUI}
 * objects running on remote systems.  It is intended to be used in conjunction with {@link GameClient} objects to complete the
 * interaction
 * <P>
 * When instantiated, this class opens a TCP/IP server listening on port 4337.
 * Once a client connects, all events in the game engine will be transmitted to the clients through this class.
 * <P>
 * This class also provides support for handling incoming messages from the UI to the engine, forwarding all incoming messages to the
 * linked {@link GameEngine} object.
 * 
 * @see PlayerUI
 * @see ClientLinkInterface
 */
public class GameServer implements ClientLinkInterface
{
	private static final int SERVER_PORT = 4337;
	
	private ServerConsole	console;
	
	private ServerSocket	server	= null;

	private PlayerClientHandler	playerClientHandler;

	private GameEngine	gc;

	/**
	 * 
	 * When executed, this constructor will create a new Server socket and start a new thread listening
	 * for incoming connections on that server.
	 *
	 */
	public GameServer()
	{
		//Create a console to post messages to.  This might be the command line or a debug interface or whatever we want.
		console = ServerConsole.getDefaultConsole();
		
		//Create the server socket.
		try
		{
			console.message("Setting up server connection");
			server = new ServerSocket(SERVER_PORT);
		} catch (IOException e)
		{
			console.errorMessage("Could not create server.");
			e.printStackTrace();
		}
		
		//Create the server's listening thread:
		Runnable runServer = new Runnable()
		{
			public void run()
			{
				try
				{
					console.message("Listening for connection.");
					
					//Listen for an incoming connection.
					Socket socket = server.accept();
					
					//When the above command returns, it will have a new client to deal with.  Handle it!
					handleNewClient(socket);
				} catch (IOException e)
				{
					console.errorMessage("Error while listening for connection.");
					e.printStackTrace();
				}
			}
		};
		
		//Start the server.
		new Thread(runServer).start();
	}
	
	/**
	 * This class provides additional functionality by listening to the client sockets
	 * and forwarding incoming messages from those clients to the game engine.
	 * <P>
	 * This method links the indicated {@link GameEngine} object to this object for this purpose.
	 *
	 * @param gc The GameEngine object to send client's messages to.
	 */
	public void linkGameController(GameEngine gc)
	{
		this.gc = gc;
	}
	
	/*
	 * There's been a new client created!  Do something with it's resultant socket so that we
	 * can communicate with that client.
	 */
	
	private void handleNewClient(Socket socket)
	{
		playerClientHandler = new PlayerClientHandler(socket, this, console);
	}

	/**
	 * 
	 * If a player selects three cards that are a set, this method is called to tell the UI to indicate that the selected
	 * cards are indeed a set.
	 *
	 * @see gjset.engine.ClientLinkInterface#confirmSet()
	 */
	public void confirmSet()
	{
		playerClientHandler.sendMessage("CONFIRM_SET");
	}

	/**
	 * 
	 * When there are no more cards to draw, and there are no more sets on the table, the game is over.
	 * Inform the UI of this fact.
	 *
	 * @see gjset.engine.ClientLinkInterface#displayEndOfGame()
	 */
	public void displayEndOfGame()
	{
		playerClientHandler.sendMessage("END_OF_GAME");
	}

	/**
	 * 
	 * Tell the player's UI to do whatever it needs to do to show that a new game has been started.
	 *
	 * @see gjset.engine.ClientLinkInterface#displayNewGame()
	 */
	public void displayNewGame()
	{
		playerClientHandler.sendMessage("NEW_GAME");
	}

	/**
	 * 
	 * If you have 21 cards on the table, you are mathematically guaranteed to have a set out there somewhere.
	 * This method is used if the player attempts to draw more cards when none are needed.
	 *
	 * @see gjset.engine.ClientLinkInterface#indicateNoNeedToDrawMoreCards()
	 */
	public void indicateNoNeedToDrawMoreCards()
	{
		playerClientHandler.sendMessage("NO_NEED_FOR_MORE_CARDS");
	}

	/**
	 * 
	 * If a player causes a situation to occur where more cards need to be drawn, and the deck is empty, send a message
	 * to the UI to indicate as much.
	 *
	 * @see gjset.engine.ClientLinkInterface#indicateOutOfCardsToDraw()
	 */
	public void indicateOutOfCardsToDraw()
	{
		playerClientHandler.sendMessage("OUT_OF_CARDS_TO_DRAW");
	}

	/**
	 * 
	 * If a player selects three cards that are not a set, this method is called to tell the UI to 
	 * indicate that the selected cards are not a set.
	 *
	 * @see gjset.engine.ClientLinkInterface#rejectSet()
	 */
	public void rejectSet()
	{
		playerClientHandler.sendMessage("REJECT_SET");
	}

	/**
	 * There has been a change on the player's information.  Update the UI with the appropriate changes.
	 *
	 * @param player
	 * @see gjset.engine.ClientLinkInterface#updatePlayer(gjset.data.Player)
	 */
	public void updatePlayer(Player player)
	{
		playerClientHandler.sendMessage("UPDATE_PLAYER:" + player.getRepresentation());
	}

	/**
	 * 
	 * Tell the player's UI that the card table has changed.  This method provides the data that will be transferred
	 * to the player/client. 
	 *
	 * @param table
	 * @see gjset.engine.ClientLinkInterface#updateTable(gjset.data.CardTable)
	 */
	public void updateTable(CardTable table)
	{
		//Put in the ID.
		String message = "UPDATE_TABLE:";
		
		//Generate the table representation
		message += table.getRepresentation() + ":";
		
		//Send it out!
		playerClientHandler.sendMessage(message);
	}

	/**
	 * 
	 * Indicates that the connected player has disconnected.  When we have more than one player, this
	 * method should have a parameter indicating which player disconnected.
	 *
	 */
	public void playerDisconnected()
	{
		//Nothing to do right now.
	}

	/**
	 * 
	 * Parse a message coming from the player and execute the appropriate action.
	 *
	 * @param line The message from the player.
	 */
	public void parseMessage(String line)
	{
		//Parse the strings.
		String messageArray[] = line.split(":");
		
		//React to the result.
		if(messageArray[0].equals("NO_MORE_SETS"))
		{
			gc.noMoreSets();
		}
		else if(messageArray[0].equals("QUIT_GAME"))
		{
			gc.quitGame();
		}
		else if(messageArray[0].equals("SELECT_CARD"))
		{
			gc.selectCard(Card.parseCard(messageArray[1]));
		}
		else if(messageArray[0].equals("NEW_GAME"))
		{
			gc.newGame();
		}
		else
		{
			console.errorMessage("Error.  Invalid message received.");
		}
	}
}
