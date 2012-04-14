package gjset;

import gjset.client.ClientController;
import gjset.tools.GlobalProperties;

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

public class ClientMain
{
	public static void main(String[] args)
	{
	    // set up default system properties
		Properties defaultProps = new Properties();
		GlobalProperties.loadPropertiesFromFile(defaultProps, "default.properties");

	    // Now get our debug properties, if they exist.
		Properties debugProps = new Properties(defaultProps);
		GlobalProperties.loadPropertiesFromFile(debugProps, "debug.properties");

	    // Get any properties set up by the user.
		Properties userProps = new Properties(debugProps);
		GlobalProperties.loadPropertiesFromFile(userProps, "user.properties");
		
		// Finally, load any temporary overrides from the commandline.
		Properties commandLineProps = new Properties(userProps);
		GlobalProperties.parseCommandLineArgs(args, commandLineProps);
		
		// Assign the global properties.
		GlobalProperties.properties = commandLineProps;
		
		// Get this show on the road!
		ClientController controller = new ClientController();		
		controller.start();
	}
}
