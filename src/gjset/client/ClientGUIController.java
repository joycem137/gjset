package gjset.client;

import gjset.data.Card;

/**
 * The interface for gui controllers on the client.  Allows different kinds of GUI controllers.
 */
public interface ClientGUIController
{

	/**
	 * Returns the client's GUI model
	 *
	 * @return
	 */
	ClientGUIModel getModel();

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
	 * Requests a new game from the server.
	 *
	 */
	void startNewGame();

}
