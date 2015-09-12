package ui;

import java.awt.Color;

import javax.swing.JLabel;

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
		super(String.valueOf(personData.ID), JLabel.CENTER);
		this.personData = personData;
		this.setBackground(Color.GREEN);
		this.setOpaque(true);
		this.setLocation((personData.loc.x + 1) * Config.SIZE + Config.SIZE / 2, (personData.loc.y + 1) * Config.SIZE);
		this.setSize(Config.SIZE / 2, Config.SIZE);
	}
	
	public void move() {
		this.setLocation((personData.loc.x + 1) * Config.SIZE + Config.SIZE / 2, (personData.loc.y + 1) * Config.SIZE);
	}

}
