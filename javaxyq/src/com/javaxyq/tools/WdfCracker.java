/**
 * 
 */
package com.javaxyq.tools;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import com.javaxyq.util.HashUtil;

/**
 * @author dewitt
 * 
 */
public class WdfCracker {
	
	public static void crack(String[]formats,Object[] values) {
		
	}

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		crackSound();
		//crackWzife();
	}
	
	
	private static void crackWzife() throws FileNotFoundException {
		WdfFile wdf = new WdfFile("E:/Game/√Œª√Œ˜”Œ/wzife.wd1");
		PrintWriter pw = new PrintWriter("resources/names/test1.lst");
		String[] formats = new String[] { "magic\\normal\\%04d.tcp","magic\\small\\%04d.tcp" };
		int max = 9999,iCount = 0;
		for(int i=0;i<max;i++) {
			String res = String.format(formats[0], i);
			long id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
			res = String.format(formats[1], i);
			id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
		}
		pw.close();
		System.out.println();
		System.out.printf("π≤%d∏ˆ£¨∆•≈‰%d∏ˆ",wdf.getFileNodeCount(),iCount);
	}

	private static void crackSound() throws FileNotFoundException {
		String[] list1 = new String[] { "attack", "magic", "hit", "defend", "die", "rusha", "rushb", "guard", "±Ø…À",
				"∑¢≈≠", "«◊Ω¸", "ŒËµ∏", "–›œ¢", "’–∫Ù" };
		WdfFile wdf = new WdfFile("E:/Game/√Œª√Œ˜”Œ/sound1.wdf");
		PrintWriter pw = new PrintWriter("resources/names/test.lst");
		String[] formats = new String[] { "char\\%04d\\%s.wav", "magic\\%04d.wav","scene\\eff%04d.wav" };
		System.out.println("--------- start crack -----------");
		int iCount = 0;
		for (int i = 1; i < 6000; i++) {
			for (int n = 0; n < list1.length; n++) {
				String action = list1[n];
				//∆•≈‰1
				String res = String.format(formats[0], i, action);
				long id = HashUtil.stringToId(res);
				if (wdf.findNode(id) != null) {
					System.out.println(res);
					pw.println(res);
					iCount ++;
				}
			}
			//∆•≈‰2
			String res = String.format(formats[1], i);
			long id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
			//∆•≈‰3
			res = String.format(formats[2], i);
			id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
		}
		pw.close();
		System.out.println();
		System.out.printf("π≤%d∏ˆ£¨∆•≈‰%d∏ˆ",wdf.getFileNodeCount(),iCount);
	}

}
