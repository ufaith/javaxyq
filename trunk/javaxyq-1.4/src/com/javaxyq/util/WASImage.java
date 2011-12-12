package com.javaxyq.util;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.javaxyq.core.GameMain;
import com.javaxyq.io.RandomAcessInputStream;

public class WASImage {
	private RandomAcessInputStream in;

	private static final int TYPE_ALPHA = 0x00;// ǰ2λ

	private static final int TYPE_ALPHA_PIXEL = 0x20;// ǰ3λ 0010 0000

	private static final int TYPE_ALPHA_REPEAT = 0x00;// ǰ3λ

	private static final int TYPE_FLAG = 0xC0;// 2����ǰ2λ 1100 0000

	private static final int TYPE_PIXELS = 0x40;// ����ǰ2λ 0100 0000

	private static final int TYPE_REPEAT = 0x80;// 1000 0000

	private static final int TYPE_SKIP = 0xC0; // 1100 0000

	private static final int WAS_IMAGE_HEADER_SIZE = 12;

	private static final String WAS_FILE_TAG = "SP";

	public int centerX;

	public int centerY;

	int[] delayLine;

	public int frameCount;

	public WASFrame[][] frames;

	public int height;

	short imageHeaderSize;

	String name;

	short[] palette;

	String path;

	public int spriteCount;

	public int width;

	public WASImage() {
		palette = new short[256];
	}

	public String getName() {
		return name;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	/**
	 * ��ȡĳ֡����ʱ֡��
	 */
	public int getFrameDelay(int index) {
		return delayLine[index];
	}

	/**
	 * �ļ���־ 2�ֽڣ� SP 53 50 / SP<br>
	 * �ļ�ͷ��С 2�ֽڣ� ������ǰ�ĸ��ֽ� 0C 00 / 12<br>
	 * ���������� 2�ֽ� 01 00 / 1<br>
	 * ÿ�����֡�� 2�ֽ� 01 00 / 1<br>
	 * �����Ŀ�� 2�ֽ� 80 02 / 640<br>
	 * �����ĸ߶� 2�ֽ� 29 00 / 41<br>
	 * ���������ĵ� X 2�ֽڣ� �з��� 00 00 / 0<br>
	 * ���������ĵ� Y 2�ֽ�, �з��� 00 00 / 0<br>
	 * 
	 * @param filename
	 */
	public synchronized void loadWAS(String filename) {
		//System.out.print("loading WAS file: " + filename + " ...");
		//long startTime = System.currentTimeMillis();
		InputStream fileIn = null;
		try {
			File file = new File(filename);
			path = file.getAbsolutePath();
			name = file.getName();
			fileIn = GameMain.class.getResourceAsStream(filename);
			byte[] buf = new byte[fileIn.available()];
			int a = 0, count = 0;
			while (fileIn.available() > 0) {
				a = fileIn.read(buf, count, fileIn.available());
				count += a;
			}
			
			// construct a new seekable stream
			in = new RandomAcessInputStream(buf);
			buf = new byte[2];
			in.read(buf, 0, 2);
			String flag = new String(buf, 0, 2);
			if (!WAS_FILE_TAG.equals(flag)) { throw new Exception("�ļ�ͷ��־����:" + flag); }
			
			// was ��Ϣ
			imageHeaderSize = readUnsignedShort();
			spriteCount = readUnsignedShort();
			frameCount = readUnsignedShort();
			width = readUnsignedShort();
			height = readUnsignedShort();
			centerX = readUnsignedShort();
			centerY = readUnsignedShort();

			// ��ȡ֡��ʱ��Ϣ
			int len = imageHeaderSize - WAS_IMAGE_HEADER_SIZE;
			if (len < 0) {
				throw new Exception("֡��ʱ��Ϣ����");
			} else if (len > 0) {
				delayLine = new int[len];
				for (int i = 0; i < delayLine.length; i++) {
					delayLine[i] = in.read();
				}
			} else
				delayLine = null;

			// ��ȡ��ɫ��
			in.seek(imageHeaderSize + 4);
			for (int i = 0; i < 256; i++) {
				palette[i] = readUnsignedShort();
			}
			frames = new WASFrame[spriteCount][];
			in.seek(imageHeaderSize + 4 + 512);
			for (int i = 0; i < spriteCount; i++) {
				frames[i] = new WASFrame[frameCount];
				for (int n = 0; n < frameCount; n++) {// ֡ƫ���б�
					WASFrame frame = new WASFrame();
					frames[i][n] = frame;
					if (delayLine != null && n < delayLine.length) {
						frames[i][n].setDelay(delayLine[n]);
					}
					frame.setFrameOffset(readInt());
				}
			}
			for (int i = 0; i < spriteCount; i++) {// ֡��Ϣ
				for (int n = 0; n < frameCount; n++) {
					WASFrame frame = frames[i][n];
					int offset = frame.getFrameOffset();
					if (offset == 0)
						continue;// blank frame
					in.seek(offset + imageHeaderSize + 4);
					frame.setX(readInt());
					frame.setY(readInt());
					frame.setWidth(readInt());
					frame.setHeight(readInt());
					frame.setLineOffsets(new int[frame.getHeight()]);
					for (int l = 0; l < frame.getHeight(); l++) {// ����������ƫ��
						frame.getLineOffsets()[l] = readInt();
					}
					parse(frame);// ����֡����
				}
			}
		} catch (Exception e) {
			System.err.println("load was file failed!");
			e.printStackTrace();
		} finally {
			if (in != null)
				in.close();
			try {
				if (fileIn != null)
					fileIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println((System.currentTimeMillis() - startTime) + "ms");
		}
	}

	/**
	 * ��ͼƬһ��RLE�����ʽ�����ݽ���,���������ݷŵ�pixels��<br>
	 * ��ʽ:��16λΪ[565]rgb��ɫֵ,16-20λΪalphaֵ(���Ϊ0x1F);
	 * 
	 * @throws IOException
	 */
	public void parse(WASFrame frame) throws IOException {
		int frameWidth = frame.getWidth();
		int frameHeight = frame.getHeight();
		int[] pixels = new int[frameHeight *frameWidth];
		frame.setPixels(pixels);
		int b;
		int x;
		int c;
		int count;
		for (int y = 0; y < frameHeight; y++) {
			x = 0;
			in.seek(frame.getLineOffsets()[y] + frame.getFrameOffset() + imageHeaderSize + 4);
			while (x < frameWidth) {
				b = in.read();
				switch ((b & TYPE_FLAG)) {
				case TYPE_ALPHA:
					if ((b & TYPE_ALPHA_PIXEL) > 0) {
						c = palette[in.read()];
						pixels[y*width +x++] = c + ((b & 0x1F) << 16);
					} else if (b != 0) {// ???
						count = b & 0x1F;// count
						b = in.read();// alpha
						c = palette[in.read()];
						for (int i = 0; i < count; i++) {
							pixels[y*width +x++] = c + ((b & 0x1F) << 16);
						}
					} else {// block end
						if (x > frameWidth) {
							System.err.println("block end error: [" + y + "][" + x + "/" + frameWidth + "]");
							continue;
						} else if (x == 0) {
							// System.err.println("x==0");
						} else {
							x = frameWidth;// set the x value to break the 'while' sentences
						}
					}
					break;
				case TYPE_PIXELS:
					count = b & 0x3F;
					for (int i = 0; i < count; i++) {
						pixels[y*width +x++] = palette[in.read()] + (0x1F << 16);
					}
					break;
				case TYPE_REPEAT:
					count = b & 0x3F;
					c = palette[in.read()];
					for (int i = 0; i < count; i++) {
						pixels[y*width +x++] = c + (0x1F << 16);
					}
					break;
				case TYPE_SKIP:
					count = b & 0x3F;
					x += count;
					break;
				}
			}
			if (x > frameWidth)
				System.err.println("block end error: [" + y + "][" + x + "/" + frameWidth + "]");
		}
	}

	private int readInt() throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		return (ch1 + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
	}

	private short readUnsignedShort() throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		return (short) ((ch2 << 8) + ch1);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("�ļ�·��:\t");
		buf.append(path);
		buf.append("\r\n");
		buf.append("�ļ���־:\t");
		buf.append(WAS_FILE_TAG);
		buf.append("\r\n");
		buf.append("�ļ�ͷ��С:\t");
		buf.append(imageHeaderSize);
		buf.append("\r\n");
		buf.append("����������:\t");
		buf.append(spriteCount);
		buf.append("\r\n");
		buf.append("����֡��:\t");
		buf.append(frameCount);
		buf.append("\r\n");
		buf.append("�����Ŀ��:\t");
		buf.append(width);
		buf.append("\r\n");
		buf.append("�����ĸ߶�:\t");
		buf.append(height);
		buf.append("\r\n");
		buf.append("����X���ĵ�:\t");
		buf.append(centerX);
		buf.append("\r\n");
		buf.append("����Y���ĵ�:\t");
		buf.append(centerY);
		buf.append("\r\n");
		return buf.toString();
	}
}

// ��������ͼƬ��������Ϣ����ʽ���£�
// ������=����(8����)+����
//
// ���͵ĸ�ʽ���£�
// ������4�֣���2���ر�ʾ��
//
// 00����ʾalpha���أ�ʣ�µ�6����Ҳ��0ʱ�����ݶν�����
// �����3��������1ʱ��ʣ�µ�5����(0~31)Ϊalpha�㡣�����Ժ���ֽ�������������
// ����ʣ�µ�5����(0~31)��alpha�����ظ�����.�����Ժ��2�ֽ���alpha,����������
// 01����ʾ�����飬ʣ�µ�6����(0~63)Ϊ���ݶεĳ��ȡ�
// 10����ʾ�ظ����� n �Σ�n Ϊʣ�µ�6����(0~63)��ʾ��
// 11����ʾ�������� n ����n Ϊʣ�µ�6����(0~63)��ʾ��
