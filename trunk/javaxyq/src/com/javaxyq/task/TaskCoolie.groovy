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
 * 任务处理单元类
 * @author dewitt
 * @date 2009-11-23 create
 */
public class TaskCoolie {
	
	/**
	 * 处理任务
	 * @param task
	 */
	public boolean process(Task task) {
		return this.invokeMethod(task.subtype,task); 
	}
	
	/**
	 * 创建任务
	 * @param subtype
	 * @param sender
	 */
	public Task create(String subtype,String sender) {
		return this.invokeMethod("create_$subtype",sender);
	}
	
	/**
	 * 生成任务的描述
	 * @param task
	 * @return
	 */
	public String desc(Task task) {
		return this.invokeMethod("desc_${task.subtype}",task);
	}
}
