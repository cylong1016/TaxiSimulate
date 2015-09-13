package ui;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import util.Util;
import config.Config;
import entity.Block;
import entity.CarData;
import entity.PersonData;
import entity.Traffic;

/**
 * 显示街道图
 * @author cylong
 * @version 2015年9月12日 上午8:51:45
 */
public class MainPanel extends JPanel {

	/** serialVersionUID */
	private static final long serialVersionUID = 8920997326735022832L;

	private Traffic traffic = new Traffic();
	private int size = Config.SIZE;
	private Block[][] blocks;
	public static int carNum = Config.CAR_NUM;
	public static int personNum = Config.PERSON_NUM;
	private CarModel[] carsModel = new CarModel[carNum];
	private PersonModel[] personModel = new PersonModel[personNum];

	public MainPanel() {
		this.setLayout(null);
		blocks = traffic.getBlocks();
		addCars();
		addPerson();
		new Run().start();
	}

	/**
	 * 添加出租车
	 * @author cylong
	 * @version 2015年9月12日 下午3:24:53
	 */
	private void addCars() {
		for(int i = 0; i < carNum; i++) {
			int x = (int)(Math.random() * (Config.NUM - 2)) + 1;
			int y = (int)(Math.random() * (Config.NUM - 2)) + 1;
			Block block = blocks[x][y];
			CarData carData = new CarData(i, block, traffic);
			carData.start();
			block.getCarSet().add(carData);

			carsModel[i] = new CarModel(carData);
			this.add(carsModel[i]);
		}
	}

	/**
	 * 添加乘客
	 * @author cylong
	 * @version 2015年9月12日 下午3:25:37
	 */
	private void addPerson() {
		new Thread() {
			public void run() {
				for(int i = 0; i < personNum; i++) {
					Util.sleep(250);
					int startX = (int)(Math.random() * Config.NUM);
					int startY = (int)(Math.random() * Config.NUM);
					Block block = blocks[startX][startY];
					int endX = (int)(Math.random() * Config.NUM);
					int endY = (int)(Math.random() * Config.NUM);
					PersonData personData = new PersonData(i, block, traffic, new Point(endX, endY));
					personData.start();
					block.getPersonSet().add(personData);
					personModel[i] = new PersonModel(personData);
					MainPanel.this.add(personModel[i]);
				}
			}
		}.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(int i = 0; i < blocks.length; i++) {
			for(int j = 0; j < blocks[i].length; j++) {
				Block block = blocks[i][j];
				g.drawRect((block.x + 1) * size, (block.y + 1) * size, size, size);
			}
		}
	}

	class Run extends Thread {

		@Override
		public void run() {
			while(true) {
				Util.sleep(100);
				for(int i = 0; i < carsModel.length; i++) {
					carsModel[i].move();
				}
			}
		}
	}

}
