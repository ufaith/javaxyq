/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import com.javaxyq.graph.Panel;


/**
 * @author dewitt
 * @date 2009-11-26 create
 */
class PanelEvent extends ActionEvent{
	
	public static final String INITIAL = "initial";
	
	public static final String UPDATE= "update";
	
	public static final String DISPOSE = "dispose";

	private Panel panel;
	
	public PanelEvent(Object source, String command, Object[] args) {
		super(source, command, args);
		if (source instanceof Panel) {
			this.panel = (Panel) source;
		}
	}
	
	public PanelEvent(Object source, String command) {
		super(source, command);
		if (source instanceof Panel) {
			this.panel = (Panel) source;
		}
	}
	
	public PanelEvent(Object source, String command,Panel panel) {
		this(source, command,panel,null);
	}
	
	public PanelEvent(Object source, String command,Panel panel, Object[] args) {
		super(source, command,args);
		this.panel = panel;
	}
	
	public Panel getPanel() {
		return panel;
	}
}
