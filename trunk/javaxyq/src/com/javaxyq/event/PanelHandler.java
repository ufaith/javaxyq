/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import com.javaxyq.graph.Panel;
import com.javaxyq.ui.UIHelper;

/**
 * 面板事件处理器基类
 * @author dewitt
 * @date 2009-11-26 create
 */
public abstract class PanelHandler implements PanelListener{
	
	protected Panel panel;
	private boolean autoUpdate; 
	private Timer timer;
	protected long period = 1000;
	
	public void actionPerformed(ActionEvent evt) {
		String cmd =evt.getCommand();
		//cmd的第一段为函数名，后面可以有参数
		this.invokeMethod0(cmd.split(" ")[0],evt); 
	}
	
	public void dispose(PanelEvent evt) {
		System.out.println("dispose: "+this.getClass().getName());
	}
	
	public void initial(PanelEvent evt) {
		panel = (Panel) evt.getSource();
		System.out.println("initial: "+this.getClass().getName());
	}	
	synchronized public void update(PanelEvent evt) {
		
	}
	
	public void close(ActionEvent evt) {
		UIHelper.hideDialog(panel);
	}
	
	public void help(ActionEvent evt) {
		System.out.println("help: "+this.getClass().getName());
	}

	public void setAutoUpdate(boolean b) {
		if(b) {
			if(timer == null) {
				timer = new Timer("update-"+this.getClass().getName(), true) ;
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						update(null);
					}
					}, 100, period );
			}
		}else {
			if(timer!=null) {
				timer.cancel();
				timer = null;
			}
		}
		this.autoUpdate = b;
	}
	
	public boolean isAutoUpdate() {
		return autoUpdate;
	}	
	/**
	 * invoke a  method
	 * @param mName method name
	 * @param arg argument 
	 * @return
	 */
	private Object invokeMethod0(String mName, Object arg) {
		try {
			Method m = this.getClass().getMethod(mName, arg.getClass());
			return m.invoke(this, arg);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}