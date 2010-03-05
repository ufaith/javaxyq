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
	 * 功效
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
		//判断药品的种类
		switch(this.efficacy) {
		case ~/恢复气血.*/:
			int val = this.efficacy.replaceAll(/恢复气血(.*)点/,'$1').toInteger();
			eff['hp']= val;
			break;
		case ~/恢复法力.*/:
			int val = this.efficacy.replaceAll(/恢复法力(.*)点/,'$1').toInteger();
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
