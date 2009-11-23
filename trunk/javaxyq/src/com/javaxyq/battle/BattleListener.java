/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-15
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.util.EventListener;

/**
 * @author dewitt
 * @date 2009-11-15 create
 */
public interface BattleListener extends EventListener {
	/**
	 * ս��ʤ��
	 * @param e
	 */
	void battleWin(BattleEvent e);
	/**
	 * ս��ʧ��
	 * @param e
	 */
	void battleDefeated(BattleEvent e);
	/**
	 * ս����ʱ
	 * @param e
	 */
	void battleTimeout(BattleEvent e);
	/**
	 * ս�������
	 * @param e
	 */
	void battleBreak(BattleEvent e);
	
}
