/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-14
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.model;

/**
 * ��Ϸ��Ʒ����
 * @author gongdewei
 * @date 2010-4-14 create
 */
public class ItemTypes {
	public static final int TYPE_MEDICINE = 0x1000;
	public static final int TYPE_MEDICINE_HP = 0x1001;
	public static final int TYPE_MEDICINE_MP = 0x1002;
	public static final int TYPE_MEDICINE_HPMP = 0x1003;//hp+mp
	public static final int TYPE_MEDICINE_INJURY = 0x1004;//����
	public static final int TYPE_MEDICINE_RESURGENT = 0x1008;//����
	public static final int TYPE_MEDICINE_SP = 0x1010;//��ŭ
	public static final int TYPE_MEDICINE_SOBERUP  = 0x1020;//������쳣
	public static final int TYPE_MEDICINE_DETOXIFY  = 0x1040;//�ⶾ
	public static final int TYPE_MEDICINE_BREAKSEAL  = 0x1080;//�����ӡ
	public static final int TYPE_WEAPON = 0x2000;
	public static final int TYPE_EQUIPMENT = 0x4000;
	
	/**
	 * ��ȡ��Ʒ������
	 * @param item
	 * @return
	 */
	public static int getType(Item item) {
		return Integer.valueOf(item.getType(),16);
	}
	
	/**
	 * �ж���Ʒ������
	 * @param item
	 * @param type
	 * @return
	 */
	public static boolean isType(Item item,int type) {
		return (getType(item) & type) == type;
	}

	public static boolean isMedicine(Item item) {
		return (getType(item) & TYPE_MEDICINE)==TYPE_MEDICINE;
	}
	public static boolean isHpMedicine(Item item) {
		return (getType(item) & TYPE_MEDICINE_HP)==TYPE_MEDICINE_HP;
	}
	public static boolean isMpMedicine(Item item) {
		return (getType(item) & TYPE_MEDICINE_MP)==TYPE_MEDICINE_MP;
	}
	
	public static boolean isWeapon(Item item) {
		return (getType(item) & TYPE_WEAPON)==TYPE_WEAPON;
	}
	public static boolean isEquipment(Item item) {
		return (getType(item) & TYPE_EQUIPMENT)==TYPE_EQUIPMENT;
	}
	
	
}
