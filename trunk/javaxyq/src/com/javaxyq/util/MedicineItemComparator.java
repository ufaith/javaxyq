/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-15
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.util;

import java.util.Comparator;

import com.javaxyq.model.Item;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.model.MedicineItem;

/**
 * 药品物品比较器
 * @author gongdewei
 * @date 2010-4-15 create
 */
public class MedicineItemComparator implements Comparator<Item> {

	/** 比较属性 ，参考ItemTypes中Medicine的子类型'0x1xxx'*/
	private int type;
	public MedicineItemComparator(int type) {
		this.type = type;
	}
	@Override
	public int compare(Item o1, Item o2) {
		int val1 = getValue(o1,type);
		int val2 = getValue(o2,type);
		return val1-val2;
	}
	/**
	 * @param o1
	 * @param type2
	 * @return
	 */
	private int getValue(Item item, int type) {
		MedicineItem it = (MedicineItem) item;
		switch (type) {
		case ItemTypes.TYPE_MEDICINE_HP:
			return it.hp;
		case ItemTypes.TYPE_MEDICINE_MP:
			return it.mp;
		case ItemTypes.TYPE_MEDICINE_INJURY:
			return it.injury;
		case ItemTypes.TYPE_MEDICINE_SP:
			return it.sp;
		default:
			break;
		}
		return 0;
	}


}
