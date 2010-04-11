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
import com.javaxyq.data.*;
/**
 * 场景事件处理类
 * @author dewitt
 * @date 2009-11-23 create
 */
class wzg implements SceneListener{
	
	void onInit(SceneEvent e) {
		println "初始化场景：五庄观.."
	}
	
	void onLoad(SceneEvent e) {
		println "已加载场景：五庄观.."
		Canvas canvas = GameMain.getSceneCanvas();
		def $道童 = canvas.findNpc("传送道童");
		def $配色师 = canvas.findNpc("配色师");
		//play background music
		
	}
	
	void onUnload(SceneEvent e) {
		//stop background music
	}
	
}