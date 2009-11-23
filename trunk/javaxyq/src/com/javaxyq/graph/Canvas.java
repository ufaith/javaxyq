package com.javaxyq.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;

//DONE ���Ӷ����Ĳ��Ŵ�������ʹ�ü��ܶ�����ֻ���ţ��Σ������������Ч��

public abstract class Canvas extends JPanel {

	private class AnimatorThread extends Thread {
		/** ������ʱ�� */
		private long duration;

		private boolean increased;

		/**
		 * ����ˢ�µļ��
		 */
		private long interval;

		private long passTime;
		
		/**
		 * @param duration
		 * @param interval
		 * @param increased
		 *            true������alpha,false ���Ǽ���alpha
		 */
		public AnimatorThread(long duration, long interval, boolean increased) {
			this.duration = duration;
			this.interval = interval;
			this.increased = increased;
			setName("animator");
		}

		@Override
		public void run() {
			synchronized (FADE_LOCK) {
				while (passTime < duration) {
	            	//System.out.println(this.getId()+" "+this.getName());
					synchronized (UPDATE_LOCK) {
						passTime += interval;
						alpha = (int) (255.0 * passTime / duration);
						if (!increased) {
							alpha = 255 - alpha;
						}
						if (alpha < 0) {
							alpha = 0;
						}
						if (alpha > 255) {
							alpha = 255;
						}
					}
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
					}
				}
				Canvas.this.fading = false;
				// System.out.println("faded");
			}
		}
	}

	private final class DrawThread extends Thread {
		{
			this.setName("DrawThread");
			this.setDaemon(true);
		}

		public void run() {
			while (canvasValid) {
				//System.out.println(this.getId()+" "+this.getName());
				synchronized (Canvas.UPDATE_LOCK) {
					if(isShowing() && isVisible()) {
						drawCanvas();
					}
				}
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final Object FADE_LOCK = new Object();

	public static final Object UPDATE_LOCK = new Object();
	
	public static final Object MOVEMENT_LOCK = new Object();
	private boolean canvasValid = true;

	protected int alpha = 0;

	private Thread drawThread = new DrawThread();

	private boolean fading;

	private Cursor gameCursor;// ���ָ��

	private long lastTime;

	/**
	 * ��������
	 */
	private Animation movingObject = null;

	private Point movingOffset = new Point();

	private List<Player> npcs;

	private Graphics2D offscreenGraphics;

	private BufferedImage offscreenImage;
	private Player player;// ��ɫ

	private int maxWidth;
	private int maxHeight;

	private int viewportY;

	private int viewportX;

	public Canvas() {
		npcs = new ArrayList<Player>();
		Dimension d = GameMain.getWindowSize();
		offscreenImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_USHORT_565_RGB);
		offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);

		NullRepaintManager.install();
		setIgnoreRepaint(true);
		setFocusable(true);
		requestFocus(true);
		setLayout(null);

		drawThread.start();
	}

	public synchronized void addNPC(Player npc) {
		npcs.add(npc);
	}

	/**
	 * ��(x,y)����һ��NPC
	 * 
	 * @param npc
	 * @param x
	 * @param y
	 */
	public void addNPC(Player npc, int x, int y) {
		npc.setSceneLocation(x, y);
		npcs.add(npc);
	}

	protected void clearNPCs() {
		this.npcs.clear();

	}

	public synchronized void draw(Graphics g, long elapsedTime) {
		if (g == null) {
			return;
		}
		try {
			g.setColor(Color.BLACK);		
			// npcs
			drawNPC(g, elapsedTime);
			// update comps on the canvas
			drawComponents(g, elapsedTime);
			// draw fade
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, getWidth(), getHeight());
			drawMemory(g);
		} catch (Exception e) {
			System.out.printf("����Canvasʱʧ�ܣ�\n");
			e.printStackTrace();
		}
	}

	private synchronized void drawCanvas() {
		Graphics g = this.getGraphics();
		long currTime = System.currentTimeMillis();
		if (lastTime == 0)
			lastTime = currTime;
		long elapsedTime = currTime - lastTime;
		lastTime = currTime;
		if (g != null) {
			this.draw(offscreenGraphics, elapsedTime);
			// draw to real graphics
			g.drawImage(offscreenImage, 0, 0, null);
			g.dispose();
		}
	}

	protected void drawComponents(Graphics g, long elapsedTime) {
		Component[] comps = getComponents();
		for (int i = comps.length - 1; i >= 0; i--) {// reverse the z-order
			Component c = comps[i];
			Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
			c.paint(g2);
			g2.dispose();
		}
		drawTooltips(g);
		// update cursor
		drawCursor(g, elapsedTime);

	}

	protected void drawCursor(Graphics g, long elapsedTime) {
		Point p = GameMain.getMousePosition();
		if (movingObject != null && p != null) {
			movingObject.update(elapsedTime);
			movingObject.draw(g, p.x + movingOffset.x, p.y + movingOffset.y);
		}
		if (gameCursor != null) {
			if (p != null) {
				gameCursor.setLocation(p.x, p.y);
			}
			gameCursor.update(elapsedTime);
			gameCursor.draw(g);
		}
	}

	protected void drawNPC(Graphics g, long elapsedTime) {
		for (Player npc : npcs) {
			npc.update(elapsedTime);
			Point p = npc.getLocation();
			// p = localToView(p);
			p.translate(-viewportX, -viewportY);
			npc.draw(g, p.x, p.y);
		}
	}
	
	protected void drawPlayer(Graphics g, long elapsedTime) {
		Player player = getPlayer();
		if (player != null) {
			// long s1 = System.currentTimeMillis();
			player.update(elapsedTime);
			Point p = player.getLocation();
			p = localToView(p);
			player.draw(g, p.x, p.y);
			// long s2 = System.currentTimeMillis();
			// if(s2-s1>0) {
			// System.out.printf("update player uses: %sms\n",s2-s1);
			// }
		}
	}

	public Point localToView(Point p) {
		return new Point(p.x - viewportX, p.y - viewportY);
	}

	private void drawTooltips(Graphics g) {
		Component[] comps = GameMain.getWindow().getLayeredPane().getComponentsInLayer(JLayeredPane.POPUP_LAYER);
		for (Component comp : comps) {
			Graphics g1 = g.create(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight());
			comp.paint(g1);
			g1.dispose();
		}
	}

	public void fadeIn(long t) {
		this.fading = true;
		long duration = t;
		long interval = t / 10;
		AnimatorThread thread = new AnimatorThread(duration, interval, false);
		thread.start();
	}

	public void fadeOut(long t) {
		this.fading = true;
		long duration = t;
		long interval = t / 10;
		AnimatorThread thread = new AnimatorThread(duration, interval, true);
		thread.start();
	}

	private Component findComponentByName(Component parent, String name) {
		if (!(parent instanceof Container)) {
			return null;
		}
		Container container = (Container) parent;
		Component[] comps = container.getComponents();
		for (Component c : comps) {
			if (name.equals(c.getName())) {
				return c;
			}
		}
		for (Component c : comps) {
			Component result = this.findComponentByName(c, name);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	protected Component findComponentByName(String name) {
		return this.findComponentByName(this, name);
	}

	/**
	 * find npc by name
	 * 
	 * @param name
	 * @return
	 */
	public Player findNpc(String name) {
		for (Player p : this.npcs) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public Cursor getGameCursor() {
		return gameCursor;
	}

	public List<Player> getNpcs() {
		return npcs;
	}

	public Graphics getOffscreenGraphics() {
		return offscreenGraphics;
	}

	public void removeMovingObject() {
		this.movingObject = null;
	}

	public void setGameCursor(String cursor) {
		this.gameCursor = ResourceStore.getInstance().getCursor(cursor);
	}

	public void setMovingObject(Animation anim, Point offset) {
		this.movingObject = anim;
		this.movingOffset = offset;
	}

	public void waitForFading() {
		while (this.fading) {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setViewPosition(int x, int y) {
		this.viewportX = x;
		this.viewportY = y;
		reviseViewport();
	}

	private void reviseViewport() {
		int canvasWidth = getWidth();
		int canvasHeight = getHeight();
		// int mapWidth = map.getWidth();
		// int mapHeight = map.getHeight();
		if (viewportX + canvasWidth > maxWidth) {
			viewportX = maxWidth - canvasWidth;
		} else if (viewportX < 0) {
			viewportX = 0;
		}
		if (viewportY + canvasHeight > maxHeight) {
			viewportY = maxHeight - canvasHeight;
		} else if (viewportY < 0) {
			viewportY = 0;
		}
	}

	protected int getMaxWidth() {
		return maxWidth;
	}

	protected void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	protected int getMaxHeight() {
		return maxHeight;
	}

	protected void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	protected int getViewportY() {
		return viewportY;
	}

	protected int getViewportX() {
		return viewportX;
	}

	public Point getViewPosition() {
		return new Point(viewportX, viewportY);
	}

	public Player getPlayer() {
		return player;
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * 
	 */
	public void dispose() {
		canvasValid = false;
	}
	
	protected void drawMemory(Graphics g) {
		if(g!=null) {
			double mb = 1024*1024; 
			double maxMem = Runtime.getRuntime().maxMemory()/mb;
			double freeMem = Runtime.getRuntime().freeMemory()/mb;
			int x = 100,y = 20;
			g.setColor(Color.GREEN);
			g.drawString(String.format("�ڴ棺%.2f/%.2f MB", freeMem,maxMem), x, y);
		}
	}

}