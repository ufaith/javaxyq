package com.javaxyq.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.javaxyq.event.EventDelegator;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Animation;

public class Label extends JLabel {

	private static final String uiClassID = "GameLabelUI";
	private static final long serialVersionUID = 7814113439988128271L;

	static {
		UIManager.put("GameLabelUI", GameLabelUI.class.getName());
	}
	private Animation anim;

	private long lastUpdateTime;

	private String textTpl;

	private String tooltipTpl;

	private TooltipTemplate template;

	private JToolTip tooltip;

	private String oldToolTipText;

	public Label(Animation anim) {
		this(null, new ImageIcon(anim.getImage()), LEFT);
		setAnim(anim);
	}

	public Label(String text) {
		this(text, null, LEFT);
	}

	public Label(Icon image) {
		this(null, image, LEFT);
	}

	public Label(String text, int horizontalAlignment) {
		this(text, null, horizontalAlignment);
	}

	public Label(Icon image, int horizontalAlignment) {
		this(null, image, horizontalAlignment);
	}

	public Label(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		this.textTpl = text;
		setIgnoreRepaint(true);
		setBorder(null);
		// setVerticalAlignment(CENTER);
		// setVerticalTextPosition(CENTER);
		// setForeground(Color.WHITE);
		setFont(UIUtils.TEXT_FONT);
		//enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		//enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	@Override
	public void paint(Graphics g) {
		if (anim != null) {
			anim.update(System.currentTimeMillis() - lastUpdateTime);
			lastUpdateTime = System.currentTimeMillis();
			setIcon(new ImageIcon(anim.getImage()));
			// System.out.println(" paint label "+lastUpdateTime);
		}
		super.paint(g);
	}

	public TooltipTemplate getTemplate() {
		return template;
	}

	public void setTemplate(TooltipTemplate template) {
		this.template = template;
	}

	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		this.anim = anim;
		if (anim != null) {
			this.lastUpdateTime = System.currentTimeMillis();
			this.setSize(anim.getWidth(), anim.getHeight());
		}
	}

	public String getTextTpl() {
		return textTpl;
	}

	public void setTextTpl(String textTpl) {
		this.textTpl = textTpl;
	}

	public JToolTip createToolTip() {
		return getTooltip();
	}

	public JToolTip getTooltip() {
		if (this.tooltip == null) {
			JToolTip tip = super.createToolTip();
			// tip.setForeground(Color.RED);
			// 设置ToolTip为透明
			tip.setOpaque(false);
			this.tooltip = tip;
		}
		return tooltip;
	}

	@Override
	public String getToolTipText() {
		if(tooltipTpl !=null && template!=null) {
			return template.getTooltipText(tooltipTpl);
		}
		return super.getToolTipText();
	}

	public String getTooltipTpl() {
		return tooltipTpl;
	}

	public void setTooltipTpl(String tooltipTpl) {
		this.tooltipTpl = tooltipTpl;
	}

	public Point getToolTipLocation(MouseEvent evt) {
		try {
			Point p = evt.getPoint();
			p.move(30, 25);
			Component win = getWindow();
			if(win!=null) {
				SwingUtilities.convertPointToScreen(p, this);
				Point winLoc = win.getLocationOnScreen();
				// Point mouseLoc = evt.getLocationOnScreen();
				Dimension tipSize = this.getTooltip().getPreferredSize();
				if (p.x + tipSize.width > winLoc.x + win.getWidth() - 10) {
					p.x = winLoc.x + win.getWidth() - tipSize.width - 10;
				}
				if (p.y + tipSize.height > winLoc.y + win.getHeight() - 10) {
					p.y = winLoc.y + win.getHeight() - tipSize.height - 10;
				}
				SwingUtilities.convertPointFromScreen(p, this);
			}
			return p;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return super.getToolTipLocation(evt);
	}

	/**
	 * @return
	 */
	private Component getWindow() {
		Container parent = this.getParent();
		while(parent!=null && !(parent instanceof RootPaneContainer)) {
			parent = parent.getParent();
		}
		return parent;
	}

	public void setToolTipText(String text) {
		putClientProperty(TOOL_TIP_TEXT_KEY, text);
		LightweightToolTipManager toolTipManager = LightweightToolTipManager.sharedInstance();
		if (text != null) {
			if (oldToolTipText == null) {
				toolTipManager.registerComponent(this);
			}
		} else {
			toolTipManager.unregisterComponent(this);
		}
		this.oldToolTipText = text;
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		// super.paintImmediately(x, y, w, h);
	}

	public boolean isValid(int x, int y) {
		if (this.anim != null) {
			return this.anim.contains(x, y);
		}
		return true;
	}

	/**
	 * 转发事件到代理器
	 */
	protected void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
		//FIXME 代理事件与监听器冲突问题
		//EventDelegator.getInstance().delegateEvent(e);
	}

	/**
	 * 转发事件到代理器
	 */
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
		//EventDelegator.getInstance().delegateEvent(e);
	}
}
