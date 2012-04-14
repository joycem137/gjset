package gjset.client;

import gjset.tools.GlobalProperties;

import java.io.IOException;
import java.util.Properties;

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
 * This class initializes the connection with the server and handles
 * authentication.
 */
public class ConnectionInitializer
{

	private ClientCommunicator communicator;

	/**
	 *
	 * @param communicator
	 */
	public ConnectionInitializer(ClientCommunicator communicator) {
		this.communicator = communicator;
	}

	/**
	 * Establish a connection with the server.
	 * This does not necessarily mean that we've authenticated with said server.
	 *
	 */
	public void connectToServer() {		
		// Extract properties from the properties table.
		Properties props = GlobalProperties.properties;
		
		String hostname = props.getProperty("server.hostname", "artless-entertainment.com");
		int port = Integer.parseInt(props.getProperty("server.port", "15536"));
		
		try {
			communicator.connectToServer(hostname, port);
		} catch (IOException e) {
			// TODO: Display an error message to the user.
			e.printStackTrace();
		}
	}

	/**
	 * Authenticate the user with the indicated username and password.
	 *
	 * @param name
	 * @param password
	 */
	public void authenticateUser(String name, String password) {
		// TODO Auto-generated method stub
		
	}

}
