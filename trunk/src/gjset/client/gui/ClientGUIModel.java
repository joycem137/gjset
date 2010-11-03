package gjset.client.gui;

import java.util.Observable;

/**
 * Stores all of the information for the model.
 */
public class ClientGUIModel extends Observable
{	
	/**
	 * Returns the number of cards in the deck.
	 *
	 * @return
	 */
	public int getCardsInDeck()
	{
		return 84;
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

}
