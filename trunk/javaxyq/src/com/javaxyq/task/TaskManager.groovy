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
 * ���������
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
	 * ע��������Ԫ
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
	 * ��ȡĳ��npc����������б�
	 * @param npc
	 * @return
	 */
	public List getTasksFor(String npcid) {
		return tasklist.findAll{task-> task.receiver==npcid};
	}
	
	/**
	 * �Ƿ���ĳ���͵�����
	 * @param type
	 * @return
	 */
	public boolean hasTaskOfType(String type) {
		return tasklist.any{task-> task.type==type};
	}
	
	/**
	 * ��ȡĳ���͵������б�
	 * @param type
	 * @return
	 */
	public List getTasksOfType(String type) {
		return tasklist.findAll{task-> task.type==type};
	}
	
	/**
	 * ��ȡĳ���͵�����
	 * @param type
	 * @param subtype
	 * @return
	 */
	public Task getTaskOfType(String type, String subtype) {
		return tasklist.find{task-> task.type==type && task.subtype == subtype};;
	}
	
	/**
	 * ���������б�
	 * @return
	 */
	public List getTaskList() {
		return tasklist;
	}
		
	/**
	 * ɾ������
	 * @param task
	 */
	public void remove(Task task) {
		tasklist.remove(task);
	}
	
	/**
	 * �������
	 * @param task
	 */
	public void add(Task task) {
		tasklist.add(task);
	}
	
	/**
	 * ��������
	 * @param task
	 * @return
	 */
	public boolean process(Task task) {
		TaskCoolie coolie = coolies[task.type];
		if(!coolie) {
			println "��������:${task.type} δע�ᣬ����ʧ�ܣ�";
			return;
		}
		return coolie.process(task);
	}

	/**
	 * ��������
	 * @param subtype
	 * @param sender
	 * @return
	 */
	public Task create(String type,String subtype,String sender) {
		TaskCoolie coolie = coolies[type];
		if(!coolie) {
			println "��������:${type} δע�ᣬ����ʧ�ܣ�";
			return;
		}
		return coolie.create(subtype,sender);
	}

	/**
	 * ������������
	 * @param task
	 * @return
	 */
	public String desc(Task task) {
		TaskCoolie coolie = coolies[task.type];
		if(!coolie) {
			println "��������:${task.type} δע�ᣬ����ʧ�ܣ�";
			return;
		}
		coolie.desc(task);
	}
}
