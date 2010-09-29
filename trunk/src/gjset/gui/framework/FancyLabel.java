package gjset.gui.framework;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;
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
 * This basically acts as a normal JLabel, except that instead of being
 * limited to the basic styles, this adds a whole bunch of extra.
 */
public class FancyLabel extends JComponent
{
	public static final int NORMAL = 0;
	public static final int	OUTLINE	= 1;
	
	private int	effect;
	
	private String text;
	private int horizontalAlignment;

	public FancyLabel(String text, int horizontalAlignment)
	{
		this.text = text;
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 * Set the "fancy effect" for this label.
	 *
	 * @param outline2
	 */
	public void setFancyEffect(int effect)
	{
		this.effect = effect;
	}
	
	/**
	 * 
	 * Overrides paintComponent method to draw the text with the indicated style. 
	 *
	 * @param g The graphics context.
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.setFont(getFont());
		Point point = calculateTextPosition(g);
		
		if(effect == NORMAL)
		{
			g.setColor(getForeground());
			g.drawString(text, point.x, point.y);
		}
		else if(effect == OUTLINE)
		{	
			g.setColor(getBackground());
			g.drawString(text, point.x - 1, point.y + 1);
			g.drawString(text, point.x - 1, point.y - 1);
			g.drawString(text, point.x + 1, point.y + 1);
			g.drawString(text, point.x + 1, point.y - 1);
			
			g.setColor(getForeground());
			g.drawString(text, point.x, point.y);

		}
	}

	/**
	 * Calculates the starting position of the text, based on the horizontal alignment value
	 *
	 * @return a Point object that represents the location to draw the string on.
	 */
	private Point calculateTextPosition(Graphics g)
	{
		FontMetrics metrics = g.getFontMetrics();
		
		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getMaxAscent();
		
		Point textPosition = new Point();
		if(horizontalAlignment == SwingConstants.CENTER)
		{
			textPosition.x = getWidth() / 2 - textWidth / 2;
			textPosition.y = getHeight() / 2 + textHeight / 2 - 2;
		}
		else // Just do left justified for now.
		{
			textPosition.x = 0;
			textPosition.y = 0;
			
			if(effect == OUTLINE)
			{
				textPosition.x++;
				textPosition.y++;
			}
		}
		
		return textPosition;
	}
}
