package gjset.tests;

import gjset.tools.MessageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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
 * This is a class that gives us all the functionality we need to pretend to be a client connecting
 * to the game server.
 */
public class MockClient
{

	private InetSocketAddress socketAddress;
	private Element lastMessage;
	private Socket socket;
	private Thread listeningThread;
	private XMLWriter writer;
	private BufferedReader reader;
	private SAXReader XMLreader;
	private int playerId;

	/**
	 * Create a client that will connect to the indicated port.
	 *
	 * @param string
	 * @param gamePort
	 */
	public MockClient(String hostname, int port)
	{
		socketAddress = new InetSocketAddress(hostname, port);
		
		DocumentFactory.getInstance();
		
		playerId = 0;
	}
	
	/**
	 * Receive a message from the server.
	 *
	 * @param message
	 */
	public void receiveMessage(Element message)
	{
		this.lastMessage = message;
		
		Element playerIdElement = message.element("playerid");
		if(playerIdElement != null)
		{
			String playerIdString = playerIdElement.getText();
			System.out.println("Got player id message of " + playerIdString);
			
			playerId = Integer.parseInt(playerIdString);
		}
	}

	/**
	 * Establishes a connection to the game server using the given hostname and port.
	 * <P>
	 * This method also kicks off a new listening thread to read incoming messages from the game server.
	 */
	public void connectToServer()
	{
		System.out.println("Starting client");
		try
		{
			socket = new Socket();
			
			//Attempt to connect to the server.
			socket.connect(socketAddress);
			
			//Get our I/O streams squared away once we're connected.
			createIOStreams();
			
			// And then start listening
			listeningThread.start();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	//Used to create the Input/Output stream handling tools for a newly created socket.
	private void createIOStreams() throws IOException
	{
		writer = new XMLWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		XMLreader = new SAXReader();
		
		Runnable listenForMessage = new Runnable()
		{
			public void run()
			{
				try
				{
					String textReceived = reader.readLine();
					while(socket.isConnected() && textReceived != null)
					{
						// Create an input stream to allow the XML parser to read from the string.
						InputStream stringInput = new ByteArrayInputStream(textReceived.getBytes());
						Document document = XMLreader.read(stringInput);
						
						// Now receive teh message.
						receiveMessage(document.getRootElement());
						
						// Then go looking for the next message.
						textReceived = reader.readLine();		
					}
				} catch (IOException e)
				{
					System.err.println("IO Exception reading input in client. (Possibly because of closed socket.)");
					//e.printStackTrace();
				} catch (DocumentException e)
				{
					System.err.println("Document Exception parsing text in client.");
					//e.printStackTrace();
				}	
			}
		};
		
		listeningThread = new Thread(listenForMessage);
	}

	/**
	 * This command sends the indicated XML to the server, appending the appropriate information.
	 *
	 * @param messageElement
	 */
	public void sendMessage(Element messageElement)
	{
		Element fullXMLElement = MessageUtils.wrapMessage(messageElement);
		try
		{
			writer.write(fullXMLElement);
			writer.write("\n");
			writer.flush();
		} catch (IOException e)
		{
			System.err.println("Failed to send message");
			e.printStackTrace();
		}
	}

	/**
	 * Destroy this client.
	 *
	 */
	public void destroy()
	{
		try
		{
			socket.close();
			listeningThread.interrupt();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Return the last message received by this client.
	 *
	 * @return
	 */
	public Element getLastMessage()
	{
		return lastMessage;
	}

	/**
	 * Return the player Id for this client.
	 *
	 * @return
	 */
	public int getPlayerId()
	{
		return playerId;
	}

}
