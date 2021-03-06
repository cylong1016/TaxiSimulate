package ui;

import java.awt.Color;

import javax.swing.JLabel;

import config.Config;
import entity.CarData;

/**
 * 出租车模型
 * @author cylong
 * @version 2015年9月12日 下午1:07:07
 */
public class CarModel extends JLabel {

	/** serialVersionUID */
	private static final long serialVersionUID = 918012164908423362L;

	private CarData carData;

	public CarModel(CarData carData) {
		this.carData = carData;
		this.setBackground(Color.BLUE);
		this.setOpaque(true);
		this.setLocation((carData.loc.x + 1) * Config.SIZE, (carData.loc.y + 1) * Config.SIZE);
		this.setSize(Config.SIZE / 3, Config.SIZE);
	}

	public void move() {
		if(carData.full) {
			this.setBackground(Color.RED);
			this.setLocation((carData.loc.x + 1) * Config.SIZE + Config.SIZE / 3, (carData.loc.y + 1) * Config.SIZE);
		} else {
			this.setBackground(Color.BLUE);
			this.setLocation((carData.loc.x + 1) * Config.SIZE, (carData.loc.y + 1) * Config.SIZE);
		}
	}

}
