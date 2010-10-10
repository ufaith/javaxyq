/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.core.Toolkit;
import com.javaxyq.model.Option;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class TalkPanel extends Panel {

	private static final int HEAD_OFFSET = 12;

	private Player talker;
	
	private Option[] options;

	private MouseListener optionHandler = new OptionHandler(this);

	private Option result;
	
	private static class OptionHandler implements MouseListener{
		private TalkPanel panel;
		public OptionHandler(TalkPanel panel) {
			this.panel = panel;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			OptionLabel label = (OptionLabel) e.getSource();
			label.getOption().setSelected(true);
			this.panel.setResult(label.getOption());
			//this.panel.close();
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
		@Override
		public void mousePressed(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
	}

	public TalkPanel(int width, int height) {
		this(width, height, true, true);
	}

	public TalkPanel(int width, int height, boolean closable, boolean movable) {
		super(width, height, closable, movable);
		setClickClosabled(true);
	}

    public void initTalk(String text,Option[] options) {
    	this.options = options;
    	this.result = null;
        this.removeAll();
        RichLabel lblText = Toolkit.getInstance().createRichLabel(16, 30, 450, 130, text);
        this.add(lblText);
        int y0 = lblText.getHeight()+lblText.getY()+10;
    	for (int i = 0; options!=null && i < options.length; i++) {
    		Option option = options[i];
            OptionLabel label = Toolkit.getInstance().createOptionLabel(20, y0 + i * 20, 450, 18, option);
            label.addMouseListener(optionHandler);
            this.add(label);
        }
    }
    
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			if (this.getTalker() != null) {
				Sprite s = ResourceStore.getInstance().findPhoto(this.getTalker().getCharacter());
				Graphics cg = GameMain.getSceneCanvas().getOffscreenGraphics();
				s.draw(cg, getX() + HEAD_OFFSET, getY() - s.getHeight());
			}
		} catch (Exception e) {
			System.err.println("绘制对话人物头像失败!");
			e.printStackTrace();
		}
	}

	public Player getTalker() {
		return talker;
	}

	public void setTalker(Player talker) {
		this.talker = talker;
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		//super.paintImmediately(x, y, w, h);
	}

	/**
	 * @return
	 */
	public Option getResult() {
		return result;
	}
	/**
	 * @param option
	 */
	protected void setResult(Option result) {
		this.result = result;
		processDefaultActions();
	}

	/**
	 * @param panel 
	 * @param result
	 */
	private void processDefaultActions() {
		System.out.println("processDefaultActions: "+result);
		String action = result.getAction();
        if (action != null) {
            if ("close".equals(action)) {
            	this.close();
            } else if ("open".equals(action)) {
            	UIHelper.showDialog((String)result.getValue());
            	this.close();
            } else if ("transport".equals(action)) {
            	String[] args =  ((String)result.getValue()).split(" ");
                String sceneId = args[0];
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                GameMain.fadeToMap(sceneId, x, y);
                this.close();
            } else if("prev".equals(action)){//TODO 上一段内容
            } else if("next".equals(action)){//TODO 下一段内容
            } else {
            //    GameMain.doAction(GameMain.getTalker(), "com.javaxyq.action." + action, arguments.split(" "));
                this.close();
            }
        }
	}	
}
