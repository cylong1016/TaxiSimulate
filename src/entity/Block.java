package entity;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 街道图中的一个方块
 * @author cylong
 * @version 2015年9月12日 上午9:47:25
 */
public class Block {

	/** 方块中心坐标 */
	public int x;
	public int y;

	private ArrayList<CarData> carSet = new ArrayList<CarData>();
	private LinkedBlockingQueue<PersonData> personSet = new LinkedBlockingQueue<PersonData>(100);

	public ArrayList<CarData> getCarSet() {
		return this.carSet;
	}

	public LinkedBlockingQueue<PersonData> getPersonSet() {
		return this.personSet;
	}

}
