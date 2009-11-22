package com.javaxyq.graph;

import java.awt.Color;
import java.util.List;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import com.javaxyq.core.GameMain;
import com.javaxyq.widget.Frame;
import com.javaxyq.widget.Sprite;

/**
 * �Զ����Button��<br>
 * ʹ����Ϸ��Դ����ʾ
 * 
 * @author gdw
 * @date
 */
public class ToggleButton extends JToggleButton {

    /**
     * ��ť����ʱ�Ƿ��Զ�������ƫ��
     */
    private boolean autoOffset = false;

    public ToggleButton() {

    }

    public ToggleButton(Action action) {
        super(action);
    }

    /**
     * ����һ������4֡ͼƬ��������Ϊ��ť��4��״̬,0-3����Ϊnormal,pressed,rollover,disabled
     * 
     * @param frames
     */
    public ToggleButton(int width, int height, List<Frame> frames) {
        setSize(width, height);
        init(frames);
    }

    public ToggleButton(Sprite sprite) {
        init(sprite);
    }

    public void init(List<Frame> frames) {
        // changed ui
        //setUI(new CustomButtonUI());
        setFont(GameMain.TEXT_FONT);
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
            if (frameCount > 0) {
                setIcon(new ImageIcon(frames.get(0).getImage()));
            }
            if (frameCount > 1) {
                ImageIcon selectedIcon = new ImageIcon(frames.get(1).getImage());
                setPressedIcon(selectedIcon);
                setSelectedIcon(selectedIcon);
            }
            if (frameCount > 2) {
                setRolloverIcon(new ImageIcon(frames.get(2).getImage()));
            }
            if (frameCount > 3) {
                setDisabledIcon(new ImageIcon(frames.get(3).getImage()));
            }
        } catch (Exception e) {
            System.err.println("occur error while create button !");
            e.printStackTrace();
            if (frames.size() < 3)
                autoOffset = true;
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
	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		//super.paintImmediately(x, y, w, h);
	}

//    class CustomButtonUI extends BasicButtonUI {
//        @Override
//        protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
//            ToggleButton btn = (ToggleButton) c;
//            ButtonModel model = btn.getModel();
//            if (btn.isAutoOffset() && model.isArmed() && model.isPressed()) {
//                defaultTextShiftOffset = 1; // down
//            } else {
//                defaultTextShiftOffset = 0;
//            }
//            setTextShiftOffset();
//            super.paintIcon(g, c, iconRect);
//        }
//        
//        protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
//            AbstractButton b = (AbstractButton) c;
//            ButtonModel model = b.getModel();
//            if (model.isArmed() && model.isPressed()) {
//                defaultTextShiftOffset = 1; // down
//            } else {
//                defaultTextShiftOffset = 0;
//            }
//            setTextShiftOffset();
//            super.paintText(g, c, textRect, text);
//        }
//        
//    }
}

