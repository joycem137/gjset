package gjset.client.gui;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton and Andrea Kilpatrick
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

/**
 * This is the main class for handling the user interface for a player.  It is responsible for creating any and all
 * components to put on the screen and interpreting incoming user actions.
 * <P>
 * Additionally, this is the main class for the client.
 */
public class PlayerUI
{
	//UI Components
	private JFrame				topFrame;
	private CardTableComponent	cardTable;
	private MessageBar			messageBar;
	private PlayerPanel			playerPanel;
	private GPLPopup			gplPopup;
	
	//Keyboard components
	private KeyStrokeFactory	keyStrokeFactory;
	
	//Engine interface
	private EngineLinkInterface		engine;

	/**
	 * 
	 * Creates a PlayerUI with the given link to the {@link GameEngine}.
	 * <P>
	 * This method also results in the construction of the UI in the AWT event dispatching thread.
	 *
	 * @param engine The object to use to communicate with the GameEngine.
	 */
	public PlayerUI(EngineLinkInterface engine)
	{
		//Store a link to the game engine.
		this.engine = engine;
		
		// Determine which keystroke factory to grab.
		if (System.getProperty("os.name").contains("Mac OS X"))
		{
			keyStrokeFactory = new MacKeyStrokeFactory();
		}
		else  // use this for Windows and other non-Mac systems.
		{
			keyStrokeFactory = new GeneralKeyStrokeFactory();
		}

		//Instantiate the UI in the AWT event dispatching thread.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createGUI();
			}
		});
	}

	//Construct the UI for the player.
	private void createGUI()
	{
		//Create the main window.
		topFrame = new JFrame("gJSet");
		topFrame.getContentPane().setLayout(new BorderLayout());
		topFrame.setSize(1024, 768);
		topFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create the sub components of the window.
		createMenu();
		createCardTable();
		createPlayerPanel();
		createMessageBar();

		//Finish constructing the window.
		topFrame.pack();
		topFrame.setVisible(true);
	}

	//Create the message bar that goes at the top of the page and displays messages.
	private void createMessageBar()
	{
		messageBar = new MessageBar();
		topFrame.add(messageBar, BorderLayout.NORTH);
	}

	//Create the card table, the big thing in the center of the screen that shows all the cards.
	private void createCardTable()
	{
		cardTable = new CardTableComponent(engine);
		topFrame.add(cardTable, BorderLayout.CENTER);
	}

	//Create the player panel to go on the bottom of the screen.
	private void createPlayerPanel()
	{
		playerPanel = new PlayerPanel(cardTable, engine);
	}

	//Create the standard UI menu bar.
	private void createMenu()
	{
		JMenuBar jMenuBar = new JMenuBar();
		topFrame.setJMenuBar(jMenuBar);

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
				engine.startNewGame();
			}
		});


		// Build the add player option
		JMenuItem addPlayerItem = new JMenuItem("Add Player", KeyEvent.VK_P);
		addPlayerItem.setAccelerator(keyStrokeFactory.getAddPlayerAcceleratorKeyStroke());
		fileMenu.add(addPlayerItem);
		addPlayerItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				//TODO: Make this do something.
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
				engine.quitGame();
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
				gplPopup.displayPopup(topFrame);
			}
		});

		// Add the help menu to the menu bar.
		jMenuBar.add(helpMenu);

	}

	/**
	 * 
	 * If an external object needs to operate directly on the {@link CardTableComponent}, provide them a link to it.
	 *
	 * @return The JComponent that represents the card table.
	 */
	public CardTableComponent getCardTable()
	{
		return cardTable;
	}
	
	/**
	 * 
	 * If an external object needs to operate directly on the {@link PlayerPanel}, provide a link to it.
	 *
	 * @return The PlayerPanel object for this system's player.
	 */
	public PlayerPanel getPlayer()
	{
		return playerPanel;
	}

	/**
	 * Return the {@link MessageBar} object located at the top of the screen.
	 *
	 * @return The {@link MessageBar} object located at the top of the screen.
	 */
	public MessageBar getMessageBar()
	{
		return messageBar;
	}

	/**
	 * 
	 * Add the {@link PlayerPanel} to the top {@link JFrame} and display it.
	 *
	 */
	public void showPlayerPanel()
	{
		topFrame.add(playerPanel, BorderLayout.SOUTH);
		topFrame.pack();
		topFrame.repaint();
	}

	/**
	 * 
	 * Remove the {@link PlayerPanel} from the top {@link JFrame} to hide it.
	 *
	 */
	public void hidePlayerPanel()
	{
		topFrame.remove(playerPanel);
		topFrame.pack();
		topFrame.repaint();
	}
}