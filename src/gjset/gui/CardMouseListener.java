package gjset.gui;

import gjset.engine.GameController;

import java.awt.event.MouseAdapter;

public class CardMouseListener extends MouseAdapter
{
	private CardComponent	card;
	private GameController		gameController;

	public CardMouseListener(CardComponent card, GameController gameController)
	{
		this.card = card;
		this.gameController = gameController;
	}

	public void mouseClicked()
	{
		gameController.onCardClicked(card);
	}
}
