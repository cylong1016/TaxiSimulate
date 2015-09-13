package entity;

import java.awt.Point;
import java.util.concurrent.LinkedBlockingQueue;

import util.Util;
import config.Config;

/**
 * 模拟一个人
 * @author cylong
 * @version 2015年9月11日 下午3:27:16
 */
public class PersonData extends Entity {

	/** 想要去的地方 */
	public Point end;
	/** 是否上车 */
	private boolean geton;
	/** 等待时间 秒 */
	private int waitTime = Config.WAIT_TIME;

	/** 打车的概率 */
	private double probability = 0.38;
	/** 是否在等待车到来 */
	private boolean waitCar;

	/** 乘上车的人 */
	public static int PEOPLE_WITH_CAR = 0;

	public PersonData(int ID, Block curBlock, Traffic traffic, Point end) {
		super(ID, curBlock, traffic);
		this.end = end;
		takeATaxi();
	}

	private void takeATaxi() {
		double temp = Math.random();
		if (temp < probability) { // 打车
			for(int i = 0; i < Config.NUM; i++) {
				for(int j = (loc.x - i < 0 ? 0 : loc.x - i); j < (loc.x + i > Config.NUM - 1 ? Config.NUM - 1 : loc.x + i); j++) {
					for(int k = (loc.y - i < 0 ? 0 : loc.y - i); k < (loc.y + i > Config.NUM - 1 ? Config.NUM - 1 : loc.y + i); k++) {
						Block tmpBlock = blocks[j][k];
						LinkedBlockingQueue<CarData> carSet = tmpBlock.getCarSet();
						CarData carData = carSet.peek();
						if (carData == null) {
							continue;
						} else {
							for(CarData data : carSet) {
								if(!data.full && data.getCalledBy() == null) {
									this.waitCar = true;
									data.setCalledBy(this);
									return;
								}
							}
						}
					}
				}

			}
		}
	}

	public static synchronized void addPeopleWithCar() {
		PEOPLE_WITH_CAR++; // 坐上车的人数 +1
	}

	public boolean isGeton() {
		return this.geton;
	}

	public void setGeton(boolean geton) {
		// 此人所在区域中的乘车人数+1
		int gridPerArea = Config.NUM / Config.AREA_NUM;
		areas[loc.x / gridPerArea][loc.y / gridPerArea].addPersonWithCar();
		this.geton = geton;
	}
	
	public boolean isWaitCar() {
		return this.waitCar;
	}
	
	public void setWaitCar(boolean waitCar) {
		this.waitCar = waitCar;
	}

	@Override
	public void run() {
		while(true) {
			Util.sleep(1000);
			if (geton) { // 上车了
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
