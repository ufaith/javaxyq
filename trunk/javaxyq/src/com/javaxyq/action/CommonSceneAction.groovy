package com.javaxyq.action;

import com.javaxyq.core.*;
import com.javaxyq.graph.*;
import com.javaxyq.config.*;
import com.javaxyq.event.*;
import com.javaxyq.ui.*;

class CommonSceneAction implements SceneListener{
	
	void onInit(SceneEvent e) {
		String sceneId = e.getScene();
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
			def action = { evt->
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
			GameMain.registerAction("com.javaxyq.action.ʦ������",new ClosureAction(action));

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
			
		}
		//play background music
		
	}
	
	void onUnload(SceneEvent e) {
		//stop background music
		//MapConfig cfg = (MapConfig) ResourceStore.getInstance().findConfig(id);
				;
	}
	
}