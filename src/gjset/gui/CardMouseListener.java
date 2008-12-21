package gjset.gui;

import gjset.engine.GameController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardMouseListener extends MouseAdapter
{
	private CardComponent	card;
	private GameController		gameController;

	public CardMouseListener(CardComponent card, GameController gameController)
	{
		this.card = card;
		this.gameController = gameController;
	}

	public void mouseClicked(MouseEvent me)
	{
		gameController.onCardClicked(card);
	}
}
