/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.task;

import com.javaxyq.model.Task;

/**
 * ������Ԫ��
 * @author dewitt
 * @date 2009-11-23 create
 */
public class TaskCoolie {
	
	/**
	 * ��������
	 * @param task
	 */
	public boolean process(Task task) {
		return this.invokeMethod(task.subtype,task); 
	}
	
	/**
	 * ��������
	 * @param subtype
	 * @param sender
	 */
	public Task create(String subtype,String sender) {
		return this.invokeMethod("create_$subtype",sender);
	}
	
	/**
	 * �������������
	 * @param task
	 * @return
	 */
	public String desc(Task task) {
		return this.invokeMethod("desc_${task.subtype}",task);
	}
}
