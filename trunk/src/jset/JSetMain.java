package jset;

import jset.engine.GameController;
import jset.gui.JSetGUI;


public class JSetMain{
	public static void main(String[] args) {
		GameController gc = new GameController();
		JSetGUI gui = new JSetGUI(gc);
	}

}
