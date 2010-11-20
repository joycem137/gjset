package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gjset.GameConstants;
import gjset.data.Card;
import gjset.data.CardTableData;
import gjset.server.CardTable;
import gjset.server.GameModel;

import org.junit.Before;
import org.junit.Test;

/**
 * Exercise the game model within the server.
 */
public class TestServerGameModel
{
	GameModel model;
	
	/**
	 * 
	 */
	@Before
	public void setUp()
	{
		model = new GameModel();
	}
	
	/**
	 * 
	 * Verify that the initial configuration of the game model is correct.
	 *
	 */
	@Test
	public void testInitialConfiguration()
	{
		
		assertEquals(GameConstants.GAME_STATE_NOT_STARTED, model.getGameState());
	}
	
	/**
	 * 
	 * Verify that starting a new game works correctly in the model.
	 *
	 */
	@Test
	public void testStartingNewGame()
	{
		model.startNewGame();
		
		assertEquals(GameConstants.GAME_STATE_IDLE, model.getGameState());
		assertEquals(69, model.getDeck().getRemainingCards());
		
		// Assert that the basic settings of the card table are correct.
		CardTableData cardTable = model.getCardTable();
		
		assertNotNull(cardTable);
		assertEquals(12, cardTable.getNumCards());
		assertEquals(3, cardTable.getRows());
		assertEquals(4, cardTable.getCols());
		
		// Make sure we can get a card out of the table.
		Card card = cardTable.getCardAt(2,1);
		assertNotNull(card);
	}
	
	/**
	 * Verify that we can draw cards from a new game.
	 */
	@Test
	public void testDrawingCards()
	{
		CardTableData cardTable = model.getCardTable();
		
		model.startNewGame();
		
		assertEquals(69, model.getDeck().getRemainingCards());
		assertEquals(12, cardTable.getNumCards());
		assertEquals(3, cardTable.getRows());
		assertEquals(4, cardTable.getCols());
		
		model.drawCards();
		
		assertEquals(66, model.getDeck().getRemainingCards());
		assertEquals(15, cardTable.getNumCards());
		assertEquals(3, cardTable.getRows());
		assertEquals(5, cardTable.getCols());
	}
	
	/**
	 * Verify that we can call set on the model.
	 */
	@Test
	public void testCallSet()
	{
		model.startNewGame();
		
		assertEquals(0, model.getSetCallerId());
		assertEquals(GameConstants.GAME_STATE_IDLE, model.getGameState());
		
		int playerId = 1;
		model.callSet(playerId);
		
		assertEquals(playerId, model.getSetCallerId());
		assertEquals(GameConstants.GAME_STATE_SET_CALLED, model.getGameState());
	}
	
	/**
	 * Verify that selecting cards works correctly.
	 */
	@Test
	public void testSelectCard()
	{
		model.startNewGame();
		
		assertEquals(GameConstants.GAME_STATE_IDLE, model.getGameState());
		
		// Get an example card.
		CardTable cardTable = model.getCardTable();
		Card card = cardTable.getCardAt(0,0);
		
		assertFalse(cardTable.isHighlighted(card));
		
		int playerId = 1;
		
		// Now select the card.
		model.toggleCardSelection(card);
		
		// Verify that the card is now highlighted.
		assertTrue(cardTable.isHighlighted(card));
		
		// Select another card.
		Card card2 = cardTable.getCardAt(0,1);
		
		// Verify that it isn't already highlighted.
		assertFalse(cardTable.isHighlighted(card2));
		
		model.toggleCardSelection(card2);
		
		// Verify that the second card is highlighted.
		assertTrue(cardTable.isHighlighted(card2));
		
		// Now test card deselection
		model.toggleCardSelection(card);
		
		assertFalse(cardTable.isHighlighted(card));
	}
}
