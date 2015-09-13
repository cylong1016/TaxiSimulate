package entity;

import config.Config;

/**
 * 街道图
 * @author cylong
 * @version 2015年9月11日 下午6:12:54
 */
public class Traffic {

	/** 方块矩阵 */
	private Block[][] blocks = new Block[Config.NUM][Config.NUM];
	/** 整个城市横竖分成多少个区域 */
	private int areaNum = Config.AREA_NUM;
	private Area[][] areas = new Area[areaNum][areaNum];

	public Traffic() {
		for(int i = 0; i < blocks.length; i++) {
			for(int j = 0; j < blocks[i].length; j++) {
				blocks[i][j] = new Block();
				blocks[i][j].x = j;
				blocks[i][j].y = i;
			}
		}

		for(int i = 0; i < areas.length; i++) {
			for(int j = 0; j < areas[i].length; j++) {
				areas[i][j] = new Area();
			}
		}
	}

	public Block[][] getBlocks() {
		return this.blocks;
	}

	public Area[][] getAreas() {
		return areas;
	}

	public int getAreaNum() {
		return areaNum;
	}

}
