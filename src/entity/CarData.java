package entity;

import java.awt.Point;
import java.util.concurrent.LinkedBlockingQueue;

import config.Config;
import util.Util;

/**
 * 出租车模型
 * @author cylong
 * @version 2015年9月11日 下午3:45:46
 */
public class CarData extends Entity {

	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;

	/** 每个方向的反方向 */
	private static final int[] AGAINST = {2, 3, 0, 1};
	
	/** 汽车走每格需要的时间 */
	private long timePerGrid = 1000;

	/** 是否有人 */
	public boolean full = false;
	/** 有人时候的目的地 */
	private Point destination = new Point();
	/** 上一步的方向 */
	private int state = 1;

	/** 车总共走的路程 */
	public static int totalPassThroughGrid = 0;
	/** 载人走的格子数 */
	public static int effectPassThroughGrid = 0;

	public CarData(int ID, Block block, Traffic traffic) {
		super(ID, block, traffic);
	}

	@Override
	public void run() {
		while(true) {
			Util.sleep(timePerGrid);
			LinkedBlockingQueue<PersonData> person = curBlock.getPersonSet();
			if (person.size() != 0 && !full) {
				PersonData personData = person.poll(); // 删除乘客在路上的信息
				if(personData == null) {
					continue;
				}
				personData.geton = true; // 乘客上车
				PersonData.addPeopleWithCar();
				destination = personData.end;
				this.full = true; // 车满
				toDestination(); // 车上有人，按照一定路线走
			} else {
				randomRun(); // 车上没人，随便走
			}

		}
	}

	private void toDestination() {
		if(loc.x - destination.x > 0) {
			while(loc.x != destination.x) {
				move(LEFT);
				addEffectPassThroughGrid();
				Util.sleep(timePerGrid);
			}
		} else if(loc.x - destination.x < 0) {
			while(loc.x != destination.x) {
				move(RIGHT);
				addEffectPassThroughGrid();
				Util.sleep(timePerGrid);
			}
		}
		
		if(loc.y - destination.y > 0) {
			while(loc.y != destination.y) {
				move(UP);
				addEffectPassThroughGrid();
				Util.sleep(timePerGrid);
			}
		} else if(loc.y - destination.y < 0) {
			while(loc.y != destination.y) {
				move(DOWN);
				addEffectPassThroughGrid();
				Util.sleep(timePerGrid);
			}
		}
		
		this.full = false; // 送完人了
	}

	/**
	 * 车上没人的时候随机走
	 * @author cylong
	 * @version 2015年9月13日 上午8:46:16
	 */
	private void randomRun() {
		int direction = state;
		int isMargin = isMargin();
		if (Math.random() >= 0.5 || isMargin != -1) { // 有50%的概率或者在边缘就重新定义方向
			if (isMargin != - 1) {
				direction = AGAINST[isMargin]; //  在边缘就直接取反方向，简单粗暴
			} else {
				direction = (int)(Math.random() * 4);
				while(AGAINST[direction] == state) { // 如果和刚刚的方向相反就重新选择方向
					direction = (int)(Math.random() * 4);
				}
			}
		}
		move(direction);
		state = direction;

		// 计算出当前所在的块，把出租车标记在上面
		try {
			curBlock = blocks[loc.y][loc.x];
		} catch (Exception e) {
			System.out.println("ID:" + ID);
			System.out.println(loc.y);
			System.out.println(loc.x);
		}
		curBlock.getCarSet().add(this);
	}
	
	/**
	 * 车是否在边缘
	 * @return 在哪个边缘 。 -1代表车不在边缘
	 * @author cylong
	 * @version 2015年9月13日  下午1:51:19
	 */
	public int isMargin() {
		if (loc.y <= 0) {
			return UP;
		}

		if (loc.y >= Config.NUM - 1) {
			return DOWN;
		}

		if (loc.x <= 0) {
			return LEFT;
		}

		if (loc.x >= Config.NUM - 1) {
			return RIGHT;
		}

		return -1;
	}

	private void move(int direction) {
		curBlock.getCarSet().remove(this); // 离开，从当前的块中移除汽车
		addTotalPassThroughGrid();
		switch(direction) {
		case UP:
			moveUP();
			break;
		case RIGHT:
			moveRight();
			break;
		case DOWN:
			moveDown();
			break;
		case LEFT:
			moveLeft();
			break;
		}
	}

	private static synchronized void addTotalPassThroughGrid() {
		totalPassThroughGrid++;
	}

	private static synchronized void addEffectPassThroughGrid() {
		effectPassThroughGrid++;
	}
}
