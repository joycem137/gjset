package gjset.tools;

import java.net.ServerSocket;
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
 *
 */
public class CommunciationTester
{

	public static void main (String args[]) {
		// Finally, load any temporary overrides from the commandline.
		Properties commandLineProps = new Properties();
		GlobalProperties.parseCommandLineArgs(args, commandLineProps);
		
		// Assign the global properties.
		GlobalProperties.properties = commandLineProps;
		
		int port = getPort();
		String hostname = getHostname();
		boolean listeningServer = getListening();
		
		if(listeningServer) {
			TestServer server = new TestServer(port);
			server.startListening();
		} else {
			TestClient client = new TestClient(hostname, port);
			client.connect();
		}
			
	}
	
	private static String getHostname() {
		String property = "";
		Properties props = GlobalProperties.properties;
		
		property = props.getProperty("server.hostname");
		
		if(property == null) {
			property = props.getProperty("p");
		}
		
		if(property == null) {
			property = props.getProperty("commandline.args.misc");
		}
		
		return property;
	}

	private static int getPort() {
		String property = "";
		Properties props = GlobalProperties.properties;
		
		property = props.getProperty("server.port");
		
		if(property == null) {
			property = props.getProperty("p");
		}
		
		if(property == null) {
			property = props.getProperty("commandline.args.misc");
		}
		
		return Integer.parseInt(property);
	}

	private static boolean getListening() {
		String property = GlobalProperties.properties.getProperty("listen");
		return property != null && property.equals("true");
	}
}
