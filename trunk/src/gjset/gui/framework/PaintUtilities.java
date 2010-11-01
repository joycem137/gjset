package gjset.gui.framework;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Just a collection of useful painting methods.
 */
public class PaintUtilities
{
	/**
	 * Paint a single texture horizontally across the screen.
	 *
	 * @param g
	 * @param image
	 * @param areaToPaint
	 */
	public static void texturePaintHorizontal(JComponent parent, Graphics g, Image image, Rectangle areaToPaint)
	{
		int imageWidth = image.getWidth(parent);
		int imageHeight = image.getHeight(parent);
		
		int cols = areaToPaint.width / imageWidth;
		
		for (int x = 0; x < cols; x++) 
		{
			g.drawImage(image, x * imageWidth + areaToPaint.x, areaToPaint.y, parent);
		}
		
		//Draw the last partial image in.
		int remainingWidth = areaToPaint.width - cols * imageWidth;
		
		if(remainingWidth > 0)
		{
			BufferedImage temp = new BufferedImage(remainingWidth, imageHeight, BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D g2 = temp.createGraphics();
			g2.drawImage(image, 0, 0, parent);
			temp.flush();
			
			g.drawImage(temp, areaToPaint.width + areaToPaint.x - remainingWidth, areaToPaint.y, parent);
		}
	}

	/**
	 * Paint a single texture vertical up and down the screen.
	 *
	 * @param g The current graphics context.
	 * @param image The Image to use as a texture
	 * @param areaToPaint A Rectangle representing the area to paint.
	 */
	public static void texturePaintVertical(JComponent parent, Graphics g, Image image, Rectangle areaToPaint)
	{
		int imageWidth = image.getWidth(parent);
		int imageHeight = image.getHeight(parent);
		
		int rows = areaToPaint.height / imageHeight;
		
		for (int y = 0; y < rows; y++) 
		{
			g.drawImage(image, areaToPaint.x, y * imageHeight + areaToPaint.y, parent);
		}
		
		//Draw the last partial image in.
		int remainingHeight = areaToPaint.height - rows * imageHeight;
		
		if(remainingHeight > 0)
		{
			BufferedImage temp = new BufferedImage(imageWidth, remainingHeight, BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D g2 = temp.createGraphics();
			g2.drawImage(image, 0, 0, parent);
			temp.flush();
			
			g.drawImage(temp, areaToPaint.x, areaToPaint.height + areaToPaint.y - remainingHeight, parent);
		}
	}
}
