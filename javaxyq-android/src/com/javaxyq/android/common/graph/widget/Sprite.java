package com.javaxyq.android.common.graph.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Sprite extends AbstractWidget {

	public static final int DIR_DOWN = 0x4;

	public static final int DIR_DOWN_LEFT = 0x1;

	public static final int DIR_DOWN_RIGHT = 0x0;

	public static final int DIR_LEFT = 0x5;

	public static final int DIR_RIGHT = 0x7;

	public static final int DIR_UP = 0x6;

	public static final int DIR_UP_LEFT = 0x2;

	public static final int DIR_UP_RIGHT = 0x3;

	// TODO private
	public Vector<Animation> animations;

	/** 自动(循环)播放 */
	private boolean autoPlay = true;

	// 中心点
	private int refPixelX;

	private int refPixelY;

	/** 精灵着色 */
	private List<Integer> colorations;

	// TODO private
	public Animation currAnimation;

	// 当前是第几个动画（哪个方向）
	private int direction;

	private String resId;

	private int repeat = -1;

	public List<Integer> getColorations() {
		return colorations;
	}

	public void setColorations(List<Integer> colorations) {
		this.colorations = colorations;
	}

	public void setColorations(int[] colorations) {
		for (int i = 0; i < colorations.length; i++) {
			this.colorations.set(i, colorations[i]);
		}
	}

	public int getRefPixelX() {
		return refPixelX;
	}

	public void setRefPixelX(int refPixelX) {
		this.refPixelX = refPixelX;
	}

	public int getRefPixelY() {
		return refPixelY;
	}

	public void setRefPixelY(int refPixelY) {
		this.refPixelY = refPixelY;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public int getDirection() {
		return direction;
	}

	public Sprite(int width, int height, int centerX, int centerY) {
		this.colorations = new ArrayList<Integer>(3);
		this.colorations.add(0);
		this.colorations.add(0);
		this.colorations.add(0);

		this.animations = new Vector<Animation>();
		setWidth(width);
		setHeight(height);
		this.refPixelX = centerX;
		this.refPixelY = centerY;
	}

	public void addAnimation(Animation anim) {
		anim.reset();
		animations.add(anim);
	}

	public void clearAnimations() {
		this.animations.clear();
	}

	public synchronized void setDirection(int index) {
		index %= animations.size();
		this.direction = index;
		currAnimation = animations.get(this.direction);
		this.currAnimation.setRepeat(this.repeat);
	}

	public synchronized void resetFrames() {
		this.currAnimation.setIndex(0);
	}
}
