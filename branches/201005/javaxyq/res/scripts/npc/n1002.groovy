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
    	String chat = "��ׯ���ǵ��ɷ�Դ�أ�ׯ�ڵ��˲ι������ͬ�٣���������֮��ϡ������#r����Ϊʦ���£�";
    	Option[] options = new Option[3];
    	options[0] = new Option("Ϊʦ��������","draw_task");
    	options[1] = new Option("ѧϰ����","learn_skill");
    	options[2] = new Option("ͽ������","close");
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
			if(!currTask.finished && !currTask.autoSpark &&currTask.subtype!='patrol') {//���Զ�������ɵ�����
				TaskManager.instance.process(currTask);
			}
			if(currTask.finished) {//���������
				TaskManager.instance.remove(currTask);
				def player = GameMain.getPlayer();
				DataStore.addMoney(player,currTask.money);
				DataStore.addExp(player, currTask.exp);
				String chat = "ͽ�������ˣ�Ϊʦ������#R${currTask.exp}#n�����#R${currTask.money}#n��Ǯ������Ŭ����"
				int times = currTask.get('times').toInteger();
				if(times==10) {
					int rounds = currTask.get('rounds').toInteger();
					//���⽱��
					def items = ['�첻��','�����ֻ�','�ɺ���','��¶Ϊ˪','����','�ܵ�'];
					def item = items[new Random().nextInt(items.size())]
					DataStore.addItemToPlayer(player,DataStore.createItem(item));
					chat += "#r����˵�${rounds}��ʦ�����񣬶��⽱����һ��#R${item}#n��"
				}
				GameMain.doTalk(GameMain.getTalker(),chat);				
			}else {//����δ���
				String chat ='�㻹���������������ظ���������Ҫȡ����ǰ������';
		    	Option[] options = new Option[2];
		    	options[0] = new Option("�ǵģ���Ҫȡ��","cancel_task");
		    	options[1] = new Option("��ȥ�������","close");
		    	Option result = GameMain.doTalk(GameMain.getTalker(),chat,options);
				if("cancel_task".equals(result.getAction())) {
					//ȡ��δ�������
					TaskManager.instance.remove(currTask);
					GameMain.doTalk(GameMain.getTalker(),'���������ȡ����');
				}
			}
		}else {//û��δ��ɵ�ʦ������
			def rand = new Random();
			def subtypes = ['deliver','lookfor','patrol'];
			def subtype = subtypes[rand.nextInt(subtypes.size())];
			def sender = '��Ԫ����';
			def task = TaskManager.instance.create('school',subtype, sender);
			TaskManager.instance.add(task);
			def desc = TaskManager.instance.desc(task);
			GameMain.doTalk(GameMain.getTalker(),desc);
		}
    	
    }
    
    private void learnSkill() {
    	Player player = GameMain.getPlayer();
    	if(player.getData().level < 10) {
    		GameMain.doTalk(GameMain.getTalker(),"��ĸ�����ǳ����Щ����������Ϊʦ�ɣ�");
    	}else {
    		UIHelper.showDialog('main_skill');
    	}
    }
}
