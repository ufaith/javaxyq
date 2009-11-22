/**
 * 
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
		def xml = new XmlParser().parse(new File('xml/items.xml'));
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
	}
	
	public static ItemListener findItemAction(String type) {
		return itemActions[type]
	}
}
