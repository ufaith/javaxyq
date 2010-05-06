package com.javaxyq.ui;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.widget.Cursor;

public class DesktopWindow extends JFrame implements GameWindow {

    private static final long serialVersionUID = -8317898227965628232L;

    private DisplayMode displayMode;

    private Canvas canvas;

    private GraphicsDevice device;

    private JFrame fullScreenWindow;

	private boolean cursorHided;

    public DesktopWindow(DisplayMode displayMode) {
        this.displayMode = displayMode;
        setResizable(false);

        setTitle(GameMain.getApplicationName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
    }

    public void hideCursor() {
    	if(!cursorHided) {
    		cursorHided = true;
	        Toolkit toolkit = Toolkit.getDefaultToolkit();
	        Image blankImage = toolkit.createImage(new byte[0]);
	        java.awt.Cursor blankCursor = toolkit.createCustomCursor(blankImage, new Point(0, 0),
	            "BLANK_CURSOR");
	        setCursor(blankCursor);
    	}
    }

    public synchronized void setFullScreen() {
        setVisible(false);
        setState(JFrame.ICONIFIED);
        fullScreenWindow = new JFrame(GameMain.getApplicationName());
        fullScreenWindow.setContentPane(canvas);
        fullScreenWindow.setUndecorated(true);
        fullScreenWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        setState(JFrame.NORMAL);
        setContentPane(canvas);
        pack();
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
        pack();
    }

	@Override
	public void showCursor() {
	}
	
	@Override
	public Point getMousePosition() throws HeadlessException {
		Point p = super.getMousePosition();
		//减去标题栏的高度差
//		Dimension d1 = getSize();
//		Dimension d2 = canvas.getSize();
//		int dx = 0;
//		int dy = -(d1.height-d2.height)+2;
//		System.out.printf("mouse translate : (%s,%s)\n",dx,dy);
//		p.translate(dx, dy);
		SwingUtilities.convertPointToScreen(p, this);
		SwingUtilities.convertPointFromScreen(p, canvas);
		return p;
	}

}
