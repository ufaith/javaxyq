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
		String sceneId = e.getScene();
		Helper.clearNPC(sceneId);
		def xml =  new XmlParser().parse(new File("resources/npcs.xml"));
		def npcs = xml.Scene.find {it.@id==sceneId }.NPC;
		for(def npc in npcs) {
			Helper.registerNPC(sceneId,npc.attributes());
		}
		
		if(sceneId =='wzg') {
			println "��ʼ����������ׯ��.."
		}else if(sceneId =='wzg_qkd') {
			println "��ʼ��������Ǭ����"
			
		}
	}
	
	void onLoad(SceneEvent e) {
		String sceneId = e.getScene();
		if(sceneId =='wzg') {
			println "�Ѽ��س�������ׯ��.."
			//def ds = DataStore;
			Canvas canvas = GameMain.getSceneCanvas();
			def $��ͯ = canvas.findNpc("���͵�ͯ");
			def $��ɫʦ = canvas.findNpc("��ɫʦ");
		}else if(sceneId =='wzg_qkd'){
			Canvas canvas = GameMain.getSceneCanvas();
			def $��Ԫ���� = canvas.findNpc("��Ԫ����");
			/*def action0 = { evt->
				int exp = 5000;
				int money = 1000;
				def player = GameMain.getPlayer();
				//��Ѫ
				def vo = player.getData();
				int cost = vo.maxHp / 3;
				if(vo.hp <= cost) {				
					GameMain.doTalk(GameMain.getTalker(),
							new TalkConfig("ͽ�������۹��ȣ�ȥ��Ϣһ�������ɣ�")
					);
					println "�����Ѫ����${cost}��,�޷��������"
					return;
				}
				DataStore.addHp(player,-cost);
				//DataStore.addMp(player,-100);
				
				DataStore.addExp(player, exp);
				DataStore.addMoney(player,money);
				DataStore.addItemToPlayer(player,DataStore.createItem('�����ֻ�'))
				GameMain.doTalk(GameMain.getTalker(),
						new TalkConfig("ͽ�������ˣ�Ϊʦ������#R"+exp+"#n�����#R"+money+"#n��Ǯ���ú�Ŭ����")
				);
				println("���"+exp+"�����"+money+"��Ǯ");
			};
			*/
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
						//DataStore.addItemToPlayer(player,DataStore.createItem('�����ֻ�'))
						GameMain.doTalk(GameMain.getTalker(),new TalkConfig("ͽ�������ˣ�Ϊʦ������#R${currTask.exp}#n�����#R${currTask.money}#n��Ǯ������Ŭ����"));
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

/*			//ս������
			def battleAction = { evt->
				GameMain.hideDialog(GameMain.getTalkPanel());
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
						
				t1.add(GameMain.getPlayer());
				//def listener = 
				GameMain.enterBattle(t1,t2);
				GameMain.battleCanvas.battleWin = { event ->
					println "ս��ʤ��"
					//TODO ���㽱������ֵ����Ǯ����Ʒ��
					int exp = t2.size() * 1250;
					t1.each{ player ->
						player.data.exp += exp;
					}
					UIHelper.prompt("��ý�����${exp}����",3000);
				}
				GameMain.battleCanvas.battleDefeated = { event ->
					println "ս��ʧ��"
					//��ѪΪ0������ָ�һ����Ѫ
					t1.each{ player ->
						if(player.data.hp ==0) {
							player.data.hp = 1;
						}
					}
					UIHelper.prompt("�ҷ�ȫ����û��ս��������",3000);
				}
			}
			GameMain.registerAction("com.javaxyq.action.����",new ClosureAction(battleAction));
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