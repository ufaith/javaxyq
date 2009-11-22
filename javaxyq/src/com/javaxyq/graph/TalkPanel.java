/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.graph;

import java.awt.Graphics;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class TalkPanel extends Panel {

	private static final int HEAD_OFFSET = 12;

	private Player talker;

	public TalkPanel(int width, int height) {
		this(width, height, true, true);
	}

	public TalkPanel(int width, int height, boolean closable, boolean movable) {
		super(width, height, closable, movable);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			if (this.getTalker() != null) {
				Sprite s = ResourceStore.getInstance().findHead(this.getTalker().getCharacter());
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
}
