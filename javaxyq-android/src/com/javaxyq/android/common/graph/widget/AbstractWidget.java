package com.javaxyq.android.common.graph.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Widget 的抽象类
 * 
 * @author 陈洋
 */
public abstract class AbstractWidget implements Widget {
	
	private float alpha = 1.0f;
	
	private int width;
	
	private int height;

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void draw(Canvas canvas,int x, int y) {
		
		this.draw(canvas, x, y, this.width, this.height);

	}

	@Override
	public void draw(Canvas canvas,int x, int y, int width, int height) {
		doDraw(canvas, x, y, width, height);

	}

	@Override
	public void draw(Canvas canvas,Rect rect) {
		this.draw(canvas, rect.left, rect.top, rect.width(), rect.height());

	}
	
	protected abstract void doDraw(Canvas canvas, int x, int y, int width, int height);

	@Override
	public void fadeIn(long t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fadeOut(long t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

}
