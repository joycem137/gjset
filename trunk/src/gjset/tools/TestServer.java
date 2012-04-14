package gjset.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  Set¨ is a registered trademark of Set Enterprises. 
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
public class TestServer
{
	private ServerSocket serverSocket;

	private List<TestClientHandler> clients;

	private Thread listeningThread;	

	private BufferedReader inputReader;
	private Thread keyboardThread;
	
	public TestServer(int port) {		
		clients = new Vector<TestClientHandler>();		
		
		//Create the server socket.
		try
		{
			System.out.println("Setting up server connection on port " + port);
			serverSocket = new ServerSocket(port);
		} catch (IOException e)
		{
			System.err.println("Could not create server.");
			e.printStackTrace();
		}
		
		createListeningThread();
		createKeyboardThread();
	}
	
	public void startListening() {
		listeningThread.start();
		keyboardThread.start();
	}

	/**
	 * Tell every client to send this message.
	 *
	 * @param message
	 */
	public void sendMessage(String message)
	{
		Iterator<TestClientHandler> iterator = clients.iterator();
		
		while(iterator.hasNext())
		{
			iterator.next().sendMessage(message);
		}
	}


	private void createListeningThread()
	{
		// Create the server's listening thread:
		Runnable runServer = new Runnable() {
			public void run() {
				while(!serverSocket.isClosed()) {
					try {
						System.out.println("Listening for connection.");
						
						// Listen for an incoming connection.
						Socket socket = serverSocket.accept();
						
						// When the above command returns, it will have a new client to deal with.  Handle it!
						handleNewClient(socket);
					} catch (IOException e) {
						System.err.println("Error while listening for connection. (Possibly because of shutdown)");
					}
				}
			}
		};
		
		// Create the listening thread.
		listeningThread = new Thread(runServer, "Server thread listening for clients");
	}

	/*
	 * There's been a new client created!  Do something with its resultant socket so that we
	 * can communicate with that client.
	 */
	private void handleNewClient(Socket socket)
	{	
		// Create the new client handler.
		TestClientHandler client = new TestClientHandler(socket, this);
		
		// Store it.
		clients.add(client);
		
		// Start the client listening.
		client.startListening();
	}
	
	private void createKeyboardThread() {
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		
		Runnable listenForInput = new Runnable() {
			public void run() {
				try {
					String textReceived = inputReader.readLine();
					while(textReceived != null)
					{
						System.out.println("Server sent: " + textReceived);
						
						sendMessage(textReceived);
						
						textReceived = inputReader.readLine();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		keyboardThread = new Thread(listenForInput, "Client Keyboard Thread");

	}

}
