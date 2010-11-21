package gjset.data;

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
		// Clear the values for the player.
		this.points = 0;
		this.penalty = 0;
		this.id = number;
		this.name = "Player " + number;
	}
		
	// Create a named player.
	public Player(String name)
	{
		// Set the player details.
		this.points = 0;
		this.penalty = 0;
		this.id = 0;
		this.name = name;
	}

	// Create a named, numbered player.
	public Player(String name, int number)
	{
		// Set the player details.
		this.points = 0;
		this.penalty = 0;
		this.id = number;
		this.name = name;
	}

	
	public Player(int points, int penalty, int id, String name)
	{
		this.points = points;
		this.penalty = penalty;
		this.id = id;
		this.name = name;
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

	public void addPoints(int x)
	{
		points += x;
	}

	public void addPenalty(int x)
	{
		penalty += x;
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
		if (name == "") {
			name = "Player " + id;
		}
	}

	public String getRepresentation()
	{
		return "" + points + ":" + penalty + ":" + id + ":" + name + ":";
	}

	public static Player parsePlayer(String[] messageArray)
	{
		int points = Integer.parseInt(messageArray[1]);
		int penalty = Integer.parseInt(messageArray[2]);
		int id = Integer.parseInt(messageArray[3]);
		String name = messageArray[4];
		return new Player(points, penalty, id, name);
	}	
}
