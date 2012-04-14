package gjset.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
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
public class TestClient
{	
	//Stores the socket to connect to the server.
	private Socket	socket;
	
	//Tools to read/write to the socket's I/O stream
	private PrintWriter writer;
	private BufferedReader reader;

	private Thread listeningThread;
	
	private BufferedReader inputReader;
	private Thread keyboardThread;

	private int connectionTimeout;

	private String hostname;

	private int port;

	public TestClient(String hostname, int port) {
		connectionTimeout = Integer.parseInt(GlobalProperties.properties.getProperty("server.timeout.connect", "5000"));
		
		this.hostname = hostname;
		this.port = port;
	}
	
	public void connect() {
		System.out.println("Connecting to server " + hostname + ":" + port);
		
		//Create our address to connect to.
		InetSocketAddress socketAddress = new InetSocketAddress(hostname, port);
		
		socket = new Socket();
		
		//Attempt to connect to the server.
		try {
			socket.connect(socketAddress, connectionTimeout);
		
			//Get our I/O streams squared away once we're connected.
			createIOStreams();
			createKeyboardThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// And then start listening
		listeningThread.start();
		keyboardThread.start();
	}

	//Used to create the Input/Output stream handling tools for a newly created socket.
	private void createIOStreams() throws IOException
	{
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		Runnable listenForMessage = new Runnable()
		{
			public void run() {
				try {
					String textReceived = reader.readLine();
					while(socket.isConnected() && textReceived != null) {
						System.out.println("Client recv: " + textReceived);
						
						// Then go looking for the next message.
						textReceived = reader.readLine();		
					}
				} catch (IOException e) {
					System.err.println("IO Exception reading input in client. (Possibly because of closed socket.)");
				}
			}
		};
		
		listeningThread = new Thread(listenForMessage, "Client Listening Thread");
	}
	
	private void createKeyboardThread() {
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		
		Runnable listenForInput = new Runnable() {
			public void run() {
				try {
					String textReceived = inputReader.readLine();
					while(textReceived != null)
					{
						System.out.println("Client sent: " + textReceived);
						
						writer.println(textReceived);
						writer.flush();
						
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
