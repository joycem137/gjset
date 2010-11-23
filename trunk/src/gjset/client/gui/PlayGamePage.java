package gjset.client.gui;

import gjset.GameConstants;
import gjset.client.ClientGUIController;
import gjset.client.ClientGUIModel;
import gjset.data.Player;
import gjset.gui.MainFrame;
import gjset.gui.framework.BigButton;
import gjset.gui.framework.FancyLabel;
import gjset.gui.framework.Page;
import gjset.gui.framework.SimpleLookAndFeel;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

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
	private PlayerPanel playerPanel;
	private List<OtherPlayerPanel> otherPlayerPanels;

	public PlayGamePage(ClientGUIController controller, MainFrame mainFrame)
	{
		super();

		// Obtain the game controller
		this.controller = controller;
		controller.getModel().addObserver(this);
		
		this.mainFrame = mainFrame;
		lnf = SimpleLookAndFeel.getLookAndFeel();
		
		configurePage();

		createCardTable();
		createDeck();
		createButtons();
		createPlayerPanel();
		createOtherPlayerPanels();
		
		createKeyboardListener();
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
		
		// Update the players.
		updatePlayers(model);
		
		// Update the deck
		int cardsInDeck = model.getCardsInDeck();
		deckPanel.updateSize(cardsInDeck);
		
		// Determine if we can call set or draw more cards.
		drawButton.setDisabled(!model.canDrawCards());
		callSetButton.setDisabled(!model.canCallSet());
		
		// Update the card table
		cardTablePanel.update(model.getCardTable());
		
		// See if we need to display the game over screen.
		if(model.getGameState() == GameConstants.GAME_STATE_GAME_OVER)
		{
			controller.startNewGame();
		}
		
		repaint();
	}

	/**
	 * Update the player panels using the list from the model.
	 *
	 * @param players
	 */
	private void updatePlayers(ClientGUIModel model)
	{
		List<Player> players = model.getPlayers();
		
		int otherPlayerPanelIndex = 0;
		Iterator<Player> iterator = players.iterator();
		while(iterator.hasNext())
		{
			Player player = iterator.next();
			if(player.getId() == model.getLocalPlayer().getId())
			{
				// Update the player panel.
				playerPanel.updatePlayerData(player);
			}
			else
			{
				// Update the "other" player panels.
				OtherPlayerPanel panel = otherPlayerPanels.get(otherPlayerPanelIndex);
				panel.updatePlayerData(player);
				panel.setVisible(true);
				
				otherPlayerPanelIndex++;
			}
		}
		
		// Hide all of the remaining other player panels.
		for(int i = otherPlayerPanelIndex; i < GameConstants.MAX_PLAYERS - 1; i++)
		{
			otherPlayerPanels.get(i).setVisible(false);
		}
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
	 * Check for incoming keyboard commands.
	 *
	 */
	private void createKeyboardListener()
	{
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "keyCallSet");
		getActionMap().put("keyCallSet", new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				controller.callSet();
			}
		});
	}
	
	/**
	 * Create the panels that will display the information for remote players.
	 *
	 */
	private void createOtherPlayerPanels()
	{
		int INSET = 5;
		Rectangle playingFrame = MainFrame.PLAYING_FIELD_AREA;
		
		otherPlayerPanels = new Vector<OtherPlayerPanel>();
		
		for(int i = 0; i < GameConstants.MAX_PLAYERS - 1; i++)
		{
			OtherPlayerPanel panel = new OtherPlayerPanel();
			
			int x = playingFrame.x + INSET;
			
			if( i % 2 == 1 )
			{
				x = (playingFrame.y + playingFrame.width) - INSET - panel.getWidth();
			}
			
			int y = playingFrame.y + (i / 2) * (panel.getHeight() + 2) + INSET;
			
			// locate the panel.
			panel.setLocation(x, y);
			
			add(panel);
			otherPlayerPanels.add(panel);
			panel.setVisible(false);
		}
	}

	/**
	 * Create the player's information panel.
	 *
	 */
	private void createPlayerPanel()
	{
		playerPanel = new PlayerPanel();
		add(playerPanel);
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
		callSetButton = createButtonAndLabel(new AbstractAction("Combo")
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
}
