/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-2-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.core;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;

import com.javaxyq.action.RandomMovementAction;
import com.javaxyq.data.DataStore;
import com.javaxyq.data.XmlDataLoader;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.Canvas;
import com.javaxyq.ui.GameWindow;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Cursor;

/**
 * @author dewitt
 * @date 2010-2-25 create
 */
public class AppletWindow extends JApplet implements GameWindow {
	@Override
	public void init() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
        //initCursor();
	}
	@Override
	public void destroy() {
		//DataStore.saveData();
	}
	
	@Override
	public void start() {
		Thread loadingThread = new Thread() {
			public void run() {
				GameMain.initApplet(AppletWindow.this, null);
				GameMain.loadGame();
			}
		};
		loadingThread.setDaemon(true);
		loadingThread.start();
	}
	
	@Override
	public void stop() {
		DataStore.saveData();
	}
	
    private DisplayMode displayMode;

    private Canvas canvas;

    private GraphicsDevice device;

    private JFrame fullScreenWindow;

	private boolean cursorHided;

    public void hideCursor() {
        // set invisible cursor
    	if(!cursorHided) {
    		cursorHided = true;
	        Image blankImage = new ImageIcon("").getImage();
	        Toolkit toolkit = Toolkit.getDefaultToolkit();
	        java.awt.Cursor blankCursor = toolkit.createCustomCursor(blankImage, new Point(0, 0),
	            "BLANK_CURSOR");
	        setCursor(blankCursor);
    	}
    }
    
    @Override
    public void showCursor() {
    }

    public synchronized void setFullScreen() {
        setVisible(false);
        //setState(JFrame.ICONIFIED);
        fullScreenWindow = new JFrame(GameMain.getApplicationName());
        fullScreenWindow.setContentPane(canvas);
        fullScreenWindow.setUndecorated(true);
        fullScreenWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fullScreenWindow.setCursor(getCursor());
        device.setFullScreenWindow(fullScreenWindow);
        if (displayMode != null && device.isDisplayChangeSupported()) {
            try {
                device.setDisplayMode(displayMode);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void restoreScreen() {
        device.setFullScreenWindow(null);
        if (fullScreenWindow != null) {
            fullScreenWindow.dispose();
        }
        //setState(JFrame.NORMAL);
        setContentPane(canvas);
        //pack();
        setVisible(true);
    }

    public boolean isFullScreen() {
        return device.getFullScreenWindow() != null;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas gameCanvas) {
        this.canvas = gameCanvas;
        canvas.setGameCursor(Cursor.DEFAULT_CURSOR);
        setContentPane(canvas);
        canvas.requestFocusInWindow();
        //pack();
    }
	@Override
	public void addWindowListener(WindowListener handler) {
	}
	@Override
	public void addWindowStateListener(WindowStateListener handler) {
	}
	@Override
	public void removeWindowListener(WindowListener handler) {
	}
	@Override
	public void removeWindowStateListener(WindowStateListener handler) {
	}
	
}
