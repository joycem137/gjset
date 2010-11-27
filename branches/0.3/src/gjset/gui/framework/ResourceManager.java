package gjset.gui.framework;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

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
 * This class does all of the wonderful things we need to do to
 * load images and use them.
 */
public class ResourceManager
{
	private static ResourceManager singleton;
	
	private static String RESOURCE_DIRECTORY = "resources";
	
	private Map<String, Image> map;
	
	/**
	 * Returns the resource manager singleton.
	 *
	 * @return
	 */
	public static ResourceManager getInstance()
	{
		if(singleton == null)
		{
			singleton = new ResourceManager();
		}
		return singleton;
	}
	
	/**
	 * 
	 * Creates the resource manager and initializes all values.
	 *
	 */
	private ResourceManager()
	{
		map = new HashMap<String, Image>();
	}
	
	/**
	 * 
	 * Returns the image object associated with the indicated image name.
	 *
	 * @param filename
	 * @return
	 */
	public Image getImage(String filename)
	{
		if(!map.containsKey(filename))
		{
			loadImage(filename);
		}
		
		return (Image)map.get(filename);
	}
	
	/**
	 * 
	 * Loads a single image and stores it in the hash table.
	 *
	 * @param filename
	 */
	private void loadImage(String filename)
	{
		String path = "/" + RESOURCE_DIRECTORY + "/" + filename;
		
		URL imageFile = getClass().getResource(path);

		// Verify that we found a file
		if(imageFile == null)
		{
			System.err.println("Could not load path " + path);
			System.exit(-1);
		}
		
		//Read the file in.
		try
		{
			Image image = ImageIO.read(imageFile);
			
			if(image== null)
			{
				System.err.println("Error Loading " + path);
				System.exit(-1);
			}
			
			//Store the image with its name
			map.put(filename, image);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("Failed to load " + path);
			System.exit(-1);
		}
	}
}
