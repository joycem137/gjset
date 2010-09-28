/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton and Andrea Kilpatrick
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

import gjset.client.gui.MainGamePanel;
import gjset.data.CardTable;
import gjset.data.Player;

/**
 * This class implements the {@link ClientLinkInterface} locally using ordinary Java message passing.
 * It should be used when the client/player and the engine are on the same machine, i.e. in single player
 * games.
 *
 */
public class LocalClientLink implements ClientLinkInterface
{
	private MainGamePanel	gui;

	/**
	 * 
	 * Blank constructor to emphasize that nothing happens during object instantiation.
	 */
	public LocalClientLink()
	{
	}
	
	/**
	 * Used to provide the LocalClientLInk with a link to the local player UI running on this system.
	 * <P>
	 * <B>Note:</b> This method MUST be called prior to using the class normally, as without it, there is no
	 * connection to the {@link MainGamePanel}
	 * 
	 * @param gui The {@link MainGamePanel} to forward messages to.
	 */
	public void setGUI(MainGamePanel gui)
	{
		this.gui = gui;
	}

	/**
	 * 
	 * Tell the player's UI to do whatever it needs to do to show that a new game has been started.
	 *
	 * @see gjset.engine.ClientLinkInterface#displayNewGame()
	 */
	public void displayNewGame()
	{
		// Remove any cards that were previously on the table.
		gui.getCardTable().reset();

		// Show a message indicating that the game has begun.
		gui.getMessageBar().displayMessage("Welcome to gjSet!");

		// Show the player panel
		gui.showPlayerPanel();
	}

	/**
	 * 
	 * If a player selects three cards that are a set, this method is called to tell the UI to indicate that the selected
	 * cards are indeed a set.
	 *
	 * @see gjset.engine.ClientLinkInterface#confirmSet()
	 */
	public void confirmSet()
	{
		// Display a message indicating that this is a set.
		gui.getMessageBar().displayMessage("That's a set!");
	}

	/**
	 * 
	 * Tell the player's UI that the card table has changed.  This method provides the data that will be transferred
	 * to the player/client. 
	 *
	 * @param table The {@link CardTable} object that has been updated and should be updated on the client.
	 * @see gjset.engine.ClientLinkInterface#updateTable(gjset.data.CardTable)
	 */
	public void updateTable(CardTable table)
	{
		gui.getCardTable().update(table);
	}

	/**
	 * 
	 * If a player causes a situation to occur where more cards need to be drawn, and the deck is empty, send a message
	 * to the UI to indicate as much.
	 *
	 * @see gjset.engine.ClientLinkInterface#indicateOutOfCardsToDraw()
	 */
	public void indicateOutOfCardsToDraw()
	{
		gui.getMessageBar().displayMessage("There are no more cards to draw.");
	}

	/**
	 * 
	 * If a player selects three cards that are not a set, this method is called to tell the UI to indicate that the selected
	 * cards are not a set.
	 *
	 * @see gjset.engine.ClientLinkInterface#rejectSet()
	 */
	public void rejectSet()
	{
		// Display a message on the gui.
		gui.getMessageBar().displayMessage("That's not a set!");
	}

	/**
	 * 
	 * When there are no more cards to draw, and there are no more sets on the table, the game is over.
	 * Inform the UI of this fact.
	 *
	 * @see gjset.engine.ClientLinkInterface#displayEndOfGame()
	 */
	public void displayEndOfGame()
	{
		gui.hidePlayerPanel();
		gui.getMessageBar().displayMessage("No sets remain.  YOU WIN!");
	}

	/**
	 * 
	 * If you have 21 cards on the table, you are mathematically guaranteed to have a set out there somewhere.
	 * This method is used if the player attempts to draw more cards when none are needed.
	 *
	 * @see gjset.engine.ClientLinkInterface#indicateNoNeedToDrawMoreCards()
	 */
	public void indicateNoNeedToDrawMoreCards()
	{
		gui.getMessageBar().displayMessage("You don't need to draw more cards.");
	}

	/**
	 * 
	 * There has been a change on the player's information.  Update the UI with the appropriate changes.
	 *
	 * @param player The player whose updated data needs to be transfered to the client UI.
	 * @see gjset.engine.ClientLinkInterface#updatePlayer(gjset.data.Player)
	 */
	public void updatePlayer(Player player)
	{
		gui.getPlayer().drawPanel(player);
	}
}
