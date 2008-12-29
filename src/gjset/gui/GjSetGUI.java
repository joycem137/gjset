package gjset.gui;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton
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

import gjset.engine.GameController;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class GjSetGUI
{
	private JFrame			topFrame;
	private CardTable		cardTable;
	private GameController	gameController;
	private MessageBar		messageBar;
	private PlayerPanel		playerPanel;
	private KeyStrokeFactory keyStrokeFactory;
	private GPLPopup	gplPopup;
	
	public GjSetGUI(GameController gameController)
	{
		this.gameController = gameController;
		gameController.linkGUI(this);
		
		//Determine which keystroke factory to grab.
		if(System.getProperty("os.name").contains("Mac OS X"))
		{
			keyStrokeFactory = new MacKeyStrokeFactory();
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createGUI();
			}
		});
	}

	private void createGUI()
	{
		topFrame = new JFrame("gJSet");
		topFrame.getContentPane().setLayout(new BorderLayout());
		topFrame.setSize(1024, 768);
		topFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createMenu();
		createCardTable();
		createPlayerPanel();
		createMessageBar();
		
		topFrame.pack();
		topFrame.setVisible(true);
	}

	private void createMessageBar()
	{
		messageBar = new MessageBar();
		topFrame.add(messageBar, BorderLayout.NORTH);
	}

	private void createCardTable()
	{
		cardTable = new CardTable(gameController);
		topFrame.add(cardTable, BorderLayout.CENTER);
	}

	private void createPlayerPanel()
	{
		playerPanel = new PlayerPanel(cardTable, gameController);
	}

	private void createMenu()
	{
		JMenuBar jMenuBar = new JMenuBar();
		topFrame.setJMenuBar(jMenuBar);

		//Build the file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		//Build the new game option
		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		newGameItem.setAccelerator(keyStrokeFactory.getNewGameAcceleratorKeyStroke());
		fileMenu.add(newGameItem);
		newGameItem.addActionListener(new ActionListener()
		{
			public void actionPerformed( ActionEvent evt )
			{
				gameController.newGame();
			}
		});

		//Build the exit item.
		JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
		exitItem.setAccelerator(keyStrokeFactory.getExitGameAcceleratorKeyStroke());
		fileMenu.add(exitItem);

		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed( ActionEvent evt )
			{
				System.exit(0);
			}
		});

		//Add the file menu to the menu bar.
		jMenuBar.add(fileMenu);
		
		//Create the help menu for the GNU stuff.
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		//Create the "GPL Text" item.
		JMenuItem gplItem = new JMenuItem("About GPL", KeyEvent.VK_G);
		helpMenu.add(gplItem);
		
		//Create the gplPopup
		gplPopup = new GPLPopup();
		
		//Add the GPL text to the gplItem
		gplItem.addActionListener(new ActionListener()
		{
			public void actionPerformed( ActionEvent evt )
			{
				gplPopup.displayPopup(topFrame);
			}
		});
		
		//Add the help menu to the menu bar.
		jMenuBar.add(helpMenu);
		
	}

	public CardTable getCardTable()
	{
		return cardTable;
	}

	public MessageBar getMessageBar()
	{
		return messageBar;
	}

	public void showPlayerPanel()
	{
		topFrame.add(playerPanel, BorderLayout.SOUTH);
		topFrame.pack();
		topFrame.repaint();
	}

	public void hidePlayerPanel()
	{
		topFrame.remove(playerPanel);
		topFrame.pack();
		topFrame.repaint();
	}
}