package gjset.server;

import org.dom4j.Element;

/**
 * This is a data class that stores a single mesasge from a particular client as an individual message.
 */
public class ServerMessage
{
	public Element rootElement;
	public PlayerClientHandler client;
	
	public ServerMessage(PlayerClientHandler client, Element rootElement)
	{
		this.client = client;
		this.rootElement = rootElement;
	}
}
