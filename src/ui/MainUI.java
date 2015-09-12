package ui;

import ui.frame.Frame;

/**
 * @author cylong
 * @version 2015年9月12日 上午8:45:26
 */
public class MainUI {

	public MainUI() {
		Frame frame = new Frame();
		MainPanel panel = new MainPanel();
		frame.add(panel);

		frame.setVisible(true);
		frame.start();
	}
}
