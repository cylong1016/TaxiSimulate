package entity;

import java.awt.Point;

import util.Util;

/**
 * 模拟一个人
 * @author cylong
 * @version 2015年9月11日 下午3:27:16
 */
public class PersonData extends Entity {

	/** 想要去的地方 */
	public Point end;
	/** 是否上车 */
	public boolean geton;
	/** 等待时间 秒 */
	private int waitTime = 15;

	/** 乘上车的人 */
	public static int PEOPLE_WITH_CAR = 0;

	public PersonData(int ID, Block curBlock, Traffic traffic, Point end) {
		super(ID, curBlock, traffic);
		this.end = end;
	}
	
	public static synchronized void addPeopleWithCar() {
		PEOPLE_WITH_CAR++; // 坐上车的人数 +1
	}

	@Override
	public void run() {
		while(true) {
			Util.sleep(1000);
			if(geton) { // 15秒内上车了
				break;
			}
			waitTime--;
			if (waitTime <= 0) {
				geton = true; // 人已经没有耐心等了，坐其他车走掉了
				curBlock.getPersonSet().remove(PersonData.this); // 街道上的人移除
				break;
			}
		}
	}

}
