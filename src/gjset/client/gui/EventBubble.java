package gjset.client.gui;

import gjset.gui.framework.PaintUtilities;
import gjset.gui.framework.ResourceManager;
import gjset.gui.framework.SimpleLookAndFeel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * This class produces a graphical object that can be used to display events.
 */
@SuppressWarnings("serial")
public class EventBubble extends JComponent
{
	public static final int STATE_NONE = 0;
	public static final int STATE_CALL = 1;
	public static final int STATE_DRAW = 2;
	
	public static final int ORIENT_LEFT = 0;
	public static final int ORIENT_RIGHT = 1;

	private Image cornerUL;
	private Image cornerUR;
	private Image cornerLL;
	private Image cornerLR;
	
	private Image left;
	private Image right;
	private Image top;
	private Image bottom;
	
	private Image realCornerLL;
	private Image realCornerLR;
	
	private Image leftTail;
	private Image rightTail;
	
	private Color backgroundColor;
	
	private Image eventCall;
	private Image eventDraw;
	
	// Now store the state of this event.
	private int state;
	private int orientation;
	
	private int leftSide;
	private int rightSide;
	
	private static final int IMAGE_FIX = 0;
	
	public EventBubble(int orientation)
	{	
		loadImages();
		setOrientation(orientation);
		setState(STATE_NONE);
		
		backgroundColor = SimpleLookAndFeel.getLookAndFeel().getEventBackgroundColor();
		
		// Set the size to be big enough to accomodate the event call object.
		setSize(eventCall.getWidth(this) + leftTail.getWidth(this) - realCornerLL.getWidth(this) + IMAGE_FIX,
				eventCall.getWidth(this) + IMAGE_FIX);
		
		setOpaque(false);
	}

	/**
	 * Return the orientation of the bubble
	 *
	 * @return
	 */
	public int getOrientation()
	{
		return orientation;
	}

	/**
	 * Set the orientation of the bubble
	 *
	 * @param orientation
	 */
	public void setOrientation(int orientation)
	{
		this.orientation = orientation;
		
		if(orientation == ORIENT_RIGHT)
		{
			// Start by setting the images.
			cornerLL = realCornerLL;
			cornerLR = rightTail;
			
			leftSide = 0;
		}
		else
		{
			// Start by setting the images.
			cornerLL = leftTail;
			cornerLR = realCornerLR;
			
			leftSide = leftTail.getWidth(this) - realCornerLL.getWidth(this);
		}
		
		// The right side is the width of the eventCall image plus the left side.
		rightSide = eventCall.getWidth(this) + leftSide + IMAGE_FIX;
	}

	public void paintComponent(Graphics g)
	{	
		// Get a whole bunch of values.
		int width = rightSide - leftSide;
		int height = getHeight();
		int widthUL = cornerUL.getWidth(this);
		int heightUL = cornerUL.getHeight(this);
		int widthUR = cornerUR.getWidth(this);
		int heightUR = cornerUR.getHeight(this);
		int heightLL = cornerLL.getHeight(this);
		int widthLL = cornerLL.getWidth(this);
		int widthLR = cornerLR.getWidth(this);
		int heightLR = cornerLR.getHeight(this);
		
		// Draw the top
		Rectangle area = new Rectangle( leftSide + widthUL, 0, 
				width - widthUL - widthUR, top.getHeight(this));
		PaintUtilities.texturePaintHorizontal(this, g, top, area);
		
		// Draw left side.
		area = new Rectangle( leftSide, heightUL, left.getWidth(this), height - heightUL - heightLL);
		PaintUtilities.texturePaintVertical(this, g, left, area);
		
		// Draw the right side.
		area = new Rectangle( rightSide - right.getWidth(this), heightUR, 
				right.getWidth(this), height - heightUR - heightLR);
		PaintUtilities.texturePaintVertical(this, g, right, area);
		
		// Draw bottom
		int bottomLeftSide;
		int bottomRightSide;
		if(orientation == ORIENT_LEFT)
		{
			bottomLeftSide = 0;
			bottomRightSide = rightSide;
		}
		else
		{
			bottomLeftSide = leftSide;
			bottomRightSide = rightSide + leftTail.getWidth(this) - realCornerLR.getWidth(this);
		}
		
		area = new Rectangle( bottomLeftSide + widthLL, 
				height - bottom.getHeight(this), 
				bottomRightSide - widthLR - widthLL - bottomLeftSide, 
				bottom.getHeight(this));
		PaintUtilities.texturePaintHorizontal(this, g, bottom, area);

		//Draw the top corners
		g.drawImage(cornerUL, leftSide, 0, this);
		g.drawImage(cornerUR, rightSide - widthUR, 0, this);
		
		// Draw the bottom corners
		g.drawImage(cornerLL, bottomLeftSide, height - heightLL, this);
		g.drawImage(cornerLR, bottomRightSide - widthLR, height - heightLR, this);
		
		// Now draw the background color.
		Rectangle backgroundArea = new Rectangle(leftSide + left.getWidth(this), 
					top.getHeight(this),
					width - right.getWidth(this) - left.getWidth(this),
					height - top.getHeight(this) - bottom.getHeight(this));
		g.setColor(backgroundColor);
		((Graphics2D) g).fill(backgroundArea);
		
		// And finally, the event object.
		Image eventToDraw = eventCall;
		switch(state)
		{
			case STATE_CALL:
				eventToDraw = eventCall;
				break;
			case STATE_DRAW:
				eventToDraw = eventDraw;
				break;
		}
		
		// Find the center to draw it at.
		int x = leftSide + (rightSide - leftSide) / 2 - eventToDraw.getWidth(this) / 2;
		int y = height / 2 - eventToDraw.getHeight(this) / 2;
		
		g.drawImage(eventToDraw, x, y, this);
	}

	/**
	 * Set the state to the indicated value.
	 *
	 * @param state
	 */
	public void setState(int state)
	{
		this.state = state;
		if(state == STATE_NONE)
		{
			setVisible(false);
		}
		else
		{
			setVisible(true);
		}
	}

	/**
	 * Based on the size of this object, load the images.
	 *
	 */
	private void loadImages()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		realCornerLL = resourceManager.getImage("bubble_ll.png");
		realCornerLR = resourceManager.getImage("bubble_lr.png");
		
		cornerUL = resourceManager.getImage("bubble_ul.png");
		cornerUR = resourceManager.getImage("bubble_ur.png");

		left = resourceManager.getImage("bubble_ml.png");
		right = resourceManager.getImage("bubble_mr.png");
		top = resourceManager.getImage("bubble_um.png");
		bottom = resourceManager.getImage("bubble_lm.png");
		
		leftTail = resourceManager.getImage("bubble_tail_l.png");
		rightTail = resourceManager.getImage("bubble_tail_r.png");
		
		eventCall = resourceManager.getImage("event_call.png");
		eventDraw = resourceManager.getImage("event_draw1.png");
	}
}
