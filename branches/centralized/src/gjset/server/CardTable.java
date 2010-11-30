package gjset.server;

import gjset.data.Card;
import gjset.data.CardTableData;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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

public class CardTable extends CardTableData
{
	public CardTable()
	{
		super();
	}

	public CardTable(int numRows, int numCols, Vector<Card> cardList, Vector<Card> highlightedCards)
	{		
		super();
		
		//Initialize the places to store the positions of cards.
		cardPositions = new Vector<Point>();

		// Create the grid on which to place cards.
		grid = new Card[3][ROW_LIMIT];
		
		//Just grab the highlighted cards list outright.
		this.selectedCards = highlightedCards;
		
		//Now set our grid size.
		gridRows = numRows;
		gridCols = numCols;

		// Create the vectors for holding cards.
		cardsOnTable = new Vector<Card>();
		
		//Now go through and add all of our cards.
		Iterator<Card> cardIterator = cardList.iterator();
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numCols; c++)
			{
				dealCardToTable(cardIterator.next(), new Point(r, c));
			}
		}
	}

	/**
	 * Return the list of selected cards.
	 *
	 * @return
	 */
	public List<Card> getSelectedCards()
	{
		return selectedCards;
	}

	public void addCards(Vector<Card> cards)
	{
		// Now position the cards on the grid.
		Iterator<Card> iterator = cards.iterator();

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
	}

	public void replaceCards(List<Card> selectedCards, List<Card> newCards)
	{
		assert selectedCards.size() == newCards.size();

		// Store the positions of the cards being removed.
		Vector<Point> removedPositions = new Vector<Point>(selectedCards.size());

		// Start by removing the cards that we selected.
		for (int i = 0; i < selectedCards.size(); i++)
		{
			// Grab the card we are going to remove.
			Card cardToRemove = selectedCards.get(i);

			// Determine the index of the card being removed.
			int index = cardsOnTable.indexOf(cardToRemove);

			// Store its row and col.
			removedPositions.add(cardPositions.get(index));

			// Finally, remove the card from the table.
			removeCardFromTable(cardToRemove);
		}

		// Next, we add the new cards to the same locations.
		for (int i = 0; i < selectedCards.size(); i++)
		{
			// Grab the card we're going to add.
			Card newCard = newCards.get(i);

			// Deal the card to the table.
			dealCardToTable(newCard, removedPositions.get(i));
		}
	}

	/**
	 * Remove all the cards from the table.
	 */
	public void removeCards()
	{
		// Reset the grid
		grid = new Card[3][ROW_LIMIT];
		gridRows = 0;
		gridCols = 0;

		// Reset the list of cards.
		cardsOnTable.clear();
		cardPositions.clear();
		selectedCards.clear();
	}

	/**
	 * Remove a list of selected cards from the table.
	 * 
	 * @param selectedCards
	 *            The list of cards that is to be removed from the table.
	 */
	public void removeCards(List<Card> selectedCards)
	{
		// Now remove the cardss
		Iterator<Card> iterator = selectedCards.iterator();
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
		Card newGrid[][] = new Card[3][ROW_LIMIT];

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
	}

	/**
	 * Toggle selection of the indicated card.
	 *
	 * @param card
	 */
	public void toggleSelection(Card card)
	{
		if(selectedCards.contains(card))
		{
			selectedCards.remove(card);
		}
		else
		{
			selectedCards.add(card);
		}
	}

	/**
	 * Un select all cards.
	 *
	 */
	public void unSelectCards()
	{
		selectedCards.removeAllElements();
	}

	private void moveCard(Card[][] newGrid, int row, int col, int newRow, int newCol)
	{
		// System.out.println("Moving card from " + row + ", " + col + " to " + newRow + ", " + newCol);

		assert grid[row][col] != null;

		// For now, this is very simple:
		newGrid[newRow][newCol] = grid[row][col];
	}

	private void removeCardFromTable(Card cardToRemove)
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

	private void dealCardToTable(Card card, Point position)
	{
		// Pull the row and col from the point.
		int row = position.x;
		int col = position.y;

		// Actually place the card on the table in the specified position.
		grid[row][col] = card;

		// Add the card to the table.
		cardsOnTable.add(card);

		// Link the cards position to the grid that it is in.
		cardPositions.add(position);

		// Update the grid rows and cols.
		gridRows = Math.max(row + 1, gridRows);
		gridCols = Math.max(col + 1, gridCols);

		//System.out.println("Added card to " + row + ", " + col + ".  Grid size is " + gridRows + ", " + gridCols);
	}
}
