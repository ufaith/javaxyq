/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.task;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.*;
import com.javaxyq.model.Task;
import com.javaxyq.widget.Player;
import com.javaxyq.ui.*;

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
		}else {
			println "��������ɣ�$task"
		}
	}
	
	/**
	 * Ѱ��
	 * @param task
	 * @return
	 */
	private boolean lookfor(Task task) {
		println "lookfor $task"
		if(!task.finished) {
			Player player =GameMain.getPlayer();
			Player target = task.get("target");
			def items = DataStore.getPlayerItems(player);
			def item = items.find{item -> item.name==task.get('item')};
			if(item) {
				item.amount --;
				if(item.amount == 0) {
					DataStore.removePlayerItem(player,item);
				}
				task.finished = true;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Ѳ��
	 * @param task
	 * @return
	 */
	private boolean patrol(Task task) {
		println "patrol $task"
		Player player = GameMain.getPlayer();
		player.stop(true);
		//��ʼ��С�ֶ���
		int level = GameMain.getPlayer().getData().getLevel();
		def t1 = [],t2 = [];
		def elfs = ['2036','2037','2009','2010','2011','2012'];
		def elfNames = ['�󺣹�','����','ܽ������','����','��������','����'];
		def random = new Random();
		int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.size()); 
			t2.add(Helper.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
		t1.add(player);
		//����ս��
		GameMain.enterBattle(t1,t2);
		UIHelper.prompt("һȺС�������ƻ����ɣ�����ץ�����š�",3000);
		//ս��ʤ������
		GameMain.battleCanvas.battleWin = { event ->
			println "ս��ʤ��"
			task.add('battle',1);
			if(task.get('battle')==2) {
				task.finished = true;
				UIHelper.prompt("ʦ��Ѳ��������ɣ���ȥ����ʦ���ɡ�",3000);
			}else {
				UIHelper.prompt("С���ǳ��㲻ע�⣬�ֲ�֪���ﵽ���",3000);
			}
			GameMain.getSceneCanvas().setLastPatrolTime(System.currentTimeMillis());
		}
		//ս��ʧ�ܴ���
		GameMain.battleCanvas.battleDefeated = { event ->
			println "ս��ʧ��"
			//��ѪΪ0������ָ�һ����Ѫ
			if(player.data.hp ==0) {
				player.data.hp = 1;
			}
			GameMain.getSceneCanvas().setLastPatrolTime(System.currentTimeMillis());
			UIHelper.prompt("�벻��С����Ҳ���ո�ǿ~~!",3000);
			//UIHelper.prompt("ʦ��Ѳ������ʧ�ܣ�����ʦ��ȡ������",3000);
		}
		
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
		task.autoSpark = true;  
		this.times %=ROUND;
		this.times ++;
		int level = GameMain.getPlayer().getData().level;
		task.exp = this.times*780*level;
		task.money = this.times * 1150*level;
		return task;
	}
	
	/**
	 * ����Ѱ������
	 * @param sender
	 * @return
	 */
	private Task create_lookfor(String sender) {
		Task task = new Task();
		task.type = 'school';
		task.subtype='lookfor';
		task.sender = sender;
		task.receiver = sender;
		task.set('item','��Ҷ��');
		this.times %=ROUND;
		this.times ++;
		int level = GameMain.getPlayer().getData().level;
		task.exp = this.times*980*level;
		task.money = this.times * 2150*level;
		
		return task;
	}
	
	/**
	 * ����Ѳ������
	 * @param sender
	 * @return
	 */
	private Task create_patrol(String sender) {
		Task task = new Task();
		task.type = 'school';
		task.subtype='patrol';
		task.sender = sender;
		task.receiver = sender;
		task.autoSpark = false;
		//FIXME ������������Ѳ�ߵĳ���id
		task.set('sceneId','wzg');
		task.set('battle',0);
		
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
