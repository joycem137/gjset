package gjset.tests;

import gjset.tests.MockServer.ClientHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * This provides a very simple interface for getting data from a client.
 */
public class MockServer
{
	private ServerSocket serverSocket;
	private Socket clientSocket;
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
		this.clientSocket = clientSocket;

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
	
	class ClientHandler implements ElementHandler
	{
		private final Socket socket;
		private final MockServer server;
		
		//Tools to read/write to the socket's I/O stream
		private XMLWriter	writer;
		private InputStream input;
		private Thread listeningThread;
		
		public ClientHandler(Socket socket, MockServer server)
		{
			System.out.println("Spawning client");
			this.socket = socket;
			this.server = server;

			createIOStreams();
			
			System.out.println("Starting to listen to client");
			listeningThread.start();
		}
		
		public void sendMessage(Element message)
		{
			try
			{
				writer.write(message);
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
				input = socket.getInputStream();
			} catch (UnsupportedEncodingException e1)
			{
				e1.printStackTrace();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			final SAXReader reader = new SAXReader();
			reader.addHandler("/combocards", this);
			
			Runnable listenForMessage = new Runnable()
			{
				public void run()
				{
					try
					{
						reader.read(input);
						System.out.println("Finished reading input from client");
					} catch (DocumentException e)
					{
						System.err.println("Error reading input (May be because of closed socket)");
						//e.printStackTrace();
					}
				}
			};
			
			listeningThread = new Thread(listenForMessage);
		}

		/**
		 * Listen for the end of the combocards element.
		 *
		 * @param path
		 * @see org.dom4j.ElementHandler#onEnd(org.dom4j.ElementPath)
		 */
		public void onEnd(ElementPath path)
		{
			lastMessage = path.getCurrent();
			System.out.println("Got message " + lastMessage);
		}

		/**
		 * Does not do anything in this implementation.
		 *
		 * @param arg0
		 * @see org.dom4j.ElementHandler#onStart(org.dom4j.ElementPath)
		 */
		public void onStart(ElementPath arg0)
		{
			// Nothing to do here.
			System.out.println("Message started " + arg0);
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
