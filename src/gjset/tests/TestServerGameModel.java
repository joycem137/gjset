package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gjset.GameConstants;
import gjset.data.Card;
import gjset.data.CardTableData;
import gjset.server.GameModel;

import org.junit.Test;

/**
 * Exercise the game model within the server.
 */
public class TestServerGameModel
{
	/**
	 * 
	 * Verify that the initial configuration of the game model is correct.
	 *
	 */
	@Test
	public void testInitialConfiguration()
	{
		GameModel model = new GameModel();
		
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
		GameModel model = new GameModel();
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
		GameModel model = new GameModel();
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
		GameModel model = new GameModel();
		model.startNewGame();
		
		assertEquals(0, model.getSetCallerId());
		assertEquals(GameConstants.GAME_STATE_IDLE, model.getGameState());
		
		int playerId = 1;
		model.callSet(playerId);
		
		assertEquals(playerId, model.getSetCallerId());
		assertEquals(GameConstants.GAME_STATE_SET_CALLED, model.getGameState());
	}
}
