package gjset.gui;


import gjset.gui.framework.Page;
import gjset.gui.framework.ResourceManager;
import gjset.gui.framework.SimpleImagePanel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008-2009 Joyce Murton and Andrea Kilpatrick
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

/**
 * This class is the core panel that will contain all other panels for the program.
 * <P>
 * The launcher, game panel, and other panels will all be places on top of this one.
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	// Keyboard components
	private KeyStrokeFactory	keyStrokeFactory;
	
	private JPanel rootPanel;
	private Page currentPage;
	
	private Rectangle playingFieldArea;

	public MainFrame()
	{
		super("Combo Cards!");
		
		// Currently hard coded information about where the playing area is.
		playingFieldArea = new Rectangle(14, 14, 996, 522);

		setOSVersion();

		createGUI();
		
		loadFirstPage();
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
		
		// Turn of resizability.  I'm a control freak. :)
		setResizable(false);
		
		// Finish constructing the window.
		setVisible(true);
	}
	
	public void loadPage(Page page)
	{
		// Destroy the old page, if there was one.
		if(currentPage != null)
		{
			System.out.println("Destroying page " + currentPage);
			
			rootPanel.remove(currentPage);
			currentPage.destroy();
		}
		
		//Load the new page.
		System.out.println("Adding page " + page);
		
		currentPage = page;
		rootPanel.add(page);
		
		rootPanel.revalidate();
		repaint();
	}

	/**
	 * Loads the first page.
	 *
	 */
	private void loadFirstPage()
	{
		loadPage(new LaunchPage(playingFieldArea, this));
	}
}
