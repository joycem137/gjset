package gjset.gui;

import gjset.engine.Card;
import gjset.engine.GameController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CardTable extends JPanel
{
	private static final long	serialVersionUID	= 1L;

	private final Color			backgroundColor		= new Color(0, 102, 0);
	private List<CardComponent>	cardsOnTable;
	private GridLayout layout;
	
	private GameController		gameController;

	// Set our parameters
	private final int CARD_BUFFER = 20; //The minimum distance between cards.

	private JPanel	cardPane;

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

		// Create the vectors for holding cards.
		cardsOnTable = new Vector<CardComponent>();

		//Create the card pane
		cardPane = new JPanel();
		cardPane.setBackground(backgroundColor);
		
		//Set up the layout
		layout = new GridLayout(3, 4, CARD_BUFFER, CARD_BUFFER);
		cardPane.setLayout(layout);
		
		//Add the card pane to the panel.
		setLayout(null);
		add(cardPane);
	}
	
	/**
	 * Repositions the cards.
	 */
	protected void updateLayout()
	{
		//Determine the number of rows and columns in the layout.
		int numCols = 0;
		int numRows = 0;
		int numCards = cardsOnTable.size();
		
		if(numCards > 6)
		{
			numRows = 3;
			numCols = numCards / numRows;
		}
		else
		{
			numCols = 3;
			numRows = numCards / numCols;
		}
		
		//Set the layout with the rows and columns.
		layout.setRows(numRows);
		layout.setColumns(numCols);

		//Now set up the position of the card pane, to center it on the screen.
		// The x coordinate is back a few cards
		double centerX = getWidth() / 2.0;
		int rowLength = (int) (numCols * Card.CARD_WIDTH + (numCols - 0.50) * CARD_BUFFER);		
		int xStart = (int) (centerX - rowLength / 2.0);

		// The y coordinate is up a few cards.
		double centerY = getHeight() / 2.0;
		int colHeight = (int) (numRows * Card.CARD_HEIGHT + (numRows - 0.50) * CARD_BUFFER);
		int yStart = (int) (centerY - colHeight / 2.0);
		
		//Relocate the card pane.
		cardPane.setBounds(xStart, yStart, rowLength, colHeight);
		
		//Repaint the component.
		repaint();
	}
	
	public void addCards( Vector<Card> cards )
	{
		Vector<CardComponent> cardComponents = createCardComponents(cards);
		
		//Add the cards to the cards array.
		cardsOnTable.addAll(cardComponents);
		
		//Add the cards to the card pane.
		Iterator<CardComponent> iterator = cardComponents.iterator();
		while(iterator.hasNext())
		{
			cardPane.add(iterator.next());
		}
		
		//Finally, update the layout with the latest data.
		updateLayout();
	}

	public void replaceCards( Vector<CardComponent> selectedCards, Vector<Card> newCards )
	{
		Vector<CardComponent> newCardComponents = createCardComponents(newCards);
		
		for (int i = 0; i < selectedCards.size(); i++)
		{
			//Replace the card on the table.
			cardsOnTable.set(cardsOnTable.indexOf(selectedCards.get(i)), newCardComponents.get(i));
			
			//Remove the card from the cardPane.
			cardPane.remove(selectedCards.get(i));
			
			//Add the new card to the cardPane.
			cardPane.add(newCardComponents.get(i));
		}
		
		repaint();
	}
	
	public void removeCards()
	{
		cardsOnTable.clear();
		cardPane.removeAll();
		repaint();
	}

	public void removeCards( Vector<CardComponent> selectedCards )
	{
		//Remove all the cards that have been selected
		cardsOnTable.removeAll(selectedCards);
		
		//Now remove everything from the card pane.
		Iterator<CardComponent> iterator = selectedCards.iterator();
		while(iterator.hasNext())
		{
			cardPane.remove(iterator.next());
		}

		//Redraw the table.
		updateLayout();
	}

	/**
	 * 
	 * @return numCards - The number of cards currently on the table.
	 */
	public int getNumCards()
	{
		return cardsOnTable.size();
	}

	public List<CardComponent> getCards()
	{
		return cardsOnTable;
	}

	private Vector<CardComponent> createCardComponents( Vector<Card> cards)
	{
		//Create our card component vector
		Vector<CardComponent> cardComponents = new Vector<CardComponent>();
		
		Iterator<Card> iterator = cards.iterator();
		while(iterator.hasNext())
		{
			cardComponents.add(new CardComponent(gameController, iterator.next()));
		}
		
		return cardComponents;
	}

}