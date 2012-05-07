package com.javaxyq.android.common.graph.widget;

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
	public void draw(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

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
