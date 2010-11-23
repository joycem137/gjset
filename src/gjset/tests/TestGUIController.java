package gjset.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import gjset.client.ClientGUIController;
import gjset.client.ClientGUIModel;
import gjset.client.ConcreteClientGUIController;
import gjset.data.Card;
import gjset.data.Player;

import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

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
 * This test suite tests the GUI Controller itself.
 */
public class TestGUIController
{
	private MockClientCommunicator client;
	private Player defaultPlayer;
	
	/**
	 * This method sets up each test before running it.
	 */
	@Before
	public void setUp()
	{
		client = new MockClientCommunicator();
		defaultPlayer = new Player(1, "Player");
	}
	
	/**
	 * This method tests the initial configuration of the controller.
	 */
	@Test
	public void testInitialSetup()
	{
		ClientGUIController controller = new ConcreteClientGUIController(client, defaultPlayer);
		
		assertNotNull(controller.getModel());
		assertEquals(controller, client.getHandler());
	}
	
	/**
	 * 
	 * This method tests whether or not set calling works.
	 *
	 */
	@Test
	public void testCallSet()
	{
		ClientGUIController controller = new ConcreteClientGUIController(client, defaultPlayer);
		controller.callSet();
		
		Element message = client.getLastMessage();
		
		assertNotNull(message);
		
		assertEquals("command", message.getName());
		assertEquals("callset", message.attributeValue("type", ""));
	}
	
	/**
	 * 
	 * This method tests whether or not card drawing works.
	 *
	 */
	@Test
	public void drawMoreCards()
	{
		ClientGUIController controller = new ConcreteClientGUIController(client, defaultPlayer);
		controller.drawMoreCards();
		
		Element message = client.getLastMessage();
		
		assertNotNull(message);
		
		assertEquals("command", message.getName());
		assertEquals("drawcards", message.attributeValue("type", ""));
	}
	
	/**
	 * 
	 * This method tests whether or not card selection works.
	 *
	 */
	@Test
	public void testSelectCard()
	{
		
		ClientGUIController controller = new ConcreteClientGUIController(client, defaultPlayer);
		
		// Now send a basic update out to get the controller in a state to handle card selection
		Element updateMessage = loadSimpleFullUpdate();
		client.injectMessage(updateMessage);

		Card card = new Card(1, Card.COLOR_RED, Card.SHADING_NONE, Card.SHAPE_SQUIGGLE);
		controller.selectCard(card);
		
		Element message = client.getLastMessage();
		
		assertNotNull(message);
		
		assertEquals("command", message.getName());
		assertEquals("selectcard", message.attributeValue("type", ""));
		
		Element cardElement = message.element("card");
		
		Card cardNew = new Card(cardElement);
		
		assertEquals(card, cardNew);
	}
	
	/**
	 * Verify that sending a message into the controller updates the model appropriately.
	 */
	@Test
	public void testUpdateModelFromController()
	{		
		ClientGUIController controller = new ConcreteClientGUIController(client, defaultPlayer);
		ClientGUIModel model = controller.getModel();
		TestGUIModel.evaluateInitialModelState(model);
		
		// Start by sending the ID to the client.
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		Element idMessage = documentFactory.createElement("combocards");
		Element playerIdNode = documentFactory.createElement("newplayer");
		
		Player defaultPlayer = new Player(1, "Player");
		playerIdNode.add(defaultPlayer.getXMLRepresentation());
		
		idMessage.add(playerIdNode);
		client.injectMessage(idMessage);
		
		// Verify that the player id got set.
		assertEquals(defaultPlayer.getId(), model.getLocalPlayer().getId());
		assertEquals(defaultPlayer.getName(), model.getLocalPlayer().getName());
		
		// Now send a basic update out.
		Element updateMessage = loadSimpleFullUpdate();
		client.injectMessage(updateMessage);
		
		// Verify that we're not creating new models all the time.
		assertEquals(model, controller.getModel());
		
		// Test the model out to make sure the update took.
		TestGUIModel.evaluateBasicModelUpdate(model);
	}
	
	/**
	 * This loads the basic update XML file.
	 *
	 * @return The Element object containing the basic update.
	 */
	private Element loadSimpleFullUpdate()
	{
		SAXReader reader = new SAXReader();
        Document document = null;
		try
		{
			String path = "/testfiles/TestGUIController.xml";
			URL testFileURL = getClass().getResource(path);
			
			document = reader.read(testFileURL);
		} catch (Exception e)
		{
			e.printStackTrace();
			fail("Could not read test xml file");
		}
		
		assertNotNull(document);
		return document.getRootElement();
	}
}
