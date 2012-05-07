package com.javaxyq.android.common.graph.widget;

import java.util.Vector;
import android.graphics.Bitmap;

public class Animation extends AbstractWidget {
	
	private Object UPDATE_LOCK = new Object();

	private Vector<Frame> frames;

	private int repeat = -1;// ���Ŵ�����-1��ʾѭ��

	private int index;// ��ǰ�������

	private Frame currFrame;// ��ǰ֡

	private long animTime;// �����Ѳ���ʱ��(1������)

	private long totalDuration;// �ܹ�����ʱ��

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
	 * ��ͷ��ʼ�����������
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
