package gjset.client.gui;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

/**
 * This class represents the message bar at the top of the screen, used to display messages to the user.
 * <P>
 * It may be overwritten in the future with a better messaging system. 
 */
@SuppressWarnings("serial")
public class MessageBar extends JComponent
{
	//Stores the offscreen image buffer to write messages to.
	private BufferedImage		image;
	
	//Stores the font of the text to use.
	private Font				messageFont;
	
	// Used to clear text after a certain amount of time.
	private Timer				messageTimer;
	
	//The length of time to keep any one message on screen.
	private static final long	MESSAGE_TIMEOUT		= 3000;

	/**
	 * 
	 * Instantiate the object, setting the font, basic size/color, etc.
	 *
	 */
	public MessageBar()
	{
		super();

		setPreferredSize(new Dimension(1024, 70));
		setMinimumSize(new Dimension(1024, 70));
		setMaximumSize(new Dimension(1024, 70));

		setBackground(new Color(0, 102, 0));
		messageFont = new Font("Arial", Font.PLAIN, 54);

		// Define the timer
		messageTimer = new Timer();
	}

	/**
	 * 
	 * Draw the offscreen buffer to the screen.  If the offscreen buffer does not yet exist, create it.
	 *
	 * @param g - Graphics context to draw to.
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//Check to see if the offscreen image buffer has been created yet.  If not, create it.
		if (image == null)
		{
			// Create the image to store the message to be displayed.
			image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);

			// Create our graphics object for the off screen image.
			Graphics2D g2 = image.createGraphics();

			// Create the basic rectangle.
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		
		//Draw the offscreen buffer to the screen.
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
	}

	/**
	 * This clears the currently displayed message immediately.
	 *
	 */
	public void clearMessage()
	{
		displayMessage("");
	}

	/**
	 * 
	 * Displays the indicated message in the message bar for 3 seconds.
	 *
	 * @param message The message to display on screen.
	 */
	public void displayMessage(String message)
	{
		// Clear any pending timers
		messageTimer.cancel();
		messageTimer = new Timer();

		// Set the timer for the new message.
		messageTimer.schedule(new TimerTask()
		{
			public void run()
			{
				clearMessage();
			}
		}, MESSAGE_TIMEOUT);

		// Create our graphics object for the off screen image.
		Graphics2D g = image.createGraphics();

		// Create the basic rectangle.
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

		// Determine where to draw the message.
		Rectangle2D messageSize = messageFont.getStringBounds(message, g.getFontRenderContext());
		Rectangle2D barSize = this.getBounds();

		// Set the font drawing settings.
		g.setColor(Color.red);
		g.setFont(messageFont);

		// Draw the message
		g.drawString(message, (int) (barSize.getCenterX() - messageSize.getCenterX()),
				(int) (barSize.getCenterY() - messageSize.getCenterY()));
		
		repaint();
	}
}
