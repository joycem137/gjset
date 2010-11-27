package gjset.client.gui;

import gjset.client.ClientGUIController;
import gjset.data.Card;
import gjset.data.CardTableData;
import gjset.gui.MainFrame;
import gjset.server.CardTable;
import gjset.server.ServerGameController;

import java.awt.Rectangle;

import javax.swing.JPanel;

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
 * Contains all of the logic necessary for graphically representing a CardTableData object.
 */
@SuppressWarnings("serial")
public class CardTablePanel extends JPanel
{
	private static final int CARD_BUFFER = 20; // The minimum distance between cards.
	
	// The frame that we are working within.
	private Rectangle playingFrame;
	
	// A container JPanel to store CardPanels within.
	private JPanel cardPane;

	// The controller for passing along to CardPanel objects that we work with.
	private ClientGUIController controller;

	/**
	 * Constructs your basic card table.
	 *
	 * @param controller
	 */
	public CardTablePanel(ClientGUIController controller)
	{
		super();
		
		this.controller = controller;
		
		configurePanel();
		addCardPane();
	}

	/**
	 * 
	 * This method should be called any time the {@link CardTable} data changes within the {@link ServerGameController}.
	 * This method will be called any time the highlighting, physical arrangement, or number/composition of
	 * cards on the table changes.
	 *
	 * @param cardTable A CardTableData object containing the latest updates on the cards that are on the table.
	 * @param isSetFinished		boolean determining whether a player just finished selecting a set.
	 * @param isSetCorrect		boolean determining whether the last set called is correct.
	 */
	public void update(CardTableData cardTable, boolean isSetFinished, boolean isSetCorrect)
	{
		//Get the number of rows and cols from the card table data.
		int gridRows = cardTable.getRows();
		int gridCols = cardTable.getCols();
		
		//If this is a "null" table, then reset the table.
		if(gridRows == 0 && gridCols == 0)
		{
			reset();
			return;
		}
		
		int horizontalBuffer = CARD_BUFFER;
		
		// Prevent the card pane from getting too big.
		if(gridCols == 7)
		{
			horizontalBuffer = CARD_BUFFER / 4;
		}
		
		// Remove all of the cards from the card pane.
		cardPane.removeAll();

		// Now put all of the cards back.
		CardPanel cardComponent = null;
		for (int row = 0; row < gridRows; row++)
		{
			for (int col = 0; col < gridCols; col++)
			{
				Card card = cardTable.getCardAt(row, col);
				cardComponent = new CardPanel(controller, card);
				cardPane.add(cardComponent);
				
				// Determine the kind of highlighting the cards should use based on the game status.
				int highlightType = CardPanel.NORMAL;
				if (isSetFinished)
				{
					highlightType = (isSetCorrect ? CardPanel.CORRECT_SET : CardPanel.INCORRECT_SET);
				}
				
				cardComponent.setHighlighted(cardTable.isHighlighted(card), highlightType);
				
				cardComponent.setLocation(col * (cardComponent.getWidth() + horizontalBuffer), row * (cardComponent.getHeight() + CARD_BUFFER));
			}
		}

		// Now set up the position of the card pane, to center it on the screen.
		// The x coordinate is back a few cards
		double centerX = getWidth() / 2.0;
		int rowLength = (int) (gridCols * cardComponent.getWidth() + (gridCols - 0.50) * horizontalBuffer);
		int xStart = (int) (centerX - rowLength / 2.0);

		// The y coordinate is up a few cards.
		double centerY = getHeight() / 2.0;
		int colHeight = (int) (gridRows * cardComponent.getHeight() + (gridRows - 0.50) * CARD_BUFFER);
		int yStart = (int) (centerY - colHeight / 2.0);

		// Relocate the card pane.
		cardPane.setBounds(xStart, yStart, rowLength, colHeight);
		
		cardPane.revalidate();
		cardPane.repaint();
	}

	/**
	 * 
	 * Reset the card table to its clean condition.  Used when finishing a game.
	 *
	 */
	public void reset()
	{
		//Clear all cards from the screen.
		cardPane.removeAll();
		cardPane.revalidate();
		cardPane.repaint();
	}

	/**
	 * Configure the raw settings of the panel.
	 *
	 */
	private void configurePanel()
	{
		setLayout(null);
		setOpaque(false);
		
		playingFrame = MainFrame.PLAYING_FIELD_AREA;
		
		setLocation(playingFrame.x, playingFrame.y);
		setSize(playingFrame.width, playingFrame.height);
	}

	/**
	 * Add a card pane to store the cards.
	 *
	 */
	private void addCardPane()
	{
		// Create the card pane
		cardPane = new JPanel();
		cardPane.setLayout(null);
		cardPane.setOpaque(false);
		add(cardPane);
	}

}
