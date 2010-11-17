package gjset.tools;

import org.dom4j.Element;

/**
 * This is a generic interface for all objects that can handle incoming XML messages
 */
public interface MessageHandler
{
	public void handleMessage(Element message);
}
