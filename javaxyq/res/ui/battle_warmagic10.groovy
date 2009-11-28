/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui_script;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.graph.Label;
import com.javaxyq.widget.Animation;

/**
 * 
 * @author dewitt
 * @date 2009-11-27 create
 */
class battle_warmagic10 extends PanelHandler {
	
	private boolean initialized = false;
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		if(initialized) return;
		initialized = true;
		def clickAction = { event ->
			String magicId = event.source.text;
			GameMain.getBattleCanvas().setSelectedMagic(magicId);
		}
		
		String[] magicIds = ['0326','0327','0328','0329'];
		String[] magicNames = ['À×»÷','ÂäÑÒ','Ë®¹¥','ÁÒ»ð'];
		for (int i = 0; i < magicIds.length; i++) {
			Animation anim = SpriteFactory.loadAnimation("/wzife/magic/normal/${magicIds[i]}.tcp");
			Label label = panel.findCompByName("magic${i+1}");
			label.setAnim(anim);
			label.setText(magicIds[i]);
			label.setToolTipText(magicNames[i]);
			label.mouseClicked= clickAction;
		}
	}

}
