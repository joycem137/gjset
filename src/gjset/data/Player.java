package gjset.data;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton and Andrea Kilpatrick.
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of gjSet are very grateful for
 *  them creating such an excellent card game.
 *  
 *  gjSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  gjSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with gjSet.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Player
{

	// Store the various details of this player.
	private int				points, penalty, id;
	private String			name;

	// Create a basic, blank player.
	public Player()
	{
		// Clear the values for the player.
		this.points = 0;
		this.penalty = 0;
		this.id = 0;
		this.name = "";
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
}
