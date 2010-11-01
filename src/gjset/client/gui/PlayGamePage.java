package gjset.client.gui;

import gjset.gui.MainFrame;
import gjset.gui.framework.Page;

import java.util.Observable;
import java.util.Observer;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards!
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  The Set Game, card design, and other elements of Set of the Set Game are
 *  registered trademarks of Set Enterprises. 
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
 * This class starts a game
 */
@SuppressWarnings("serial")
public class PlayGamePage extends Page implements Observer
{
	private MainFrame	mainFrame;
	private ClientGUIController controller;
	
	// All of the various screen components.
	private DeckPanel	deckPanel;

	public PlayGamePage(ClientGUIController controller, MainFrame mainFrame)
	{
		super();
		
		this.mainFrame = mainFrame;
		
		configurePage();
		
		createDeck();

		// Obtain the game controller
		this.controller = controller;
		controller.getClientGUIModel().addObserver(this);
	}
	
	/**
	 * Create the deck object.
	 *
	 */
	private void createDeck()
	{
		deckPanel = new DeckPanel();
		add(deckPanel);
	}

	/**
	 * Configure the page's basic settings.
	 *
	 */
	private void configurePage()
	{
		// We'll want to cover the *entire* screen.
		setSize(mainFrame.getSize());
		setLocation(0,0);
	}
	/**
	 * Cycle through all of the screen settings and place objects in their appropriate locations.
	 *
	 * @param observable
	 * @param object
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object object)
	{
		ClientGUIModel model = (ClientGUIModel) observable;
		
		int cardsInDeck = model.getCardsInDeck();
		deckPanel.updateSize(cardsInDeck);
		
	}
}
