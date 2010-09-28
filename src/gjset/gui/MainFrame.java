package gjset.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
public class MainFrame extends JFrame
{
	private GPLPopup			gplPopup;

	// Keyboard components
	private KeyStrokeFactory	keyStrokeFactory;

	private final Color			backgroundColor	= new Color(0, 102, 0); // Store the background color to be used here.

	private Launcher			launcherDialog;

	public MainFrame()
	{
		super("gJSet");

		// Determine which keystroke factory to grab.
		if (System.getProperty("os.name").contains("Mac OS X"))
		{
			keyStrokeFactory = new MacKeyStrokeFactory();
		}
		else
		// use this for Windows and other non-Mac systems.
		{
			keyStrokeFactory = new GeneralKeyStrokeFactory();
		}

		createGUI();
	}

	// Construct the UI for the player.
	private void createGUI()
	{
		// Create the main window.
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(null);

		// Set the color of the background.
		getContentPane().setBackground(backgroundColor);

		// Create the sub components of the window.
		createMenu();

		// Finish constructing the window.
		setVisible(true);

		// We're going to start with the launcher open. Add it to the panel.
		showLauncherDialog();
	}

	/**
	 * This method creates and displays the launcher panel.
	 */
	private void showLauncherDialog()
	{
		if (launcherDialog == null)
		{
			launcherDialog = new Launcher();
			add(launcherDialog);
		}
	}

	// Create the standard UI menu bar.
	private void createMenu()
	{
		JMenuBar jMenuBar = new JMenuBar();
		setJMenuBar(jMenuBar);

		// Build the file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		// Build the new game option
		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.setAccelerator(keyStrokeFactory.getNewGameAcceleratorKeyStroke());
		fileMenu.add(newGameItem);
		newGameItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				showLauncherDialog();
			}
		});

		// Build the exit item.
		JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
		exitItem.setAccelerator(keyStrokeFactory.getExitGameAcceleratorKeyStroke());
		fileMenu.add(exitItem);

		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				// TODO: Exit the game.
				System.exit(0);
			}
		});

		// Add the file menu to the menu bar.
		jMenuBar.add(fileMenu);

		// Create the help menu for the GNU stuff.
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		// Create the "GPL Text" item.
		JMenuItem gplItem = new JMenuItem("About GPL", KeyEvent.VK_G);
		helpMenu.add(gplItem);

		// Create the gplPopup
		gplPopup = new GPLPopup();

		// Add the GPL text to the gplItem
		gplItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				displayGPLPopup();
			}
		});

		// Add the help menu to the menu bar.
		jMenuBar.add(helpMenu);

	}

	private void displayGPLPopup()
	{
		gplPopup.displayPopup(this);
	}
}
