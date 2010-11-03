package gjset.client.gui;

import gjset.data.Card;
import gjset.data.CardTable;
import gjset.engine.GameEngine;
import gjset.gui.MainFrame;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 *
 */
@SuppressWarnings("serial")
public class CardTablePanel extends JPanel
{
	private static final int CARD_BUFFER = 20; // The minimum distance between cards.
	
	private Rectangle playingFrame;
	
	private JPanel cardPane;
	private GridLayout layout;

	private ClientGUIController controller;

	/**
	 * Constructs your basic card table.
	 *
	 * @param playingFieldArea
	 */
	public CardTablePanel(ClientGUIController controller)
	{
		super();
		
		this.controller = controller;
		
		configurePanel();
		addCardPane();
	}

	/**
	 * Configure the raw settings of the panel.
	 *
	 */
	private void configurePanel()
	{
		setLayout(null);
		setOpaque(false);
		
		playingFrame = MainFrame.PLAYING_FIELD_AREA;
		
		setLocation(playingFrame.x, playingFrame.y);
		setSize(playingFrame.width, playingFrame.height);
	}

	/**
	 * Add a card pane to store the cards.
	 *
	 */
	private void addCardPane()
	{
		// Create the card pane
		cardPane = new JPanel();
		//cardPane.setLayout(null);
		cardPane.setOpaque(false);
		add(cardPane);
		
		// Set up the layout
		layout = new GridLayout(3, 4, CARD_BUFFER, CARD_BUFFER);
		cardPane.setLayout(layout);
	}

	/**
	 * 
	 * This method should be called any time the {@link CardTable} data changes within the {@link GameEngine}.
	 * This method will be called any time the highlighting, physical arrangement, or number/composition of
	 * cards on the table changes.
	 *
	 * @param cardTable A CardTable object containing the latest updates on the cards that are on the table.
	 */
	public void update(CardTable cardTable)
	{
		//Get the number of rows and cols from the card table data.
		int gridRows = cardTable.getRows();
		int gridCols = cardTable.getCols();
		
		//If this is a "null" table, then reset the table.
		if(gridRows == 0 && gridCols == 0)
		{
			reset();
			return;
		}
		
		int cardBuffer = CARD_BUFFER;
		
		// Remove all of the cards from the card pane.
		cardPane.removeAll();

		// Now put all of the cards back.
		CardPanel cardComponent = null;
		for (int row = 0; row < gridRows; row++)
		{
			for (int col = 0; col < gridCols; col++)
			{
				Card card = cardTable.getCardAt(row, col);
				cardComponent = new CardPanel(controller, card);
				cardPane.add(cardComponent);
				cardComponent.setHighlighted(cardTable.isHighlighted(card));
				
				cardComponent.setLocation(col * (cardComponent.getWidth() + CARD_BUFFER), row * (cardComponent.getHeight() + CARD_BUFFER));
			}
		}

		// Now set up the position of the card pane, to center it on the screen.
		// The x coordinate is back a few cards
		double centerX = getWidth() / 2.0;
		int rowLength = (int) (gridCols * cardComponent.getWidth() + (gridCols - 0.50) * CARD_BUFFER);
		int xStart = (int) (centerX - rowLength / 2.0);

		// The y coordinate is up a few cards.
		double centerY = getHeight() / 2.0;
		int colHeight = (int) (gridRows * cardComponent.getHeight() + (gridRows - 0.50) * CARD_BUFFER);
		int yStart = (int) (centerY - colHeight / 2.0);

		// Relocate the card pane.
		cardPane.setBounds(xStart, yStart, rowLength, colHeight);
		
		cardPane.revalidate();
		cardPane.repaint();
	}

	/**
	 * 
	 * Reset the card table to its pristine condition.  Used when finishing a game.
	 *
	 */
	public void reset()
	{
		//Clear all cards from the screen.
		cardPane.removeAll();
		cardPane.revalidate();
		cardPane.repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		System.out.println("Repainting CardTablePanel.");
		System.out.println("Main panel stats are: " + getBounds());
		System.out.println("Card pane stats are: " + cardPane.getBounds());
		Component cards[] = cardPane.getComponents();
		if(cards.length > 0)
		{
			Component card = cards[0];
			System.out.println("A card looks like " + card.getBounds());
		}
	}

}
