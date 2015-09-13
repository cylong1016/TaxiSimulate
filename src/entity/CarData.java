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
	
	/** 初始的坐标 */
	public Point originaLoc;

	/** 每个方向的反方向 */
	private static final int[] AGAINST = {2, 3, 0, 1};
	
	/** 汽车走每格需要的时间 */
	private long timePerGrid = 1000;

	/** 是否有人 */
	public boolean full = false;
	/** 上一步的方向 */
	private int state = 1;

	/** 车总共走的路程 */
	public static int totalPassThroughGrid = 0;
	/** 载人走的格子数 */
	public static int effectPassThroughGrid = 0;
	
	public CarData(int ID, Block block, Traffic traffic) {
		super(ID, block, traffic);
		this.originaLoc = new Point(curBlock.x, curBlock.y);
	}

	@Override
	public void run() {
		while(true) {
			Util.sleep(timePerGrid);
			PersonData personData = checkPerson();
			if (personData == null) {
				randomRun(); // 车上没人，随便走
			}

		}
	}
	
	/**
	 * 判断当前位置有没有人车里没人就可以接
	 * @return PersonData 或者 null
	 * @author cylong
	 * @version 2015年9月13日  下午6:48:46
	 */
	private PersonData checkPerson() {
		LinkedBlockingQueue<PersonData> person = curBlock.getPersonSet();
		if (person.size() != 0 && !full) {
			PersonData personData = person.poll(); // 删除乘客在路上的信息
			if(personData == null) {
				return null;
			}
			personData.setGeton(true);; // 乘客上车
			PersonData.addPeopleWithCar();
			this.full = true; // 车满
			toDestination(personData.end); // 车上有人，按照一定路线走
		}
		return null;
	}
	
	/**
	 * 出租车送人到目的地
	 * @author cylong
	 * @version 2015年9月13日  下午6:28:50
	 */
	private void toDestination(Point destination) {
		goToDestination(destination);
		this.full = false; // 送完人了
		goBack(); // 返回
	}

	/**
	 * 送完人后返回
	 * @author cylong
	 * @version 2015年9月13日  下午6:43:00
	 */
	private void goBack() {
		goToDestination(originaLoc);
	}
	
	private void goToDestination(Point destination) {
		if(loc.x - destination.x > 0) {
			while(loc.x != destination.x) {
				if(checkPerson() != null) {
					return;
				}
				move(LEFT);
				if(full) {
					addEffectPassThroughGrid();
				}
				Util.sleep(timePerGrid);
			}
		} else if(loc.x - destination.x < 0) {
			while(loc.x != destination.x) {
				if(checkPerson() != null) {
					return;
				}
				move(RIGHT);
				if(full) {
					addEffectPassThroughGrid();
				}
				Util.sleep(timePerGrid);
			}
		}
		
		if(loc.y - destination.y > 0) {
			while(loc.y != destination.y) {
				if(checkPerson() != null) {
					return;
				}
				move(UP);
				if(full) {
					addEffectPassThroughGrid();
				}
				Util.sleep(timePerGrid);
			}
		} else if(loc.y - destination.y < 0) {
			while(loc.y != destination.y) {
				if(checkPerson() != null) {
					return;
				}
				move(DOWN);
				if(full) {
					addEffectPassThroughGrid();
				}
				Util.sleep(timePerGrid);
			}
		}
	}

	/**
	 * 车上没人的时候随机走
	 * @author cylong
	 * @version 2015年9月13日 上午8:46:16
	 */
	private void randomRun() {
		int direction = getDirection();
		move(direction);
		state = direction;
	}
	
	/**
	 * 根据初始位置修正车的方向，使车在一个范围内运动
	 * @return 返回车的方向或者是-1
	 * @author cylong
	 * @version 2015年9月13日  下午5:29:13
	 */
	private int correctDirection() {
		int range = 9;
		int xOffset = Math.abs(loc.x - originaLoc.x);
		int yOffset = Math.abs(loc.y - originaLoc.y);
		if(xOffset + yOffset <= range) { // 未超出范围
			return -1;
		}
		
		// 超出范围了
		if(loc.x == originaLoc.x) {
			if(loc.y > originaLoc.y) {
				return UP;
			} else if (loc.y < originaLoc.y) {
				return DOWN;
			}
		} else if(loc.y == originaLoc.y) {
			if(loc.x > originaLoc.x) {
				return LEFT;
			} else if (loc.x < originaLoc.x) {
				return RIGHT;
			}
		}
		
		// true 水平走， false 垂直走
		boolean hv = false;
		
		if(Math.random() < 0.5) {
			hv = !hv;
		}
		
		if(hv) {
			if(loc.x > originaLoc.x) {
				return LEFT;
			} else if (loc.x < originaLoc.x) {
				return RIGHT;
			}
		} else {
			if(loc.y > originaLoc.y) {
				return UP;
			} else if (loc.y < originaLoc.y) {
				return DOWN;
			}
		}
		
		return 0;
		
	}

	/**
	 * 获得车的下一步方向
	 * @return 方向
	 * @author cylong
	 * @version 2015年9月13日  下午5:20:49
	 */
	private int getDirection() {
		
		int direction = correctDirection(); // 超出范围就纠正方向
		if(direction != -1) {
			return direction;
		}
		
		direction = state;
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
		return direction;
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

	private static synchronized void addTotalPassThroughGrid() {
		totalPassThroughGrid++;
	}

	private static synchronized void addEffectPassThroughGrid() {
		effectPassThroughGrid++;
	}
}
