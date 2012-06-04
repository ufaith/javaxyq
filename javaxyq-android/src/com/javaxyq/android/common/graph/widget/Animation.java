package com.javaxyq.android.common.graph.widget;

import java.util.Vector;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Animation extends AbstractWidget {

	private Object UPDATE_LOCK = new Object();

	private Vector<Frame> frames;

	private int repeat = -1;// 播放次数，-1表示循环

	private int index;// 当前动画序号

	private Frame currFrame;// 当前帧

	private long animTime;// 动画已播放时间(1周期内)

	private long totalDuration;// 总共持续时间

	private int frameCount;

	public Animation() {
		frames = new Vector<Frame>();
	}

	public int getIndex() {
		return index;
	}

	public synchronized void addFrame(Bitmap image, long duration, int centerX,
			int centerY) {
		totalDuration += duration;
		Frame frame = new Frame(image, totalDuration, centerX, centerY);
		frames.add(frame);
		currFrame = frame;
		frameCount = frames.size();
	}

	/**
	 * 从头开始播放这个动画
	 */
	public synchronized void reset() {
		animTime = 0;
		index = 0;
		currFrame = frames.size() > 0 ? frames.get(0) : null;
	}

	public void setRepeat(int repeat) {
		if (this.repeat != repeat) {
			this.repeat = repeat;
			this.reset();
		}
	}

	public Vector<Frame> getFrames() {
		return frames;
	}

	public void setIndex(int index) {
		this.index = index;
		this.currFrame = frames.get(index);
	}
	
	public synchronized int getRefPixelX() {
		return (currFrame == null) ? 0 : currFrame.getRefPixelX();
	}

	public synchronized int getRefPixelY() {
		return (currFrame == null) ? 0 : currFrame.getRefPixelY();
	}
	
	public synchronized Bitmap getImage() {
		return (currFrame == null) ? null : currFrame.getImage();
	}

	@Override
	protected void doDraw(Canvas canvas, int x, int y, int width, int height) {
		this.currFrame.draw(canvas, x, y, width, height);
		
	}
	
	/**
	 * 根据消逝的时间更新目前应该显示哪一帧动画
	 * 
	 * @param elapsedTime
	 * @return 返回此次跳过的帧数
	 */
	public int update(long elapsedTime) {
		if (repeat == 0) {
			return 0;
		}
		animTime += elapsedTime;
		int orgIndex = index;
		this.updateToTime(animTime);
		return (frameCount + index - orgIndex) % frameCount;
	}
	
	/**
	 * 从第0帧开始计算，更新到elapsedTime时间后的帧
	 * 
	 * @param playTime
	 */
	public void updateToTime(long playTime) {
		synchronized (UPDATE_LOCK) {
			if (repeat == 0) {
				return;
			}
			if (frames.size() > 1) {
				animTime = playTime;
				// update the image
				if (animTime >= totalDuration) {
					animTime %= totalDuration;
					repeat -= (repeat > 0) ? 1 : 0;
					if (repeat != 0) {
						index = 0;
					} else {
						index = frameCount - 1;
					}
					currFrame = frames.get(index);
				}
				while (animTime > frames.get(index).getEndTime()) {
					index++;
				}
				currFrame = frames.get(index);
			} else if (frames.size() > 0) {
				currFrame = frames.get(0);
			} else {
				currFrame = null;
			}
			UPDATE_LOCK.notifyAll();
		}
	}

}
