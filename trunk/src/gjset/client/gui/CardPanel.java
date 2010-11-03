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
	private Image	cardHalo;
	private Image	cardBack;
	
	private SymbolImageFactory symbolFactory;
	
	private boolean selected;
	private boolean faceUp;
	
	public CardPanel()
	{
		super();
		
		selected = false;
		faceUp = true;
		
		symbolFactory = SymbolImageFactory.getInstance();
		
		obtainResources();
		configurePanel();
		addActionListener();
	}


	/**
	 * Adds all of the action listeners to our class.
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
					// Depress the button.
					selected = !selected;
					repaint();
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
		// Draw the card background and abort early.
		if(!faceUp)
		{
			g.drawImage(cardBack, 0, 0, this);
			return;
		}
		
		// Draw the card background.
		g.drawImage(cardImage, 0, 0, this);
		
		// Draw the halo
		if(selected)
		{
			g.drawImage(cardHalo, 0, 0, this);
		}
		
		// Draw the symbols, centered on the image.
		Image symbolImage = symbolFactory.getImage(3, Card.COLOR_RED, Card.SHAPE_OVAL, Card.SHADING_STRIPED);
		g.drawImage(symbolImage, cardImage.getWidth(this) / 2 - symbolImage.getWidth(this) / 2,
				cardImage.getHeight(this) / 2 - symbolImage.getHeight(this) / 2, this);
	}
}
