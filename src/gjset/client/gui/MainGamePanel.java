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
import gjset.engine.GameEngine;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is the main class for handling the user interface for a player.  It is responsible for creating any and all
 * components to put on the screen and interpreting incoming user actions.
 * <P>
 * Additionally, this is the main class for the client.
 * 
 * @see JPanel
 */
public class MainGamePanel extends JPanel
{
	//UI Components
	private CardTableComponent	cardTable;
	private MessageBar			messageBar;
	private PlayerPanel			playerPanel;
	
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
	public MainGamePanel(EngineLinkInterface engine)
	{
		//Store a link to the game engine.
		this.engine = engine;
		
		//Set the panel layout and size.
		setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(1024, 768));
		
		//Create the UI Elementss
		createMessageBar();
		createCardTable();
		createPlayerPanel();
	}

	//Create the message bar that goes at the top of the page and displays messages.
	private void createMessageBar()
	{
		messageBar = new MessageBar();
		add(messageBar, BorderLayout.NORTH);
	}

	//Create the card table, the big thing in the center of the screen that shows all the cards.
	private void createCardTable()
	{
		cardTable = new CardTableComponent(engine);
		add(cardTable, BorderLayout.CENTER);
	}

	//Create the player panel to go on the bottom of the screen.
	private void createPlayerPanel()
	{
		playerPanel = new PlayerPanel(cardTable, engine);
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
		add(playerPanel, BorderLayout.SOUTH);
		repaint();
	}

	/**
	 * 
	 * Remove the {@link PlayerPanel} from the top {@link JFrame} to hide it.
	 *
	 */
	public void hidePlayerPanel()
	{
		remove(playerPanel);
		repaint();
	}
}
