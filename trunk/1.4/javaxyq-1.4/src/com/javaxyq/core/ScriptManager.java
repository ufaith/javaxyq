/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.core;

import com.javaxyq.event.PlayerListener;

/**
 * Ω≈±æπ‹¿Ì∆˜
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class ScriptManager {
	private static final ScriptManager instance = new ScriptManager();
	private ScriptManager() {
	}
	public static ScriptManager getInstance() {
		return instance;
	}
	
	public PlayerListener findListener(String npcId) {
		return GroovyScript.loadClass("scripts/npc/n"+npcId+".groovy", PlayerListener.class);
	}
}
