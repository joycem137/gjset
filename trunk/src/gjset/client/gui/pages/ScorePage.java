package gjset.client.gui.pages;

import gjset.client.ClientGUIController;
import gjset.client.gui.MainFrame;
import gjset.data.PlayerData;
import gjset.gui.framework.Border;
import gjset.gui.framework.Button;
import gjset.gui.framework.FancyLabel;
import gjset.gui.framework.Page;
import gjset.gui.framework.SimpleLookAndFeel;
import gjset.gui.framework.TextField;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingConstants;

/**
 * This is the final score page that shows a breakdown of all of the players' scores.
 */
@SuppressWarnings("serial")
public class ScorePage extends Page
{
	private MainFrame mainFrame;
	private ClientGUIController controller;
	private SimpleLookAndFeel lnf;
	private Color realBackgroundColor;
	private FancyLabel title;
	private Border border;
	
	/**
	 * Create the score page.
	 *
	 * @param mainFrame
	 * @param controller
	 */
	public ScorePage(MainFrame mainFrame, ClientGUIController controller)
	{
		this.mainFrame = mainFrame;
		this.controller = controller;

		lnf = SimpleLookAndFeel.getLookAndFeel();
		
		createGameStartHandler();
		
		configurePage();
		
		realBackgroundColor = lnf.getDialogBackgroundColor();
		
		createBorder();
		createTitle();
		
		createPlayerResults();
		createButtons();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//Fill in the background.
		Rectangle backgroundArea = border.getInnerArea();
		g.setColor(realBackgroundColor);
		((Graphics2D) g).fill(backgroundArea);
	}

	/**
	 * Create a handler fore the controller to fire upon game start.
	 *
	 */
	private void createGameStartHandler()
	{
		controller.addGameStartTrigger(new Runnable()
		{
			public void run()
			{
				PlayGamePage page = new PlayGamePage(controller, mainFrame);
				mainFrame.loadPage(page);
			}
		});
	}

	/**
	 * Sets the basic position and size of the page.
	 *
	 * @param frame A Rectangle representing the frame this page sits in.
	 */
	private void configurePage()
	{	
		Rectangle frame = MainFrame.PLAYING_FIELD_AREA;
		
		//Set page information
		int pageWidth = 600;
		int pageHeight = 400;
		
		setSize(pageWidth, pageHeight);
		
		//Center horizontally in the frame
		int pageX = frame.width / 2 - pageWidth / 2 + frame.x;
		int pageY = frame.height / 2 - pageHeight / 2;
		
		setLocation(pageX, pageY);
	}

	/**
	 * Create the buttons that will be placed on the screen.
	 *
	 */
	private void createButtons()
	{
		Rectangle usableArea = border.getInnerArea();
		int buttonY = usableArea.y + usableArea.height - 40;
		int horizInset = 50;
		
		Action quitAction = new AbstractAction("Quit")
		{
			public void actionPerformed(ActionEvent e)
			{
				controller.disconnectPlayer();
				controller.destroy();
				
				LaunchPage page = new LaunchPage(mainFrame);
				mainFrame.loadPage(page);
			}
		};
		Button quitButton = createButton(quitAction, horizInset, buttonY);
		
		if(controller.getModel().getLocalPlayer().getId() == 1)
		{
			Action playAgainAction = new AbstractAction("Play again")
			{
				public void actionPerformed(ActionEvent e)
				{
					controller.startNewGame();
					
					PlayGamePage page = new PlayGamePage(controller, mainFrame);
					mainFrame.loadPage(page);
				}
			};
			
			createButton(playAgainAction, usableArea.x + usableArea.width - horizInset - quitButton.getWidth() + 10, buttonY);
		}
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
	 * Go through the list of players and add information about each.
	 *
	 */
	private void createPlayerResults()
	{
		Rectangle usableArea = border.getInnerArea();
		final int INSET = 5;
		final int BUFFER = 4;
		
		// Add the column labels.
		FancyLabel nameLabel = createNewColumnLabel("Name", 250, usableArea.x + INSET);
		FancyLabel pointsLabel = createNewColumnLabel("Points", 80, nameLabel.getX() + nameLabel.getWidth() + BUFFER);
		FancyLabel penaltyLabel = createNewColumnLabel("Penalty", 80, pointsLabel.getX() + pointsLabel.getWidth() + BUFFER);
		FancyLabel scoreLabel = createNewColumnLabel("Total", 80, penaltyLabel.getX() + penaltyLabel.getWidth() + BUFFER);
		
		// Now add the players.
		int topY = nameLabel.getY() + nameLabel.getHeight();
		List<PlayerData> players = controller.getModel().getPlayers();
		for(int i = 0; i < players.size(); i++)
		{
			int yPos = topY + i * (28 + BUFFER);
			PlayerData player = players.get(i);
			
			// Add the name field.
			TextField nameField = createTextField(player.getName(), nameLabel.getWidth());
			nameField.setLocation(nameLabel.getX(), yPos);
			
			// Add the points field
			TextField pointsField = createTextField("" + player.getPoints(), pointsLabel.getWidth());
			pointsField.setLocation(pointsLabel.getX(), yPos);
			
			// Add the penalty field
			TextField penaltyField = createTextField("" + player.getPenalty(), penaltyLabel.getWidth());
			penaltyField.setLocation(penaltyLabel.getX(), yPos);
			
			// Add the score field
			TextField scoreField = createTextField("" + player.getScore(), scoreLabel.getWidth());
			scoreField.setLocation(scoreLabel.getX(), yPos);
		}
	}

	/**
	 * Create a column label with the indicated values.
	 *
	 * @param text
	 * @param width
	 * @param x
	 * @return
	 */
	private FancyLabel createNewColumnLabel(String text, int width, int x)
	{
		Rectangle usableArea = border.getInnerArea();
		final int INSET = 5;
		
		FancyLabel label = new FancyLabel(text);
		
		label.setSize(width, 30);
		label.setLocation(x, usableArea.y + INSET);
		
		label.setFancyEffect(FancyLabel.OUTLINE);
		label.setFont(lnf.getScorePageColumnTitleFont());
		label.setForeground(lnf.getScorePageColumnTitleFG());
		label.setBackground(lnf.getScorePageColumnTitleBG());
				
		add(label);
		return(label);
	}

	/**
	 * Create a text field with the indicated pre-assigned text.
	 *
	 * @param text
	 * @return
	 */
	private TextField createTextField(String text, int width)
	{
		final int TEXTFIELD_HEIGHT = 28;
		
		TextField field = new TextField(lnf.getDialogTextFieldStyle());

		field.setSize(width, TEXTFIELD_HEIGHT);
		
		field.setEditable(false);
		field.setFont(lnf.getDialogInputFont());
		field.setForeground(lnf.getDialogInputTextColor());
		
		field.setText(text);
		
		add(field);
		
		return field;
	}

	/**
	 * Add a title to the border.
	 *
	 */
	private void createTitle()
	{
		title = new FancyLabel("Dialog", SwingConstants.CENTER);
		title.setText("Final Scores");
		
		// Design the label.
		title.setFont(lnf.getDialogTitleFont());
		title.setFancyEffect(FancyLabel.OUTLINE);
		title.setForeground(lnf.getDialogTitleFG());
		title.setBackground(lnf.getDialogTitleBG());
		
		// Center the title in the title area of the border.
		Rectangle titleArea = border.getTitleArea();
		title.setSize(titleArea.width, titleArea.height);
		title.setLocation(titleArea.x, titleArea.y);
		
		// Add the title to the border.
		border.add(title);
	}

	/**
	 * Create the border around this window.
	 *
	 */
	private void createBorder()
	{
		border = new Border(lnf.getBorderStyle(), true);
		border.setSize(getWidth(), getHeight());
		add(border);
	}

}
