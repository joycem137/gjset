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
import gjset.data.Player;

import java.net.URL;
import java.util.List;

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
		
		evaluateInitialModelState(model);
	}
	
	/**
	 * 
	 * Performs the tests required to test the initial state of the model.
	 * Public to allow other classes to access this.
	 *
	 * @param model
	 */
	public static void evaluateInitialModelState(ClientGUIModel model)
	{
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
		
		evaluateBasicModelUpdate(model);
	}

	/**
	 * Perform the tests necessary to verify that the basic model was correctly updated.
	 *
	 * @param model
	 */
	public static void evaluateBasicModelUpdate(ClientGUIModel model)
	{
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
	
	@Test
	public void testPlayerData()
	{
		ClientGUIModel model = new ClientGUIModel();
		model.setPlayerId(2);
		
		Element gameupdateElement = loadBasicUpdate();
		
		model.update(gameupdateElement);
		
		// Get the list of players from the model.
		List<Player> players = model.getPlayers();
		
		assertNotNull(players);
		
		// Make sure we have enough players.
		assertEquals(3, players.size());
		
		// Then make sure the players are correct.
		Player player1 = players.get(0);
		
		assertEquals("Player 1", player1.getName());
		assertEquals(12, player1.getPoints());
		assertEquals(6, player1.getPenalty());
		assertEquals(6, player1.getScore());
		
		Player player3 = players.get(2);
		
		assertEquals("Player 3", player3.getName());
		assertEquals(0, player3.getPoints());
		assertEquals(0, player3.getPenalty());
		assertEquals(0, player3.getScore());
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
