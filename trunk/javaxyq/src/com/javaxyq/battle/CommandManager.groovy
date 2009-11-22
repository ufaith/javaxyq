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
	 * �غ�ս��
	 */
	synchronized public void turnBattle() {
		turnBegin();
		//TODO ����ս�����
		def params = [:];//Ӱ������
		def t1 = canvas.getAdversaryTeam();
		def t2 = canvas.getOwnsideTeam();
		def results = battleCalculator.calc(cmdlist,t2,t1,params);
		//����ִ��ָ��
		results.each{ cmd ->
			try {
				println "ִ�У�$cmd";
				this.interpretor.exec(cmd);
			}catch(Exception e) {
				println "ս��ָ��ִ��ʧ�ܣ�$cmd"
				e.printStackTrace();
			}
		}
		turnEnd();
	}

	private Random random = new Random();
	/**
	 * �غϿ�ʼ
	 */
	protected void turnBegin() {
		//����npc��ָ��
		def t1 = canvas.getAdversaryTeam();
		def t2 = canvas.getOwnsideTeam();

		t1.each{ elf ->
			def target = t2.get(random.nextInt(t2.size()));
			cmdlist << new Command('attack',elf,target);
		}

	}
		/**
	 * �غϽ���
	 */
	protected void turnEnd() {
		cmdlist = [];
		// TODO fireEvent
		def t1 = canvas.getAdversaryTeam();
		def t2 = canvas.getOwnsideTeam();
		//����з���λ�������������ҷ�ʤ��
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
		
		//����ҷ���λȫ����������ս��ʧ��
		boolean failure = true;
		for(int i=0;i<t2.size();i++) {
			def player = t2[i];
			if(player.data.hp>0) {
				failure = false;
				break;
			}
		}
		if(failure) {
			//ս��ʧ��
			canvas.fireBattleEvent(new BattleEvent(canvas,BattleEvent.BATTLE_DEFEATED));
			return;
		}
		
		canvas.turnBegin();
	}	
	synchronized public void addCmd(Command cmd) {
		cmdlist << cmd;
	}

}
