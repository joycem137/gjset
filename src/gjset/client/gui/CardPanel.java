package gjset.client.gui;

import gjset.data.Card;
import gjset.gui.SymbolImageFactory;
import gjset.gui.framework.ResourceManager;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

/**
 * This class stores all of the code necessary for drawing cards.
 */
@SuppressWarnings("serial")
public class CardPanel extends JComponent
{
	private Image cardImage;
	private Image cardHalo;
	private Image cardBack;
	
	private SymbolImageFactory symbolFactory;
	private ClientGUIController controller;
	
	private boolean highlighted;
	private boolean faceUp;
	private Card cardData;
	
	public CardPanel(ClientGUIController controller, Card cardData)
	{
		super();
		
		this.cardData = cardData;
		this.controller = controller;
		
		highlighted = false;
		faceUp = true;
		
		symbolFactory = SymbolImageFactory.getInstance();
		
		obtainResources();
		configurePanel();
		addActionListener();
	}
	
	/**
	 * 
	 * Resets this cards information to the indicated card data.
	 *
	 * @param cardData
	 */
	public void setCardData(Card cardData)
	{
		this.cardData = cardData;
		repaint();
	}
	
	/**
	 * 
	 * Can be used to turn this card face up or face down.
	 *
	 * @param value
	 */
	public void setFaceUp(boolean value)
	{
		this.faceUp = value;
		repaint();
	}
	
	/**
	 * 
	 * This function is used to set whether this card should be highlighted or not.
	 *
	 * @param value
	 */
	public void setHighlighted(boolean value)
	{
		this.highlighted = value;
		repaint();
	}


	/**
	 * Adds all of the action listeners to our class.
	 * @param controller 
	 *
	 */
	private void addActionListener()
	{
		MouseInputAdapter ma = new MouseInputAdapter()
		{
			public void mouseReleased(MouseEvent me)
			{
				if(faceUp && contains(me.getPoint()))
				{
					// Tell the controller we selected this card.
					controller.selectCard(cardData);
				}
			}
		};
		
		addMouseListener(ma);
	}

	/**
	 * Set all the basics
	 *
	 */
	private void configurePanel()
	{
		setSize(cardImage.getWidth(this), cardImage.getHeight(this));
	}

	/**
	 * Get all of the resources we need for building cards
	 *
	 */
	private void obtainResources()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		cardImage = resourceManager.getImage("card.png");
		cardHalo = resourceManager.getImage("card_halo.png");
		cardBack = resourceManager.getImage("card_back.png");
	}
	
	public void paintComponent(Graphics g)
	{
		System.out.println("CardPanel.paintComponent()");
		// Draw the card background and abort early.
		if(!faceUp || cardData == null)
		{
			g.drawImage(cardBack, 0, 0, this);
			return;
		}
		
		// Draw the card background.
		g.drawImage(cardImage, 0, 0, this);
		
		// Draw the halo
		if(highlighted)
		{
			g.drawImage(cardHalo, 0, 0, this);
		}
		
		// Draw the symbols, centered on the image.
		Image symbolImage = symbolFactory.getImage(cardData);
		g.drawImage(symbolImage, cardImage.getWidth(this) / 2 - symbolImage.getWidth(this) / 2,
				cardImage.getHeight(this) / 2 - symbolImage.getHeight(this) / 2, this);
	}
}
