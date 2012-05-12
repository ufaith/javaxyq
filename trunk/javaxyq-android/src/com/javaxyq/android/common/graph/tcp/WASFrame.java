package com.javaxyq.android.common.graph.tcp;

public class WASFrame {

	public WASFrame(int x, int y, int width, int height, int delay,
			int frameOffset, int[] lineOffset) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.delay = delay;
		this.frameOffset = frameOffset;
		this.lineOffsets = lineOffset;
	}

	public WASFrame() {
		// TODO Auto-generated constructor stub
	}

	/** 数据帧偏移 */
	private int frameOffset;

	/** 行数据偏移 */
	private int[] lineOffsets;

	/** 延时帧数 */
	private int delay = 1;

	/** 高度 */
	private int height;

	/** 宽度 */
	private int width;

	/** 图像偏移x */
	private int x;

	/** 图像偏移y */
	private int y;

	/**
	 * 图像原始数据<br>
	 * 0-15位RGB颜色（565）<br>
	 * 16-20为alpha值<br>
	 * pixels[x+y*width]
	 */
	private int[] pixels;

	public int getFrameOffset() {
		return frameOffset;
	}

	public void setFrameOffset(int frameOffset) {
		this.frameOffset = frameOffset;
	}

	public int[] getLineOffsets() {
		return lineOffsets;
	}

	public void setLineOffsets(int[] lineOffsets) {
		this.lineOffsets = lineOffsets;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}
}
