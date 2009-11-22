//TODO ����256����ɫ??

package com.javaxyq.util;

import java.awt.Image;
import java.io.OutputStream;
import java.util.Vector;

/**
 * ��Ϸ��Դ�ļ�-WAS��ʽ������
 * 
 * @author Langlauf
 * @date
 */
public class WASEncoder {
	short imageHeaderSize;

	short[] palette;

	// size
	int width;

	int height;

	// ���ĵ�(x,y)
	int centerX;

	int centerY;

	int frameCount;

	int spriteCount;

	Vector<Image> images;

	/** ֡��ʱ */
	Vector<Integer> delays;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param spriteCount
	 *            ���鶯������
	 * @param frameCount
	 *            ÿ�����鶯��֡��
	 */
	public WASEncoder(int x, int y, int width, int height, int spriteCount, int frameCount) {
		this.centerX = x;
		this.centerY = y;
		this.width = width;
		this.height = height;
		this.spriteCount = spriteCount;
		this.frameCount = frameCount;
	}

	/**
	 * �������Ϊ1,����֡����ȷ��
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public WASEncoder(int x, int y, int width, int height) {
		this(x, y, width, height, 1, -1);
	}

	/**
	 * �������Ϊ1,����֡����ȷ��<br>
	 * x,yΪ0
	 * 
	 * @param width
	 * @param height
	 */
	public WASEncoder(int width, int height) {
		this(0, 0, width, height, 1, -1);
	}

	public void addFrame(Image image) {
		addFrame(image, 1);
	}

	public void addFrames(java.util.List<Image> images) {
		for (Image image : images) {
			addFrame(image, 1);
		}
	}

	public void addFrame(Image image, int delay) {
		images.add(image);
		delays.add(delay);
	}

	/**
	 * ��ͼ����������ָ������
	 * 
	 * @param out
	 */
	public void encode(OutputStream out) {

	}
}
