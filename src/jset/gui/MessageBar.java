package jset.gui;

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

public class MessageBar extends JComponent
{
	public BufferedImage image;
	private Font messageFont;
	private Timer messageTimer; //Used to clear text after a certain amount of time.
	private static final long MESSAGE_TIMEOUT = 3000;
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8467508002566639620L;

	public MessageBar()
	{
		super();
		
		setPreferredSize(new Dimension(1024, 70));
		setMinimumSize(new Dimension(1024, 70));
		setMaximumSize(new Dimension(1024, 70));
		
		setBackground(new Color(0, 102, 0));
		messageFont = new Font("Arial", Font.PLAIN, 54);
		
		//Define the timer
		messageTimer = new Timer();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(image == null)
		{
			image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		}
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
	}
	
	//This message clears the currently displayed message.
	public void clearMessage()
	{
		//TODO This method should animate the process of clearing the message.
		displayMessage("");
	}
	
	public void displayMessage(String message)
	{
		//Clear any pending timers
		messageTimer.cancel();
		messageTimer = new Timer();
		
		//Set the timer for the new message.
		//TODO Allow the message time to be part of the function call.
		messageTimer.schedule(new TimerTask()
		{
			public void run()
			{
				clearMessage();
			}
		}, MESSAGE_TIMEOUT);
		
		//Create our graphics object for the off screen image.
		Graphics2D g = image.createGraphics();
		
		//Create the basic rectangle.
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//Determine where to draw the message.
		Rectangle2D messageSize = messageFont.getStringBounds(message, g.getFontRenderContext());		
		Rectangle2D barSize = this.getBounds();
		
		//Set the font drawing settings.
		//TODO Improve the drawing style of the string.
		g.setColor(Color.red);
		g.setFont(messageFont);
		
		//Draw the message
		g.drawString(message, (int)(barSize.getCenterX() - messageSize.getCenterX()), (int)(barSize.getCenterY() - messageSize.getCenterY()));
		
		//TODO Only repaint this section of the screen.
		repaint();
	}
}
