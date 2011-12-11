/**
 * 
 */
package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.javaxyq.core.Canvas;

/**
 * @author gongdewei
 * @date 2011-7-24 create
 */
public class TestCanvas extends Canvas {

	private static final long serialVersionUID = -7709678466279021561L;

	private Character character;
	
	public TestCanvas(int width, int height) {
		super(width, height);
	}
	
	@Override
	public synchronized void draw(Graphics g, long elapsedTime) {
		//super.draw(g, elapsedTime);
		g.clearRect(0, 0, getWidth(), getHeight());
		if(character !=null) {
			character.update(elapsedTime);
			character.draw(g);
		}
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
}
