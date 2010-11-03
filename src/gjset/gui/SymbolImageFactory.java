package gjset.gui;

import gjset.data.Card;
import gjset.gui.framework.ResourceManager;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

import javax.swing.JPanel;

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
	
	private static final int SYMBOL_BUFFER = 2;

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
		
		int imageHeight = shadowImage.getHeight(null);
		int imageWidth = shadowImage.getWidth(null);
		
		// Create the image object to store the drawing of the card.
		BufferedImage image = new BufferedImage(imageWidth, SYMBOL_BUFFER * (number - 1) + number * imageHeight, 
				BufferedImage.TYPE_INT_ARGB_PRE);

		// Get the image's graphics context.
		Graphics2D g = ((BufferedImage) image).createGraphics();
		
		// Draw each symbol.
		int yPos;
		for (int i = 0; i < number; i++)
		{
			yPos = i * (imageHeight + SYMBOL_BUFFER);
			
			// Draw the shadow
			g.drawImage(shadowImage, 0, yPos, null);

			// Draw the actual symbol.
			Image colorSymbol = colorizeSymbol(color, baseImage);
			g.drawImage(colorSymbol, 0, yPos, null);
		}

		// Flush the image drawing.
		image.flush();
		
		return image;
	}
	
	/**
	 * Draw an image to the indicated graphics context, colorizing it as we go.
	 *
	 * @param color
	 * @param baseImage
	 */
	private Image colorizeSymbol(int color, Image baseImage)
	{
		ColorizingFilter filter = new ColorizingFilter(color);
		
		// I guess we need a jpanel to host this?
		JPanel blah = new JPanel();
		Image image = blah.createImage(new FilteredImageSource(baseImage.getSource(), filter));
		
		return image;
	}
	
	class ColorizingFilter extends RGBImageFilter 
	{
		private int colorMask;
	    public ColorizingFilter(int color)
	    {
	    	if(color == Card.COLOR_RED)
	    	{
	    		colorMask = 0xff0000;
	    	}
	    	else if(color == Card.COLOR_GREEN)
	    	{
	    		colorMask = 0x00ff00;
	    	}
	    	else if(color == Card.COLOR_BLUE)
	    	{
	    		colorMask = 0x0000ff;
	    	}
	    	
	    	// The filter's operation does not depend on the
	    	// pixel's location, so IndexColorModels can be
	    	// filtered directly.
	    	canFilterIndexColorModel = true;
	    }

	    public int filterRGB(int x, int y, int rgb) 
	    {	
	    	return rgb | colorMask;
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
		System.out.println("Getting image");
		return masterImages[number - 1][color - 1][shape - 1][shading - 1];
	}

	/**
	 * Returns an image that represents the indicated card data.
	 *
	 * @param cardData
	 * @return
	 */
	public Image getImage(Card cardData)
	{
		return getImage(cardData.getNumber(), cardData.getColor(), cardData.getShape(), cardData.getShading());
	}
	
}
