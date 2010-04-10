package com.javaxyq.graph;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import com.javaxyq.config.ImageConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.EventDispatcher;
import com.javaxyq.event.EventException;
import com.javaxyq.event.EventTarget;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.widget.SpriteImage;
import com.javaxyq.ui.*;

//FIXME SpriteImage�Ĵ��������⣬Image�����Sprite���޷�����
/**
 * ������
 * 
 * @author Langlauf
 * @date
 */
public class Panel extends JPanel implements EventTarget {
	private static final long serialVersionUID = 3207034027692111969L;

	private EventListenerList listenerList = new EventListenerList();

	private ArrayList<SpriteImage> sprites = new ArrayList<SpriteImage>();

	/** �Ƿ�����һ��ر� */
	private boolean closable;

	/** �Ƿ��ʼ�� */
	private boolean initialized;

	/** ����������Ƿ������ر� */
	private boolean clickClosabled;

	/** actionId�󶨱� */
	private Map<String, String> actionIdBindings = new HashMap<String, String>();

	private MouseAdapter mouseHandler = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				Panel dlg = Panel.this;
				Container parent = dlg.getParent();
				// ����������Ч����
				Point p = e.getPoint();
				if (isValid(p)) {
					if (clickClosabled) {
						UIHelper.hideDialog(Panel.this);
					} else {
						lastPosition = p;
						parent.setComponentZOrder(dlg, 0);// �Ƶ����ϲ�
					}
				} else {
					// ���������Ǵ�͸������,���¼����ݸ�������
					int x = dlg.getX();
					int y = dlg.getY();
					MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), e
							.getModifiers(), x + p.x, y + p.y, 1, false);
					parent.dispatchEvent(event);
				}
				e.consume();
			} else if (e.getButton() == MouseEvent.BUTTON3) {// �һ��ر�
				if (closable) {
					UIHelper.hideDialog(Panel.this);
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			lastPosition = null;

		}

		public void mouseDragged(MouseEvent e) {// �ƶ����
			if (lastPosition != null && movable) {
				Point location = Panel.this.getLocation();
				location.translate(e.getX() - lastPosition.x, e.getY() - lastPosition.y);
				Panel.this.setLocation(location);
			}
		}

		public void mouseMoved(MouseEvent e) {
			Point p = e.getPoint();
			Panel dlg = Panel.this;
			Container parent = dlg.getParent();
			int x = dlg.getX();
			int y = dlg.getY();
			// ���������Ǵ�͸������,���¼����ݸ�������
			if (!isValid(p)) {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), e
						.getModifiers(), x + p.x, y + p.y, 1, false);
				parent.dispatchEvent(event);
			} else {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), e
						.getModifiers(), x + p.x, y + p.y, 1, false);

				parent.dispatchEvent(event);
			}
		}

		public void mouseEntered(MouseEvent e) {
			Point p = e.getPoint();
			Panel dlg = Panel.this;
			Container parent = dlg.getParent();
			int x = dlg.getX();
			int y = dlg.getY();
			// ���������Ǵ�͸������,���¼����ݸ�������
			if (!isValid(p)) {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), e
						.getModifiers(), x + p.x, y + p.y, 1, false);
				parent.dispatchEvent(event);
			} else {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), e
						.getModifiers(), x + p.x, y + p.y, 1, false);

				parent.dispatchEvent(event);
			}
		}

	};

	/** �Ƿ�����϶� */
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
	 * �رմ����/�Ի���
	 */
	public void close() {
		Container p = Panel.this.getParent();
		p.remove(Panel.this);
	}

	/**
	 * �жϸõ��Ƿ�Ϊ��Ч������(�Ǵ�͸����)
	 * 
	 * @param p
	 * @return
	 */
	public boolean isValid(Point p) {
		if (!isVisible()) {
			return false;
		}
		if (bgImage != null && bgImage.contains(p.x - bgImage.getX(), p.y - bgImage.getY())) {
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
	 *            ����ͼƬ
	 * @param closable
	 *            �Ƿ���Թر�
	 * @param movable
	 *            �Ƿ�����ƶ�
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
		addMouseMotionListener(mouseHandler);
	}

	public boolean handleEvent(EventObject evt) throws EventException {
		if (evt instanceof ActionEvent) {
			handleActionEvent((ActionEvent) evt);
			return true;
		} else if (evt instanceof java.awt.event.ActionEvent) {
			handleActionEvent(new ActionEvent((java.awt.event.ActionEvent) evt));
			return true;
		} else if (evt instanceof MouseEvent) {
			handleMouseEvent((MouseEvent) evt);
			return true;
		} else if (evt instanceof PanelEvent) {
			handlePanelEvent((PanelEvent) evt);
			return true;
		}
		return false;
	}

	public void fireEvent(PanelEvent e) {
		EventDispatcher.getInstance(Panel.class, PanelEvent.class).dispatchEvent(e);
	}

	/**
	 * ��������¼�
	 * 
	 * @param event
	 */
	private void handleMouseEvent(MouseEvent evt) {
		String actionId = getBindingActionId(evt);
		if (actionId != null) {
			ActionEvent actionEvent = new ActionEvent(evt.getSource(), actionId, new Object[] { evt });
			handleActionEvent(actionEvent);
		}
	}

	/**
	 * @param evt
	 * @return
	 */
	private String getBindingActionId(ComponentEvent evt) {
		// actionKey -> name-eventType, like 'label��Ѫ-mousepressed'
		String paramString = evt.paramString();
		// �õ��¼������� 'MOUSE_PRESSED'
		String eventType = paramString.substring(0, paramString.indexOf(','));
		// ɾ���ָ���»��� 'MOUSE_PRESSED' -> 'mousepressed'
		eventType = eventType.toLowerCase().replaceAll("_", "");
		String actionKey = evt.getComponent().getName() + "-" + eventType;
		// ��ð󶨵�actionId
		String actionId = actionIdBindings.get(actionKey);
		return actionId;
	}

	private void handleActionEvent(ActionEvent evt) {
		PanelListener[] listeners = listenerList.getListeners(PanelListener.class);
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].actionPerformed(evt);
		}
	}

	private void handlePanelEvent(PanelEvent evt) {
		PanelListener[] listeners = listenerList.getListeners(PanelListener.class);
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].actionPerformed(evt);
		}
	}

	public void addPanelListener(PanelListener l) {
		listenerList.add(PanelListener.class, l);
	}

	public void removePanelListener(PanelListener l) {
		listenerList.remove(PanelListener.class, l);
	}

	/**
	 * �Ƴ���������¼�������
	 */
	public void removeAllPanelListeners() {
		PanelListener[] listeners = listenerList.getListeners(PanelListener.class);
		for (int i = 0; i < listeners.length; i++) {
			listenerList.remove(PanelListener.class, listeners[i]);
		}
	}

	/**
	 * �󶨿ؼ����¼�
	 * 
	 * @param comp
	 * @param eventType
	 * @param actionId
	 */
	public void bindAction(Component comp, String eventType, String actionId) {
		// like 'name-mouseclicked'
		String key = comp.getName() + "-" + eventType.toLowerCase();
		actionIdBindings.put(key, actionId);
	}

	/**
	 * ��������ָ�����ֵ��ӿؼ�
	 * 
	 * @param name
	 * @return
	 */
	public Component findCompByName(String name) {
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
		// Component[] comps = getComponents();
		// for (int i = comps.length - 1; i >= 0; i--) {
		// Component c = comps[i];
		// Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(),
		// c.getHeight());
		// c.paint(g2);
		// g2.dispose();
		// }
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
	}

	public boolean isClickClosabled() {
		return clickClosabled;
	}

	public void setClickClosabled(boolean clickClosabled) {
		this.clickClosabled = clickClosabled;
	}

}