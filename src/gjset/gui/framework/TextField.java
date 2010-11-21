package gjset.gui.framework;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTextField;

/**
 * This class takes your basic text field functionality and changes the painting of it to include 
 * a custom background image.
 */
@SuppressWarnings("serial")
public class TextField extends JTextField
{
	private Image left;
	private Image middle;
	private Image right;

	public TextField(String style)
	{
		super();

		// Get all of our images.
		ResourceManager resourceManager = ResourceManager.getInstance();
		left = resourceManager.getImage(style + "_l.png");
		middle = resourceManager.getImage(style + "_m.png");
		right = resourceManager.getImage(style + "_r.png");
		
		setOpaque(false);
		setDragEnabled(false);
		setBackground(new Color(0, 0, 0, 0));
		setBorder(null);;
	}
	
	public Insets getInsets()
	{
		return new Insets(0, left.getWidth(this) - 5, 0, right.getWidth(this) - 5);
	}
	
	public void paint(Graphics g)
	{	
		// Draw the background.
		
		// Draw the edges first.
		g.drawImage(left, 0, 0, this);
		g.drawImage(right, getWidth() - right.getWidth(this), 0, this);
		
		// Finally, draw the middle.
		Rectangle areaToPaint = new Rectangle(left.getWidth(this), 0, 
				getWidth() - left.getWidth(this) - right.getWidth(this), 
				middle.getHeight(this));
		PaintUtilities.texturePaintHorizontal(this, g, middle, areaToPaint);

		// Draw the rest.
		super.paint(g);
	}

	/**
	 * Calculates the starting position of the text, based on the horizontal alignment value
	 *
	 * @return a Point object that represents the location to draw the string on.
	 */
	private Point calculateTextPosition(Graphics g)
	{
		FontMetrics metrics = g.getFontMetrics();
		
		int textHeight = metrics.getMaxAscent();
		
		Point textPosition = new Point();
		textPosition.x = left.getWidth(this) - 5;
		textPosition.y = getHeight() / 2 + textHeight / 2 - 2;
		
		return textPosition;
	}

}
