package gjset.gui;

import gjset.GameConstants;
import gjset.client.ClientCommunicator;
import gjset.client.ClientGUIController;
import gjset.client.ConcreteClientCommunicator;
import gjset.client.GameInitiationHandler;
import gjset.client.GameInitiator;
import gjset.client.gui.PlayGamePage;
import gjset.gui.framework.Button;
import gjset.gui.framework.TextField;
import gjset.tools.MessageHandler;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.dom4j.Element;

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
 * This class starts the gaming process.  We connect to the server, get some
 * player information, then display the current lobby data.
 */
@SuppressWarnings("serial")
public class LobbyPage extends DialogPage implements MessageHandler
{

	private MainFrame mainFrame;
	private ClientCommunicator client;
	private GameInitiator initiator;
	private Button startButton;
	private List<TextField> textFields;
	
	private static final int INSET = 5;

	/**
	 * Build the lobby with the indicated information.
	 *
	 * @param name
	 * @param string
	 * @param port
	 * @param mainFrame
	 */
	public LobbyPage(String username, String hostname, int port, MainFrame mainFrame)
	{
		super();
		
		this.mainFrame = mainFrame;
		
		title.setText("Game Lobby");
		
		createTextFields();
		createButtons();
		
		createGameElements(username, hostname, port);
	}

	/**
	 * View incoming game updates to determine whether to display
	 *
	 * @param root
	 * @see gjset.tools.MessageHandler#handleMessage(org.dom4j.Element)
	 */
	@SuppressWarnings("unchecked")
	public void handleMessage(Element root)
	{
		if(root.element("commandresponse") != null)
		{
			if(root.element("commandresponse").element("newplayer") != null)
			{
				// When we get new player information, confirm that we're #1 to display the start button.
				String idText = root.element("commandresponse").element("newplayer").element("player").attributeValue("id", "0");
				startButton.setVisible(idText.equals("1"));
			}
		}
		else if(root.element("gameupdate") != null)
		{
			// Update all of the text fields with user names.
			List<Element> playerElements = root.element("gameupdate").element("players").elements("player");
			Iterator<Element> iterator = playerElements.iterator();
			while(iterator.hasNext())
			{
				Element playerElement = iterator.next();
				String idText = playerElement.attributeValue("id", "0");
				Integer id = Integer.parseInt(idText);
				
				// Update the text field.
				TextField field = textFields.get(id - 1);
				String username = playerElement.element("name").getText();
				field.setText(username);
			}
			
			int numPlayers = playerElements.size();
			for(int i = numPlayers; i < GameConstants.MAX_PLAYERS; i++)
			{
				textFields.get(i).setText("");
			}
		}
	}

	public void destroy()
	{
		client.removeMessageHandler(this);
		
		mainFrame = null;
		client = null;
		textFields = null;
	}

	/**
	 * Create the buttons that will be placed on the screen.
	 *
	 */
	private void createButtons()
	{
		Rectangle usableArea = border.getInnerArea();

		int buttonY = 170;
		int horizInset = 50;
		
		Action goBackAction = new AbstractAction("Return")
		{
			public void actionPerformed(ActionEvent e)
			{
				// Tear down the client.
				initiator.cancelInitiation();
				
				// Go back to the launch page
				LaunchPage page = new LaunchPage(mainFrame);
				mainFrame.loadPage(page);
			}
		};
		Button backButton = createButton(goBackAction, horizInset, buttonY);
		
		
		Action startAction = new AbstractAction("Start Game")
		{
			public void actionPerformed(ActionEvent e)
			{
				initiator.indicateReadyToStart();
			}
		};
		
		startButton = createButton(startAction, usableArea.x + usableArea.width - horizInset - backButton.getWidth() + 10, buttonY);
		startButton.setVisible(false);
	}

	/**
	 * Create a button on this page with the indicated values.
	 *
	 * @param action
	 * @param x
	 * @param y
	 */
	private Button createButton(Action action, int x, int y)
	{
		Button button = new Button(action, lnf.getDialogButtonStyle());
		
		button.setFont(lnf.getDialogButtonFont());
		button.setForeground(lnf.getDialogButtonTextColor());
		
		button.setSize(120, 22);
		button.setLocation(x, y);
		add(button);
		
		return button;
	}

	/**
	 * Create all of the text fields that will contain the players.
	 *
	 */
	private void createTextFields()
	{
		textFields = new Vector<TextField>();
		Rectangle usableArea = border.getInnerArea();
		
		for(int i = 0; i < GameConstants.MAX_PLAYERS; i++)
		{
			// Create the text field
			TextField field = new TextField(lnf.getDialogTextFieldStyle());
			
			// Set some features of this text field.
			field.setEditable(false);
			field.setFont(lnf.getDialogInputFont());
			field.setForeground(lnf.getDialogInputTextColor());
			
			// Set the size of the field.
			int fieldWidth = (int) ((usableArea.getWidth() - 2 * INSET) / 2 - INSET / 2);
			field.setSize(fieldWidth, 28);
			
			int x = usableArea.x + INSET;
			if( i % 2 == 1 )
			{
				x = (usableArea.x + usableArea.width) - INSET - field.getWidth();
			}
			
			int y = usableArea.y + (i / 2) * (field.getHeight() + 2) + INSET;
			
			// locate the panel.
			field.setLocation(x, y);
			
			add(field);
			textFields.add(field);
		}
	}

	/**
	 * This method connects to the server using th indicated hostname and port, then kicks off the game initiator.
	 *
	 */
	private void createGameElements(String username, String hostname, int port)
	{
		client = new ConcreteClientCommunicator(hostname, port);
		
		// Add ourselves so that we can show which players are in the lobby.
		client.addMessageHandler(this);
		
		initiator = new GameInitiator(client, username, new GameInitiationHandler()
		{
			public void onGameInitiated(ClientGUIController controller)
			{
				// Switch to the play game page.
				PlayGamePage page = new PlayGamePage(controller, mainFrame);
				mainFrame.loadPage(page);
			}
		});
	}

}
