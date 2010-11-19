package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gjset.GameConstants;
import gjset.data.Card;
import gjset.data.CardTableData;
import gjset.server.GameModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class TestServerGameModel
{

	/**
	 * TODO: Describe method
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * TODO: Describe method
	 *
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

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
	
	
		
}
