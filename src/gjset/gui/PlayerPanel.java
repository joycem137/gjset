package gjset.gui;

import gjset.engine.GameController;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class PlayerPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3670300153110634539L;

	private Image				offScreenImage;
	private Graphics2D			offScreenGraphics;

	private CardTable			table;
	private GameController		gameController;

	private static final Color	panelColor			= new Color(204, 255, 255);

	public PlayerPanel(CardTable table, final GameController gameController)
	{
		super();

		this.table = table;
		this.gameController = gameController;

		//Set the size of the card table.
		setPreferredSize(new Dimension(720, 200));
		setMinimumSize(new Dimension(720, 200));
		setMaximumSize(new Dimension(720, 200));

		//Handle resizing the player panel.
		addComponentListener(new ComponentAdapter()
		{
			public void componentResized( ComponentEvent e )
			{
				JComponent component = (JComponent) e.getSource();

				int width = component.getWidth();
				int height = component.getHeight();

				offScreenImage = component.createImage(width, height);
				offScreenGraphics = (Graphics2D) offScreenImage.getGraphics();
				drawPanel();
			}
		});

		setLayout(new FlowLayout());

		JButton setButton = new JButton(new AbstractAction("No more sets.")
		{
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1742911717343411450L;

			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				gameController.noMoreSets();
			}
		});

		//Set the button's size.
		setButton.setPreferredSize(new Dimension(120, 30));
		setButton.setMaximumSize(new Dimension(120, 30));
		setButton.setMinimumSize(new Dimension(120, 30));

		//Add the button to the screen.
		add(setButton);
	}

	public void drawPanel()
	{
		//Draw the background
		offScreenGraphics.setColor(table.getBackground());
		offScreenGraphics.fillRect(0, 0, getWidth(), getHeight());

		//Draw the bottom half of the panel.
		offScreenGraphics.setColor(panelColor);
		offScreenGraphics.fillRoundRect(15, getHeight() - 80, getWidth() - 30, 50, 15, 15);

		//Draw the bottom of the border of the panel.
		offScreenGraphics.setColor(Color.black);
		offScreenGraphics.setStroke(new BasicStroke(3.0f));
		offScreenGraphics.drawRoundRect(15, 0, getWidth() - 30, getHeight() - 30, 15, 15);

		//Draw the top half of the panel.
		offScreenGraphics.setColor(panelColor);
		offScreenGraphics.fillRect(15, 0, getWidth() - 30, getHeight() - 60);

		//Draw the border of the bottom of the panel.
		offScreenGraphics.setColor(Color.black);
		offScreenGraphics.setStroke(new BasicStroke(3.0f));
		offScreenGraphics.drawLine(15, 0, 15, getHeight() - 60);
		offScreenGraphics.drawLine(getWidth() - 15, 0, getWidth() - 15, getHeight() - 60);

		//Flush the image.
		offScreenImage.flush();

		//Repaint the screen.
		repaint();
	}

	public void paintComponent( Graphics g )
	{
		super.paintComponent(g);
		g.drawImage(offScreenImage, 0, 0, getWidth(), getHeight(), this);
	}
}
