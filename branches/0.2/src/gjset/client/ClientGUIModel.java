package gjset.client;

import gjset.GameConstants;
import gjset.data.CardTableData;

import java.util.Observable;

import org.dom4j.Element;

/**
 * Stores all of the information for the GUI's model.  Note that this class does not just store the model for the
 * game state, but specifically the model for the GUI.  All information needed to render to the screen is stored here.
 */
public class ClientGUIModel extends Observable
{	
	private int setCallerId;
	private int playerId;
	private int gameState;
	private int deckSize;
	
	private CardTableData cardTableData;
	
	public ClientGUIModel()
	{
		cardTableData = null;
		
		deckSize = 0;
		
		setCallerId = -1;
		playerId = -1;
		
		gameState = GameConstants.GAME_STATE_NOT_STARTED;
	}
	
	/**
	 * Returns the number of cards in the deck.
	 *
	 * @return
	 */
	public int getCardsInDeck()
	{
		return deckSize;
	}
	
	/**
	 * Returns the indicated game state.
	 *
	 * @return
	 */
	public int getGameState()
	{
		return gameState;
	}

	/**
	 * 
	 * Returns true if the player is allowed to select cards on this client.
	 *
	 * @return
	 */
	public boolean canSelectCards()
	{
		return (gameState == GameConstants.GAME_STATE_IDLE || 
				( gameState == GameConstants.GAME_STATE_SET_CALLED && setCallerId == playerId )); 
	}

	/**
	 * Returns true if this client is allowed to call set.
	 *
	 * @return
	 */
	public boolean canCallSet()
	{
		return gameState == GameConstants.GAME_STATE_IDLE;
	}

	/**
	 * Returns the card table associated with this model.
	 *
	 * @return
	 */
	public CardTableData getCardTable()
	{
		return cardTableData;
	}

	/**
	 * Returns true if the GUI is able to request a drawing of cards.
	 *
	 * @return
	 */
	public boolean canDrawCards()
	{
		return gameState == GameConstants.GAME_STATE_IDLE
			&& deckSize > 0 && cardTableData.getCols() < 7;
	}
	
	/**
	 * 
	 * Receive server data and convert it back into reasonable data.
	 *
	 * @param data - An XML Node containing the model information.
	 */
	public void update(Element modelElement)
	{
		// Get the deck size.
		String deckText = modelElement.element("deck").getText();
		deckSize = Integer.parseInt(deckText);
		
		// Get the game state
		String gameStateText = modelElement.element("gamestate").getText();
		gameState = Integer.parseInt(gameStateText);
		
		// Get the id of the player that called set, if appropriate.
		if(gameState == GameConstants.GAME_STATE_SET_CALLED)
		{
			String setCallerIdString = modelElement.element("setcaller").getText();
			setCallerId = Integer.parseInt(setCallerIdString);
		}
		else
		{
			setCallerId = -1;
		}
		
		// Get the card table.
		Element cardTableElement = modelElement.element("cardtable");
		cardTableData = new CardTableData(cardTableElement);
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * 
	 * Sets the player id to the indicated value.
	 *
	 * @param playerId
	 */
	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	/**
	 * Return the player Id.
	 *
	 * @return
	 */
	public int getPlayerId()
	{
		return playerId;
	}

}
