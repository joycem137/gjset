package gjset.client.gui.pages;

import gjset.GameConstants;
import gjset.client.ClientGUIController;
import gjset.client.ClientGUIModel;
import gjset.client.gui.CardTablePanel;
import gjset.client.gui.DeckPanel;
import gjset.client.gui.EventBubble;
import gjset.client.gui.LocalPlayerPanel;
import gjset.client.gui.MainFrame;
import gjset.client.gui.RemotePlayerPanel;
import gjset.data.Card;
import gjset.data.PlayerData;
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
import javax.swing.ActionMap;
import javax.swing.InputMap;
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
	private MainFrame	mainFrame;
	private ClientGUIController controller;
	private SimpleLookAndFeel lnf;
	
	// All of the various screen components.
	private DeckPanel deckPanel;
	private BigButton callSetButton;
	private BigButton drawButton;
	private CardTablePanel cardTablePanel;
	private LocalPlayerPanel localPlayerPanel;
	private List<RemotePlayerPanel> remotePlayerPanels;

	private FancyLabel callSetLabel;

	public PlayGamePage(ClientGUIController controller, MainFrame mainFrame)
	{
		super();

		// Obtain the game controller
		this.controller = controller;
		controller.getModel().addObserver(this);
		
		this.mainFrame = mainFrame;
		lnf = SimpleLookAndFeel.getLookAndFeel();
		
		configurePage();

		createRemotePlayerPanels();
		createCardTable();
		createPlayerPanel();
		createDeck();
		createButtons();
		
		createActionMap();
		createKeyMap();
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
		
		boolean showSetButton = model.getPlayers().size() > 1;
		callSetButton.setVisible(showSetButton);
		callSetLabel.setVisible(showSetButton);
		
		// Update the card table
		boolean isSetFinished = (model.getGameState() == GameConstants.GAME_STATE_SET_FINISHED);
		cardTablePanel.update(model.getCardTable(), isSetFinished, model.getSetCorrect());
		
		// See if we need to display the game over screen.
		if(model.getGameState() == GameConstants.GAME_STATE_GAME_OVER)
		{
			ScorePage page = new ScorePage(mainFrame, controller);
			mainFrame.loadPage(page);
		}
		
		repaint();
	}
	
	/**
	 * 
	 * Destroy this page.
	 *
	 * @see gjset.gui.framework.Page#destroy()
	 */
	public void destroy()
	{
		ClientGUIModel model = controller.getModel();
		model.deleteObserver(this);
		
		mainFrame = null;
		controller = null;
		lnf = null;
		deckPanel = null;
		cardTablePanel = null;
		localPlayerPanel = null;
		remotePlayerPanels = null;
		callSetLabel = null;
	}

	/**
	 * Update the player panels using the list from the model.
	 *
	 * @param players
	 */
	private void updatePlayers(ClientGUIModel model)
	{
		List<PlayerData> players = model.getPlayers();
		
		// Show or hide the local player event bubble.
		localPlayerPanel.setBubbleVisible(players.size() > 1);
		
		int setCallerId = model.getSetCaller();
		
		int remotePlayerPanelIndex = 0;
		Iterator<PlayerData> iterator = players.iterator();
		while(iterator.hasNext())
		{
			PlayerData player = iterator.next();
			if(player.getId() == model.getLocalPlayer().getId())
			{
				// Update the player panel.
				localPlayerPanel.updatePlayerData(player, setCallerId);
			}
			else
			{
				// Update the "other" player panels.
				RemotePlayerPanel panel = remotePlayerPanels.get(remotePlayerPanelIndex);
				panel.updatePlayerData(player, setCallerId);
				panel.setVisible(true);
				
				remotePlayerPanelIndex++;
			}
		}
		
		// Hide all of the remaining remote player panels.
		for(int i = remotePlayerPanelIndex; i < GameConstants.MAX_PLAYERS - 1; i++)
		{
			remotePlayerPanels.get(i).setVisible(false);
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
	 * Create the input map for all of the actions associated with this page.
	 *
	 */
	private void createKeyMap()
	{
		InputMap map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "keyCallSet");
		
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "keySelectCard11");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "keySelectCard12");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "keySelectCard13");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "keySelectCard14");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "keySelectCard15");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0), "keySelectCard16");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0), "keySelectCard17");
		
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "keySelectCard21");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "keySelectCard22");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "keySelectCard23");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "keySelectCard24");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "keySelectCard25");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "keySelectCard26");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0), "keySelectCard27");
		
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "keySelectCard31");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "keySelectCard32");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "keySelectCard33");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "keySelectCard34");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "keySelectCard35");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "keySelectCard36");
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "keySelectCard37");
	}

	/**
	 * Create all of the mappings for the sorts of actions that can be performed on this page.
	 *
	 */
	private void createActionMap()
	{
		ActionMap map = getActionMap();
		
		map.put("keyCallSet", new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				controller.callSet();
			}
		});
		
		for(int r = 1; r <= 3; r++)
		{
			for(int c = 1; c <= 7; c++)
			{
				map.put("keySelectCard" + r + "" + c, new SelectCardAction(r - 1, c - 1));
			}
		}
	}
	
	/**
	 * Create the panels that will display the information for remote players.
	 *
	 */
	private void createRemotePlayerPanels()
	{
		int INSET = 5;
		Rectangle playingFrame = MainFrame.PLAYING_FIELD_AREA;
		
		remotePlayerPanels = new Vector<RemotePlayerPanel>();
		
		for(int i = 0; i < GameConstants.MAX_PLAYERS - 1; i++)
		{
			RemotePlayerPanel panel = new RemotePlayerPanel();
			
			int x = playingFrame.x + INSET;
			
			if( i % 2 == 1 )
			{
				x = (playingFrame.y + playingFrame.width) - INSET - panel.getWidth();
				panel.setOrientation(EventBubble.ORIENT_RIGHT);
			}
			
			int y = playingFrame.y + (i / 2) * (panel.getMainHeight() + 2) + INSET;
			
			// locate the panel.
			panel.setLocation(x, y);
			
			remotePlayerPanels.add(panel);
			panel.setVisible(false);
		}
		
		// Add the panels in reverse to allow them to overlap correctly.
		for(int i = remotePlayerPanels.size() - 1; i >= 0; i--)
		{
			add(remotePlayerPanels.get(i));
		}
	}

	/**
	 * Create the player's information panel.
	 *
	 */
	private void createPlayerPanel()
	{
		localPlayerPanel = new LocalPlayerPanel();
		add(localPlayerPanel);
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
		Rectangle usableArea = MainFrame.CONTROL_PANEL_AREA;
		int leftCenter = (localPlayerPanel.getX() - usableArea.x) / 2 + usableArea.x;
		
		callSetButton = createButton(new AbstractAction()
		{
			public void actionPerformed(ActionEvent evt)
			{
				controller.callSet();
			}
		}, "button_call", leftCenter);

		callSetLabel = createLabel(callSetButton, "Combo", leftCenter);
		
		int rightCenter
			= (usableArea.x + usableArea.width - localPlayerPanel.getX() - localPlayerPanel.getWidth()) / 2 
			+ localPlayerPanel.getX() + localPlayerPanel.getWidth() - 15;
		
		drawButton = createButton(new AbstractAction("Draw")
		{
			public void actionPerformed(ActionEvent evt)
			{
				controller.drawMoreCards();
			}
		}, "button_draw", rightCenter);
	
		createLabel(drawButton, "Draw", rightCenter);
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
	private BigButton createButton(Action action, String style, int centerX)
	{
		// Add the button
		BigButton button = new BigButton(action, style);
		button.setDisabled(true);
		add(button);
		
		int xPos = centerX - button.getWidth() / 2;
		
		button.setLocation(xPos, 620);
		
		return button;
	}
	
	private FancyLabel createLabel(BigButton button, String text, int centerX)
	{
		FancyLabel label = new FancyLabel(text, SwingConstants.CENTER);
		label.setFancyEffect(FancyLabel.OUTLINE);
		label.setBackground(lnf.getBigButtonLabelBackground());
		label.setForeground(lnf.getBigButtonLabelForeground());
		label.setFont(lnf.getBigButtonFont());
		
		label.setSize(button.getWidth(), button.getHeight());
		
		int xPos = centerX - label.getWidth() / 2;
		
		label.setLocation(xPos, 620 + button.getHeight() - 15);
		add(label);
		
		return label;
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
	 * A helper class that is used to select cards.
	 */
	private class SelectCardAction extends AbstractAction
	{
		private int row;
		private int col;
	
		public SelectCardAction(int row, int col)
		{
			super();
			
			this.row = row;
			this.col = col;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			// Get the indicated card and then tell the controller to select it.
			Card card = controller.getModel().getCardTable().getCardAt(row, col);
			controller.selectCard(card);
		}
	}
}
