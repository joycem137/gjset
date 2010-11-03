package gjset.client.gui;

import gjset.data.CardTable;
import gjset.engine.Deck;

import java.util.Observable;

/**
 * Stores all of the information for the model.
 */
public class ClientGUIModel extends Observable
{	
	private Deck deck;
	private CardTable cardTable;
	
	public ClientGUIModel()
	{
		deck = new Deck();
		cardTable = new CardTable();
		deck.shuffle();
		cardTable.removeCards();
		cardTable.addCards(deck.drawCards(12));
	}
	
	public void drawCards()
	{
		if(deck.remainingCards() > 0)
		{
			cardTable.addCards(deck.drawCards(3));
			setChanged();
			notifyObservers();
		}
	}
	
	/**
	 * Returns the number of cards in the deck.
	 *
	 * @return
	 */
	public int getCardsInDeck()
	{
		return deck.remainingCards();
	}

	/**
	 * Returns true if this client is allowed to call set.
	 *
	 * @return
	 */
	public boolean canCallSet()
	{
		return true;
	}

	/**
	 * Returns the card table associated with this model.
	 *
	 * @return
	 */
	public CardTable getCardTable()
	{
		return cardTable;
	}

	/**
	 * Returns true if the GUI is able to request a drawing of cards.
	 *
	 * @return
	 */
	public boolean canDrawCards()
	{
		return deck.remainingCards() > 0 && cardTable.getCols() < 7;
	}

}
