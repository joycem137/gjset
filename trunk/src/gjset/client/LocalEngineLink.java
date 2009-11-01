package gjset.client;

import gjset.client.gui.CardComponent;
import gjset.engine.GameEngine;

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

/**
 * Implements the {@link EngineLinkInterface} locally.  This means that all communication between the engine
 * and the clients/player objects takes place using standard message passing on the local machine.
 * 
 * @author Joyce Murton
 * @see EngineLinkInterface
 */
public class LocalEngineLink implements EngineLinkInterface
{

	private GameEngine	gc;

	/**
	 * 
	 * Used to provide the LocalEngineLink with a link to the local game engine running on this system.
	 * <P>
	 * <B>Note:</b> This method MUST be called prior to using the class normally, as without it, there is no
	 * connection to the {@link GameEngine}
	 *
	 * @author Joyce Murton
	 * @param gc The GameEngine object that this class will send messages to.
	 */
	public void setEngine(GameEngine gc)
	{
		this.gc = gc;
	}

	/**
	 * 
	 * Tells the engine that the player wishes to end this game.
	 * <P>
	 * At this time, this is a simple method to indicate that a player is quitting the game.
	 * As multiple players are introduced, this method will be scrapped in favor of a method of
	 * detecting dropped player and similar issues.
	 *
	 * @author Joyce Murton
	 * @see gjset.client.EngineLinkInterface#quitGame()
	 */
	public void quitGame()
	{
		gc.quitGame();
	}

	/**
	 * Tells the engine to start a new game.
	 * <P>
	 * At this time, this is all that needs to take place.  This will be overwritten in the future as
	 * starting new games becomes more complex.
	 *
	 * @author Joyce Murton
	 * @see gjset.client.EngineLinkInterface#startNewGame()
	 */
	public void startNewGame()
	{
		gc.newGame();
	}

	/**
	 * 
	 * Used by the client when the player selects the "No more sets" button.
	 * This indicates that the player thinks there are no more sets on the board
	 * and that the engine should react appropriately.
	 *
	 * @author Joyce Murton
	 * @see gjset.client.EngineLinkInterface#callNoMoreSets()
	 */
	public void callNoMoreSets()
	{
		gc.noMoreSets();
	}

	/**
	 * 
	 * Tells the engine to select the card represented by the on screen {@link CardComponent} object.
	 *
	 * @author Joyce Murton
	 * @param card The card that was selected by this player/client.
	 * @see gjset.client.EngineLinkInterface#selectCard(gjset.client.gui.CardComponent)
	 */
	public void selectCard(CardComponent card)
	{
		gc.selectCard(card.getCard());
	}

}
