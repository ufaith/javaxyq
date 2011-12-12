/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package com.javaxyq.action;

import com.javaxyq.model.Option;
import com.javaxyq.task.TaskManager 
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;
import com.javaxyq.data.*;
import com.javaxyq.core.*;
import com.javaxyq.event.*;
import com.javaxyq.config.*;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
class n1002 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	String chat = "五庄观是地仙发源地，庄内的人参果与天地同寿，乃是三界之珍稀宝贝！#r你找为师何事？";
    	Option[] options = new Option[3];
    	options[0] = new Option("为师门做贡献","draw_task");
    	options[1] = new Option("学习技能","learn_skill");
    	options[2] = new Option("徒儿告退","close");
    	Option result = GameMain.doTalk(evt.getPlayer(),chat,options);
		if("draw_task".equals(result.getAction())) {
			drawTask();
		}else if("learn_skill".equals(result.getAction())) {
			learnSkill();
		}
    	
    }
	
    private void drawTask() {
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
				GameMain.doTalk(GameMain.getTalker(),chat);				
			}else {//任务未完成
				String chat ='你还有任务在身，不能重复接任务。你要取消当前任务吗？';
		    	Option[] options = new Option[2];
		    	options[0] = new Option("是的，我要取消","cancel_task");
		    	options[1] = new Option("我去完成任务","close");
		    	Option result = GameMain.doTalk(GameMain.getTalker(),chat,options);
				if("cancel_task".equals(result.getAction())) {
					//取消未完成任务
					TaskManager.instance.remove(currTask);
					GameMain.doTalk(GameMain.getTalker(),'你的任务已取消。');
				}
			}
		}else {//没有未完成的师门任务
			def rand = new Random();
			def subtypes = ['deliver','lookfor','patrol'];
			def subtype = subtypes[rand.nextInt(subtypes.size())];
			def sender = '镇元大仙';
			def task = TaskManager.instance.create('school',subtype, sender);
			TaskManager.instance.add(task);
			def desc = TaskManager.instance.desc(task);
			GameMain.doTalk(GameMain.getTalker(),desc);
		}
    	
    }
    
    private void learnSkill() {
    	Player player = GameMain.getPlayer();
    	if(player.getData().level < 10) {
    		GameMain.doTalk(GameMain.getTalker(),"你的根基尚浅，过些日子再来找为师吧！");
    	}else {
    		UIHelper.showDialog('main_skill');
    	}
    }
}
