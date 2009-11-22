/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-15
 * http://kylixs.blog.163.com
 * kylixs@qq.com
 */

/**
 * @author dewitt
 * @date 2009-11-15 create
 */
public class FindNearest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] table= new int[] {0,10,20,30};
		int input = 4;
		int val = Integer.MAX_VALUE;
		int result = 0;
		for (int i = 0; i < table.length; i++) {
			int v2 = Math.abs(table[i]-input);
			if(v2 > val) {
				result = table[i-1];
				break;
			}
			val = v2;
		}
		System.out.println("最接近的数字是："+result);
	}

}
