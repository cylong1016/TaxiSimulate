package ui;

import java.awt.Font;
import java.awt.TextArea;

import util.Util;
import config.Config;
import entity.Area;
import entity.CarData;
import entity.PersonData;

/**
 * 显示最终信息
 * @author cylong
 * @version 2015年9月13日 下午10:23:13
 */
public class ShowInfo extends TextArea {

	/** serialVersionUID */
	private static final long serialVersionUID = -2030788354591740825L;

	private Area[][] areas;
	
	private Font font = new Font("微软雅黑", Font.PLAIN, 12);

	public ShowInfo(Area[][] areas) {
		this.areas = areas;
		this.setEditable(false);
		this.setSize(200, 500);
		this.setLocation(677, 136);
		this.setFont(font);
		new Time().start();
	}

	/***
	 * 记录多长时间的数据
	 * @author cylong
	 * @version 2015年9月13日 上午9:40:05
	 */
	class Time extends Thread {

		@Override
		public void run() {
			int temp = 0;
			while((temp++) != Config.TOTAL_TIME) {
				Util.sleep(1000);
			}

			double personWithCarPercent = PersonData.PEOPLE_WITH_CAR * 1.0 / Config.PERSON_NUM;
			double carWithoutPerson = 1 - CarData.effectPassThroughGrid * 1.0 / CarData.totalPassThroughGrid;
			
			append("出租车数：" + Config.CAR_NUM + "\r\n");
			append("总路程：" + CarData.totalPassThroughGrid + "\r\n");
			append("载人路程：" + CarData.effectPassThroughGrid + "\r\n");
			append("空载率：" + "\r\n" + carWithoutPerson + "\r\n");
			append("\r\n");
			append("总人数：" + Config.PERSON_NUM + "\r\n");
			append("被接到的人数：" + PersonData.PEOPLE_WITH_CAR + "\r\n");
			append("人被接到概率:" + "\r\n" + personWithCarPercent + "\r\n");
			append("\r\n");
			showAreaInfo(5, 5);
			showAreaInfo(7, 7);
			showAreaInfo(9, 9);
		}
		
		private void showAreaInfo(int x, int y) {
			double personPerAreaWithCarPercent = areas[x][y].getPersonWithCar() * 1.0 / areas[x][y].getTotalPerson();
			append("[" + x + ", " + y + "] 总人数：" + areas[x][y].getTotalPerson() + "\r\n");
			append("[" + x + ", " + y + "] 人被接到数：" + areas[x][y].getPersonWithCar() + "\r\n");
			append("[" + x + ", " + y + "] 人被接到概率：" + "\r\n" + personPerAreaWithCarPercent + "\r\n");
			append("\r\n");
		}
	}

}
