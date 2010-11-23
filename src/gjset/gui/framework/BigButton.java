package gjset.gui.framework;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  Set¨ is a registered trademark of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of Combo Cards are very grateful for
 *  them creating such an excellent card game.
 *  
 *  Combo Cards is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Combo Cards is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with Combo Cards.  If not, see <http://www.gnu.org/licenses/>.
 */

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
