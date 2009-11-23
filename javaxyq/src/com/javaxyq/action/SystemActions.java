package com.javaxyq.action;

import java.io.IOException;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.graph.Panel;
import com.javaxyq.graph.TextField;
import com.javaxyq.util.BrowserLauncher;
import com.javaxyq.widget.Player;

/**
 * 系统事件处理代码
 */
public class SystemActions extends BaseAction {

    public void doAction(ActionEvent e) {
        String cmd = e.getCommand();
        Object source = e.getSource();
        System.out.println("action: "+cmd);
        if ("com.javaxyq.action.聊天输入".equals(cmd)) {
            TextField editor = (TextField) e.getSource();
            String text = editor.getText();
            if(text !=null && text.length()>0) {
            	GameMain.getPlayer().say(text);
            }
            editor.setText("");
            //TODO 输入历史记录
        } else if ("com.javaxyq.action.全屏切换".equals(cmd)) {
           //XXX GameMain.fullScreen();
        } else if (cmd.startsWith("com.javaxyq.action.退出游戏")) {
            GameMain.doAction(e.getSource(), "com.javaxyq.action.beforeExit");
            System.exit(0);
        } else if (cmd.startsWith("com.javaxyq.action.游戏主页")) {
            try {
                BrowserLauncher.openURL(GameMain.getHomeURL());
            } catch (IOException e1) {
                System.err.println("打开homeURL失败！");
                //e1.printStackTrace();
            }
        } else if (cmd.startsWith("com.javaxyq.action.游戏博客")) {
        	try {
        		//TODO 添加游戏博客参数
        		BrowserLauncher.openURL("http://blog.csdn.net/Kylixs");
        	} catch (IOException e1) {
        		System.err.println("打开blog失败！");
        		//e1.printStackTrace();
        	}
        } else if (cmd.startsWith("com.javaxyq.action.人物动作")) {
            Player player = GameMain.getSceneCanvas().getPlayer();
            //String desc = cmd.substring(23);
            //player.setState(desc);
        } else if (cmd.startsWith("com.javaxyq.action.dialog.")) {
            Panel dlg = DialogFactory.getDialog(cmd);
            GameMain.showHideDialog(dlg);
        } else if (cmd.startsWith("com.javaxyq.action.transport")) {
            String sceneId = e.getArgumentAsString(0);
            int x = e.getArgumentAsInt(1);
            int y = e.getArgumentAsInt(2);
            GameMain.fadeToMap(sceneId, x, y);
        } else if (cmd.startsWith("com.javaxyq.action.")) {

        } else {

        }
    }

}
