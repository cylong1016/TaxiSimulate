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

	/** 多少人没有乘上车 */
	public static int leave = 0;

	public PersonData(int ID, Block block, Traffic traffic, Point end) {
		super(ID, block, traffic);
		this.end = end;
	}
	
	private synchronized void addLeave() {
		leave++; // 未坐上车的人数 +1
	}

	@Override
	public void run() {
		while(true) {
			Util.sleep(1000);
			waitTime--;
			if (waitTime <= 0) {
				geton = true; // 人已经没有耐心等了，坐其他车走掉了
				addLeave();
			}
		}
	}

}
