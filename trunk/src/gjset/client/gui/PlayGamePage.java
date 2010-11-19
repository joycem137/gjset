package gjset.client.gui;

import gjset.client.ClientGUIController;
import gjset.client.ClientGUIModel;
import gjset.gui.MainFrame;
import gjset.gui.SimpleLookAndFeel;
import gjset.gui.framework.BigButton;
import gjset.gui.framework.FancyLabel;
import gjset.gui.framework.Page;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingConstants;

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
	private static final int BUTTON_INSET = 200;
	
	private MainFrame	mainFrame;
	private ClientGUIController controller;
	private SimpleLookAndFeel lnf;
	
	// All of the various screen components.
	private DeckPanel deckPanel;
	private BigButton callSetButton;
	private BigButton drawButton;
	private CardTablePanel cardTablePanel;

	public PlayGamePage(ClientGUIController controller, MainFrame mainFrame)
	{
		super();
		
		this.mainFrame = mainFrame;
		lnf = SimpleLookAndFeel.getLookAndFeel();
		
		configurePage();

		createCardTable();
		createDeck();
		createButtons();

		// Obtain the game controller
		this.controller = controller;
		controller.getClientGUIModel().addObserver(this);
	}
	
	/**
	 * Create the objects that are going to lie on the table.
	 *
	 */
	private void createCardTable()
	{
		cardTablePanel = new CardTablePanel(controller);
		add(cardTablePanel);
	}

	/**
	 * Create the call set and draw buttons.
	 *
	 */
	private void createButtons()
	{
		callSetButton = createButtonAndLabel(new AbstractAction("Call Set")
		{
			public void actionPerformed(ActionEvent evt)
			{
				controller.callSet();
			}
		}, "button_call", BUTTON_INSET, false);
		
		drawButton = createButtonAndLabel(new AbstractAction("Draw")
		{
			public void actionPerformed(ActionEvent evt)
			{
				controller.drawMoreCards();
			}
		}, "button_draw", getWidth() - BUTTON_INSET, true); // This call to the call set button width is a bit of a hack.
	}
	
	/**
	 * 
	 * Create a big button and add a label underneath it.
	 *
	 * @param action
	 * @param style
	 * @param xPos
	 * @param adjustXPos
	 * 
	 * @return
	 */
	private BigButton createButtonAndLabel(Action action, String style, int xPos, boolean adjustXPos)
	{
		// Add the button
		BigButton button = new BigButton(action, style);
		button.setDisabled(true);
		add(button);
		
		// Adjust the x position by the width of the button.
		if(adjustXPos)
		{
			xPos -= button.getWidth();
		}
		
		button.setLocation(xPos, 620);
		
		FancyLabel label = new FancyLabel((String)action.getValue(Action.NAME), SwingConstants.CENTER);
		label.setFancyEffect(FancyLabel.OUTLINE);
		label.setBackground(lnf.getBigButtonLabelBackground());
		label.setForeground(lnf.getBigButtonLabelForeground());
		label.setFont(lnf.getBigButtonFont());
		
		label.setSize(button.getWidth(), button.getHeight());
		label.setLocation(xPos, 620 + button.getHeight() - 15);
		add(label);
		
		return button;
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
	 * Check all of the objects in the model to determine whether any have changed.
	 *
	 * @param observable
	 * @param object
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object object)
	{
		ClientGUIModel model = (ClientGUIModel) observable;
		
		// Update the deck
		int cardsInDeck = model.getCardsInDeck();
		deckPanel.updateSize(cardsInDeck);
		
		// Determine if we can call set or draw more cards.
		drawButton.setDisabled(!model.canDrawCards());
		callSetButton.setDisabled(!model.canCallSet());
		
		// Update the card table
		cardTablePanel.update(model.getCardTable());
		
		repaint();
	}
}
