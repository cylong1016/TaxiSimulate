package entity;


/**
 * 将整个地图分成很多个区域
 * @author cylong
 * @version 2015年9月13日  下午9:49:04
 */
public class Area {
	private int totalPerson;
	private int personWithCar;
	
	public void addTotalPerson() {
		totalPerson++;
	}
	
	public void addPersonWithCar() {
		personWithCar++;
	}
	
	public int getTotalPerson() {
		return this.totalPerson;
	}
	
	public int getPersonWithCar() {
		return this.personWithCar;
	}
	
	
}
