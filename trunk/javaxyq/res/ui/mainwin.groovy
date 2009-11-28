package ui_script;


import com.javaxyq.core.SpriteFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.*;
import com.javaxyq.graph.*;
import com.javaxyq.ui.*;
/**
 * 游戏主窗口
 * @author dewitt
 * @date 2009-11-26 create
 */
class mainwin extends PanelHandler{
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		println "initial：system.mainwin "
		def btnHeader = panel.findCompByName("btn-人物头像");
		def playerId = GameMain.getPlayer().getCharacter();
		def sprite = SpriteFactory.loadSprite("wzife/photo/facesmall/${playerId}.tcp");
		btnHeader.init(sprite);
		
	}

	public void dispose(PanelEvent evt) {
		println "dispose: system.mainwin "
	}
	
	/**
	 * 补充人物气血
	 */
	public void eke_player_hp(ActionEvent evt) {
		println "补充人物气血"
	}
	
	/**
	 * 补充人物魔法值
	 * @param evt
	 */
	public void eke_player_mp(ActionEvent evt) {
		println "补充人物魔法"
	}
	
	/**
	 * 补充召唤兽气血
	 */
	public void eke_summon_hp(ActionEvent evt) {
		println "补充召唤兽气血"
	}
	
	/**
	 * 补充召唤兽魔法值
	 * @param evt
	 */
	public void eke_summoned_mp(ActionEvent evt) {
		println "补充召唤兽魔法"
	}

	/**
	 * 全屏切换
	 * @param evt
	 */
	public void fullscreen(ActionEvent evt) {
		//GameMain.fullScreen();
	}
	public void world_map(ActionEvent evt) {
		UIHelper.showHideDialog('world_map');
	}
	public void scene_map(ActionEvent evt) {		
		UIHelper.showHideDialog('scene_map');
	}
	public void summon_status(ActionEvent evt) {
		UIHelper.showHideDialog('summon_status');
	}
	public void player_status(ActionEvent evt) {
		UIHelper.showHideDialog('player_status');
	}
	/**
	 * 输入聊天内容
	 * @param evt
	 */
	public void chat(ActionEvent evt) {
		TextField editor = (TextField) evt.getSource();
		String text = editor.getText();
		if(text !=null && text.length()>0) {
			GameMain.getPlayer().say(text);
		}
		editor.setText("");
	}
	public void attack(ActionEvent evt) {
	}
	public void open_item(ActionEvent evt) {
		UIHelper.showHideDialog('item');		
	}
	public void giving(ActionEvent evt) {
		//UIHelper.showDialog('giving');		
	}
	public void exchange(ActionEvent evt) {
		//UIHelper.showDialog('exchange');		
	}
	public void team(ActionEvent evt) {
	}
	public void task_list(ActionEvent evt) {
		UIHelper.showHideDialog('tasklist');		
	}
	/**
	 * 打开帮派
	 * @param evt
	 */
	public void open_org(ActionEvent evt) {
	}
	/**
	 * 法术快捷栏
	 * @param evt
	 */
	public void quick_magic(ActionEvent evt) {
	}
	public void friend_list(ActionEvent evt) {
	}
	/**
	 * 人物动作
	 * @param evt
	 */
	public void open_motion(ActionEvent evt) {
	}
	/**
	 * 系统设置
	 * @param evt
	 */
	public void system_setting(ActionEvent evt) {		
		UIHelper.showHideDialog('game_exit');		
	}
	/**
	 * 频道选择
	 * @param evt
	 */
	public void change_channel(ActionEvent evt) {
	}
	
}