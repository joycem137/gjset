package gjset.gui.framework;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

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
 * This class works like any ordinary JPanel, but draws a background image underneath all of its children.
 */
@SuppressWarnings("serial")
public class SimpleImagePanel extends JPanel
{
	protected Image image;
	
	/**
	 * 
	 * Create this SimpleImagePanel with the indicated background image.
	 *
	 * @param image
	 */
	public SimpleImagePanel(Image image)
	{
		this.image = image;
	}
	
	/**
	 * 
	 * When painting this component, just draw the image.  Do nothing else.
	 *
	 * @param g
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		g.drawImage(image, 0, 0, this);
	}
}
