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

	public Traffic() {
		for(int i = 0; i < blocks.length; i++) {
			for(int j = 0; j < blocks[i].length; j++) {
				blocks[i][j] = new Block();
				blocks[i][j].x = j;
				blocks[i][j].y = i;
			}
		}
	}

	public Block[][] getBlocks() {
		return this.blocks;
	}

}
