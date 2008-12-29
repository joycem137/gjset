package gjset.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton
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

public class GPLPopup
{
	private final String gplTextString;
	private JDialog	gplDialog;
	
	public GPLPopup()
	{
		//Load the GPL text from memory.
		String gplFilename = "resources/COPYING";
		FileReader reader = null;
		try
		{
			reader = new FileReader(gplFilename);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		char gplTextChars[] = new char[36000];
		int charsRead = 0;
		try
		{
			charsRead = reader.read(gplTextChars);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Read " + charsRead + " bytes from GPL.");
		
		gplTextString = new String(gplTextChars);
	}

	public void displayPopup(JFrame parentFrame)
	{
		//Create the basic dialog
		gplDialog = new JDialog(parentFrame, "GNU Public License", true);
		gplDialog.setSize(new Dimension(600, 600));
		gplDialog.setLayout(new BorderLayout());
		
		//Create the text area for the GPL
		JTextArea textPane = new JTextArea();
		textPane.setText(gplTextString);
		textPane.setEditable(false);
		
		//Create a scroll pane to allow the text to be scrolled.
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Ensure that we're scrolled to the top of the pane.
		textPane.setCaretPosition(0);
		
		//Add the text area to the dialog
		gplDialog.add(scrollPane, BorderLayout.CENTER);
		
		//Create a button for the dialog
		JButton button = new JButton("OK");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				gplDialog.setVisible(false);
			}
		});
		gplDialog.add(button, BorderLayout.SOUTH);
		
		//Show the dialog.
		gplDialog.setVisible(true);
	}
}
