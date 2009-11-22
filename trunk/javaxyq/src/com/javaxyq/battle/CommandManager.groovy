/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-11
 * http://kylixs.blog.163.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.javaxyq.widget.Player;
import com.javaxyq.battle.BattleCalculator;
import com.javaxyq.core.GameMain;
import com.javaxyq.ui.UIHelper;
/**
 * @author dewitt
 * @date 2009-11-11 create
 */
class CommandManager  {
	private BattleCanvas canvas;
	private List cmdlist;
	private List playerlist;
	private CommandInterpreter interpretor;
	private BattleCalculator battleCalculator;
	
	public CommandManager(BattleCanvas canvas) {
		this.canvas = canvas;
		this.interpretor = new CommandInterpreter(canvas);
		this.cmdlist = [];
		this.battleCalculator = new BattleCalculator();
	}
	

	/**
	 * 回合战斗
	 */
	synchronized public void turnBattle() {
		turnBegin();
		//TODO 计算战斗结果
		def params = [:];//影响因素
		def t1 = canvas.getAdversaryTeam();
		def t2 = canvas.getOwnsideTeam();
		def results = battleCalculator.calc(cmdlist,t2,t1,params);
		//依次执行指令
		results.each{ cmd ->
			try {
				println "执行：$cmd";
				this.interpretor.exec(cmd);
			}catch(Exception e) {
				println "战斗指令执行失败！$cmd"
				e.printStackTrace();
			}
		}
		turnEnd();
	}

	private Random random = new Random();
	/**
	 * 回合开始
	 */
	protected void turnBegin() {
		//生成npc的指令
		def t1 = canvas.getAdversaryTeam();
		def t2 = canvas.getOwnsideTeam();

		t1.each{ elf ->
			def target = t2.get(random.nextInt(t2.size()));
			cmdlist << new Command('attack',elf,target);
		}

	}
		/**
	 * 回合结束
	 */
	protected void turnEnd() {
		cmdlist = [];
		// TODO fireEvent
		def t1 = canvas.getAdversaryTeam();
		def t2 = canvas.getOwnsideTeam();
		//如果敌方单位都已死亡，则我方胜利
		boolean win = true;
		for(int i=0;i<t1.size();i++) {
			def elf = t1[i];
			if(elf.data.hp>0) {
				win = false;
				break;
			}
		}
		if(win) {
			canvas.fireBattleEvent(new BattleEvent(canvas,BattleEvent.BATTLE_WIN));
			return;
		}
		
		//如果我方单位全部死亡，则战斗失败
		boolean failure = true;
		for(int i=0;i<t2.size();i++) {
			def player = t2[i];
			if(player.data.hp>0) {
				failure = false;
				break;
			}
		}
		if(failure) {
			//战斗失败
			canvas.fireBattleEvent(new BattleEvent(canvas,BattleEvent.BATTLE_DEFEATED));
			return;
		}
		
		canvas.turnBegin();
	}	
	synchronized public void addCmd(Command cmd) {
		cmdlist << cmd;
	}

}
