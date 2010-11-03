package gjset.gui;

import gjset.data.Card;
import gjset.gui.framework.ResourceManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 */
public class SymbolImageFactory
{

	private static SymbolImageFactory	singleton;

	/**
	 * Return the image factory instance.
	 *
	 * @return
	 */
	public static SymbolImageFactory getInstance()
	{
		if(singleton == null)
		{
			singleton = new SymbolImageFactory();
		}
		
		return singleton;
	}
	
	private static final int SHAPE_SHADOW_OFFSET = 1;

	private Image baseImages[][];
	private Image shadowImages[][];
	
	private Image masterImages[][][][];
	
	protected SymbolImageFactory()
	{
		createImageArrays();
		obtainBaseImages();
		createMasterImages();
	}

	/**
	 * Creates all of the image arrays we're dealing with here.
	 *
	 */
	private void createImageArrays()
	{
		baseImages = new Image[3][3];

		shadowImages = new Image[3][3];
		
		masterImages = new Image[3][3][3][3];
	}

	/**
	 * Obtain all of the base images for the class.
	 *
	 */
	private void obtainBaseImages()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		for(int shape = 1; shape <= 3; shape++)
		{
			for(int shading = 1; shading <= 3; shading++)
			{
				baseImages[shape - 1][shading - 1] = resourceManager.getImage("symbol_" + getShapeName(shape) + "_" + shading + ".png");
				shadowImages[shape - 1][shading - 1] = resourceManager.getImage("symbol_" + getShapeName(shape) + "_" + shading + "_s.png");
			}
		}
	}

	/**
	 * Return the shape name used in filenames for this shape.
	 *
	 * @param shape
	 * @return
	 */
	private String getShapeName(int shape)
	{
		switch(shape)
		{
			case Card.SHAPE_OVAL:
				return "rect";
			case Card.SHAPE_SQUIGGLE:
				return "tilde";
			case Card.SHAPE_DIAMOND:
				return "diamond";
			default:
				return "";
		}
	}

	/**
	 * Cycle through all of our base images and use them to generate our master images.
	 *
	 */
	private void createMasterImages()
	{
		// CAPTAIN! BEGIN THE LOOPING!
		// AYE CAPTAIN!
		for(int number = 1; number <= 3; number++)
		{
			for(int color = 1; color <= 3; color++)
			{
				for(int shape = 1; shape <= 3; shape++)
				{
					for(int shading = 1; shading <= 3; shading++)
					{
						masterImages[number - 1][color - 1][shape - 1][shading - 1] = generateImage(number, color, shape, shading);
					}
				}
			}
		}
	}

	/**
	 * Generate the indicated image.
	 *
	 * @param number
	 * @param color
	 * @param shape
	 * @param shading
	 * 
	 * @return
	 */
	private Image generateImage(int number, int color, int shape, int shading)
	{
		// Get the base images from which this image should be generated.
		Image baseImage = baseImages[shape - 1][shading - 1];
		Image shadowImage = shadowImages[shape - 1][shading - 1];
		
		int imageHeight = baseImage.getHeight(null);
		int imageWidth = baseImage.getWidth(null);
		
		// Create the image object to store the drawing of the card.
		BufferedImage image = new BufferedImage(imageWidth + SHAPE_SHADOW_OFFSET * 2, 
				number * (imageHeight + SHAPE_SHADOW_OFFSET) + SHAPE_SHADOW_OFFSET, 
				BufferedImage.TYPE_INT_ARGB_PRE);

		// Get the image's graphics context.
		Graphics2D g = ((BufferedImage) image).createGraphics();
		
		// Draw each symbol.
		int yPos;
		for (int i = 0; i < number; i++)
		{
			yPos = SHAPE_SHADOW_OFFSET + i * (imageHeight + SHAPE_SHADOW_OFFSET);
			
			// Draw the shadow
			g.drawImage(shadowImage, 2 * SHAPE_SHADOW_OFFSET, yPos + SHAPE_SHADOW_OFFSET, null);

			// Draw the actual symbol.
			g.drawImage(baseImage, SHAPE_SHADOW_OFFSET, yPos, null);
		}

		// Flush the image drawing.
		image.flush();
		
		return image;
	}
	
	// Convert the color property to a Color object.
	private Color getRealColor(int color)
	{
		switch (color)
		{
			case Card.COLOR_RED:
				return Color.red;
			case Card.COLOR_BLUE:
				return Color.blue;
			case Card.COLOR_GREEN:
				return Color.green;
			default:
				return Color.black;
		}
	}

	/**
	 * Returns the image of the symbol with the indicated attributes.
	 *
	 * @param number
	 * @param color
	 * @param shape
	 * @param shading
	 * 
	 * @return
	 */
	public Image getImage(int number, int color, int shape, int shading)
	{
		return masterImages[number - 1][color - 1][shape - 1][shading - 1];
	}
	
}
