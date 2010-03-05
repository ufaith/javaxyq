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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.javaxyq.action.DefaultTransportAction;
import com.javaxyq.action.RandomMovementAction;
import com.javaxyq.graph.Canvas;
import com.javaxyq.graph.GameWindow;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.ClassUtil;
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
	}
	
	@Override
	public void start() {
		Thread loadingThread = new Thread() {
			public void run() {
				loadGame();
			}
		};
		loadingThread.setDaemon(true);
		loadingThread.start();
	}
	
	private void loadGame() {
		GameMain.setDebug(false);
		GameMain.setShowCopyright(false);
		GameMain.setApplicationName("JavaXYQ ");
		GameMain.setVersion("1.4 M2");
		GameMain.setHomeURL("http://javaxyq.googlecode.com/");
		GameMain.initApplet(this, null);
		GameMain.updateLoading("loading game ...");
		UIHelper.init();
		//GameMain.updateLoading("loading game ...");
		GameMain.loadGame();
		
		GameMain.updateLoading("loading groovy ...");
		ClassUtil.init();
		GameMain.updateLoading("loading cursor ...");
		Main.defCursors();
		GameMain.setCursor(Cursor.DEFAULT_CURSOR);
		
		GameMain.updateLoading("loading actions ...");
		Main.defActions();
		GameMain.updateLoading("loading scenes ...");
		Main.defScenes();
		GameMain.updateLoading("loading talks ...");
		Main.defTalks();
		GameMain.updateLoading("loading ui ...");
		Main.preprocessUIs();
		
		GameMain.updateLoading("loading npcs ...");
		Helper.loadNPCs();
		
		GameMain.registerAction("com.javaxyq.action.transport",new DefaultTransportAction());
		MovementManager.addMovementAction("random", new RandomMovementAction());
		
		//task
		TaskManager.instance.register("school", "com.javaxyq.task.SchoolTaskCoolie");
		
		GameMain.updateLoading("loading data ...");
		DataStore.init();
		ItemManager.init();
		DataStore.loadData();
		GameMain.updateLoading("starting game ...");
		GameMain.stopLoading();
		//GameMain.setPlayingMusic(false);//debug
	}
	
	@Override
	public void stop() {
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
