package gjset.client;


import static org.junit.Assert.*;
import gjset.GameConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This test tests the integrity of the GUI Model and verifies that the correct information is present.
 */
public class TestGUIModel
{

	/**
	 * Called before each test.
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * Called after each test.
	 *
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * 
	 * Test that the initial setup of the model is correct.
	 *
	 */
	@Test
	public void testInitialModelState()
	{
		ClientGUIModel model = new ClientGUIModel();
		
		assertEquals(model.getGameState(), GameConstants.GAME_STATE_NOT_STARTED);
		assertEquals(model.getCardsInDeck(), 0);
		assertFalse(model.canSelectCards());
		assertFalse(model.canCallSet());
		assertNull(model.getCardTable());
		assertFalse(model.canDrawCards());
	}
	
	/**
	 * This test verifies that the test XML file actually updates the model correctly.
	 */
	@Test
	public void testXMLUpdate()
	{
		ClientGUIModel model = new ClientGUIModel();
		
		
	}
}
