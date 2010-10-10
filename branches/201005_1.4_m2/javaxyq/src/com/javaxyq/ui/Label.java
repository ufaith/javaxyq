package com.javaxyq.ui;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.codehaus.groovy.control.CompilationFailedException;

import com.javaxyq.core.GameMain;
import com.javaxyq.data.DataStore;
import com.javaxyq.event.EventDelegator;
import com.javaxyq.widget.Animation;

public class Label extends JLabel {

	private static final String uiClassID = "GameLabelUI";
	private static final long serialVersionUID = 7814113439988128271L;

	static {
		UIManager.put("GameLabelUI", "com.javaxyq.ui.GameLabelUI");
	}
	private Animation anim;

	private long lastUpdateTime;

	private String textTpl;

	private String tooltipTpl;

	private Template template;

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
		setFont(GameMain.TEXT_FONT);
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
		try {
			if (template != null) {
				return template.make(DataStore.getProperties(GameMain.getPlayer())).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getToolTipText();
	}

	public String getTooltipTpl() {
		return tooltipTpl;
	}

	public void setTooltipTpl(String tooltipTpl) {
		this.tooltipTpl = tooltipTpl;
		if (tooltipTpl == null) {
			this.template = null;
			return;
		}
		SimpleTemplateEngine engine = new groovy.text.SimpleTemplateEngine();
		try {
			this.template = engine.createTemplate(tooltipTpl);
			setToolTipText(" ");
		} catch (CompilationFailedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Point getToolTipLocation(MouseEvent evt) {
		try {
			Component win = (Component) GameMain.getWindow();
			Point winLoc = win.getLocationOnScreen();
			// Point mouseLoc = evt.getLocationOnScreen();
			Dimension tipSize = this.getTooltip().getPreferredSize();
			Point p = evt.getPoint();
			p.move(30, 25);
			SwingUtilities.convertPointToScreen(p, this);
			if (p.x + tipSize.width > winLoc.x + win.getWidth() - 10) {
				p.x = winLoc.x + win.getWidth() - tipSize.width - 10;
			}
			if (p.y + tipSize.height > winLoc.y + win.getHeight() - 10) {
				p.y = winLoc.y + win.getHeight() - tipSize.height - 10;
			}
			SwingUtilities.convertPointFromScreen(p, this);
			return p;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return super.getToolTipLocation(evt);
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
	 * 代理鼠标事件
	 * FIXME 改进
	 */
	public void delegateMouseEvent() {
		this.addMouseListener(new MouseAdapter() {
		});
	}

	/**
	 * 转发事件到代理器
	 */
	protected void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
		EventDelegator.getInstance().delegateEvent(e);
	}

	/**
	 * 转发事件到代理器
	 */
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
		EventDelegator.getInstance().delegateEvent(e);
	}
}
