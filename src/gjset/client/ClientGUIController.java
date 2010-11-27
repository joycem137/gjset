package gjset.client;

import gjset.data.Card;

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
 * The interface for gui controllers on the client.  Allows different kinds of GUI controllers.
 */
public interface ClientGUIController
{

	/**
	 * Returns the client's GUI model
	 *
	 * @return
	 */
	ClientGUIModel getModel();

	/**
	 * Initiates a request to call set from the client machine.
	 *
	 */
	void callSet();

	/**
	 * Initiates a request to draw more cards on the client machine.
	 *
	 */
	void drawMoreCards();

	/**
	 * Initiates a request from the client to select the indicated card. This may cause a "set" to be called.
	 *
	 * @param cardData
	 */
	void selectCard(Card cardData);

	/**
	 * Requests a new game from the server.
	 *
	 */
	void startNewGame();

	/**
	 * Send a message to the server indicating that this player is to be disconnected.
	 *
	 */
	void disconnectPlayer();

	/**
	 * Add this to a list of methods that will be called upon game start.
	 *
	 * @param runnable
	 */
	void addGameStartTrigger(Runnable runnable);

	/**
	 * Destroy this controller
	 *
	 */
	void destroy();

}
