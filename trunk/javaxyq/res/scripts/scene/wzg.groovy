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
 * �����¼�������
 * @author dewitt
 * @date 2009-11-23 create
 */
class wzg implements SceneListener{
	
	void onInit(SceneEvent e) {
		println "��ʼ����������ׯ��.."
	}
	
	void onLoad(SceneEvent e) {
		println "�Ѽ��س�������ׯ��.."
		Canvas canvas = GameMain.getSceneCanvas();
		def $��ͯ = canvas.findNpc("���͵�ͯ");
		def $��ɫʦ = canvas.findNpc("��ɫʦ");
		//play background music
		
	}
	
	void onUnload(SceneEvent e) {
		//stop background music
	}
	
}