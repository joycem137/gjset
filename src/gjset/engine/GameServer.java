package gjset.engine;

import gjset.data.Card;
import gjset.data.CardTable;
import gjset.data.Player;
import gjset.engine.gui.ServerConsole;
import gjset.engine.server.RemoteClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

public class GameServer implements ClientInterface
{
	private static final int SERVER_PORT = 4337;
	
	private ServerConsole	console;
	
	private ServerSocket	server	= null;

	private RemoteClientHandler	playerClientHandler;

	private GameController	gc;

	public GameServer()
	{
		console = ServerConsole.getDefaultConsole();
		
		//Create the server manager.
		try
		{
			console.message("Setting up server connection");
			server = new ServerSocket(SERVER_PORT);
		} catch (IOException e)
		{
			console.errorMessage("Could not create server.");
			e.printStackTrace();
		}
		
		//Start the server's listening thread:
		Runnable runServer = new Runnable()
		{
			public void run()
			{
				try
				{
					console.message("Listening for connection.");
					Socket socket = server.accept();
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
	
	public void linkGameController(GameController gc)
	{
		this.gc = gc;
	}
	
	private void handleNewClient(Socket socket)
	{
		playerClientHandler = new RemoteClientHandler(socket, this, console);
	}

	public void confirmSet()
	{
		playerClientHandler.sendMessage("CONFIRM_SET");
	}

	public void displayEndOfGame()
	{
		playerClientHandler.sendMessage("END_OF_GAME");
	}

	public void displayNewGame()
	{
		playerClientHandler.sendMessage("NEW_GAME");
	}

	public void indicateNoNeedToDrawMoreCards()
	{
		playerClientHandler.sendMessage("NO_NEED_FOR_MORE_CARDS");
	}

	public void indicateOutOfCardsToDraw()
	{
		playerClientHandler.sendMessage("OUT_OF_CARDS_TO_DRAW");
	}

	public void rejectSet()
	{
		playerClientHandler.sendMessage("REJECT_SET");
	}

	public void updatePlayer(Player player)
	{
		playerClientHandler.sendMessage("UPDATE_PLAYER:" + player.getRepresentation());
	}

	public void updateTable(CardTable table)
	{
		//Put in the ID.
		String message = "UPDATE_TABLE:";
		
		//Generate the table representation
		message += table.getRepresentation() + ":";
		
		//Send it out!
		playerClientHandler.sendMessage(message);
	}

	public void playerDisconnected()
	{
		//Nothing to do right now.
	}

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
