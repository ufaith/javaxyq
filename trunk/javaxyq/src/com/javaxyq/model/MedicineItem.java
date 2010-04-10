/*
 * JavaXYQ Source Code 
 * MedicineItem MedicineItem.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.javaxyq.util.StringUtils;

/**
 * ҩƷ����ģ��
 * @author dewitt
 */
public class MedicineItem extends Item {
	/**
	 * ��Ч
	 */
	public String efficacy;
	private static final long serialVersionUID = -5332286396631443691L;
	
	public MedicineItem() {
		this.type = TYPE_MEDICINE;
	}
	
	public MedicineItem clone() throws CloneNotSupportedException {
		MedicineItem item = new MedicineItem();
		item.efficacy = this.efficacy;
		item.id= this.id;
		item.name= this.name;
		item.type= this.type;
		item.desc= this.desc;
		item.level= this.level;
		item.price= this.price;
		item.amount = this.amount ;
		return item;
	}
	
	public Map getEfficacyParams() {
		Map eff = new HashMap(); 
		//�ж�ҩƷ������
//		switch(this.efficacy) {
//		case ~/�ָ���Ѫ.*/:
//			int val = this.efficacy.replaceAll(/�ָ���Ѫ(.*)��/,'$1').toInteger();
//			eff['hp']= val;
//			break;
//		case ~/�ָ�����.*/:
//			int val = this.efficacy.replaceAll(/�ָ�����(.*)��/,'$1').toInteger();
//			eff['mp'] = val;
//			break;
//		}
		if(this.efficacy!=null) {
			String strhp = StringUtils.substringBetween(efficacy, "�ָ���Ѫ", "��");
			if(strhp!=null) {
				eff.put("hp", Integer.valueOf(strhp));
			}
			String strmp = StringUtils.substringBetween(efficacy, "�ָ�����", "��");
			if(strmp!=null) {
				eff.put("mp", Integer.valueOf(strmp));
			}
		}
		return eff;
	}
	protected void writeObject(ObjectOutputStream s)
	throws IOException
	{
		super.writeObject(s);
		s.writeUTF(efficacy);
	}
	
	/**
	 * Reconstitute this object from a stream (i.e., deserialize it).
	 */
	protected void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		super.readObject(s);
		efficacy= s.readUTF();
	}

	@Override
	public String toString() {
		return String.format(
			"MedicineItem [efficacy=%s, amount=%s, desc=%s, id=%s, level=%s, name=%s, price=%s, type=%s, getEfficacyParams()=%s]",
			efficacy, amount, desc, id, level, name, price, type, getEfficacyParams());
	}
	
	
}
