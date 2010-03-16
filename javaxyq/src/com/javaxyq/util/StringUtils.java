/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-8
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.util;

/**
 * @author Administrator
 * @date 2010-3-8 create
 */
public class StringUtils {

	public static String afterLast(String str,String delimiter) {
		int p = str.lastIndexOf(delimiter);
		return (p>=0&&p<str.length()-1)?str.substring(p+delimiter.length()):null;
	}
}
