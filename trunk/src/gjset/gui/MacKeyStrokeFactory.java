package gjset.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class MacKeyStrokeFactory implements KeyStrokeFactory
{

	@Override
	public KeyStroke getNewGameAcceleratorKeyStroke()
	{	
		return KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK);
	}

	@Override
	public KeyStroke getExitGameAcceleratorKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.META_MASK);
	}

}
