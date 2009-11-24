/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */

package com.javaxyq.action;

import com.javaxyq.core.*;
import com.javaxyq.task.*;
import com.javaxyq.graph.*;
import com.javaxyq.config.*;
import com.javaxyq.event.*;
import com.javaxyq.ui.*;

/**
 * 场景事件处理类
 * @author dewitt
 * @date 2009-11-23 create
 */
class CommonSceneAction implements SceneListener{
	
	void onInit(SceneEvent e) {
		String sceneId = e.getScene();
		Helper.clearNPC(sceneId);
		def xml =  new XmlParser().parse(new File("resources/npcs.xml"));
		def npcs = xml.Scene.find {it.@id==sceneId }.NPC;
		for(def npc in npcs) {
			Helper.registerNPC(sceneId,npc.attributes());
		}
		
		if(sceneId =='wzg') {
			println "初始化场景：五庄观.."
		}else if(sceneId =='wzg_qkd') {
			println "初始化场景：乾坤殿"
			
		}
	}
	
	void onLoad(SceneEvent e) {
		String sceneId = e.getScene();
		if(sceneId =='wzg') {
			println "已加载场景：五庄观.."
			//def ds = DataStore;
			Canvas canvas = GameMain.getSceneCanvas();
			def $道童 = canvas.findNpc("传送道童");
			def $配色师 = canvas.findNpc("配色师");
		}else if(sceneId =='wzg_qkd'){
			Canvas canvas = GameMain.getSceneCanvas();
			def $镇元大仙 = canvas.findNpc("镇元大仙");
			/*def action0 = { evt->
				int exp = 5000;
				int money = 1000;
				def player = GameMain.getPlayer();
				//卖血
				def vo = player.getData();
				int cost = vo.maxHp / 3;
				if(vo.hp <= cost) {				
					GameMain.doTalk(GameMain.getTalker(),
							new TalkConfig("徒儿你劳累过度，去休息一下再来吧！")
					);
					println "你的气血不足${cost}点,无法完成任务！"
					return;
				}
				DataStore.addHp(player,-cost);
				//DataStore.addMp(player,-100);
				
				DataStore.addExp(player, exp);
				DataStore.addMoney(player,money);
				DataStore.addItemToPlayer(player,DataStore.createItem('六道轮回'))
				GameMain.doTalk(GameMain.getTalker(),
						new TalkConfig("徒儿辛苦了，为师奖励你#R"+exp+"#n经验和#R"+money+"#n金钱，好好努力！")
				);
				println("获得"+exp+"经验和"+money+"金钱");
			};
			*/
			def action = { evt ->
				def currTasks = TaskManager.instance.getTasksOfType('school');
				if(currTasks && currTasks.size()>0) {
					def currTask = currTasks[0];
					if(!currTask.finished && !currTask.autoSpark &&currTask.subtype!='patrol') {//非自动触发完成的任务
						TaskManager.instance.process(currTask);
					}
					if(currTask.finished) {//任务已完成
						TaskManager.instance.remove(currTask);
						def player = GameMain.getPlayer();
						DataStore.addMoney(player,currTask.money);
						DataStore.addExp(player, currTask.exp);
						//DataStore.addItemToPlayer(player,DataStore.createItem('六道轮回'))
						GameMain.doTalk(GameMain.getTalker(),new TalkConfig("徒儿辛苦了，为师奖励你#R${currTask.exp}#n经验和#R${currTask.money}#n金钱，继续努力！"));
					}else {//任务未完成
						def cfg = new TalkConfig('你还有任务在身，不能重复接任务。你要取消当前任务吗？');
						cfg.addLink(new LinkConfig('是的，我要取消','取消师门任务',''));
						cfg.addLink(new LinkConfig('我去完成任务','close',''));
						GameMain.doTalk(GameMain.getTalker(),cfg);
					}
				}else {//没有未完成的师门任务
					def rand = new Random();
					def subtypes = ['deliver','lookfor','patrol'];
					def subtype = subtypes[rand.nextInt(subtypes.size())];
					def sender = '镇元大仙';
					def task = TaskManager.instance.create('school',subtype, sender);
					TaskManager.instance.add(task);
					def desc = TaskManager.instance.desc(task);
					GameMain.doTalk(GameMain.getTalker(),new TalkConfig(desc));
				}
			}
			GameMain.registerAction("com.javaxyq.action.师门任务",new ClosureAction(action));

			//取消师门任务
			def cancelTaskAction = {evt ->
				def currTasks = TaskManager.instance.getTasksOfType('school');
				if(currTasks && currTasks.size()>0) {
					def currTask = currTasks[0];
					if(!currTask.finished) {//取消未完成任务
						TaskManager.instance.remove(currTask);
						GameMain.doTalk(GameMain.getTalker(),new TalkConfig('你的任务已取消。'));
					}
				}
			}
			GameMain.registerAction("com.javaxyq.action.取消师门任务",new ClosureAction(cancelTaskAction));

/*			//战斗任务
			def battleAction = { evt->
				GameMain.hideDialog(GameMain.getTalkPanel());
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
						
				t1.add(GameMain.getPlayer());
				//def listener = 
				GameMain.enterBattle(t1,t2);
				GameMain.battleCanvas.battleWin = { event ->
					println "战斗胜利"
					//TODO 计算奖励经验值、金钱、物品等
					int exp = t2.size() * 1250;
					t1.each{ player ->
						player.data.exp += exp;
					}
					UIHelper.prompt("获得奖励：${exp}经验",3000);
				}
				GameMain.battleCanvas.battleDefeated = { event ->
					println "战斗失败"
					//气血为0的人物恢复一点气血
					t1.each{ player ->
						if(player.data.hp ==0) {
							player.data.hp = 1;
						}
					}
					UIHelper.prompt("我方全军覆没，战斗结束。",3000);
				}
			}
			GameMain.registerAction("com.javaxyq.action.练武",new ClosureAction(battleAction));
*/			
		}
		//play background music
		
	}
	
	void onUnload(SceneEvent e) {
		//stop background music
		//MapConfig cfg = (MapConfig) ResourceStore.getInstance().findConfig(id);
		String sceneId = e.getScene();
		Helper.clearNPC(sceneId);
	}
	
}