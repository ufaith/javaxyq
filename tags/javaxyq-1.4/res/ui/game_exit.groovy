/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui_script;

import java.awt.Desktop;
import java.net.URI;
import java.util.Timer;

import com.javaxyq.core.*;
import com.javaxyq.event.*;
import com.javaxyq.ui.*;
import com.javaxyq.data.*;

/**
 * �˳���Ϸ�Ի���ű�
 * @author dewitt
 * @date 2009-11-27 create
 */
class game_exit extends PanelHandler {
	
	private static final String blogURL = "http://blog.csdn.net/Kylixs";
	private Timer timer;
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
	}	

	public void dispose(PanelEvent evt) {
		println "dispose: system.mainwin "
	}
	
	public void exit_game(ActionEvent evt) {
		saveData();
		GameMain.exit();
	}
	public void visit_homepage(ActionEvent evt) {
		Desktop.getDesktop().browse(new URI(GameMain.getHomeURL()));
	}
	public void visit_blog(ActionEvent evt) {
		Desktop.getDesktop().browse(new URI(blogURL));
	}
	public void contributors(ActionEvent evt) {
		UIHelper.showDialog("contributors");
	}
	
	public void toggle_debug(ActionEvent evt) {
		GameMain.setDebug(!GameMain.isDebug());
		def btn = panel.findCompByName("debugbtn");
		if(GameMain.isDebug()) {
			btn.setText("�رյ���");
			println '����Ϸ����'
		}else {
			btn.setText("�򿪵���");
			println '�ر���Ϸ����'
		}
	}
	
	public void toggle_music(ActionEvent evt) {
		GameMain.setPlayingMusic(!GameMain.isPlayingMusic());
		def btn = panel.findCompByName("musicbtn");
		if(GameMain.isPlayingMusic()) {
			btn.setText("�ر�����");
			println '����Ϸ��������'
		}else {
			btn.setText("������");
			println '�ر���Ϸ��������'
		}
	}
	
	public void saveData() {
		DataStore.saveData();
	}
}
