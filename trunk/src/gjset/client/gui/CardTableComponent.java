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
import gjset.data.Card;
import gjset.data.CardTable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Handles the work of representing a {@link CardTable} object to the user.
 */
public class CardTableComponent extends JPanel
{
	// This is used to handle laying out the cards in a grid for the moment.
	private GridLayout			layout;
	private JPanel				cardPane;

	// Store a link to the game engine
	private EngineLinkInterface		engine;

	// Set our parameters
	private final int			CARD_BUFFER			= 20;					// The minimum distance between cards.
	private final Color			backgroundColor		= new Color(0, 102, 0); // Store the background color to be used here.

	/**
	 * 
	 * This constructor takes a link to the {@link GameEngine} and initializes all of its graphical elements.
	 *
	 * @param engine A link to the {@link GameEngine}
	 */
	public CardTableComponent(EngineLinkInterface engine)
	{
		super();

		// Obtain the game controller
		this.engine = engine;

		// Set the size of the card table.
		setPreferredSize(new Dimension(720, 497));
		setMinimumSize(new Dimension(720, 497));
		setMaximumSize(new Dimension(720, 497));

		// Give the card table a border
		setBorder(BorderFactory.createLineBorder(Color.black));

		// Give the card table a background color.
		setBackground(backgroundColor);

		// Create the card pane
		cardPane = new JPanel();
		cardPane.setBackground(backgroundColor);

		// Set up the layout
		layout = new GridLayout(3, 4, CARD_BUFFER, CARD_BUFFER);
		cardPane.setLayout(layout);

		// Add the card pane to the panel.
		setLayout(null);
		add(cardPane);
	}

	/**
	 * 
	 * This method should be called any time the {@link CardTable} data changes within the {@link GameEngine}.
	 * This method will be called any time the highlighting, physical arrangement, or number/composition of
	 * cards on the table changes.
	 *
	 * @param cardTable A CardTable object containing the latest updates on the cards that are on the table.
	 */
	public void update(CardTable cardTable)
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
		
		// Set the layout with the rows and columns.
		layout.setRows(gridRows);
		layout.setColumns(gridCols);

		// Remove all of the cards from the card pane.
		cardPane.removeAll();

		// Now put all of the cards back.
		for (int row = 0; row < gridRows; row++)
		{
			for (int col = 0; col < gridCols; col++)
			{
				Card card = cardTable.getCardAt(row, col);
				CardComponent cardComponent = new CardComponent(engine, card);
				cardPane.add(cardComponent);
				cardComponent.setHighlight(cardTable.isHighlighted(card));
			}
		}

		// Now set up the position of the card pane, to center it on the screen.
		// The x coordinate is back a few cards
		double centerX = getWidth() / 2.0;
		int rowLength = (int) (gridCols * CardComponent.CARD_WIDTH + (gridCols - 0.50) * CARD_BUFFER);
		int xStart = (int) (centerX - rowLength / 2.0);

		// The y coordinate is up a few cards.
		double centerY = getHeight() / 2.0;
		int colHeight = (int) (gridRows * CardComponent.CARD_HEIGHT + (gridRows - 0.50) * CARD_BUFFER);
		int yStart = (int) (centerY - colHeight / 2.0);

		// Relocate the card pane.
		cardPane.setBounds(xStart, yStart, rowLength, colHeight);

		// Repaint the component.
		cardPane.doLayout();
		repaint();
	}

	/**
	 * 
	 * Reset the card table to its pristine condition.  Used when finishing a game.
	 *
	 */
	public void reset()
	{
		//Clear all cards from the screen.
		cardPane.removeAll();
		cardPane.doLayout();
		cardPane.repaint();
	}

}