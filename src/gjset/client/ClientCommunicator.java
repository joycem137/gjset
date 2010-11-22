package gjset.client;

import gjset.tools.MessageHandler;

import org.dom4j.Element;

/**
 * This class encapsulates the basic behavior of a communicator on the client side.
 */
public interface ClientCommunicator
{

	/**
	 * Provides a link to the game UI so that messages coming back from the server can be executed on the UI.
	 *
	 * @param gui The {@link MainGamePanel} object to forward incoming messages to.
	 */
	public abstract void addMessageHandler(MessageHandler gui);

	/**
	 * Remove the indicated message handler from the message handlers.
	 *
	 * @param gameInitiator
	 */
	public abstract void removeMessageHandler(MessageHandler handler);

	/**
	 * Send the indicated message over.
	 *
	 * @param root
	 */
	public abstract void sendMessage(Element messageElement);

	/**
	 * Destroy the indicated client.
	 *
	 */
	public abstract void destroy();

	/**
	 * Instruct the client to connect to the server.
	 *
	 */
	public abstract void connectToServer();

}