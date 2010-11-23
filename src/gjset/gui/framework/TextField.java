package gjset.gui.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

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
 * This class takes your basic text field functionality and changes the painting of it to include 
 * a custom background image.
 */
@SuppressWarnings("serial")
public class TextField extends JTextField
{
	private Image left;
	private Image middle;
	private Image right;

	public TextField(String style)
	{
		super();

		// Get all of our images.
		ResourceManager resourceManager = ResourceManager.getInstance();
		left = resourceManager.getImage(style + "_l.png");
		middle = resourceManager.getImage(style + "_m.png");
		right = resourceManager.getImage(style + "_r.png");
		
		setOpaque(false);
		setDragEnabled(false);
		setBackground(new Color(0, 0, 0, 0));
		setBorder(null);;
	}
	
	public Insets getInsets()
	{
		return new Insets(0, left.getWidth(this) - 5, 0, right.getWidth(this) - 5);
	}
	
	public void paint(Graphics g)
	{	
		// Draw the background.
		
		// Draw the edges first.
		g.drawImage(left, 0, 0, this);
		g.drawImage(right, getWidth() - right.getWidth(this), 0, this);
		
		// Finally, draw the middle.
		Rectangle areaToPaint = new Rectangle(left.getWidth(this), 0, 
				getWidth() - left.getWidth(this) - right.getWidth(this), 
				middle.getHeight(this));
		PaintUtilities.texturePaintHorizontal(this, g, middle, areaToPaint);

		// Draw the rest.
		super.paint(g);
	}

}
