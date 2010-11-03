package gjset.client.gui;



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

}
