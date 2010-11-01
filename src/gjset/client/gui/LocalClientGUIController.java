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

}
