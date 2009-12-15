/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-5
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.graph;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 * @author dewitt
 * @date 2009-12-5 create
 */
public class TCPImage extends Image {

	static final int TYPE_ALPHA = 0x00;// ǰ2λ

	static final int TYPE_ALPHA_PIXEL = 0x20;// ǰ3λ 0010 0000

	static final int TYPE_ALPHA_REPEAT = 0x00;// ǰ3λ

	static final int TYPE_FLAG = 0xC0;// 2����ǰ2λ 1100 0000

	static final int TYPE_PIXELS = 0x40;// ����ǰ2λ 0100 0000

	static final int TYPE_REPEAT = 0x80;// 1000 0000

	static final int TYPE_SKIP = 0xC0; // 1100 0000

	/** �ļ�ͷ��� */
	static final String WAS_FILE_TAG = "SP";

	static final int TCP_HEADER_SIZE = 12;

	/** �ļ��� */
	private String filename;
	
	/** Reference Pixel X (���ҵ�X)	 */
	private int refPixelX;

	/** Reference Pixel Y (���ҵ�Y) */
	private int refPixelY;

	/** ������������ */
	private int animCount;

	/** ������֡�� */
	private int frameCount;

	/** �ļ�ͷ��С */
	private int headerSize;

	/** ԭʼ��ɫ�� */
	private short[] originPalette;

	/** ��ǰ��ɫ�� */
	private short[] palette;

	/** ������ */
	private int width;
	/** ����߶� */
	private int height;

	private TCPImageSource source;

	/**
	 * ����һ��TCPͼƬ
	 * @param filename tcp�ļ���
	 * @param index ����������
	 */
	public TCPImage(String filename,int index) {
		this.filename = filename;
	}

	public Graphics getGraphics() {
		return null;
	}

	public int getHeight(ImageObserver observer) {
		return height;
	}

	public Object getProperty(String name, ImageObserver observer) {
		return null;
	}

	public ImageProducer getSource() {
//		if (source == null) {
//			source = new TCPImageSource(filename);
//		}
		return source;
	}

	public int getWidth(ImageObserver observer) {
		return width;
	}

}
