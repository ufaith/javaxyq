package com.javaxyq.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

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

	public Animation(Animation anim) {
		totalDuration = anim.totalDuration;
		frames = anim.frames;
		currFrame = anim.currFrame;
		frameCount = frames.size();
	}

	public synchronized void addFrame(Frame frame) {
		frames.add(frame);
		totalDuration += frame.getEndTime();
		currFrame = frame;
		frameCount = frames.size();
	}

	public synchronized void addFrame(BufferedImage image, long duration, int centerX, int centerY) {
		totalDuration += duration;
		Frame frame = new Frame(image, totalDuration, centerX, centerY);
		frames.add(frame);
		currFrame = frame;
		frameCount = frames.size();
	}

	/**
	 * �������ŵ�ʱ�����ĿǰӦ����ʾ��һ֡����
	 * 
	 * @param elapsedTime
	 * @return ���ش˴�������֡��
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
	 * �ӵ�0֡��ʼ���㣬���µ�elapsedTimeʱ����֡
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

	public synchronized Image getImage() {
		return (currFrame == null) ? null : currFrame.getImage();
	}

	/**
	 * ��ͷ��ʼ�����������
	 */
	public synchronized void reset() {
		animTime = 0;
		index = 0;
		currFrame = frames.size() > 0 ? frames.get(0) : null;
	}

	public synchronized int getWidth() {
		return (currFrame == null) ? 0 : currFrame.getWidth();
	}

	public synchronized int getHeight() {
		return (currFrame == null) ? 0 : currFrame.getHeight();
	}

	public synchronized int getCenterX() {
		return (currFrame == null) ? 0 : currFrame.getCenterX();
	}

	public synchronized int getCenterY() {
		return (currFrame == null) ? 0 : currFrame.getCenterY();
	}

	public int getRepeat() {
		return repeat;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		this.currFrame = frames.get(index);
	}

	public long getTotalDuration() {
		return totalDuration;
	}

	public Animation clone() {
		return new Animation(this);
	}

	public Frame getCurrFrame() {
		return currFrame;
	}

	public void setCurrFrame(Frame currFrame) {
		this.currFrame = currFrame;
	}

	public long getAnimTime() {
		return animTime;
	}

	public void setAnimTime(long animTime) {
		this.animTime = animTime;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	@Override
	public void dispose() {
		for (Frame f : this.frames) {
			f.dispose();
		}
		this.frames.clear();
	}

	@Override
	protected void doDraw(Graphics2D g, int x, int y, int width, int height) {
		this.currFrame.draw(g, x, y, width, height);
	}

	public boolean contains(int x, int y) {
		return this.currFrame.contains(x, y);
	}

	/**
	 * �ȴ��������Ž���
	 */
	public void waitFor() {
		synchronized (UPDATE_LOCK) {
			while (true) {
				if(repeat==0 && index == frameCount-1 || repeat ==-1) {
					break;
				}
				try {
					UPDATE_LOCK.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
