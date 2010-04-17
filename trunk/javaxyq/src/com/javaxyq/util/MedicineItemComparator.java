/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-15
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.util;

import java.util.Comparator;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.MedicineItem;
import com.javaxyq.model.Item;
import com.javaxyq.model.ItemTypes;

/**
 * ҩƷ��Ʒ�Ƚ���
 * @author gongdewei
 * @date 2010-4-15 create
 */
public class MedicineItemComparator implements Comparator<ItemInstance> {

	/** �Ƚ����� ���ο�ItemTypes��Medicine��������'0x1xxx'*/
	private int type;
	public MedicineItemComparator(int type) {
		this.type = type;
	}
	@Override
	public int compare(ItemInstance o1, ItemInstance o2) {
		int val1 = getValue(o1.getItem(),type);
		int val2 = getValue(o2.getItem(),type);
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
			return it.getHp();
		case ItemTypes.TYPE_MEDICINE_MP:
			return it.getMp();
		case ItemTypes.TYPE_MEDICINE_INJURY:
			return it.getInjury();
//		case ItemTypes.TYPE_MEDICINE_SP:
//			return it.sp;
		default:
			break;
		}
		return 0;
	}


}
