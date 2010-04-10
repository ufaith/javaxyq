/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.core;

import java.util.HashMap;
import java.util.Map;

import com.javaxyq.data.XmlDataLoader;
import com.javaxyq.event.ItemListener;
import com.javaxyq.model.Item;

/**
 * @author dewitt
 *
 */
public class ItemManager {
	
	private static Map<String,ItemListener> itemActions = new HashMap<String, ItemListener>();

	private ItemManager() {
	}
	
	public static void init() {
		XmlDataLoader.loadItems();
	}
	
	public static void addItem(String type,ItemListener l) {
		itemActions.put(type, l);
	}
	
	public static ItemListener findItemAction(String type) {
		return itemActions.get(type);
	}
}
