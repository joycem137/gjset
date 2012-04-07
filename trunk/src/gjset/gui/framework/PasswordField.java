package gjset.gui.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JPasswordField;

/**
 * This is a duplication of TextField to support password fields.
 */
@SuppressWarnings("serial")
public class PasswordField extends JPasswordField
{
	private Image left;
	private Image middle;
	private Image right;

	public PasswordField(String style)
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
}
