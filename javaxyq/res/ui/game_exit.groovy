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

import com.javaxyq.core.GameMain;
import com.javaxyq.event.*;
import com.javaxyq.ui.*;

/**
 * 退出游戏对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
class game_exit extends PanelHandler {
	
	private static final String blogURL = "http://blog.csdn.net/Kylixs";

	public void exit_game(ActionEvent evt) {
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
	
}
