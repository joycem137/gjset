package gjset.gui.framework;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;

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
 * Border objects are objects that can be dropped on the screen to display a border over something.
 * 
 * They are used like an ordinary JComponent.  Any objects that should be displayed on top of the
 * border should be added to the border itself.  Otherwise, they can be added below the border.
 */
@SuppressWarnings("serial")
public class Border extends JComponent
{
	private Image cornerUL;
	private Image cornerUR;
	private Image cornerLL;
	private Image cornerLR;
	private Image left;
	private Image right;
	private Image top;
	private Image bottom;
	
	public Border(String style, boolean useTitle)
	{
		setLayout(null);
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		if(useTitle)
		{
			cornerUL = resourceManager.getImage(style + "_title_l.png");
			cornerUR = resourceManager.getImage(style + "_title_r.png");
			top 	= resourceManager.getImage(style + "_title_m.png");
		}
		else
		{	
			cornerUL = resourceManager.getImage(style + "_ul.png");
			cornerUR = resourceManager.getImage(style + "_ur.png");
			top 	= resourceManager.getImage(style + "_um.png");
		}
		
		cornerLL = resourceManager.getImage(style + "_ll.png");
		cornerLR = resourceManager.getImage(style + "_lr.png");
		
		left 	= resourceManager.getImage(style + "_ml.png");
		right 	= resourceManager.getImage(style + "_mr.png");
		bottom 	= resourceManager.getImage(style + "_lm.png");
	}

	public void paintComponent(Graphics g)
	{
		int width = getWidth();
		int height = getHeight();
		
		int topCornerWidth = cornerUL.getWidth(this);
		int topCornerHeight = cornerUL.getHeight(this);
		
		int bottomCornerWidth = cornerLL.getWidth(this);
		int bottomCornerHeight = cornerLL.getHeight(this);
		
		//Draw the sides
		Rectangle area = new Rectangle( topCornerWidth, 0, width - topCornerWidth - bottomCornerWidth, top.getHeight(this));
		PaintUtilities.texturePaintHorizontal(this, g, top, area);
		
		area = new Rectangle( bottomCornerWidth, height - bottom.getHeight(this), 
				width - topCornerWidth - bottomCornerWidth, bottom.getHeight(this));
		PaintUtilities.texturePaintHorizontal(this, g, bottom, area);
		
		area = new Rectangle( 0, topCornerHeight, left.getWidth(this), height - topCornerHeight - bottomCornerHeight);
		PaintUtilities.texturePaintVertical(this, g, left, area);
		
		area = new Rectangle( width - right.getWidth(this), topCornerHeight, 
				right.getWidth(this), height - topCornerHeight - bottomCornerHeight);
		PaintUtilities.texturePaintVertical(this, g, right, area);
		
		//Draw the corners
		g.drawImage(cornerUL, 0, 0, this);
		g.drawImage(cornerUR, width - topCornerWidth, 0, this);
		g.drawImage(cornerLL, 0, height - bottomCornerHeight, this);
		g.drawImage(cornerLR, width - bottomCornerWidth, height - bottomCornerHeight, this);
	}

	/**
	 * Returns the interior of the border.
	 *
	 * @return A Rectangle object representing the interior of the border.
	 */
	public Rectangle getInnerArea()
	{
		return new Rectangle(left.getWidth(this), top.getHeight(this),
							getWidth() - left.getWidth(this) - right.getWidth(this),
							getHeight() - top.getHeight(this) - bottom.getHeight(this));
	}
	
	/**
	 * 
	 * Returns the area of the entire top bar.  Used for titles when they are available.
	 *
	 * @return A Rectangle object representing the title bar.
	 */
	public Rectangle getTitleArea()
	{
		return new Rectangle(cornerUL.getWidth(this), 0,
							getWidth() - cornerUL.getWidth(this) - cornerUR.getWidth(this),
							top.getHeight(this));
	}
}
