package gjset;

import gjset.engine.GameController;
import gjset.gui.GjSetGUI;


public class GjSetMain{
	public static void main(String[] args) {
		GameController gc = new GameController();
		GjSetGUI gui = new GjSetGUI(gc);
	}

}
