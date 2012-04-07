package gjset.client.gui.pages;

import gjset.client.ClientController;
import gjset.gui.DialogPage;
import gjset.gui.framework.Button;
import gjset.gui.framework.PasswordField;
import gjset.gui.framework.TextField;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
 * Asks for the username and password of the user to enter into the server.
 */
@SuppressWarnings("serial")
public class ServerConnectPage extends DialogPage
{
	private TextField nameField;
	private PasswordField passwordField;

	/**
	 * Construct the page.
	 *
	 * @param controller
	 */
	public ServerConnectPage(ClientController controller)
	{
		super(controller);
		
		title.setText("Enter username and Password");
		
		createInputFields();
		createButtons();
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
		
		Action goBackAction = new AbstractAction("Back")
		{
			public void actionPerformed(ActionEvent e)
			{
				controller.backAPage();
			}
		};
		Button backButton = createButton(goBackAction, horizInset, buttonY);
		
		Action hostAction = new AbstractAction("Log In")
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean validFields = validateFields();
				if(validFields)
				{
					String name = nameField.getText();
					char[] passwordArray = passwordField.getPassword();
					String password = new String(passwordArray);
					
					controller.attemptLogin(name, password);
				}
			}
		};
		
		createButton(hostAction, usableArea.x + usableArea.width - horizInset - backButton.getWidth() + 10, buttonY);
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
	 * Create the input fields on the screen.
	 *
	 */
	private void createInputFields()
	{
		Rectangle usableArea = border.getInnerArea();
		
		nameField = new TextField(lnf.getDialogTextFieldStyle());
		setupField(nameField, "Username:", new Rectangle(usableArea.x, 50, usableArea.width, 40));
		
		passwordField = new PasswordField(lnf.getDialogTextFieldStyle());
		setupField(passwordField, "Password:", new Rectangle(usableArea.x, 90, usableArea.width, 40));
		passwordField.setEchoChar('*');
		
	}

	/**
	 * Setup the indicated text field with the provided label in front of it.
	 *
	 * @param action
	 * @param string
	 */
	private void setupField(JTextField field, String labelText, Rectangle frame)
	{
		// Create the label for the field.
		JLabel label = new JLabel(labelText);
		label.setFont(lnf.getDialogFont());

		label.setLocation(frame.x + 15, frame.y - 7);
		label.setSize(frame.width - label.getX(), 40);
		
		add(label);
		
		field.setFont(lnf.getDialogInputFont());
		field.setForeground(lnf.getDialogInputTextColor());

		field.setLocation(200, frame.y);
		field.setSize(210, 28);
		
		add(field);
	}
	
	/**
	 * 
	 * Validates the input fields.  Returns true if the fields contain valid data.  False if not.
	 *
	 * @return
	 */
	private boolean validateFields()
	{
		// Fail on an empty name field.
		String nameString = nameField.getText();
		if(nameString == null || nameString == "") return false;
		
		String passwordString = new String(passwordField.getPassword());
		if(passwordString == null || passwordString == "") return false;	
		
		return true;
	}
	
}