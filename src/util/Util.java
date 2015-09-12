package util;

/**
 * 一些通用的方法
 * @author cylong
 * @version Nov 11, 2014 10:37:05 PM
 */
public class Util {

	/**
	 * 睡眠方法
	 * @param time 睡眠时间（毫秒）
	 * @since 2014 / 4 / 10 1 : 16 AM
	 */
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
