package gjset.tests;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * This provides a very simple interface for getting data from a client.
 */
public class MockServer
{
	private ServerSocket serverSocket;
	private ClientHandler client;
	
	private Element lastMessage;
	
	private Thread serverThread;
	
	public MockServer(int port)
	{
		try
		{
			serverSocket = new ServerSocket(port);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		createServerThread();
		System.out.println("Server created");
	}
	
	/**
	 * 
	 * Create the thread that will listen for incoming clients.
	 *
	 */
	private void createServerThread()
	{
		//Create the server's listening thread:
		Runnable runServer = new Runnable()
		{
			public void run()
			{
				try
				{	
					//Listen for an incoming connection.
					Socket socket = serverSocket.accept();
					
					//When the above command returns, it will have a new client to deal with.  Handle it!
					handleNewClient(socket);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		
		//Create the thread.
		serverThread = new Thread(runServer);
	}
	
	/**
	 * 
	 * Start the server thread.
	 *
	 */
	public void listenForClients()
	{
		System.out.println("Starting server");
		serverThread.start();
	}
	
	/**
	 * 
	 * Handle the new incoming client connection.
	 *
	 * @param clientSocket
	 */
	private void handleNewClient(Socket clientSocket)
	{
		//Get our I/O streams.
		client = new ClientHandler(clientSocket, this);
	}

	/**
	 * 
	 * Send the indicated message to the client.
	 *
	 * @param messageElement
	 */
	public void sendMessage(Element messageElement)
	{
		System.out.println("Sending message " + messageElement);
		client.sendMessage(messageElement);
	}
	
	class ClientHandler
	{
		private final Socket socket;
		
		//Tools to read/write to the socket's I/O stream
		private XMLWriter	writer;
		private BufferedReader reader;
		private Thread listeningThread;
		
		public ClientHandler(Socket socket, MockServer server)
		{
			System.out.println("Spawning client");
			this.socket = socket;

			createIOStreams();
			
			System.out.println("Starting to listen to client");
			listeningThread.start();
		}
		
		public void sendMessage(Element message)
		{
			try
			{
				writer.write(message);
				writer.write("\n");
				writer.flush();
			} catch (IOException e)
			{
				System.err.println("Failed to send message");
				e.printStackTrace();
			}
		}

		/**
		 * Create the IO streams to talk to the client.
		 *
		 */
		private void createIOStreams()
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
		 * Listen for the end of the combocards element.
		 *
		 * @param message
		 */
		public void receiveMessage(Element message)
		{
			lastMessage = message;
			System.out.println("Got message " + lastMessage);
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
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the most recent message received.
	 *
	 * @return
	 */
	public Element getLastMessage()
	{
		return lastMessage;
	}

	/**
	 * Destroy the server, shutting down all behaviors.
	 *
	 */
	public void destroy()
	{
		System.out.println("Destroying server");
		client.destroy();
		
		serverThread.interrupt();
		try
		{
			serverSocket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		MockServer server = new MockServer(5151);
		server.listenForClients();
	}
}
