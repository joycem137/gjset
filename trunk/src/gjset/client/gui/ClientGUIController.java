package gjset.client.gui;

import gjset.data.Card;



/**
 *
 */
public interface ClientGUIController
{

	/**
	 * Returns the client's GUI model
	 *
	 * @return
	 */
	ClientGUIModel getClientGUIModel();

	/**
	 * Initiates a request to call set from the client machine.
	 *
	 */
	void callSet();

	/**
	 * Initiates a request to draw more cards on the client machine.
	 *
	 */
	void drawMoreCards();

	/**
	 * Initiates a request from the client to select the indicated card. This may cause a "set" to be called.
	 *
	 * @param cardData
	 */
	void selectCard(Card cardData);

	/**
	 * TODO: Describe method
	 *
	 */
	void simulate();

}
