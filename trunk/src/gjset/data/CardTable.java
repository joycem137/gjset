package gjset.data;

import gjset.engine.ClientInterface;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008-2009 Joyce Murton
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

public class CardTable
{
	private List<Point>			cardPositions;				// Store the row and cols of all the cards in the above list.

	// Store all of the cards on the table.
	private List<Card>			cardsOnTable;				// The actual list of cards.
	private List<Card>			highlightedCards;			//Show all of the highlighted cards.
	
	// Store the geometry of where the cards belong.
	private Card[][]			grid;						// The actual grid of cards.
	private int					gridRows;					// How many rows we are currently using.
	private int					gridCols;					// How many cols we are currently using.

	public static final int		CARD_LIMIT	= 21;		// max number of cards on the table at a time	
	public static final int		ROW_LIMIT	= 7;		// max number of rows on the table at a time	
	
	private ClientInterface		player;

	public CardTable(ClientInterface player)
	{
		//Store a link to the player interface.
		this.player = player;

		// And initialize the places to store the positions of cards.
		cardPositions = new Vector<Point>();

		// Create the grid on which to place cards.
		grid = new Card[3][ROW_LIMIT];
		gridRows = 0;
		gridCols = 0;

		// Create the vectors for holding cards.
		cardsOnTable = new Vector<Card>();
		highlightedCards = new Vector<Card>();
	}

	public CardTable(int numRows, int numCols, Vector<Card> cardList, Vector<Card> highlightedCards)
	{
		this.player = null;
		
		//Initialize the places to store the positions of cards.
		cardPositions = new Vector<Point>();

		// Create the grid on which to place cards.
		grid = new Card[3][ROW_LIMIT];
		
		//Just grab the highlighted cards list outright.
		this.highlightedCards = highlightedCards;
		
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

	public boolean isHighlighted(Card card)
	{
		return highlightedCards.contains(card);
	}

	public void setHighlight(Card card, boolean value)
	{
		if(value)
		{
			highlightedCards.add(card);
		}
		else
		{
			highlightedCards.remove(card);
		}
		
		player.updateTable(this);
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

		// Finally, update the layout with the latest data.
		player.updateTable(this);
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

		// Then we update the layout.
		player.updateTable(this);
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
		highlightedCards.clear();

		// Clean up the screen.
		player.resetTable();
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

		// Redraw the table.
		player.updateTable(this);
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

	/**
	 * 
	 * @return numCards - The number of cards currently on the table.
	 */
	public int getNumCards()
	{
		return cardsOnTable.size();
	}

	public List<Card> getCards()
	{
		return cardsOnTable;
	}

	public int getRows()
	{
		return gridRows;
	}

	public int getCols()
	{
		return gridCols;
	}

	public Card getCardAt(int row, int col)
	{
		return grid[row][col];
	}

	public String getRepresentation()
	{
		String representation = new String();
		
		//Define the table.
		representation += "" + gridRows;
		representation += "" + gridCols;
		
		//Describe each card on the table.
		for(int r = 0; r < gridRows; r++)
		{
			for(int c = 0; c < gridCols; c++)
			{
				Card card = grid[r][c];
				representation += card.getRepresentation();
				if(isHighlighted(card))
				{
					representation += "1";
				}
				else
				{
					representation += "0";
				}
			}
		}
		
		return representation;
	}

	public static CardTable parseTable(String representation)
	{
		//Get the number of rows and columns.
		int numRows = representation.charAt(0) - '0';
		int numCols = representation.charAt(1) - '0';
		
		//Get all of the cards on the table.
		Vector<Card> cardList = new Vector<Card>();
		Vector<Card> highlightedCards = new Vector<Card>();

		for(int i = 0; i < numRows * numCols; i++)
		{
			String cardRepresentation = representation.substring(2 + i * 5, 6 + i * 5);
			Card card = Card.parseCard(cardRepresentation);
			cardList.add(card);
			
			if(representation.charAt(6 + i * 5) == '1')
			{
				highlightedCards.add(card);
			}
		}
		
		return new CardTable(numRows, numCols, cardList, highlightedCards);
	}
}
