package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gjset.GameConstants;
import gjset.client.ClientGUIModel;
import gjset.data.Card;
import gjset.data.CardTableData;

import java.net.URL;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

/**
 * This test tests the integrity of the GUI Model and verifies that the correct information is present.
 */
public class TestGUIModel
{
	/**
	 * 
	 * Test that the initial setup of the model is correct.
	 *
	 */
	@Test
	public void testInitialModelState()
	{
		ClientGUIModel model = new ClientGUIModel();
		
		assertEquals(GameConstants.GAME_STATE_NOT_STARTED, model.getGameState());
		assertEquals(0, model.getCardsInDeck());
		assertFalse(model.canSelectCards());
		assertFalse(model.canCallSet());
		assertNull(model.getCardTable());
		assertFalse(model.canDrawCards());
	}
	
	/**
	 * This test verifies that the test XML file actually updates the model correctly
	 * with basic information.  Nothing fancy.
	 */
	@Test
	public void testBasicModelUpdate()
	{	
		ClientGUIModel model = new ClientGUIModel();
		model.setPlayerId(1);
		
		Element gameupdateElement = loadBasicUpdate();
		
		model.update(gameupdateElement);
		
		// Now test that all of the correct values got updated.
		assertEquals(69, model.getCardsInDeck());
		assertEquals(GameConstants.GAME_STATE_IDLE, model.getGameState());
		assertTrue(model.canSelectCards());
		assertTrue(model.canCallSet());
		assertTrue(model.canDrawCards());

		
		// Verify the card table.
		CardTableData cardTable = model.getCardTable();
		
		assertNotNull(cardTable);
		assertEquals(12, cardTable.getNumCards());
		assertEquals(3, cardTable.getRows());
		assertEquals(4, cardTable.getCols());
	}

	/**
	 * 
	 * Perform more in depth tests of the card table to verify that it is working correctly.
	 *
	 */
	@Test
	public void testCardTable()
	{
		ClientGUIModel model = new ClientGUIModel();
		model.setPlayerId(1);
		
		Element gameupdateElement = loadBasicUpdate();
		
		model.update(gameupdateElement);
		
		// Verify the card table.
		CardTableData cardTable = model.getCardTable();
		
		assertNotNull(cardTable);
		assertEquals(12, cardTable.getNumCards());
		assertEquals(3, cardTable.getRows());
		assertEquals(4, cardTable.getCols());
		
		// Verify one of the cards.
		Card card = cardTable.getCardAt(2,1);
		
		assertEquals(1, card.getNumber());
		assertEquals(Card.COLOR_BLUE, card.getColor());
		assertEquals(Card.SHAPE_DIAMOND, card.getShape());
		assertEquals(Card.SHADING_STRIPED, card.getShading());
		
		assertFalse(cardTable.isHighlighted(card));
		
		// Verify another card.
		card = cardTable.getCardAt(0,2);
		assertEquals(3, card.getNumber());
		assertEquals(Card.COLOR_RED, card.getColor());
		assertEquals(Card.SHAPE_SQUIGGLE, card.getShape());
		assertEquals(Card.SHADING_NONE, card.getShading());
		
		assertTrue(cardTable.isHighlighted(card));
	}
	
	/**
	 * This loads the basic update XML file.
	 *
	 * @return The Element object containing the basic update.
	 */
	private Element loadBasicUpdate()
	{
		SAXReader reader = new SAXReader();
        Document document = null;
		try
		{
			String path = "/testfiles/TestGUIModelBasic.xml";
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
