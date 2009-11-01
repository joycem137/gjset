/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton
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

import gjset.client.gui.PlayerUI;
import gjset.data.CardTable;
import gjset.data.Player;

public class LocalClientLink implements ClientLinkInterface
{
	private PlayerUI	gui;

	public LocalClientLink()
	{
	}

//	@Override
	public void displayNewGame()
	{
		// Remove any cards that were previously on the table.
		gui.getCardTable().reset();

		// Show a message indicating that the game has begun.
		gui.getMessageBar().displayMessage("Welcome to gjSet!");

		// Show the player panel
		gui.showPlayerPanel();
	}
	
	//Connect the GUI to the system.
	public void setGUI(PlayerUI gui)
	{
		this.gui = gui;
	}

//	@Override
	public void confirmSet()
	{
		// Display a message indicating that this is a set.
		gui.getMessageBar().displayMessage("That's a set!");
	}

//	@Override
	public void updateTable(CardTable table)
	{
		gui.getCardTable().update(table);
	}

//	@Override
	public void indicateOutOfCardsToDraw()
	{
		gui.getMessageBar().displayMessage("There are no more cards to draw.");
	}

//	@Override
	public void rejectSet()
	{
		// Display a message on the gui.
		gui.getMessageBar().displayMessage("That's not a set!");
	}

//	@Override
	public void displayEndOfGame()
	{
		gui.hidePlayerPanel();
		gui.getMessageBar().displayMessage("No sets remain.  YOU WIN!");
	}

//	@Override
	public void indicateNoNeedToDrawMoreCards()
	{
		gui.getMessageBar().displayMessage("You don't need to draw more cards.");
	}

//	@Override
	public void resetTable()
	{
		gui.getCardTable().reset();
	}

	public void updatePlayer(Player player)
	{
		gui.getPlayer().drawPanel(player);
	}
}
