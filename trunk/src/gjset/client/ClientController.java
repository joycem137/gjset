package gjset.client;

import gjset.client.gui.MainFrame;
import gjset.client.gui.pages.LaunchPage;
import gjset.client.gui.pages.ServerConnectPage;

import javax.swing.SwingUtilities;

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
 * This is the primary controller for the client.  It manages messages
 * across all of the different UI pages and handles changing pages as appropriate.
 */
public class ClientController
{
	private MainFrame mainFrame;
	private LaunchPage launchPage;
	private ServerConnectPage userLoginPage;
	
	private ConcreteClientCommunicator communicator;
	private ConnectionInitializer connectionInitializer;
	
	public ClientController() {	
		mainFrame = new MainFrame();
		
		createPages();
	}

	/**
	 * Create all of our pages
	 */
	private void createPages()
	{
		launchPage = new LaunchPage(this);
		userLoginPage = new ServerConnectPage(this);
	}

	/**
	 * Start running the application.
	 */
	public void start()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				mainFrame.loadPage(launchPage);
			}
		});
	}

	/**
	 * Launch a multiplayer game.
	 */
	public void launchMultiplayerGame()
	{
		communicator = new ConcreteClientCommunicator();
		connectionInitializer = new ConnectionInitializer(communicator);
		connectionInitializer.connectToServer();
		
		// Switch to the join a game page.
		mainFrame.loadPage(userLoginPage);
	}

	/**
	 * Attempt to log in using the indicated username and password.
	 *
	 * @param name
	 * @param password
	 */
	public void attemptLogin(String name, String password)
	{
		connectionInitializer.authenticateUser(name, password);
		
		// TODO: Go to a connecting... page.
	}

	/**
	 * Create a new user account
	 *
	 */
	public void createAccount() {
		// TODO: Go to a page to select a username and password.		
	}

	/**
	 * Go back one page.
	 *
	 */
	public void backAPage()
	{
		mainFrame.backAPage();
	}
	
}
