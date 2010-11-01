package gjset.gui;

import java.awt.Color;
import java.awt.Font;

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
public class SimpleLookAndFeel
{

	private static SimpleLookAndFeel	singleton;

	/**
	 * TODO: Describe method
	 *
	 * @return
	 */
	public static SimpleLookAndFeel getLookAndFeel()
	{
		if(singleton == null)
		{
			singleton = new SimpleLookAndFeel();
		}
		return singleton;
	}

	private Font	defaultFont;
	private Color	dialogBackgroundColor;

	protected SimpleLookAndFeel()
	{
		defaultFont = new Font("Tahoma Bold", Font.BOLD, 20);
		dialogBackgroundColor = new Color(227, 209, 156);
		
	}
	
	public Font getDialogFont()
	{
		return defaultFont;
	}

	public String getDialogButtonStyle()
	{
		return "button_brown";
	}

	public Color getDialogBackgroundColor()
	{
		return dialogBackgroundColor;
	}
	
	public String getBorderStyle()
	{
		return "window";
	}

	public Font getDialogTitleFont()
	{
		return defaultFont;
	}

	public Color getDialogTitleFG()
	{
		return Color.white;
	}
	
	public Color getDialogTitleBG()
	{
		return Color.black;
	}

	public Font getDeckSizeFont()
	{
		Font deckFont = defaultFont.deriveFont(35.0f);
		
		return deckFont;
	}

	public Color getDeckSizeColor()
	{
		return Color.white;
	}
}
