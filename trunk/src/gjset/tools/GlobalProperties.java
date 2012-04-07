package gjset.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

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
 * This class enables the use of a global properties table across the entire application.
 */
public class GlobalProperties
{	
	public static Properties properties;
	

	/**
	 * Parse the command line arguments into a properties object.
	 *
	 * @param args
	 * @param commandLineProps
	 */
	public static void parseCommandLineArgs(String[] args, Properties commandLineProps)
	{
		String currentFlag = null;
		Vector<String> miscArgs = new Vector<String>();
		
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if(currentFlag != null) {
				// If we already have a current flag, then we look to see what we're dealing with next.
				if(arg.startsWith("-")) {
					commandLineProps.setProperty(currentFlag, "true");
					currentFlag = arg;
				} else {
					commandLineProps.setProperty(currentFlag, arg);
					currentFlag = null;
				}
			} else {				
				if(arg.startsWith("-")) {
					currentFlag = arg.substring(1);				
				} else {
					// This is a miscellaneous argument that has no key.  Just a value.
					miscArgs.add(arg);
				}
			}
		}
		
		// Upon termination, if we still have an outstanding flag, put that in the parameter map.
		if(currentFlag != null) {
			commandLineProps.setProperty(currentFlag, "true");
		}
		
		// If we have any miscellaneous arguments, values without keys, then put them in the properties here.
		if(miscArgs.size() > 0) {
			commandLineProps.setProperty("commandline.args.misc", join(miscArgs, ","));
		}
	}
	
    public static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

	/**
	 * Load Properties from the indicated file.
	 *
	 * @param defaultProps
	 * @param string
	 */
	public static void loadPropertiesFromFile(Properties props, String filename) {

		try {
			String path = "/" + filename;
			InputStream stream = props.getClass().getResourceAsStream(path);
			
			if(stream == null) return;
			
			props.load(stream);
			stream.close();
		} catch (Exception e) {
			// If there are any errors, we just skip over this.
		}
	}
	
}
