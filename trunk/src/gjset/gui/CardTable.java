package gjset.gui;

import gjset.engine.GameController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class CardTable extends JComponent
{
	private static final long	serialVersionUID	= 1L;

	private final Color			backgroundColor		= new Color(0, 102, 0);
	private List<Card>			cards;
	protected Image				offScreenImage;
	protected Graphics			offScreenGraphics;

	private GameController		gameController;

	public CardTable(GameController gameController)
	{
		super();

		//Obtain the game controller
		this.gameController = gameController;
		
		//Set the size of the card table.
		setPreferredSize(new Dimension(720, 497));
		setMinimumSize(new Dimension(720, 497));
		setMaximumSize(new Dimension(720, 497));

		//Give the card table a border
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Give the card table a background color.
		setBackground(backgroundColor);

		// Create the vector for holding cards.
		cards = new Vector<Card>();

		//Handle resizing the table.
		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				JComponent component = (JComponent) e.getSource();

				int width = component.getWidth();
				int height = component.getHeight();

				offScreenImage = component.createImage(width, height);
				offScreenGraphics = offScreenImage.getGraphics();
				updateLayout();
				drawTable();
			}
		});

		//Handle Card selection
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				// Determine what we clicked on
				Card newCard = identifyCard(e.getX(), e.getY());
				if (newCard != null)
				{
					selectCard(newCard);
				}
			}
		});
	}
	
	protected void selectCard(Card card)
	{
		gameController.selectCard(card);
	}

	private Card identifyCard(int x, int y)
	{
		Iterator<Card> iterator = cards.iterator();
		while (iterator.hasNext())
		{
			Card card = iterator.next();
			if (card.contains(x, y))
			{
				return card;
			}
		}
		return null;
	}

	/**
	 * Repositions the cards.
	 */
	protected void updateLayout()
	{
		int rowLength = 0;
		// Set our layout parameters.
		switch (cards.size())
		{
			case 3:
				rowLength = 3;
				break;
			case 6:
				rowLength = 3;
				break;
			case 9:
				rowLength = 3;
				break;
			case 12:
				rowLength = 4;
				break;
			case 15:
				rowLength = 5;
				break;
			case 18:
				rowLength = 6;
				break;
		}

		// Set our parameters
		final int CARD_BUFFER = 20;

		// The x coordinate is back a few cards
		double halfRowCards = (double) rowLength / 2.0;
		double xStart = getWidth() / 2.0 - halfRowCards * Card.WIDTH
				- (halfRowCards - 0.50) * CARD_BUFFER;

		// The y coordinate is up a few cards.
		double halfColCards = ((double) cards.size() / (double) rowLength) / 2.0;
		double yStart = getHeight() / 2.0 - halfColCards * Card.HEIGHT
				- (halfColCards - 0.50) * CARD_BUFFER;

		int x = (int) xStart;
		int y = (int) yStart;
		int rowIndex = 0;
		int colIndex = 0;

		// Now set the positions
		Iterator<Card> iterator = cards.iterator();
		while (iterator.hasNext())
		{
			Card card = iterator.next();
			card.setPosition(x, y);

			rowIndex++;
			if (rowIndex == rowLength)
			{
				// Reset the row
				rowIndex = 0;
				colIndex++;

				// Now update the x and y values.
				x = (int) xStart;
				y = (int) yStart + colIndex * (CARD_BUFFER + Card.HEIGHT);
			}
			else
			{
				// Just keep on trucking.
				x += CARD_BUFFER + Card.WIDTH;
			}
		}
	}

	public void drawTable()
	{
		// Draw the background.
		offScreenGraphics.setColor(backgroundColor);
		offScreenGraphics.fillRect(0, 0, getWidth(), getHeight());

		// Paint the card
		Iterator<Card> iterator = cards.iterator();
		while (iterator.hasNext())
		{
			Card card = iterator.next();
			card.paint(offScreenGraphics);
		}
		offScreenImage.flush();

		//TODO Only repaint this portion of the screen.
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(offScreenImage, 0, 0, getWidth(), getHeight(), this);
	}

	public void addCards(Vector<Card> newCards)
	{
		cards.addAll(newCards);
		updateLayout();
		drawTable();
	}

	public void replaceCards(Vector<Card> selectedCards, Vector<Card> drawCards)
	{
		for(int i = 0; i < selectedCards.size(); i++)
		{
			cards.set(cards.indexOf(selectedCards.get(i)), drawCards.get(i));
		}
		updateLayout();
	}
	
	public void removeCards()
	{
		cards.clear();
		drawTable();
	}

	public void removeCards(Vector<Card> selectedCards)
	{
		//Remove all the cards that have been selected
		cards.removeAll(selectedCards);
		
		//Redraw the table.
		updateLayout();
		drawTable();
	}

	/**
	 * 
	 * @return numCards - The number of cards currently on the table.
	 */
	public int getNumCards()
	{
		return cards.size();
	}
	
	public List<Card> getCards()
	{
		return cards;
	}

	/**
	 * Search through all of the cards for the indicated test card.
	 * 
	 * @param testCard
	 * @return true if card is present.  False otherwise.
	 */
	public boolean contains(Card testCard) {
		Iterator<Card> iterator = cards.iterator();
		{
			while(iterator.hasNext())
			{
				//If we find the card, abort and send it out.
				if(iterator.next().equals(testCard)) return true;
			}
		}
		return false;
	}
}
