/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-2-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.ui;

import java.awt.Point;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JLayeredPane;

/**
 * @author dewitt
 * @date 2010-2-25 create
 */
public interface GameWindow {
	void setFullScreen();

	void restoreScreen();

	boolean isFullScreen();

	Canvas getCanvas();

	void setCanvas(Canvas gameCanvas);
	
	void hideCursor();
	
	void addWindowListener(WindowListener handler);
	void removeWindowListener(WindowListener handler);
	
	void addWindowStateListener(WindowStateListener handler);
	void removeWindowStateListener(WindowStateListener handler);

	/**
	 * @return
	 */
	Point getMousePosition();

	/**
	 * @return
	 */
	JLayeredPane getLayeredPane();

	/**
	 * 
	 */
	void showCursor();
	
}
