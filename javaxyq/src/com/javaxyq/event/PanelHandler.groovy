/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import com.javaxyq.util.ClosureTask;
import java.util.Timer;

import com.javaxyq.graph.Panel;
import com.javaxyq.ui.*;

/**
 * 面板事件处理器基类
 * @author dewitt
 * @date 2009-11-26 create
 */
abstract class PanelHandler implements PanelListener{
	
	protected Panel panel;
	private boolean autoUpdate; 
	private Timer timer;
	
	final public void actionPerformed(ActionEvent evt) {
		String cmd =evt.getCommand();
		//cmd的第一段为函数名，后面可以有参数
		this.invokeMethod(cmd.split()[0],evt); 
	}
	
	public void dispose(PanelEvent evt) {
		println "dispose: ${this.getClass().getName()}"
	}
	
	public void initial(PanelEvent evt) {
		panel = evt.getSource();
		println "initial: ${this.getClass().getName()}"
	}	
	synchronized public void update(PanelEvent evt) {
		
	}
	
	public void close(ActionEvent evt) {
		UIHelper.hideDialog(panel);
	}
	
	public void help(ActionEvent evt) {
		println "help: ${this.getClass().getName()}"
	}

	public void setAutoUpdate(boolean b) {
		if(b) {
			if(!timer) {
				timer = new Timer("update-${this.getClass().getName()}", true) ;
				timer.schedule(new ClosureTask({this.update(null)}), 100, 500);
			}
		}else {
			if(timer) {
				timer.cancel();
				timer = null;
			}
		}
		this.autoUpdate = b;
	}
	
	public boolean isAutoUpdate() {
		return autoUpdate;
	}	
}