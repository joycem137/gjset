package gjset.gui;


import gjset.client.gui.PlayGamePage;
import gjset.gui.framework.Border;
import gjset.gui.framework.Button;
import gjset.gui.framework.FancyLabel;
import gjset.gui.framework.Page;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
public class LaunchPage extends Page
{
	private Color	realBackgroundColor;
	private Border	border;
	private SimpleLookAndFeel	lnf;
	private MainFrame	mainframe;

	/**
	 * This creates a LaunchPage with a link back to the parent {@link MainFrame} that created
	 * it.
	 *
	 * @param mainFrame - The parent frame of this object.
	 */
	public LaunchPage(Rectangle frame, MainFrame mainframe)
	{
		super();
		
		this.mainframe = mainframe;
		
		lnf = SimpleLookAndFeel.getLookAndFeel();
		
		setBasicInformation(frame);
		
		realBackgroundColor = lnf.getDialogBackgroundColor();
		
		createBorder();
		createTitle();
		createButtons();
	}

	/**
	 * Adds all of the buttons to the screen.
	 *
	 */
	private void createButtons()
	{	
		Rectangle usableArea = border.getInnerArea();
		
		addButtonAndLabel(new AbstractAction("Play Against the Computer?")
		{
			public void actionPerformed(ActionEvent e)
			{
				PlayGamePage page = new PlayGamePage();
				mainframe.loadPage(page);
			}
		}, new Rectangle(usableArea.x, 50, usableArea.width, 40));
		
		addButtonAndLabel(new AbstractAction("Join a Game?")
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO: Load Join a game page.
			}
		}, new Rectangle(usableArea.x, 100, usableArea.width, 40));
		
		addButtonAndLabel(new AbstractAction("Host a Game?")
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO: Load Host a game page.
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
		
		button.setSize(40, 22);
		button.setLocation(40, frame.y);
		add(button);
		
		JLabel label = new JLabel((String)action.getValue(Action.NAME));
		label.setFont(lnf.getDialogFont());

		label.setLocation(115, frame.y - 10);
		label.setSize(frame.width - label.getX(), 40);
		
		add(label);
	}

	/**
	 * Add a title to the border.
	 *
	 */
	private void createTitle()
	{
		FancyLabel title;
		title = new FancyLabel("New Game", SwingConstants.CENTER);
		
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

	/**
	 * Sets the basic position and size of the page.
	 *
	 * @param frame A Rectangle representing the frame this page sits in.
	 */
	private void setBasicInformation(Rectangle frame)
	{
		// Turn off the automatic layout system.
		setLayout(null);
		setOpaque(false);
		
		//Set page information
		int pageWidth = 435;
		int pageHeight = 220;
		
		setSize(pageWidth, pageHeight);
		
		//Center horizontally in the frame
		int pageX = frame.width / 2 - pageWidth / 2 + frame.x;
		int pageY = frame.height / 2 - pageHeight / 2;
		
		setLocation(pageX, pageY);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//Fill in the background.
		Rectangle backgroundArea = border.getInnerArea();
		g.setColor(realBackgroundColor);
		((Graphics2D) g).fill(backgroundArea);
	}
}
