/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.core;

import com.javaxyq.event.ItemListener;

/**
 * @author dewitt
 *
 */
public class ItemManager {
	
	private static Map itemActions = [:];

	private ItemManager() {
	}
	
	public static void init() {
		loadItems();
	}
	
	private static void loadItems() {
		try {
		def xml = new XmlParser().parse(GameMain.getFile('xml/items.xml'));
		for(Node item in xml.Item) {
			if(item.@type && item.@class) {
				try {
				itemActions[item.@type] = Class.forName(item.@class).newInstance();
				}catch(Exception e) {
					println "加载物品的处理类失败！${item.@type} => ${item.@class}"
					e.printStackTrace();
				}
			}
		}
		}catch(e) {
			println "load items failed!";
			e.printStackTrace();
		}
	}
	
	public static ItemListener findItemAction(String type) {
		return itemActions[type]
	}
}
