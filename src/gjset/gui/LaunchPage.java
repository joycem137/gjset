package gjset.gui;


import gjset.GameConstants;
import gjset.client.ClientGUIController;
import gjset.client.ConcreteClientCommunicator;
import gjset.client.ConcreteClientGUIController;
import gjset.client.gui.PlayGamePage;
import gjset.gui.framework.Button;
import gjset.server.GameServer;
import gjset.server.ServerGameController;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards!
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  The Set Game, card design, and other elements of Set of the Set Game are
 *  registered trademarks of Set Enterprises. 
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
 * This page provides launching points for the other applications that we might run.
 */
@SuppressWarnings("serial")
public class LaunchPage extends DialogPage
{
	private MainFrame	mainFrame;

	/**
	 * This creates a LaunchPage with a link back to the parent {@link MainFrame} that created
	 * it.
	 *
	 * @param mainFrame - The parent frame of this object.
	 */
	public LaunchPage(MainFrame mainframe)
	{
		super();
		
		this.mainFrame = mainframe;
		
		title.setText("New Game");
		
		createButtons();
	}

	/**
	 * Adds all of the buttons to the screen.
	 *
	 */
	private void createButtons()
	{	
		Rectangle usableArea = border.getInnerArea();
		
		addButtonAndLabel(new AbstractAction("Play Alone")
		{
			public void actionPerformed(ActionEvent e)
			{
				// First create the server.
				GameServer server = new GameServer(GameConstants.GAME_PORT);
				ServerGameController serverController = new ServerGameController(server);
				server.listenForClients();
				
				// Tell the game to start right away.
				serverController.startNewGame();
				
				// Then create the client.
				ConcreteClientCommunicator client = new ConcreteClientCommunicator("127.0.0.1", GameConstants.GAME_PORT);
				ClientGUIController controller = new ConcreteClientGUIController(client);
				
				PlayGamePage page = new PlayGamePage(controller, mainFrame);
				
				// Load the actual page
				mainFrame.loadPage(page);
				
				// And now connect the client!
				client.connectToServer();
			}
		}, new Rectangle(usableArea.x, 50, usableArea.width, 40));
		
		addButtonAndLabel(new AbstractAction("Join a Network Game")
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO: Load Join a game page.
			}
		}, new Rectangle(usableArea.x, 100, usableArea.width, 40));
		
		addButtonAndLabel(new AbstractAction("Host a Network Game")
		{
			public void actionPerformed(ActionEvent e)
			{
				// Switch to the host a game page.
				HostAGamePage page = new HostAGamePage(mainFrame);
				mainFrame.loadPage(page);
			}
		}, new Rectangle(usableArea.x, 150, usableArea.width, 40));
	}

	/**
	 * Added a button and an associated label.
	 *
	 * @param action
	 * @param string
	 */
	private void addButtonAndLabel(Action action, Rectangle frame)
	{
		// You may now proceed to add all of the buttons and labels.
		Button button = new Button(action, lnf.getDialogButtonStyle());
		
		button.setTextVisible(false);
		button.setSize(40, 22);
		button.setLocation(40, frame.y);
		add(button);
		
		JLabel label = new JLabel((String)action.getValue(Action.NAME));
		label.setFont(lnf.getDialogFont());

		label.setLocation(115, frame.y - 10);
		label.setSize(frame.width - label.getX(), 40);
		
		add(label);
	}
}
