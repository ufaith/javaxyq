package ui_script;
import com.javaxyq.util.ClosureTask;


import java.awt.Point;
import java.util.Timer;

import com.javaxyq.core.DataStore;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.*;
import com.javaxyq.graph.*;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.ui.*;
/**
 * 游戏主窗口
 * @author dewitt
 * @date 2009-11-26 create
 */
class mainwin extends PanelHandler{
	
	private Timer timer;
	
	private Label sceneLabel;
	private Label coordinateLabel;
	private Label hpTrough ;
	private Label mpTrough ;
	private Label spTrough ;
	private Label expTrough ;

	public void initial(PanelEvent evt) {
		super.initial(evt);
		println "initial：system.mainwin "
		def btnHeader = panel.findCompByName("btn-人物头像");
		def playerId = GameMain.getPlayer().getCharacter();
		def sprite = SpriteFactory.loadSprite("wzife/photo/facesmall/${playerId}.tcp");
		btnHeader.init(sprite);
		coordinateLabel = (Label) panel.findCompByName("人物坐标");
		sceneLabel = (Label) panel.findCompByName("地图名称");
		hpTrough = (Label) panel.findCompByName("人物气血");
		mpTrough = (Label) panel.findCompByName("人物魔法");
		spTrough = (Label) panel.findCompByName("人物愤怒");
		expTrough = (Label) panel.findCompByName("人物经验");
		
		updateCoords();
		timer = new Timer();
		timer.schedule(new ClosureTask({this.update(null)}), 500, 500);
	}

	public void dispose(PanelEvent evt) {
		println "dispose: system.mainwin "
	}
	
	public void update(PanelEvent evt) {
		if (GameMain.getPlayer() == null)
			return;
		updateCoords();
		Canvas canvas = GameMain.getGameCanvas();
		def player = canvas.getPlayer();
		if(!player)return;
		PlayerVO playerVO = player.getData();
		int maxLen = 50;
		// 人物气血
		int len = playerVO.getHp() * maxLen / playerVO.getMaxHp();
		hpTrough.setSize(len, hpTrough.getHeight());
		// 人物魔法
		len = playerVO.getMp() * maxLen / playerVO.getMaxMp();
		mpTrough.setSize(len, mpTrough.getHeight());
		// 人物愤怒
		len = playerVO.getSp() * maxLen / 150;
		spTrough.setSize(len, spTrough.getHeight());
		// 人物经验
		len = (int) (playerVO.getExp() * maxLen / DataStore.getLevelExp(playerVO.getLevel()));
		expTrough.setSize(len, expTrough.getHeight());

		// TODO summon状态
		// summon气血
		// summon魔法
		// summon经验
	}

	private void updateCoords() {
		Canvas canvas = GameMain.getGameCanvas();
		if(canvas == GameMain.getSceneCanvas()) {
			// 人物坐标
			Point pp = canvas.getPlayerSceneLocation();
			String strCoordinate = "X:" + pp.@x + " Y:" + pp.@y;
			coordinateLabel.setText(strCoordinate);
			sceneLabel.setText(canvas.getSceneName());
		}
		
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
	 * 补充summon气血
	 */
	public void eke_summon_hp(ActionEvent evt) {
		println "补充summon气血"
	}
	
	/**
	 * 补充summon魔法值
	 * @param evt
	 */
	public void eke_summoned_mp(ActionEvent evt) {
		println "补充summon魔法"
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