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
 * '门派任务'处理单元
 * @author dewitt
 * @date 2009-11-23 create
 */
class SchoolTaskCoolie extends TaskCoolie {
	
	/** 每10次为一轮任务 */
	private static final int ROUND = 10;
	
	def rand = new Random();
	/** 任务次数 */
	int times;
	/**
	 * 送信
	 * @param task
	 * @return
	 */
	private boolean deliver(Task task) {
		println "deliver $task"
		if(!task.finished) {
			Player player =GameMain.getPlayer();
			Player target = task.get("target");
			task.finished = true;
			GameMain.doTalk(target,new TalkConfig("我已收到你师傅的来信，赶快回去禀报吧。"));
			return true;
		}
	}
	
	/**
	 * 寻物
	 * @param task
	 * @return
	 */
	private boolean lookfor(Task task) {
		println "lookfor $task"
		
	}
	
	/**
	 * 巡逻
	 * @param task
	 * @return
	 */
	private boolean patrol(Task task) {
		println "patrol $task"
		
	}
	
	/**
	 * 创建送信任务
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
		task.add('item','四叶花');
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
	
	/**生成送信任务的描述
	 * @param task
	 * @return
	 */
	private String desc_deliver(Task task) {
		return "师傅有事与#R${task.receiver}#W商议，你将信件送过去。";
	}
	
	private String desc_lookfor(Task task) {
		return "门派建设需要#R${task.get('item')}#W，你下山去寻找一个回来。";
	}
	
	private String desc_patrol(Task task) {
		return '近日听闻有异动，你到外面到处走走看看，发现有不轨之徒，教训一下。';
	}
	
	/**
	 * 随机选择一个NPC
	 * @return
	 */
	private String randomNpc() {
		//FIXME 完善随机NPC 
		def npcs = ResourceStore.getInstance().getAllNpcs();
		def index = rand.nextInt(npcs.size());
		return npcs.get(index).name;
	}
}
