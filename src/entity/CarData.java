package entity;

import java.util.ArrayList;

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

	/** 是否有人 */
	public boolean full = false;
	/** 上一步的方向 */
	private int state = 1;

	public CarData(int ID, Block block, Traffic traffic) {
		super(ID, block, traffic);
	}

	@Override
	public void run() {
		while(true) {
			Util.sleep(500);
			int direction = state;
			if(Math.random() >= 0.5 || isMargin()) { // 有50%的概率或者在边缘就重新定义方向
				if(isMargin()) {
					direction = AGAINST[direction]; //  在边缘就直接取反方向，简单粗暴
					move(direction);
					Util.sleep(500);
				} else {
					direction = (int)(Math.random() * 4);
					while(AGAINST[direction] == state) { // 如果和刚刚的方向相反就重新选择方向
						direction = (int)(Math.random() * 4);
					}
				}
			}
			move(direction);
			state = direction;
		}
	}

	private void move(int direction) {
		curBlock.getCarSet().remove(this); // 离开，从当前的块中移除汽车
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
		curBlock = blocks[loc.y][loc.x];
		ArrayList<PersonData> person = curBlock.getPersonSet();
		if(person.size() != 0) {
			PersonData personData = person.get(0);
			System.out.println("车 " + this.ID + " 人 " + personData.ID);
		}
		curBlock.getCarSet().add(this);
	}
}
