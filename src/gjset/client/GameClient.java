package gjset.client;

import gjset.client.gui.CardComponent;
import gjset.client.gui.MainGamePanel;
import gjset.data.CardTable;
import gjset.data.Player;
import gjset.engine.GameEngine;
import gjset.engine.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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
 * This class implements the {@link EngineLinkInterface} to provide a player/client UI with a link to a {@link GameEngine}
 * object running a remote system.  It is intended to be used in conjunction with a {@link GameServer} object to complete the
 * interaction
 * <P>
 * When the {@link #connectToServer} method is called, this class opens a TCP/IP connection to a TCP/IP server hosting the game engine.
 * Subsequent UI actions on the part of the user will be transmitted to the engine through this class.
 * <P>
 * This class also provides support for handling incoming messages from the engine to the UI, forwarding all incoming messages to the
 * linked {@link MainGamePanel} object.
 * 
 * @see GameEngine
 * @see EngineLinkInterface
 */
public class GameClient implements EngineLinkInterface
{
	//Stores a link to the UI.
	private MainGamePanel	gui;
	
	//Stores the socket to connect to the server.
	private Socket	socket;
	
	//Tools to read/write to the socket's I/O stream
	private PrintWriter	writer;
	private BufferedReader	reader;

	/**
	 * Blank constructor to assert that nothing is done on object instantiation.
	 */
	public GameClient()
	{
	}
	
	/**
	 * Provides a link to the game UI so that messages coming back from the server can be executed on the UI.
	 *
	 * @param gui The {@link MainGamePanel} object to forward incoming messages to.
	 */
	public void linkGUI(MainGamePanel gui)
	{
		this.gui = gui;
	}
	
	/**
	 * Establishes a connection to the game server using the given hostname and port.
	 * <P>
	 * This method also kicks off a new listening thread to read incoming messages from the game server.
	 *
	 * @param hostname A {@link String} containing the IP Address or hostname of the server.
	 * @param port An <code>int</code> containing the port number of the server to connect to.
	 */
	public void connectToServer(String hostname, int port)
	{
		try
		{
			//Create our address to connect to.
			SocketAddress socketAddress = new InetSocketAddress(hostname, port);
			
			socket = new Socket();
			
			//Attempt to connect to the server.
			socket.connect(socketAddress);
			
			//Get our I/O streams squared away once we're connected.
			createIOStreams();
			
			startListeningThread();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Used to listen to start listening to messages incoming from the server.
	private void startListeningThread()
	{
		Thread listeningThread = new Thread(new Runnable()
		{
			public void run()
			{
				while(socket.isConnected())
				{
					try
					{
						//Read a line
						String line = reader.readLine();
						
						/* 
						 * Then deal with it.  Note that the handling of messages takes place in this thread,
						 * So message handling should be snappy.  If a message requires a lot of processing,
						 * it should be started in a separate thread.
						 * 
						 * Either that, or we should update this function to kick off a new thread whenever
						 * we get a message.  But that would require more complex thread management.
						 */
						parseMessage(line);
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		
		//Of course, what would be the fun of creating a thread if you didn't make it do anything?
		listeningThread.start();
	}

	//Used to create the Input/Output stream handling tools for a newly created socket.
	private void createIOStreams() throws IOException
	{
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	//Sends the indicated message to the server.
	private void sendMessage(String message)
	{
		writer.println(message);
		writer.flush();
	}

	/**
	 * Parses incoming messages from the server and deals with them.
	 *
	 * @param line The newline terminated incoming message.
	 */
	protected void parseMessage(String line)
	{
		//Split the line into fields.
		String messageArray[] = line.split(":");
		
		//The first field is an ID field indicating what type of message this is.  Check it out.
		if(messageArray[0].equals("CONFIRM_SET"))
		{
			// Display a message indicating that this is a set.
			gui.getMessageBar().displayMessage("That's a set!");
		}
		else if(messageArray[0].equals("END_OF_GAME"))
		{
			//Hide the player panel, since the game is over.
			gui.hidePlayerPanel();
			
			//Display victory message.
			gui.getMessageBar().displayMessage("No sets remain.  YOU WIN!");
		}
		else if(messageArray[0].equals("NEW_GAME"))
		{
			// Remove any cards that were previously on the table.
			gui.getCardTable().reset();

			// Show a message indicating that the game has begun.
			gui.getMessageBar().displayMessage("Welcome to gjSet!");

			// Show the player panel
			gui.showPlayerPanel();
		}
		else if(messageArray[0].equals("NO_NEED_FOR_MORE_CARDS"))
		{
			gui.getMessageBar().displayMessage("You don't need to draw more cards.");
		}
		else if(messageArray[0].equals("OUT_OF_CARDS_TO_DRAW"))
		{
			gui.getMessageBar().displayMessage("There are no more cards to draw.");
		}
		else if(messageArray[0].equals("REJECT_SET"))
		{
			// Display a message on the gui.
			gui.getMessageBar().displayMessage("That's not a set!");
		}
		else if(messageArray[0].equals("UPDATE_TABLE"))
		{
			//The server has sent us an updated table.  The second parameter is a representation of the new table.
			CardTable table = CardTable.parseTable(messageArray[1]);
			
			//Now go and update our table!
			gui.getCardTable().update(table);
		}
		else if(messageArray[0].equals("UPDATE_PLAYER"))
		{
			//There's been an update to the player.  Parse the second parameter to create some new player data.
			Player player = Player.parsePlayer(messageArray);
			
			//And now, update the player's data.
			gui.getPlayer().drawPanel(player);
		}
	}

	/**
	 * Used by the client when the player selects the "No more sets" button.
	 * This indicates that the player thinks there are no more sets on the board
	 * and that the engine should react appropriately.
	 *
	 * @see gjset.client.EngineLinkInterface#callNoMoreSets()
	 */
	public void callNoMoreSets()
	{
		sendMessage("NO_MORE_SETS");
	}

	/**
	 * Tells the engine that the player wishes to end this game.
	 * <P>
	 * At this time, this is a simple method to indicate that a player is quitting the game.
	 * As multiple players are introduced, this method will be scrapped in favor of a method of
	 * detecting dropped player and similar issues.
	 *
	 * @see gjset.client.EngineLinkInterface#quitGame()
	 */
	public void quitGame()
	{
		sendMessage("QUIT_GAME");
	}

	/**
	 * Tells the engine to select the card represented by the on screen {@link CardComponent} object.
	 *
	 * @param card The card that was selected by this player/client.
	 * @see gjset.client.EngineLinkInterface#selectCard(gjset.client.gui.CardComponent)
	 */
	public void selectCard(CardComponent card)
	{
		//Set the outgoing message ID
		String message = "SELECT_CARD:";
		
		//Add the card's data
		message += card.getCard().getRepresentation();
		
		//Send it out!
		sendMessage(message);
	}

	/**
	 * Tells the engine to start a new game.
	 * <P>
	 * At this time, this is all that needs to take place.  This will be overwritten in the future as
	 * starting new games becomes more complex.
	 *
	 * @see gjset.client.EngineLinkInterface#startNewGame()
	 */
	public void startNewGame()
	{
		sendMessage("NEW_GAME");
	}

}
