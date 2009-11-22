/*
 * JavaXYQ Source Code 
 * Item Item.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://kylixs.blog.163.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.model;

/**
 * @author dewitt
 *
 */
class Item {
	public static final String TYPE_MEDICINE = 'Ò©Æ·';
	public static final String TYPE_WEAPON = 'ÎäÆ÷';
	public static final String TYPE_EQUIPMENT = '×°±¸';
	
	long id;
	String name;
	String type;
	String res;
	String desc;
	int level;
	int price;
	int amount = 1;
	
	public String toString() {
		def props = this.getProperties();
		props.remove('class');
		props.remove('metaClass');
		return props.toString();
	}
}
