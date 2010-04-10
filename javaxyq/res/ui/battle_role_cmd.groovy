/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui_script;

import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;

/**
 * @author dewitt
 * @date 2009-11-27 create
 */
class battle_role_cmd extends PanelHandler {

	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
	}
	
	public void warmagic(ActionEvent evt) {
		BattleCanvas canvas = GameMain.getBattleCanvas();
		canvas.selectMagic();
	}
	public void waritem(ActionEvent evt) {
		BattleCanvas canvas = GameMain.getBattleCanvas();
		canvas.selectItem();
	}
	public void wardefend(ActionEvent evt) {
		BattleCanvas canvas = GameMain.getBattleCanvas();
		canvas.defendCmd();
	}
	public void warcatch(ActionEvent evt) {
		
	}
	public void warrunaway(ActionEvent evt) {
		BattleCanvas canvas = GameMain.getBattleCanvas();
		canvas.runawayCmd();
	}
}
