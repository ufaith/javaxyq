package com.javaxyq.model;

/**
 * 任务实体类
 * @author dewitt
 * @date 2009-11-23 create
 */
public class Task {
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
	
}
