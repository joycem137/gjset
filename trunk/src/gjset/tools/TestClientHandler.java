package gjset.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

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
public class TestClientHandler
{
	private TestServer	server;
	
	private Socket	socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	private Thread listeningThread;
	private int clientId;
	
	public TestClientHandler(Socket socket, TestServer testServer) {
		this.socket = socket;
		this.server = server;
		this.clientId =(int)(Math.random() * 10000);
		
		System.out.println("Creating client handler");
		
		//Get our I/O streams.
		try {	
			createIOStreams();
		} catch (IOException e)
		{
			System.err.println(clientId + ": Could not obtain I/O streams for client socket.");
			e.printStackTrace();
		}
	}

	public void startListening() {
		System.out.println(clientId + ": Starting to listen to client");
		
		// Start listening.
		listeningThread.start();
		
		// Now that we're listening, initialize the other end.
		sendMessage("Hello!");
	}

	/**
	 * TODO: Describe method
	 *
	 * @param message
	 */
	public void sendMessage(String message) {		
		writer.println(message);
		writer.flush();
	}

	//Used to create the Input/Output stream handling tools for a newly created socket.
	private void createIOStreams() throws IOException
	{
		try
		{
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		Runnable listenForMessage = new Runnable()
		{
			public void run()
			{
				try
				{
					String textReceived = reader.readLine();
					while(socket.isConnected() && textReceived != null)
					{
						System.out.println(clientId + ": Recvd: " + textReceived);
						
						// Then go looking for the next message.
						textReceived = reader.readLine();		
					}
					
//					destroy();
				} catch (IOException e)
				{
					System.err.println(clientId + ": IO Exception reading input in client handler. (Possibly because of closed socket.)");
					//e.printStackTrace();
				}
			}
		};
		
		listeningThread = new Thread(listenForMessage, "Client Handler Thread" + clientId);
	}

}
