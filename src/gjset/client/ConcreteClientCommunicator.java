package gjset.client;

import gjset.client.exceptions.FailedConnectionException;
import gjset.tools.GlobalProperties;
import gjset.tools.MessageHandler;
import gjset.tools.MessageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.DocumentException;
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
 * This class handles all communication between the client and the server.
 * <P>
 * When the {@link #connectToServer} method is called, this class opens a TCP/IP connection to a TCP/IP server hosting the game engine. Subsequent UI actions on
 * the part of the user will be transmitted to the engine through this class.
 * <P>
 * This class also provides support for handling incoming messages from the engine to the UI, forwarding all incoming messages to the linked
 * {@link ClientGUIController} object.
 * 
 */
public class ConcreteClientCommunicator implements ClientCommunicator
{
	// Stores all message handlers.
	private List<MessageHandler> handlers;

	// Stores the socket to connect to the server.
	private Socket socket;

	// Tools to read/write to the socket's I/O stream
	private XMLWriter writer;
	private BufferedReader reader;

	// The destination to connect to.
	private SocketAddress socketAddress;

	private Thread listeningThread;

	private SAXReader XMLreader;

	private int connectionTimeout;
	private int connectionAttempts;
	private int maxRetries;

	private Thread connectionThread;

	private String serverAddress;

	private int port;

	/**
	 * Blank constructor to assert that nothing is done on object instantiation.
	 * 
	 */
	public ConcreteClientCommunicator() {
		// Extract properties from the properties table.
		Properties props = GlobalProperties.properties;

		connectionTimeout = Integer.parseInt(props.getProperty("server.timeout.connect", "5000"));
		maxRetries = Integer.parseInt(props.getProperty("server.reconnects.max", "5"));

		handlers = new Vector<MessageHandler>();

		createListeningThread();
		createConnectionThread();
	}

	/**
	 * Adds a message handler to the communicator.
	 * 
	 * @param handler
	 * @see gjset.client.ClientCommunicator#addMessageHandler(gjset.client.ClientGUIController)
	 */
	@Override
	public void addMessageHandler(MessageHandler handler) {
		handlers.add(handler);
	}

	/**
	 * Remove the indicated message handler
	 * 
	 * @param gameInitiator
	 */
	public void removeMessageHandler(MessageHandler handler) {
		handlers.remove(handler);
	}

	/**
	 * Receive a message from the server.
	 * 
	 * @param message
	 */
	public void receiveMessage(Element message) {
		// Copy the list so that we can modify the original in the various handlers.
		List<MessageHandler> listCopy = new Vector<MessageHandler>(handlers);
		Iterator<MessageHandler> iterator = listCopy.iterator();
		while (iterator.hasNext()) {
			iterator.next().handleMessage(message);
		}
	}

	/**
	 * Establishes a connection to the game server using the given hostname and port.
	 * <P>
	 * This method also kicks off a new listening thread to read incoming messages from the game server.
	 */
	public void connectToServer(String serverAddress, int port) {
		if (connectionThread.isAlive() || listeningThread.isAlive()) {
			System.out.println("Already attempting to connect to server.");
			return;
		}

		this.serverAddress = serverAddress;
		this.port = port;

		connectionThread.start();
	}

	/**
	 * This command sends the indicated XML to the server, appending the appropriate information.
	 * 
	 * @param messageElement
	 * @throws IOException
	 * @see gjset.client.ClientCommunicator#sendMessage(org.dom4j.tree.DefaultElement)
	 */
	@Override
	public void sendMessage(Element messageElement) {
		Element fullXMLElement = MessageUtils.wrapMessage(messageElement);
		try {
			writer.write(fullXMLElement);
			writer.write("\n");
			writer.flush();
		} catch (IOException e) {
			handleConnectionError(e);
		}
	}

	/**
	 * Shut down this client
	 * 
	 */
	public void destroy() {
		try {
			socket.close();
			listeningThread.interrupt();
			connectionThread.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Private method that attempts to establish a connection on the socket.
	 * 
	 * This assumes that the socket has already been created and initialized.
	 * 
	 */
	private void tryToConnect() {
		connectionAttempts++;

		System.out.println("Making connection attempt # " + connectionAttempts + " of " + (maxRetries + 1));

		// Attempt to connect to the server.
		try {
			socket = new Socket();
			socket.connect(socketAddress, connectionTimeout);
		} catch (SocketTimeoutException ste) {
			System.err.println("Failed to connect to server in time.");
			ste.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Based on the state of the socket, decide what to do.
			if (socket.isConnected()) {
				// Hooray! We're conencted!
				handleSuccessfulConnection();
			}
			else if (connectionAttempts <= maxRetries) {
				// Boo. Something went wrong. Try again.
				tryToConnect();
			}
			else {
				// Boooooo. Something is really wrong. We're not going to connect.
				System.err.println("Connection failed.");

				handleConnectionError(new FailedConnectionException());
			}
		}
	}

	/**
	 * This method is called when we have a successful connection.
	 * 
	 */
	private void handleSuccessfulConnection() {

		System.out.println("Connection successful");

		// Get our I/O streams squared away once we're connected.
		try {
			createIOStreams();
		} catch (IOException e) {
			System.err.println("Something crazy happened. I thought we were connected, but we weren't. Oh well.");
			e.printStackTrace();
			handleConnectionError(new FailedConnectionException());
		}

		// If we successfully connected, start the listening thread.
		if (reader != null) {
			listeningThread.start();
			connectionThread = null;
		}
	}

	/**
	 * Handle an error from the system.
	 * 
	 * @param e
	 */
	private void handleConnectionError(Exception e) {
		Iterator<MessageHandler> iterator = handlers.iterator();
		while (iterator.hasNext()) {
			iterator.next().handleConnectionError(e);
		}
	}

	// Used to create the Input/Output stream handling tools for a newly created socket.
	private void createIOStreams() throws IOException {
		writer = new XMLWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		XMLreader = new SAXReader();
	}

	/**
	 * Creates the thread the listens to the socket.
	 * 
	 */
	private void createListeningThread() {
		Runnable listenForMessage = new Runnable() {
			public void run() {
				try {
					String textReceived = reader.readLine();
					while (socket.isConnected() && textReceived != null) {
						// Create an input stream to allow the XML parser to read from the string.
						InputStream stringInput = new ByteArrayInputStream(textReceived.getBytes());
						Document document = XMLreader.read(stringInput);

						// Now receive the message.
						receiveMessage(document.getRootElement());

						// Then go looking for the next message.
						textReceived = reader.readLine();
					}
				} catch (IOException e) {
					System.err.println("IO Exception reading input in client. (Possibly because of closed socket.)");
					handleConnectionError(e);
				} catch (DocumentException e) {
					System.err.println("Document Exception parsing text in client.");
					handleConnectionError(e);
				}
			}
		};

		listeningThread = new Thread(listenForMessage, "Client Listening Thread");
	}

	/**
	 * Create the thread the connects to the server.
	 * 
	 */
	private void createConnectionThread() {
		Runnable beginConnecting = new Runnable() {
			public void run() {
				System.out.println("Initializing socket to connect to " + serverAddress + ":" + port);

				// Create our address to connect to.
				socketAddress = new InetSocketAddress(serverAddress, port);

				connectionAttempts = 0;

				tryToConnect();
			}
		};

		connectionThread = new Thread(beginConnecting, "Client Connection Thread");
	}
}