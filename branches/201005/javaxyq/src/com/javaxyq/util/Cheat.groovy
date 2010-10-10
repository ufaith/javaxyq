/*
 * JavaXYQ Source Code 
 * Cheat Cheat.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.util;

import com.javaxyq.core.GameMain;

/**
 * @author dewitt
 *
 */
public class Cheat {
	
	public static void process(String cmdLine) {
		def args = cmdLine.split();
		String cmd = args[0];
		def playerdata = GameMain.getPlayer().getData();
		switch(cmd) {
			case ~/\+(.*)/:
				String attr = cmd.substring(1);
				playerdata[attr] += args[1].toInteger();
				break;
			case ~/\-(.*)/:
				String attr = cmd.substring(1);
				playerdata[attr] -= args[1].toInteger();
				break;
			case ~/(.*)=/:
				String attr = cmd.substring(1);
				playerdata[attr] = args[1].toInteger();
				break;
		}
	}
}
