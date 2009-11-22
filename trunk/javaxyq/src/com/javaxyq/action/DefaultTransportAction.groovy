package com.javaxyq.action;

import com.javaxyq.core.*;
import com.javaxyq.graph.*;
import com.javaxyq.event.*;

/**
 * Ĭ�ϵĴ����¼�������
 * @author dewitt
 */
class DefaultTransportAction extends BaseAction{
	public void doAction(ActionEvent e) {
		String sceneId = e.getArgumentAsString(0);
		int x = e.getArgumentAsInt(1);
		int y = e.getArgumentAsInt(2);
		Main.setScene(sceneId, x, y);
		GameMain.hideDialog(GameMain.getTalkPanel());
				
	}
}