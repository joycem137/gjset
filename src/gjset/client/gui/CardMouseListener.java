package gjset.client.gui;

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

import gjset.client.EngineLinkInterface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This listens to mouse actions for a given {@link CardComponent} object.
 * This class is created with an {@link EngineLinkInterface} to communicate with the {@link GameEngine}.
 * When clicked, this object tells the engine to select the associated card.
 * 
 * @see CardComponent
 */
public class CardMouseListener extends MouseAdapter
{
	private CardComponent	card;
	private EngineLinkInterface	engine;

	/**
	 * 
	 * The constructor takes in links to the associated {@link CardComponent} and {@link EngineLinkInterface} and stores them.
	 *
	 * @param card The card that this listener is attached to.
	 * @param engine The link to the game engine.
	 */
	public CardMouseListener(CardComponent card, EngineLinkInterface engine)
	{
		this.card = card;
		this.engine = engine;
	}

	/**
	 * 
	 * Tells the {@link GameEngine} that this player has selected the associated {@link CardComponent} object.
	 *
	 * @param me The {@link MouseEvent} associated with this mouse action.
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me)
	{
		engine.selectCard(card);
	}
}
