package gjset.client;

import gjset.client.gui.CardComponent;


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

/**
 * This interface provides a single client/player system with a link to the engine.  
 * That engine might be implemented remotely, as in the case of a remotely hosted game,
 * or it might be implemented locally, as in the case of a single player game.
 * <P>
 * Regardless, this interface allows the client to send various messages to the engine.
 * Any and all communication from the player/client to the engine takes place through
 * these methods. 
 * <P>
 * This class is specifically meant to facilitate messages from a {@link PlayerUI} object
 * to a {@link GameEngine} object.
 * 
 * @author Joyce Murton
 * @see GameClient
 * @see LocalEngineLink
 * @see ClientLinkInterface
 */
public interface EngineLinkInterface
{
	/**
	 * 
	 * Tells the engine to select the card represented by the on screen {@link CardComponent} object.
	 *
	 * @author Joyce Murton
	 * @param card The card that was selected by this player/client.
	 */
	void selectCard(CardComponent card);
	
	/**
	 * 
	 * Tells the engine to start a new game.
	 * <P>
	 * At this time, this is all that needs to take place.  This will be overwritten in the future as
	 * starting new games becomes more complex.
	 *
	 * @author Joyce Murton
	 */
	void startNewGame();
	
	/**
	 * 
	 * Tells the engine that the player wishes to end this game.
	 * <P>
	 * At this time, this is a simple method to indicate that a player is quitting the game.
	 * As multiple players are introduced, this method will be scrapped in favor of a method of
	 * detecting dropped player and similar issues.
	 *
	 * @author Joyce Murton
	 */
	void quitGame();
	
	/**
	 * 
	 * Used by the client when the player selects the "No more sets" button.
	 * This indicates that the player thinks there are no more sets on the board
	 * and that the engine should react appropriately.
	 *
	 * @author Joyce Murton
	 */
	void callNoMoreSets();
}
