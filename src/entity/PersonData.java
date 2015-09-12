package entity;

import java.awt.Point;

/**
 * 模拟一个人
 * @author cylong
 * @version 2015年9月11日 下午3:27:16
 */
public class PersonData extends Entity {

	/** 想要去的地方 */
	public Point end;

	public PersonData(int ID, Block block, Traffic traffic, Point end) {
		super(ID, block, traffic);
		this.end = end;
	}

	@Override
	public void run() {
		
	}

}
