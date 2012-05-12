package com.javaxyq.android.common.graph.widget;

import android.graphics.Bitmap;

public class Frame extends AbstractWidget {

	private Bitmap image;

	/** 帧的结束时间 */
	private long endTime;

	private int refPixelX;

	private int refPixelY;

	public Frame(Bitmap image, long endTime, int centerX, int centerY) {
		this.image = image;
		this.endTime = endTime;
		this.refPixelX = centerX;
		this.refPixelY = centerY;
		setWidth(image.getWidth());
		setHeight(image.getHeight());
	}

	public Bitmap getImage() {
		return image;
	}

}
