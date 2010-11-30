package gjset.gui;

import gjset.gui.framework.Border;
import gjset.gui.framework.FancyLabel;
import gjset.gui.framework.Page;
import gjset.gui.framework.SimpleLookAndFeel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.SwingConstants;

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
 * Create a Generic blank dialog page.  Used so that subclasses don't have to repeat this work.
 */
@SuppressWarnings("serial")
public abstract class DialogPage extends Page
{
	protected FancyLabel title;
	protected Color realBackgroundColor;
	protected Border border;
	protected SimpleLookAndFeel lnf;

	/**
	 * This creates a LaunchPage with a link back to the parent {@link MainFrame} that created
	 * it.
	 *
	 */
	public DialogPage()
	{
		super();
		
		lnf = SimpleLookAndFeel.getLookAndFeel();
		
		configurePage(MainFrame.PLAYING_FIELD_AREA);
		
		realBackgroundColor = lnf.getDialogBackgroundColor();
		
		createBorder();
		createTitle();
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
	 * Sets the basic position and size of the page.
	 *
	 * @param frame A Rectangle representing the frame this page sits in.
	 */
	protected void configurePage(Rectangle frame)
	{	
		//Set page information
		int pageWidth = 435;
		int pageHeight = 220;
		
		setSize(pageWidth, pageHeight);
		
		//Center horizontally in the frame
		int pageX = frame.width / 2 - pageWidth / 2 + frame.x;
		int pageY = frame.height / 2 - pageHeight / 2;
		
		setLocation(pageX, pageY);
	}

	/**
	 * Add a title to the border.
	 *
	 */
	private void createTitle()
	{
		title = new FancyLabel("Dialog", SwingConstants.CENTER);
		
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
