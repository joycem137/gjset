package gjset.client.gui;

import gjset.gui.GeneralKeyStrokeFactory;
import gjset.gui.KeyStrokeFactory;
import gjset.gui.MacKeyStrokeFactory;
import gjset.gui.framework.Page;
import gjset.gui.framework.ResourceManager;
import gjset.gui.framework.SimpleImagePanel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
 * This class is the core panel that will contain all other panels for the program.
 * <P>
 * The launcher, game panel, and other panels will all be places on top of this one.
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	// Keyboard components
	private KeyStrokeFactory keyStrokeFactory;
	
	private JPanel rootPanel;
	
	private Stack<Page> pageHistory;
	private Page currentPage;
	
	public static final Rectangle PLAYING_FIELD_AREA = new Rectangle(14, 14, 996, 524);
	public static final Rectangle CONTROL_PANEL_AREA = new Rectangle(89, 582, 846, 185);
	public static final Rectangle TEXT_AREA = new Rectangle(16, 545, 992, 37);

	public MainFrame()
	{
		super("Combo Cards!");
		
		pageHistory = new Stack<Page>();	

		setOSVersion();

		createGUI();
	}

	public void loadPage(Page page)
	{
		// Destroy the old page, if there was one.
		if(currentPage != null)
		{
			// Push the page onto the stack.
			pageHistory.push(currentPage);
			
			// Now destroy the old page.
			rootPanel.remove(currentPage);
			currentPage.onHide();
		}
		
		//Load the new page.
		currentPage = page;
		rootPanel.add(page);
		
		rootPanel.revalidate();
		page.onShow();
		repaint();
	}

	/**
	 * Return to the previous page.
	 *
	 */
	public void backAPage()
	{
		if(!pageHistory.empty()) {
			loadPage(pageHistory.pop());
		}
	}

	/**
	 * Performs any tasks associated with setting the OS.
	 *
	 */
	private void setOSVersion()
	{
		// Determine which keystroke factory to grab.
		if (System.getProperty("os.name").contains("Mac OS X"))
		{
			keyStrokeFactory = new MacKeyStrokeFactory();

			// Set the page translucent.
			setBackground(new Color(1f, 1f, 1f, 0.0f));
			getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
		}
		else
		// use this for Windows and other non-Mac systems.
		{
			keyStrokeFactory = new GeneralKeyStrokeFactory();
			setBackground(new Color(0, 102, 0));
		}
	}

	// Construct the UI for the player.
	private void createGUI()
	{
		// Create the main window.
		setSize(1024, 768 + 43);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(null);
		
		//Add the background to the image
		ResourceManager resourceManager = ResourceManager.getInstance();
		Image backgroundImage = resourceManager.getImage("window_main.png");
		
		rootPanel = new SimpleImagePanel(backgroundImage);
		add(rootPanel);
		
		rootPanel.setLayout(null);
		rootPanel.setLocation(0, 0);
		rootPanel.setSize(1024, 768);
		
		// Turn off resizability.  I'm a control freak. :)
		setResizable(false);
		
		// Finish constructing the window.
		setVisible(true);
	}
	
}
