/**
 * 
 */
package com.javaxyq.battle;

import com.javaxyq.widget.Player;

/**
 * @author dewitt
 *
 */
class Command {
	private String cmd;
	private Player source;
	private Player target;
	private Map params = [:];
	public Command(String cmd,Player source) {
		this(source,null,cmd);
	}
	public Command(String cmd,Player source,Player target) {
		this.cmd = cmd;
		this.source = source;
		this.target = target;
	}
	public Command(String cmd,Player source,Player target,Map params) {
		this(cmd,source,target);
		this.params = params;
	}
	public String getCmd() {
		return this.cmd;
	}
	/**
	 * 添加一个参数
	 * @param name
	 * @param value
	 */
	public void add(String name,Object value) {
		this.params[name] = value;
	}
	public Object get(String name) {
		return this.params[name];
	}
	public int getInt(String name) {
		def val= this.params[name];
		return val?val.toInteger():0;
	}
	public boolean getBool(String name) {
		def val = this.params[name];
		return val?val.toBoolean():false;
	}
	
	/**
	 * @return the source
	 */
	public Player getSource() {
		return source;
	}
	/**
	 * @return the target
	 */
	public Player getTarget() {
		return target;
	}
	
	public String toString() {
		return "${source.name} $cmd ${target?target.name:''} params:$params";
	}	
}
