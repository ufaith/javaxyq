package com.javaxyq.android.common.graph.tcp;

public class WASFrame {
	
	public WASFrame(int x, int y, int width, int height, int delay,int frameOffset,int []lineOffset) {
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
	
	/** ����֡ƫ�� */
	private int frameOffset;
	
	/** ������ƫ�� */
	private int[] lineOffsets;
	
	/** ��ʱ֡�� */
	private int delay = 1;
	
	/** �߶� */
	private int height;
	
	/** ��� */
	private int width;
	
	/** ͼ��ƫ��x */
	private int x;
	
	/** ͼ��ƫ��y */
	private int y;
	
	/**
	 * ͼ��ԭʼ����<br>
	 * 0-15λRGB��ɫ��565��<br>
	 * 16-20Ϊalphaֵ<br>
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
