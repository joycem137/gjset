package gjset.data;

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
 * A class containing all of the information necessary to represent a single card in the game.
 * 
 * In the game of Set, every card has four different properties: color, shape, number, and shading.
 * This class encapsulates those characteristics and provides static constants for referencing them.
 * <P>
 * This class is purely a <I>data</I> based class and should not contain any UI elements.
 * <P>
 * This class can be used in either of two ways:<br>
 * 1. Create a card based on specific parameters for each of the four different properties for a card.<BR>
 * 2. Create a blank card whose properties are yet to be defined.
 * 
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
	public static final int	SHADING_STRIPED		= 2;
	public static final int	SHADING_FULL		= 3;

	// Store constant indices for the various properties that exist.
	public static final int	PROP_NUMBER			= 1;
	public static final int	PROP_COLOR			= 2;
	public static final int	PROP_SHADING		= 3;
	public static final int	PROP_SHAPE			= 4;

	// Store the various properties of this card.
	private int				number;
	private int 			color;
	private int				shading;
	private int				shape;

	/**
	 * Constructs a card with no properties set so that its properties can be added later.  <BR>
	 * Note that until those properties are set, this card is invalid.
	 *
	 */
	public Card()
	{
		// Clear the values for all cards.
		this.number = 0;
		this.color = 0;
		this.shading = 0;
		this.shape = 0;
	}

	/**
	 * 
	 * Constructs a card with the indicated values.
	 *
	 * @param number The number of shapes to be on the card.
	 * @param color The color of the shapes on the card.
	 * @param shading The shading of the shapes on the card.
	 * @param shape The shape of the shapes on the card.
	 */
	public Card(int number, int color, int shading, int shape)
	{
		// Store the card properties.
		this.number = number;
		this.color = color;
		this.shading = shading;
		this.shape = shape;
	}

	/**
	 * This creates a card using the information stored in an XML element.
	 *
	 * @param cardElement
	 */
	public Card(Element cardElement)
	{
		parseCard(cardElement);
	}

	/**
	 * 
	 * Returns the number property of this card, indicating how many shapes are on the card.<BR>
	 * Valid responses are 1, 2, and 3.
	 *
	 * @author Joyce Murton
	 * @return The number property of the card.
	 */
	public int getNumber()
	{
		return number;
	}

	/**
	 * 
	 * Returns the color property of the card, indicating what color the shapes are on the card.<BR>
	 * Valid responses are 1, 2, and 3.
	 * 
	 * The mapping is as follows:
	 * 1 - Red
	 * 2 - Blue
	 * 3 - Green
	 *
	 * @return The color property of the card.
	 */
	public int getColor()
	{
		return color;
	}

	/**
	 * 
	 * Returns the shape property of the card, indicating the shape of the shapes on the card.<BR>
	 * Valid responses are 1, 2, and 3.
	 * 
	 * The mapping is as follows:
	 * 1 - Oval
	 * 2 - Squiggle
	 * 3 - Diamond
	 *
	 * @return The shape property of the card.
	 */
	public int getShape()
	{
		return shape;
	}

	/**
	 * 
	 * Returns the shading property of the card, indicating the shading of the shapes on the card.<BR>
	 * Valid responses are 1, 2, and 3.
	 * 
	 * The mapping is as follows
	 * 1 - None (No filling.)
	 * 2 - Full (Also can be described as filled)
	 * 3 - Striped (Shape has vertical stripes in its interior.)
	 *
	 * @return The shading property of the card.
	 */
	public int getShading()
	{
		return shading;
	}

	/**
	 * 
	 * Using an index to represent one of the properties of the card, this method will return the indicated property.<BR>
	 * Valid parameters for the <code>property</code> parameter are 1, 2, 3, 4.
	 * <P>
	 * The mapping is as follows:<BR>
	 * 1 - Number<BR>
	 * 2 - Color<BR>
	 * 3 - Shading<BR>
	 * 4 - Shape
	 *
	 * @author Joyce Murton
	 * @param property Indicates the property to return.
	 * @return The value of the indicated property.
	 */
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

	/**
	 * 
	 * Sets the indicated property to the provided value.<BR>
	 * Valid parameters for the <code>property</code> parameter are 1, 2, 3, 4.
	 * 
	 * The mapping is as follows:
	 * 1 - Number
	 * 2 - Color
	 * 3 - Shading
	 * 4 - Shape
	 *
	 * @author Joyce Murton
	 * @param property Indicates the property whose value is to be set.
	 * @param value The new value for the indicated property.
	 */
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
	}

	/**
	 * Returns a human readable description of the card.
	 * 
	 * @author Joyce Murton
	 * @return A human readable description of the card.
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

	/**
	 * 
	 * This method is used to test whether one card equals another.
	 *
	 * @author Joyce Murton
	 * @param o The object to compare this Card object with.
	 * @return <code>true</code> if <code>o</code> is a {@link Card} type object, and 
	 * 			the properties of <code>o</code> are the same as on this object.  
	 * <P>
	 * 			<code>false</code> if <code>o</code> is not a {@link Card} type object, or,
	 * 			if it is, if the properties of that Card are the same as the properties
	 *          for this one.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		Card card = (Card)o;
		boolean result = (card.color == color && card.shading == shading && card.shape == shape && card.number == number);
		//System.out.println("Comparing " + this.toString() + " with " + card.toString() + ": " + result);
		return result; 
	}

	/**
	 * 
	 * Returns a representation of the card as a {@link String}.  This representation is a unique parsable string that 
	 * can be transformed back into a {@link Card} object using the static {@link #parseCard} method.
	 *
	 * @return The representation of this Card
	 */
	public Element getRepresentation()
	{
		DefaultElement cardElement =  new DefaultElement("card");

		cardElement.add(createElement("number", number));
		cardElement.add(createElement("color", color));
		cardElement.add(createElement("shape", shape));
		cardElement.add(createElement("shading", shading));
		
		return cardElement;
	}
	
	/**
	 * 
	 * Creates an element with the indicated tag and value.
	 *
	 * @param tag
	 * @param value
	 * @return
	 */
	private Element createElement(String tag, int value)
	{
		DefaultElement element = new DefaultElement(tag);
		element.setText("" + value);
		
		return element;
	}

	/**
	 * 
	 * Constructs a new {@link Card} object using the format returned by <code>getRepresentation</code>.
	 *
	 * @param representation The {@link String} representation of the Card.
	 * @return The new Card object represented by the <code>representation</code> String.
	 */
	public void parseCard(Element cardElement)
	{	
		String numberText = cardElement.element("number").getText();
		number = Integer.parseInt(numberText);
		
		String colorText = cardElement.element("color").getText();
		color = Integer.parseInt(colorText);
		
		String shadingText = cardElement.element("shading").getText();
		shading = Integer.parseInt(shadingText);
		
		String shapeText = cardElement.element("shape").getText();
		shape = Integer.parseInt(shapeText);
	}
}
