package gjset.server;

import gjset.server.gui.ServerConsole;
import gjset.tools.MessageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

public class PlayerClientHandler
{
	private GameServer	server;
	private ServerConsole	console;
	
	private Socket	socket;
	private XMLWriter writer;
	private BufferedReader reader;
	
	private Thread listeningThread;
	private int playerId;
	
	private DocumentFactory documentFactory;

	public PlayerClientHandler(Socket socket, GameServer server, ServerConsole console, int playerId)
	{
		this.socket = socket;
		this.server = server;
		this.console = console;
		this.playerId = playerId;
		
		console.message("Creating client handler with id " + playerId);
		
		documentFactory = DocumentFactory.getInstance();
		
		//Get our I/O streams.
		try
		{	
			createIOStreams();
		} catch (IOException e)
		{
			console.errorMessage("Could not obtain I/O streams for client socket.");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Executes the listening thread.
	 *
	 */
	public void startListening()
	{
		console.message("Starting to listen to client");
		
		// Start listening.
		listeningThread.start();
		
		// Now that we're listening, initialize the other end.
		sendInitializationMessage();
	}

	/**
	 * Send a message to the client indicating our version and its player id.
	 *
	 */
	private void sendInitializationMessage()
	{
		console.message("Initializing client");
		
		Element message = documentFactory.createElement("playerid");
		message.addText("" + playerId);
		
		sendMessage(message);
	}

	//Used to create the Input/Output stream handling tools for a newly created socket.
	private void createIOStreams() throws IOException
	{
		try
		{
			writer = new XMLWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		final SAXReader XMLreader = new SAXReader();
		
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
						
						// Now receive the message.
						receiveMessage(document.getRootElement());
						
						// Then go looking for the next message.
						textReceived = reader.readLine();		
					}
				} catch (IOException e)
				{
					System.err.println("IO Exception reading input in client handler. (Possibly because of closed socket.)");
					//e.printStackTrace();
				} catch (DocumentException e)
				{
					System.err.println("Document Exception parsing text in client handler.");
					//e.printStackTrace();
				}	
			}
		};
		
		listeningThread = new Thread(listenForMessage);
	}

	/**
	 * Return the id for this player.
	 *
	 * @return
	 */
	public int getPlayerId()
	{
		return playerId;
	}

	/**
	 * This command sends the indicated XML to the server, appending the appropriate information.
	 *
	 * @param messageElement
	 * @see gjset.client.ClientCommunicator#sendMessage(org.dom4j.tree.DefaultElement)
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
	 * 
	 * Handle receiving the message.
	 *
	 * @param message
	 */
	public void receiveMessage(Element message)
	{
		server.receiveMessage(this, message);
	}

	/**
	 * Shut down this client handler.
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

}
