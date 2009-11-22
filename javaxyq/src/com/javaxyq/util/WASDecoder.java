package com.javaxyq.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import com.javaxyq.core.Toolkit;
import com.javaxyq.io.RandomAcessInputStream;

/**
 * was(tcp/tca)解码器
 * 
 * @author 龚德伟
 * @date
 */
public class WASDecoder {

	static final int TYPE_ALPHA = 0x00;// 前2位

	static final int TYPE_ALPHA_PIXEL = 0x20;// 前3位 0010 0000

	static final int TYPE_ALPHA_REPEAT = 0x00;// 前3位

	static final int TYPE_FLAG = 0xC0;// 2进制前2位 1100 0000

	static final int TYPE_PIXELS = 0x40;// 以下前2位 0100 0000

	static final int TYPE_REPEAT = 0x80;// 1000 0000

	static final int TYPE_SKIP = 0xC0; // 1100 0000

	static final String WAS_FILE_TAG = "SP";

	static final int WAS_IMAGE_HEADER_SIZE = 12;

	// 中心点(x,y)
	private int centerX;

	private int centerY;

	private int frameCount;

	private int height;

	private int headerSize;

	private short[] originPalette;

	private short[] palette;

	private int spriteCount;

	// size
	private int width;

	private int[] schemeIndexs;

	private Section[] sections;

	private List<WASFrame> frames;

	private RandomAcessInputStream randomIn;

	public WASDecoder() {
		palette = new short[256];
		originPalette = new short[256];
		frames = new ArrayList<WASFrame>();
	}

	/**
	 * 设置着色方案
	 */
	public void coloration(int[] schemeIndexs) {
		for (int i = 0; i < schemeIndexs.length; i++) {
			this.coloration(i, schemeIndexs[i]);
		}
		this.schemeIndexs = schemeIndexs;
	}

	public void loadColorationProfile(String filename) {
		InputStream is = Toolkit.getInputStream(filename);
		if (is != null) {
			Scanner scanner = new Scanner(is);
			scanner.useDelimiter("(\r\n)|(\n\r)|[\n\r=]");
			// section
			String strLine = scanner.next();
			String[] values = strLine.split(" ");// StringUtils.split(strLine);
			int sectionCount = Integer.parseInt(values[0]);
			// section 区间
			int[] sectionBounds = new int[sectionCount + 1];
			for (int i = 0; i < sectionBounds.length; i++) {
				sectionBounds[i] = Integer.parseInt(values[i + 1]);
			}
			// create section
			Section[] sections = new Section[sectionCount];
			for (int i = 0; i < sections.length; i++) {
				Section section = new Section(sectionBounds[i], sectionBounds[i + 1]);
				int schemeCount = Integer.parseInt(scanner.next());
				for (int s = 0; s < schemeCount; s++) {
					String[] strSchemes = new String[3];
					strSchemes[0] = scanner.next();
					strSchemes[1] = scanner.next();
					strSchemes[2] = scanner.next();
					ColorationScheme scheme = new ColorationScheme(strSchemes);
					section.addScheme(scheme);
				}

				sections[i] = section;
			}
			setSections(sections);
		}
	}

	public int[] getSchemeIndexs() {
		return schemeIndexs;
	}

	public Section[] getSections() {
		return sections;
	}

	public void setSections(Section[] sections) {
		this.sections = sections;
	}

	public int getSectionCount() {
		return this.sections.length;
	}

	public int getSchemeCount(int section) {
		return this.sections[section].getSchemeCount();
	}

	public short[] getOriginPalette() {
		return originPalette;
	}

	/**
	 * 修改某个区段的着色
	 * 
	 * @param sectionIndex
	 * @param schemeIndex
	 */
	public void coloration(int sectionIndex, int schemeIndex) {
		if (this.sections == null) {
			return;
		}
		Section section = this.sections[sectionIndex];
		ColorationScheme scheme = section.getScheme(schemeIndex);
		for (int i = section.getStart(); i < section.getEnd(); i++) {
			this.palette[i] = scheme.mix(this.originPalette[i]);
		}
	}

	// public void setColorSections(Section[] sections) {
	// this.sections = sections;
	// }

	/**
	 * 将图像数据画到Image上
	 */
	public void draw(int[][] pixels, WritableRaster raster, int x, int y, int w, int h) {
		int[] iArray = new int[4];
		for (int y1 = 0; y1 < h && y1 + y < height; y1++) {
			for (int x1 = 0; x1 < w && x1 + x < width; x1++) {
				// red
				iArray[0] = ((pixels[y1][x1] >>> 11) & 0x1F) << 3;
				// green
				iArray[1] = ((pixels[y1][x1] >>> 5) & 0x3f) << 2;
				// blue
				iArray[2] = (pixels[y1][x1] & 0x1F) << 3;
				// alpha
				iArray[3] = ((pixels[y1][x1] >>> 16) & 0x1f) << 3;
				//iArray[3]+= 7;
				try {
					raster.setPixel(x1 + x, y1 + y, iArray);
				} catch (Exception e) {
					System.out.printf("%s: x=%s,y=%s,pixel=[%s,%s,%s,%s]\n", e.getMessage(), x1 + x, y1 + y, iArray[0],
							iArray[1], iArray[2], iArray[3]);
					// e.printStackTrace();
				}
			}
		}
	}

	public short[] getPalette() {
		return palette;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public BufferedImage getFrame(int index) {
		WASFrame frame = this.frames.get(index);
		try {
			if (frame.getPixels() == null) {
				int[][] pixels = parse(frame);
				frame.setPixels(pixels);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (this.frameCount == 1) {//修正单帧动画的偏移问题
			return createImage(centerX, centerY, frame.getWidth(), frame.getHeight(), frame.getPixels());
		} else {
			return createImage(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), frame.getPixels());
		}
	}

	/**
	 * parse frame every time
	 * 
	 * @param index
	 * @return
	 */
	public BufferedImage getFrameImage(int index) {
		WASFrame frame = this.frames.get(index);
		try {
			int[][] pixels = parse(frame);
			if (this.frameCount == 1) {
				return createImage(centerX, centerY, frame.getWidth(), frame.getHeight(), pixels);
			} else {
				return createImage(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), pixels);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public BufferedImage getFullFrame(int index) {
		WASFrame frame = this.frames.get(index);
		try {
			if (frame.getPixels() == null) {
				int[][] pixels = parse(frame);
				frame.setPixels(pixels);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return createImage(centerX, centerY, frame.getWidth(), frame.getHeight(), frame.getPixels());
	}

	private int[][] parse(WASFrame frame) throws IOException {
		return this
				.parse(randomIn, frame.getFrameOffset(), frame.getLineOffsets(), frame.getWidth(), frame.getHeight());
	}

	public Vector<Image> getFrames() {
		return null;
	}

	public int getDelay(int index) {
		return this.frames.get(index).getDelay();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getSpriteCount() {
		return spriteCount;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void load(InputStream in) throws IllegalStateException, IOException {
		randomIn = prepareInputStream(in);

		// was 信息
		headerSize = randomIn.readUnsignedShort();
		spriteCount = randomIn.readUnsignedShort();
		frameCount = randomIn.readUnsignedShort();
		width = randomIn.readUnsignedShort();
		height = randomIn.readUnsignedShort();
		centerX = randomIn.readUnsignedShort();
		centerY = randomIn.readUnsignedShort();

		// 读取帧延时信息
		int len = headerSize - WAS_IMAGE_HEADER_SIZE;
		if (len < 0) {
			throw new IllegalStateException("帧延时信息错误: " + len);
		}
		int[] delays = new int[len];
		for (int i = 0; i < len; i++) {
			delays[i] = randomIn.read();
		}

		// 读取调色板
		randomIn.seek(headerSize + 4);
		for (int i = 0; i < 256; i++) {
			originPalette[i] = randomIn.readUnsignedShort();
		}
		// 复制调色板
		System.arraycopy(originPalette, 0, palette, 0, 256);

		// 帧偏移列表
		int[] frameOffsets = new int[spriteCount * frameCount];
		randomIn.seek(headerSize + 4 + 512);
		for (int i = 0; i < spriteCount; i++) {
			for (int n = 0; n < frameCount; n++) {
				frameOffsets[i * frameCount + n] = randomIn.readInt();
			}
		}
		// 帧信息
		int frameX, frameY, frameWidth, frameHeight;
		for (int i = 0; i < spriteCount; i++) {
			for (int n = 0; n < frameCount; n++) {
				int offset = frameOffsets[i * frameCount + n];
				if (offset == 0)
					continue;// blank frame
				randomIn.seek(offset + headerSize + 4);
				frameX = randomIn.readInt();
				frameY = randomIn.readInt();
				frameWidth = randomIn.readInt();
				frameHeight = randomIn.readInt();
				// 行像素数据偏移
				int[] lineOffsets = new int[frameHeight];
				for (int l = 0; l < frameHeight; l++) {
					lineOffsets[l] = randomIn.readInt();
				}
				// 创建帧对象
				int delay = 1;
				if (i < delays.length) {
					delay = delays[i];
				}
				WASFrame frame = new WASFrame(frameX, frameY, frameWidth, frameHeight, delay, offset, lineOffsets);
				this.frames.add(frame);

				// 解析帧数据
				// int[][] pixels = parse(randomIn, offset, lineOffsets,
				// frameWidth, frameHeight);
				// createImage(frameX, frameY, frameWidth, frameHeight, pixels);
			}
		}
	}

	public BufferedImage createImage(int x, int y, int frameWidth, int frameHeight, int[][] pixels) {
		// use sprite's width & height
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		draw(pixels, image.getRaster(), centerX - x, centerY - y, frameWidth, frameHeight);
		return image;
	}

	private RandomAcessInputStream prepareInputStream(InputStream in) throws IOException, IllegalStateException {
		byte[] buf;
		RandomAcessInputStream randomIn;
		buf = new byte[2];
		in.mark(10);
		in.read(buf, 0, 2);
		String flag = new String(buf, 0, 2);
		if (!WAS_FILE_TAG.equals(flag)) {
			throw new IllegalStateException("文件头标志错误:" + print(buf));
		}
		if (in instanceof RandomAcessInputStream) {
			in.reset();
			randomIn = (RandomAcessInputStream) in;
		} else {
			byte[] buf2 = new byte[in.available() + buf.length];
			System.arraycopy(buf, 0, buf2, 0, buf.length);
			int a = 0, count = buf.length;
			while (in.available() > 0) {
				a = in.read(buf2, count, in.available());
				count += a;
			}
			// construct a new seekable stream
			randomIn = new RandomAcessInputStream(buf2);
		}
		// skip header
		randomIn.seek(2);
		return randomIn;
	}

	private String print(byte[] buf) {
		String output = "[";
		for (byte b : buf) {
			output += b;
			output += ",";
		}
		output += "]";
		return output;
	}

	public void load(String filename) throws Exception {
		// InputStream fileIn = getClass().getResourceAsStream(filename);
		// File file = new File(filename);
		// InputStream fileIn = new FileInputStream(file);
		load(Toolkit.getInputStream(filename));
	}

	private int[][] parse(RandomAcessInputStream in, int frameOffset, int[] lineOffsets, int frameWidth, int frameHeight)
			throws IOException {
		int[][] pixels = new int[frameHeight][frameWidth];
		int b,x,c;
		int index;
		int count;
		for (int y = 0; y < frameHeight; y++) {
			x = 0;
			in.seek(lineOffsets[y] + frameOffset + headerSize + 4);
			while (x < frameWidth) {
				b = in.read();
				switch ((b & TYPE_FLAG)) {
				case TYPE_ALPHA:
					if ((b & TYPE_ALPHA_PIXEL) > 0) {
						index = in.read();
						c = palette[index];
						// palette[index]=0;

						pixels[y][x++] = c + ((b & 0x1F) << 16);
					} else if (b != 0) {// ???
						count = b & 0x1F;// count
						b = in.read();// alpha
						index = in.read();
						c = palette[index];
						// palette[index]=0;

						for (int i = 0; i < count; i++) {
							pixels[y][x++] = c + ((b & 0x1F) << 16);
						}
					} else {// block end
						if (x > frameWidth) {
							System.err.println("block end error: [" + y + "][" + x + "/" + frameWidth + "]");
							continue;
						} else if (x == 0) {
							// System.err.println("x==0");
						} else {
							x = frameWidth;// set the x value to break the
							// 'while' sentences
						}
					}
					break;
				case TYPE_PIXELS:
					count = b & 0x3F;
					for (int i = 0; i < count; i++) {
						index = in.read();
						pixels[y][x++] = palette[index] + (0x1F << 16);
						// palette[index]=0;

					}
					break;
				case TYPE_REPEAT:
					count = b & 0x3F;
					index = in.read();
					c = palette[index];
					// palette[index]=0;

					for (int i = 0; i < count; i++) {
						pixels[y][x++] = c + (0x1F << 16);
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
		return pixels;
	}

	public void resetPalette() {
		System.arraycopy(originPalette, 0, palette, 0, 256);
	}

}
