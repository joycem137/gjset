package gjset.gui.framework;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

/**
 * This lass supports the large buttons present on the main interface.
 */
@SuppressWarnings("serial")
public class BigButton extends JComponent
{
	private Action	action;
	private boolean	pressed;
	private boolean enabled;
	private boolean mouseOver;
	
	private Image unpressedImage;
	private Image pressedImage;
	private Image disabledImage;
	private Image mouseOverImage;

	public BigButton(Action action, String style)
	{
		this.action = action;
		pressed = false;
		enabled = true;
		
		// Get all of our images.
		ResourceManager resourceManager = ResourceManager.getInstance();
		unpressedImage = resourceManager.getImage(style + ".png");
		pressedImage = resourceManager.getImage(style + "_d.png");
		disabledImage = resourceManager.getImage(style + "_grey.png");
		mouseOverImage = resourceManager.getImage(style + "_halo.png");
		
		// Add a listener.
		addActionListener();
		
		setSize(unpressedImage.getWidth(this), unpressedImage.getHeight(this));
	}
	
	/**
	 * Adds all of the action listeners to our class.
	 *
	 */
	private void addActionListener()
	{
		MouseInputAdapter ma = new MouseInputAdapter()
		{
			public void mouseEntered(MouseEvent me)
			{
				mouseOver = true;
				repaint();
			}
			
			public void mousePressed(MouseEvent me)
			{
				if(enabled)
				{
					// Depress the button.
					pressed = true;
					repaint();
				}
			}
			
			public void mouseReleased(MouseEvent me)
			{
				if(pressed)
				{
					// Button released!
					pressed = false;
					repaint();
					
					// Perform the requisite action.
					action.actionPerformed(new ActionEvent(this, me.getID(), ""));
				}
			}
			
			public void mouseExited(MouseEvent me)
			{
				// We exited the button.  Do not press it.
				mouseOver = false;
				pressed = false;
				repaint();
			}
		};
		
		addMouseListener(ma);
	}
	
	/**
	 * 
	 * Paint the button!
	 *
	 * @param g
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		if(!enabled)
		{
			g.drawImage(disabledImage, 0, 0, this);
		}
		else if(pressed)
		{
			g.drawImage(pressedImage, 0, 0, this);
		}
		else if(mouseOver)
		{
			g.drawImage(mouseOverImage, 0, 0, this);
		}
		else
		{
			g.drawImage(unpressedImage, 0, 0, this);
		}
	}

	/**
	 * Sets the disabled value for this button.
	 *
	 * @param b
	 */
	public void setDisabled(boolean b)
	{
		enabled = !b;
		repaint();
	}

}
