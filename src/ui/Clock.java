package ui;

import java.awt.Font;

import javax.swing.JLabel;

import util.Util;
import config.Config;

/**
 * 显示一下时间
 * @author cylong
 * @version 2015年9月13日 上午11:34:29
 */
public class Clock extends JLabel {

	/** serialVersionUID */
	private static final long serialVersionUID = 4148081753365278883L;
	
	private Font font = new Font("微软雅黑", Font.PLAIN, 20);
	
	public Clock() {
		super("时间: 0", JLabel.CENTER);
		this.setSize(100, 50);
		this.setLocation(750, 100);
		this.setFont(font);
		new Run().start();
	}

	class Run extends Thread {

		int temp = 0;

		@Override
		public void run() {
			while((temp++) != Config.TOTAL_TIME) {
				Util.sleep(1000);
				setText("时间: " + String.valueOf(temp));
			}
		}
	}

}
