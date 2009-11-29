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
	}
	
	void onLoad(SceneEvent e) {
		Canvas canvas = GameMain.getSceneCanvas();
		def $镇元大仙 = canvas.findNpc("镇元大仙");
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
					String chat = "徒儿辛苦了，为师奖励你#R${currTask.exp}#n经验和#R${currTask.money}#n金钱，继续努力！"
					int times = currTask.get('times').toInteger();
					if(times==10) {
						int rounds = currTask.get('rounds').toInteger();
						//额外奖励
						def items = ['天不老','六道轮回','仙狐涎','白露为霜','麝香','熊胆'];
						def item = items[new Random().nextInt(items.size())]
						DataStore.addItemToPlayer(player,DataStore.createItem(item));
						chat += "#r完成了第${rounds}轮师门任务，额外奖励你一个#R${item}#n！"
					}
					GameMain.doTalk(GameMain.getTalker(),new TalkConfig(chat) );				
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

		//play background music
		
	}
	
	void onUnload(SceneEvent e) {
		//stop background music
		//MapConfig cfg = (MapConfig) ResourceStore.getInstance().findConfig(id);
		//String sceneId = e.getScene();
		//Helper.clearNPC(sceneId);
	}
	
}