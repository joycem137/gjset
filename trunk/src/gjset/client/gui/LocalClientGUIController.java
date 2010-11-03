package gjset.client.gui;

/**
 *
 */
public class LocalClientGUIController implements ClientGUIController
{

	private ClientGUIModel	model;

	/**
	 * Construct a controller with the indicated model.
	 *
	 * @param guiModel
	 */
	public LocalClientGUIController(ClientGUIModel model)
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

}
