package gjset.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gjset.GameConstants;
import gjset.data.Card;
import gjset.data.CardTableData;
import gjset.data.Player;
import gjset.server.CardTable;
import gjset.server.GameModel;

import java.util.List;

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
		model.addNewPlayer("Player");
		model.startNewGame();
		
		assertEquals(GameConstants.GAME_STATE_IDLE, model.getGameState());
		
		int playerId = 1;
		model.callSet(playerId);
		
		assertEquals(playerId, model.getSetCaller().getId());
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
	
	/**
	 * Test the player management.
	 */
	@Test
	public void testPlayerManagement()
	{
		// First try adding a bunch of new players.
		Player player1 = model.addNewPlayer("Test 1");
		assertNotNull(player1);
		
		Player player2 = model.addNewPlayer("Test 2");
		assertNotNull(player2);
		
		Player player3 = model.addNewPlayer("Test 3");
		assertNotNull(player3);
		
		Player player4 = model.addNewPlayer("Test 4");
		assertNotNull(player4);
		
		// Check the configuration of one of the players.
		assertEquals("Test 1", player1.getName());
		assertEquals(1, player1.getId());
		assertEquals(0, player1.getPoints());
		assertEquals(0, player1.getPenalty());
		assertEquals(0, player1.getScore());
		
		// Now get the full list of players from the model.
		List<Player> players = model.getPlayers();
		
		// And test it.
		assertNotNull(players);
		
		assertEquals(4, players.size());
		assertEquals(player3, players.get(2));
	}
}
