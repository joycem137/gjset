package gjset.client.gui;

import gjset.data.Player;
import gjset.gui.MainFrame;
import gjset.gui.framework.ResourceManager;
import gjset.gui.framework.SimpleImagePanel;
import gjset.gui.framework.SimpleLookAndFeel;

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
public class LocalPlayerPanel extends JPanel
{
	private SimpleLookAndFeel lnf;

	// And store the various items that should be on screen.
	private JLabel nameLabel;
	private JLabel scoreLabel;
	private SimpleImagePanel portraitPanel;
	
	
	// And our two first order objects.
	private JPanel mainPanel;
	private EventBubble eventBubble;
	
	private static final int HORIZONTAL_INSET = 6;
	private static final int VERTICAL_INSET = 7;
	
	public LocalPlayerPanel()
	{
		lnf = SimpleLookAndFeel.getLookAndFeel();
		
		// The order that we have to call this in is interesting, since there's a lot of dependencies here.
		// Just go with me on this.  I don't like it either.
		// TODO: Improve all of the dependencies here.
		configurePanel();
		
		createPortrait();
		createEventBubble();
		
		setSizeAndLocation();
		
		createPlayerNameLabel();
		createPlayerScorePanel();
		
		add(mainPanel);
	}

	public void updatePlayerData(Player player, int setCallerId)
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
		
		// Set up the event bubble.
		if(player.getWantsToDraw())
		{
			eventBubble.setState(EventBubble.STATE_DRAW);
		}
		else if(player.getId() == setCallerId)
		{
			eventBubble.setState(EventBubble.STATE_CALL);
		}
		else
		{
			eventBubble.setState(EventBubble.STATE_NONE);
		}
	}
	
	/**
	 * Configure the basic panel settings.
	 *
	 */
	private void configurePanel()
	{
		// First do this for us.
		setLayout(null);
		setOpaque(false);
		
		// Create the main panel for putting things in.
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		
		// And the background.
		mainPanel.setBackground(lnf.getPlayerPanelBackgroundColor());
	}

	/**
	 * Create the main panel upon which most of this stuff will reside.
	 *
	 */
	private void setSizeAndLocation()
	{
		// Get the extra allotment for the event bubble
		int bubbleOffset = eventBubble.getHeight() / 2;
		
		// The size comes from Jamie's original specifications.
		mainPanel.setSize(282, 110);
		setSize(mainPanel.getWidth(), mainPanel.getHeight() + bubbleOffset);
		
		// Center this panel in the control frame.		
		Rectangle controlFrame = MainFrame.CONTROL_PANEL_AREA;
		
		int baseX = controlFrame.x + controlFrame.width / 2 - getWidth() / 2;
		int baseY = controlFrame.y + controlFrame.height / 2 - getHeight() / 2;
		
		setLocation(baseX, baseY - bubbleOffset);
		mainPanel.setLocation(0, bubbleOffset);
	}

	/**
	 * Create the event bubble to show on screen.
	 *
	 */
	private void createEventBubble()
	{
		eventBubble = new EventBubble(EventBubble.ORIENT_LEFT);
		
		// Position the bubble.
		int x = portraitPanel.getX() + portraitPanel.getWidth() / 2;
		int y = 15 + portraitPanel.getY() - portraitPanel.getHeight() / 2 + eventBubble.getHeight() / 2;
		
		eventBubble.setLocation(x, y);
		
		add(eventBubble);
	}

	/**
	 * Create the portrait panel
	 *
	 */
	private void createPortrait()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		Image defaultPortraitImage = resourceManager.getImage("icon_player_default.png");
		
		portraitPanel = new SimpleImagePanel(defaultPortraitImage);
		portraitPanel.setLocation(HORIZONTAL_INSET, VERTICAL_INSET);
		mainPanel.add(portraitPanel);
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
		int rightSideOfPortrait = 2 * HORIZONTAL_INSET + portraitPanel.getWidth();
		nameLabel.setLocation(rightSideOfPortrait, 13);
		nameLabel.setSize(mainPanel.getWidth() - rightSideOfPortrait, 30);
		
		// Then finally, add it.
		mainPanel.add(nameLabel);
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
		int rightSideOfPortrait = 2 * HORIZONTAL_INSET + portraitPanel.getWidth();
		scoreLabel.setLocation(rightSideOfPortrait, nameLabel.getY() + nameLabel.getHeight() + 4);
		scoreLabel.setSize(mainPanel.getWidth() - rightSideOfPortrait, 50);
		
		// Then finally add it.
		mainPanel.add(scoreLabel);
	}
	
}
