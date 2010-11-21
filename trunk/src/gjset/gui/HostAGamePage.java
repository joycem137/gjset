package gjset.gui;

import gjset.GameConstants;
import gjset.gui.framework.Button;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;


/**
 * This page provides configuration settings for hosting a game.
 */
@SuppressWarnings("serial")
public class HostAGamePage extends DialogPage
{

	private MainFrame mainFrame;
	private JTextField nameField;
	private JTextField portField;

	/**
	 * Construct the page.
	 *
	 * @param mainFrame
	 */
	public HostAGamePage(MainFrame mainFrame)
	{
		super();
		
		this.mainFrame = mainFrame;
		
		title.setText("Host A Game");
		
		createInputFields();
		createButtons();
	}

	/**
	 * Create the buttons that will be placed on the screen.
	 *
	 */
	private void createButtons()
	{
		int buttonY = 160;
		
		Action goBackAction = new AbstractAction("Return")
		{
			public void actionPerformed(ActionEvent e)
			{
				LaunchPage page = new LaunchPage(mainFrame);
				mainFrame.loadPage(page);
			}
		};
		createButton(goBackAction, 50, buttonY);
		
		
		Action hostAction = new AbstractAction("Host Game")
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean validFields = validateFields();
				if(validFields)
				{
					String name = nameField.getText();
					int port = Integer.parseInt(portField.getText());
				}
			}
		};
		
		createButton(hostAction, 240, buttonY);
	}

	/**
	 * Create a button on this page with the indicated values.
	 *
	 * @param action
	 * @param x
	 * @param y
	 */
	private void createButton(Action action, int x, int y)
	{
		Button button = new Button(action, lnf.getDialogButtonStyle());
		
		button.setFont(lnf.getDialogButtonFont());
		button.setForeground(lnf.getDialogButtonTextColor());
		
		button.setSize(120, 22);
		button.setLocation(x, y);
		add(button);
	}

	/**
	 * Create the input fields on the screen.
	 *
	 */
	private void createInputFields()
	{
		Rectangle usableArea = border.getInnerArea();
		
		nameField = addInputFieldAndLabel("Player Name:", new Rectangle(usableArea.x, 50, usableArea.width, 40));
		portField = addInputFieldAndLabel("Server Port:", new Rectangle(usableArea.x, 100, usableArea.width, 40));
		
		portField.setText("" + GameConstants.GAME_PORT);
		nameField.setText("Player");
	}

	/**
	 * Added a button and an associated label.
	 *
	 * @param action
	 * @param string
	 */
	private JTextField addInputFieldAndLabel(String labelText, Rectangle frame)
	{
		// Create the label for the field.
		JLabel label = new JLabel(labelText);
		label.setFont(lnf.getDialogFont());

		label.setLocation(frame.x + 15, frame.y - 5);
		label.setSize(frame.width - label.getX(), 40);
		
		add(label);
		
		// Create the field itself.
		JTextField field = new JTextField();
		
		field.setLayout(null);
		field.setFont(lnf.getDialogInputFont());

		field.setLocation(180, frame.y);
		field.setSize(230, 30);
		
		add(field);
		
		return field;
	}
	
	/**
	 * 
	 * Validates the input fields.  Returns true if the fields contain valid data.  False if not.
	 *
	 * @return
	 */
	private boolean validateFields()
	{
		String nameString = nameField.getText();
		
		// Fail on an empty name field.
		if(nameString == null) return false;
		
		String portString = portField.getText();
		
		// Fail on empty port field.
		if(portString == null) return false;
		
		int port = 0;
		try
		{
			port = Integer.parseInt(portString);
		}
		catch(NumberFormatException nfe)
		{
			// Fail on a bad parse.
			return false;
		}
		
		// Fail on an invalid port;
		if(port < 0) return false;
		
		return true;
	}
	
}
