package gjset.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gjset.client.ClientGUIController;
import gjset.client.ConcreteClientGUIController;
import gjset.data.Card;

import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;


/**
 * This test suite tests the GUI Controller itself.
 */
public class TestGUIController
{
	private MockClientCommunicator client;
	
	/**
	 * This method sets up each test before running it.
	 */
	@Before
	public void setUp()
	{
		client = new MockClientCommunicator();
	}
	
	/**
	 * This method tests the initial configuration of the controller.
	 */
	@Test
	public void testInitialSetup()
	{
		ClientGUIController controller = new ConcreteClientGUIController(client);
		
		assertNotNull(controller.getClientGUIModel());
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
		ClientGUIController controller = new ConcreteClientGUIController(client);
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
		ClientGUIController controller = new ConcreteClientGUIController(client);
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
	public void selectCard()
	{
		Card card = new Card(1, Card.COLOR_RED, Card.SHADING_NONE, Card.SHAPE_SQUIGGLE);
		
		ClientGUIController controller = new ConcreteClientGUIController(client);
		controller.selectCard(card);
		
		Element message = client.getLastMessage();
		
		assertNotNull(message);
		
		assertEquals("command", message.getName());
		assertEquals("selectcard", message.attributeValue("type", ""));
		
		Element cardElement = message.element("card");
		
		Card cardNew = new Card(cardElement);
		
		assertEquals(card, cardNew);
	}
}
