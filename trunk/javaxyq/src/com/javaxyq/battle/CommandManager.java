/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-11
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.javaxyq.widget.Player;
/**
 * @author dewitt
 * @date 2009-11-11 create
 */
public class CommandManager  {
	private BattleCanvas canvas;
	private List cmdlist;
	private List playerlist;
	private CommandInterpreter interpretor;
	private BattleCalculator battleCalculator;
	
	public CommandManager(BattleCanvas canvas) {
		this.canvas = canvas;
		this.interpretor = new CommandInterpreter(canvas);
		this.cmdlist = new ArrayList();
		this.battleCalculator = new BattleCalculator();
	}
	

	/**
	 * �غ�ս��
	 */
	synchronized public void turnBattle() {
		turnBegin();
		//TODO ����ս�����
		Map params = new HashMap();//Ӱ������
		List<Player> t1 = canvas.getAdversaryTeam();
		List<Player> t2 = canvas.getOwnsideTeam();
		List<Command> results = battleCalculator.calc(cmdlist,t2,t1,params);
		//����ִ��ָ��
		for (int i = 0; i < results.size(); i++) {
			Command cmd = results.get(i);
			try {
				System.out.println("ִ�У�$cmd");
				this.interpretor.exec(cmd);
			}catch(Exception e) {
				System.out.println("ս��ָ��ִ��ʧ�ܣ�$cmd");
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
		List<Player> t1 = canvas.getAdversaryTeam();
		List<Player> t2 = canvas.getOwnsideTeam();
		for (int i = 0; i < t1.size(); i++) {
			Player elf = t1.get(i);
			Player target = t2.get(random.nextInt(t2.size()));
			cmdlist.add( new Command("attack",elf,target));
		}

	}
		/**
	 * �غϽ���
	 */
	protected void turnEnd() {
		cmdlist.clear();
		// TODO fireEvent
		List<Player> t1 = canvas.getAdversaryTeam();
		List<Player> t2 = canvas.getOwnsideTeam();
		//����з���λ�������������ҷ�ʤ��
		boolean win = true;
		for(int i=0;i<t1.size();i++) {
			Player elf = t1.get(i);
			if(elf.getData().hp > 0) {
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
			Player player = t2.get(i);
			if(player.getData().hp>0) {
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
		cmdlist.add(cmd);
	}

}
