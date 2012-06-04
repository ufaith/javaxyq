package com.javaxyq.android.common.graph.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

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
	
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
    protected void doDraw(Canvas canvas, int x, int y, int width, int height) {
        int x1 = x - this.refPixelX;
        int y1 = y - this.refPixelY;
        canvas.drawBitmap(this.image,  new Rect(0, 0, width,height),new Rect(x1, y1, x1 + width, y1 + height), new Paint());
    }

}
