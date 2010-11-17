package gjset.client;

import gjset.engine.GameEngine;
import gjset.engine.GameServer;
import gjset.tools.MessageHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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
public class ConcreteClientCommunicator implements ClientCommunicator, ElementHandler
{
	private static final int COMM_VERSION = 1;

	private final DocumentFactory documentFactory;
	
	//Stores all message handlers.
	private List<MessageHandler> handlers;
	
	//Stores the socket to connect to the server.
	private Socket	socket;
	
	//Tools to read/write to the socket's I/O stream
	private XMLWriter	writer;
	private InputStream input;

	// The destination to connect to.
	private SocketAddress socketAddress;

	private Thread listeningThread;

	/**
	 * Blank constructor to assert that nothing is done on object instantiation.
	 *
	 * @param hostname A {@link String} containing the IP Address or hostname of the server.
	 * @param port An <code>int</code> containing the port number of the server to connect to.
	 */
	public ConcreteClientCommunicator(String hostname, int port)
	{
		//Create our address to connect to.
		socketAddress = new InetSocketAddress(hostname, port);
		
		documentFactory = DocumentFactory.getInstance();
		
		handlers = new Vector<MessageHandler>();
	}
	
	/**
	 * Adds a message handler to the communicator.
	 *
	 * @param handler
	 * @see gjset.client.ClientCommunicator#addMessageHandler(gjset.client.ClientGUIController)
	 */
	@Override
	public void addMessageHandler(MessageHandler handler)
	{
		handlers.add(handler);
	}
	
	/**
	 * react to the end of a combo cards message.
	 *
	 * @param path
	 * @see org.dom4j.ElementHandler#onEnd(org.dom4j.ElementPath)
	 */
	public void onEnd(ElementPath path)
	{
		Element message = path.getCurrent();
		Iterator<MessageHandler> iterator = handlers.iterator();
		while(iterator.hasNext())
		{
			iterator.next().handleMessage(message);
		}
	}

	/**
	 * React to the start of a combocards message.
	 *
	 * @param arg0
	 * @see org.dom4j.ElementHandler#onStart(org.dom4j.ElementPath)
	 */
	public void onStart(ElementPath arg0)
	{
		// Nothing to do on a message start.
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
	 * This command sends the indicated XML to the server, appending the appropriate information.
	 *
	 * @param messageElement
	 * @see gjset.client.ClientCommunicator#sendMessage(org.dom4j.tree.DefaultElement)
	 */
	@Override
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
		versionElement.setText("" + COMM_VERSION);
		rootElement.add(versionElement);
		
		rootElement.add(messageElement);
		
		return rootElement;
	}

}
