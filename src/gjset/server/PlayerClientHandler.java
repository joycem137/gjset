package gjset.server;

import gjset.GameConstants;
import gjset.server.gui.ServerConsole;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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

public class PlayerClientHandler implements ElementHandler
{
	private GameServer	server;
	private ServerConsole	console;
	
	private Socket	socket;
	private XMLWriter writer;
	private InputStream input;
	
	private Thread listeningThread;
	private int playerId;
	
	private DocumentFactory documentFactory;

	public PlayerClientHandler(Socket socketIn, GameServer serverIn, ServerConsole consoleIn, int playerId)
	{
		this.socket = socketIn;
		this.server = serverIn;
		this.console = consoleIn;
		this.playerId = playerId;
		
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
		listeningThread.start();
	}

	//Used to create the Input/Output stream handling tools for a newly created socket.
	private void createIOStreams() throws IOException
	{
		writer = new XMLWriter(socket.getOutputStream());
		input = socket.getInputStream();
		
		final SAXReader reader = new SAXReader();
		reader.addHandler("/combocards", this);
		
		Runnable listenForMessage = new Runnable()
		{
			public void run()
			{
				try
				{
					reader.read(input);
				} catch (DocumentException e)
				{
					System.err.println("Error reading input (Possibly because of closed socket)");
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
		Element fullXMLElement = wrapMessage(messageElement);
		try
		{
			writer.write(fullXMLElement);
			writer.flush();
		} catch (IOException e)
		{
			System.err.println("Failed to send message");
			e.printStackTrace();
		}
	}

	/**
	 * Wraps a message with enclosing tags and a comm version.
	 *
	 * @param messageElement
	 * @return
	 */
	private Element wrapMessage(Element messageElement)
	{	
		Element rootElement = documentFactory.createElement("combocards");
		
		Element versionElement = documentFactory.createElement("version");
		versionElement.setText("" + GameConstants.COMM_VERSION);
		rootElement.add(versionElement);
		
		rootElement.add(messageElement);
		
		return rootElement;
	}

	/**
	 * Handle an incoming message from the client.
	 *
	 * @param arg0
	 * @see org.dom4j.ElementHandler#onEnd(org.dom4j.ElementPath)
	 */
	public void onEnd(ElementPath path)
	{
		server.receiveMessage(this, path.getCurrent());
	}

	/**
	 * Handle the start of a particular element.
	 *
	 * @param arg0
	 * @see org.dom4j.ElementHandler#onStart(org.dom4j.ElementPath)
	 */
	public void onStart(ElementPath path)
	{
		// Nothing to do.
	}

}
