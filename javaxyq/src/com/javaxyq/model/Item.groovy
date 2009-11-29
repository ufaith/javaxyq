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
 * @author dewitt
 *
 */
class Item implements Serializable{
	public static final String TYPE_MEDICINE = 'Ò©Æ·';
	public static final String TYPE_WEAPON = 'ÎäÆ÷';
	public static final String TYPE_EQUIPMENT = '×°±¸';
	
	String id;
	String name;
	String type;
	String desc;
	int level;
	int price;
	int amount = 1;
	
	public String toString() {
		def props = this.getProperties();
		props.remove('class');
		props.remove('metaClass');
		return props.toString();
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
