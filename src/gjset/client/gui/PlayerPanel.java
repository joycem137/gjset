package gjset.client.gui;

import gjset.data.Player;
import gjset.gui.MainFrame;
import gjset.gui.framework.ResourceManager;
import gjset.gui.framework.SimpleLookAndFeel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
 * This class stores all of the information for displaying the score, timer, portrait, and username for
 * this system's player.
 */
@SuppressWarnings("serial")
public class PlayerPanel extends JPanel
{
	private SimpleLookAndFeel lnf;
	
	// Store our default portrait.  In the future, this contain a selectable image.
	private Image defaultPortraitImage;

	// And store the various items that should be on screen.
	private JLabel nameLabel;
	private JLabel scoreLabel;
	
	private static final int HORIZONTAL_INSET = 6;
	private static final int VERTICAL_INSET = 7;
	
	public PlayerPanel()
	{
		lnf = SimpleLookAndFeel.getLookAndFeel();

		ResourceManager resourceManager = ResourceManager.getInstance();
		
		defaultPortraitImage = resourceManager.getImage("icon_player_default.png");
		
		configurePanel();
		
		createPlayerNameLabel();
		createPlayerScorePanel();
	}

	public void updatePlayerData(Player player)
	{
		nameLabel.setText(player.getName());
		
		int score = player.getScore();
		String scoreString = "" + score;
		
		// Add a leading 0.
		if(score < 10)
		{
			scoreString = "0" + scoreString;
		}
		
		scoreLabel.setText(scoreString);
	}

	/**
	 * 
	 * Paint this object onto the screen.
	 *
	 * @param g
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		// Start by drawing the background.
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		// Then draw the portrait.
		g.drawImage(defaultPortraitImage, HORIZONTAL_INSET, VERTICAL_INSET, this);
	}

	/**
	 * Configure the basic panel settings.
	 *
	 */
	private void configurePanel()
	{
		setLayout(null);
		setOpaque(false);
	
		// The size comes from Jamie's original specifications.
		setSize(282, 110);
		
		// Center this panel in the control frame.		
		Rectangle controlFrame = MainFrame.CONTROL_PANEL_AREA;
		setLocation(controlFrame.x + controlFrame.width / 2 - getWidth() / 2, 
				controlFrame.y + controlFrame.height / 2 - getHeight() / 2);
		
		setBackground(lnf.getPlayerPanelBackgroundColor());
	}

	/**
	 * Add the label for the player's name.
	 *
	 */
	private void createPlayerNameLabel()
	{
		nameLabel = new JLabel();
		
		// Configure the label.
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setFont(lnf.getPlayerPanelNameFont());
		nameLabel.setForeground(lnf.getPlayerPanelNameColor());
		
		// Position the label.
		int rightSideOfPortrait = 2 * HORIZONTAL_INSET + defaultPortraitImage.getWidth(this);
		nameLabel.setLocation(rightSideOfPortrait, 13);
		nameLabel.setSize(getWidth() - rightSideOfPortrait, 30);
		
		// Then finally, add it.
		add(nameLabel);
	}

	/**
	 * Add the label for the player's score.
	 *
	 */
	private void createPlayerScorePanel()
	{
		scoreLabel = new JLabel();
		
		// Configure the label.
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scoreLabel.setFont(lnf.getPlayerPanelScoreFont());
		scoreLabel.setForeground(lnf.getPlayerPanelScoreColor());
		
		// Position the label.
		int rightSideOfPortrait = 2 * HORIZONTAL_INSET + defaultPortraitImage.getWidth(this);
		scoreLabel.setLocation(rightSideOfPortrait, nameLabel.getY() + nameLabel.getHeight() + 4);
		scoreLabel.setSize(getWidth() - rightSideOfPortrait, 50);
		
		// Then finally add it.
		add(scoreLabel);
	}
	
}
