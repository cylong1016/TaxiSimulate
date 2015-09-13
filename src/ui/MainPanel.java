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
	
	private Clock clock;
	
	private double [] distributePercent = {0.015, 0.09, 0.115, 0.13, 0.15,
	                                       0.15, 0.13, 0.115, 0.09, 0.015};
	/** 整个城市横竖分成多少个区域 */
	private int areaNum = 10;
	private int gridPerArea = Config.NUM / areaNum;
	private double[][] areaPercent = new double[areaNum][areaNum];
	private double[] linearPercent = new double[areaNum * areaNum];

	public MainPanel() {
		this.setLayout(null);
		blocks = traffic.getBlocks();
		clock = new Clock();
		this.add(clock);
		
		for(int i = 0; i < areaPercent.length; i++) {
			for(int j = 0; j < areaPercent[i].length; j++) {
				areaPercent[i][j] = distributePercent[i] * distributePercent[j];
				linearPercent[i * areaNum + j] = areaPercent[i][j];
			}
		}
		
		for(int i = 1; i < linearPercent.length; i++) {
			linearPercent[i] = linearPercent[i] + linearPercent[i - 1];
		}
		
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
			int[] areaIndex = getAreaIndex();
			int x = areaIndex[0];
			int y = areaIndex[1];
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
	 * 1. 将整体分成若干块，人落在某一块的概率不同，生成一个人的时候通过概率计算他落在哪一块，然后在那个块里随机选择位置
	 * 2. 人的终点站生成也是越远概率越小
	 * @author cylong
	 * @version 2015年9月12日 下午3:25:37
	 */
	private void addPerson() {
		new Thread() {
			public void run() {
				
				for(int i = 0; i < personNum; i++) {
//					if(i % ((personNum / Config.TOTAL_TIME) / 10) == 0) {
//						Util.sleep(25); // 每0.1秒出现一些人
//					}
					Util.sleep(6);
					System.out.println(i);
					int[] areaIndex = getAreaIndex();
					// 整体中人起始坐标
					int startX = areaIndex[0];
					int startY = areaIndex[1];
					Block block = blocks[startX][startY];
					int[] endIndex = getEndIndex(startX, startY);
					PersonData personData = new PersonData(i, block, traffic, new Point(endIndex[0], endIndex[1]));
					personData.start();
					block.getPersonSet().add(personData);
					personModel[i] = new PersonModel(personData);
					MainPanel.this.add(personModel[i]);
				}
			}

		}.start();
	}
	
	/***
	 * 根据起始坐标获得终止坐标
	 * @return 数组两个元素分别是x、y
	 * @author cylong
	 * @version 2015年9月13日  下午4:27:43
	 */
	private int[] getEndIndex(int startX, int startY) {
		double[] endAreaPercent = {0.07, 0.2, 0.24, 0.29, 0.2};
		double[] linearEndPercent = new double[endAreaPercent.length];
		linearEndPercent[0] = endAreaPercent[0];
		for(int i = 1; i < linearEndPercent.length; i++) {
			linearEndPercent[i] = linearEndPercent[i - 1] + endAreaPercent[i];
		}
		
		double temp = Math.random();
		int distance = 0; // 计算总偏移距离
		for(int i = 0; i < linearEndPercent.length; i++) {
			if(temp < linearEndPercent[i]) {
				distance = i;
				break;
			}
		}
		
		// 有0.1的概率距离是5以上【具体多少也是随机的】
		if(distance == endAreaPercent.length - 1) {
			distance += (int)(Math.random() * 5);
		}
		
		int xOffset = (int)(Math.random() * (distance + 1));
		int yOffset = distance - xOffset;
		int endX = 0;
		int endY = 0;
		if(Math.random() < 0.5) {
			endX = startX + xOffset * gridPerArea;
			endX = endX > Config.NUM - 1 ? Config.NUM - 1 : endX;
		} else {
			endX = startX - xOffset * gridPerArea;
			endX = endX < 0 ? 0 : endX;
		}
		if(Math.random() < 0.5) {
			endY = startY + yOffset * gridPerArea;
			endY = endY > Config.NUM - 1 ? Config.NUM - 1 : endY;
		} else {
			endY = startY - yOffset * gridPerArea;
			endY = endY < 0 ? 0 : endY;
		}
		return new int[]{endX, endY};
	}
	
	/**
	 * 获得起始坐标
	 * @return 数组两个元素分别是x、y
	 * @author cylong
	 * @version 2015年9月13日  下午4:27:30
	 */
	private int[] getAreaIndex() {
		double temp = Math.random();
		int t = 0;
		for(int j = 0; j < linearPercent.length; j++) {
			if(temp < linearPercent[j]) {
				t = j;
				break;
			}
		}
		// 落在哪个区域
		int x = t % areaNum;
		int y = t / areaNum;
		
		// 相对整体的起始坐标
		int startX = (int)((Math.random() * gridPerArea) + x * gridPerArea);
		int startY = (int)((Math.random() * gridPerArea) + y * gridPerArea);
		return new int[]{startX, startY};
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
