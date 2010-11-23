package gjset.client.gui;

import gjset.data.Player;
import gjset.gui.framework.ResourceManager;
import gjset.gui.framework.SimpleLookAndFeel;

import java.awt.Graphics;
import java.awt.Graphics2D;
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
public class OtherPlayerPanel extends JPanel
{
	private SimpleLookAndFeel lnf;
	
	// Store our default portrait.  In the future, this contain a selectable image.
	private Image defaultPortraitImage;

	// And store the various items that should be on screen.
	private JLabel nameLabel;
	private JLabel scoreLabel;
	
	private static final int HORIZONTAL_INSET = 14;
	private static final int VERTICAL_INSET = 8;
	
	private static final double IMAGE_SCALE = 0.66666666;
	
	public OtherPlayerPanel()
	{
		lnf = SimpleLookAndFeel.getLookAndFeel();

		configurePanel();
		
		getScaledPortraitImage();
		
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
	 * Create the portrait image, scaling it down to fit on this page.
	 *
	 */
	private void getScaledPortraitImage()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		 Image baseImage = resourceManager.getImage("icon_player_default.png");
		 
		 int newWidth = (int)(baseImage.getWidth(this) * IMAGE_SCALE);
		 int newHeight = (int)(baseImage.getHeight(this) * IMAGE_SCALE);
		 
	     ImageFilter replicate = new ReplicateScaleFilter(newWidth, newHeight);
	     ImageProducer prod = new FilteredImageSource(baseImage.getSource(),replicate);
	     
	     defaultPortraitImage = createImage(prod);
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
		setSize(92, 127);
		
		setBackground(lnf.getOtherPlayerPanelBackgroundColor());
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
		int bottomOfImage = VERTICAL_INSET + defaultPortraitImage.getHeight(this);
		nameLabel.setLocation(0, bottomOfImage + 4);
		nameLabel.setSize(getWidth(), 15);
		
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
		scoreLabel.setFont(lnf.getOtherPlayerPanelScoreFont());
		scoreLabel.setForeground(lnf.getOtherPlayerPanelScoreColor());
		
		// Position the label.
		scoreLabel.setLocation(0, nameLabel.getY() + nameLabel.getHeight() + 1);
		scoreLabel.setSize(getWidth(), 30);
		
		// Then finally add it.
		add(scoreLabel);
	}
	

}
