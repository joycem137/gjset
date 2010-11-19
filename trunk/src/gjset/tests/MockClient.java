package gjset.tests;

import gjset.GameConstants;

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

/**
 * This is a class that gives us all the functionality we need to pretend to be a client connecting
 * to the game server.
 */
public class MockClient
{

	private InetSocketAddress socketAddress;
	private DocumentFactory documentFactory;
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
		
		documentFactory = DocumentFactory.getInstance();
		
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
		Element fullXMLElement = wrapMessage(messageElement);
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
