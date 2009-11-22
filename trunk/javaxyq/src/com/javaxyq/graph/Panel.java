package com.javaxyq.graph;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JPanel;

import com.javaxyq.config.ImageConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.widget.SpriteImage;

//FIXME SpriteImage的处理有问题，Image后跟着Sprite，无法出现
/**
 * 面板组件
 * 
 * @author Langlauf
 * @date
 */
public class Panel extends JPanel {
	private static final long serialVersionUID = 3207034027692111969L;

	private ArrayList<SpriteImage> sprites = new ArrayList<SpriteImage>();

	/** 是否可以右击关闭 */
	private boolean closable;

	private boolean initialized;

	private MouseListener mouseHandler = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				Panel dlg = Panel.this;
				Container parent = dlg.getParent();
				// 如果点击在有效区域
				Point p = e.getPoint();
				if (isValid(p)) {
					lastPosition = p;
					parent.setComponentZOrder(dlg, 0);// 移到最上层
				} else {
					// 如果点击的是穿透的区域,把事件传递给父容器
					int x = dlg.getX();
					int y = dlg.getY();
					MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), e
							.getModifiers(), x + p.x, y + p.y, 1, false);
					parent.dispatchEvent(event);
				}
				e.consume();
			} else if (e.getButton() == MouseEvent.BUTTON3) {// 右击关闭
				if (closable) {
					// close();
					GameMain.hideDialog(Panel.this);
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			lastPosition = null;
		}
	};

	private MouseMotionListener mouseMotionHandler = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {// 移动面板
			if (lastPosition != null && movable) {
				Point location = Panel.this.getLocation();
				location.translate(e.getX() - lastPosition.x, e.getY() - lastPosition.y);
				Panel.this.setLocation(location);
			}
		}
	};

	/** 是否可以拖动 */
	private boolean movable;

	private Point lastPosition;

	private List<ImageConfig> imageConfigs = new ArrayList<ImageConfig>();

	private String initialAction;

	private String disposeAction;

	private SpriteImage bgImage;

	public Panel(int width, int height) {
		this(width, height, true, true);
	}

	/**
	 * 关闭此面板/对话框
	 */
	public void close() {
		Container p = Panel.this.getParent();
		p.remove(Panel.this);
	}

	/**
	 * 判断该点是否为有效区域内(非穿透区域)
	 * 
	 * @param p
	 * @return
	 */
	public boolean isValid(Point p) {
		if (!isVisible()) {
			return false;
		}
		if(bgImage!=null && bgImage.contains(p.x - bgImage.getX(), p.y-bgImage.getY())) {
			return true;
		}
		for (SpriteImage image : sprites) {
			if (image.contains(p.x - image.getX(), p.y - image.getY())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param bgImage
	 *            背景图片
	 * @param closable
	 *            是否可以关闭
	 * @param movable
	 *            是否可以移动
	 */
	public Panel(int width, int height, boolean closable, boolean movable) {
		this.closable = closable;
		this.movable = movable;
		// changed viewer properties
		setIgnoreRepaint(true);
		setBorder(null);
		setLayout(null);
		// setOpaque(false);
		setFocusable(false);
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseMotionHandler);
	}

	/**
	 * 遍历查找指定名字的子控件
	 * 
	 * @param name
	 * @return
	 */
	public Component getComponentByName(String name) {
		if (name != null) {
			Component[] comps = getComponents();
			for (Component comp : comps) {
				if (name.equals(comp.getName()))
					return comp;
			}
		}
		return null;
	}

	public void paint(Graphics g) {
		init();
		if (bgImage != null) {
			bgImage.draw(g, bgImage.getX(), bgImage.getY());
		}
		for (SpriteImage image : sprites) {
			image.draw(g, image.getX(), image.getY());
		}
		// paintComponent(g);
		paintBorder(g);
		paintChildren(g);
//		Component[] comps = getComponents();
//		for (int i = comps.length - 1; i >= 0; i--) {
//			Component c = comps[i];
//			Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
//			c.paint(g2);
//			g2.dispose();
//		}
		// System.out.println("paint panel ...");
	}

	private void init() {
		if (!this.initialized) {
			for (ImageConfig cfg : imageConfigs) {
				SpriteImage image = ResourceStore.getInstance().createImage(cfg);
				this.sprites.add(image);
			}
			this.initialized = true;
		}
	}

	public void setClosable(boolean b) {
		closable = b;
	}

	public void setMovable(boolean b) {
		movable = b;
	}

	public void addImage(ImageConfig imageConfig) {
		this.imageConfigs.add(imageConfig);
	}

	public boolean removeImage(String id) {
		for (int i = 0; i < this.imageConfigs.size(); i++) {
			ImageConfig cfg = this.imageConfigs.get(i);
			if (id.equals(cfg.getId())) {
				this.imageConfigs.remove(i);
				this.sprites.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean setImage(String id, ImageConfig newCfg) {
		for (int i = 0; i < this.imageConfigs.size(); i++) {
			ImageConfig cfg = this.imageConfigs.get(i);
			if (id.equals(cfg.getId())) {
				this.imageConfigs.set(i, newCfg);
				SpriteImage s = ResourceStore.getInstance().createImage(newCfg);
				this.sprites.set(i, s);
				return true;
			}
		}
		return false;
	}

	public void setInitialAction(String initialAction) {
		this.initialAction = initialAction;
	}

	public String getInitialAction() {
		return initialAction;
	}

	public void initActionMap(ActionMap map) {
		for (Object key : map.allKeys()) {
			getActionMap().put(key, map.get(key));
		}
	}

	public String getDisposeAction() {
		return this.disposeAction;
	}

	public void setDisposeAction(String disposeAction) {
		this.disposeAction = disposeAction;
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		// super.paintImmediately(x, y, w, h);
	}

	public SpriteImage getBgImage() {
		return bgImage;
	}

	public void setBgImage(SpriteImage bgImage) {
		this.bgImage = bgImage;
	}

	public void setBgImage(ImageConfig cfg) {
		this.bgImage = ResourceStore.getInstance().createImage(cfg);
		;
	}
}
