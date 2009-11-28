/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import com.javaxyq.graph.Panel;
import com.javaxyq.ui.*;

/**
 * ����¼�����������
 * @author dewitt
 * @date 2009-11-26 create
 */
abstract class PanelHandler implements PanelListener{
	
	protected Panel panel;
	
	final public void actionPerformed(ActionEvent evt) {
		String cmd =evt.getCommand();
		//cmd�ĵ�һ��Ϊ����������������в���
		this.invokeMethod(cmd.split()[0],evt); 
	}
	
	public void dispose(PanelEvent evt) {
		println "dispose: ${this.getClass().getName()}"
	}
	
	public void initial(PanelEvent evt) {
		panel = evt.getSource();
		println "initial: ${this.getClass().getName()}"
	}	
	public void update(PanelEvent evt) {
		
	}
	
	public void close(ActionEvent evt) {
		UIHelper.hideDialog(panel);
	}
	
	public void help(ActionEvent evt) {
		println "help: ${this.getClass().getName()}"
	}
}