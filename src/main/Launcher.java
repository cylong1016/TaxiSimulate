package main;

import ui.MainUI;
import util.Util;
import config.Config;
import entity.CarData;
import entity.PersonData;

/**
 * 模拟出租车系统
 * @author cylong
 * @version 2015年9月12日 上午8:43:41
 */
public class Launcher {

	public static void main(String[] args) {
		new MainUI();
		new Time().start();
	}

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

		System.out.println("出租车数：" + Config.CAR_NUM
				+ " 总路程：" + CarData.totalPassThroughGrid 
				+ " 载人路程：" + CarData.effectPassThroughGrid 
				+ " 空载率：" + carWithoutPerson);
		System.out.println("总人数：" + Config.PERSON_NUM
				+ " 被接到的人数：" + PersonData.PEOPLE_WITH_CAR
				+ " 人被接到概率:" + personWithCarPercent);

	}
}