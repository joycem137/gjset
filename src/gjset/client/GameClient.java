package gjset.client;

import gjset.client.gui.CardComponent;
import gjset.client.gui.GjSetGUI;
import gjset.data.CardTable;
import gjset.data.Player;

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
 *  gjSet is Copyright 2008-2009 Joyce Murton
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

public class GameClient implements EngineInterface
{
	private GjSetGUI	gui;
	private Socket	socket;
	private PrintWriter	writer;
	private BufferedReader	reader;

	public GameClient()
	{
	}
	
	public void linkGUI(GjSetGUI gui)
	{
		this.gui = gui;
	}
	
	public void connectToServer(String hostname, int port)
	{
		try
		{
			SocketAddress socketAddress = new InetSocketAddress(hostname, port);
			socket = new Socket();
			socket.connect(socketAddress);
			
			//Get our I/O streams.
			createIOStreams();
			
			startListeningThread();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
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
						String line = reader.readLine();
						parseMessage(line);
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		
		listeningThread.start();
	}

	private void createIOStreams() throws IOException
	{
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	private void sendMessage(String message)
	{
		writer.println(message);
		writer.flush();
	}

	protected void parseMessage(String line)
	{
		//Split the line into fields.
		String messageArray[] = line.split(":");
		
		if(messageArray[0].equals("CONFIRM_SET"))
		{
			// Display a message indicating that this is a set.
			gui.getMessageBar().displayMessage("That's a set!");
		}
		else if(messageArray[0].equals("END_OF_GAME"))
		{
			gui.hidePlayerPanel();
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
			CardTable table = CardTable.parseTable(messageArray[1]);
			gui.getCardTable().update(table);
		}
		else if(messageArray[0].equals("UPDATE_PLAYER"))
		{
			Player player = Player.parsePlayer(messageArray);
			gui.getPlayer().drawPanel(player);
		}
	}

	//Events to send to the engine.
	public void callNoMoreSets()
	{
		sendMessage("NO_MORE_SETS");
	}

	public void quitGame()
	{
		sendMessage("QUIT_GAME");
	}

	public void selectCard(CardComponent card)
	{
		//Set the ID
		String message = "SELECT_CARD:";
		
		//Add the card's data
		message += card.getCard().getRepresentation();
		
		//Send it out!
		sendMessage(message);
	}

	public void startNewGame()
	{
		sendMessage("NEW_GAME");
	}

}
