package gjset.client.gui;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of gjSet are very grateful for
 *  them creating such an excellent card game.
 *  
 *  gjSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  gjSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with gjSet.  If not, see <http://www.gnu.org/licenses/>.
 */

import gjset.client.EngineLinkInterface;
import gjset.data.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class PlayerPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3670300153110634539L;

	private Image				offScreenImage;
	private Graphics2D			offScreenGraphics;

	private CardTableComponent	table;
	private Player				player				= new Player(1);

	private Font				messageFont;
	
	private static final Color	panelColor			= new Color(0, 51, 0);

	public PlayerPanel(CardTableComponent table, final EngineLinkInterface engine)
	{
		super();

		this.table = table;

		// Set the size of the card table.
		setPreferredSize(new Dimension(720, 200));
		setMinimumSize(new Dimension(720, 200));
		setMaximumSize(new Dimension(720, 200));

		// Handle resizing the player panel.
		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				JComponent component = (JComponent) e.getSource();

				int width = component.getWidth();
				int height = component.getHeight();

				offScreenImage = component.createImage(width, height);
				offScreenGraphics = (Graphics2D) offScreenImage.getGraphics();
				drawPanel(player);
			}
		});

		setLayout(new FlowLayout());

		JButton setButton = new JButton(new AbstractAction("No more sets.")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1742911717343411450L;

			public void actionPerformed(ActionEvent arg0)
			{
				engine.callNoMoreSets();
			}
		});

		// Set the button's size.
		setButton.setPreferredSize(new Dimension(180, 30));
		setButton.setMaximumSize(new Dimension(180, 30));
		setButton.setMinimumSize(new Dimension(180, 30));

		// Add the button to the screen.
		add(setButton);
	}

	public void drawPanel(Player player)
	{
		// Draw the background
		offScreenGraphics.setColor(table.getBackground());
		offScreenGraphics.fillRect(0, 0, getWidth(), getHeight());

		// Draw the bottom half of the panel.
		offScreenGraphics.setColor(panelColor);
		offScreenGraphics.fillRoundRect(15, getHeight() - 80, getWidth() - 30, 50, 15, 15);

		// Draw the bottom of the border of the panel.
		offScreenGraphics.setColor(Color.black);
		offScreenGraphics.setStroke(new BasicStroke(3.0f));
		offScreenGraphics.drawRoundRect(15, 0, getWidth() - 30, getHeight() - 30, 15, 15);

		// Draw the top half of the panel.
		offScreenGraphics.setColor(panelColor);
		offScreenGraphics.fillRect(15, 0, getWidth() - 30, getHeight() - 60);

		// Draw the border of the bottom of the panel.
		offScreenGraphics.setColor(Color.black);
		offScreenGraphics.setStroke(new BasicStroke(3.0f));
		offScreenGraphics.drawLine(15, 0, 15, getHeight() - 60);
		offScreenGraphics.drawLine(getWidth() - 15, 0, getWidth() - 15, getHeight() - 60);

		// Display the player score info.
		this.player = player;
		messageFont = new Font("Arial", Font.PLAIN, 12);
		offScreenGraphics.setColor(Color.red);
		offScreenGraphics.setFont(messageFont);
		offScreenGraphics.drawString(player.getName(), 30, 15);
		offScreenGraphics.setColor(Color.blue);
		offScreenGraphics.drawString(String.valueOf(player.getScore()), 30, 35);		
		
		// Flush the image.
		offScreenImage.flush();

		// Repaint the screen.
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(offScreenImage, 0, 0, getWidth(), getHeight(), this);
	}
}
