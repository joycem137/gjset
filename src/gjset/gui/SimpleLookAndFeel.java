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
	 * Return the system's default look and feel.
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

	public Font getDialogInputFont()
	{
		return defaultFont.deriveFont(13.0f);
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

	public Font getBigButtonFont()
	{
		Font buttonFont = defaultFont.deriveFont(18.0f);
		
		return buttonFont;
	}

	public Color getBigButtonLabelBackground()
	{
		return new Color(61, 28, 8);
	}

	public Color getBigButtonLabelForeground()
	{
		return new Color(243, 232, 175);
	}

	public Color getPlayerPanelBackgroundColor()
	{
		return new Color(0, 0, 0, 130);
	}

	public Font getPlayerPanelNameFont()
	{
		Font playerNameFont = defaultFont.deriveFont(25.0f);
		
		return playerNameFont;
	}

	public Color getPlayerPanelNameColor()
	{
		return new Color(235, 235, 125);
	}

	public Font getPlayerPanelScoreFont()
	{
		Font font = defaultFont.deriveFont(42.0f);
		
		return font;
	}

	public Color getPlayerPanelScoreColor()
	{
		return Color.white;
	}

	public Color getOtherPlayerPanelBackgroundColor()
	{
		// Return the exact same color as the Local Player Panel.
		return getPlayerPanelBackgroundColor();
	}

	public Font getOtherPlayerPanelNameFont()
	{
		// Derive the font from the local player font.
		Font playerFont = getPlayerPanelNameFont();
		
		Font font = playerFont.deriveFont(12.0f);
		
		return font;
	}

	public Color getOtherPlayerPanelNameColor()
	{
		return new Color(255, 107, 107);
	}

	public Font getOtherPlayerPanelScoreFont()
	{
		// Derive the font from the local player font.
		Font playerFont = getPlayerPanelScoreFont();
		
		Font font = playerFont.deriveFont(30.0f);
		
		return font;
	}

	public Color getOtherPlayerPanelScoreColor()
	{
		// This should just be the same color as the local player.
		return getPlayerPanelScoreColor();
	}
}
