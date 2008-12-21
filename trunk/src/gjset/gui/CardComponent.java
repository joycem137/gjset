package gjset.gui;

import gjset.engine.Card;
import gjset.engine.GameController;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

public class CardComponent extends JComponent
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private boolean				highlighted;				//Indicates whether this card is highlighted.

	private Card				card;						//Stores the card data for this card.

	//Create a basic blank card.
	public CardComponent(GameController gameController, Card card)
	{
		this.card = card;

		//Create the mouse listener for this object
		addMouseListener(new CardMouseListener(this, gameController));

		//When they are created, cards are not highlighted.
		highlighted = false;

		//Set the height and width of the card.
		setMaximumSize(new Dimension(card.getImage().getWidth(this), card.getImage().getHeight(this)));
		setMinimumSize(new Dimension(card.getImage().getWidth(this), card.getImage().getHeight(this)));
	}

	//Actually paint the card.
	protected void paintComponent( Graphics g )
	{
		Graphics2D g2 = (Graphics2D) g;

		// Draw the card image to the screen.
		g.drawImage(card.getImage(), 0, 0, this);

		// Draw the highlight
		if (highlighted)
		{
			// Draw the main highlight.
			g.setColor(Color.yellow);
			g2.setStroke(new BasicStroke(6.3f));
			g2.draw(new RoundRectangle2D.Double(0, 0, Card.CARD_WIDTH, Card.CARD_HEIGHT, 15, 15));
		}
	}

	//Set the highlight on the card.
	public void setHighlight( boolean highlightValue )
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

	public boolean equals( CardComponent cardComponent )
	{
		return card.equals(cardComponent.card);
	}

	public Card getCard()
	{
		return card;
	}
}