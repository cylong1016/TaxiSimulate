package ui;

import java.awt.Color;

import javax.swing.JLabel;

import util.Util;
import config.Config;
import entity.PersonData;


/**
 * 人模型
 * @author cylong
 * @version 2015年9月12日  下午3:18:28
 */
public class PersonModel extends JLabel {

	/** serialVersionUID */
	private static final long serialVersionUID = 6645796915105582988L;
	
	private PersonData personData;
	
	public PersonModel(PersonData personData) {
		this.personData = personData;
		this.setBackground(Color.GREEN);
		this.setOpaque(true);
		this.setLocation((personData.loc.x + 1) * Config.SIZE + Config.SIZE * 2 / 3, (personData.loc.y + 1) * Config.SIZE);
		this.setSize(Config.SIZE / 3, Config.SIZE);
		new Vanish().start();
	}
	
	class Vanish extends Thread {
		
		@Override
		public void run() {
			while(true) {
				Util.sleep(200);
				if(personData.geton) {
					PersonModel.this.setVisible(false);
				}
			}
		}
	}

}
