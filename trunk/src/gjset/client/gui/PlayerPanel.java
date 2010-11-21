package gjset.client.gui;

import gjset.data.Player;
import gjset.gui.MainFrame;
import gjset.gui.SimpleLookAndFeel;
import gjset.gui.framework.ResourceManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
	private JLabel playerNameLabel;
	
	public PlayerPanel()
	{
		lnf = SimpleLookAndFeel.getLookAndFeel();

		ResourceManager resourceManager = ResourceManager.getInstance();
		
		defaultPortraitImage = resourceManager.getImage("icon_player_default.png");
		
		configurePanel();
		
		addPlayerNameLabel();
		
		updatePlayerData(new Player());
	}

	/**
	 * Add the label for the player's name.
	 *
	 */
	private void addPlayerNameLabel()
	{
		playerNameLabel = new JLabel("", SwingConstants.CENTER);
		
		playerNameLabel.setFont(lnf.getPlayerPanelNameFont());
		playerNameLabel.setForeground(lnf.getPlayerPanelNameColor());
		
		int rightSideOfPortrait = 6 + defaultPortraitImage.getWidth(this) + 6;
		
		playerNameLabel.setLocation(rightSideOfPortrait, 22);
		playerNameLabel.setSize(getWidth() - rightSideOfPortrait, 30);
		
		add(playerNameLabel);
	}
	
	public void updatePlayerData(Player player)
	{
		playerNameLabel.setText(player.getName());
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
		g.drawImage(defaultPortraitImage, 6, 7, this);
	}
	
}
