package gjset.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import gjset.engine.GameController;

public class GjSetGUI
{
	private JFrame topFrame;
	private CardTable cardTable;
	private GameController	gameController;
	private MessageBar messageBar;
	private PlayerPanel playerPanel;

	public GjSetGUI(GameController gameController)
	{
		this.gameController = gameController;
		gameController.linkGUI(this);
		
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGUI(); 
            }
        });
	}
	
	private void createGUI()
	{
		topFrame = new JFrame("gJSet");
		topFrame.getContentPane().setLayout(new BorderLayout());
		topFrame.setSize(1024, 768);
		topFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createMenu();
		createCardTable();
		createPlayerPanel();
		createMessageBar();

		topFrame.pack();
		topFrame.setVisible(true);		
	}

	private void createMessageBar()
	{
		messageBar = new MessageBar();
		topFrame.add(messageBar, BorderLayout.NORTH);
	}

	private void createCardTable()
	{		
		cardTable = new CardTable(gameController);
		topFrame.add(cardTable, BorderLayout.CENTER);
	}
	
	private void createPlayerPanel()
	{
		playerPanel = new PlayerPanel(cardTable, gameController);
		topFrame.add(playerPanel, BorderLayout.SOUTH);
	}

	private void createMenu()
	{
		JMenuBar jMenuBar = new JMenuBar();
		topFrame.setJMenuBar(jMenuBar);

		//Build the file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		//Build the new game option
		JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
		fileMenu.add(newGameItem);
		newGameItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				gameController.newGame();
			}
		});
		
		//Build the exit item.
		JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
		fileMenu.add(exitItem);

		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				System.exit(0);
			}
		});
		
		//Add the file menu to the menu bar.
		jMenuBar.add(fileMenu);
	}

	public CardTable getCardTable()
	{
		return cardTable;
	}

	public MessageBar getMessageBar()
	{
		return messageBar;
	}
}
