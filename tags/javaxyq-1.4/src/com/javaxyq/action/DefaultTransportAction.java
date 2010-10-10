package com.javaxyq.action;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.ui.UIHelper;

/**
 * 默认的传送事件处理器
 * @author dewitt
 */
public class DefaultTransportAction extends BaseAction{
	
	public void doAction(ActionEvent e) {
		String sceneId = e.getArgumentAsString(0);
		int x = e.getArgumentAsInt(1);
		int y = e.getArgumentAsInt(2);
		GameMain.setScene(sceneId, x, y);
		UIHelper.hideDialog(UIHelper.getTalkPanel());
				
	}
}