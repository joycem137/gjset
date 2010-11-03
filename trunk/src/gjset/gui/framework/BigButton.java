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
	
	private Image unpressedImage;
	private Image pressedImage;
	private Image disabledImage;

	public BigButton(Action action, String style)
	{
		this.action = action;
		pressed = false;
		enabled = true;
		
		// Get all of our images.
		ResourceManager resourceManager = ResourceManager.getInstance();
		unpressedImage = resourceManager.getImage(style + ".png");
		pressedImage = resourceManager.getImage(style + "_halo.png");
		disabledImage = resourceManager.getImage(style + "_grey.png");
		
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
