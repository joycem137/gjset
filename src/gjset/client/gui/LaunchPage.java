package gjset.client.gui;

import gjset.gui.Border;
import gjset.gui.Page;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

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
 *
 */
public class LaunchPage extends Page
{
	private Color	realBackgroundColor;
	private Border	border;

	/**
	 * This creates a LaunchPage with a link back to the parent {@link MainFrame} that created
	 * it.
	 *
	 * @param mainFrame - The parent frame of this object.
	 */
	public LaunchPage(Rectangle frame)
	{
		super();
		
		setBackground(new Color(1f, 1f, 1f, 0f));
		
		setBasicInformation(frame);
		
		realBackgroundColor = new Color(227, 209, 156);
		
		createBorder();
	}

	/**
	 * Create the border around this window.
	 *
	 */
	private void createBorder()
	{
		border = new Border("window");
		border.setSize(getWidth(), getHeight());
		add(border);
	}

	/**
	 * Sets the basic position and size of the page.
	 *
	 * @param frame
	 */
	private void setBasicInformation(Rectangle frame)
	{
		//Set page information
		int pageWidth = 531;
		int pageHeight = 325;
		
		setSize(pageWidth, pageHeight);
		
		//Center horizontally in the frame
		int pageX = frame.width / 2 - pageWidth / 2 + frame.x;
		int pageY = frame.height / 2 - pageHeight / 2;
		
		setLocation(pageX, pageY);
	}
	
	public void paintComponent(Graphics g)
	{
		int borderWidth = border.getBorderWidth();
		g.setColor(realBackgroundColor);
		g.fillRect(borderWidth, borderWidth, getWidth() - 2 * borderWidth, getHeight() - 2 * borderWidth);
	}
}
