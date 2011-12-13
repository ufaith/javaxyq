package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;

import com.javaxyq.event.EventDelegator;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Frame;
import com.javaxyq.widget.Sprite;

/**
 * �Զ����Button��<br>
 * ʹ����Ϸ��Դ����ʾ
 * 
 * @author gdw
 * @date
 */
public class Button extends JButton {

	private static final String uiClassID = "GameButtonUI";
	static {
		UIManager.put("GameButtonUI", "com.javaxyq.ui.GameButtonUI");
	}

	/**
	 * ��ť����ʱ�Ƿ��Զ�������ƫ��
	 */
	private boolean autoOffset = false;
	private String tooltipTpl;
	private TooltipTemplate template;
	private String oldToolTipText;

	public Button() {

	}

	public Button(Action action) {
		super(action);
	}

	/**
	 * ����һ������4֡ͼƬ��������Ϊ��ť��4��״̬,0-3����Ϊnormal,pressed,rollover,disabled
	 * 
	 * @param frames
	 */
	public Button(int width, int height, List<Frame> frames) {
		setSize(width, height);
		init(frames);
	}

	public Button(Sprite sprite) {
		init(sprite);
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	private void init(List<Frame> frames) {
		// changed ui
		setUI(new CustomButtonUI());
		setFont(UIUtils.TEXT_FONT);
		setForeground(Color.WHITE);
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.CENTER);
		setHorizontalAlignment(JButton.CENTER);
		setVerticalAlignment(JButton.CENTER);
		// changed viewer properties
		setIgnoreRepaint(true);
		setBorder(null);
		// setOpaque(false);
		setFocusable(false);
		setContentAreaFilled(false);
		// set icons
		try {
			int frameCount = frames.size();
			this.setAutoOffset(frameCount < 3);
			if (frameCount > 0) {
				setIcon(new ImageIcon(frames.get(0).getImage()));
			}
			if (frameCount > 1) {
				setPressedIcon(new ImageIcon(frames.get(1).getImage()));
			}
			if (frameCount > 2) {
				setRolloverIcon(new ImageIcon(frames.get(2).getImage()));
			}
			if (frameCount > 3) {
				setDisabledIcon(new ImageIcon(frames.get(3).getImage()));
			}
		} catch (Exception e) {
			System.err.println("����Buttonʧ��!");
			e.printStackTrace();
		}
	}

	public void init(Sprite sprite) {
		setSize(sprite.getWidth(), sprite.getHeight());
		List<Frame> frames = sprite.getAnimation(0).getFrames();
		init(frames);
	}

	public boolean isAutoOffset() {
		return autoOffset;
	}

	// ��ײ���
	// public boolean contains(int x, int y) {
	// return super.contains(x, y);
	// }

	/**
	 * ��ť����ʱ�Ƿ��Զ�������ƫ��
	 */
	public void setAutoOffset(boolean autoOffset) {
		this.autoOffset = autoOffset;
	}

	static class CustomButtonUI extends BasicButtonUI {
		@Override
		protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
			Button btn = (Button) c;
			ButtonModel model = btn.getModel();
			if (btn.isAutoOffset() && model.isArmed() && model.isPressed()) {
				defaultTextShiftOffset = 1; // down
			} else {
				defaultTextShiftOffset = 0;
			}
			setTextShiftOffset();
			super.paintIcon(g, c, iconRect);
		}

		protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
			AbstractButton b = (AbstractButton) c;
			ButtonModel model = b.getModel();
			if (model.isArmed() && model.isPressed()) {
				defaultTextShiftOffset = 1; // down
			} else {
				defaultTextShiftOffset = 0;
			}
			setTextShiftOffset();
			super.paintText(g, c, textRect, text);
		}

	}

	public JToolTip createToolTip() {
		JToolTip tip = super.createToolTip();
		// tip.setForeground(Color.RED);
		// ����ToolTipΪ͸��
		tip.setOpaque(false);
		return tip;
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

	@Override
	protected void fireActionPerformed(ActionEvent event) {
		super.fireActionPerformed(event);
		EventDelegator.getInstance().delegateEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand()));
	}

}