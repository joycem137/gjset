package gjset.gui;


import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008-2009 Joyce Murton and Andrea Kilpatrick
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

/**
 * This class contains all of the UI elements for creating the launcher for the system.
 */
public class Launcher extends Page
{
	private JLabel	title;

	/**
	 * This creates a LauncherPanel with a link back to the parent {@link MainFrame} that created
	 * it.
	 *
	 * @param mainFrame - The parent frame of this object.
	 */
	public Launcher()
	{
		super();
		
		setLayout(new GridLayout(4, 1));
		setBackground(Color.white);
		
		//Get the parent container
		Container parent = getParent();
		
		//Set page information
		pageWidth = 531;
		pageHeight = 325;
		pageX = 246;
		pageY = 221;
		
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		createUIElements();
	}

	/**
	 * Creates all of the UI elements for the launcher.
	 */
	private void createUIElements()
	{
		title = new JLabel("New Game", SwingConstants.CENTER);
		title.setFont(new Font("Tahoma Bold", Font.BOLD, 40));
		title.setSize(531, 60);
		title.setLocation(0, 0);
		add(title);
		
		JButton button = new JButton(new AbstractAction("Play Against the Computer")
			{

				public void actionPerformed(ActionEvent e)
				{
					// TODO Auto-generated method stub
					
				}
			});

		button.setFont(new Font("Tahoma Bold", Font.BOLD, 20));
		button.setSize(395, 37);
		button.setLocation(68, 88);
		add(button);
		
		button = new JButton(new AbstractAction("Host a Game")
			{

				public void actionPerformed(ActionEvent e)
				{
					// TODO Auto-generated method stub
					
				}
			});

		button.setFont(new Font("Tahoma Bold", Font.BOLD, 20));
		button.setSize(395, 37);
		button.setLocation(68, 151);
		add(button);
		
		button = new JButton(new AbstractAction("Join a Game")
			{

				public void actionPerformed(ActionEvent e)
				{
					// TODO Auto-generated method stub
					
				}
			});

		button.setFont(new Font("Tahoma Bold", Font.BOLD, 20));
		button.setSize(395, 37);
		button.setLocation(68, 214);
		add(button);
	}

}
