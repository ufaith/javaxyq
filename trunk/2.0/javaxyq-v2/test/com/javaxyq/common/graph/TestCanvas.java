package com.javaxyq.common.graph;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * @author gongdewei
 * @date 2011-7-24 create
 */
public class TestCanvas extends JPanel {

	private static final long serialVersionUID = -7709678466279021561L;
	public static final Object FADE_LOCK = new Object();
	public static final Object UPDATE_LOCK = new Object();
	protected long updateInterval = 20;
	private Character character;
	private BufferedImage offscreenImage;
	private Graphics2D offscreenGraphics;
	private long lastTime;
	public boolean canvasValid = true;
	
	public TestCanvas(int width, int height) {
		setIgnoreRepaint(true);
		setFocusable(true);
		requestFocus(true);
		setLayout(null);
		setSize(width, height);
		setPreferredSize(new Dimension(width,height));
		
		offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);

		// 禁止tab键切换焦点
		Set<AWTKeyStroke> keystrokes = new HashSet<AWTKeyStroke>(
				getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		keystrokes.remove(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keystrokes);

		DrawThread drawThread = new DrawThread();
		drawThread.start();
	}
	
	private synchronized void drawCanvas() {
		Graphics g = this.getGraphics();
		long currTime = System.currentTimeMillis();
		if (lastTime == 0)
			lastTime = currTime;
		long elapsedTime = currTime - lastTime;
		lastTime = currTime;
		if (g != null && offscreenGraphics!=null) {
			this.draw(offscreenGraphics, elapsedTime);
			// draw to real graphics
			g.drawImage(offscreenImage, 0, 0, null);
			g.dispose();
		}
	}
	public synchronized void draw(Graphics g, long elapsedTime) {
		//super.draw(g, elapsedTime);
		g.clearRect(0, 0, getWidth(), getHeight());
		if(character !=null) {
			character.update(elapsedTime);
			character.draw(g);
		}
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	private final class DrawThread extends Thread {
		public DrawThread() {
			this.setName("DrawThread");
			this.setDaemon(true);
		}
		public void run() {
			while (canvasValid ) {
				// System.out.println(this.getId()+" "+this.getName());
				synchronized (UPDATE_LOCK) {
					if (isShowing() && isVisible()) {
						drawCanvas();
						//drawCount ++;
					}
					try {
						UPDATE_LOCK.wait(updateInterval );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
