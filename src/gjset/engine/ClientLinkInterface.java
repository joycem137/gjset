/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008-2009 Joyce Murton
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of gjSet are very grateful for
 *  them creating such an excellent card game.
 *  
 *  gjSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  gjSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with gjSet.  If not, see <http://www.gnu.org/licenses/>.
 */

package gjset.engine;

import gjset.data.CardTable;
import gjset.data.Player;

/**
 * This interface provides the engine with a way to contact a single player/client.
 * Any and all communications from the engine to a player/client should take place
 * through this interface.
 * <P>
 * This class is specifcally meant to facilitate messages from a {@link GameEngine} object
 * to a {@link PlayerUI} object.
 * 
 * @see LocalClientLink
 * @see GameServer
 * @see EngineLinkInterface
 */
public interface ClientLinkInterface
{
	/**
	 * 
	 * Tell the player's UI to do whatever it needs to do to show that a new game has been started.
	 *
	 */
	void displayNewGame();
	
	/**
	 * 
	 * Tell the player's UI that the card table has changed.  This method provides the data that will be transferred
	 * to the player/client. 
	 *
	 * @param table The {@link CardTable} object that has been updated and should be updated on the client.
	 */
	void updateTable(CardTable table);
	
	/**
	 * 
	 * If a player selects three cards that are a set, this method is called to tell the UI to indicate that the selected
	 * cards are indeed a set.
	 *
	 */
	void confirmSet();
	
	/**
	 * 
	 * If a player selects three cards that are not a set, this method is called to tell the UI to indicate that the selected
	 * cards are not a set.
	 *
	 */
	void rejectSet();
	
	/**
	 * 
	 * If a player causes a situation to occur where more cards need to be drawn, and the deck is empty, send a message
	 * to the UI to indicate as much.
	 *
	 */
	void indicateOutOfCardsToDraw();
	
	/**
	 * When there are no more cards to draw, and there are no more sets on the table, the game is over.
	 * Inform the UI of this fact.
	 *
	 */
	void displayEndOfGame();
	
	/**
	 * 
	 * If you have 21 cards on the table, you are mathematically guaranteed to have a set out there somewhere.
	 * This method is used if the player attempts to draw more cards when none are needed.
	 *
	 */
	void indicateNoNeedToDrawMoreCards();
	
	/**
	 * 
	 * There has been a change on the player's information.  Update the UI with the appropriate changes.
	 *
	 * @param player The player whose updated data needs to be transfered to the client UI.
	 */
	void updatePlayer(Player player);

}
