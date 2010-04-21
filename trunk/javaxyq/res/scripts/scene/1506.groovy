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
		def elfNames = ['大海龟'];
		def random = new Random();
		int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.size()); 
			t2.add(Helper.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
				
		t1.add(GameMain.getPlayer());
		//进入战斗场景 
		GameMain.enterBattle(t1,t2);
		//监听战斗事件
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
	
	void onUnload(SceneEvent e) {
	}
	
}