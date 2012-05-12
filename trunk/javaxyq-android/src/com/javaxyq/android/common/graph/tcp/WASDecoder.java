package com.javaxyq.android.common.graph.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.javaxyq.android.common.io.RandomAcessInputStream;

/**
 * was(tcp/tca)解码器
 * @author chenyang
 *
 */
public class WASDecoder {
	
	static final int TYPE_ALPHA = 0x00;// 前2位

	static final int TYPE_ALPHA_PIXEL = 0x20;// 前3位 0010 0000

	static final int TYPE_ALPHA_REPEAT = 0x00;// 前3位

	static final int TYPE_FLAG = 0xC0;// 2进制前2位 1100 0000

	static final int TYPE_PIXELS = 0x40;// 以下前2位 0100 0000

	static final int TYPE_REPEAT = 0x80;// 1000 0000

	static final int TYPE_SKIP = 0xC0; // 1100 0000
	
	/** WAS文件头标识 */
	private static final String WAS_FILE_TAG = "SP";
	
	/** TCP文件头大小 */
	static final int TCP_HEADER_SIZE = 12;
	
	/** 角色文件读取流 */
	private RandomAcessInputStream randomIn;
	
	/** 文件头大小 */
	private int headerSize;
	
	/** 包含的动画个数 */
	private int animCount;
	
	/** 动画的帧数 */
	private int frameCount;
	
	/** 精灵宽度 */
	private int width;
	
	/** 精灵高度 */
	private int height;
	
	private int[] schemeIndexs;
	
	/** 悬挂点x坐标 */
	private int refPixelX;
	
	/** 悬挂点x坐标 */
	private int refPixelY;
	
	/** 原始调色板 */
	private short[] originPalette;
	
	/** 当前调色板 */
	private short[] palette;
	
	private Section[] sections;
	
	/** 帧集合 */
	private List<WASFrame> frames;
	
	public Section[] getSections() {
		return sections;
	}

	public void setSchemeIndexs(int[] schemeIndexs) {
		this.schemeIndexs = schemeIndexs;
	}

	public void setSections(Section[] sections) {
		this.sections = sections;
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public RandomAcessInputStream getRandomIn() {
		return randomIn;
	}

	public void setRandomIn(RandomAcessInputStream randomIn) {
		this.randomIn = randomIn;
	}

	public int getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}

	public int getAnimCount() {
		return animCount;
	}

	public void setAnimCount(int animCount) {
		this.animCount = animCount;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	public short[] getOriginPalette() {
		return originPalette;
	}

	public void setOriginPalette(short[] originPalette) {
		this.originPalette = originPalette;
	}

	public short[] getPalette() {
		return palette;
	}

	public void setPalette(short[] palette) {
		this.palette = palette;
	}

	public List<WASFrame> getFrames() {
		return frames;
	}

	public void setFrames(List<WASFrame> frames) {
		this.frames = frames;
	}

	public int[] getSchemeIndexs() {
		return schemeIndexs;
	}
	
	public int getDelay(int index) {
		return this.frames.get(index).getDelay();
	}

	public WASDecoder() {
		palette = new short[256];
		originPalette = new short[256];
		frames = new ArrayList<WASFrame>();
	}
	
	private int[] parse(WASFrame frame) throws IOException {
		return this.parse(randomIn, frame.getFrameOffset(), frame.getLineOffsets(), frame.getWidth(), frame.getHeight());
	}
	
	private int[] parse(RandomAcessInputStream in, int frameOffset, int[] lineOffsets, int frameWidth, int frameHeight)
			throws IOException {
		int[] pixels = new int[frameHeight*frameWidth];
		int b, x, c;
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

						pixels[y*frameWidth+ x++] = c + ((b & 0x1F) << 16);
					} else if (b != 0) {// ???
						count = b & 0x1F;// count
						b = in.read();// alpha
						index = in.read();
						c = palette[index];
						// palette[index]=0;

						for (int i = 0; i < count; i++) {
							pixels[y*frameWidth+ x++] = c + ((b & 0x1F) << 16);
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
						pixels[y*frameWidth+ x++] = palette[index] + (0x1F << 16);
						// palette[index]=0;

					}
					break;
				case TYPE_REPEAT:
					count = b & 0x3F;
					index = in.read();
					c = palette[index];
					// palette[index]=0;

					for (int i = 0; i < count; i++) {
						pixels[y*frameWidth+ x++] = c + (0x1F << 16);
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
	/**
	 * 准备文件写入流
	 * @param in 写入流
	 * @return 可跳到制定位置的字节流
	 * @throws IOException
	 */
	private RandomAcessInputStream prepareInputStream(InputStream in) throws IOException {
		
		byte[] buf;
		RandomAcessInputStream randomIn;
		buf = new byte[2];
		in.mark(10);
		in.read(buf,0,2);
		String flag = new String(buf,0,2);
		System.out.println("filehead:"+flag);
		if(!WASDecoder.WAS_FILE_TAG.equals(flag)) {
			throw new IllegalStateException("文件头标志错误：" + print(buf));
		}
		
		if(in instanceof RandomAcessInputStream) {
			in.reset();
			randomIn = (RandomAcessInputStream) in;
		} else {
			byte[] buf2 = new byte[in.available() + buf.length];
			System.arraycopy(buf, 0, buf2, 0, buf.length);
			int begin = buf.length ; 
			in.read(buf2, begin, in.available());
			randomIn = new RandomAcessInputStream(buf2);
		}
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
	
	public void load(InputStream in) throws IOException {
		randomIn = prepareInputStream(in);
		
		// was 信息
		headerSize = randomIn.readUnsignedShort();
		animCount = randomIn.readUnsignedShort();
		frameCount = randomIn.readUnsignedShort();
		width = randomIn.readUnsignedShort();
		height = randomIn.readUnsignedShort();
		refPixelX = randomIn.readUnsignedShort();
		refPixelY = randomIn.readUnsignedShort();
		
		//读取帧延时信息
		int len = headerSize - WASDecoder.TCP_HEADER_SIZE;
		if(len < 0) {
			throw new IllegalStateException("帧延时信息错误：" + len);
		}
		int[] delays = new int[len];
		for( int i = 0 ; i < len ; i ++ ) {
			delays[i] = randomIn.read();
		}
		
		// 读取调色板
		randomIn.seek(headerSize + 4);
		for ( int i = 0 ; i < 256 ; i ++ ) {
			originPalette[i] = randomIn.readUnsignedShort();
		}
		
		//复制调色板
		System.arraycopy(originPalette, 0, palette, 0, 256);
		
		// 帧偏移列表
		int[] frameOffsets = new int[animCount * frameCount];
		randomIn.seek(headerSize + 4 + 512);
		for(int i = 0 ; i <animCount ; i ++) {
			for(int j = 0 ; j < frameCount ; j ++) {
				frameOffsets[i * frameCount + j] = randomIn.readInt();
			}
		}
		
		// 帧信息
		int frameX, frameY, frameWidth, frameHeight;
		for(int i = 0 ; i < animCount ; i ++) {
			for( int j = 0 ; j < frameCount ; j ++) {
				int offset = frameOffsets[i * frameCount + j];
				if(0 == offset) {
					continue;//blank frame
				}
				randomIn.seek(offset + headerSize + 4);
				frameX = randomIn.readInt();
				frameY = randomIn.readInt();
				frameWidth = randomIn.readInt();
				frameHeight = randomIn.readInt();
				// 行像素数据偏移
				int[] lineOffsets = new int[frameHeight];
				for(int l = 0 ; l < frameHeight ; l ++) {
					lineOffsets[l] = randomIn.readInt();
				}
				
				// 创建帧对象
				int delay = 1;
				if( i < delays.length) {
					delay = delays[i];
				}
				
				WASFrame frame = new WASFrame(frameX, frameY, frameWidth, frameHeight, delay, offset, lineOffsets);
				this.frames.add(frame);
			}
		}
	}
	
	public void loadColorationProfile(Context context,String fileName) throws IOException {
		AssetManager assetManager = context.getAssets();
		InputStream is = assetManager.open(fileName);
		Scanner scanner = new Scanner(is);
		scanner.useDelimiter("(\r\n)|(\n\r)|[\n\r=]");
		//section
		String strLine = scanner.next();
		String[] values = strLine.split(" ");
		int sectionCount = Integer.parseInt(values[0]);
		//section 区间
		int[] sectionBounds = new int[sectionCount + 1];
		for(int i = 0 ; i < sectionBounds.length; i ++) {
			sectionBounds[i] = Integer.parseInt(values[i + 1]);
		}
		//create section
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
	
	/**
	 * 设置着色方案
	 */
	public void coloration(int[] schemeIndexs) {
		for (int i = 0; i < schemeIndexs.length; i++) {
			this.coloration(i, schemeIndexs[i]);
		}
		this.schemeIndexs = schemeIndexs;
	}
	
	public Bitmap getFrame(int index) {
		WASFrame frame = this.frames.get(index);
		try{
			if(frame.getPixels() == null) {
				frame.setPixels(parse(frame));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if(this.frameCount == 1) {//修正单帧动画的偏移问题
			return createImage(refPixelX, refPixelY, frame.getWidth(), frame.getHeight(), frame.getPixels());
		} else {
			return createImage(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), frame.getPixels());
		}
 	}
	
	public Bitmap createImage(int x, int y, int frameWidth, int frameHeight, int[] pixels) {
		Bitmap image = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
		this.draw(pixels, image,refPixelX - x,refPixelY - y, frameWidth, frameHeight);
		return image;
		
	}
	
	public void draw(int[] pixels,Bitmap bitmap,int x, int y ,int w, int h) {
		int[] iArray = new int[4];
		for(int y1 = 0 ; y1 < h && y1 + y < height ; y1 ++) {
			for(int x1 = 0 ; x1 < w && x1 + x < width ; x1 ++) {
				//red 5
				iArray[0] = ((pixels[y1*w + x1] >>> 11) & 0x1F) << 3;
				//green 6
				iArray[1] = ((pixels[y1*w +x1] >>> 5) & 0x3f) << 2;
				// blue 5
				iArray[2] = (pixels[y1*w +x1] & 0x1F) << 3;
				// alpha 5
				iArray[3] = ((pixels[y1*w +x1] >>> 16) & 0x1f) << 3;
				try {
					int color = Color.argb(iArray[3], iArray[0], iArray[1], iArray[2]);
					bitmap.setPixel(x + x1, y + y1, color);
//					bitmap.setPixels(iArray, 0, 4, x1 + x, y1 + y, 4, 1);
				} catch (Exception e) {
					System.out.printf("%s: x=%s,y=%s,pixel=[%s,%s,%s,%s]\n", e.getMessage(), x1 + x, y1 + y, iArray[0],
							iArray[1], iArray[2], iArray[3]);
				}
			}
		}
	}
 
}
