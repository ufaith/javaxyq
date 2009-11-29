package com.javaxyq.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 任务实体类
 * @author dewitt
 * @date 2009-11-23 create
 */
public class Task implements Serializable{
	private String sender;
	private String receiver;
	private Date createDate;
	private String type;
	private String subtype;
	private String id;
	private int exp;
	private int money;
	/** 任务是否已完成 */
	private boolean finished;
	/** 自动触发、响应任务（任务完成时） */
	private boolean autoSpark;  
	private Map params;
	
	public Task() {
		params = [:];
		createDate = new Date();
	}
	
	public Task(String type,String subtype,String sender,String receiver) {
		params = [:];
		createDate = new Date();
		this.type = type;
		this.subtype = subtype;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	/**
	 * 设置某个参数的值
	 * @param paramName
	 * @param value
	 */
	public void set(String paramName,Object value) {
		params[paramName] = value;
	}
	
	/**
	 * 增加某个参数的值！注意，与set不一样
	 * @param paramName
	 * @param value
	 */
	public void add(String paramName,Object value) {
		params[paramName] += value;
	}
	public void remove(String param) {
		params.remove(param);
	}
	public Object get(String param) {
		return params[param];
	}
	
	/**
	 * @return the autoSpark
	 */
	public boolean isAutoSpark() {
		return autoSpark;
	}
	
	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return the subtype
	 */
	public String getSubtype() {
		return subtype;
	}
	
	@Override
	public String toString() {
		return """Task [type=$type, subtype=$subtype, receiver=$receiver, sender=$sender, finished=$finished, autoSpark=$autoSpark,   
		createDate=$createDate , exp=$exp ,id=$id , money=$money, params=$params ]""";
	}
	
	protected void writeObject(ObjectOutputStream s)
	throws IOException
	{
		s.writeBoolean(autoSpark);
		s.writeObject(createDate);
		s.writeInt(exp);
		s.writeBoolean(finished);
		s.writeUTF(id);
		s.writeInt(money);
		//params
		s.writeObject(params);
		s.writeUTF(receiver);
		s.writeUTF(sender);
		s.writeUTF(subtype);
		s.writeUTF(type);
	}
	
	/**
	 * Reconstitute this object from a stream (i.e., deserialize it).
	 */
	protected void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		autoSpark=s.readBoolean();
		createDate=s.readObject();
		exp=s.readInt();
		finished=s.readBoolean();
		id=s.readUTF();
		money=s.readInt();
		//params
		params=s.readObject();
		receiver=s.readUTF();
		sender=s.readUTF();
		subtype=s.readUTF();
		type=s.readUTF();
				
	}
	
}
