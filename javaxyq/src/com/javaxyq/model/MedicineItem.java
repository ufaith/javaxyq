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
 * 药品数据模型
 * @author dewitt
 */
public class MedicineItem extends Item {
	public int hp;
	public int mp;
	public int sp;
	public int injury;
	public String efficacy;
	private static final long serialVersionUID = -5332286396631443691L;
	
	public MedicineItem() {
		this.type = ItemTypes.TYPE_MEDICINE;
	}
	
	public MedicineItem clone() throws CloneNotSupportedException {
		MedicineItem item = new MedicineItem();
		item.id= this.id;
		item.name= this.name;
		item.type= this.type;
		item.desc= this.desc;
		item.level= this.level;
		item.price= this.price;
		item.amount = this.amount ;
		item.hp = this.hp;
		item.mp = this.mp;
		item.sp = this.sp;
		item.injury = this.injury;
		item.efficacy = this.efficacy;
		return item;
	}
	
	/**
	 * 功效
	 */
	public String actualEfficacy() {
		boolean first = true;
		StringBuilder buf = new StringBuilder(32);
		if(hp != 0) {
			buf.append("恢复气血");
			buf.append(hp);
			buf.append("点");
			first = false;
		}
		if(mp != 0) {
			if(!first)buf.append("，");
			buf.append("恢复法力");
			buf.append(mp);
			buf.append("点");
			first = false;
		}
		if(sp != 0) {
			if(!first)buf.append("，");
			buf.append("回复愤怒");
			buf.append(sp);
			buf.append("点");
			first = false;
		}
		if(injury != 0) {//治疗伤势
			if(!first)buf.append("，");
			buf.append("治疗伤势");
			buf.append(injury);
			buf.append("点");
			first = false;
		}
		return buf.toString();
	}
	
	protected void writeObject(ObjectOutputStream s)
	throws IOException
	{
		super.writeObject(s);
		s.writeUTF(this.efficacy);
		s.writeInt(this.hp);
		s.writeInt(this.mp);
		s.writeInt(this.sp);
		s.writeInt(this.injury);
	}
	
	/**
	 * Reconstitute this object from a stream (i.e., deserialize it).
	 */
	protected void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		super.readObject(s);
		this.efficacy = s.readUTF();
		this.hp = s.readInt();
		this.mp = s.readInt();
		this.sp = s.readInt();
		this.injury = s.readInt();
	}

	@Override
	public String toString() {
		return String
			.format(
				"MedicineItem [efficacy=%s, hp=%s, injury=%s, mp=%s, sp=%s, amount=%s, desc=%s, id=%s, level=%s, name=%s, price=%s, type=%s, actualEfficacy()=%s]",
				efficacy, hp, injury, mp, sp, amount, desc, id, level, name, price, type, actualEfficacy());
	}


	
	
}
