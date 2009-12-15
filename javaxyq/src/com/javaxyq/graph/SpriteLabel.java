/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-8
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.graph;

import javax.swing.JLabel;

import com.javaxyq.widget.Sprite;

/**
 * @author dewitt
 * @date 2009-12-8 create
 */
public class SpriteLabel extends JLabel {

	public SpriteLabel(Sprite sprite) {
		this(sprite, 0, 0, sprite.getWidth(), sprite.getHeight());
	}

	public SpriteLabel(Sprite sprite, int x, int y) {
		this(sprite, x, y, sprite.getWidth(), sprite.getHeight());
	}

	public SpriteLabel(Sprite sprite, int x, int y, int width, int height) {
		//todo
	}
}
