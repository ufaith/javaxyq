package com.javaxyq.android.common.graph.widget;

import java.util.Vector;
import android.graphics.Bitmap;

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
	
	public synchronized void addFrame(Bitmap image, long duration, int centerX, int centerY) {
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
}
