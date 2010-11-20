package gjset.client.gui;

import gjset.gui.MainFrame;
import gjset.gui.SimpleLookAndFeel;
import gjset.gui.framework.ResourceManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * This class stores all of the information for displaying the score, timer, portrait, and username for
 * this system's player.
 */
@SuppressWarnings("serial")
public class PlayerPanel extends JPanel
{
	private SimpleLookAndFeel lnf;
	
	// Store all of our various images.
	private Image defaultPortraitImage;
	
	public PlayerPanel()
	{
		lnf = SimpleLookAndFeel.getLookAndFeel();

		ResourceManager resourceManager = ResourceManager.getInstance();
		
		defaultPortraitImage = resourceManager.getImage("icon_player_default.png");
		
		configurePanel();
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
