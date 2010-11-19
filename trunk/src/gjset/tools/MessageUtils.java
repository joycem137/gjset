package gjset.tools;

import gjset.GameConstants;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

/**
 * This class contains a wide variety of useful classes and utilities for working with messages.
 */
public class MessageUtils
{
	/**
	 * Wraps a message with enclosing tags and a comm version.
	 *
	 * @param messageElement
	 * @return
	 */
	public static Element wrapMessage(Element messageElement)
	{	
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		
		Element rootElement = documentFactory.createElement("combocards");
		
		Element versionElement = documentFactory.createElement("version");
		versionElement.setText(GameConstants.COMM_VERSION);
		rootElement.add(versionElement);
		
		rootElement.add(messageElement);
		
		return rootElement;
	}
}
