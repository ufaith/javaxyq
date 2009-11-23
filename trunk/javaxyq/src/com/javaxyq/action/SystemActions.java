package com.javaxyq.action;

import java.io.IOException;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.graph.Panel;
import com.javaxyq.graph.TextField;
import com.javaxyq.util.BrowserLauncher;
import com.javaxyq.widget.Player;

/**
 * ϵͳ�¼��������
 */
public class SystemActions extends BaseAction {

    public void doAction(ActionEvent e) {
        String cmd = e.getCommand();
        Object source = e.getSource();
        System.out.println("action: "+cmd);
        if ("com.javaxyq.action.��������".equals(cmd)) {
            TextField editor = (TextField) e.getSource();
            String text = editor.getText();
            if(text !=null && text.length()>0) {
            	GameMain.getPlayer().say(text);
            }
            editor.setText("");
            //TODO ������ʷ��¼
        } else if ("com.javaxyq.action.ȫ���л�".equals(cmd)) {
           //XXX GameMain.fullScreen();
        } else if (cmd.startsWith("com.javaxyq.action.�˳���Ϸ")) {
            GameMain.doAction(e.getSource(), "com.javaxyq.action.beforeExit");
            System.exit(0);
        } else if (cmd.startsWith("com.javaxyq.action.��Ϸ��ҳ")) {
            try {
                BrowserLauncher.openURL(GameMain.getHomeURL());
            } catch (IOException e1) {
                System.err.println("��homeURLʧ�ܣ�");
                //e1.printStackTrace();
            }
        } else if (cmd.startsWith("com.javaxyq.action.��Ϸ����")) {
        	try {
        		//TODO �����Ϸ���Ͳ���
        		BrowserLauncher.openURL("http://blog.csdn.net/Kylixs");
        	} catch (IOException e1) {
        		System.err.println("��blogʧ�ܣ�");
        		//e1.printStackTrace();
        	}
        } else if (cmd.startsWith("com.javaxyq.action.���ﶯ��")) {
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
