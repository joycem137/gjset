package gjset;

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
 * This class stores a bunch of constants for the game.
 */
public class GameConstants
{
	public static final int GAME_STATE_NOT_STARTED = 0;
	public static final int GAME_STATE_IDLE = 1;
	public static final int GAME_STATE_SET_CALLED = 2;
	public static final int GAME_STATE_SET_FINISHED = 3; // This state represents the condition when a set was finished.
	public static final int GAME_STATE_GAME_OVER = 4;
	
	public static final int GAME_PORT = 15563;
	
	public static final String COMM_VERSION = "2";
	
	public static final int MAX_PLAYERS = 8;
	
	public static final int SET_POINTS = 3;
	public static final int SET_PENALTY = 2;
}
