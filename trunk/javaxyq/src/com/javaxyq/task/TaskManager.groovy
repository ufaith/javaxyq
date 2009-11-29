/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * please visit http://javaxyq.googlecode.com
 * mail to kylixs@qq.com
 */
package com.javaxyq.task;

import com.javaxyq.model.Task;

/**
 * 任务管理器
 * @author dewitt
 * @date 2009-11-23 create
 */
public class TaskManager {
	public static final TaskManager instance = new TaskManager();
	private List tasklist = [];
	private Map coolies = [:];
	private TaskManager() {
	}
	
	/**
	 * 注册任务处理单元
	 * @param type
	 * @param coolie
	 */
	public void register(String type, coolie) {
		if (coolie instanceof String) {
			coolie = Class.forName(coolie).newInstance();
		}
		coolies[type] = coolie;
	}
	
	/**
	 * 读取某个npc处理的任务列表
	 * @param npc
	 * @return
	 */
	public List getTasksFor(String npcid) {
		return tasklist.findAll{task-> task.receiver==npcid};
	}
	
	/**
	 * 是否有某类型的任务
	 * @param type
	 * @return
	 */
	public boolean hasTaskOfType(String type) {
		return tasklist.any{task-> task.type==type};
	}
	
	/**
	 * 读取某类型的任务列表
	 * @param type
	 * @return
	 */
	public List getTasksOfType(String type) {
		return tasklist.findAll{task-> task.type==type};
	}
	
	/**
	 * 读取某类型的任务
	 * @param type
	 * @param subtype
	 * @return
	 */
	public Task getTaskOfType(String type, String subtype) {
		return tasklist.find{task-> task.type==type && task.subtype == subtype};;
	}
	
	/**
	 * 返回任务列表
	 * @return
	 */
	public List getTaskList() {
		return tasklist;
	}
		
	/**
	 * 删除任务
	 * @param task
	 */
	public void remove(Task task) {
		tasklist.remove(task);
	}
	
	/**
	 * 添加任务
	 * @param task
	 */
	public void add(Task task) {
		tasklist.add(task);
	}
	
	/**
	 * 处理任务
	 * @param task
	 * @return
	 */
	public boolean process(Task task) {
		TaskCoolie coolie = coolies[task.type];
		if(!coolie) {
			println "任务类型:${task.type} 未注册，处理失败！";
			return;
		}
		return coolie.process(task);
	}

	/**
	 * 创建任务
	 * @param subtype
	 * @param sender
	 * @return
	 */
	public Task create(String type,String subtype,String sender) {
		TaskCoolie coolie = coolies[type];
		if(!coolie) {
			println "任务类型:${type} 未注册，创建失败！";
			return;
		}
		return coolie.create(subtype,sender);
	}

	/**
	 * 生成任务描述
	 * @param task
	 * @return
	 */
	public String desc(Task task) {
		TaskCoolie coolie = coolies[task.type];
		if(!coolie) {
			println "任务类型:${task.type} 未注册，处理失败！";
			return;
		}
		coolie.desc(task);
	}
}
