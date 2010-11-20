package gjset.tests;

import org.dom4j.Element;

import gjset.server.PlayerClientHandler;
import gjset.server.ServerMessageHandler;

/**
 * Pretend to be your ordinary message handler for the server.
 */
public class MockServerMessageHandler implements ServerMessageHandler
{
	private int lastClientId;
	private Element lastMessage;

	/**
	 * Store the message so that we can find out later that this message got sent.
	 *
	 * @param client
	 * @param message
	 * @see gjset.server.ServerMessageHandler#handleMessage(gjset.server.PlayerClientHandler, org.dom4j.Element)
	 */
	public void handleMessage(PlayerClientHandler client, Element message)
	{
		this.lastClientId = client.getPlayerId();
		this.lastMessage = message;
	}

	/**
	 * Return the last message we received.
	 *
	 * @return
	 */
	public Element getLastMessage()
	{
		return lastMessage;
	}

	/**
	 * Return the last client id we received a message from.
	 *
	 * @return
	 */
	public int getLastClientId()
	{
		return lastClientId;
	}

	/**
	 * Handle a new client.
	 *
	 * @param client
	 * @see gjset.server.ServerMessageHandler#handleNewClient(gjset.server.PlayerClientHandler)
	 */
	public void handleNewClient(PlayerClientHandler client)
	{
		// Do nothing.
	}

}
