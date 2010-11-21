package gjset.data;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
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

public class Player
{
	// Store the various details of this player.
	private int	points;
	private int penalty;
	private int id;
	private String name;

	// Create a basic, blank player.
	public Player()
	{
		// Clear the values for the player.
		this.points = 0;
		this.penalty = 0;
		this.id = 0;
		this.name = "Player";
	}

	// Create a player based on player number.
	public Player(int number)
	{
		this();
		
		// Clear the values for the player.
		this.id = number;
		this.name = "Player " + number;
	}

	/**
	 * Build a player from the indicated player element.
	 *
	 * @param playerElement
	 */
	public Player(Element element)
	{
		parsePlayer(element);
	}

	public int getScore()
	{
		return (points - penalty);
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public int getPenalty()
	{
		return penalty;
	}
	
	public String getName()
	{
		return name;
	}

	/**
	 * Return the id for this player.
	 *
	 * @return
	 */
	public int getId()
	{
		return id;
	}

	public void addPoints(int x)
	{
		points += x;
	}

	public void addPenalty(int x)
	{
		penalty += x;
		
		// Never allow us to go negative.
		if(penalty > points)
		{
			penalty = points;
		}
	}

	public void resetScore()
	{
		penalty = 0;
		points = 0;
	}
	
	public void setName (String playername)
	{
		name = playername;
	}
	
	public void setId (int x)
	{
		id = x;
		if (name == "")
		{
			name = "Player " + id;
		}
	}

	/**
	 * 
	 * Build the player using the indicated player element.
	 *
	 * @param playerElement
	 */
	private void parsePlayer(Element playerElement)
	{
		String idString = playerElement.attributeValue("id", "0");
		id = Integer.parseInt(idString);
		
		String pointsString = playerElement.element("points").getText();
		points = Integer.parseInt(pointsString);
		
		String penaltyString = playerElement.element("penalty").getText();
		penalty = Integer.parseInt(penaltyString);
		
		name = playerElement.element("name").getText();
	}

	/**
	 * Return a representation of the player.
	 *
	 * @return
	 */
	public Element getXMLRepresentation()
	{
		DocumentFactory documentFactory = DocumentFactory.getInstance();
		
		Element playerElement = documentFactory.createElement("player");
		playerElement.addAttribute("id", "" + id);
		
		Element pointsElement = documentFactory.createElement("points");
		pointsElement.setText("" + points);
		playerElement.add(pointsElement);
		
		Element penaltyElement = documentFactory.createElement("penalty");
		penaltyElement.setText("" + penalty);
		playerElement.add(penaltyElement);
		
		Element nameElement = documentFactory.createElement("name");
		nameElement.setText(name);
		playerElement.add(nameElement);
		
		return playerElement;
	}	
}
