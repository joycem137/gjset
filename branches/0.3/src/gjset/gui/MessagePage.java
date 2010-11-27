package gjset.gui;

import gjset.gui.framework.Button;
import gjset.gui.framework.Page;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextArea;


/**
 * This page displays a simple message.
 */
@SuppressWarnings("serial")
public class MessagePage extends DialogPage
{
	private MainFrame mainFrame;
	private JTextArea messageComponent;
	private Button continueButton;
	private Page destinationPage;

	/**
	 * Construct this message page with a currently blank message and a continue button.
	 *
	 * @param mainFrame
	 * @param string 
	 */
	public MessagePage(MainFrame mainFrame, String titleString)
	{
		super();
		
		this.mainFrame = mainFrame;
		
		title.setText(titleString);
		
		createButton();
		createLabel();
	}

	/**
	 * Set the message on the screen.
	 *
	 * @param messageText
	 */
	public void setMessage(String messageText)
	{
		messageComponent.setText(messageText);
	}

	/**
	 * Set the destination page for the continue button.
	 *
	 * @param launchPage
	 */
	public void setDestination(Page destinationPage)
	{
		this.destinationPage = destinationPage;
	}

	/**
	 * Create the message label itself.
	 *
	 */
	private void createLabel()
	{
		messageComponent = new JTextArea();
		
		messageComponent.setEditable(false);  
		messageComponent.setCursor(null);  
		messageComponent.setOpaque(false);  
		messageComponent.setFocusable(false);
		
		//messageComponent.setHorizontalAlignment(SwingConstants.CENTER);
		
		messageComponent.setWrapStyleWord(true);  
		messageComponent.setLineWrap(true);  
		
		Rectangle borderArea = border.getInnerArea();
		int INSET = 5;
		messageComponent.setSize(borderArea.width - 2 * INSET, borderArea.height - 50);
		messageComponent.setLocation(borderArea.x + INSET, borderArea.y);
		
		messageComponent.setFont(lnf.getDialogMessageFont());
		
		add(messageComponent);
	}

	/**
	 * Create the button to continue to the next page.
	 *
	 */
	private void createButton()
	{
		Action continueAction = new AbstractAction("Continue")
		{
			public void actionPerformed(ActionEvent e)
			{
				// Switch to the indicated page.
				if(destinationPage != null)
				{
					mainFrame.loadPage(destinationPage);
				}
			}
		};
		
		continueButton = new Button(continueAction, lnf.getDialogButtonStyle());
		
		continueButton.setFont(lnf.getDialogButtonFont());
		continueButton.setForeground(lnf.getDialogButtonTextColor());
		
		continueButton.setSize(120, 22);
		continueButton.setLocation(getWidth() / 2 - continueButton.getWidth() / 2, 170);
		
		add(continueButton);
	}

}
