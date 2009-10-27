package gjset.data;

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

public class Card
{
	// Store constants for handling the various properties of the cards.
	public static final int	COLOR_RED			= 1;
	public static final int	COLOR_BLUE			= 2;
	public static final int	COLOR_GREEN			= 3;

	public static final int	SHAPE_OVAL			= 1;
	public static final int	SHAPE_SQUIGGLE		= 2;
	public static final int	SHAPE_DIAMOND		= 3;

	public static final int	SHADING_NONE		= 1;
	public static final int	SHADING_FULL		= 2;
	public static final int	SHADING_STRIPED		= 3;

	// Store constant indices for the various properties that exist.
	public static final int	PROP_NUMBER			= 1;
	public static final int	PROP_COLOR			= 2;
	public static final int	PROP_SHADING		= 3;
	public static final int	PROP_SHAPE			= 4;

	// Store the various properties of this card.
	private int				number, color, shading, shape;

	// Create a basic blank card.
	public Card()
	{
		// Clear the values for all cards.
		this.number = 0;
		this.color = 0;
		this.shading = 0;
		this.shape = 0;
	}

	// The basic card constructor
	public Card(int number, int color, int shading, int shape)
	{
		// Store the card properties.
		this.number = number;
		this.color = color;
		this.shading = shading;
		this.shape = shape;
	}

	public int getNumber()
	{
		return number;
	}

	public int getColor()
	{
		return color;
	}

	public int getShape()
	{
		return shape;
	}

	public int getShading()
	{
		return shading;
	}

	public int getProperty(int property)
	{
		switch (property)
		{
			case PROP_NUMBER:
				return number;
			case PROP_COLOR:
				return color;
			case PROP_SHADING:
				return shading;
			case PROP_SHAPE:
				return shape;
			default:
				return 0;
		}
	}

	public void setProperty(int property, int value)
	{
		// Set the property
		switch (property)
		{
			case PROP_NUMBER:
				number = value;
				break;
			case PROP_COLOR:
				color = value;
				break;
			case PROP_SHADING:
				shading = value;
				break;
			case PROP_SHAPE:
				shape = value;
				break;
		}

		// Check to see if the card can be redrawn
		for (int p2 = 0; p2 <= 4; p2++)
		{
			// Abort if we come across an unfulfilled value.
			if (getProperty(p2) == 0) return;
		}
	}

	/**
	 * This method returns a plaintext description of the card.
	 */
	public String toString()
	{
		String s = new String();

		// Indicate the number of items.
		s += number + " ";

		// Indicate the color
		switch (color)
		{
			case COLOR_RED:
				s += "red ";
				break;
			case COLOR_BLUE:
				s += "blue ";
				break;
			case COLOR_GREEN:
				s += "green ";
				break;
		}

		// Indicate the shading
		switch (shading)
		{
			case SHADING_NONE:
				s += "unshaded ";
				break;
			case SHADING_FULL:
				s += "shaded ";
				break;
			case SHADING_STRIPED:
				s += "striped ";
				break;
		}

		// Handle the shapes.
		switch (shape)
		{
			case SHAPE_OVAL:
				s += "oval";
				break;
			case SHAPE_SQUIGGLE:
				s += "squiggle";
				break;
			case SHAPE_DIAMOND:
				s += "diamond";
				break;
		}

		// Pluralize the shape name.
		if (number > 1) s += "s";

		return s;
	}

	public boolean equals(Object o)
	{
		Card card = (Card)o;
		boolean result = (card.color == color && card.shading == shading && card.shape == shape && card.number == number);
		//System.out.println("Comparing " + this.toString() + " with " + card.toString() + ": " + result);
		return result; 
	}

	public String getRepresentation()
	{
		return "" + number + color + shading + shape;
	}

	public static Card parseCard(String representation)
	{	
		int number = representation.charAt(0) - '0';
		int color = representation.charAt(1) - '0';
		int shading = representation.charAt(2) - '0';
		int shape = representation.charAt(3) - '0';
		return new Card(number, color, shading, shape);
	}
}
