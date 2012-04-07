package gjset.tools;

import java.util.HashMap;
import java.util.Map;
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
 * This is a singleton convenience class.  It will parse through the command line parameters
 * and provide the user with a standard lookup map of parameters.
 */
public class CommandLineParameters
{
	private static Map<String, String> parameterMap;
	private static Vector<String> parameterList;

	/**
	 * Parse the parameters into easy to understand values
	 *
	 * @param args
	 */
	public static void parse(String[] args)
	{
		String currentFlag = null;
		
		parameterMap = new HashMap<String, String>();
		parameterList = new Vector<String>();
		
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if(currentFlag != null) {
				// If we already have a current flag, then we look to see what we're dealing with next.
				if(arg.startsWith("-")) {
					parameterMap.put(currentFlag, "true");
					currentFlag = arg;
				} else {
					parameterMap.put(currentFlag, arg);
					currentFlag = null;
				}
			} else {				
				if(arg.startsWith("-")) {
					currentFlag = arg.substring(1);				
				} else {
					parameterList.add(arg);
				}
			}
		}
		
		// Upon termination, if we still have an outstanding flag, put that in the parameter map.
		if(currentFlag != null) {
			parameterMap.put(currentFlag, "true");
		}
	}

	/**
	 * Return the indicated parameter, if it exists.  Otherwise, return null.
	 *
	 * @param string
	 * @return
	 */
	public static String get(String key)
	{
		return parameterMap.get(key);		
	}

	/**
	 * 
	 * Returns true if the indicated key is within the command line parameters
	 * and isn't set to false.
	 *
	 * @param key
	 * @return
	 */
	public static boolean check(String key) {
		if(parameterMap.containsKey(key)) {
			String value = parameterMap.get(key);
			return value.length() > 0 && !value.equals("false");
		} else {
			return parameterList.contains(key);
		}
	}

}
