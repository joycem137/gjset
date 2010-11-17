package gjset.tests;

import org.dom4j.Element;

import gjset.tools.MessageHandler;

/**
 *
 */
public class MockMessageHandler implements MessageHandler
{
	private Element message;

	/**
	 * Stores the last message that was received.
	 *
	 * @param message
	 * @see gjset.tools.MessageHandler#handleMessage(org.dom4j.Element)
	 */
	public void handleMessage(Element message)
	{
		this.message = message;
	}

}
