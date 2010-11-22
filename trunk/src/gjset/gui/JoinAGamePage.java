package gjset.gui;

import gjset.GameConstants;
import gjset.gui.framework.Button;
import gjset.gui.framework.TextField;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;

/**
 * Provides the user with a list of options with which to join a game.
 */
@SuppressWarnings("serial")
public class JoinAGamePage extends DialogPage
{
	private MainFrame mainFrame;
	private TextField nameField;
	private TextField ipField;
	private TextField portField;

	/**
	 * Construct the page.
	 *
	 * @param mainFrame
	 */
	public JoinAGamePage(MainFrame mainFrame)
	{
		super();
		
		this.mainFrame = mainFrame;
		
		title.setText("Join A Game");
		
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
		
		Action goBackAction = new AbstractAction("Return")
		{
			public void actionPerformed(ActionEvent e)
			{
				LaunchPage page = new LaunchPage(mainFrame);
				mainFrame.loadPage(page);
			}
		};
		Button backButton = createButton(goBackAction, horizInset, buttonY);
		
		Action hostAction = new AbstractAction("Join Game")
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean validFields = validateFields();
				if(validFields)
				{
					String name = nameField.getText();
					String ip = ipField.getText();
					int port = Integer.parseInt(portField.getText());
					
					LobbyPage page = new LobbyPage(name, ip, port, mainFrame);
					mainFrame.loadPage(page);
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
		
		nameField = addInputFieldAndLabel("Player Name:", new Rectangle(usableArea.x, 50, usableArea.width, 40));
		ipField = addInputFieldAndLabel("Server Address:", new Rectangle(usableArea.x, 90, usableArea.width, 40));
		portField = addInputFieldAndLabel("Server Port:", new Rectangle(usableArea.x, 130, usableArea.width, 40));
		
		portField.setText("" + GameConstants.GAME_PORT);
		
		int randomNumber = (int) Math.ceil(Math.random() * 1000);
		
		nameField.setText("Anonymous " + randomNumber);
	}

	/**
	 * Added a button and an associated label.
	 *
	 * @param action
	 * @param string
	 */
	private TextField addInputFieldAndLabel(String labelText, Rectangle frame)
	{
		// Create the label for the field.
		JLabel label = new JLabel(labelText);
		label.setFont(lnf.getDialogFont());

		label.setLocation(frame.x + 15, frame.y - 7);
		label.setSize(frame.width - label.getX(), 40);
		
		add(label);
		
		// Create the field itself.
		TextField field = new TextField(lnf.getDialogTextFieldStyle());
		
		field.setFont(lnf.getDialogInputFont());
		field.setForeground(lnf.getDialogInputTextColor());

		field.setLocation(200, frame.y);
		field.setSize(210, 28);
		
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
		// Fail on an empty name field.
		String nameString = nameField.getText();
		if(nameString == null) return false;
		
		String portString = portField.getText();
		
		// Fail on empty hostname
		String ipString = ipField.getText();
		if(ipString == null) return false;
		
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