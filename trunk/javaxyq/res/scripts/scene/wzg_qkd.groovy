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
 * �����¼�������
 * @author dewitt
 * @date 2009-11-23 create
 */
class CommonSceneAction implements SceneListener{
	void onInit(SceneEvent e) {
	}
	
	void onLoad(SceneEvent e) {
		Canvas canvas = GameMain.getSceneCanvas();
		def $��Ԫ���� = canvas.findNpc("��Ԫ����");
		def action = { evt ->
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
					GameMain.doTalk(GameMain.getTalker(),new TalkConfig(chat) );				
				}else {//����δ���
					def cfg = new TalkConfig('�㻹���������������ظ���������Ҫȡ����ǰ������');
					cfg.addLink(new LinkConfig('�ǵģ���Ҫȡ��','ȡ��ʦ������',''));
					cfg.addLink(new LinkConfig('��ȥ�������','close',''));
					GameMain.doTalk(GameMain.getTalker(),cfg);
				}
			}else {//û��δ��ɵ�ʦ������
				def rand = new Random();
				def subtypes = ['deliver','lookfor','patrol'];
				def subtype = subtypes[rand.nextInt(subtypes.size())];
				def sender = '��Ԫ����';
				def task = TaskManager.instance.create('school',subtype, sender);
				TaskManager.instance.add(task);
				def desc = TaskManager.instance.desc(task);
				GameMain.doTalk(GameMain.getTalker(),new TalkConfig(desc));
			}
		}
		GameMain.registerAction("com.javaxyq.action.ʦ������",new ClosureAction(action));

		//ȡ��ʦ������
		def cancelTaskAction = {evt ->
			def currTasks = TaskManager.instance.getTasksOfType('school');
			if(currTasks && currTasks.size()>0) {
				def currTask = currTasks[0];
				if(!currTask.finished) {//ȡ��δ�������
					TaskManager.instance.remove(currTask);
					GameMain.doTalk(GameMain.getTalker(),new TalkConfig('���������ȡ����'));
				}
			}
		}
		GameMain.registerAction("com.javaxyq.action.ȡ��ʦ������",new ClosureAction(cancelTaskAction));

		//play background music
		
	}
	
	void onUnload(SceneEvent e) {
		//stop background music
		//MapConfig cfg = (MapConfig) ResourceStore.getInstance().findConfig(id);
		//String sceneId = e.getScene();
		//Helper.clearNPC(sceneId);
	}
	
}