/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.task;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.model.Task;
import com.javaxyq.widget.Player;

/**
 * '��������'����Ԫ
 * @author dewitt
 * @date 2009-11-23 create
 */
class SchoolTaskCoolie extends TaskCoolie {
	
	/** ÿ10��Ϊһ������ */
	private static final int ROUND = 10;
	
	def rand = new Random();
	/** ������� */
	int times;
	/**
	 * ����
	 * @param task
	 * @return
	 */
	private boolean deliver(Task task) {
		println "deliver $task"
		if(!task.finished) {
			Player player =GameMain.getPlayer();
			Player target = task.get("target");
			task.finished = true;
			GameMain.doTalk(target,new TalkConfig("�����յ���ʦ�������ţ��Ͽ��ȥ�����ɡ�"));
			return true;
		}
	}
	
	/**
	 * Ѱ��
	 * @param task
	 * @return
	 */
	private boolean lookfor(Task task) {
		println "lookfor $task"
		
	}
	
	/**
	 * Ѳ��
	 * @param task
	 * @return
	 */
	private boolean patrol(Task task) {
		println "patrol $task"
		
	}
	
	/**
	 * ������������
	 * @param sender
	 * @return
	 */
	private Task create_deliver(String sender) {
		Task task = new Task();
		task.type = 'school';
		task.subtype='deliver';
		task.sender = sender;
		task.receiver = randomNpc();
		this.times %=ROUND;
		this.times ++;
		int level = GameMain.getPlayer().getData().level;
		task.exp = this.times*780*level;
		task.money = this.times * 1150*level;
		return task;
	}
	
	private Task create_lookfor(String sender) {
		Task task = new Task();
		task.type = 'school';
		task.subtype='lookfor';
		task.sender = sender;
		task.receiver = sender;
		task.add('item','��Ҷ��');
		this.times %=ROUND;
		this.times ++;
		int level = GameMain.getPlayer().getData().level;
		task.exp = this.times*980*level;
		task.money = this.times * 2150*level;
		
		return task;
	}
	
	private Task create_patrol(String sender) {
		Task task = new Task();
		task.type = 'school';
		task.subtype='patrol';
		task.sender = sender;
		task.receiver = sender;
		this.times %=ROUND;
		this.times ++;
		int level = GameMain.getPlayer().getData().level;
		task.exp = this.times*1080*level;
		task.money = this.times * 1550*level;
				
		return task;
	}
	
	/**�����������������
	 * @param task
	 * @return
	 */
	private String desc_deliver(Task task) {
		return "ʦ��������#R${task.receiver}#W���飬�㽫�ż��͹�ȥ��";
	}
	
	private String desc_lookfor(Task task) {
		return "���ɽ�����Ҫ#R${task.get('item')}#W������ɽȥѰ��һ��������";
	}
	
	private String desc_patrol(Task task) {
		return '�����������춯���㵽���浽�����߿����������в���֮ͽ����ѵһ�¡�';
	}
	
	/**
	 * ���ѡ��һ��NPC
	 * @return
	 */
	private String randomNpc() {
		//FIXME �������NPC 
		def npcs = ResourceStore.getInstance().getAllNpcs();
		def index = rand.nextInt(npcs.size());
		return npcs.get(index).name;
	}
}
