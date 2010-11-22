package gjset.tests;

import org.dom4j.Element;

import gjset.client.ClientCommunicator;
import gjset.tools.MessageHandler;

/**
 *
 */
public class MockClientCommunicator implements ClientCommunicator
{
	private MessageHandler handler;
	private Element message;

	/**
	 * Grab the message handler.
	 *
	 * @param gui
	 * @see gjset.client.ClientCommunicator#addMessageHandler(gjset.tools.MessageHandler)
	 */
	public void addMessageHandler(MessageHandler handler)
	{
		this.handler = handler;
	}

	/**
	 * Pretend to send the indicated message.
	 *
	 * @param messageElement
	 * @see gjset.client.ClientCommunicator#sendMessage(org.dom4j.Element)
	 */
	public void sendMessage(Element message)
	{
		this.message = message;
	}

	/**
	 * returns the message handler assigned to this client.
	 *
	 * @return
	 */
	public MessageHandler getHandler()
	{
		return handler;
	}

	/**
	 * Returns the last mesasge sent using the sendMessage method.
	 *
	 * @return
	 */
	public Element getLastMessage()
	{
		return message;
	}

	/**
	 * Pretend to send the indicated message to the message handler.
	 *
	 * @param updateMessage
	 */
	public void injectMessage(Element updateMessage)
	{
		handler.handleMessage(updateMessage);
	}

	/**
	 * Remove this object.
	 *
	 * @see gjset.client.ClientCommunicator#destroy()
	 */
	public void destroy()
	{
		// Nothing to do.
	}

}
