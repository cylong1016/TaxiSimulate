package entity;

import java.awt.Point;

import config.Config;

/**
 * 出租车和人抽象出来的实体
 * @author cylong
 * @version 2015年9月12日 上午11:24:02
 */
public class Entity extends Thread {

	public int ID;

	/** 所在的坐标 */
	public Point loc;
	/** 街道 */
	protected Traffic traffic;

	protected Block[][] blocks;

	/** 当前所在方块 */
	protected Block curBlock;
	
	protected Area[][] areas;

	public Entity(int ID, Block curBlock, Traffic traffic) {
		super();
		this.ID = ID;
		this.curBlock = curBlock;
		this.loc = new Point(curBlock.x, curBlock.y);
		this.traffic = traffic;
		this.blocks = traffic.getBlocks();
		this.areas = traffic.getAreas();
	}

	public void moveUP() {
		loc.y--;
		loc.y = loc.y < 0 ? 0 : loc.y;
	}

	public void moveDown() {
		loc.y++;
		loc.y = loc.y > Config.NUM - 1 ? Config.NUM - 1 : loc.y;
	}

	public void moveLeft() {
		loc.x--;
		loc.x = loc.x < 0 ? 0 : loc.x;
	}

	public void moveRight() {
		loc.x++;
		loc.x = loc.x > Config.NUM - 1 ? Config.NUM - 1 : loc.x;
	}

	public Block getCurBlock() {
		return this.curBlock;
	}

}
