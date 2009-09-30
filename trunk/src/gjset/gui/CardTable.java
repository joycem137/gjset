package gjset.gui;

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

import gjset.engine.Card;
import gjset.engine.GameController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CardTable extends JPanel
{
	private static final long	serialVersionUID	= 1L;

	// Store all of the cards on the table.
	private List<CardComponent>	cardsOnTable;								// The actual list of cards.
	private List<Point>			cardPositions;								// Store the row and cols of all the cards in the above list.

	// Store the geometry of where the cards belong.
	private CardComponent[][]	grid;										// The actual grid of cards.
	private int					gridRows;									// How many rows we are currently using.
	private int					gridCols;									// How many cols we are currently using.

	// This is used to handle laying out the cards in a grid for the moment.
	private GridLayout			layout;
	private JPanel				cardPane;

	// Store a link to the main controller.
	private GameController		gameController;

	// Set our parameters
	private final int			CARD_BUFFER			= 20;					// The minimum distance between cards.
	private final Color			backgroundColor		= new Color(0, 102, 0); // Store the background color to be used here.

	public CardTable(GameController gameController)
	{
		super();

		// Obtain the game controller
		this.gameController = gameController;

		// Set the size of the card table.
		setPreferredSize(new Dimension(720, 497));
		setMinimumSize(new Dimension(720, 497));
		setMaximumSize(new Dimension(720, 497));

		// Give the card table a border
		setBorder(BorderFactory.createLineBorder(Color.black));

		// Give the card table a background color.
		setBackground(backgroundColor);

		// Create the vectors for holding cards.
		cardsOnTable = new Vector<CardComponent>();

		// And initialize the places to store the positions of cards.
		cardPositions = new Vector<Point>();

		// Create the grid on which to place cards.
		grid = new CardComponent[3][6];
		gridRows = 0;
		gridCols = 0;

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
	 * Repositions the cards.
	 */
	protected void updateLayout()
	{
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
				cardPane.add(grid[row][col]);
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

	public void addCards(Vector<Card> cards)
	{
		Vector<CardComponent> cardComponents = createCardComponents(cards);

		// Now position the cards on the grid.
		Iterator<CardComponent> iterator = cardComponents.iterator();

		// Start by dropping cards by column.
		int row = gridRows;
		int col = 0;
		while (iterator.hasNext() && cardsOnTable.size() < 9)
		{
			// Verify that the space is not in use.
			// System.out.println("Checking grid at " + row + ", " + col + " with " + cardsOnTable.size() + " cards.");
			if (grid[row][col] == null)
			{
				dealCardToTable(iterator.next(), new Point(row, col));
			}

			// Move to the next column.
			col++;

			// Move to the next row if needed..
			if (col >= 3)
			{
				col = 0;
				row++;
			}
		}

		// Now let's do that again! Only this time, do it by rows.
		row = 0;
		col = gridCols;
		while (iterator.hasNext())
		{
			// System.out.println("Checking grid at " + row + ", " + col + " with " + cardsOnTable.size() + " cards.");
			// Verify that the space is not in use.
			if (grid[row][col] == null)
			{
				dealCardToTable(iterator.next(), new Point(row, col));
			}

			// Move to the next row.
			row++;

			// Move to the next column if needed.
			if (row >= gridRows)
			{
				col++;
				row = 0;
			}
		}

		// Finally, update the layout with the latest data.
		updateLayout();
	}

	public void replaceCards(List<CardComponent> selectedCards, List<Card> newCards)
	{
		assert selectedCards.size() == newCards.size();

		// Store the positions of the cards being removed.
		Vector<Point> removedPositions = new Vector<Point>(selectedCards.size());

		// Start by removing the cards that we selected.
		for (int i = 0; i < selectedCards.size(); i++)
		{
			// Grab the card we are going to remove.
			CardComponent cardToRemove = selectedCards.get(i);

			// Determine the index of the card being removed.
			int index = cardsOnTable.indexOf(cardToRemove);

			// Store its row and col.
			removedPositions.add(cardPositions.get(index));

			// Finally, remove the card from the table.
			removeCardFromTable(cardToRemove);
		}

		// Build the card components for the new cards.
		Vector<CardComponent> newCardComponents = createCardComponents(newCards);

		// Next, we add the new cards to the same locations.
		for (int i = 0; i < selectedCards.size(); i++)
		{
			// Grab the card we're going to add.
			CardComponent newCard = newCardComponents.get(i);

			// Deal the card to the table.
			dealCardToTable(newCard, removedPositions.get(i));
		}

		// Then we update the layout.
		updateLayout();
	}

	/**
	 * Remove all the cards from the table.
	 */
	public void removeCards()
	{
		// Reset the grid
		grid = new CardComponent[3][6];
		gridRows = 0;
		gridCols = 0;

		// Reset the list of cards.
		cardsOnTable.clear();
		cardPositions.clear();

		// Clean up the screen.
		cardPane.removeAll();
		repaint();
	}

	/**
	 * Remove a list of selected cards from the table.
	 * 
	 * @param selectedCards
	 *            The list of cards that is to be removed from the table.
	 */
	public void removeCards(List<CardComponent> selectedCards)
	{
		// Now remove the cardss
		Iterator<CardComponent> iterator = selectedCards.iterator();
		while (iterator.hasNext())
		{
			// Remove the card from the table.
			removeCardFromTable(iterator.next());
		}

		// We're going to create a new grid and add the information to it.
		if (cardsOnTable.size() > 9)
		{
			// Update the gridRow and gridCol values for the size of the new grid.
			gridRows = 3;
			gridCols = cardsOnTable.size() / gridRows;
		}
		else
		{
			// Update the size of the grid
			gridCols = 3;
			gridRows = cardsOnTable.size() / gridCols;
		}

		// Create a new grid to drop everything into.
		CardComponent newGrid[][] = new CardComponent[3][6];

		// Grab an iterator for the two lists that we care about
		Iterator<Point> locationIterator = cardPositions.iterator();
		List<Point> newPositions = new Vector<Point>();

		// Drop the cards into position.
		for (int row = 0; row < gridRows; row++)
		{
			for (int col = 0; col < gridCols; col++)
			{
				Point point = locationIterator.next();
				moveCard(newGrid, point.x, point.y, row, col);
				newPositions.add(new Point(row, col));
			}
		}

		// Put the new grid over the old grid.
		grid = newGrid;
		cardPositions.clear();
		cardPositions = newPositions;

		// Redraw the table.
		updateLayout();
	}

	private void moveCard(CardComponent[][] newGrid, int row, int col, int newRow, int newCol)
	{
		// System.out.println("Moving card from " + row + ", " + col + " to " + newRow + ", " + newCol);

		assert grid[row][col] != null;

		// For now, this is very simple:
		newGrid[newRow][newCol] = grid[row][col];
	}

	private void removeCardFromTable(CardComponent cardToRemove)
	{
		// Get the index of the card to remove
		int index = cardsOnTable.indexOf(cardToRemove);

		// Identify the row and col of this card.
		Point position = cardPositions.get(index);

		// Now clear the grid.
		grid[position.x][position.y] = null;

		// Remove the card position from the table.
		cardPositions.remove(position);

		// Remove the card from the table.
		cardsOnTable.remove(cardToRemove);

		// We do not want to update gridRow and gridCol, as the appropriate behavior cannot be determined from within this method.
	}

	private void dealCardToTable(CardComponent cardComponent, Point position)
	{
		// Pull the row and col from the point.
		int row = position.x;
		int col = position.y;

		// Actually place the card on the table in the specified position.
		grid[row][col] = cardComponent;

		// Add the card to the table.
		cardsOnTable.add(cardComponent);

		// Link the cards position to the grid that it is in.
		cardPositions.add(position);

		// Update the grid rows and cols.
		gridRows = Math.max(row + 1, gridRows);
		gridCols = Math.max(col + 1, gridCols);

		//System.out.println("Added card to " + row + ", " + col + ".  Grid size is " + gridRows + ", " + gridCols);
	}

	/**
	 * 
	 * @return numCards - The number of cards currently on the table.
	 */
	public int getNumCards()
	{
		return cardsOnTable.size();
	}

	public List<CardComponent> getCards()
	{
		return cardsOnTable;
	}

	private Vector<CardComponent> createCardComponents(List<Card> cards)
	{
		// Create our card component vector
		Vector<CardComponent> cardComponents = new Vector<CardComponent>();

		Iterator<Card> iterator = cards.iterator();
		while (iterator.hasNext())
		{
			cardComponents.add(new CardComponent(gameController, iterator.next()));
		}

		return cardComponents;
	}

}