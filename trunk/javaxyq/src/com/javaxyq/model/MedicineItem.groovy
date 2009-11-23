/*
 * JavaXYQ Source Code 
 * MedicineItem MedicineItem.groovy
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
class MedicineItem extends Item {
	/**
	 * ��Ч
	 */
	String efficacy;
	
	public MedicineItem() {
		this.type = TYPE_MEDICINE;
	}
	
	protected Object clone() throws CloneNotSupportedException {
		MedicineItem item = new MedicineItem();
		item.efficacy = this.efficacy;
		item.id= this.id;
		item.name= this.name;
		item.type= this.type;
		item.res= this.res;
		item.desc= this.desc;
		item.level= this.level;
		item.price= this.price;
		item.amount = this.amount ;
		return item;
	}
	
	public Map getEfficacyParams() {
		def eff = [:]; 
		//�ж�ҩƷ������
		switch(this.efficacy) {
		case ~/�ָ���Ѫ.*/:
			int val = this.efficacy.replaceAll(/�ָ���Ѫ(.*)��/,'$1').toInteger();
			eff['hp']= val;
			break;
		case ~/�ָ�����.*/:
			int val = this.efficacy.replaceAll(/�ָ�����(.*)��/,'$1').toInteger();
			eff['mp'] = val;
			break;
		}
		return eff;
	}
}