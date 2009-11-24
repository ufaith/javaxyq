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
		}else {
			println "任务已完成？$task"
		}
	}
	
	/**
	 * 寻物
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
	 * 巡逻
	 * @param task
	 * @return
	 */
	private boolean patrol(Task task) {
		println "patrol $task"
		Player player = GameMain.getPlayer();
		player.stop(true);
		//初始化小怪队伍
		int level = GameMain.getPlayer().getData().getLevel();
		def t1 = [],t2 = [];
		def elfs = ['2036','2037','2009','2010','2011','2012'];
		def elfNames = ['大海龟','巨蛙','芙蓉仙子','树怪','蝴蝶仙子','花妖'];
		def random = new Random();
		int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.size()); 
			t2.add(Helper.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
		t1.add(player);
		//进入战斗
		GameMain.enterBattle(t1,t2);
		UIHelper.prompt("一群小贼真正破坏门派，被你抓个正着。",3000);
		//战斗胜利处理
		GameMain.battleCanvas.battleWin = { event ->
			println "战斗胜利"
			task.add('battle',1);
			if(task.get('battle')==2) {
				task.finished = true;
				UIHelper.prompt("师门巡逻任务完成，快去禀报师傅吧。",3000);
			}else {
				UIHelper.prompt("小贼们趁你不注意，又不知道溜到哪里。",3000);
			}
			GameMain.getSceneCanvas().setLastPatrolTime(System.currentTimeMillis());
		}
		//战斗失败处理
		GameMain.battleCanvas.battleDefeated = { event ->
			println "战斗失败"
			//气血为0的人物恢复一点气血
			if(player.data.hp ==0) {
				player.data.hp = 1;
			}
			GameMain.getSceneCanvas().setLastPatrolTime(System.currentTimeMillis());
			UIHelper.prompt("想不到小贼们也武艺高强~~!",3000);
			//UIHelper.prompt("师门巡逻任务失败，请找师傅取消任务。",3000);
		}
		
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
		task.autoSpark = true;  
		this.times %=ROUND;
		this.times ++;
		int level = GameMain.getPlayer().getData().level;
		task.exp = this.times*780*level;
		task.money = this.times * 1150*level;
		return task;
	}
	
	/**
	 * 创建寻物任务
	 * @param sender
	 * @return
	 */
	private Task create_lookfor(String sender) {
		Task task = new Task();
		task.type = 'school';
		task.subtype='lookfor';
		task.sender = sender;
		task.receiver = sender;
		task.set('item','四叶花');
		this.times %=ROUND;
		this.times ++;
		int level = GameMain.getPlayer().getData().level;
		task.exp = this.times*980*level;
		task.money = this.times * 2150*level;
		
		return task;
	}
	
	/**
	 * 创建巡逻任务
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
		//FIXME 根据门派设置巡逻的场景id
		task.set('sceneId','wzg');
		task.set('battle',0);
		
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
