package gjset.tools;

import gjset.GameConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  Set¨ is a registered trademark of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of Combo Cards are very grateful for
 *  them creating such an excellent card game.
 *  
 *  Combo Cards is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Combo Cards is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with Combo Cards.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	
	/**
	 * Useful for debugging, this command pretty prints XML.
	 * 
	 * @param element
	 * @return
	 */
	public static String prettyPrint(Element element)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		XMLWriter writer;
		try
		{
			writer = new XMLWriter(stream);
			writer.write(element);
			writer.flush();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return stream.toString();
	}
	
}
