package gjset.client.gui;

import gjset.data.Card;

/**
 *
 */
public class ConcreteClientGUIController implements ClientGUIController
{

	private ClientGUIModel	model;

	/**
	 * Construct a controller with the indicated model.
	 *
	 * @param guiModel
	 */
	public ConcreteClientGUIController(ClientGUIModel model)
	{
		this.model = model;
	}

	/**
	 * Returns the model that this controller is using.
	 *
	 * @return
	 * @see gjset.client.gui.ClientGUIController#getClientGUIModel()
	 */
	public ClientGUIModel getClientGUIModel()
	{
		return model;
	}

	/**
	 * Initiates a request to call set from this client.
	 *
	 * @see gjset.client.gui.ClientGUIController#callSet()
	 */
	public void callSet()
	{
		System.out.println("Calls set");
	}

	/**
	 * Initiates a request to draw more cards from this client.
	 *
	 * @see gjset.client.gui.ClientGUIController#drawMoreCards()
	 */
	public void drawMoreCards()
	{
		System.out.println("Draw more cards!");
	}

	/**
	 * Initiates a request to select a given card. Note that this may cause the game to declare a set.
	 *
	 * @param cardData
	 * @see gjset.client.gui.ClientGUIController#selectCard(gjset.data.Card)
	 */
	public void selectCard(Card cardData)
	{
		System.out.println("Yay! Card selected!");
	}

}
