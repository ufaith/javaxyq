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
class dhw implements SceneListener{
	
	void onInit(SceneEvent e) {
	}
	
	void onLoad(SceneEvent e) {
		
	}
	
	void onWalk(SceneEvent e) {
		String sceneId = e.getScene();
		Canvas canvas = GameMain.getSceneCanvas();

		int level = GameMain.getPlayer().getData().getLevel();
		def t1 = [],t2 = [];
		def elfs = ['2036'];
		def elfNames = ['�󺣹�'];
		def random = new Random();
		int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.size()); 
			t2.add(Helper.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
				
		t1.add(GameMain.getPlayer());
		//����ս������ 
		GameMain.enterBattle(t1,t2);
		//����ս���¼�
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
	
	void onUnload(SceneEvent e) {
	}
	
}