package gjset.client.gui;

import gjset.data.Player;
import gjset.gui.framework.ResourceManager;
import gjset.gui.framework.SimpleImagePanel;
import gjset.gui.framework.SimpleLookAndFeel;

import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;

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
 * This is the panel along the sides of the screen that show the list of other players joined to this game.
 */
@SuppressWarnings("serial")
public class RemotePlayerPanel extends JPanel
{
	private SimpleLookAndFeel lnf;

	// And store the various items that should be on screen.
	private JLabel nameLabel;
	private JLabel scoreLabel;
	private SimpleImagePanel portraitPanel;

	private EventBubble eventBubble;
	
	private JPanel mainPanel;

	private int realX;

	private int realY;
	
	private static final int HORIZONTAL_INSET = 14;
	private static final int VERTICAL_INSET = 8;
	
	private static final double IMAGE_SCALE = 0.66666666;
	
	public RemotePlayerPanel()
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

	/**
	 * 
	 * Override the set location method to set it more correctly.
	 *
	 * @param x
	 * @param y
	 * @see java.awt.Component#setLocation(int, int)
	 */
	public void setLocation(int x, int y)
	{
		this.realX = x;
		this.realY = y;
		
		// Get the extra allotment for the event bubble
		int verticalOffset = eventBubble.getHeight() / 2;
		
		super.setLocation(x, y - verticalOffset);
	}
	
	/**
	 * 
	 * Return the width of the mainPanel
	 *
	 * @return
	 */
	public int getMainWidth()
	{
		return mainPanel.getWidth();
	}
	
	/**
	 * 
	 * return the height of the mainPanel.
	 *
	 * @return
	 */
	public int getMainHeight()
	{
		return mainPanel.getHeight();
	}
		
	/**
	 * Change the orientation of the event bubble.
	 *
	 * @param orientation
	 */
	public void setOrientation(int orientation)
	{
		int x = portraitPanel.getX() + portraitPanel.getWidth() / 2;
		int y = 5 + portraitPanel.getY() - portraitPanel.getHeight() / 2 + eventBubble.getHeight() / 2;
		
		// Get the extra allotment for the event bubble
		int verticalOffset = eventBubble.getHeight() / 2;
		int horizontalOffset = eventBubble.getWidth() / 2;
		
		eventBubble.setOrientation(orientation);
		setLocation(realX, realY);
		
		if(orientation == EventBubble.ORIENT_LEFT)
		{
			eventBubble.setLocation(x, y);
			mainPanel.setLocation(0, verticalOffset);
		}
		else
		{
			eventBubble.setLocation(0, y);
			mainPanel.setLocation(horizontalOffset, verticalOffset);
		}
	}

	/**
	 * 
	 * Update the information for this player.
	 *
	 * @param player
	 * @param setCallerId
	 */
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
		mainPanel.setBackground(lnf.getRemotePlayerPanelBackgroundColor());
	}

	/**
	 * Create the main panel upon which most of this stuff will reside.
	 *
	 */
	private void setSizeAndLocation()
	{
		// Get the extra allotment for the event bubble
		int verticalOffset = eventBubble.getHeight() / 2;
		int horizontalOffset = eventBubble.getWidth() / 2;
		
		// The size comes from Jamie's original specifications.
		mainPanel.setSize(92, 127);
		setSize(mainPanel.getWidth() + horizontalOffset, mainPanel.getHeight() + verticalOffset);
		
		mainPanel.setLocation(0, verticalOffset);
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
		int y = 5 + portraitPanel.getY() - portraitPanel.getHeight() / 2 + eventBubble.getHeight() / 2;
		
		eventBubble.setLocation(x, y);
		
		add(eventBubble);
	}

	/**
	 * Create the portrait panel
	 *
	 */
	private void createPortrait()
	{
		Image defaultPortraitImage = getScaledPortraitImage();
		
		portraitPanel = new SimpleImagePanel(defaultPortraitImage);
		portraitPanel.setLocation(HORIZONTAL_INSET, VERTICAL_INSET);
		
		mainPanel.add(portraitPanel);
	}

	/**
	 * Create the portrait image, scaling it down to fit on this page.
	 *
	 */
	private Image getScaledPortraitImage()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		 Image baseImage = resourceManager.getImage("icon_player_default.png");
		 
		 int newWidth = (int)(baseImage.getWidth(this) * IMAGE_SCALE);
		 int newHeight = (int)(baseImage.getHeight(this) * IMAGE_SCALE);
		 
	     ImageFilter replicate = new ReplicateScaleFilter(newWidth, newHeight);
	     ImageProducer prod = new FilteredImageSource(baseImage.getSource(),replicate);
	     
	     return createImage(prod);
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
		nameLabel.setFont(lnf.getOtherPlayerPanelNameFont());
		nameLabel.setForeground(lnf.getOtherPlayerPanelNameColor());
		
		// Position the label.
		int bottomOfImage = VERTICAL_INSET + portraitPanel.getHeight();
		nameLabel.setLocation(0, bottomOfImage + 4);
		nameLabel.setSize(mainPanel.getWidth(), 15);
		
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
		scoreLabel.setFont(lnf.getOtherPlayerPanelScoreFont());
		scoreLabel.setForeground(lnf.getOtherPlayerPanelScoreColor());
		
		// Position the label.
		scoreLabel.setLocation(0, nameLabel.getY() + nameLabel.getHeight() + 1);
		scoreLabel.setSize(mainPanel.getWidth(), 30);
		
		// Then finally add it.
		mainPanel.add(scoreLabel);
	}
	

}
