package gjset.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Card
{
	//Store constants for handling the various properties of the cards.
	public static final int COLOR_RED = 1;
	public static final int COLOR_BLUE = 2;
	public static final int COLOR_GREEN = 3;

	public static final int SHAPE_OVAL = 1;
	public static final int SHAPE_SQUIGGLE = 2;
	public static final int SHAPE_DIAMOND = 3;

	public static final int SHADING_NONE = 1;
	public static final int SHADING_FULL = 2;
	public static final int SHADING_STRIPED = 3;
	
	//Store constant indices for the various properties that exist.
	public static final int PROP_NUMBER = 1;
	public static final int PROP_COLOR = 2;
	public static final int PROP_SHADING = 3;
	public static final int PROP_SHAPE = 4;

	private Image image; //The card's image.
	private int x, y; //The card's current location.
	private int width; //The card's width.
	private int height; //The card's height.
	
	//The width and height of the cards are currently fixed.  This may be changed later.
	public static final int WIDTH = 100;
	public static final int HEIGHT = 139;
	
	private boolean highlighted; //Indicates whether this card is highlighted.
	
	//Store the various properties of this card.
	private int number, color, shading, shape;

	//Create a basic blank card.
	public Card()
	{
		//Clear the values for all cards.
		this.number = 0;
		this.color = 0;
		this.shading = 0;
		this.shape = 0;
		
		//When they are created, cards are not highlighted.
		highlighted = false;
	}
	
	//The basic card constructor
	public Card(int number, int color, int shading, int shape)
	{
		//Call the blank constructor.
		this();
		
		//Store the card properties.
		this.number = number;
		this.color = color;
		this.shading = shading;
		this.shape = shape;
		
		//Generate the actual card image.
		generateCardImage();
	}

	//Draws the image of the card for use later.
	public void generateCardImage()
	{
		//Set the shadow offset.
		final int CARD_SHADOW_OFFSET = 3;
		final int SHAPE_SHADOW_OFFSET = 2;
		
		//Set the width and height of the card.
		width = WIDTH;
		height = HEIGHT;
		
		//Create the image object to store the drawing of the card.
		image = new BufferedImage(width + CARD_SHADOW_OFFSET, height
				+ CARD_SHADOW_OFFSET, BufferedImage.TYPE_INT_ARGB_PRE);

		//Get the image's graphics context.
		Graphics2D g = ((BufferedImage) image).createGraphics();

		// Draw card shadow
		g.setColor(Color.black);
		g.fillRoundRect(CARD_SHADOW_OFFSET, CARD_SHADOW_OFFSET, width, height,
				15, 15);

		// Draw card background.
		g.setColor(Color.white);
		g.fillRoundRect(0, 0, width, height, 15, 15);

		// Draw card outline.
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1.0f));
		g.drawRoundRect(0, 0, width, height, 15, 15);

		//Determine where the first symbol should start, based on the number of symbols.
		int topSymbolY = getTopYPosition();

		//Draw each symbol.
		for (int i = 0; i < number; i++)
		{
			// Draw the shadow
			g.setColor(Color.black);
			drawSymbol(g, 15 + SHAPE_SHADOW_OFFSET, topSymbolY + i * 44
					+ SHAPE_SHADOW_OFFSET);

			// Draw the actual color.
			g.setColor(getDrawingColor());
			drawSymbol(g, 15, topSymbolY + i * 44);
		}

		//Flush the image drawing.
		image.flush();
	}
	
	//Modifies the position of this card.
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	//Draws the symbol at the currently specified location.
	private void drawSymbol(Graphics2D g, int x, int y)
	{
		//Symbol sizes are currently set statically.
		final int SHAPE_WIDTH = 70;
		final int SHAPE_HEIGHT = 30;
		
		Shape drawingShape = null;

		//Determine which shape we're going to draw.
		switch (shape)
		{
			case SHAPE_OVAL:
				//The oval is a simple round rectangle.
				drawingShape = new RoundRectangle2D.Double(x, y, SHAPE_WIDTH,
						SHAPE_HEIGHT, 25.0, 25.0);
				break;
			case SHAPE_SQUIGGLE:
				/*
				 * The squiggle is drawn by doing a complicated path using lines and quadratic
				 * curves.
				 */
				Path2D.Double squiggle = new Path2D.Double();
				squiggle.moveTo(x, y);
				squiggle.lineTo(x, y + SHAPE_HEIGHT - 5);
				squiggle.curveTo(x + 23, y + SHAPE_HEIGHT + 20, x + 46, y
						+ SHAPE_HEIGHT - 30, x + SHAPE_WIDTH, y + SHAPE_HEIGHT);
				squiggle.lineTo(x + SHAPE_WIDTH, y + 5);
				squiggle.curveTo(x + 46, y - 20, x + 23, y + 30, x, y);

				drawingShape = squiggle;
				break;
			case SHAPE_DIAMOND:
				//The diamond is a simple polygon.
				Polygon diamond = new Polygon();
				diamond.addPoint(x, y + SHAPE_HEIGHT / 2);
				diamond.addPoint(x + SHAPE_WIDTH / 2, y);
				diamond.addPoint(x + SHAPE_WIDTH, y + SHAPE_HEIGHT / 2);
				diamond.addPoint(x + SHAPE_WIDTH / 2, y + SHAPE_HEIGHT);

				drawingShape = diamond;
				break;
		}
		
		//Actually draw the shape.
		g.setStroke(new BasicStroke(4.0f));
		g.draw(drawingShape);
		
		//Now handle shading the shape.
		if (shading == SHADING_FULL)
		{
			//Fill the entire shape.
			g.fill(drawingShape);
		}
		else if (shading == SHADING_STRIPED)
		{
			//Set the width of the lines to be drawn.
			g.setStroke(new BasicStroke(2.0f));
			
			//Move across the object, left to right.
			for(int xPos = x + 6; xPos < x + SHAPE_WIDTH; xPos += 6)
			{
				//Identify the top and bottom of the shape.
				int topY = -1;
				int bottomY = -1;

				//Crawl along each pixel to determine if it is inside the shape or not.
				for(int yPos = y; yPos < y + SHAPE_HEIGHT; yPos++)
				{
					//Check to see if this is above our current estimate for the top.
					if(topY < 0)
					{
						if(drawingShape.contains(xPos, yPos))
						{
							topY = yPos;
						}
					}
					else if(bottomY < 0) //Do the same for the bottom.
					{
						if(!drawingShape.contains(xPos, yPos))
						{
							bottomY = yPos - 1;
						}
					}
				}
				
				//Now draw the line
				if(bottomY > 0)
				{
					//If we found the bottom, draw from the top to the bottom.
					g.drawLine(xPos, topY, xPos, bottomY);
				}
				else if(topY > 0)
				{
					//Sometimes the bottom IS the bottom of the object.  Draw to there.
					g.drawLine(xPos, topY, xPos, y + SHAPE_HEIGHT);
				}
			}
		}
	}

	//Identify the y position of the top symbol, based on the number of symbols to be drawn.
	private int getTopYPosition()
	{
		switch (number)
		{
			case 1:
				return 54;
			case 2:
				return 31;
			case 3:
				return 10;
			default:
				return 0;
		}
	}

	//Convert the color property to a Color object.
	private Color getDrawingColor()
	{
		switch (color)
		{
			case COLOR_RED:
				return Color.red;
			case COLOR_BLUE:
				return Color.blue;
			case COLOR_GREEN:
				return Color.green;
			default:
				return Color.black;
		}
	}

	//Actually paint the card.
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		// Draw the card image to the screen.
		g.drawImage(image, x, y, null);

		// Draw the highlight
		if (highlighted)
		{
			// Draw the main highlight.
			g.setColor(Color.yellow);
			g2.setStroke(new BasicStroke(6.0f));
			g2.draw(new RoundRectangle2D.Double(x, y, width, height, 15, 15));
		}
	}

	//Set the highlight on the card.
	public void setHighlight(boolean highlightValue)
	{
		highlighted = highlightValue;
	}

	//Determine if the indicated point is on the card or not.
	public boolean contains(int xCheck, int yCheck)
	{
		return (xCheck >= x && xCheck <= x + width && yCheck >= y && yCheck <= y
				+ height);
	}
	
	//Various getters.
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}

	public boolean isHighlighted()
	{
		return highlighted;
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
		switch(property)
		{
			case PROP_NUMBER: return number;
			case PROP_COLOR: return color;
			case PROP_SHADING: return shading;
			case PROP_SHAPE: return shape;
			default: return 0;
		}
	}
	
	public void setProperty(int property, int value)
	{
		//Set the property
		switch(property)
		{
			case PROP_NUMBER: number = value; break;
			case PROP_COLOR: color = value; break;
			case PROP_SHADING: shading = value; break;
			case PROP_SHAPE: shape = value; break;
		}
		
		//Check to see if the card can be redrawn
		for(int p2 = 0; p2 <= 4; p2++)
		{
			//Abort if we come across an unfulfilled value.
			if(getProperty(p2) == 0) return;
		}
		
		//Generate the card image.
		generateCardImage();
	}

	/**
	 * Reset the card to its original position and state.
	 */
	public void reset()
	{
		highlighted = false;
		x = 0;
		y = 0;
	}
	
	/**
	 * This method returns a plaintext description of the card.
	 */
	public String toString()
	{
		String s = new String();
		
		//Indicate the number of items.
		s += number + " ";
		
		//Indicate the color
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
		
		//Indicate the shading
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
		
		//Handle the shapes.
		switch(shape)
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
		
		//Pluralize the shape name.
		if(number > 1) s += "s";
		
		return s;
	}
	
	public boolean equals(Card card)
	{
		return card.color == color && card.shading == shading && card.shape == shape && card.number == number;
	}
}
