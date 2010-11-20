package gjset.data;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
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
 * This class stores all of the information inherent in a card table.
 */
public class CardTableData
{
	protected List<Point>			cardPositions;				// Store the row and cols of all the cards in the above list.

	// Store all of the cards on the table.
	protected List<Card>			cardsOnTable;				// The actual list of cards.
	protected Vector<Card>			selectedCards;				//Show all of the highlighted cards.
	
	// Store the geometry of where the cards belong.
	protected Card[][]			grid;						// The actual grid of cards.
	protected int					gridRows;					// How many rows we are currently using.
	protected int					gridCols;					// How many cols we are currently using.

	public static final int		CARD_LIMIT	= 21;		// max number of cards on the table at a time	
	public static final int		ROW_LIMIT	= 7;		// max number of rows on the table at a time	
	
	public CardTableData()
	{
		// And initialize the places to store the positions of cards.
		cardPositions = new Vector<Point>();

		// Create the grid on which to place cards.
		grid = new Card[3][ROW_LIMIT];
		gridRows = 0;
		gridCols = 0;

		// Create the vectors for holding cards.
		cardsOnTable = new Vector<Card>();
		selectedCards = new Vector<Card>();
	}
	
	public CardTableData(Element root)
	{
		this();
		parseTable(root);
	}

	public boolean isHighlighted(Card card)
	{
		return selectedCards.contains(card);
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

	public Element getRepresentation()
	{
		DefaultElement root = new DefaultElement("cardtable");
		
		DefaultElement sizeElement = new DefaultElement("size");
		root.add(sizeElement);
		
		//Define the table.
		DefaultElement rowsElement = new DefaultElement("rows");
		rowsElement.setText("" + gridRows);
		sizeElement.add(rowsElement);
		
		DefaultElement colsElement = new DefaultElement("cols");
		colsElement.setText("" + gridCols);
		sizeElement.add(colsElement);
		
		DefaultElement cardsElement = new DefaultElement("cards");
		root.add(cardsElement);
		
		//Describe each card on the table.
		for(int r = 0; r < gridRows; r++)
		{
			for(int c = 0; c < gridCols; c++)
			{
				Card card = grid[r][c];
				Element cardElement = card.getXMLRepresentation();
				
				if(isHighlighted(card))
				{
					cardElement.addAttribute("highlighted", "true");
				}
				
				cardsElement.add(cardElement);
			}
		}
		
		return root;
	}

	@SuppressWarnings("rawtypes")
	private void parseTable(Element root)
	{
		//Get the number of rows and columns.
		String numRowsString = root.element("size").element("rows").getText();
		gridRows = Integer.parseInt(numRowsString);
		
		String numColsString = root.element("size").element("cols").getText();
		gridCols = Integer.parseInt(numColsString);
		
		int row = 0;
		int col = 0;
		
		List cardElements = root.element("cards").elements("card");
		Iterator iterator = cardElements.iterator();
		while(iterator.hasNext())
		{
			Element cardElement = (Element)iterator.next();
			Card card = new Card(cardElement);
			
			// Drop the card on the table.
			addCardToTable(card, row, col);
			
			if(cardElement.attributeValue("highlighted", "false").equals("true"))
			{
				selectedCards.add(card);
			}
			
			// Now update the row and column.
			col++;
			if (col >= gridCols)
			{
				col = 0;
				row++;
			}
		}
	}

	/**
	 * Adds the indicated card to the incidate row and column on the screen.
	 *
	 * @param card The card to add
	 * @param row The row to add the card to
	 * @param col The colum to ad the card to.
	 */
	private void addCardToTable(Card card, int row, int col)
	{
		cardsOnTable.add(card);
		cardPositions.add(new Point(row, col));
		grid[row][col] = card;
	}
}
