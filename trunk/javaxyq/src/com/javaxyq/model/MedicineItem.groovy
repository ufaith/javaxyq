/*
 * JavaXYQ Source Code 
 * MedicineItem MedicineItem.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author dewitt
 *
 */
class MedicineItem extends Item {
	/**
	 * ��Ч
	 */
	public String efficacy;
	private static final long serialVersionUID = -5332286396631443691L;
	
	public MedicineItem() {
		this.type = TYPE_MEDICINE;
	}
	
	protected Object clone() throws CloneNotSupportedException {
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
	
}
