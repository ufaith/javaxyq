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
	private boolean finished;
	private Map params;
	
	public Task() {
		params = [:];
		createDate = new Date();
	}
	public void add(String paramName,Object value) {
		params[paramName] = value;
	}
	public void remove(String param) {
		params.remove(param);
	}
	public Object get(String param) {
		return params[param];
	}
	
	public String toString() {
		def props = this.getProperties();
		props.remove('class');
		props.remove('metaClass');
		return props.toString();
	}	
}
