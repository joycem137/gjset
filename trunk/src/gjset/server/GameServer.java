package gjset.server;

import gjset.server.gui.ServerConsole;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Element;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of Combo Cards are very grateful for
 *  them creating such an excellent card game.
 *  
 *  Combo Cards is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Combo Cards is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with Combo Cards.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 */
public class GameServer
{
	private ServerConsole console;
	
	private ServerSocket serverSocket;

	private List<PlayerClientHandler> clients;
	private List<ServerMessageHandler> handlers;

	private Thread listeningThread;

	/**
	 * 
	 * Starts the server and prepares it to start listening for incoming connections.
	 * @param port 
	 *
	 */
	public GameServer(int port)
	{
		//Create a console to post messages to.  This might be the command line or a debug interface or whatever we want.
		console = ServerConsole.getDefaultConsole();
		
		clients = new Vector<PlayerClientHandler>();
		handlers = new Vector<ServerMessageHandler>();
		
		//Create the server socket.
		try
		{
			console.message("Setting up server connection");
			serverSocket = new ServerSocket(port);
		} catch (IOException e)
		{
			console.errorMessage("Could not create server.");
			e.printStackTrace();
		}
		
		// Create the server's listening thread:
		Runnable runServer = new Runnable()
		{
			public void run()
			{
				while(!serverSocket.isClosed())
				{
					try
					{
						console.message("Listening for connection.");
						
						// Listen for an incoming connection.
						Socket socket = serverSocket.accept();
						
						// When the above command returns, it will have a new client to deal with.  Handle it!
						handleNewClient(socket);
					} catch (IOException e)
					{
						console.errorMessage("Error while listening for connection.");
						e.printStackTrace();
					}
				}
			}
		};
		
		// Create the listening thread.
		listeningThread = new Thread(runServer);
	}
	
	/**
	 * 
	 * Starts the listening thread.
	 *
	 */
	public void listenForClients()
	{
		listeningThread.start();
	}
	
	/*
	 * There's been a new client created!  Do something with it's resultant socket so that we
	 * can communicate with that client.
	 */
	
	/**
	 * Return the full list of clients.
	 *
	 * @return
	 */
	public List<PlayerClientHandler> getClients()
	{
		return clients;
	}

	/**
	 * Send the indicated message to all message handlers attached to the server.
	 *
	 * @param current
	 */
	public void receiveMessage(PlayerClientHandler client, Element message)
	{
		List<ServerMessageHandler> handlersCopy = new Vector<ServerMessageHandler>(handlers);
		Iterator<ServerMessageHandler> iterator = handlersCopy.iterator();
		while(iterator.hasNext())
		{
			iterator.next().handleMessage(client, message);
		}
	}

	/**
	 * Destroy the server, shutting down all behaviors.
	 *
	 */
	public void destroy()
	{
		console.message("Destroying server");
		
		List<PlayerClientHandler> clientsCopy = new Vector<PlayerClientHandler>(clients);
		Iterator<PlayerClientHandler> iterator = clientsCopy.iterator();
		while(iterator.hasNext())
		{
			iterator.next().destroy();
		}
		
		clients = null;
		handlers = null;
		
		listeningThread.interrupt();
		try
		{
			serverSocket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Add a message handler to the server to receive incoming messages.
	 *
	 * @param handler
	 */
	public void addMessageHandler(ServerMessageHandler handler)
	{
		handlers.add(handler);
	}

	/**
	 * Tell every client to send this message.
	 *
	 * @param message
	 */
	public void sendMessage(Element message)
	{
		Iterator<PlayerClientHandler> iterator = clients.iterator();
		
		while(iterator.hasNext())
		{
			iterator.next().sendMessage(message.createCopy());
		}
	}

	/**
	 * Remove this client from the server.
	 *
	 * @param playerClientHandler
	 */
	public void removeClient(PlayerClientHandler client)
	{
		clients.remove(client);
	}

	/*
	 * There's been a new client created!  Do something with its resultant socket so that we
	 * can communicate with that client.
	 */
	private void handleNewClient(Socket socket)
	{	
		// Create the new client handler.
		PlayerClientHandler client = new PlayerClientHandler(socket, this, console);
		
		// Store it.
		clients.add(client);
		
		// Start the client listening.
		client.startListening();
	}
}
