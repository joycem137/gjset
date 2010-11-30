package gjset.gui.framework;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
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
 * This is a replacement for the basic JButton class that allows us to
 * do more simple skinning.
 */
@SuppressWarnings("serial")
public class Button extends JComponent
{
	private Image	leftUp;
	private Image	middleUp;
	private Image	rightUp;
	private Image	leftDown;
	private Image	middleDown;
	private Image	rightDown;
	
	private boolean	pressed;
	private Action	action;
	private boolean mouseDown;
	
	private String text;
	private boolean textVisible;

	/**
	 * Build a button with the indicated action.
	 *
	 * @param action An action specifying the behavior of the button.
	 * @param style A string indicated the button style to use from the resource directory
	 */
	public Button(Action action, String style)
	{	
		this.action = action;
		pressed = false;
		mouseDown = false;
		
		// Get all of our images.
		ResourceManager resourceManager = ResourceManager.getInstance();
		leftUp = resourceManager.getImage(style + "_l.png");
		middleUp = resourceManager.getImage(style + "_m.png");
		rightUp = resourceManager.getImage(style + "_r.png");
		
		leftDown = resourceManager.getImage(style + "_d_l.png");
		middleDown = resourceManager.getImage(style + "_d_m.png");
		rightDown = resourceManager.getImage(style + "_d_r.png");
		
		// Add a listener.
		addActionListener();
		
		// Start with the action's text and text being visible.
		text = (String)action.getValue(Action.NAME);
		textVisible = true;
	}
	
	/**
	 * Indicate whether or not text is visible on the button.
	 *
	 * @param value
	 */
	public void setTextVisible(boolean value)
	{
		textVisible = value;
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
		// Draw the button.
		if(pressed)
		{
			// Draw the edges first.
			g.drawImage(leftDown, 0, 0, this);
			g.drawImage(rightDown, getWidth() - rightDown.getWidth(this), 0, this);
			
			// Finally, draw the middle.
			Rectangle areaToPaint = new Rectangle(leftDown.getWidth(this), 0, 
					getWidth() - leftDown.getWidth(this) - rightDown.getWidth(this), 
					middleDown.getHeight(this));
			PaintUtilities.texturePaintHorizontal(this, g, middleDown, areaToPaint);
		}
		else
		{
			// Draw the edges first.
			g.drawImage(leftUp, 0, 0, this);
			g.drawImage(rightUp, getWidth() - rightUp.getWidth(this), 0, this);
			
			// Finally, draw the middle.
			Rectangle areaToPaint = new Rectangle(leftUp.getWidth(this), 0, 
					getWidth() - leftUp.getWidth(this) - rightUp.getWidth(this), 
					middleUp.getHeight(this));
			PaintUtilities.texturePaintHorizontal(this, g, middleUp, areaToPaint);
		}
		
		// Draw the text.
		if(textVisible)
		{
			g.setFont(getFont());
			Point point = calculateTextPosition(g);
			
			g.setColor(getForeground());
			g.drawString(text, point.x, point.y);
		}
		
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
				// Depress the button.
				mouseDown = true;
				pressed = true;
				repaint();
			}
			
			public void mouseReleased(MouseEvent me)
			{
				if(pressed)
				{
					// Button released!
					mouseDown = false;
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
			
			public void mouseEntered(MouseEvent me)
			{
				if(mouseDown)
				{
					pressed = true;
					repaint();
				}
			}
		};
		
		addMouseListener(ma);
	}
	
	/**
	 * Calculates the starting position of the text, based on the horizontal alignment value
	 *
	 * @return a Point object that represents the location to draw the string on.
	 */
	private Point calculateTextPosition(Graphics g)
	{
		FontMetrics metrics = g.getFontMetrics();
		
		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getMaxAscent();
		
		Point textPosition = new Point();
		textPosition.x = getWidth() / 2 - textWidth / 2;
		textPosition.y = getHeight() / 2 + textHeight / 2 - 2;
		
		// Offset a little if pressed to give buttons that 3D look.
		if(pressed)
		{
			textPosition.x++;
			textPosition.y++;
		}
		
		return textPosition;
	}
}
