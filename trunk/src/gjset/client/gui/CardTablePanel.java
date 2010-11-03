package gjset.client.gui;

import gjset.gui.MainFrame;

import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 *
 */
@SuppressWarnings("serial")
public class CardTablePanel extends JPanel
{
	private Rectangle	playingFrame;

	/**
	 * Constructs your basic card table.
	 *
	 * @param playingFieldArea
	 */
	public CardTablePanel()
	{
		super();
		
		configurePanel();
		
		addTestCard();
	}

	/**
	 * Add a test card to test card designs.
	 *
	 */
	private void addTestCard()
	{
		CardPanel card = new CardPanel();
		add(card);
		
		// For now, center the card.
		card.setLocation(playingFrame.width / 2 - card.getWidth() / 2, playingFrame.height / 2 - card.getHeight() / 2);
	}

	/**
	 * Configure the raw settings of the panel.
	 *
	 */
	private void configurePanel()
	{
		setLayout(null);
		setOpaque(false);
		
		playingFrame = MainFrame.PLAYING_FIELD_AREA;
		
		setLocation(playingFrame.x, playingFrame.y);
		setSize(playingFrame.width, playingFrame.height);
	}

}
