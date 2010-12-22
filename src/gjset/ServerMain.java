package gjset;

import gjset.server.GameServer;
import gjset.server.ServerController;
import gjset.server.gui.CommandLineConsole;
import gjset.server.gui.ServerConsole;

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

public class ServerMain
{

	/**
	 * Start the server
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		ServerConsole console = new CommandLineConsole();
		
		
		int port = Integer.parseInt(args[0]);
		
		// Create the game server and tell it to listen for clients.
		GameServer server = new GameServer(port, console);
		ServerController serverController = new ServerController(server, console);
		
		console.setController(serverController);
		
		
		server.listenForClients();
	}

}
