/*
 * JavaXYQ Source Code 
 * Item Item.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 物品数据模型类
 * @author dewitt
 */
public class Item implements Serializable{
	public static final String TYPE_MEDICINE = "药品";
	public static final String TYPE_WEAPON = "武器";
	public static final String TYPE_EQUIPMENT = "装备";
	private static final long serialVersionUID = -4027503236184637259L;
	
	public String id;
	public String name;
	public String type;
	public String desc;
	public int level;
	public int price;
	public int amount = 1;
	
	@Override
	public String toString() {
		return String.format("Item [amount=%s, desc=%s, id=%s, level=%s, name=%s, price=%s, type=%s]", amount, desc,
				id, level, name, price, type);
	}

	protected void writeObject(ObjectOutputStream s)
	throws IOException
	{
		s.writeUTF(id);
		s.writeUTF(name);
		s.writeUTF(type);
		s.writeUTF(desc);
		s.writeInt(level);
		s.writeInt(price);
		s.writeInt(amount);
	}
	
	/**
	 * Reconstitute this object from a stream (i.e., deserialize it).
	 */
	protected void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		id= s.readUTF();
		name= s.readUTF();
		type= s.readUTF();
		desc= s.readUTF();
		level= s.readInt();
		price= s.readInt();
		amount= s.readInt();
	}
}
