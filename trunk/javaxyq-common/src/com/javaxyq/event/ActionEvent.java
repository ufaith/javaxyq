/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.event;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class ActionEvent extends java.awt.event.ActionEvent {

	// private long when;

	private Object[] arguments;

	public ActionEvent(Object source, String command, Object[] args) {
		super(source, java.awt.event.ActionEvent.ACTION_PERFORMED, command, 0, 0);
		if (args == null) {
			args = new Object[] {};
		}
		this.arguments = args;
	}

	// public ActionEvent(Object source,Object ... args) {
	//        
	// }

	public ActionEvent(Object source, String command) {
		this(source, command, null);
	}

	/**
	 * @param evt
	 */
	public ActionEvent(java.awt.event.ActionEvent evt) {
		super(evt.getSource(), java.awt.event.ActionEvent.ACTION_PERFORMED, evt.getActionCommand());

	}

	public String getCommand() {
		return getActionCommand();
	}

	public Object[] getArguments() {
		return arguments;
	}

	public Object getArgument(int i) {
		if (arguments == null || i < 0 || arguments.length < i) {
			return null;
		}
		return arguments[i];
	}

	public String getArgumentAsString(int i) {
		if (arguments == null || i < 0 || arguments.length < i) {
			return null;
		}
		return (String) arguments[i];
	}

	public int getArgumentAsInt(int i) {
		if (arguments == null || i < 0 || arguments.length <= i) {
			return 0;
		}
		if (arguments[i] instanceof Integer) {
			Integer val = (Integer) arguments[i];
			return val;
		}
		try {
			return Integer.parseInt((String) arguments[i]);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public boolean isConsumed() {
		return super.isConsumed();
	}
	@Override
	public void consume() {
		super.consume();
	}
}
