package gjset.server;

import org.dom4j.Element;

/**
 *
 */
public interface ServerMessageHandler
{

	/**
	 * Receive a message from a particular client.
	 * @param client 
	 *
	 * @param message
	 */
	void handleMessage(PlayerClientHandler client, Element message);

}
