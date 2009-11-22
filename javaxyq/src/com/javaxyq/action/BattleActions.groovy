package com.javaxyq.action;

import com.javaxyq.battle.*;
import com.javaxyq.core.*;
import com.javaxyq.graph.*;
import com.javaxyq.ui.*;
import com.javaxyq.widget.*;
import com.javaxyq.model.*;


/**
 * 战斗系统的相关事件处理
 * @author dewitt
 */
class BattleActions extends BaseAction {
		
	private ItemDetailLabel detailLabel = new ItemDetailLabel();
	
	public void doAction(ActionEvent e) {
		String cmd = e.getCommand();
		Object source = e.getSource();
		System.out.println("BattleActions:" + cmd);
		BattleCanvas canvas = GameMain.getBattleCanvas();
		switch(cmd) {
			case ~/.*\.initial$/:
				
				
				break;
			case ~/.*\.dispose$/:
				break;
			case ~/.*\.warmagic10$/://初始化法术选择对话框
				//268,110 -> 358,110
				//268,156
				//大小：40*40  ，间距：50*6，纵向排列：5*2
				final Panel panel = source;
				def clickAction = { event ->
					String magicId = event.source.text;
					canvas.setSelectedMagic(magicId);
					
				}
				
				String[] magicIds = ['0326','0327','0328','0329'];
				String[] magicNames = ['雷击','落岩','水攻','烈火'];
				for (int i = 0; i < magicIds.length; i++) {
					Animation anim = SpriteFactory.loadAnimation("/wzife/magic/normal/${magicIds[i]}.tcp");
					Label label = panel.getComponentByName("magic${i+1}");
					label.setAnim(anim);
					label.setText(magicIds[i]);
					label.setToolTipText(magicNames[i]);
					label.mouseClicked= clickAction;
				}
				
				break;
			case ~/.*\.useitem$/://初始化道具选择对话框
				
				break;
			case ~/.*\.warmagic$/:
				canvas.selectMagic();
				break;
			case ~/.*\.waritem$/:
				canvas.selectItem();
				break;
			case ~/.*\.wardefend$/:
				canvas.defendCmd();
				break;
			case ~/.*\.warrunaway$/:
				canvas.runawayCmd();
				break;
		}
	}

}