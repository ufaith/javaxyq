package com.javaxyq.graph;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.widget.Cursor;

public class Window extends JFrame {

    private static final long serialVersionUID = -8317898227965628232L;

    private DisplayMode displayMode;

    private Canvas canvas;

    private GraphicsDevice device;

    private JFrame fullScreenWindow;

    public Window(DisplayMode displayMode) {
        this.displayMode = displayMode;
        setResizable(false);

        setTitle(GameMain.getApplicationName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
        initCursor();
    }

    private void initCursor() {
        // set invisible cursor
        Image blankImage = new ImageIcon("").getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        java.awt.Cursor blankCursor = toolkit.createCustomCursor(blankImage, new Point(0, 0),
            "BLANK_CURSOR");
        setCursor(blankCursor);

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

}
