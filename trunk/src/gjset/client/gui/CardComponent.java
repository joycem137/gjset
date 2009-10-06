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

import gjset.client.EngineInterface;
import gjset.data.Card;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class CardComponent extends JComponent
{

	// The width and height of the cards are currently fixed. This may be changed later.
	public static final int	CARD_WIDTH			= 100;
	public static final int	CARD_HEIGHT			= 139;

	// Symbol sizes are currently set statically.
	private final int		SHAPE_WIDTH			= 70;
	private final int		SHAPE_HEIGHT		= 30;

	// Set the shadow offset.
	private final int		CARD_SHADOW_OFFSET	= 3;
	private final int		SHAPE_SHADOW_OFFSET	= 2;
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private boolean				highlighted;				// Indicates whether this card is highlighted.

	private Card				card;						// Stores the card data for this card.
	private Image				image;						// Stores the image of the card

	// Create a basic blank card.
	public CardComponent(EngineInterface engine, Card card)
	{
		this.card = card;
		generateCardImage();

		// Create the mouse listener for this object
		addMouseListener(new CardMouseListener(this, engine));

		// When they are created, cards are not highlighted.
		highlighted = false;

		// Set the height and width of the card.
		setMaximumSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		setMinimumSize(new Dimension(image.getWidth(this), image.getHeight(this)));
	}

	// Draws the image of the card for use later.
	private void generateCardImage()
	{
		// Create the image object to store the drawing of the card.
		image = new BufferedImage(CARD_WIDTH + CARD_SHADOW_OFFSET, CARD_HEIGHT + CARD_SHADOW_OFFSET,
				BufferedImage.TYPE_INT_ARGB_PRE);

		// Get the image's graphics context.
		Graphics2D g = ((BufferedImage) image).createGraphics();

		// Draw card shadow
		g.setColor(Color.black);
		g.fillRoundRect(CARD_SHADOW_OFFSET, CARD_SHADOW_OFFSET, CARD_WIDTH, CARD_HEIGHT, 15, 15);

		// Draw card background.
		g.setColor(Color.white);
		g.fillRoundRect(0, 0, CARD_WIDTH, CARD_HEIGHT, 15, 15);

		// Draw card outline.
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1.0f));
		g.drawRoundRect(0, 0, CARD_WIDTH, CARD_HEIGHT, 15, 15);

		// Determine where the first symbol should start, based on the number of symbols.
		int topSymbolY = getTopYPosition();

		// Draw each symbol.
		for (int i = 0; i < card.getNumber(); i++)
		{
			// Draw the shadow
			g.setColor(Color.black);
			drawSymbol(g, 15 + SHAPE_SHADOW_OFFSET, topSymbolY + i * 44 + SHAPE_SHADOW_OFFSET);

			// Draw the actual color.
			g.setColor(getDrawingColor());
			drawSymbol(g, 15, topSymbolY + i * 44);
		}

		// Flush the image drawing.
		image.flush();
	}

	// Draws the symbol at the currently specified location.
	private void drawSymbol(Graphics2D g, int x, int y)
	{
		Shape drawingShape = null;

		// Determine which shape we're going to draw.
		switch (card.getShape())
		{
			case Card.SHAPE_OVAL:
				// The oval is a simple round rectangle.
				drawingShape = new RoundRectangle2D.Double(x, y, SHAPE_WIDTH, SHAPE_HEIGHT, 25.0, 25.0);
				break;
			case Card.SHAPE_SQUIGGLE:
				/*
				 * The squiggle is drawn by doing a complicated path using lines and quadratic curves.
				 */
				GeneralPath squiggle = new GeneralPath();
				squiggle.moveTo(x, y);
				squiggle.lineTo(x, y + SHAPE_HEIGHT - 5);
				squiggle.curveTo(x + 23, y + SHAPE_HEIGHT + 20, x + 46, y + SHAPE_HEIGHT - 30, x + SHAPE_WIDTH, y
						+ SHAPE_HEIGHT);
				squiggle.lineTo(x + SHAPE_WIDTH, y + 5);
				squiggle.curveTo(x + 46, y - 20, x + 23, y + 30, x, y);

				drawingShape = squiggle;
				break;
			case Card.SHAPE_DIAMOND:
				// The diamond is a simple polygon.
				Polygon diamond = new Polygon();
				diamond.addPoint(x, y + SHAPE_HEIGHT / 2);
				diamond.addPoint(x + SHAPE_WIDTH / 2, y);
				diamond.addPoint(x + SHAPE_WIDTH, y + SHAPE_HEIGHT / 2);
				diamond.addPoint(x + SHAPE_WIDTH / 2, y + SHAPE_HEIGHT);

				drawingShape = diamond;
				break;
		}

		// Actually draw the shape.
		g.setStroke(new BasicStroke(4.0f));
		g.draw(drawingShape);

		// Now handle shading the shape.
		if (card.getShading() == Card.SHADING_FULL)
		{
			// Fill the entire shape.
			g.fill(drawingShape);
		}
		else if (card.getShading() == Card.SHADING_STRIPED)
		{
			// Set the width of the lines to be drawn.
			g.setStroke(new BasicStroke(2.0f));

			// Move across the object, left to right.
			for (int xPos = x + 6; xPos < x + SHAPE_WIDTH; xPos += 6)
			{
				// Identify the top and bottom of the shape.
				int topY = -1;
				int bottomY = -1;

				// Crawl along each pixel to determine if it is inside the shape or not.
				for (int yPos = y; yPos < y + SHAPE_HEIGHT; yPos++)
				{
					// Check to see if this is above our current estimate for the top.
					if (topY < 0)
					{
						if (drawingShape.contains(xPos, yPos))
						{
							topY = yPos;
						}
					}
					else if (bottomY < 0) // Do the same for the bottom.
					{
						if (!drawingShape.contains(xPos, yPos))
						{
							bottomY = yPos - 1;
						}
					}
					else if(drawingShape.contains(xPos, yPos))
					{
						topY = yPos;
						bottomY = -1;
					}
				}

				// Now draw the line
				if (bottomY > 0)
				{
					// If we found the bottom, draw from the top to the bottom.
					g.drawLine(xPos, topY, xPos, bottomY);
				}
				else if (topY > 0)
				{
					// Sometimes the bottom IS the bottom of the object. Draw to there.
					g.drawLine(xPos, topY, xPos, y + SHAPE_HEIGHT);
				}
			}
		}
	}

	// Identify the y position of the top symbol, based on the number of symbols to be drawn.
	private int getTopYPosition()
	{
		switch (card.getNumber())
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

	// Convert the color property to a Color object.
	private Color getDrawingColor()
	{
		switch (card.getColor())
		{
			case Card.COLOR_RED:
				return Color.red;
			case Card.COLOR_BLUE:
				return Color.blue;
			case Card.COLOR_GREEN:
				return Color.green;
			default:
				return Color.black;
		}
	}

	// Actually paint the card.
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		// Draw the card image to the screen.
		g.drawImage(image, 0, 0, this);

		// Draw the highlight
		if (highlighted)
		{
			// Draw the main highlight.
			g.setColor(Color.yellow);
			g2.setStroke(new BasicStroke(6.3f));
			g2.draw(new RoundRectangle2D.Double(0, 0, CARD_WIDTH, CARD_HEIGHT, 15, 15));
		}
	}

	// Set the highlight on the card.
	public void setHighlight(boolean highlightValue)
	{
		highlighted = highlightValue;
		repaint();
	}

	public boolean isHighlighted()
	{
		return highlighted;
	}

	/**
	 * Reset the card to its original position and state.
	 */
	public void reset()
	{
		highlighted = false;
	}

	/**
	 * This method returns a plaintext description of the card.
	 */
	public String toString()
	{
		return card.toString();
	}

	public boolean equals(CardComponent cardComponent)
	{
		return card.equals(cardComponent.card);
	}

	public Card getCard()
	{
		return card;
	}
}